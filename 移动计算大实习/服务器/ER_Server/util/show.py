import pickle  #导入pickle模块
import matplotlib.pyplot as plt
path = 'CK'
pickle_file1 = open(path+"/Test_acc_list.pkl", "rb")  #打开存在的数据文件
Test_acc_list = pickle.load(pickle_file1)  #读取文件数据
pickle_file2 = open(path+"/train_loss_list.pkl", "rb")  #打开存在的数据文件
train_loss_list = pickle.load(pickle_file2)  #读取文件数据

# 绘制训练过程中每一步的损失函数值
plt.figure(1) # 生成第一个图，且当前要处理的图为fig.1
plt.figure(figsize=(10, 8))
plt.plot(Test_acc_list,'g-')
plt.ylabel('Test_acc_list')

plt.figure(2) # 生成第二个图，且当前要处理的图为fig.2
plt.figure(figsize=(10, 8))
plt.plot(train_loss_list,'g-')
plt.ylabel('train_loss_list')
# plt.plot(train_loss_list, color="r", linestyle="-", linewidth=1)
# plt.plot(test_loss_list, color="b", linestyle="-",linewidth=1)
plt.show()
pickle_file1.close()
pickle_file2.close()