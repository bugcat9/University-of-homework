from Server.server import Server
import sys
import getopt

def main(argv):
    port = None
    opts,args=getopt.getopt(argv,'p:',[])
    for opt,arg in opts:
        if opt=='-p':
            port=arg
    server = Server(port)



if __name__ == "__main__":
     main(sys.argv[1:])


# if __name__ == '__main__':
#     port = input("port:")
#     server = Server(port)