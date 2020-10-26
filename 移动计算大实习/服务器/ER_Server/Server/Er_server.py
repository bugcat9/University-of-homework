import socket               # 导入 socket 模块
import threading
import time
import sys
import os
import struct
from util.Predict import predict
from models.vggModel import vggModel
#进行对应的文件接收
def deal_data(conn, addr):
    print('Accept new connection from {0}'.format(addr))
    #conn.settimeout(500)
    #conn.send('Hi, Welcome to the server!'.encode())
    while 1:
        buf = conn.recv(4)
        length = int.from_bytes(buf, byteorder='big')
        buf = conn.recv(length)
        new_filename = '../rec/'+buf.decode()
        print(buf.decode())
        buf = conn.recv(8)
        filesize=int.from_bytes(buf, byteorder='big')
        print(filesize)
        if buf:
            #filename, filesize = struct.unpack('128sl', buf)
            # new_filename = '1.png'
            print('file new name is {0} and filesize is {1}'.format(new_filename,filesize))
            recvd_size = 0  # 定义已接收文件的大小
            fp = open(new_filename, 'wb')
            print('start receiving...')
            while not recvd_size == filesize:
                if filesize - recvd_size > 1024:
                    data = conn.recv(1024)
                    recvd_size += len(data)
                else:
                    data = conn.recv(filesize - recvd_size)
                    recvd_size = filesize
                fp.write(data)
            fp.close()
            print('end receive...')
        res = predict(new_filename)
        ans=struct.pack(">i", res)
        print(ans)
        conn.send(ans)
       #conn.close()
        break


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# host = socket.gethostname() # 获取本地主机名
port = 3030                # 设置端口
s.bind(("", port))        # 绑定端口
s.listen(5)                 # 等待客户端连接
while True:
    sock,addr = s.accept()     # 建立客户端连接
    # 创建新线程来处理TCP连接:
    t = threading.Thread(target=deal_data, args=(sock, addr))
    t.start()
