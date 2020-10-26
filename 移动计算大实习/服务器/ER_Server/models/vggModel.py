import torch
import torch.nn as nn
import torchvision.models as models
import torch.nn.functional as F

# 自定义一个Net类，继承于torch.nn.Module类,采用迁移学习的相关知识进行
class vggModel(torch.nn.Module):
    # Net类的初始化函数
    def __init__(self):
        super(vggModel, self).__init__()
        self.features = models.vgg19(pretrained=True).features[:37]
        self.avgpool= nn.AdaptiveAvgPool2d(output_size=(7, 7))
        self.classifier = torch.nn.Sequential(torch.nn.Linear(in_features=25088, out_features=7, bias=True))
    # 网络的前向传播函数，构造计算图
    def forward(self, x):
        out = self.features(x)
        out = self.avgpool(out)
        out = out.view(out.size(0), -1)
        out = F.dropout(out, p=0.5, training=self.training)#使用dropout防止过拟合
        out = self.classifier(out)
        return out

if __name__=="__main__":
    Net=vggModel()
    torch.save(Net, 'vggModel.pkl')  # 将模型存储下来
    print(Net)