import torch
import torch.nn as nn
from torchvision import transforms

from models.resNet import  resNet
from models.simpleModelPlus import simpleModelPlus
from trainSet import TrainSet
from trainSet import TestSet
import torch.optim as optim
from torch.autograd import Variable
import pickle
#一些参数
batch_size = 2#块的大小
learn_rate = 0.001   #学习速率
use_cuda = torch.cuda.is_available()    #gpu是否可用
#use_cuda = False
total_epoch = 1 #总共训练多少次
#进行记录
train_loss_list =list()
#train_acc_list = list()
Test_loss_list = list()
#Test_acc_list = list()

best_Test_loss = 100000000.0
best_Test_epoch = 0

# Data
print('==> Preparing data..')
#需要对图片进行一定的处理，处理成ImgeNet必备网络
transform_train = transforms.Compose([
     transforms.ToPILImage(),
     transforms.RandomResizedCrop(224),
     transforms.RandomHorizontalFlip(),
     transforms.ToTensor(),
    #transforms.Normalize([[0.485, 0.456, 0.406], [0.229, 0.224, 0.225]])
     ])

transform_test = transforms.Compose([
    transforms.ToPILImage(),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    #transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])
# 载入数据时使用的处理器个数,这个目前必须是0不然会报错
num_workers = 0
#读取数据
output_file = "data/feel_beautiful.pkl"
f = open(output_file, 'rb')
train_imgs, train_scores, test_imgs, test_scores = pickle.load(f)


trainset = TrainSet(train_imgs,train_scores,transform=transform_train)
trainloader = torch.utils.data.DataLoader(trainset, batch_size=batch_size, shuffle=True, num_workers=0)
Testset = TestSet(test_imgs,test_scores,transform=transform_test)
Testloader = torch.utils.data.DataLoader(Testset, batch_size=batch_size, shuffle=False, num_workers=0)

#建立模型
#net = torch.load('models/simpleModelPlus.pkl')
net = simpleModelPlus()
#net = resNet()
#print(net)
if use_cuda:
    net.cuda()

#定义误差的计算公式
loss_func  = nn.MSELoss()
#优化方法
optimizer = optim.Adam(net.parameters(), lr=learn_rate)


# Training
def train(epoch):
    print('\nEpoch: %d' % (epoch + 1))
    net.train()
    train_loss = 0
    total = 0

    for batch_idx, (inputs, targets) in enumerate(trainloader):

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()

        inputs, targets = Variable(inputs), Variable(targets)
        outputs = net(inputs)


        outputs = torch.squeeze(outputs)
        # 进行计算
        loss = loss_func(outputs.float(), targets.float())

        train_loss += loss.item()
        total += targets.size(0)
        # 开始优化步骤
        # 每次开始优化前将梯度置为0
        optimizer.zero_grad()
        # 误差反向传播
        loss.backward()
        # 按照最小loss优化参数
        optimizer.step()

    print('Training第{}轮 \t训练损失: {:.6f} '.format(epoch + 1, train_loss /total))

    train_loss_list.append(train_loss / total)
    # train_acc_list.append(Train_acc)


# 跟上面过程类似
def Test(epoch):
    global best_Test_loss
    global best_Test_epoch

    net.eval()
    test_loss = 0
    total = 0
    for batch_idx, (inputs, targets) in enumerate(Testloader):

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()

        with torch.no_grad():
            # inputs, targets = Variable(inputs, volatile=True), Variable(targets)
            inputs, targets = Variable(inputs), Variable(targets)
            outputs = net(inputs)

            outputs = torch.squeeze(outputs)
            # 进行计算
            loss = loss_func(outputs.float(), targets.float())
            test_loss += loss.item()
            total += targets.size(0)

    print('Test第{}轮 \t训练损失: {:.6f} '.format(epoch + 1, test_loss / total))
    test_loss = test_loss / total
    Test_loss_list.append(test_loss)

    # 将loss存起来
    if best_Test_loss > test_loss:
        print('Saving..')
        print("best_Test_loss: %0.3f" % test_loss)
        torch.save(net, 'model.pkl')  # 将模型存储下来
        best_Test_loss = test_loss
        best_Test_epoch = epoch


for epoch in range(total_epoch):
    train(epoch)
    Test(epoch)

print("best_Test_loss: %0.3f" % best_Test_loss)
print("best_Test_epoch: %d" % best_Test_epoch)

pickle_file1 = open("train_loss_list.pkl", "wb")  # 创建pkl数据文件
pickle_file2 = open("Test_loss_list.pkl", "wb")  # 创建pkl数据文件


pickle.dump(train_loss_list, pickle_file1)  #把数据装入已经建好的文件中
pickle.dump(Test_loss_list, pickle_file2)  #把数据装入已经建好的文件中


pickle_file1.close()  #关闭数据流
pickle_file2.close()  #关闭数据流
