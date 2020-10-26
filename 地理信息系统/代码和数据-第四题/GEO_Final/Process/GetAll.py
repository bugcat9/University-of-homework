import pickle
import json
import csv
#用于从分数的txt中，然后进行相对应的处理之后
path = 'vgg'
type_list=['beatiful','boring','depressing','lively','safety','wealth']

#存成csv
csv_save_path = "all_"+path+"new.csv"
csvfile = open(csv_save_path,"w",newline ='')
csv_writer = csv.writer(csvfile)
csv_head = ("longitude","latitude",'beatiful','boring','depressing','lively','safety','wealthy','ImageName')
csv_writer.writerow(csv_head)

#存json
json_save_path = "all_"+path+"new.json"
jsonfile = open(json_save_path,"w")
scorelist =[]
#读取所有情感
all_emotion=[]
for emotion_type in type_list:
    filepath = path+'/'+emotion_type+'/'+emotion_type+".txt"
    with open(filepath,'r') as f:
        emtion = f.readlines()
        all_emotion.append(emtion)

#读取视频当中的文件
output_file = 'VID_20191212_100933.pickle'
f = open(output_file, 'rb')
imgs, time_, longitude, latitude=pickle.load(f)


for i in range(len(imgs)):
    emtion=[]
    for j in range(len(type_list)):
        #需要去掉空格
        emtion.append(all_emotion[j][i].strip())
        #d={'time':time_[i],'score':all_emotion[j][i].strip(),'type':type_list[j]}
    d={"longitude":longitude[i],"latitude":latitude[i],'beatiful':emtion[0],'boring':emtion[1],
      'depressing':emtion[2],'lively':emtion[3],'safety':emtion[4],'wealthy':emtion[5],'ImageName':longitude[i]+"_"+latitude[i]}
    row =(longitude[i],latitude[i],emtion[0],emtion[1],emtion[2],emtion[3],emtion[4],emtion[5],longitude[i]+"_"+latitude[i])
    scorelist.append(d)
    csv_writer.writerow(row)

json.dump(scorelist,jsonfile)
f.close()
csvfile.close()
jsonfile.close()
print('finish')