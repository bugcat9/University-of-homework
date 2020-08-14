

from Package.Constants import messages

class Package:
    def __init__(self,ver,length):
        self.type=None
        self.message=None
        self.ver=ver
        self.len=length
#登陆请求
class LOGIN_REQUEST(Package):
    def __init__(self,*args):
        super().__init__(4,16)

        self.name=args[0]
        self.type=messages.LOGIN_REQUEST
        #名字必须是string
        if type(self.name)!=str:
            return
        length = len(self.name)
        #wow的包，制造
        #4代表版本号，16代表长度，1代表包类型，name是包内容，剩下的是补全0
        self.message=int(4).to_bytes(1,byteorder = 'big')+int(16).to_bytes(2,byteorder = 'big')+int(1).to_bytes(1,byteorder = 'big')\
                     +self.name.encode()+int(0).to_bytes(12-length,byteorder = 'big')
#login回复包
class LOGIN_REPLY(Package):
    def __init__(self,*args):
        super().__init__(4,16)
        self.type = messages.LOGIN_REPLY
        self.Error_code=args[0]     #都是int类型
        self.HP=args[1]
        self.EXP=args[2]
        self.x=args[3]
        self.y=args[4]

        if type(self.Error_code)!=int or type(self.HP)!=int or type(self.EXP)!=int or type(self.x)!=int or type(self.y)!=int:
            return
        #制造msg，用于发送数据
        self.message=int(4).to_bytes(1,byteorder = 'big')+int(16).to_bytes(2,byteorder = 'big')+int(2).to_bytes(1,byteorder = 'big')\
                        +self.Error_code.to_bytes(1,byteorder = 'big')+self.HP.to_bytes(4,byteorder = 'big')+self.EXP.to_bytes(4,byteorder = 'big')\
                        +self.x.to_bytes(1,byteorder = 'big')+self.y.to_bytes(1,byteorder = 'big')+int(0).to_bytes(1,byteorder = 'big')
#移动包
class MOVE(Package):
    def __init__(self, *args):
        super().__init__(4,8)
        self.type=messages.MOVE
        self.Direction=args[0]      #int类型
        if type(self.Direction)!=int or self.Direction>3 or self.Direction<0:
            return
        self.message = int(4).to_bytes(1, byteorder='big') + int(8).to_bytes(2, byteorder='big') + int(3).to_bytes(1,byteorder='big')\
        +self.Direction.to_bytes(1, byteorder='big')+int(0).to_bytes(3,byteorder = 'big')

#移动的广播包
class MOVE_NOTIFY(Package):
    def __init__(self, *args):
        super().__init__(4,24)
        self.type=messages.MOVE_NOTIFY
        self.player_name=args[0]
        self.x=args[1]
        self.y=args[2]
        self.HP=args[3]
        self.EXP=args[4]

        if type(self.player_name)!=str or type(self.x)!=int or type(self.y)!=int  or type(self.HP)!=int or type(self.EXP)!=int:
            return
        length = len(self.player_name)
        self.message = int(4).to_bytes(1, byteorder='big') + int(24).to_bytes(2, byteorder='big') + int(4).to_bytes(1,byteorder='big') \
            +self.player_name.encode()+int(0).to_bytes(10-length,byteorder = 'big')+self.x.to_bytes(1,byteorder='big')+self.y.to_bytes(1,byteorder = 'big')\
            +self.HP.to_bytes(4,byteorder = 'big')+self.EXP.to_bytes(4,byteorder = 'big')

# 攻击包
class ATTACK(Package):
    def __init__(self, *args):
        super().__init__(4,16)
        self.type=messages.ATTACK
        self.Victim_name=args[0]
        if type(self.Victim_name)!=str:
            return
        length = len(self.Victim_name)
        self.message = int(4).to_bytes(1, byteorder='big') + int(16).to_bytes(2, byteorder='big') + int(5).to_bytes(1,byteorder='big') \
                       + self.Victim_name.encode() + int(0).to_bytes(12 - length, byteorder='big')

class ATTACK_NOTIFY(Package):
    def __init__(self, *args):
        super().__init__(4,32)
        self.type=messages.ATTACK_NOTIFY
        self.Attacker_name=args[0]
        self.Victim_name=args[1]
        self.Damage=args[2]
        self.HP=args[3]

        if type(self.Attacker_name)!=str or type(self.Victim_name)!=str or type(self.Damage)!=int or type(self.HP)!=int:
            return
        length1 = len(self.Attacker_name)
        length2 = len(self.Victim_name)
        self.message= int(4).to_bytes(1, byteorder='big') + int(32).to_bytes(2, byteorder='big') + int(6).to_bytes(1,byteorder='big') \
                    +self.Attacker_name.encode()+int(0).to_bytes(10 - length1, byteorder='big')\
                      +self.Victim_name.encode()+int(0).to_bytes(10 - length2, byteorder='big')\
                    +self.Damage.to_bytes(1,byteorder='big')+self.HP.to_bytes(4,byteorder = 'big')+int(0).to_bytes(3, byteorder='big')


class SPEAK(Package):
    def __init__(self, *args):
        super().__init__(4,-1)
        self.type=messages.SPEAK
        self.Msg=args[0]        #这个是说话的msg,和包自身的message不相同
        if type(self.Msg)!=str or len(self.Msg)>255:
            return

        #将长度变成4的倍数
        if len(self.Msg)%4==0:
            length=len(self.Msg)+4
            self.message = int(4).to_bytes(1, byteorder='big') + int(length).to_bytes(2, byteorder='big') + int(7).to_bytes(1, byteorder='big') \
                           + self.Msg.encode()
        else:
            length=(len(self.Msg)//4+1)*4+4
            self.message = int(4).to_bytes(1, byteorder='big') + int(length).to_bytes(2, byteorder='big') + int(7).to_bytes(1, byteorder='big') \
                           + self.Msg.encode() + int(0).to_bytes(4 - (len(self.Msg) % 4), byteorder='big')

        self.len=length


class SPEAK_NOTIFY(Package):
    def __init__(self, *args):
        super().__init__(4,-1)
        self.type = messages.SPEAK_NOTIFY
        self.Broadcaster_name=args[0]
        self.Msg=args[1]

        if type(self.Broadcaster_name)!=str or type(self.Msg)!=str:
            return
        length1 = len(self.Broadcaster_name)
        length2 = len(self.Msg)
        # 将包变成4的倍数
        if length2%4==0:
            self.message = int(4).to_bytes(1, byteorder='big') + int(16 + length2).to_bytes(2, byteorder='big') + int(8).to_bytes(1, byteorder='big') \
                     +self.Broadcaster_name.encode()+int(0).to_bytes(10-length1,byteorder='big')\
                     +self.Msg.encode()+int(0).to_bytes(2, byteorder='big')
            self.len=16+length2
        else:
            l=((16+length2)//4+1)*4
            m=4-(length2%4)
            self.message = int(4).to_bytes(1, byteorder='big') + int(l).to_bytes(2, byteorder='big') + int(
                8).to_bytes(1, byteorder='big') \
                           + self.Broadcaster_name.encode() + int(0).to_bytes(10 - length1, byteorder='big') \
                           + self.Msg.encode()+int(0).to_bytes(m+2, byteorder='big')
            self.len=l


class LOGOUT(Package):
    def __init__(self, *args):
        super().__init__(4,4)
        self.type =messages.LOGOUT
        self.message = int(4).to_bytes(1, byteorder='big') + int(4).to_bytes(2, byteorder='big') + int(9).to_bytes(1,byteorder='big')

class LOGOUT_NOTIFY(Package):
    def __init__(self, *args):
        super().__init__(4,16)
        self.type=messages.LOGOUT_NOTIFY
        self.player_name=args[0]
        if type(self.player_name)!=str:
            return
        length=len(self.player_name)
        self.message = int(4).to_bytes(1, byteorder='big') + int(16).to_bytes(2, byteorder='big') + int(10).to_bytes(1,byteorder='big')\
                       +self.player_name.encode() + int(0).to_bytes(12 - length, byteorder='big')

class INVALID_STATE(Package):
    def __init__(self, *args):
        super().__init__(4,8)
        self.type=messages.INVALID_STATE
        self.Error_code=args[0]
        if type(self.Error_code)!=int:
            return
        self.message = int(4).to_bytes(1, byteorder='big') + int(8).to_bytes(2, byteorder='big') + int(11).to_bytes(1,byteorder='big')\
            +self.Error_code.to_bytes(1,byteorder='big')+int(0).to_bytes(3,byteorder='big')


if __name__ =='__main__':
    '''
    print('111111111111');
    name="ee122"
    b=name.encode()
    a=struct.pack('%ds'%len(name),name.encode())
    print(len(b))
    print(struct.unpack('%ds'%len(name),a))
    '''
    s=[1]*254
    l=MOVE(1)
    print(l.message)


