from sim.api import *
from sim.basics import *
'''
Create your RIP router in this file.
'''
from copy import deepcopy
from _ast import Or
from operator import or_

class RIPRouter (Entity):
    def __init__(self):
        # Add your code here!
        self.routingTable=dict()
        self.DistanceTable=dict()
        self.table=dict()

    def handle_rx (self, packet, port):
        # print packet,self
        #create    a     new   packet
        RtUpdatepacket=RoutingUpdate()
        RtUpdatepacket.src=self
        # Add your code here!
        if isinstance(packet, DiscoveryPacket) :
            if self.routingTable.get(packet.src)==None and packet.is_link_up==True:
                self.routingTable[packet.src]=port
                self.DistanceTable[packet.src]=packet.latency
                
                if packet.src in self.table:
                    self.table[packet.src][packet.src]=self.DistanceTable[packet.src]
                else:
                    self.table[packet.src]=dict()
                    self.table[packet.src][packet.src]=self.DistanceTable[packet.src]
                    
               
                RtUpdatepacket.add_destination(packet.src, self.DistanceTable[packet.src])
                self.send(RtUpdatepacket,port,flood=True)    
                
            elif self.routingTable.get(packet.src)!=None and packet.is_link_up==False:
                port1=self.routingTable.pop(packet.src)
                self.DistanceTable.pop(packet.src)
                RtUpdatepacket.add_destination(packet.src,float("inf"))
                for dest in list(self.routingTable):
                    if self.routingTable[dest]==port1:
                        self.routingTable.pop(dest)
                        self.DistanceTable.pop(dest)
                        RtUpdatepacket.add_destination(dest,float("inf"))
                        
                del self.table[packet.src]
                
                for neighbor in self.table:
                    for dest in self.table[neighbor]:
                        if dest not in self.routingTable:
                            self.routingTable[dest]=self.routingTable[neighbor]
                            self.DistanceTable[dest]=self.table[neighbor][dest]
                            RtUpdatepacket.add_destination(dest,self.table[neighbor][dest])
                        elif self.table[neighbor][dest]<self.DistanceTable[dest]:
                            self.routingTable[dest]=self.routingTable[neighbor]
                            self.DistanceTable[dest]=self.table[neighbor][dest]
                            RtUpdatepacket.add_destination(dest,self.table[neighbor][dest])
                
                self.send(RtUpdatepacket,port,flood=True)    
            
            
        elif isinstance(packet, RoutingUpdate):
            #print packet.all_dests()
            isupdate=False
            RtUpdatepacket=RoutingUpdate()
            RtUpdatepacket.src=self
            if packet.src not in self.routingTable:
                return 
            
            for dest in packet.all_dests():
                if dest==self:
                    continue
                
                if  dest not in self.routingTable:
                        self.routingTable[dest]=self.routingTable[packet.src]
                        self.DistanceTable[dest]=packet.get_distance(dest)+self.DistanceTable[packet.src]
                        RtUpdatepacket.add_destination(dest, self.DistanceTable[dest])
                        isupdate=True
                else:
                    if self.DistanceTable[dest]>packet.get_distance(dest)+self.DistanceTable[packet.src]:
                        self.routingTable[dest]=self.routingTable[packet.src]
                        self.DistanceTable[dest]=packet.get_distance(dest)+self.DistanceTable[packet.src]
                        RtUpdatepacket.add_destination(dest, self.DistanceTable[dest])
                        isupdate=True
                    elif self.routingTable[dest]==self.routingTable[packet.src] and packet.get_distance(dest)+self.DistanceTable[packet.src]>self.DistanceTable[dest]:
                        self.routingTable.pop(dest)
                        self.DistanceTable.pop(dest)
                        RtUpdatepacket.add_destination(dest, float("inf"))
                        isupdate=True
                if  packet.src in self.table:
                    self.table[packet.src][dest]=packet.get_distance(dest)+self.DistanceTable[packet.src]
            
            if isupdate==True:
            #create    a     new   packet
                self.send(RtUpdatepacket,port,flood=True)
                
            isupdate=False
            RtUpdatepacket=RoutingUpdate()
            RtUpdatepacket.src=self
            for neighbor in self.table:
                    for dest in self.table[neighbor]:
                        if dest not in self.routingTable:
                            self.routingTable[dest]=self.routingTable[neighbor]
                            self.DistanceTable[dest]=self.table[neighbor][dest]
                            RtUpdatepacket.add_destination(dest,self.table[neighbor][dest])
                            isupdate=True
                        elif self.table[neighbor][dest]<self.DistanceTable[dest]:
                            self.routingTable[dest]=self.routingTable[neighbor]
                            self.DistanceTable[dest]=self.table[neighbor][dest]
                            RtUpdatepacket.add_destination(dest,self.table[neighbor][dest])    
                            isupdate=True
            if isupdate==True:
            #create    a     new   packet
                self.send(RtUpdatepacket,port,flood=True)
        else:
            port=self.routingTable.get(packet.dst)
            self.send(packet, port, flood=False)
        
     
         
