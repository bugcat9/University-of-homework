import random
from core import world
from core import events

#default_latency = 0.5

# Make default latency 1 - Kaifei Chen(kaifei@berkeley.edu)
default_latency = 1

class Cable (object):
  """ 
  Entities can be connected by a Cable.  If no Cable is used, there's
  a default behavior.
  Note that a Cable is unidirectional.  In many cases, you'll actually
  want to install an identical Cable in both directions.
  """
  def initialize (self, src, srcport, dst, dstport):
    """ Called to set up the ends. """
    for a in ['src','srcPort','srcEntity',
              'dst','dstPort','dstEntity']:
      setattr(self, a, None)
    self.src = src
    self.srcPort = srcport
    self.srcEnt = src.entity
    self.dst = dst
    self.dstPort = dstport
    self.dstEnt = dst.entity

  def transfer (self, packet):
    """ Implement this in subclasses. """
    pass

  def get_connections (self):
    """ Return the list of things we're connected to. """
    pass


class BasicCable (Cable):
  """
  This is a plain old connection between two Entities.  It just
  transfers the data after some amount of time.
  """
  def __init__ (self, latency = None):
    if latency is not None:
      self.latency = latency
    else:
      self.latency = default_latency

  def transfer (self, packet):
    def rx ():
      packet.mark(self.dstEnt) #FIXME: do this somewhere more convenient
      self.dstEnt.handle_rx(packet, self.dstPort)

    world.doLater(self.latency, rx)

    events.packet(self.srcEnt.name, self.dstEnt.name, packet, self.latency)


class UnreliableCable (BasicCable):
  """
  Very much like BasicCable except it drops packets sometimes.
  """
  @classmethod
  def pair (cls, latency = None, drop = .1, drop_reverse = None):
    """
    Create a pair of these (one for each direction)
    drop is the drop rate for A to B.
    drop_reverse is the drop rate for B to A (defaults to the same as drop)
    """
    if drop_reverse is None: drop_reverse = drop
    return ( cls(latency = latency, drop = drop),
             cls(latency = latency, drop = drop_reverse) )

  def __init__ (self, latency = None, drop = .1):
    """
    Drop 10% by default
    """
    super(UnreliableCable, self).__init__(latency = latency)
    self.drop = drop

  def transfer (self, packet):
    if random.random() >= self.drop:
      super(UnreliableCable, self).transfer(packet)
    else:
      events.packet(self.srcEnt.name, self.dstEnt.name, packet,
                    self.latency, drop=True)

