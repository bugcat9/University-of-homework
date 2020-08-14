"""
The core of the network simulator.
Students should not need to inspect this module at all, and direct
utilization of functionality herein is liable to make you fail a
project.  Also, pieces of the implementation will change during
grading.
"""

import comm_tcp as interface
#import comm_udp as interface
#import comm as interface

import sys
import sim
import copy
import threading
import Queue
import time
import weakref

import logging
import traceback

class EventLogger (logging.Handler):
  _attributes = [
    'created','filename','funcName','levelname','levelno','lineno',
    'module','msecs','name','pathname','process','processName',
    'relativeCreated','thread','threadName','args',
  ]

  #  def __init__ (self, *args, **kw):
  #  logging.Handler.__init__(self, *args, **kw)

  def emit (self, record):
    o = {'message' : self.format(record)}
    o['type'] = 'log'
    if True:
      for attr in self._attributes:
        if hasattr(record, attr):
          o[attr] = getattr(record, attr)
      fmt = self.formatter
      if fmt is None:
        fmt = logging._defaultFormatter
      o['asctime'] = fmt.formatTime(record)
      if record.exc_info:
        o['exc_info'] = [str(record.exc_info[0]),
                         str(record.exc_info[1]),
                         traceback.format_tb(record.exc_info[2],1)]
        o['exc'] = traceback.format_exception(*record.exc_info)
    events.send_log(o)

if not sys.modules['__main__'].__dict__.get("_DISABLE_CONSOLE_LOG", False):
  #print "Enabling console log"
  logging.basicConfig(level=logging.DEBUG)
  pass
else:
  #print "Disabling console log"
  pass
logging.getLogger().addHandler(EventLogger())
simlog = logging.getLogger("simulator")
userlog = logging.getLogger("user")


import code

class stdout_wrapper:
  def write (self, s):
    sys.__stdout__.write(s)
    events.send_console(s)

if sys.modules['__main__'].__dict__.get("_ENABLE_GUI", False):
  sys.stdout = stdout_wrapper()
  sys.stderr = sys.stdout

class Interp (code.InteractiveInterpreter):
  def write (self, s):
    events.send_console(s)

interp = Interp(sys.modules['__main__'].__dict__)

class NullAddressType (object):
  """
  There is one instance of this: NullAddress
  It can be used for non-routable packets and such.  It has the
  advantage over None of having a .name propery, so if you have
  code that prints entity.name, it won't choke.
  """
  def __init__ (self):
    self.name = "NullAddress"

  def __repr__ (self):
    return "<NullAddress>"

NullAddress = NullAddressType()

class Timer (object):
  """ It's a timer.
  You should just create this with api.create_timer()."""
  def __init__ (self, seconds, target=None, args=(), kw={}, passSelf=False):
    self.seconds = seconds
    world.doLater(seconds, self.timeout)
    self.func = target
    self.stopped = False
    self.args = list(args)
    self.kw = dict(kw)
    if passSelf:
      self.args = [self] + self.args

  def cancel (self):
    self.stopped = True

  def timer (self):
    if self.func:
      self.func(*self.args,**self.kw)

  def timeout (self):
    if self.stopped: return
    try:
      rv = self.timer()
      if rv is not False:
        world.doLater(self.seconds, self.timeout)
    except:
      simlog.error("Exception while executing a timer")
      traceback.print_exc()


class OneShot (Timer):
  """ It's a single-shot timer.
  You should just create this with api.create_timer()."""
  def timeout (self):
    if self.stopped: return
    try:
      self.timer()
    except:
      pass


class World (object):
  """ Mostly this dispatches events in the simulator. """
  def __init__ (self):
    self.queue = Queue.PriorityQueue()
    self._thread = None
    self._count = 0

    # When the world isn't running, items are put in the prelist.
    # They're added to the queue when the world is started, and
    # their start times are adjusted so that they are relative to
    # when the world was started, NOT to when they were added.
    self._prelist = []

  def _real_doLater (_self, _seconds, _method, *_args, **_kw):
    t = time.time() + _seconds
    _self.queue.put((t, _self._count, _method, _args, _kw))
    _self._count += 1

  def start (self):
    assert self._thread is None

    for a,b,c,d in self._prelist:
      self._real_doLater(a, b, *c, **d)
    self._prelist = []

    self._thread = threading.Thread(target=self.run)
    self._thread.daemon = True
    self._thread.start()

  def do (self, _method, *args, **kw):
    self.doLater(0, _method, *args, **kw)

  def doLater (_self, _seconds, _method, *_args, **_kw):
    if _self._thread is not None:
      _self._real_doLater(_seconds, _method, *_args, **_kw)
    else:
      _self._prelist.append((_seconds, _method, _args, _kw))

  def run (self):
    timeout = None
    waiting = Queue.PriorityQueue()

    while True:
      try:
        t = time.time()
        while not waiting.empty():
          o = waiting.get()
          if o[0] <= t:
            self.queue.put(o)
            timeout = None
          else:
            waiting.put(o)
            o = waiting.get()
            waiting.put(o)
            timeout = o[0] - t
            break
        #print "World waiting for",timeout

        o = self.queue.get(True, timeout)
      except:
        #print "empty"
        continue

      t = time.time()
      if o[0] > t:
        # Hasn't expired yet...
        #print "recycle"
        waiting.put(o)
        o = waiting.get()
        waiting.put(o)
        timeout = o[0] - t
        continue
      # Expired
      timeout = None
      if False:
        if hasattr(o[2], "im_self"):
          print o[2].im_self.__class__.__name__ + "." + o[2].im_func.__name__,
        else:
          print o[2],
        print o[3],o[4] if len(o[4]) else ''
      o[2](*o[3],**o[4])


class TopoNode (object):
  """ A container for an Entity that connects it to other Entities and
      provides some infrastructure functionality. """
  def __repr__ (self):
    e = str(self.entity)
    if e.startswith('<') and e.endswith('>'):
      e = e[1:-1]
    return "<T:" + str(self.entity) + ">"

  def get_ports (self):
    """ Returns (self, mynum, remote, remotenum) info about ports """
    o = []
    for n,p in enumerate(self.ports):
      if p is not None:
        o.append((self.entity.name,n,p.dstEnt.name,p.dstPort))
    return o

  def __init__ (self, numPorts = 0, growPorts =  True):
    self.ports = [None] * numPorts
    self.growPorts = growPorts
    self.entity = None

  def linkTo (self, topoEntity, cable = None, fillEmpty = True, latency = None):
    """
    You can specify a cable to use in several ways:
     None           Both directions use BasicCable
     Cable-Subclass Both directions use instances of Cable-Subclass
                    made with an empty argument list to the constructor
     (S->D,D->S)    A tuple.  Either end can be None (meaning to not
                    connect that direction), a Cable subclass (to get a
                    default instance), or a Cable instance.
    So the following are equivalent:
     a.linkTo(b, (C, None)); b.linkTo(a, (D, None))
      and
     a.linkTo(b, (C, D))
    """
    from cable import Cable, BasicCable
    if cable is None:
      cable = (BasicCable, BasicCable)
    elif isinstance(cable, Cable):
      raise RuntimeError("Can't share a single Cable in both directions!")
    elif isinstance(cable, tuple):
      pass
#    elif isinstance(cable, BidirectionalCable):
    else:
      cable = (cable, cable)

    def fixCableEnd (c, le, lp, re, rp):
      if c is None: c = BasicCable
      # Add latency if the c is BasicCable - Kaifei Chen(kaifei@berkeley.edu)
      if isinstance(c, type) and issubclass(c, BasicCable):
        c = c(latency=latency)
      elif isinstance(c, type) and issubclass(c, Cable):
        c = c()
      c.initialize(le, lp, re, rp)
      return c

    topoEntity = topoOf(topoEntity)
    def getPort (entity):
      if not fillEmpty or entity.ports.count(None) == 0:
        assert self.growPorts
        entity.ports.append(None)
        return len(entity.ports) - 1
      return entity.ports.index(None)

    assert topoEntity is not self

    remotePort = getPort(topoEntity)
    localPort = getPort(self)

    if cable[0] is not None:
      c = fixCableEnd(cable[0], self, localPort, topoEntity, remotePort)
      self.ports[localPort] = c

      #self.send(sim.basics.DiscoveryPacket(self.entity, True), localPort)
      
      # Get latency if c is BasicCable - Kaifei Chen(kaifei@berkeley.edu)
      l = c.latency if isinstance(c, BasicCable) else None  # latency
      self.send(sim.basics.DiscoveryPacket(self.entity, latency=l), localPort)

    if cable[1] is not None:
      c = fixCableEnd(cable[1], topoEntity, remotePort, self, localPort)
      topoEntity.ports[remotePort] = c

      #topoEntity.send(sim.basics.DiscoveryPacket(topoEntity.entity, True), remotePort)

      # Get latency if c is BasicCable - Kaifei Chen(kaifei@berkeley.edu)
      l = c.latency if isinstance(c, BasicCable) else None  # latency
      topoEntity.send(sim.basics.DiscoveryPacket(topoEntity.entity, latency=l), remotePort)

    world.doLater(.5, events.send_link_up, self.entity.name, localPort,
             topoEntity.entity.name, remotePort)

    return (localPort, remotePort)

  def unlinkTo (self, topoEntity):
    topoEntity = topoOf(topoEntity)
    def goDown (index):
      port = self.ports[index]
      if port is None: return
      other = port.dst
      otherPort = port.dstPort
      events.send_link_down(self.entity.name, index, other.entity.name, otherPort)
      
      #topoEntity.entity.handle_rx(sim.basics.DiscoveryPacket(self.entity, False), otherPort)
      #self.entity.handle_rx(sim.basics.DiscoveryPacket(topoEntity.entity, False), index)
      
      # Assign infinity to latency - Kaifei Chen(kaifei@berkeley.edu)
      topoEntity.entity.handle_rx(sim.basics.DiscoveryPacket(self.entity, latency=float("inf")), otherPort)
      self.entity.handle_rx(sim.basics.DiscoveryPacket(topoEntity.entity, latency=float("inf")), index)

      other.ports[otherPort] = None
      self.ports[index] = None

    remove = [index for index,value in enumerate(self.ports)
              if value is not None and value.dst is topoEntity]
    for index in remove:
      world.doLater(0.5, goDown, index)

  def isConnectedTo (self, other):
    other = topoOf(other)
    for p in self.ports:
      if p is None: continue
      if p.dst is other:
        return True
    return False

  def disconnect (self):
    for p in (port for port in self.ports if port):
      self.unlinkTo(p.dst)

  def send (self, packet, port, flood = False):
    """
    Port can be a port number or a list of port numbers.
    If flood is True, Port can be a port number NOT to flood out of
    or None to flood all ports.
    """
    packet.ttl -= 1
    if packet.ttl == 0:
      simlog.warning("Expired %s / %s", packet, ','.join(e.name for e in packet.trace))
      return
    if (packet.src is None) or (packet.src is NullAddress):
      packet.src = self.entity

    if not isinstance(port, list):
      ports = [port]
    elif port is None:
      ports = []
    else:
      ports = port

    if flood:
      ports = [p for p in  range(0, len(self.ports)) if p not in ports]

    for remote in ports:
      if remote >=0 and remote < len(self.ports):
        remote = self.ports[remote]
        if remote is not None:
          p = copy.copy(packet)
          import basics
          if isinstance(packet, basics.RoutingUpdate):
            p.paths = copy.copy(packet.paths)
          remote.transfer(p)


def _getByName (name):
  return topoOf(sys.modules['__builtin__'].__dict__.get(name, None))

topo = weakref.WeakValueDictionary()
def CreateEntity (_name, _kind, *args, **kw):
  """
  Creates an Entity of kind, where kind is an Entity subclass.
  name is the name for the entity (e.g., "s1").
  Additional arguments are pased to the new Entity's __init__().
  Returns the TopoNode containing the new Entity.
  """
  if _name in sys.modules['__builtin__'].__dict__:
    raise NameError(str(_name) + " already exists")
  import api

  e = _kind(*args, **kw)
  setattr(e, 'name', _name)
  numPorts = 0
  growPorts = True
  if hasattr(e, 'num_ports'):
    ports = e.num_ports
    growPorts = False

  te = TopoNode(numPorts, growPorts)
  te.entity = e

  kind = "host" if isinstance(e, api.HostEntity) else "switch"
  world.do(events.send_entity_up,e.name, kind)
  simlog.info(e.name+" up!")

  # Add working methods
  setattr(e, 'get_port_count', lambda : len(te.ports))
  def send (packet, port=None, flood=False):
    te.send(packet, port, flood)
  setattr(e, 'send', send)
  def set_debug (*args):
    #print e.name + ':', ' '.join((str(s) for s in args))
    world.do(events.set_debug,e.name, ' '.join((str(s) for s in args)))
  setattr(e, 'set_debug', set_debug)
  def log (msg, *args, **kw):
    level = "debug"
    if "level" in kw:
      level = kw["level"].lower()
      del kw["level"]
    if level not in ['debug', 'info', 'warning', 'error', 'critical', 'exception']:
      level = "debug"
    func = getattr(userlog, level)
    msg = "%s:" + msg # Black magic
    args = tuple([e.name] + list(args))
    func(msg, *args, **kw)
  setattr(e, 'log', log)

  for m in ['linkTo', 'unlinkTo', 'disconnect']:
    setattr(e, m, getattr(te, m))

  def remove ():
    te.disconnect()
    world.do(events.send_entity_down,_name)
    try:
      del sys.modules['__builtin__'].__dict__[_name]
    except:
      pass
  setattr(e, 'remove', remove)

  # Make a global variable with the right name
  #sys.modules['__main__'].__dict__[_name] = e
  #sim.__dict__[_name] = e
  sys.modules['__builtin__'].__dict__[_name] = e

  # This is so we can find its TopoNode
  topo[e] = te
  return e

def topoOf (entity):
  """ Get TopoNode that contains entity.  Students never use this. """
  if type(entity) is TopoNode:
    # We were actually passed a topo object
    return entity
  t = topo.get(entity, None)
  return t

world = World()
events = interface.interface()

def simulate ():
  """ Runs the simulator. """
  world.start()
