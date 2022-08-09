import scipy.io as scio
from sklearn import decomposition
import sklearn.discriminant_analysis
from arithmetic.LDA import LDA
from arithmetic.PCA import PCA
from arithmetic.SVD import SVD
import string
import numpy as np
from matplotlib import pyplot as plt
import matplotlib as mpl

map_color = {0: 'g', 1: 'r', 2: 'b', 3: 'c', 4: 'y', 5: 'm'}
mpl.rcParams['font.sans-serif'] = ['SimHei']  # 指定默认字体 SimHei为黑体
mpl.rcParams['axes.unicode_minus'] = False  # 用来正常显示负号

#从data里面加载数据
def load_data():
    # dataFile = 'data/BU3D_feature.mat'
    dataFile = 'data/BU3D-bq-final.mat'

    data = scio.loadmat(dataFile)['data']
    label = data[:,-1]
    eigenvector = data[:,0:-1]
    return (eigenvector,label)


# 读取鸢尾花数据集,特征依次是：花萼长度,花萼宽度,花瓣长度,花瓣宽度
def read_iris():
    from sklearn.datasets import load_iris
    data_set = load_iris()
    data_x = data_set.data
    label = data_set.target
    return data_x,label


def plot_PCA():
    data, label = load_data()
    print(np.shape(data))

    pca = sklearn.decomposition.PCA(n_components=2)
    pcaData = PCA(data, 2)
    pypcaData = pca.fit_transform(data)

    color = list(map(lambda x: map_color[x], label))

    plt.figure()
    plt.subplot(121)
    plt.scatter(np.array(pcaData[:, 0]), np.array(pcaData[:, 1]), c=color)
    plt.title('PCA:自己的算法')

    plt.subplot(122)
    plt.scatter(np.array(pypcaData[:, 0]), np.array(pypcaData[:, 1]), c=color)
    plt.title('PCA:sklearn中的算法')

    plt.show()

def plot_LDA():
    data, label = load_data()
    print(np.shape(data))

    lda = sklearn.discriminant_analysis.LinearDiscriminantAnalysis(n_components=3)
    ldaData = LDA(data, label,3)
    ldaData = np.real(ldaData)
    pyldaData = lda.fit(data, label).transform(data)

    color = list(map(lambda x: map_color[x], label))

    plt.figure()
    plt.subplot(121)
    plt.scatter(np.array(ldaData[:, 0]), np.array(ldaData[:, 1]), c=color)
    plt.title('LDA:自己的算法')

    plt.subplot(122)
    plt.scatter(np.array(pyldaData[:, 0]), np.array(pyldaData[:, 1]), c=color)
    plt.title('LDA:sklearn中的算法')

    plt.show()

def plot_SVD():
    data, label = load_data()
    print(np.shape(data))

    tsvd = decomposition.TruncatedSVD(n_components=2)

    svdData = SVD(data, 2)

    pysvdData = tsvd.fit_transform(data)

    color = list(map(lambda x: map_color[x], label))

    plt.figure()
    plt.subplot(121)
    plt.scatter(np.array(svdData[:, 0]), np.array(svdData[:, 1]), c=color)
    plt.title('SVD:自己的算法')

    plt.subplot(122)
    plt.scatter(np.array(pysvdData[:, 0]), np.array(pysvdData[:, 1]), c=color)
    plt.title('SVD:sklearn中的算法')

    plt.show()

def Parser_email(emailPath):
    '''
    将email解析成一个单词数组返回
    :return: 返回一个email的单词数组
    '''
    words = []
    with open(emailPath,'r',encoding='gb18030', errors='ignore') as f:
        for line in f.readlines():
            for word in line.split():
                word = word.strip('\n')                 #去除，头部和末尾的 '\n'
                # word = word.strip(string.digits)        #去除头和尾的数字
                word = word.strip(string.punctuation)   #去除头和尾符号
                if word.count(string.digits)>0 or word.count(string.punctuation)>0:
                    continue
                if len(word)>0:
                    words.append(word)
    # print(len(words))
    return words

#在贝叶斯里面被调用
def creatEmailSet(x_id, label):
    '''
    创造对应的垃圾邮件和非垃圾邮件的数据集，
    其中垃圾邮件20封作为训练集5封作为测试集
    非垃圾邮件20封作为训练集5封作为测试集
    :param x_id:对应的下标
    :param label:对应的类别
    :return: hamList 和 spamList
    '''
    hamList = []     #非垃圾邮件词库
    spamList = []    #垃圾邮件词库

    hamPath = '../data/email/ham/{}.txt'
    spamPath = '../data/email/spam/{}.txt'



    for i,j in zip(x_id,label):
        # 因为垃圾邮件和非垃圾邮件各有25封，将垃圾邮件放在非垃集邮件后面，
        # 所以下标需要进行这样处理
        i = i%25+1
        if j==0:    #说明是非垃集邮件
            path = hamPath.format(i)
            hamList.extend(Parser_email(path))
        else:
            path = spamPath.format(i)
            spamList.extend(Parser_email(path))

    return hamList,spamList



def CountTokens(words):
    '''
    统计词频
    :param words:词库数组
    :return:dict[],key：单词，value：出现的频率
    '''
    word_count_dict =dict()
    for word in words:
        count=word_count_dict.get(word,0)
        word_count_dict[word] = count+1
    return word_count_dict

if __name__=="__main__":
    # hamSet,spamSet = creatEmailSet()
    # print(hamSet)
    # print(spamSet)
    path = 'data/email/ham/1.txt'
    Parser_email(path)

