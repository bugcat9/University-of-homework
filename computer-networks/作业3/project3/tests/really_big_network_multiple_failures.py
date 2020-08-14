
import sys
sys.path.append('.')

import sim
from sim.basics import BasicHost, RoutingUpdate, DiscoveryPacket
import sim.topo as topo
from sim.api import *
from rip_router import RIPRouter
import sim.topo as topo
import os
import time



class FakeEntity (Entity):
    def __init__(self, expected, to_announce, time):
        self.expect = expected
        self.announce = to_announce
        self.num_rx = 0
        if(self.announce):
            self.timer = create_timer(time, self.send_announce)    

    def handle_rx(self, packet, port):
        if(self.expect):
            if(isinstance(packet, RoutingUpdate)):
                self.num_rx += 1
                if(self.expect[0] in packet.all_dests() and packet.get_distance(self.expect[0]) == (self.expect[1])):
                    os._exit(0)
                elif(self.num_rx > 3):
                    os._exit(50)
                   
    def send_announce(self):
        if(self.announce):
            update = RoutingUpdate()
            update.add_destination(self.announce[0], self.announce[1])
            self.send(update, flood=True)

class ReceiveEntity (Entity):
    def __init__(self, expected, to_announce, time):
        self.expect = expected
        self.announce = to_announce
        self.num_rx = 0
        if(self.announce):
            self.timer = create_timer(time, self.send_announce)    

    def handle_rx(self, packet, port):
        if(not isinstance(packet, RoutingUpdate) and not isinstance(packet, DiscoveryPacket)):
            self.num_rx += 1
            if(not self.expect):
                print("Sent packet to unexpected destination!")
                os._exit(50)
            else:
                if(len(packet.trace) != len(self.expect) + 1):
                    print("Incorrect packet path!") 
                    print(packet.trace)
                    return
                
                for i in range(len(self.expect)):
                    if(packet.trace[i] != self.expect[i]):
                        print("Incorrect packet path!")
                        print(packet.trace[i])
                        print(self.expect[i])
                        return
                        #os._exit(50)
                os._exit(0) 
    
    def send_announce(self):
        if(self.announce):
            update = RoutingUpdate()
            update.add_destination(self.announce[0], self.announce[1])
            self.send(update, flood=True)

def create (switch_type = RIPRouter, host_type = BasicHost):
    """
    Creates a topology with loops that looks like:
    h1a    s4--s5           h2a
       \  /      \          /
        s1        s2--s6--s7
       /  \      /     \    \    
    h1b    --s3--       s8--s9--h2b
    """
    switch_type.create('s1')
    switch_type.create('s2')
    switch_type.create('s3')
    switch_type.create('s4')
    switch_type.create('s5')

    switch_type.create('s6')
    switch_type.create('s7')
    switch_type.create('s8')
    switch_type.create('s9')


    host_type.create('h1a')
    host_type.create('h1b')
    host_type.create('h2a')
    host_type.create('h2b')


    ReceiveEntity.create('sneakylistener', [s7, s9, s8, s6, s2, s5, s4, s1] , [h1a, 1], 5)

    topo.link(sneakylistener, h1a)
    topo.link(sneakylistener, s1)
    topo.link(s1, h1b)
    topo.link(s2, s6)
    topo.link(s6, s8)
    topo.link(s8, s9)
    topo.link(s9, h2b)
    topo.link(s6, s7)
    topo.link(s7, s9)
    topo.link(s7, h2a)
    

    topo.link(s1, s3)
    topo.link(s3, s2)

    topo.link(s1, s4)
    topo.link(s4, s5)
    topo.link(s5, s2)

import sim.core
from rip_router import RIPRouter as switch

import sim.api as api
import logging
api.simlog.setLevel(logging.DEBUG)
api.userlog.setLevel(logging.DEBUG)

_DISABLE_CONSOLE_LOG = True

create(switch)
start = sim.core.simulate
start()
time.sleep(30)
topo.unlink(s1, h1b)
topo.unlink(s7, s6)
topo.unlink(s3, s2)
time.sleep(15)
h2a.ping(h1a)
print("first ping sent")
time.sleep(15)
h2a.ping(h1a)
print("second ping sent")
time.sleep(30)
h2a.ping(h1a)
print("third ping sent")
time.sleep(30)
print("TIMEOUT")
os._exit(50)
