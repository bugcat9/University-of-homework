import core
from random import random as rand

# There is one instance of this: NullAddress
# It can be used for non-routable packets and such.  It has the
# advantage over None of having a .name propery, so if you have
# code that prints entity.name, it won't choke.
NullAddress = core.NullAddress

# There are two loggers: one that has output from the simulator, and one
# that has output from you (simlog and userlog respectively).  These are
# Python logger objects, and you can configure them as you see fit.  See
# http://docs.python.org/library/logging.html for info.
simlog = core.simlog
userlog = core.userlog

def create_timer (seconds, target, recurring = True, pass_self = False,
                  args=(), kw={}):
    """
    A timer.
    Will call the callable /target/ every /seconds/ seconds, passing
    it the specified positional and keyword arguments.
    Will also pass itself as a final positional argument if pass_self
    is True.
    You can call .cancel() on the returned timer object to cancel it.
    """
    if recurring:
        return core.Timer(seconds, target=target,
                          passSelf=pass_self, args=args, kw=kw)
    else:
        return core.OneShot(seconds, target=target,
                            passSelf=pass_self, args=args, kw=kw)


def hsv_to_rgb (h, s, v, a = 1):
  """
  Convert hue, saturation, value (0..1) to RGBA.
  """
  import math
  f,i = math.modf(h * 6)
  p = v * (1-s)
  q = v * (1-f*s)
  t = v * (1-(1-f)*s)
  i %= 6
  if i == 0:   r,g,b = v,t,p 
  elif i == 1: r,g,b = q,v,p 
  elif i == 2: r,g,b = p,v,t 
  elif i == 3: r,g,b = p,q,v 
  elif i == 4: r,g,b = t,p,v 
  else: r,g,b = v,p,q 

  return [r,g,b,a]


class Packet (object):
  def __init__ (self, dst=NullAddress, src=NullAddress):
    """
    Create a packet from src to dst.
    If src is None, it is filled in with the sending Entity.
    If dst is None, nothing special happens, but when it gets
    to the next hop, the receiver probably won't know what to do with it!
    """
    self.src = src
    self.dst = dst
    self.ttl = 20   # TTL.  Decremented for each entity we go through.
    self.trace = [] # Trace of all entities we've been sent through.

    # When using NetVis, packets are visible, and you can set the color.
    # color is a list of red, green, blue, and (optionally) alpha values.
    # Each value is between 0 and 1.  alpha of 0 is transparent.  1 is opaque.
    self.outer_color = hsv_to_rgb(rand(), rand()*.25+.1, rand()*.95+.5,.75)
    self.inner_color = [0,0,0,0] # transparent

  def mark (self, entity):
    """
    You should never call this.  It's called by the framework to track
    where a packet has been, so that you can inspect it in self.trace.
    """
    self.trace.append(entity)

  def __repr__ (self):
    return "<%s from %s->%s>" % (self.__class__.__name__,
                                 self.src.name if self.src else None,
                                 self.dst.name if self.dst else None)


class Entity (object):
  """
  Base class for all entities (switches, hosts, etc.).
  """

  @classmethod
  def create (cls, name, *args, **kw):
    """
    A factory method on the class, which generates an instance.
    Use this instead of the normal instance creation mechanism.
    """
    return core.CreateEntity(name, cls, *args, **kw)

  def get_port_count (self):
    """
    Returns the number of ports this entity has.
    This function may appear to be unimplemented, but it does
    in fact work.
    """
    pass

  def handle_rx (self, packet, port):
    """
    Called by the framework when this Entity receives a packet.
    packet is a Packet (or subclass).
    port is the port number it arrived on.
    You probably want to override it.
    """
    pass

  def set_debug (self, *args):
    """
    Turns all arguments into a debug message for this Entity.
    This function may appear to be unimplemented, but it does
    in fact work.
    """
    pass

  def log (self, msg, *args, **kwargs):
    """
    This lets you log messages through the log system, which is a bit more
    elegant than a print statement.  This function is very much like the
    debug/info/warning/error/critical/exception methods in the Python
    logging module.  See http://docs.python.org/library/logging.html .
    A primary difference is that it defaults to debug level, but you
    specify another level by including a keyword argument with the name
    of the level you want, e.g, self.log("foo!", level="error").  The
    default level is "debug".
    See the main simulator.py for some more info about configuring the
    logs.
    Note that you can also use api.userlog.debug(...) and friends directly.

    This function may appear to be unimplemented, but it does
    in fact work.
    """
    pass

  def send (self, packet, port=None, flood=False):
    """
    Sends the packet out of a specific port or ports.
    If the packet's src is None, it will be set automatically
    to this Entity.
    port can be a numeric port number, or a list of port numbers.
    If flood is True, the meaning of port is reversed -- packets will
    be sent from all ports EXCEPT those listed.

    This function may appear to be unimplemented, but it does
    in fact work.
    """
    pass

  def remove (self):
    """
    Removes this entity from existence.

    This function may appear to be unimplemented, but it does
    in fact work.
    """
    pass

  def __repr__ (self):
    return "<" + self.__class__.__name__ + " " + str(self.name) + ">"


class HostEntity (Entity):
  """
  Hosts should inherit from this.
  This is just so the GUI knows to draw them differently.
  """

