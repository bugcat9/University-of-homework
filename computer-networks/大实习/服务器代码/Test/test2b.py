#!/usr/bin/env python

# Author: Junda Liu (liujd@cs)
# Author: DK Moon (dkmoon@cs)

import fcntl, os, random, re, signal, socket, struct, sys, types
import curses.ascii
from subprocess import Popen, PIPE, STDOUT
from time import sleep, time

# Turn on this flag if need more debugging messages.
verbose = 1

# Environment set up.
_version_=0.31
c_bin = './client'
try:
    s_bin = "./"+sys.argv[1]
except:
    s_bin = "./server"
refs_bin = "./ref_server"
plst="peers.lst"

s_port = random.randrange(80, 256)*256 + random.randrange(1, 30)
s_players = ['mario', 'peach', 'ryu', 'ken', 'guile', 'chunli', 'sophitia']
s_timer = 1.0  # in sec

EE122_VALID_VERSION = 4
EE122_X_SIZE = 100
EE122_Y_SIZE = 100
MALFORMED_MESSAGE_TEXT = 'The gate to the tiny world of warcraft has disappeared.'

# Checks the python version.
if sys.version_info < (2,4):
    print 'This script requires python 2.4 or higher. Exit now.'
    sys.exit(1)

def check_server_binary():
    """ Verifies the server binary is executable file. """
    if not os.path.isfile(s_bin):
        print "Can't find server binary.You should put it in the same folder. Exit now."
        sys.exit(1)
    if not os.access(s_bin, os.X_OK):
        print "Server file '%s' is not executable. need to run chmod +x." % s_bin
        sys.exit(1)
    if not os.path.isfile(refs_bin):
        print "Can't find reference server binary.You should rename it to ref_server and put it in the same folder. Exit now."
        sys.exit(1)
    if not os.access(refs_bin, os.X_OK):
        print "Server file '%s' is not executable. need to run chmod +x." % s_bin
        sys.exit(1)

def launch_server(port):
    null = open('/dev/null', 'w')
    server = Popen([s_bin, '-t', str(port), '-u', str(port)], stdout = null)
    if server.poll():
        raise RuntimeError('! Failed to launch the server.')
    #print 'Start server at port %d' % port
    return server

def launch_refserver(port):
    server = Popen([refs_bin, '-t', str(port), '-u', str(port)], stdout = PIPE, stderr = STDOUT)
    if server.poll():
        raise RuntimeError('! Failed to launch the reference server.')
    v = fcntl.fcntl(server.stdout, fcntl.F_GETFL)
    fcntl.fcntl(server.stdout, fcntl.F_SETFL, v | os.O_NONBLOCK)
    #print 'Start reference server at port %d' % port
    return server

def write_player(players, pinfo='40 1 20 20'):
    datadir='users'

    if not os.path.isdir(datadir):
        os.mkdir(datadir)

    for i in range(len(players)):
        if os.path.isfile(datadir+'/'+players[i]):
            continue
        f=open(datadir+'/'+players[i],'w')
        f.write(pinfo)
        f.close()

def calc_svrid(ipstr, port):
    ret = 0
    for e in ipstr.split('.'):
        if int(e) == 0:
            return ret % 1024
        ret = int(e) + 31*ret
    if (port/256) > 0:
        ret = port/256 + 31*ret
        ret = port % 256 + 31*ret
    return ret %1024

if __name__ == '__main__':
    print '* This script is for Project 2B.'
    print '* Version:', _version_, 'NOT for grading!'
    print '* Future verison will check more conditions, so current PASS may become FAIL.'

    check_server_binary()

    svr_ip = socket.gethostbyname(socket.getfqdn())
    print '* IP address:', svr_ip

    #print '* Write player data'
    write_player(s_players)

    if os.path.isfile(plst):
        print '* Rename peers.lst to peers.lst.bk'
        os.rename(plst,plst+".bk")

    #print '* Time allowance for most tests is %d sec' % s_timer
    print 'Test single server: P2P_ID and peers.lst',

    svrid = calc_svrid(svr_ip, s_port)
    svr = launch_server(s_port)
    sleep(s_timer)
    try:
        f = open(plst, 'r')
        line=f.readlines()[0]
        f.close()
        exp=str(svrid)+' '+svr_ip+' '+str(s_port)
        if line.startswith(exp):
            print "PASS"
        else:
            print "FAIL"
            print "Expect: %s" % exp
            print "But get:%s" % line
    except:
        print "FAIL. Read peers.lst exception."
    os.kill(svr.pid, signal.SIGTERM)

    print 'Test single server: peers.lst ID conflicts',
    f = open(plst, 'w')
    f.write(str(svrid)+' '+svr_ip+' '+str(s_port+1))
    f.close()
    #svr = launch_server(s_port)
    svr = Popen([s_bin, '-t', str(s_port), '-u', str(s_port)], stdout = PIPE)
    sleep(s_timer)
    if svr.poll() is not None:
        print "PASS"
    else:
        print "FAIL. Server doesn't exit when ID conflicts"
        os.kill(svr.pid, signal.SIGTERM)
    os.remove(plst)

    print 'Test 1 server and 1 ref_server: ref_server sends join request',
    svr = launch_server(s_port)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        ref_svr = launch_refserver(s_port + 1000)
        sleep(s_timer*2)
        refout = ref_svr.stdout.read().split('\n')
        i=0
        for line in refout:
            if line.startswith('P2P: recv'):
                i=i+1
        if i==2:
            print "PASS"
        else:
            print "FAIL. ref_server only received %d response, should be 2" % i

    print 'Test 1 server and 1 ref_server: ref_server crashes',
    os.kill(ref_svr.pid, signal.SIGTERM)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        print "PASS"

    print 'Test 1 server and 1 ref_server: ref_server comes up again',
    if svr.poll():
        print "FAIL. server terminated."
    else:
        ref_svr = launch_refserver(s_port + 1000)
        sleep(s_timer*2)
        refout = ref_svr.stdout.read().split('\n')
        i=0
        for line in refout:
            if line.startswith('P2P: recv'):
                i=i+1
        if i==2:
            print "PASS"
        else:
            print "FAIL. ref_server only received %d response, should be 2" % i

    os.kill(svr.pid, signal.SIGTERM)
    os.remove(plst)

    print 'Test 1 server and 1 ref_server: server sends join request',
    ref_svr = launch_refserver(s_port)
    sleep(s_timer)
    svr = launch_server(s_port + 100)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        refout = ref_svr.stdout.read().split('\n')
        i=0
        j=0
        for line in refout:
            if line.startswith('P2P: recv'):
                i=i+1
            if line.startswith('P2P: send'):
                j=j+1
        if i==1 and j==2:
            print "PASS"
        else:
            print "FAIL. ref_server has %d recv and %d send. should be 1 and 2" % (i,j)

    print 'Test 1 server and 1 ref_server: ref_server crashes',
    os.kill(ref_svr.pid, signal.SIGTERM)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        print "PASS"
    sleep(s_timer)
    os.kill(svr.pid, signal.SIGTERM)
    os.remove(plst)
#---------ref svr ref------------
    print 'Test 1 server and 2 ref_servers: server id in the middle and joins last',
    ref1 = launch_refserver(s_port)
    sleep(s_timer)
    ref2 = launch_refserver(s_port + 100)
    sleep(s_timer)
    ref1id = calc_svrid(svr_ip, s_port)
    ref2id = calc_svrid(svr_ip, s_port + 100)
    svrid = calc_svrid(svr_ip, s_port +50)
    print '(ref1id %d ref2id %d svrid %d)' % (ref1id, ref2id, svrid),

    svr = launch_server(s_port + 50)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        exp="recv P2P_JOIN_REQUEST from %d" % svrid
        if ref1.stdout.read().find(exp)>0 and ref2.stdout.read().find(exp)>0:
            print "PASS"
        else:
            print "FAIL. Not both refsvr recv P2P_JOIN"

    print 'Test 1 server and 2 ref_servers: ref2 crashes',
    os.kill(ref2.pid, signal.SIGTERM)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        if ref1.stdout.read().find("New connection") >= 0:
            print "PASS"
        else:
            print "FAIL. ref1 didn't get new connection from sever."
    
    os.kill(ref1.pid, signal.SIGTERM)
    os.kill(svr.pid, signal.SIGTERM)
    os.remove(plst)

    print 'Test 1 server and 2 ref_servers: server id in the middle and joins second',
    ref1 = launch_refserver(s_port)
    sleep(s_timer)
    ref1id = calc_svrid(svr_ip, s_port)
    ref2id = calc_svrid(svr_ip, s_port + 100)
    svrid = calc_svrid(svr_ip, s_port +50)
    print '(ref1id %d ref2id %d svrid %d)' % (ref1id, ref2id, svrid),
    svr = launch_server(s_port + 50)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        ref2 = launch_refserver(s_port + 100)
        sleep(s_timer)
        refout = ref2.stdout.read().split('\n')
        i=0
        j=0
        for line in refout:
            if line.startswith('P2P: recv'):
                i=i+1
            if line.startswith('New connection'):
                j=j+1
        if i==2 and j==1:
            print "PASS"
        else:
            print "FAIL. ref2 has %d recv and %d new connection. should be 2 and 1" % (i,j)

    os.kill(ref1.pid, signal.SIGTERM)
    os.kill(ref2.pid, signal.SIGTERM)
    os.kill(svr.pid, signal.SIGTERM)
    os.remove(plst)
#---------ref ref svr------------
    print 'Test 1 server and 2 ref_servers: server id is biggest and joins last',
    ref1 = launch_refserver(s_port)
    sleep(s_timer)
    ref2 = launch_refserver(s_port + 51)
    sleep(s_timer)
    ref1id = calc_svrid(svr_ip, s_port)
    ref2id = calc_svrid(svr_ip, s_port + 51)
    svrid = calc_svrid(svr_ip, s_port +100)
    print '(ref1id %d ref2id %d svrid %d)' % (ref1id, ref2id, svrid),

    svr = launch_server(s_port + 100)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        exp="recv P2P_JOIN_REQUEST from %d" % svrid
        if ref1.stdout.read().find(exp)>0 and ref2.stdout.read().find(exp)>0:
            print "PASS"
        else:
            print "FAIL. Not both refsvr recv P2P_JOIN"

    print 'Test 1 server and 2 ref_servers: ref1 crashes',
    os.kill(ref1.pid, signal.SIGTERM)
    sleep(s_timer)
    if svr.poll():
        print "FAIL. server terminated."
    else:
        if ref2.stdout.read().find("New connection") >= 0:
            print "PASS"
        else:
            print "FAIL. ref2 didn't get new connection from sever."
    
    os.kill(ref2.pid, signal.SIGTERM)
    os.kill(svr.pid, signal.SIGTERM)
    os.remove(plst)
