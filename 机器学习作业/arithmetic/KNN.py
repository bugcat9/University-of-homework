import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from util import creatEmailSet, CountTokens, Parser_email
from scipy import spatial
#


hamPath = '../data/email/ham/{}.txt'
spamPath = '../data/email/spam/{}.txt'

def generateWordSet(x_id,label):
    '''
    产生词库
    :param x_id:转化为单词的id，如： 1.txt 的id 为0以此类推
    :param label:对应的类型
    :return:set类型的词库
    '''
    Lexicon = set()  # 词库
    # 生成词库
    for i, j in zip(x_id, label):
        i = i % 25 + 1  # 因为垃圾邮件和非垃圾邮件各有25封
        if j == 0:  # 说明是非垃集邮件
            path = hamPath.format(i)

        else:
            path = spamPath.format(i)

        words = Parser_email(path)
        for word in words:
            Lexicon.add(word)
    return Lexicon

def word2Vec(words,wordList):
    '''
    将单词转化为特征向量
    :param words:单词的数组
    :param wordList:词库
    :return:
    '''
    vec = np.zeros(len(wordList))
    words = CountTokens(words)  #计算单词的词频
    for i in range(len(wordList)):
        vec[i] = words.get(wordList[i],0)

    return vec

def kNearestNeighbor(k,test_words_vec,train_words_vec_List):
    #找出距离最短的k个邻居
    neighbors = [-1]*k
    # distanceLsit = [float('inf')]*k
    distanceLsit = [-1]*k
    for index,trian_words_vec in enumerate(train_words_vec_List):
        distance = spatial.distance.cosine(test_words_vec,trian_words_vec)
        # distance = spatial.distance.euclidean(test_words_vec,trian_words_vec)

        #理论上使用二分法进行排序比较好，此次就使用简单的遍历然后排序，因为一般来说k比较小
        for i in range(k):
            if distance>distanceLsit[i]:
                distanceLsit.insert(i,distance)
                distanceLsit.pop()
                neighbors.insert(i,index)
                neighbors.pop()
                break

    return neighbors,distanceLsit


def main(k):
    x = np.arange(0, 50)  # 将垃圾邮件和非垃集放在一起，前25为非垃集，后25为垃圾邮件
    y = [0] * 25 + [1] * 25

    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.20)  # 随机分割出来
    # 得到训练集词库
    Lexicon = generateWordSet(x_train,y_train)  # 词库
    Lexicon = list(Lexicon)
    train_words_vec_List = []

    for i, j in zip(x_train, y_train):
        i = i % 25 + 1  # 因为垃圾邮件和非垃圾邮件各有25封
        if j == 0:  # 说明是非垃集邮件
            path = hamPath.format(i)
        else:
            path = spamPath.format(i)
        words = Parser_email(path)
        #得到单词向量
        train_words_vec_List.append(word2Vec(words,Lexicon))

    accuracy = 0.
    test_words_vec_List =[]
    for i, j in zip(x_test, y_test):
        i = i % 25 + 1
        if j == 0:
            path = hamPath.format(i)
        else:
            path = spamPath.format(i)

        words = Parser_email(path)
        # 得到单词向量
        test_words_vec=word2Vec(words, Lexicon)

        k = 4
        neighbors,distanceLsit = kNearestNeighbor(k,test_words_vec,train_words_vec_List)
        # print(distanceLsit,neighbors)
        hamcount = 0
        for index in neighbors:
            if y_train[index]==0:
                hamcount+=1
        if hamcount > k/2 :
            # print(1 , j)
            if j==1:
                accuracy+=1;
        else:
            # print(0 ,j)
            if j==0:
                accuracy+=1

    accuracy = accuracy / len(y_test)
    print('k=',k,'准确度为：', accuracy)



def use_skearn():
    x = np.arange(0, 50)  # 将垃圾邮件和非垃集放在一起，前25为非垃集，后25为垃圾邮件
    y = [0] * 25 + [1] * 25

    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.20)  # 随机分割出来
    # 得到训练集词库
    Lexicon = generateWordSet(x_train, y_train)  # 词库
    Lexicon = list(Lexicon)
    train_words_vec_List = []

    for i, j in zip(x_train, y_train):
        i = i % 25 + 1  # 因为垃圾邮件和非垃圾邮件各有25封
        if j == 0:  # 说明是非垃集邮件
            path = hamPath.format(i)
        else:
            path = spamPath.format(i)
        words = Parser_email(path)
        # 得到单词向量
        train_words_vec_List.append(word2Vec(words, Lexicon))

    neigh = KNeighborsClassifier(n_neighbors=3)  # 定义一个KNN分类器
    neigh.fit(np.array(train_words_vec_List), np.array(y_train))  # 训练分类器
    test_words_vec_List = []
    for i, j in zip(x_test, y_test):
        i = i % 25 + 1
        if j == 0:
            path = hamPath.format(i)
        else:
            path = spamPath.format(i)

        words = Parser_email(path)
        # 得到单词向量
        test_words_vec = word2Vec(words, Lexicon)
        test_words_vec_List.append(test_words_vec)

    predict = neigh.predict(np.array(test_words_vec_List))  # 预测样本属于哪个类
    print("predict:", predict)
    print("y_test:", np.array(y_test))
    score = neigh.score(np.array(test_words_vec_List), np.array(y_test), sample_weight=None)  # 计算预测的准确度
    print("准确度为:", score)


if __name__ == '__main__':
    k = 5
    for i in range(k):
        use_skearn()