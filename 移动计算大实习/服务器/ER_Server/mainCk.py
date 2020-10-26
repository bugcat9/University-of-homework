import torch
import torch.nn as nn
from torchvision import transforms
import torch.optim as optim
from torch.autograd import Variable
import pickle
import numpy as np
from dateset.CK import CK
from dateset.fer import FER2013
from models.vggModel import vggModel
#一些参数


batch_size = 8#块的大小
learn_rate = 0.01   #学习速率
use_cuda = torch.cuda.is_available()    #gpu是否可用

total_epoch = 10 #总共训练多少次

best_Test_acc = 0.0
best_Test_epoch = 0

#进行记录
train_loss_list =list()
train_acc_list = list()
Test_loss_list = list()
Test_acc_list = list()

cut_size = 44
#需要对图片进行一定的处理，处理成ImgeNet必备网络
transform_train = transforms.Compose([
     transforms.RandomResizedCrop(cut_size),
     transforms.RandomHorizontalFlip(),
     transforms.ToTensor(),
     ])

transform_test = transforms.Compose([
    transforms.CenterCrop(cut_size),
    transforms.ToTensor(),
])

trainset = CK(split = 'Training', transform=transform_train)
trainloader = torch.utils.data.DataLoader(trainset, batch_size=batch_size, shuffle=True, num_workers=0)
Testset = CK(split = 'Testing', transform=transform_test)
Testloader = torch.utils.data.DataLoader(Testset, batch_size=batch_size, shuffle=False, num_workers=0)

#建立图
#net = torch.load('../input/vggmodel/vggModel.pkl')
net = vggModel()
#net = torch.load('../input/vggmodel/vggModel.pkl')
if use_cuda:
    net.cuda()

#定义误差的计算公式
criterion  = nn.CrossEntropyLoss()
#优化方法
optimizer = optim.Adam(net.parameters(), lr=learn_rate)

# Training
def train(epoch):
    print('\nEpoch: %d' % (epoch + 1))
    global Train_acc
    net.train()
    train_loss = 0
    correct = 0
    total = 0

    for batch_idx, (inputs, targets) in enumerate(trainloader):

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()

        optimizer.zero_grad()
        inputs, targets = Variable(inputs), Variable(targets)
        outputs = net(inputs)
        loss = criterion(outputs, targets)
        loss.backward()
        optimizer.step()

        train_loss += loss.item()
        _, predicted = torch.max(outputs.data, 1)
        total += targets.size(0)
        correct += predicted.eq(targets.data).cpu().sum()
        # print( 'Loss: %.3f | Acc: %.3f%% (%d/%d)'
        #     % (train_loss/(batch_idx+1), 100.*correct/total, correct, total))

    Train_acc = 100. * correct / total
    print('Training第%d轮 \t训练 loss:%.3f   and  Acc:%.3f ' % (epoch + 1, train_loss / total, Train_acc))

    train_loss_list.append(train_loss / total)  # 记录下来，方便后面画曲线
    train_acc_list.append(Train_acc)


# 跟上面过程类似
def Test(epoch):
    global Test_acc
    global best_Test_acc
    global best_Test_epoch
    net.eval()
    test_loss = 0
    correct = 0
    total = 0
    for batch_idx, (inputs, targets) in enumerate(Testloader):
        # bs, ncrops, c, h, w = np.shape(inputs)
        # inputs = inputs.view(-1, c, h, w)

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()
        inputs, targets = Variable(inputs, volatile=True), Variable(targets)
        outputs = net(inputs)
        # outputs_avg = outputs.view(bs, ncrops, -1).mean(1)  # avg over crops
        loss = criterion(outputs, targets)
        test_loss += loss.item()
        _, predicted = torch.max(outputs.data, 1)
        total += targets.size(0)
        correct += predicted.eq(targets.data).cpu().sum()

    Test_acc = 100. * correct / total
    print('Test第%d轮 \t训练 loss:%.3f   and  Acc:%.3f ' % (epoch + 1, test_loss / total, Test_acc))

    Test_loss_list.append(test_loss / total)
    Test_acc_list.append(Test_acc)

    # 将acc存起来以及比较好的模型存储起来
    if Test_acc > best_Test_acc:
        print('Saving..')
        print("best_Test_acc: %0.3f" % Test_acc)
        torch.save(net, 'ck_best_model.pkl')  # 将模型存储下来
        best_Test_acc = Test_acc
        best_Test_epoch = epoch


for epoch in range(total_epoch):
    train(epoch)
    Test(epoch)

print('final!!!!!!!!!!!!!!!!!')

print("best_Test_acc: %0.3f" % best_Test_acc)
print("best_Test_epoch: %d" % best_Test_epoch)

pickle_file1 = open("train_loss_list.pkl", "wb")  # 创建pkl数据文件
pickle_file2 = open("train_acc_list.pkl", "wb")  # 创建pkl数据文件
pickle_file3 = open("Test_loss_list.pkl", "wb")  # 创建pkl数据文件
pickle_file4 = open("Test_acc_list.pkl", "wb")  # 创建pkl数据文件

pickle.dump(train_loss_list, pickle_file1)  #把数据装入已经建好的文件中
pickle.dump(train_acc_list, pickle_file2)  #把数据装入已经建好的文件中
pickle.dump(Test_loss_list, pickle_file3)  #把数据装入已经建好的文件中
pickle.dump(Test_acc_list, pickle_file4)  #把数据装入已经建好的文件中


pickle_file1.close()  #关闭数据流
pickle_file2.close()  #关闭数据流
pickle_file3.close()  #关闭数据流
pickle_file4.close()  #关闭数据流