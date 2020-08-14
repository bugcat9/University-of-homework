from sim.api import *
from sim.basics import *

'''
Create your RIP router in this file.
'''
class RIPRouter (Entity):
    def __init__(self):
        # Add your code here!
        pass

    def handle_rx (self, packet, port):
        # Add your code here!
        raise NotImplementedError
