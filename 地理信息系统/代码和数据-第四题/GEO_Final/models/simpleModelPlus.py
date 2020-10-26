import torch
import torch.nn as nn
import torchvision.models as models
import torch.nn.functional as F

# 自定义一个Net类，继承于torch.nn.Module类,采用迁移学习的相关知识进行
class simpleModelPlus(torch.nn.Module):
    # Net类的初始化函数
    def __init__(self):
        super(simpleModelPlus, self).__init__()
		#使用vgg16前面30层
        self.features = models.vgg16(pretrained=True).features[:31]
		#添加平均池化层
        self.avgpool= nn.AdaptiveAvgPool2d(output_size=(7, 7))
		#添加两层全连接层
        self.classifier = torch.nn.Sequential(torch.nn.Linear(in_features=25088, out_features=4096, bias=True),
                                       torch.nn.ReLU(inplace=True),
                                       torch.nn.Dropout(p=0.5, inplace=False),
                                       torch.nn.Linear(in_features=4096, out_features=1, bias=True),
                                       )

    # 网络的前向传播函数，构造计算图
    def forward(self, x):
        out = self.features(x)
        out = self.avgpool(out)
        out = out.view(out.size(0), -1)
        out = self.classifier(out)
        return out

if __name__=="__main__":
    Net=simpleModelPlus()
    torch.save(Net, 'simpleModelPlus.pkl')  # 将模型存储下来
    print(Net)