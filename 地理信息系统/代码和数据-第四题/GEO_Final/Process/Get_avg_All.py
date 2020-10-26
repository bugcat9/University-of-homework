import pickle
import json
import csv
#得到平均的分数，采取3次一分，从txt当中读取
path = 'vgg'
type_list=['beatiful','boring','depressing','lively','safety','wealth']

#存成csv
csv_save_path = "all_avg_3_time"+path+".csv"
csvfile = open(csv_save_path,"w",newline ='')
csv_writer = csv.writer(csvfile)
#csv_head = ("longitude","latitude",'beatiful','boring','depressing','lively','safety','wealth','ImageName')
csv_head = ('time','score','type','ImageName')
csv_writer.writerow(csv_head)

#存json
json_save_path = "all_avg_3_time"+path+".json"
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

index = 1
templongitude=[]
templatitude=[]
temptime=[]
tempEmtion = []
num = 3 #说明是3个取次平均，需要进行求解
for i in range(len(imgs)//num):
    start = i*num   #起始位置
    end = (i+1)*num #终止位置
    templongitude = longitude[start:end]
    templatitude =latitude[start:end]
    temptime = time_[start:end]
    tempEmtion = []
    for j in range(6):
        tempEmtion.append(all_emotion[j][start:end])

    avg_emtion = [0,0,0,0,0,0]
    #也可以通过for循环，这里为了省事，就直接使用这个
    for j in range(num):
        avg_emtion[0] += float(tempEmtion[0][j])
        avg_emtion[1] += float(tempEmtion[1][j])
        avg_emtion[2] += float(tempEmtion[2][j])
        avg_emtion[3] += float(tempEmtion[3][j])
        avg_emtion[4] += float(tempEmtion[4][j])
        avg_emtion[5] += float(tempEmtion[5][j])

    for j in range(6):
        avg_emtion[j] = avg_emtion[j]/num

    # 键值对类型，用于生成json
    d = {"longitude": templongitude[num // 2], "latitude": templatitude[num // 2],
         'beatiful':avg_emtion[0], 'boring':avg_emtion[1] , 'depressing':avg_emtion[2] ,
         'lively': avg_emtion[3], 'safety':avg_emtion[4] , 'wealth':avg_emtion[5] ,
         'ImageName': templongitude[num // 2] + '_' + templatitude[num // 2]}
    scorelist.append(d)
	
	#注释部分为求解加上时间的，为时序图准备的
    # for j in range(len(type_list)):
    #     d = {
    #         'time': temptime[num // 2], 'score': avg_emtion[j], 'type': type_list[j],
    #         'ImageName': templongitude[num // 2] + '_' + templatitude[num // 2]
    #     }
    #     scorelist.append(d)

    #用于生成csv
    row = (
        templongitude[num // 2], templatitude[num // 2], avg_emtion[0], avg_emtion[1], avg_emtion[2],
        avg_emtion[3], avg_emtion[4], avg_emtion[5], templongitude[num // 2] + '_' + templatitude[num // 2])
    csv_writer.writerow(row)
	
	#只是部分为诶求解加上时间的
    # for j in range(len(type_list)):
    #     row = (temptime[num // 2], avg_emtion[j], type_list[j],
    #            templongitude[num // 2] + '_' + templatitude[num // 2])
    #     csv_writer.writerow(row)

    

print(index)
json.dump(scorelist,jsonfile)
f.close()
csvfile.close()
jsonfile.close()
print('finish')