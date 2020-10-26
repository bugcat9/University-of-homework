import pickle  #导入pickle模块
import matplotlib.pyplot as plt
#用于画6和1的图
path = 'vgg'
type= 'wealth'
type_list=['beatiful','boring','depressing','lively','safety','wealth']
all_emtion_train_loss=[]
all_emtion_test_loss=[]

for i in range(len(type_list)):
    with open(path+'/'+type_list[i]+'/'+"train_loss_list.pkl", "rb") as f:
        train_loss_list = pickle.load(f)  # 读取文件数据
        all_emtion_train_loss.append(train_loss_list)
    with open(path+'/'+type_list[i]+'/'+"Test_loss_list.pkl", "rb") as f:
        test_loss_list = pickle.load(f)  # 读取文件数据
        all_emtion_test_loss.append(test_loss_list)
		
#画6合一的图
axList = []
for i in range(6):
    axList.append(plt.subplot(2,3,i+1))

for i in range(6):
    plt.sca(axList[i])
    plt.title(type_list[i])
    plt.plot(all_emtion_train_loss[i], color="r", linestyle="-", linewidth=1,label="train_loss")
    plt.plot(all_emtion_test_loss[i], color="b", linestyle="-", linewidth=1,label="test_loss")
    plt.legend(loc='upper left', bbox_to_anchor=(0.2, 0.95))
plt.show()


