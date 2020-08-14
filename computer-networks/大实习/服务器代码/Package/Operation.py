from Package.Constants import messages
from Package.Packages import LOGIN_REQUEST, LOGIN_REPLY,MOVE,MOVE_NOTIFY,ATTACK,ATTACK_NOTIFY,SPEAK,SPEAK_NOTIFY,LOGOUT,LOGOUT_NOTIFY,INVALID_STATE

import struct
class operation:
    #制造包
    @staticmethod
    def make_packet(type,*args):
        package=None
        if type==messages.LOGIN_REQUEST and len(args)==1:
            package=LOGIN_REQUEST(*args)
        if type == messages.LOGIN_REPLY and len(args)==5:
            package=LOGIN_REPLY(*args)

        if type==messages.MOVE and len(args)==1:
            package = MOVE(*args)

        if type==messages.MOVE_NOTIFY and len(args)==5:
            package=MOVE_NOTIFY(*args)

        if type==messages.ATTACK and len(args)==1:
            package=ATTACK(*args)

        if type==messages.ATTACK_NOTIFY and len(args)==4:
            package=ATTACK_NOTIFY(*args)

        if type == messages.SPEAK and len(args)==1:
            package = SPEAK(*args)

        if type == messages.SPEAK_NOTIFY and len(args)==2:
            package = SPEAK_NOTIFY(*args)

        if type == messages.LOGOUT and len(args)==0:
            package = LOGOUT(*args)

        if type == messages.LOGOUT_NOTIFY and len(args)==1:
            package = LOGOUT_NOTIFY(*args)

        if type == messages.INVALID_STATE and len(args)==1:
            package = INVALID_STATE(*args)

        if package==None or package.message == None:
            return None

        return package

    #登陆请求
    @staticmethod
    def on_login_request(msg):
        i=0
        for i in range(4, len(msg)):
            if msg[i] == 0:
                name = msg[4:i].decode()
                break
        if name.isalnum()==False:   #判断名字符合规定
            return  None
        package = LOGIN_REQUEST(name)
        if package.message==None:
            return None
        return package

    #登陆应答
    @staticmethod
    def on_login_reply(msg):
        Error_code = int.from_bytes(msg[4:5], byteorder='big')
        HP = int.from_bytes(msg[5:9], byteorder='big')
        EXP = int.from_bytes(msg[9:13], byteorder='big')
        x = int.from_bytes(msg[13:14], byteorder='big')
        y = int.from_bytes(msg[14:15], byteorder='big')
        package = LOGIN_REPLY(Error_code, HP, EXP, x, y)
        if package.message==None:
            return None
        return package

    #move
    @staticmethod
    def  on_move(msg):
        Direction=int.from_bytes(msg[4:5], byteorder='big')
        package=MOVE(Direction)
        if package.message==None:
            return None
        return package

    #move_notify
    @staticmethod
    def on_move_notify(msg):
        for i in range(4,14):
            if msg[i] == 0:
                player_name = msg[4:i].decode()
                break
        else:
            player_name=msg[4:14].decode()

        if player_name.isalnum()==False:
            return None

        x=int.from_bytes(msg[14:15], byteorder='big')
        y = int.from_bytes(msg[15:16], byteorder='big')
        HP=int.from_bytes(msg[16:20], byteorder='big')
        EXP=int.from_bytes(msg[20:24], byteorder='big')
        package = MOVE_NOTIFY(player_name,x,y,HP,EXP)
        if package.message==None:
            return None
        return package

    @staticmethod
    def on_attack(msg):
        for i in range(4, len(msg)):
            if msg[i] == 0:
                Victim_name = msg[4:i].decode()
                break
        if  Victim_name.isalnum()==False:
            return None
        package = ATTACK(Victim_name)
        if package.message==None:
            return None
        return package

    @staticmethod
    def on_attack_notify(msg):
        for i in range(4,14):
            if msg[i] == 0:
                Attacker_name = msg[4:i].decode()
                break
        else:
            Attacker_name=msg[4:14].decode()

        for i in range(14, 24):
            if msg[i] == 0:
                Victim_name = msg[14:i].decode()
                break
        else:
            Victim_name = msg[14:24].decode()

        if Attacker_name.isalnum() ==False or Victim_name.isalnum()==False:
            return None

        Damage=int.from_bytes(msg[24:25], byteorder='big')
        HP=int.from_bytes(msg[25:29], byteorder='big')
        package = ATTACK_NOTIFY(Attacker_name,Victim_name,Damage,HP)
        if package.message==None:
            return None
        return package

    @staticmethod
    def on_speak(msg):
        for i in range(4,len(msg)):
            if msg[i] == 0:
                speak_Msg=msg[4:i].decode()
                break
        else:
            speak_Msg = msg[4:].decode()
        if speak_Msg.isprintable()==False:
            return None
        package = SPEAK(speak_Msg)
        if package.message==None:
            return None
        return package

    @staticmethod
    def on_speak_notify(msg):
        for i in range(4,14):
            if msg[i] == 0:
                Broadcaster_name = msg[4:i].decode()
                break
        else:
            Broadcaster_name=msg[4:14].decode()

        for i in range(14,len(msg)):
            if msg[i]==0:
                speak_Msg = msg[14:i].decode()
                break
        else:
            speak_Msg = msg[14:].decode()

        if Broadcaster_name.isalnum()==False or speak_Msg.isprintable()==False:
            return None

        package = SPEAK_NOTIFY(Broadcaster_name,speak_Msg)
        if package.message==None:
            return None
        return package

    @staticmethod
    def on_logout(msg):
        package = LOGOUT()
        return package

    @staticmethod
    def on_logout_notify(msg):
        for i in range(4, len(msg)):
            if msg[i] == 0:
                player_name = msg[4:i].decode()
                break
        if player_name.isalnum()==False:
            return None

        package = LOGOUT_NOTIFY(player_name)

        if package.message==None:
            return None
        return package

    @staticmethod
    def on_invalif_state(msg):
        Error_code=int.from_bytes(msg[4:5], byteorder='big')
        package = INVALID_STATE(Error_code)
        if package.message==None:
            return None
        return package

    #拆包,返回一个列表
    @staticmethod
    def unpack_msg(msg):
        if int.from_bytes(msg[0:1], byteorder = 'big')!=4 or len(msg)%4!=0:
            return None
        totalLength=int.from_bytes(msg[1:3], byteorder = 'big')
        Msgtype=int.from_bytes(msg[3:4], byteorder='big')
        packages=[]
        while len(msg)-totalLength>=0 and totalLength!=0:
            package=None
            if Msgtype == 1:
                package=operation.on_login_request(msg)

            elif Msgtype == 2:
                package=operation.on_login_reply(msg)

            elif Msgtype == 3:
                package= operation.on_move(msg)

            elif Msgtype == 4:
                package=operation.on_move_notify(msg)

            elif Msgtype == 5:
                package=operation.on_attack(msg)

            elif Msgtype == 6:
                package=operation.on_attack_notify(msg)

            elif Msgtype == 7:
                package=operation.on_speak(msg)

            elif Msgtype == 8:
                package=operation.on_speak_notify(msg)

            elif Msgtype == 9:
                package=operation.on_logout(msg)

            elif Msgtype == 10:
                package=operation.on_logout_notify(msg)

            elif Msgtype == 11:
                package=operation.on_invalif_state(msg)

            else:
                return None

            msg=msg[totalLength:]
            Msgtype = int.from_bytes(msg[3:4], byteorder='big')
            totalLength = int.from_bytes(msg[1:3], byteorder='big')
            if package==None:
                return None
            else:
                packages.append(package)
        return packages


if __name__ =='__main__':
    # l1=operation.make_packet(messages.MOVE,1)
    # l2=operation.make_packet(messages.LOGIN_REPLY,0,10,11,12,13)
    # packages=operation.unpack_msg(l1.message+l2.message)
    # print(len(packages))
    # print(packages[0].type)
    # print(packages[1].HP)
    message = '1234567890 ' * 23 + 'X'
    print(len(message))
    l=operation.make_packet(messages.SPEAK,message)
    print(len(l.message))
    packages = operation.unpack_msg(l.message)
    print(len(packages[0].message))
