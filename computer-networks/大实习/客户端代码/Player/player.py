# 玩家类，存储玩家的名字，位置，经验值和血量
from Package.Constants import *


class player:
    def __init__(self, name):
        self.name = name
        self.HP = 10
        self.EXP = 10
        self.x = 10
        self.y = 10

    def setHP(self, hp):
        self.HP = hp
        pass

    def getHP(self):
        return self.HP

    def setEXP(self, exp):
        self.EXP = exp
        pass

    def getEXP(self):
        return self.EXP

    def setLocal(self, x, y):
        self.x = x
        self.y = y

    def setx(self, x):
        self.x = x

    def sety(self, y):
        self.y = y

    def getx(self):
        return self.x

    def gety(self):
        return self.y

    # 移动，参数为direction枚举
    def move(self, direct):
        if direct == direction.EAST:
            self.setx(self.x + 3)
            # 向右移动3个坐标
            # 须留意，后期可改为：移动距离可设置
        elif direct == direction.NORTH:
            self.sety(self.y + 3)
        elif direct == direction.SOUTH:
            self.sety(self.y - 3)
        elif direct == direction.WEST:
            self.setx(self.x - 3)

    def moveto(self, x, y):
        self.setLocal(x, y)

    def be_attacked(self, damage):
        self.setHP(self.getHP() - damage)
