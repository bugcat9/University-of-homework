import torch
from torchvision import transforms
import numpy as np
import pickle
import json
import csv
from models.resNet import resNet
from models.simpleModelPlus import simpleModelPlus

#用于写单个网络的分数，会存成csv以及json
path = 'resnet'
type= 'beatiful'
type_list=['beatiful','boring','depressing','lively','safety','wealty']
model_path = path+'/'+type+'/'+'model.pkl'
use_cuda = torch.cuda.is_available()    #gpu是否可用
net = torch.load(model_path)

#读取视频当中的文件
output_file = 'VID_20191212_100933.pickle'
f = open(output_file, 'rb')
imgs, time_, longitude, latitude=pickle.load(f)


# print(np.shape(imgs))
imgs = np.array(imgs)
#print(np.shape(imgs))
transform_test = transforms.Compose([
    transforms.ToPILImage(),
    transforms.CenterCrop(224),
    transforms.ToTensor()
])

#进行相对应的处理

#print(net)
if use_cuda:
    net.cuda()
net.eval()

#存成csv
csv_save_path = path+'/'+type+'/'+type+".csv"
csvfile = open(csv_save_path,"w",newline ='')
csv_writer = csv.writer(csvfile)
csv_head = ("longitude","latitude","score")
csv_writer.writerow(csv_head)

#存json
json_save_path = path+'/'+type+'/'+type+".json"
jsonfile = open(json_save_path,"w")
scorelist=list()

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

        d = {"longitude":longitude[i],"latitude":latitude[i],"score":score}
        scorelist.append(d)
        row =(longitude,latitude,score)
        csv_writer.writerow(row)


json.dump(scorelist,jsonfile)
f.close()
csvfile.close()
jsonfile.close()

print('finish!!!!!!!!!!!!!!!!!!!!!!')