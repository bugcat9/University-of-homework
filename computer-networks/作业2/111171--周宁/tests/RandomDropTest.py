import random
from BasicTest import *
"""
This tests random packet drops. We randomly decide to drop about half of the
packets that go through the forwarder in either direction.

Note that to implement this we just needed to override the handle_packet()
method -- this gives you an example of how to extend the basic test case to
create your own.
"""
class RandomDropTest(BasicTest):
    def handle_packet(self):
        for p in self.forwarder.in_queue:
            if random.choice([True, False]):
                self.forwarder.out_queue.append(p)
        # empty out the in_queue
        self.forwarder.in_queue = []
class Drop2Test(BasicTest):
    def __init__(self,forwarder, input_file):
        super(Drop2Test, self).__init__(forwarder, input_file)
        self.count=0
    def handle_packet(self):
        for p in self.forwarder.in_queue:
            if p.address==self.forwarder.receiver_addr and p.seqno==2 and self.count<3:
                self.count+=1
                continue
            self.forwarder.out_queue.append(p)
        # empty out the in_queue
        self.forwarder.in_queue = []
    
            
class unsoredTest(BasicTest):
    def handle_packet(self):
        if len(self.forwarder.in_queue)==2:
            for i in range(len(self.forwarder.in_queue)-1,-1,-1):
                self.forwarder.out_queue.append(self.forwarder.in_queue[i])
            self.forwarder.in_queue = []
        elif self.forwarder.in_queue[0].address==self.forwarder.receiver_addr:
            for p in self.forwarder.in_queue:
                self.forwarder.out_queue.append(p)
            self.forwarder.in_queue = []
                