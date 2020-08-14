from string import *
from Player.player import *
from socket import *
from Package.Constants import *
from Package.Operation import *
from time import sleep
import threading
import sys
import os
import getopt

lock=threading.RLock()
class client:

    def __init__(self,IP,port):
        self.IP=IP
        self.port=port
        try:
            #如果服务器未开启，则抛出异常
            self.client_socket=socket(AF_INET,SOCK_STREAM)
            self.client_socket.connect((IP,port))
        except:
            sys.stdout.write('The gate to the tiny world of warcraft is not ready.\n')
            self.show_input()
            os._exit(0)

        self.vision_range=5       #视野范围
        self.player=None           #玩家

        self.direction_dictionary={'north':direction.NORTH.value,'south':direction.SOUTH.value,
                                   'west':direction.WEST.value,'east':direction.EAST.value}
        #方向集合
        self.players_in_vision=set()
        #在视野范围内的玩家，包括这台客户端的玩家


    def do_login(self,name):
        #处理login指令
        #参数为登录名
        if " " in name:
            #名字中不能有空格
            sys.stdout.write("! Invalid name: %s"%(name))
            self.show_input()
        else:
            requestType = messages.LOGIN_REQUEST        #message type
            package=operation.make_packet(requestType,name)
            self.player=player(name)
            self.client_socket.send(package.message)

    def do_move(self,direction):
        #处理move指令
        #参数为（north，south，west，east）
        requestType = messages.MOVE
        move_directon=self.direction_dictionary.get(direction)
        if move_directon==None:
            #错误的方向参数
            sys.stdout.write('! Invalid direction: %s.\n'%(direction))
            self.show_input()
        else:
            #方向参数正确
            package=operation.make_packet(requestType,move_directon)
            self.client_socket.send(package.message)

    def do_attack(self,name):
        #处理attack指令
        if name in self.players_in_vision and name!=self.player.name:
            #只有攻击对象在攻击者视野范围内且攻击对象不时攻击者的时候才能攻击
            requestType = messages.ATTACK
            package=operation.make_packet(requestType,name)
            self.client_socket.send(package.message)
        else:
            #攻击对象不在视野范围内
            sys.stdout.write('The target is not visible.\n')
            self.show_input()


    def do_speak(self,msg):
        #处理speak指令
        #参数msg为speak内容
        requestType = messages.SPEAK
        package=operation.make_packet(requestType,msg)
        self.client_socket.send(package.message)

    def do_logout(self):
        #处理登出
        requestType = messages.LOGOUT
        package=operation.make_packet(requestType)
        self.client_socket.send(package.message)


    def do_login_reply(self,package,name):
        #处理LOGIN_REPLY
        #参数package：解析服务器数据所得的包，Packages
        #参数name是玩家登录时的登录名
        if package.Error_code==0:          #login success
            #登录成功，创建player对象，同步属性
            sys.stdout.write("Welcome to the tiny world of warcraft.\n")
            self.player=player(name)
            self.player.setAttribute(package.HP,package.EXP,package.x,package.y)     #get attributes
            self.players_in_vision.add(name)
        elif package.Error_code==1:
            #登录的帐号已经被登录
            self.player=None
            sys.stdout.write('A player with the same name is already in the game.\n')
        self.show_input()


    def do_move_notify(self,package):
        #处理MOVE_NOTIFY消息
        #参数是解析服务器数据所得的包，Packages
        if package.player_name == self.player.name:
            #如果玩家名是这台客户端上登录的玩家，则更新玩家属性
            self.player.setAttribute(package.HP, package.EXP, package.x, package.y)  # change player local
        if self.point_in_vision(package.x,package.y):
            #当玩家处于视野范围内时才会输出位置信息
            sys.stdout.write("%s: location=(%d,%d), HP=%d, EXP=%d.\n"%(package.player_name,package.x,package.y,package.HP,package.EXP))
            self.show_input()
            self.players_in_vision.add(package.player_name)
        else:
            self.players_in_vision.discard(package.player_name)


    def point_in_vision(self,x,y):
        #判断一个点（x，y）是否在视野范围内
        if x>=self.player.x-self.vision_range and x<=self.player.x+self.vision_range:
            if y>=self.player.y-self.vision_range and y<=self.player.y+self.vision_range:
                return True
        return False

    def do_attack_notify(self,package):
        #处理ATTACK_NOTIFY消息
        #参数是从服务器接受的数据解析成的包
        Attacker=package.Attacker_name
        Victim=package.Victim_name
        if Attacker in self.players_in_vision and Victim in self.players_in_vision:
            #判断攻击者和被攻击者是否在事业范围内
            #只有两者都在事业范围内时才会打印消息
            if not package.HP==0:
                #血量非0时打印造成伤害
                sys.stdout.write("%s damaged %s by %d. %s\'s HP is now %d.\n"%(Attacker,Victim,package.Damage,Victim,package.HP))
            else:
                #血量为0时打印击杀消息
                sys.stdout.write("%s killed %s.\n"%(Attacker,Victim))
            self.show_input()

    def do_speak_notify(self,package):
        #处理SPEAK_NOTIFY消息
        #参数时Packages的对象
        sys.stdout.write("%s: %s.\n"%(package.Broadcaster_name,package.Msg))
        self.show_input()

    def do_logout_notify(self,package):
        #处理LOGOUT_NOTIFY数据
        #参数是Packages的对象
        sys.stdout.write("Player %s has left the tiny world of warcraft.\n"%(package.player_name))
        self.show_input()

    def do_invalid_state(self,package):
        #处理INVALID-STATE数据
        #参数是一个Packages的对象
        if package.Error_code==0:
            sys.stdout.write("You must log in first.\n")
            #登录之前不能做其他指令
        elif package.Error_code==1:
            sys.stdout.write("You already logged in.\n")
            #不能重复登录
        self.show_input()


    def invalidcommand(self,list):
        #参数是指令分割后得到的数组
        #判断指令是否符合规范
        if len(list)==0 or len(list)>2:
            return False
        if list[0]=="logout" and len(list)>1:
            return False
        return True

    def receiv_msg(self):
        msg=self.client_socket.recv(1024)
        if len(msg)==0:
            #接受到一个空字符，说明连接已经断开
            sys.stdout.write("The gate to the tiny world of warcraft has disappeared.\n")
            self.show_input()
            os._exit(0)
        packages = operation.unpack_msg(msg)
        if packages==None:
            #打包失败，说明接受到一个畸形包
            sys.stdout.write("Meteor is striking the world.\n")
            self.show_input()
            os._exit(0)
            #返回值为一个Package的数组
        return packages

    def run_send(self):
        self.show_input()
        while True:
            #输入指令
            command=input()
            self.show_input()
            command_list=command.split(" ",1)
            #处理指令及参数
            Type=""
            if self.invalidcommand(command_list):
                Type=command_list[0]
                #指令格式
            else:
                sys.stdout.write("! Invalid command: %s.Available commands = login, move, attack, speak, logout.\n"%(Type))
                self.show_input()
            if Type=="login":
                name=command_list[1]
                self.do_login(name)
            elif Type=="move":
                direction=command_list[1]
                self.do_move(direction)
            elif Type=="attack":
                name=command_list[1]
                self.do_attack(name)
            elif Type=="speak":
                msg=command_list[1]
                self.do_speak(msg)
            elif Type=="logout":
                self.do_logout()
                sys.stdout.write("The gate to the tiny world of warcraft has disappeared.\n")
                self.show_input()
                os._exit(0)
            else:
                sys.stdout.write("! Invalid command: %s.Available commands = login, move, attack, speak, logout.\n"%(Type))
                self.show_input()
            sleep(0.8)


    def run_receiv(self):
        #从服务器接受消息
        while True:
            packages=self.receiv_msg()
            lock.acquire()
            for package in packages:
                type=package.type
                #判断是那一种消息类型
                if type is messages.LOGIN_REPLY:
                    self.invalidlocation(package.x,package.y)
                    self.do_login_reply(package,self.player.name)
                elif type is messages.MOVE_NOTIFY:
                    self.invalidname(package.player_name)
                    self.invalidlocation(package.x,package.y)
                    self.do_move_notify(package)
                elif type is messages.ATTACK_NOTIFY:
                    self.invalidname(package.Attacker_name)
                    self.invalidname(package.Victim_name)
                    self.do_attack_notify(package)
                elif type is messages.SPEAK_NOTIFY:
                    self.invalidname(package.Broadcaster_name)
                    self.invalidspeak(package.Msg)
                    self.do_speak_notify(package)
                elif type is messages.LOGOUT_NOTIFY:
                    self.invalidname(package.player_name)
                    self.do_logout_notify(package)
                elif type is messages.INVALID_STATE:
                    self.do_invalid_state(package)
            lock.release()

    def show_input(self):
        #每次输出结束后在新的一行打印command>，清除缓冲区
        sys.stdout.write("command> ")
        sys.stdout.flush()

    def invalidname(self,name):
        #判断收到的包中的名字是否符合规范
        if name==None or name=="" or " " in name:
            sys.stdout.write("Meteor is striking the world.\n")
            self.show_input()
            os._exit(0)

    def invalidspeak(self,msg):
        #判断收到的speak内容是否符合规范
        if len(msg)==0:
            sys.stdout.write("Meteor is striking the world.\n")
            self.show_input()
            os._exit(0)

    def invalidlocation(self,x,y):
        #判断位置信息是否有效
        if x<0 or x>99 or y<0 or y>99:
            sys.stdout.write("Meteor is striking the world.\n")
            self.show_input()
            os._exit(0)

def main(argv):
    try:
        opts,args=getopt.getopt(argv,'s:p:',[])
        ip=None
        port=None
        for opt,arg in opts:
           if opt=='-s':
               ip=arg
           elif opt=='-p':
               port=arg
        #ip='192.168.43.22'
        #port=8080
        oneclient=client(ip,int(port))
        try:
            t1=threading.Thread(target=oneclient.run_receiv)
            t2=threading.Thread(target=oneclient.run_send)
            t1.setDaemon(True)
            t2.setDaemon(True)
            t1.start()
            t2.start()
            t1.join()
            t2.join()
        except KeyboardInterrupt:
            pass
        except:
            sys.stdout.write("Error: unable to start thread.\n")

    except:
        sys.stdout.write("! ./client -s <serverID> -p <serverport>\n")




if __name__=='__main__':
    main(sys.argv[1:])


