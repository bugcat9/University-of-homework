'''
Created on 2019年4月30日

@author: zhouning
'''
#14个字节
class BITMAPFILEHEADER:
    def __init__(self, *args):
        if len(args)==5:
            self.bfType=args[0]      #文件类型，对于bmp格式，其值是'MB'19778
            self.bfSize=args[1]         #文件总大小
            self.bfReserved1=args[2]        #保留，置0
            self.bfReserved2=args[3]        #保留，置0
            self.bfOffBits=args[4]              #位图数据偏移长度
            
#40个字节
class BITMAPINFOHEADER:
    def __init__(self, *args):
        if len(args)==11:
            self.biSize=args[0]         #信息头大小,sizeof(BITMAPINFOHEADER)
            self.biWidth=args[1]      #图片宽度
            self.biHeight=args[2]         #图片高度
            self.biPlanes=args[3]        #面数,置1
            self.biBitCount=args[4]        #每一位像素占的位数,对于本题为8
            self.biCompression=args[5]              #压缩,0比表示不压缩.BI_RGB
            self.biSizeImage=args[6]              #图片大小
            self.biXPelsPerMeter=args[7]              #分辨率，每米多少个像素
            self.biYPelsPerMeter=args[8]              #分辨率，每米多少个像素
            self.biClrUsed=args[9]              #用到的颜色数
            self.biClrImportant=args[10]              #重要的颜色
        