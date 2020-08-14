from sim.api import *
from sim.basics import *
import time

class Hub (Entity):
  """ A simple hub -- floods all packets """

  def handle_rx (self, packet, port):
    """
    Just sends the packet back out of every port except the one it came
    in on.
    """
    self.send(packet, port, flood=True)
