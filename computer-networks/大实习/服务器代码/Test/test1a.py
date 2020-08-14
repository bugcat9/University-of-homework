#!/usr/bin/env python

_version_=0.97
print "This script is for part 1 of project 1 only."
print "Version:",_version_#,"NOT used for grading!"
import sys,os,signal
if sys.version_info < (2,4):
    print "This script requires python 2.4 or higher. Exit now."
    sys.exit(1)

s_bin="./server"

try:
    c_bin="./"+sys.argv[1]
except:
    c_bin="./client"

if not os.path.isfile(c_bin):
    print "Can't find client binary. You should either pass its name as an argument or rename it to 'client'. Exit now."
    sys.exit(1)

if not os.path.isfile(s_bin):
    print "Can't find server binary. You should put it in the same folder. Exit now."
    sys.exit(1)

#some util funcs
#cmd:[msgtype, length, padding], 0 means variable
dictcmd={'login':['01 ',16,2],
        'logout':['09 ', 4,0],
          'move':['03 ', 8,3],
        'attack':['05 ',16,2],
         'speak':['07 ', 0,0],
         'north':'00 ',
         'south':'01 ',
          'east':'02 ',
          'west':'03 '}
dictmsg={'02':['Welcome to the tiny world of warcraft','A player with the same name is already in the game'],
         '04':[': location=(', ',', '), HP=', ', EXP=',''],
         '06':[' damaged ',' by ','. ',"'s HP is now "],
         '08':[': '],
         '0a':['Player ',' has left the tiny world of warcraft'],
         '0b':['You must log in first','You already logged in']}
errmsgs={'con_fail':'The gate to the tiny world of warcraft is not ready.',
        'bad_msg':'Meteor is striking the world.',
        'discon':"The gate to the tiny world of warcraft has disappeared.",
        'tgt_invis':"The target is not visible"}
# (fault type, trigger_command, expect, no_expect)
malformedcases=[(1,'move north', errmsgs['bad_msg'], None, "an invalid version"),
                (4,'speak speak_message', None, 'speak_message', "a truncated last packet"),
                (6,'speak speak_message', errmsgs['bad_msg'], None, "a packet not 4byte aligned"),
                (7,'move north', errmsgs['bad_msg'], None, "an invalid player name"),
                (8,'speak speak_message', errmsgs['bad_msg'], None, "an invalid speak message" ),
                (9,'move north', errmsgs['bad_msg'], None, "an invalid location")]

def killsubprocs(subplst):
    for p in subplst:
        try:
            #try:
            #    print p.stdout.read()
            #except:
            #    pass
            os.kill(p.pid, signal.SIGTERM)
        except:
            pass

def cmd2hex(cmd, arg):
    hex='04 '
    if dictcmd[cmd][1] > 0:
        hex+='00 %02x ' % dictcmd[cmd][1] #won't work if len>256
    else:
        #speak msg
        msglen=4+len(arg)+4-len(arg)%4
        hexlen='%04x ' % msglen
        hex+=hexlen[:2]+' '+hexlen[2:]
    hex+=dictcmd[cmd][0]
    if len(arg) > 0:
        try:
            hex+=dictcmd[arg]
        except:
            hex+="".join(['%02x ' % ord(c) for c in arg])

    return hex

def hex2str(hex):
    byte=hex.split()[3:]
    #print byte
    mtype=byte[0]
    if mtype=='02':
        return dictmsg[mtype][int(byte[1])]
    if mtype=='04':
        varlst=[''.join(byte[1:byte.index('00')]).decode('hex'),
                str(eval('0x'+byte[11])),
                str(eval('0x'+byte[12])),
                str(eval('0x'+''.join(byte[13:17]))),
                str(eval('0x'+''.join(byte[17:-1])))]
        return ''.join([''.join(e) for e in zip(varlst,dictmsg[mtype])])
    if mtype=='06':
        attacker=''.join(byte[1:byte.index('00')]).decode('hex')
        tmp=byte[11:]
        victim=''.join(tmp[:tmp.index('00')]).decode('hex')
        damage=str(eval('0x'+byte[21]))
        hp=eval('0x'+''.join(byte[22:26]))
        if hp > 0:
            msg=dictmsg[mtype]
            return attacker+msg[0]+victim+msg[1]+damage+msg[2]+victim+msg[3]+str(hp)
        else:
            return attacker+' killed '+victim
    if mtype=='08':
        player=''.join(byte[1:byte.index('00')]).decode('hex')
        tmp=byte[11:]
        msg=''.join(tmp[:tmp.index('00')]).decode('hex')
        return player+dictmsg[mtype][0]+msg
    if mtype=='0a':
        player=''.join(byte[1:byte.index('00')]).decode('hex')
        return dictmsg[mtype][0]+player+dictmsg[mtype][1]
    if mtype=='0b':
        return dictmsg[mtype][int(byte[1])]
    return 'Invalid msg type:'+mtype

#init user file so we know they will see each other
datadir='users'
player=['mario', 'peach']
pinfo=['40 0 20 20', '33 0 20 20']

if not os.path.isdir(datadir):
    os.mkdir(datadir)

for i in range(len(player)):
    f=open(datadir+'/'+player[i],'w')
    f.write(pinfo[i])
    f.close()

#cmds to stdin, (playerid,cmd,arg)
cmds=[(0,'login',player[0]),
        (1,'login',player[0]),
        (1,'move','east'),
        (1,'login',player[1]),
        (0,'move','east'),
        (1,'login',player[1]),
        (1,'move','east'),
        (1,'move','north'),
        (1,'login',player[1]),
        (1,'attack',player[0]),
        (0,'speak','I come for peace!'),
        (1,'move','south'),
        (1,'attack',player[0]),
        (1,'move','west'),
        (0,'move','west'),
        (0,'attack',player[1]),
        (1,'speak','this is a very long long long long message, but still shorter than 256 bytes.'),
        (1,'speak','1234567890 '*23+'X'),
        (0,'logout',''),
        (1,'logout','')]

import random, fcntl, time
from subprocess import Popen, PIPE, STDOUT

points=0
timer=1.0
#start server
port=random.randrange(30000,65000)
print "Starting server at port",port
svr=Popen([s_bin, "-p", str(port)], stdout=PIPE, stderr=STDOUT)
time.sleep(timer)
if svr.poll() is not None:
    print "Server error:",svr.stdout.read(),"Exit now."
    sys.exit(1)
#get '* Listening socket is ready.\n'
fcntl.fcntl(svr.stdout, fcntl.F_SETFL, os.O_NONBLOCK)
time.sleep(timer)
svr.stdout.readline()

#start clients
print "Starting clients: ",
plst=[]
pfd={}#server side fds
for i in range(len(player)):
    print i,
    tmp=Popen([c_bin,'-s','127.0.0.1','-p',str(port)], stdin=PIPE, stdout=PIPE, stderr=STDOUT)
    time.sleep(timer)
    if tmp.poll() is not None:
        print "Client error:",tmp.stdout.read(),"Exit now."
        killsubprocs(plst.append(svr))
        sys.exit(1)
    else:
        plst.append(tmp)
        #get sth like 'New connection from 127.0.0.1.53890. fd=5\n'
        time.sleep(timer)
        pfd[svr.stdout.readline().split('=')[1][:-1]]=i
print ' '

#set client stdout to nonblock, avoid readline hang
for p in plst:
    fcntl.fcntl(p.stdout, fcntl.F_SETFL, os.O_NONBLOCK)

#start testing
npass=0
rpass=0
fpass=0
rtest=35
ftest=0
for cmd in cmds:
    if cmd[0] >= len(plst):
        print 'Client id',cmd[0],'is not valid, should be smaller than',len(plst)
        continue
    print "Testing client",cmd[0],':',cmd[1],cmd[2],
    if plst[cmd[0]].poll() is not None:
        print "Client",cmd[0],"has exited. Try next test case."
        continue
    if len(cmd[2]) > 0:
        plst[cmd[0]].stdin.write(cmd[1]+' '+cmd[2]+'\n')
    else:
        plst[cmd[0]].stdin.write(cmd[1]+'\n')

    goodhex=cmd2hex(cmd[1],cmd[2])
    try:
        time.sleep(timer)
        svrout=svr.stdout.readline()
        while "received" not in svrout:
            svrout=svr.stdout.readline()
        realhex=svrout.split('[')[1][:-1]
        if realhex.find(goodhex) is 0:
            print 'PASS'
            npass=npass+1
        else:
            print 'FAIL'
            print 'Expect: [',goodhex,']'
            print 'But get:[',realhex
    except:
        print 'FAIL'
        print 'Expect: [',goodhex,']'
        print 'But get nothing.'
        continue

    while len(realhex)>0:
        try:
            svrout=svr.stdout.readline()
            fd=svrout.split(':')[1].split()[0]
            realhex=svrout.split('[')[1][:-1]
            print "Testing client",pfd[fd],"of server reply",
            #rtest=rtest+1
            try:
                goodstr=hex2str(realhex)
            except:
                import traceback
                traceback.print_exc()
            try:
                time.sleep(timer)
                realstr=plst[pfd[fd]].stdout.readline()[:-1]
                #print realstr
                if goodstr in realstr:
                    print 'PASS'
                    rpass=rpass+1
                else:
                    print 'FAIL'
                    print 'Expect: ',goodstr
                    print 'But get:',realstr.replace('command> ','')
            except:
                #not always fail, may because not in sight move_notify
                #need func isignore, need to track all player positions
                #for now we make positions always in sight
                print 'FAIL'
                print 'Expect: ',goodstr
                print 'But get nothing'

        except:
            break
if npass==len(cmds):
    print 'Your client passes all',npass,'command test cases, and',rpass,'of',rtest,'server reply tests.'
else:
    print 'Your client only passes',npass,'of',len(cmds),'command test cases, and',rpass,'of',rtest,'server reply tests.'
#try:
#    print svr.stdout.readline()
#except:
#    pass
os.kill(svr.pid, signal.SIGTERM)
killsubprocs(plst)
print 'Now testing exception handling.'
print 'Testing connect failure:',
ftest += 1
tmp=Popen([c_bin,'-s','127.0.0.1','-p',str(port)], stdin=PIPE, stdout=PIPE, stderr=STDOUT)
time.sleep(timer)
if tmp.poll() is not None:
    outstr=tmp.stdout.read().replace('command> ','')
    if errmsgs['con_fail'] in outstr:
        print 'PASS'
        fpass += 1
    else:
        print 'FAIL'
        print 'Expect:',errmsgs['con_fail']
        print 'But get:',outstr
else:
    print "FAIL","Client didn't exit after connect failure"
    os.kill(tmp.pid, signal.SIGTERM)
print 'Testing server disconnect:',
ftest += 1
svr=Popen([s_bin, "-p", str(port)], stdout=PIPE, stderr=STDOUT)
time.sleep(timer)
if svr.poll() is not None:
    print "Server error:",svr.stdout.read(),"Exit now."
    sys.exit(1)
tmp=Popen([c_bin,'-s','127.0.0.1','-p',str(port)], stdin=PIPE, stdout=PIPE, stderr=STDOUT)
time.sleep(timer)
os.kill(svr.pid, signal.SIGTERM)
#fcntl.fcntl(tmp.stdout, fcntl.F_SETFL, os.O_NONBLOCK)
time.sleep(timer)
if tmp.poll() is not None:
    outstr=tmp.stdout.read().replace('command> ','')
    if errmsgs['discon'] in outstr:
        print 'PASS'
        fpass += 1
    else:
        print 'FAIL'
        print 'Expect:',errmsgs['discon']
        print 'But get:',outstr
else:
    print "FAIL","Client didn't exit after connect failure"
    os.kill(tmp.pid, signal.SIGTERM)
for (case_no, cmd, expect, no_expect, help) in malformedcases:
    print "Testing malformed packet case: %s:" % help,
    ftest += 1
    svr=Popen([s_bin, "-p", str(port), "-f", str(case_no)], stdout=PIPE, stderr=STDOUT)
    tmp=Popen([c_bin,'-s','127.0.0.1','-p',str(port)], stdin=PIPE, stdout=PIPE, stderr=STDOUT)
    tmp.stdin.write('login %s\n' % player[0])
    time.sleep(timer)
    # gives the trigger command.
    tmp.stdin.write('%s\n' % cmd)
    time.sleep(3)
    if tmp.poll() is not None:
        outstr=tmp.stdout.read().replace('command> ','')
        # Ignore the first greeting message and the first move notify
        outstr = outstr[outstr.find('\n', outstr.find(dictmsg['04'][0])):]
        if expect and not expect in outstr:
            print 'FAIL'
            print 'Expect:',expect
            print 'But get:',outstr
        elif no_expect and no_expect in outstr:
            print 'FAIL'
            print 'Must not expect:',no_expect
            print 'But get:',outstr
        else:
            print 'PASS'
            fpass += 1
    else:
        print "FAIL","Client didn't exit after a malformed server packet."
        os.kill(tmp.pid, signal.SIGTERM)
    os.kill(svr.pid, signal.SIGTERM)

points=80.0*(npass+rpass)/(len(cmds)+rtest) + 20.0*fpass/ftest
print "npass: %d" % npass
print "rpass: %d" % rpass
print "fpass: %d" % fpass
print "Points: %.1f" % points
sys.exit(0)
