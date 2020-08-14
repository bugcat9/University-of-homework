import socket
from Package.Constants import messages
from Player.player import player
from Package.Operation import operation
from threading import Timer
import random
import threading
import os
import Server.server
import sys
import getopt


OnlineRole = set(())#已经登陆的用户
lock = threading.Lock()
pros = list()#所有的子线程
roles = set(())
fd=4

class Server:
    buffsize = 2048
    def __init__(self,port):

        if not os.path.exists("users"):
            os.makedirs("users")
        for filename in os.listdir("users"):
            roles.add(filename)

        s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind(('',int(port)))
        print('* Listening socket is ready.')
        s.listen(25)
        self.run(s)
        s.close()


    def run(self,s):
        while True:
            clientsock,clientaddress = s.accept()
            print('New connection from '+clientaddress[0]+'.'+'%d. '%clientaddress[1]+ 'fd=%d'%fd)
            #print("连接已建立")
            t = threading.Thread(target =self.tcplink,args = (clientsock,clientaddress))
            t.setDaemon(True)       #设置为守护线程
            t.start()

    def tcplink(self,sock,addre):
        global  fd
        pro = processor(sock)  # 制作处理者

        pro.fd = fd
        if fd-4>=len(pros):
            # 锁fd
            lock.acquire()
            fd += 1
            lock.release()
        else:
            lock.acquire()
            fd=len(pros)+5
            lock.release()

        pros.append(pro)         #加一个进程
        while True:
            try:
                recvData = sock.recv(self.buffsize)
            except Exception:
                continue
            if recvData.decode() == "":
                packet = operation.make_packet(messages.LOGOUT)
                if pro.curPlay != None:
                    pro.doLOGOUT(packet)
                else:
                    pro.sock.close()

                print('* Socket closed. fd=%d'%pro.fd)
                print('- Garbage collected. fd=%d'%pro.fd)

                lock.acquire()
                fd=pro.fd
                lock.release()

                break

            packets = operation.unpack_msg(recvData)      #解析数据,制作包
            if packets == None:
                packet = operation.make_packet(messages.LOGOUT)
                if pro.curPlay != None:
                    pro.doLOGOUT(packet)
                else:
                    pro.sock.close()
                return
            dealRes = True                               #處理包的結果
            for packet in packets:
                if pro.doMessage(packet) == False:            #将包传给处理者
                    dealRes = False
                    break
            if dealRes == False:
                break

class processor:
    def __init__(self,sock):
        self.roles = set(())
        self.curPlay =None
        self.isLogin = False
        self.sock = sock
        self.fd=-1

    def addHP(self):
        self.curPlay.HP +=1
        t = Timer(5,self.addHP)
        t.start()
    #处理消息,需要判断消息是否正确，有待改进
    def doMessage(self,packet):
        self.log('received',packet)
        if packet.type == messages.LOGIN_REQUEST:
            self.doLOGIN_REQUEST(packet)
            return True
        elif self.isLogin == False:
            self.doInvalid(0)
            return True
        elif packet.type == messages.MOVE:
            self.deMOVE(packet)
            return True
        elif packet.type == messages.ATTACK:
            self.doATTACK(packet)
            return True
        elif packet.type == messages.SPEAK:
            self.doSPEAK(packet)
            return True
        elif packet.type == messages.LOGOUT:
            self.doLOGOUT(packet)
            return False

    #args: name
    #role文件资料顺序 HP EXP local
    def doLOGIN_REQUEST(self,packet):
        if(self.isLogin == True):
            self.doInvalid(1)
            return

        elif(packet.name in OnlineRole):

            notifePacket = operation.make_packet(messages.LOGIN_REPLY, 1, 0, 0, 0, 0)
            self.sock.send(notifePacket.message)
            self.log('sending',notifePacket)#在屏幕上打印
            return
        self.name = packet.name


        # 如果不在角色当中，就新建角色
        if packet.name+".txt" not in roles:
            self.curPlay = player(packet.name)
            self.createNewRole(self.curPlay)
            self.writeRole(self.curPlay)
        else:#否则就读角色
            self.curPlay = player(self.name)
            self.readRole(self.curPlay)


        Reply_Packet = operation.make_packet(messages.LOGIN_REPLY,0,self.curPlay.HP,self.curPlay.EXP,self.curPlay.x,
                                       self.curPlay.y)

        notify_Packet = operation.make_packet(messages.MOVE_NOTIFY, self.curPlay.name, self.curPlay.x, self.curPlay
                                             .y, self.curPlay.HP, self.curPlay.EXP)
        lock.acquire()
        OnlineRole.add(self.name)
        lock.release()

        self.sock.send(Reply_Packet.message+notify_Packet.message)
       # self.sock.send(notify_Packet.message)
        self.log('sending', Reply_Packet)  # 在屏幕上打印
        self.log('sending',notify_Packet)

        # self.broadCast(notify_Packet)   #给已经登陆的人发送此用户已经登陆的信息
        for key in pros:
            if key.curPlay!=None and key.curPlay.name != self.curPlay.name:   #要登陆了才能収到广播包
                key.sock.send(notify_Packet.message)
                key.log('sending',notify_Packet)#广播也需要打印
      #  self.Casted()
        # 对于后登陆的人，给他发送开始登陆人的位置
        for key in pros:
            if key.curPlay!=None and key.curPlay.name != self.curPlay.name :
                # 发送别人
                notify_Packet1 = operation.make_packet(messages.MOVE_NOTIFY, key.curPlay.name, key.curPlay.x, key.curPlay
                                                      .y, key.curPlay.HP, key.curPlay.EXP)
                self.sock.send(notify_Packet1.message)
                self.log('sending', notify_Packet1)  # 在屏幕上打印
        self.addHP()
        self.isLogin = True

    #args:Direction
    def deMOVE(self,packet):
    #     先判断移动
        print(packet.Direction)
        if packet.Direction == 0:
            self.curPlay.y = (self.curPlay.y-3+99) %99
        elif packet.Direction == 1:
            self.curPlay.y = (self.curPlay.y+3+99) %99
        elif packet.Direction == 2:
            self.curPlay.x = (self.curPlay.x+3+99) %99
        elif packet.Direction == 3:
            self.curPlay.x = (self.curPlay.x-3+99) %99
        #MOVE_NOTIFY packet
        notifePacket = operation.make_packet(messages.MOVE_NOTIFY,self.curPlay.name,self.curPlay.x,self.curPlay
                                       .y,self.curPlay.HP,self.curPlay.EXP)

        self.broadCast(notifePacket)

    # args:Victim_names
    def doATTACK(self,packet):
        self.curPlay.EXP +=3
        notify_Packet = None
        Victimer = None
        for key in pros:
            if key.curPlay.name == packet.Victim_name:
                Damage = random.randint(10, 20)
                Victimer = key
                if Damage  <= key.curPlay.HP:
                    key.curPlay.HP -= Damage
                else:
                    key.curPlay.HP = 0
                notify_Packet = operation.make_packet(messages.ATTACK_NOTIFY, self.curPlay.name, packet.Victim_name, Damage, key.curPlay.HP)
                break
        self.broadCast(notify_Packet)
        if Victimer.curPlay.HP == 0:
            self.resur(Victimer)
            victimPacket = operation.make_packet(messages.MOVE_NOTIFY, Victimer.curPlay.name, Victimer.curPlay.x, Victimer.curPlay
                                                 .y, Victimer.curPlay.HP, Victimer.curPlay.EXP)
            self.broadCast(victimPacket)

    # 復活
    def resur(self,Victimer):
        self.createNewRole(Victimer.cuePlay)
        Victimer.HP = random.randint(30,50)

    # args:Msg
    def doSPEAK(self,packet):
        notifePacket = operation.make_packet(messages.SPEAK_NOTIFY,self.curPlay.name,packet.Msg)
        self.broadCast(notifePacket)

    def doLOGOUT(self,packet):
        notifePacket = operation.make_packet(messages.LOGOUT_NOTIFY,self.curPlay.name)
        pros.remove(self)
        OnlineRole.remove(self.curPlay.name)
        self.broadCast(notifePacket)
        self.writeRole(self.curPlay)
        self.sock.close()
        print('- Garbage collected. fd=%d' % self.fd)#打印消息

    def writeRole(self,player):
        filePath = os.getcwd() + os.path.sep + "users" + os.path.sep + player.name + ".txt"
        with open(filePath, 'w') as f:
            f.write(str(player.HP) + ' ')
            f.write(str(player.EXP) + ' ')
            f.write(str(player.x)+' '+str(player.y))
            f.close()

    def readRole(self,player):
        filePath = os.getcwd() + os.path.sep + "users" + os.path.sep + player.name + ".txt"
        with open(filePath, 'r') as f:
            data = f.read()
            datas = data.split()
            player.HP = int(datas[0])
            player.EXP = int(datas[1])
            player.x = int(datas[2])
            player.y = int(datas[3])
            f.close()

    def createNewRole(self,player):
        player.x = random.randint(0,99)
        player.y = random.randint(0,99)
        player.HP = random.randint(100,120)
        player.EXP = 0

    #广播包
    def broadCast(self,packet):
        for key in pros:
            if key.curPlay!=None:   #要登陆了才能収到广播包
                key.sock.send(packet.message)
                key.log('sending',packet)#广播也需要打印

    def doInvalid(self,Validtype):
        ValidPacket = None
        if Validtype == 0:
            ValidPacket = operation.make_packet(messages.INVALID_STATE,0)
        elif Validtype == 1:
            ValidPacket = operation.make_packet(messages.INVALID_STATE, 1)
        self.sock.send(ValidPacket.message)
        self.log('sending',ValidPacket)

    def Casted(self):
        for key in pros:
            if key.curPlay.name != self.curPlay.name:
                notifePacket = operation.make_packet(messages.MOVE_NOTIFY,key.curPlay.name,key.curPlay.x,key.curPlay.y
                                                     ,key.curPlay.HP,key.curPlay.EXP)
    #用于显示,type指received和sending
    def log(self,type,packet):
        s=str()
        for i in packet.message:
            s=s+'%02x '%i
        #有待改进
        print('-fd:%d '%self.fd+type+' msg ver:%d len:%d type:%d '%(packet.ver,packet.len,packet.type.value)+'raw_pkt(net_byte_order)=['+s+']')

def main(argv):
    try:
        port = None
        opts, args = getopt.getopt(argv, 'p:', [])
        for opt, arg in opts:
            if opt == '-p':
                port = arg
        server = Server(port)
    except:
        print('! The server port number is not defined.\n'+
            '! Usage: ./server -p <port_number> -f <fault type>\n'
            'On a fault, the client will disconnect.\n'+
            ' Thus, only one fault can be given.\n'+
            'Fault types: 1=Invalid version in move pkt\n'+
            '             2=Invalid version in speak pkt\n'+
            '             3=Insufficient move pkt length\n'+
            '             4=Insufficient speak pkt length\n'+
            '             5=Move pkt not 4byte aligned\n'+
            '             6=Speak pkt not 4byte aligned\n'+
            '             7=Invalid name in move pkt\n'+
            '             8=Invalid speak message\n'+
            '             9=Invalid player location\n'
            )
#
# if __name__ == "__main__":
#     main(sys.argv[1:])
#
if __name__ == '__main__':
    port = input("port:")
    server = Server(port)
