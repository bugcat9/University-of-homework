import torch
import torch.nn as nn
import torchvision.models as models
import torch.nn.functional as F

# 自定义一个Net类，继承于torch.nn.Module类,采用迁移学习的相关知识进行
class resNet(torch.nn.Module):
    # Net类的初始化函数
    def __init__(self):
        super(resNet, self).__init__()
		#使用预先训练好的resnet18
        model = models.resnet18(pretrained=True)
        feature = torch.nn.Sequential(*list(model.children())[:])
        self.features = feature[:8]
        self.avgpool = feature[8:9]
		#修改最后一层fc层
        self.fc = nn.Linear(in_features=512, out_features=1, bias=True)

    # 网络的前向传播函数，构造计算图
    def forward(self, x):
        out = self.features(x)
        out = self.avgpool(out)
        out = out.view(out.size(0), -1)
        out = self.fc(out)
        return out


if __name__=="__main__":
    Net=resNet()
    torch.save(Net, 'resNet.pkl')  # 将模型存储下来
    print(Net)