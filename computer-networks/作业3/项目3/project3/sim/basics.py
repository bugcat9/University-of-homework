from api import *

class BasicHost (HostEntity):
  """ Basic host with a ping method """

  def ping (self, dst, data=None):
    """ Sends a Ping packet to dst. """
    self.send(Ping(dst, data=data), flood=True)

  def handle_rx (self, packet, port):
    """
    Silently drops messages to nobody.
    Warns about received messages to someone besides itself.
    Prints received messages.
    Returns Pings with a Pong.
    """
    if packet.dst is NullAddress:
      # Silently drop messages not to anyone in particular
      return

    trace = ','.join((s.name for s in packet.trace))

    if packet.dst is not self:
      self.log("NOT FOR ME: %s %s" % (packet, trace), level="WARNING")
    else:
      self.log("rx: %s %s" % (packet, trace))
      if type(packet) is Ping:
        # Trace this path
        import core
        core.events.highlight_path([packet.src] + packet.trace)
        # Send a pong response
        self.send(Pong(packet), port)


class Ping (Packet):
  """ A Ping packet """
  def __init__ (self, dst, data=None):
    Packet.__init__(self, dst=dst)
    self.data = data
    self.outer_color[3] = 1 # Full opacity
    self.inner_color = [1,1,1,1] # white

  def __repr__ (self):
    d = self.data
    if d is not None:
      d = ': ' + str(d)
    else:
      d = ''
    return "<Ping %s->%s ttl:%i%s>" % (self.src.name, self.dst.name, self.ttl, d)


class Pong (Packet):
  """
  A Pong packet.  It's a returned Ping.  The original Ping is in
  the .original property.
  """
  def __init__ (self, original):
    Packet.__init__(self, dst=original.src)
    self.original = original

    # Flip colors from original
    self.outer_color = original.inner_color
    self.inner_color = original.outer_color

  def __repr__ (self):
    return "<Pong " + str(self.original) + ">"

#class DiscoveryPacket (Packet):
#    """
#    A "link up/down" packet.
#    """
#    def __init__(self, src, is_link_up):
#        Packet.__init__(self, src=src)
#        self.is_link_up = is_link_up
#
#    def __repr__ (self):
#        return "<%s from %s->%s, %s>" % (self.__class__.__name__,
#                                 self.src.name if self.src else None,
#                                 self.dst.name if self.dst else None,
#                                 self.is_link_up)

# Changed to enable link latency - Kaifei Chen(kaifei@berkeley.edu)      
class DiscoveryPacket (Packet):
    """
    A "link latency change" packet.
    latency should be float("inf") if the link is down.
    """
    def __init__(self, src, latency):
        Packet.__init__(self, src=src)
        self.latency = latency
        self.is_link_up = (latency != None and latency != float("inf"))

    def __repr__ (self):
        return "<%s from %s->%s, %s, %s>" % (self.__class__.__name__,
                                 self.src.name if self.src else None,
                                 self.dst.name if self.dst else None,
                                 self.latency,
                                 self.is_link_up)

class RoutingUpdate (Packet): 
    """
    A Routing Update message to use with your RIPRouter implementation.
    """

    def __init__(self):
        Packet.__init__(self)
        self.paths = {}

    def add_destination(self, dest, distance):
        """
        Add a destination to announce, along with senders distance to that dest.
        """
        self.paths[dest] = distance

    def get_distance(self, dest):
        """
        Get the distance to the specified destination.
        """
        return self.paths[dest]

    def all_dests(self):
        """
        Get a list of all destinations with paths announced in this message.
        """
        return self.paths.keys()

    def str_routing_table(self):
        return str(self.paths) 
