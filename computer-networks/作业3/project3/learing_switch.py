from sim.api import *
from sim.basics import *

'''
Create your RIP router in this file.
'''
class LearningSwitch (Entity):
    def __init__(self):
        # Add your code here!
        self.routingTable=dict()

    def handle_rx (self, packet, port):
        # Add your code here!
        #raise NotImplementedError
        if self.routingTable.get(packet.dst)!=None:
            if  packet.dst!=self:
                port=self.routingTable.get(packet.dst)
                self.send(packet, port, flood=False)
        else:
            self.routingTable[packet.src]=port
            self.send(packet, port, flood=True)
