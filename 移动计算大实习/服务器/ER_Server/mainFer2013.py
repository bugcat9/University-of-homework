import torch
import torch.nn as nn
from torchvision import transforms
import torch.optim as optim
from torch.autograd import Variable
import pickle
from dateset.fer import FER2013

#一些参数
from models.vggModel import vggModel

batch_size = 64#块的大小
learn_rate = 0.001   #学习速率
use_cuda = torch.cuda.is_available()    #gpu是否可用

total_epoch = 1 #总共训练多少次
public_best_Test_acc = 0.0
public_best_Test_epoch = 0
private_best_Test_acc = 0.0
private_best_Test_epoch = 0

#进行记录
train_loss_list =list()
train_acc_list = list()
public_Test_loss_list = list()
public_Test_acc_list = list()
privaye_Test_loss_list = list()
private_Test_acc_list = list()

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

trainset = FER2013(split ='Training', transform=transform_train)
trainloader = torch.utils.data.DataLoader(trainset, batch_size=batch_size, shuffle=True, num_workers=0)
PublicTestset = FER2013(split = 'PublicTest', transform=transform_test)
PublicTestloader = torch.utils.data.DataLoader(PublicTestset, batch_size=batch_size, shuffle=False, num_workers=0)
PrivateTestset = FER2013(split = 'PrivateTest', transform=transform_test)
PrivateTestloader = torch.utils.data.DataLoader(PrivateTestset, batch_size=batch_size, shuffle=False, num_workers=0)

#建立图
#net = torch.load('../input/simplemodelplus/simpleModelPlus.pkl')
net = vggModel()
if use_cuda:
    net.cuda()

#定义误差的计算公式
criterion  = nn.CrossEntropyLoss()
#优化方法
optimizer = optim.Adam(net.parameters(), lr=learn_rate)

# Training
def train(epoch):
    print('\nEpoch: %d' % (epoch + 1))
    net.train()
    train_loss = 0
    total = 0
    correct = 0
    Train_acc = 0
    for batch_idx, (inputs, targets) in enumerate(trainloader):

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()

        inputs, targets = Variable(inputs), Variable(targets)
        outputs = net(inputs)

        # 进行计算
        loss = criterion(outputs, targets)
        # 开始优化步骤
        # 每次开始优化前将梯度置为0
        optimizer.zero_grad()
        # 误差反向传播
        loss.backward()
        # 按照最小loss优化参数
        optimizer.step()
        train_loss += loss.item()
        total += targets.size(0)
        _, predicted = torch.max(outputs.data, 1)
        correct += predicted.eq(targets.data).cpu().sum()
        # print( 'Loss: %.3f | Acc: %.3f%% (%d/%d)'
        #     % (train_loss/(batch_idx+1), 100.*correct/total, correct, total))

    Train_acc = 100. * correct / total
    print('Training第%d 轮 \t训练 loss:%.3f   and  Acc:%.3f '%(epoch + 1, train_loss /total,Train_acc))

    train_loss_list.append(train_loss / total)#记录下来，方便后面画曲线
    train_acc_list.append(Train_acc)


# 跟上面过程类似
def PublicTest(epoch):
    global public_best_Test_acc
    global public_best_Test_epoch

    net.eval()
    correct = 0
    public_test_loss = 0
    total = 0
    for batch_idx, (inputs, targets) in enumerate(PublicTestloader):

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()

        with torch.no_grad():
            # inputs, targets = Variable(inputs, volatile=True), Variable(targets)
            inputs, targets = Variable(inputs), Variable(targets)
            outputs = net(inputs)

            # 进行计算
            loss = criterion(outputs, targets)
            # 开始优化步骤
            public_test_loss += loss.item()
            total += targets.size(0)
            _, predicted = torch.max(outputs.data, 1)
            correct += predicted.eq(targets.data).cpu().sum()

    public_acc = 100. * correct / total
    print('PublicTest第%d轮 \t训练 loss:%.3f   and  Acc:%.3f ' % (epoch + 1, public_test_loss / total, public_acc))

    public_Test_loss_list.append(public_test_loss / total)
    public_Test_acc_list.append(public_acc)

    # 将acc存起来以及比较好的模型存储起来
    if public_acc > public_best_Test_acc:
        print('Saving..')
        print("public_best_Test_acc: %0.3f" % public_acc)
        torch.save(net, 'public_model.pkl')  # 将模型存储下来
        public_best_Test_acc = public_acc
        public_best_Test_epoch = epoch

# 跟上面过程类似
def PrivateTest(epoch):
    global private_best_Test_acc
    global private_best_Test_epoch

    net.eval()
    correct = 0
    private_test_loss = 0
    total = 0
    for batch_idx, (inputs, targets) in enumerate(PrivateTestloader):

        if use_cuda:
            inputs, targets = inputs.cuda(), targets.cuda()

        with torch.no_grad():
            # inputs, targets = Variable(inputs, volatile=True), Variable(targets)
            inputs, targets = Variable(inputs), Variable(targets)
            outputs = net(inputs)

            outputs = torch.squeeze(outputs)
            # 进行计算
            loss = criterion(outputs, targets)
            private_test_loss += loss.item()
            total += targets.size(0)
            _, predicted = torch.max(outputs.data, 1)
            correct += predicted.eq(targets.data).cpu().sum()

    private_acc = 100. * correct / total
    print('PrivateTest第%d轮 \t训练 loss:%.3f   and  Acc:%.3f ' % (epoch + 1, private_test_loss / total, private_acc))

    privaye_Test_loss_list.append(private_test_loss / total)
    private_Test_acc_list.append(private_acc)

    # 将acc存起来以及比较好的模型存储起来
    if private_acc > private_best_Test_acc:
        print('Saving..')
        print("private_best_Test_acc: %0.3f" % private_acc)
        torch.save(net,'private_model.pkl')  # 将模型存储下来
        private_best_Test_acc = private_acc
        private_best_Test_epoch = epoch

for epoch in range(total_epoch):
    train(epoch)
    PublicTest(epoch)
    PrivateTest(epoch)

print('final!!!!!!!!!!!!!!!!!')


print("public_best_Test_acc: %0.3f" % public_best_Test_acc)
print("public_best_Test_epoch: %d" % public_best_Test_epoch)
print("private_best_Test_acc: %0.3f" % private_best_Test_acc)
print("private_best_Test_epoch: %d" % private_best_Test_acc)

pickle_file1 = open("train_loss_list.pkl", "wb")  # 创建pkl数据文件
pickle_file2 = open("train_acc_list.pkl", "wb")  # 创建pkl数据文件
pickle_file3 = open("public_Test_loss_list.pkl", "wb")  # 创建pkl数据文件
pickle_file4 = open("public_Test_acc_list.pkl", "wb")  # 创建pkl数据文件
pickle_file5 = open("privaye_Test_loss_list.pkl", "wb")  # 创建pkl数据文件
pickle_file6 = open("private_Test_acc_list.pkl", "wb")  # 创建pkl数据文件

pickle.dump(train_loss_list, pickle_file1)  #把数据装入已经建好的文件中
pickle.dump(train_acc_list, pickle_file2)  #把数据装入已经建好的文件中
pickle.dump(public_Test_loss_list, pickle_file3)  #把数据装入已经建好的文件中
pickle.dump(public_Test_acc_list, pickle_file4)  #把数据装入已经建好的文件中
pickle.dump(privaye_Test_loss_list, pickle_file5)  #把数据装入已经建好的文件中
pickle.dump(private_Test_acc_list, pickle_file6)  #把数据装入已经建好的文件中

pickle_file1.close()  #关闭数据流
pickle_file2.close()  #关闭数据流
pickle_file3.close()  #关闭数据流
pickle_file4.close()  #关闭数据流
pickle_file5.close()  #关闭数据流
pickle_file6.close()  #关闭数据流