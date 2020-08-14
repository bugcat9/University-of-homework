"""
This is the communication interface for working with the NOX-derived GUI.
It may need a little love to work again, but it'd probably be a better
idea to adapt the NOX-derived GUI to use the newer TCP stream interface
instead, as it has many advantages.
"""

import comm
import socket
import json

class GuiInterface(comm.NullInterface):
  def __init__ (self):
    self.recv = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    self.recv.bind(("127.0.0.1", 4445))
    self.thread = threading.Thread(target = self._recvLoop)
    self.thread.daemon = True
    self.thread.start()

  def _recvLoop (self):
    import select
    while True:
      (rx, tx, xx) = select.select([self.recv], [], [])
      if len(rx):
        d = self.recv.recv(4096)
        try:
          world.doLater(0, self.handle_recv, json.loads(d))
        except:
          traceback.print_exc()

  def handle_recv (self, data):
    import basics
    if data['type'] == "ping":
      src = getattr(sim, data['src'])
      dst = getattr(sim, data['dst'])
      src.send(basics.Ping(dst), flood=True)
    elif data['type'] == "console":
      # Execute python command, return output to GUI
      #print "Got command:", data['command']
      r = interp.runsource(data['command'], "<gui>")
      if r:
        events.send_console_more(data['command'])

  def sendToGui(self, dict_msg):
    sock = socket.socket( socket.AF_INET, socket.SOCK_DGRAM )
    sock.sendto( json.dumps(dict_msg), ("127.0.0.1", 4444) )
  
  def send_console(self, text):
    self.sendToGui({'type':'console','msg':text})

  def send_console_more(self, text):
    self.sendToGui({'type':'console_more','command':text})

  def send_log(self, record):
    self.sendToGui(record)

  def send_entity_up(self, name, kind):
    #print name
    msg = {}
    msg['type'] = 'topology'
    msg['command'] = 'add'
    msg['node_type'] = kind
    msg['node_id'] = name
    self.sendToGui(msg)
    
  def send_link_up(self, srcid, sport, dstid, dport):
    msg = {}
    links = [
      {'src port': sport,
      'src id': srcid,
      'dst id': dstid,
      'src type': 'switch',
      'dst type': 'switch',
      'dst port': dport}]
    msg['type'] = 'topology'
    msg['command'] = 'add'
    msg['links'] = links
    self.sendToGui(msg)
    
  def send_link_down(self, srcid, sport, dstid, dport):
    msg = {}
    links = [
      {'src port': sport,
      'src id': srcid,
      'dst id': distid,
      'src type': 'switch',
      'dst type': 'switch',
      'dst port': dport}]
    msg['type'] = 'topology'
    msg['command'] = 'remove'
    msg['links'] = links
    self.sendToGui(msg)
  
  def highlight_path (self, nodes):
    """ Sends a path to the GUI to be highlighted """
    nodes = [n.name for n in nodes]
    msg = {'type':'highlight', 'nodes':nodes}
    self.sendToGui(msg)

  def set_debug(self, nodeid, msg):
    msg = {
           'type' : 'debug',
           'node_id' : nodeid,
           'msg': msg,
          }
    self.sendToGui(msg)
    

interface = GuiInterface
