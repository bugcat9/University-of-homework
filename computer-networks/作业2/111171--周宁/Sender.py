import sys
import getopt

import Checksum
import BasicSender
import threading
import thread
'''
This is a skeleton sender class. Create a fantastic transport protocol here.
'''
#sys.argv =[sys.argv[0],'-fREADME','-p33122','-a127.0.0.1']
class Sender(BasicSender.BasicSender):
    def __init__(self, dest, port, filename, debug=False):
        super(Sender, self).__init__(dest, port, filename, debug)

    # Main sending loop.
    def start(self):
        self.base=0
        self.nextseqno=0 
        self.seqno=0
        self.cwnd=5
        self.sndpkt =[0]* self.cwnd
        self.order=0
        self.count=0
        self.timerout=5
        self.reservezone=2
        self.size=1000
        self.isend=False
        msg_type=None
        #self.infile.close()
        while self.seqno<self.cwnd and self.isend==False:
            if msg_type=='end':
                self.send(self.sndpkt[(self.nextseqno)%self.cwnd])
                print "sent: %s" % self.sndpkt[self.nextseqno%self.cwnd]
                self.nextseqno+=1
                if self.nextseqno==self.seqno:
                    self.isend=True
            
                break
            msg = self.infile.read(self.size)
            if self.seqno==0:
                msg_type='start'
            elif msg=='':
                msg_type = 'end'
                msg_type1,seqno,msg,checksum=self.split_packet(self.sndpkt[self.nextseqno-1])
            else:
                msg_type='data'
            packet = self.make_packet(msg_type,self.seqno,msg)
            self.sndpkt[self.seqno]=packet
            self.seqno+=1
        self.rdt_send()
        self.msg_type=msg_type
    def handle_response(self,response_packet):
        if Checksum.validate_checksum(response_packet):
            #NoProblem
            print "recv: %s"% response_packet
            msg_type, seqno, data, checksum=self.split_packet(response_packet)
            #self.nextseqno=seqno
            if self.order!=seqno:
                self.order=seqno
                self.count=0
                self.handle_new_ack(seqno)
            else:
                self.count+=1
                if self.count==3:
                    print 'is to  self.handle_dup_ack'
                    self.handle_dup_ack(seqno)
                    self.count=0
                else:
                    self.handle_new_ack(seqno)
        else:
            #
            print "recv: %s <--- CHECKSUM FAILED" % response_packet
            self.handle_timeout()
    
    def handle_timeout(self):
        print 'time out'
        self.timer=threading.Timer(1,self.rdt_rcv)
        self.timer.start()
        
        for i in range(self.base,self.nextseqno):
            index=i%self.cwnd
            self.send(self.sndpkt[index])
            #print "sent: %s" % self.sndpkt[index]

    def handle_new_ack(self, ack):
        if self.base<=int(ack):
            self.base=int(ack)
        while self.nextseqno<(self.base+self.cwnd-self.reservezone) and self.isend==False:
            if self.msg_type=='end':
                self.send(self.sndpkt[(self.nextseqno)%self.cwnd])
                print "sent: %s" % self.sndpkt[self.nextseqno%self.cwnd]
                self.nextseqno+=1
                if self.nextseqno==self.seqno:
                    self.isend=True
                   
                break
            msg = self.infile.read(self.size)
            i=self.seqno%self.cwnd
            if self.seqno==0:
                self.msg_type='start'
            elif msg=='':
                self.msg_type = 'end'
                msg_type,seqno,msg,checksum=self.split_packet(self.sndpkt[i-1])
                self.seqno=self.seqno-1
                i=i-1
            else:
                self.msg_type='data'
            packet = self.make_packet(self.msg_type,self.seqno,msg)
            
            self.sndpkt[i]=packet
            self.send(self.sndpkt[self.nextseqno%self.cwnd])
            #print "sent: %s" % self.sndpkt[self.nextseqno%self.cwnd]
            self.seqno+=1
            self.nextseqno+=1
        if self.base==self.nextseqno:
            self.timer.cancel()
        else:
            self.timer=threading.Timer(1,self.rdt_rcv)
            self.timer.start()
    def handle_dup_ack(self, ack):
        self.timer.cancel()
        self.timer=threading.Timer(1,self.rdt_rcv)
        self.timer.start()
        
        for i in range(self.base,self.nextseqno):
            index=i%self.cwnd
            self.send(self.sndpkt[index])
            #print "sent: %s" % self.sndpkt[index]
            
      
    #nums is Number of files issued
    def rdt_send(self):
        while self.nextseqno<(self.base+self.cwnd-self.reservezone):
            i=self.nextseqno%self.cwnd
            self.send(self.sndpkt[i])
            #print "sent: %s" % self.sndpkt[i]
            #start Timer
            if self.nextseqno==self.base:
                self.timer=threading.Timer(1,self.rdt_rcv)
                self.timer.start()
                
            self.nextseqno+=1
            
    def rdt_rcv(self):
        response = self.receive(self.timerout)
        self.handle_response(response)  

    def log(self, msg):
        if self.debug:
            print msg
            

'''
This will be run if you run this script from the command line. You should not
change any of this; the grader may rely on the behavior here to test your
submission.
'''
if __name__ == "__main__":
    def usage():
        print "BEARS-TP Sender"
        print "-f FILE | --file=FILE The file to transfer; if empty reads from STDIN"
        print "-p PORT | --port=PORT The destination port, defaults to 33122"
        print "-a ADDRESS | --address=ADDRESS The receiver address or hostname, defaults to localhost"
        print "-d | --debug Print debug messages"
        print "-h | --help Print this usage message"

    try:
        opts, args = getopt.getopt(sys.argv[1:],
                               "f:p:a:d", ["file=", "port=", "address=", "debug="])
    except:
        usage()
        exit()

    port = 33122
    dest = "localhost"
    filename = None
    debug = False

    for o,a in opts:
        if o in ("-f", "--file="):
            filename = a
        elif o in ("-p", "--port="):
            port = int(a)
        elif o in ("-a", "--address="):
            dest = a
        elif o in ("-d", "--debug="):
            debug = True

    s = Sender(dest,port,filename,debug)
    try:
        s.start()
    except (KeyboardInterrupt, SystemExit):
        exit()
