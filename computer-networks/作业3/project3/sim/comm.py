"""
This simulator can call methods in this class to inform external
programs that various events have occurred.
"""

class NullInterface (object):
  """ Interface that does nothing / base class """
  def send_console(self, text):
    pass

  def send_console_more (self, text):
    pass

  def send_log (self, record):
    pass
  
  def send_entity_down (self, name):
    pass

  def send_entity_up (self, name, kind):
    pass

  def send_link_up (self, srcid, sport, dstid, dport):
    pass
  
  def packet (self, n1, n2, packet, duration, drop=False):
    pass

  def send_link_down (self, srcid, sport, dstid, dport):
    pass
  
  def highlight_path (self, nodes):
    """ Sends a path to the GUI to be highlighted """
    pass

  def set_debug (self, nodeid, msg):
    pass

interface = NullInterface
