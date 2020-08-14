
import sim
from sim.core import CreateEntity, topoOf
from sim.basics import BasicHost
from hub import Hub
import sim.topo as topo

def create (switch_type = Hub, host_type = BasicHost):
   

    switch_type.create('s1')
    switch_type.create('s2')
    switch_type.create('s3')
    switch_type.create('s4')
 

    host_type.create('h1a')
    host_type.create('h1b')
    host_type.create('h2a')
    host_type.create('h2b')

    topo.link(s1, h1a)
    topo.link(s1, h1b)
    topo.link(s2, h2a)
    topo.link(s2, h2b)

  

    topo.link(s1, s4)
    topo.link(s4, s3)
    topo.link(s3, s2)
