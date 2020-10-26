import torch
from torchvision import transforms
import numpy as np
import pickle
import json
import csv
from models.resNet import resNet
from models.simpleModelPlus import simpleModelPlus

#用于将单个分数写到txt当中
path = 'vgg'	#网络类型
type= 'wealth'	#情感类型
type_list=['beatiful','boring','depressing','lively','safety','wealth']
model_path = path+'/'+type+'/'+'model.pkl'
use_cuda = torch.cuda.is_available()    #gpu是否可用


net = torch.load(model_path)

#读取视频当中的文件
output_file = 'VID_20191212_100933.pickle'
f = open(output_file, 'rb')
imgs, time_, longitude, latitude=pickle.load(f)


imgs = np.array(imgs)

transform_test = transforms.Compose([
    transforms.ToPILImage(),
    transforms.CenterCrop(224),
    transforms.ToTensor()
])

#进行相对应的处理


if use_cuda:
    net.cuda()
net.eval()


scorelist=list()

#存到txt中
txt_save_path = path+'/'+type+'/'+type+".txt"
txtfile = open(txt_save_path,"w")


avg_score = 0
for i in range(len(imgs)):
    img = imgs[i]
    input = transform_test(img)
    input = torch.unsqueeze(input, 0)
    if use_cuda:
        input = input.cuda()
    with torch.no_grad():
        input = torch.autograd.Variable(input)
        output = net(input)
    score = round(output.item(), 3)
    txtfile.write('%.3f'%score+'\r')


f.close()
txtfile.close()
print('finish!!!!!!!!!!!!!!!!!!!!!!')