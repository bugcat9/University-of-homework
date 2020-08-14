from enum import Enum

class messages(Enum):
    LOGIN_REQUEST = 1
    LOGIN_REPLY=2
    MOVE=3
    MOVE_NOTIFY=4
    ATTACK=5
    ATTACK_NOTIFY=6
    SPEAK=7
    SPEAK_NOTIFY=8
    LOGOUT=9
    LOGOUT_NOTIFY=10
    INVALID_STATE=11
    MAX_MESSAGE=12


class direction(Enum):
    NORTH = 0
    SOUTH=1
    EAST=2
    WEST=3