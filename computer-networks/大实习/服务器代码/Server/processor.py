from Package.Constants import messages
from Player.player import player
from Package.Operation import operation
from threading import Timer
import datetime
import random
import threading
import os
import Server.server

OnlineRole = set(())
lock = threading.Lock()

class processor:
    def __init__(self,sock):
        self.roles = set(())
        self.curPlay =None
        self.isLogin = False
        self.sock = sock
        if not os.path.exists("Users"):
            os.makedirs("Users")
        for filename in os.listdir("Users"):
            self.roles.add(filename)

    def addHP(self):
        self.curPlay.HP +=1
        t = Timer(5,self.addHP)
        t.start()
    #处理消息
    def doMessage(self,packet):
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
            return
        self.name = packet.name
        # 這個是新創建
        if packet.name+".txt" not in self.roles:
            self.curPlay = player(packet.name)
            self.createNewRole(self.curPlay)
            self.writeRole(self.curPlay)
        self.curPlay = player(self.name)
        self.readRole(self.curPlay)


        notifePacket = operation.make_packet(messages.LOGIN_REPLY,0,self.curPlay.HP,self.curPlay.EXP,self.curPlay.x,
                                       self.curPlay.y)
        notifePacket2 = operation.make_packet(messages.MOVE_NOTIFY, self.curPlay.name, self.curPlay.x, self.curPlay
                                             .y, self.curPlay.HP, self.curPlay.EXP)
        lock.acquire()
        OnlineRole.add(self.name)
        lock.release()
        self.sock.send(notifePacket.message)
        self.broadCast(notifePacket2)
        self.Casted()
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
        notifePacket = None
        Victimer = None
        for key in server.pros:
            if key.curPlay.name == packet.Victim_name:
                Damage = random.randint(10, 20)
                Victimer = key
                if Damage  <= key.curPlay.HP:
                    key.curPlay.HP -= Damage
                else:
                    key.curPlay.HP = 0
                notifePacket = operation.make_packet(messages.ATTACK_NOTIFY, self.curPlay.name, packet.Victim_name, Damage, key.curPlay.HP)
                break
        self.broadCast(notifePacket)
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
        server.pros.remove(self)
        OnlineRole.remove(self.curPlay.name)
        self.broadCast(notifePacket)
        self.writeRole(self.curPlay)
        self.sock.close()

    def writeRole(self,player):
        filePath = os.getcwd() + os.path.sep + "Users" + os.path.sep + player.name + ".txt"
        with open(filePath, 'w') as f:
            f.write(str(player.HP) + '\t')
            f.write(str(player.EXP) + "\t")
            f.write(str(player.x)+"\t"+str(player.y))
            f.close()

    def readRole(self,player):
        filePath = os.getcwd() + os.path.sep + "Users" + os.path.sep + player.name + ".txt"
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

    def broadCast(self,packet):
        for key in server.pros:
            key.sock.send(packet.message)

    def doInvalid(self,Validtype):
        ValidPacket = None
        if Validtype == 0:
            ValidPacket = operation.make_packet(messages.INVALID_STATE,0)
        elif Validtype == 1:
            ValidPacket = operation.make_packet(messages.INVALID_STATE, 1)
        self.sock.send(ValidPacket.message)


    def Casted(self):
        for key in server.pros:
            if key.curPlay.name != self.curPlay.name:
                notifePacket = operation.make_packet(messages.MOVE_NOTIFY,key.curPlay.name,key.curPlay.x,key.curPlay
                                       .y,key.curPlay.HP,key.curPlay.EXP)
                self.sock.send(notifePacket.message)