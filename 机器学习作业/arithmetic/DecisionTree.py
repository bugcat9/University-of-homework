from util import read_iris
import numpy as np
import pandas as pd
'''
python决策树实现,使用的是最传统的ID3实现
'''


#pd.value_counts作用取特定的数量

# 读取数据集
data,label =  read_iris()

#计算香浓熵，香浓熵代表了混乱度
def calEnt(label):
    n = label.shape[0]
    p = pd.value_counts(label)/n
    ent = (-p*np.log2(p)).sum()
    return ent

#选择最好的属性
#data的行是一列数据
#data的列代表相对应的属性
def ChooseBestArrt(data,lable):
    baseEnt = calEnt(label)  # 计算原始熵
    bestGain = 0  # 初始化信息增益
    axis = -1
    for i in range(data.shape[1]):
        levels = pd.value_counts(data[:,i]).index  # 提取出当前列的所有取值
        ents = 0  # 初始化子节点的信息熵
        for j in levels:  # 对当前列的每一个取值进行循环
            childSet = data[data[:, i] == j]  # 某一个子节点的dataframe
            ent = calEnt(childSet)  # 计算某一个子节点的信息熵
            ents += (childSet.shape[0] / data.shape[0]) * ent  # 计算当前列的信息熵
        # print(f'第{i}列的信息熵为{ents}')
        infoGain = baseEnt - ents  # 计算当前列的信息增益
        # print(f'第{i}列的信息增益为{infoGain}')
        if (infoGain > bestGain):
            bestGain = infoGain  # 选择最大信息增益
            axis = i  # 最大信息增益所在列的索引
    return axis





ent = ChooseBestArrt(data,label)
print(ent)