'''
Created on 2019年4月30日

@author: zhouning
'''
from BMPHead import  * 
import struct
import time
import decimal
from PyQt5.QtCore import *
from _tracemalloc import start
class CompressBmp(QThread):
     #  通过类成员对象定义信号对象
    _signal = pyqtSignal()
    def __init__(self,filename):
        super().__init__()
        self.filename=filename
        self.readBMP()
        self.s=[0]*(self.InfoHeader.biHeight*self.InfoHeader.biWidth+1)
        self.b=[0]*(self.InfoHeader.biHeight*self.InfoHeader.biWidth+1)
        self.l=[0]*(self.InfoHeader.biHeight*self.InfoHeader.biWidth+1)
        
    def readBMP(self):
        with open(self.filename,'rb') as f:
            #读文件的前两个头
            x=f.read(14)
            fh=struct.unpack('<HHHII',x )
            y=f.read(40)
            ih=struct.unpack('<IIIHHIIIIII',y )
            self.FileHeader=BITMAPFILEHEADER(*fh)
            self.InfoHeader= BITMAPINFOHEADER(*ih)
            size=self.FileHeader.bfOffBits-14-40
            RGBQUAD=f.read(size)    #调色板在此题没什么大用处，可以直接读直接写
            
            format='<%dB'%self.InfoHeader.biWidth
            self.p=[]
            #读的时候转化为蛇形矩阵
            for i in range(self.InfoHeader.biHeight):
                line=list(struct.unpack(format,f.read(self.InfoHeader.biWidth)))
                if i%2!=0:
                    line.reverse()
                self.p=self.p+line
                    
            file=self.filename[0:len(self.filename)-3]+'zinp'
            #对后面没什么影响直接写就行
            with open(file, 'wb') as f:
                f.write(x)
                f.write(y)
                f.write(RGBQUAD)
                
                
    def run(self):
        self.Compress()
        self.writeFile()
    #进行dp
    def Compress(self):
        def length(i):
            k=1;
            i=i//2
            while i>0:
                k+=1
                i=i//2
            return k
            
        Lmax = 256
        header = 11
        n=self.InfoHeader.biHeight*self.InfoHeader.biWidth+1
        
        for i in range(n):
            self.b[i] = length(self.p[i-1])#计算像素点p需要的存储位数  
            bmax = self.b[i];
            self.s[i] = self.s[i - 1] + bmax;
            self.l[i] = 1;
            j=2
            while j<=i and j<=Lmax:
                if  bmax<self.b[i - j + 1]:
                    bmax = self.b[i - j + 1]
                if self.s[i]>self.s[i - j] + j * bmax:
                    self.s[i] =self. s[i - j] + j * bmax
                    self.l[i] = j
                j+=1
            self.s[i] += header
        
        #追踪解
        self.count=1       #顺带可以记录一下解的段数
        self.c=[]
        n=n-1
        while n!=0:
            self.c.append(self.l[n])     #c[j]表示分段后第j段长度为l[n]
            n=n-self.l[n]
            self.count=self.count+1
        self.count=self.count-1
        self.c.reverse()#对c数组逆置

    def  writeFile(self):
        file=self.filename[0:len(self.filename)-3]+'zinp'
        signalcount=0
        percentage=self.count//100
        with open(file,'ab+') as f:
            #先将多少段写进去
            f.write(struct.pack('<I',self.count))
            #buffer储存当前的数据，pos指当前buffer储存了多少位,offset指偏移量
            buffer=0
            pos=0
            offset=0
            index=0
            bitnum=8
            for i in range(self.count):
                #先将段头储存进去，段头代表位数和长度
                L=self.c[i]
                B=max(self.b[index+1:index+L+1])
                buffer=(buffer<<3)^(B-1)
                buffer=(buffer<<8)^(L-1)
                pos+=11
                #当前的位数大于bitnum时
                while pos>=bitnum:
                    offset=pos-bitnum
                    temp=buffer>>offset
                    f.write(struct.pack('<B',temp))
                    buffer=buffer&((1<<offset)-1)
                    pos=pos-bitnum
                
                for j in range(L):
                    #每次读一个下标
                    buffer=(buffer<<B)^self.p[index]
                    index+=1
                    pos+=B
                    while pos>=bitnum:
                        offset=pos-bitnum
                        temp=buffer>>offset
                        f.write(struct.pack('<B',temp))
                        buffer=buffer&((1<<offset)-1)
                        pos=pos-bitnum
                    
                    signalcount+=1
                    if  signalcount==percentage:
                        signalcount=0
                        self._signal.emit()
            #最后位数不够，补充0
            if pos:
                buffer=buffer<<(bitnum-pos)
                f.write(struct.pack('<B',buffer))

class unCompressBmp(QThread):
    _signal = pyqtSignal()
    def __init__(self,filename):
        super().__init__()
        self.filename=filename
        
    def unCompress(self):
        with open(self.filename,'rb') as f:
            x=f.read(14)
            fh=struct.unpack('<HIHHI',x )
            y=f.read(40)
            ih=struct.unpack('<IIIHHIIIIII',y )
            self.FileHeader=BITMAPFILEHEADER(*fh)
            self.InfoHeader= BITMAPINFOHEADER(*ih)
            size=self.FileHeader.bfOffBits-14-40
            RGBQUAD=f.read(size)    #调色板在此题没什么大用处，可以直接读直接写
            file=self.filename[:len(self.filename)-4]+'bmp'
            print(file)
            self.p=[0]*self.InfoHeader.biHeight*self.InfoHeader.biWidth
            #buffer储存当前的数据，pos指当前buffer储存了多少位,offset指偏移量
            buffer=0
            lastbuffer=0
            pos=0
            offset=0
            index=0
            bitnum=8
            B=0
            L=0
            self.count=struct.unpack('<I',f.read(4))[0]
            
            signalcount=0
            percentage=self.count//100
            for i in range(0,self.count):
                #读B
                if pos>=3:
                    offset=pos-3
                    B=lastbuffer>>offset
                    lastbuffer=lastbuffer&((1<<offset)-1)
                    pos=pos-3
                else:
                    buffer=struct.unpack('<B',f.read(1))[0]
                    offset=3-pos
                    a=buffer>>(bitnum-offset)
                    B=(lastbuffer<<offset)^a
                    lastbuffer=buffer&((1<<(bitnum-offset))-1)
                    pos=bitnum-offset
                B+=1
                
                    #读l
                if pos>=8:
                    offset=pos-8
                    L=lastbuffer>>offset
                    lastbuffer=lastbuffer&((1<<offset)-1)
                    pos=pos-8
                else:
                    buffer=struct.unpack('<B',f.read(1))[0]
                    offset=8-pos
                    a=buffer>>(bitnum-offset)
                    L=(lastbuffer<<offset)^a
                    lastbuffer=buffer&((1<<(bitnum-offset))-1)
                    pos=bitnum-offset
                L+=1
                    
                for j in range(L):
                    if pos>=B:
                        offset=pos-B
                        self.p[index]=lastbuffer>>offset
                        lastbuffer=lastbuffer&((1<<offset)-1)
                        pos=pos-B
                    else:
                        buffer=struct.unpack('B',f.read(1))[0]
                        offset=B-pos
                        a=buffer>>(bitnum-offset)
                        self.p[index]=(lastbuffer<<offset)^a
                        lastbuffer=buffer&((1<<(bitnum-offset))-1)
                        pos=bitnum-offset
                    index+=1
                    
                    signalcount+=1
                    if  signalcount==percentage:
                        signalcount=0
                        self._signal.emit()
        
        with open(file,'wb') as f1:
            f1.write(x)
            f1.write(y)
            f1.write(RGBQUAD)
            format='%dB'%self.InfoHeader.biWidth
            for i in range(self.InfoHeader.biHeight):
                line=self.p[i*self.InfoHeader.biWidth:(i+1)*self.InfoHeader.biWidth]
                if i%2!=0:
                    line.reverse()
                f1.write(struct.pack(format,*line))   
    def run(self):
        self.unCompress()
            
if __name__ == '__main__':
    filename='img/纹理.zinp'
    start =time.time()
    '''
    start =time.time()
    a=CompressBmp(filename)
    a.Compress()
    a.writeFile()
    
    '''
    b=unCompressBmp(filename)
    b.unCompress()
    print(time.time()-start)
    print('跑完了')