import csv
import os
import pprint

from PIL import Image
import cv2
import numpy as np
import pickle

#图片文件目录
feelfile='D:\\Users\\GISDATA\\scores\\scores\\wealthy'
#CSV文件，保存了图片名和对应分数
input_file = 'D:\\Users\\GISDATA\\scores\\scores\\auto_save_scores_wealthy.csv'
#输出的pickle文件
output_file_pickle="D:\\Users\\GISDATA\\scores\\scores\\feel_boring"



#将数据随机分为训练集和测试集
#获取对应的图片和分数，将图片转为矩阵
#使用pickle写入到文件中
def write_pictle(input_file,input_dir,output_file,):
    read_file = open(input_file, "r")
    csv_read=csv.reader(read_file)
    result = dict(csv_read) #转化为字典

    with open(output_file,'wb') as write_file:
        #训练集分数
        train_score = []
        #训练集图片矩阵
        train_img = []
        #测试集图片矩阵
        test_img=[]
        #测试集分数
        test_score=[]
        length=len(result)
        for num in range(int(8*length/10)):
            #训练集
            (file,score)=result.popitem()
            if file=='File':
                continue
            file_name = input_dir + '\\' + file + '.png'  # 得到文件的位置
            img = Image.open(file_name)  # 读取图片
            img = img.convert("RGB")
            img= np.array(img)
            train_img.append(img)
            train_score.append(score)
        for num in range(int(2*length/10)):
            #测试集
            (file, score) = result.popitem()
            if file == 'File':
                continue
            file_name = input_dir + '\\' + file + '.png'  # 得到文件的位置
            #img = cv2.imread(file_name)
            # img=cv2.resize(img, (224, 224), interpolation=cv2.INTER_CUBIC)
            # img = img[:, :, :: -1]
            img = Image.open(file_name)  # 读取图片
            img = img.convert("RGB")
            img = img.resize((224, 224))
            img = np.array(img)
            test_img.append(img)
            test_score.append(score)
        f_img = open(output_file_pickle, 'wb')
        pickle.dump((train_img,train_score,test_img,test_score), f_img, 0)
        f_img.close()
    read_file.close()


#测试文件是否正确
def pickle_load(output_file):  # 使用pickle从文件中重构python对象
    f = open(output_file, 'rb')
    train_imgs,train_scores,test_imgs,test_scores = pickle.load(f)
    for img in train_imgs:
        #测试：矩阵的维度是否正确
        print(img.shape)
    img=Image.fromarray(img.astype('uint8')).convert('RGB')
    Image._show(img)
    img = Image.fromarray(test_imgs[0].astype('uint8')).convert('RGB')
    Image._show(img)

    #测试数据完整性
    #打印测试集和训练集数量
    print(len(train_imgs))
    print(len(train_scores))
    print(len(test_imgs))
    print(len(test_scores))

    f.close()




if __name__=='__main__':
    #处理数据
    write_pictle(input_file, feelfile, output_file_pickle)
    #测试输出文件
    pickle_load(output_file_pickle)

