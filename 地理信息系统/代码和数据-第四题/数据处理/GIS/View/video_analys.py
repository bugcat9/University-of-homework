import pickle
import time
import cv2
import os
import csv

import numpy as np
from PIL import Image

#视频的路径
video_path='D:\\Users\\GISDATA\\GIS-Q4-Data&Ref&App\\03video_data_demo\\20191212_100933(Video_Demo)\\VID_20191212_100933.mp4'
#video_path='D:\\Users\\GISDATA\\Q4-guanggu-20191229_133445\\20191229_133445\\VID_20191229_133445.mp4'
#CSV文件路径
csv_path=video_path+'.csv'
#txt文件路径
txt_path=video_path+'.txt'
#dir_path=video_path[:-4]+"_\\"
#存放图片的目录
dir_path=video_path[:-4]+"\\"

#视频截取得到的图片所在目录
videofile="D:\\Users\\GISDATA\\GIS-Q4-Data&Ref&App\\03video_data_demo\\20191212_100933(Video_Demo)\\VID_20191212_100933"
#使用pickle输出文件的路径
video_output="D:\\Users\\GISDATA\\GIS-Q4-Data&Ref&App\\03video_data_demo\\20191212_100933(Video_Demo)\\VID_20191212_100933.pickle"
#videofile="D:\\Users\\GISDATA\\Q4-guanggu-20191229_133445\\20191229_133445\\VID_20191229_133445"
#video_output=videofile+'.pickle'



height=445
width=700


rotate=90

#videofile 视频路径
#rotate 将视频旋转的角度，可选：90，180，270
#time 时间戳
#filename 保存图片的路径
#根据视频路径读取视频
#设置图像旋转角度
#设置输出文件名
#因为视频中部分图像被挡住，需要设置图片裁剪的宽和高
#根据时间戳截取视频的制定帧
def getPictByTime(videofile,rotate_,time,filename,height,width):
    cameraCapture = cv2.VideoCapture(videofile)
    # 获取帧率
    fps = cameraCapture.get(cv2.CAP_PROP_FPS)
    # 设置得到第fps*time帧
    cameraCapture.set(cv2.CAP_PROP_POS_FRAMES, time * fps)
    success, img = cameraCapture.read()

    if (success is False): return
    #判断旋转角度
    if rotate_ == 90:
        img = cv2.transpose(img)
        img = cv2.flip(img, 1)
    elif rotate_ == 180:
        img = cv2.transpose(img)
        img = cv2.transpose(img)
    elif rotate_==270:
        img = cv2.transpose(img)
        img = cv2.flip(img, -1)

    img = img[0:height, 0:width]
    #将图片转为RGB格式
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img = Image.fromarray(img)
    img = img.resize((224, 224))
    img.save(filename)


#videopath 视频路径
#CSVfilename CSV文件路径
#txtfilename txt文件路径
#savepath 文件保存的目录
#读取txt和CSV文件，得到时间戳，调用函数getPictByTime截取并保存图片
def readCSV(videopath,CSVfilename,txtfilename,savepath):
    ftxt=open(txtfilename,'r')
    st_=ftxt.readline()
    fcsv=open(CSVfilename,'r')
    d=csv.reader(fcsv)

    start_datetime=time.mktime(time.strptime(st_,'%Y-%m-%d %H:%M:%S:%f '))
    print(start_datetime)
    i=0
    for line in d:
        if i == 0:                #去除掉第一排的标题
            i += 1
            continue
        #print(line)
        curtime=time.mktime(time.strptime(line[0],'%Y-%m-%d %H:%M:%S:%f'))
        m_time=curtime-start_datetime
        #根据需要决定是否要加上时间信息
        time_=line[0].split(' ')[1]
        #文件路径中不能有冒号
        time_=time_.replace(':', '.')
        filename = savepath+line[3] + '_' + line[2] + '.png'

        getPictByTime(videopath,rotate,m_time,filename,height,width)



#videofile 视频截取图片存放路径
#output_file pickle输出路径
def WriteVideo(videofile,output_file):
    files=os.listdir(videofile)
    #时间，根据需要设置
    #time_=[]
    #图片矩阵
    imgs=[]
    #纬度
    latitude=[]
    #经度
    longtitude=[]
    for file in files:
        location=file.split('_')
        file = videofile + "\\" + file
        img = Image.open(file)  # 读取图片
        img = img.convert("RGB")    #将图片格式转为RGB
        img = np.array(img)
        imgs.append(img)

        longtitude.append(location[1])
        latitude.append(location[2][:-4])
    write_file=open(output_file,'wb')
    pickle.dump((imgs, longtitude, latitude), write_file, 0)
    write_file.close()


#output_file pickle输出的文件
#测试pickle的输出文件是否正确
def pickle_load_video(output_file):
    f = open(output_file, 'rb')
    imgs,longitude,latitude=pickle.load(f)
    for num in range(len(imgs)):
        img=imgs[num]
        print(img.shape)
        print(float(longitude[num]),float(latitude[num]))
    img=Image.fromarray(imgs[5])
    Image._show(img)


if __name__ == '__main__':
    readCSV(video_path,csv_path,txt_path,dir_path)
    WriteVideo(videofile, video_output)
    pickle_load_video(video_output)

