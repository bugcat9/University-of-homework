'''
垃圾邮件检测，所以是一个二分类问题
训练采取40封邮件，测试集10封邮件
'''
from util import creatEmailSet, Parser_email, CountTokens
import numpy as np

from sklearn.model_selection import train_test_split




def classifyNB(words,prior,word_count_list,totalCount,kindOfAllWord):
    '''
    判定是否是垃圾邮件
    :param words: 需要判断的单词数组
    :param prior: 先验概率数组，在这里先验概率数组的大小为2
    :param word_count_list: 垃圾邮件和非垃圾邮件的词库数组，里面的元素是字典
    :param totalCount:  单词总数数组
    :param kindOfAllWord:   总单词种类
    :return:各种类别相对应的分数
    '''

    socre = []
    #将先验概率加入
    for i in prior:
        socre.append(np.log(i))
        # socre.append(i)


    for i in range(len(socre)):
        for word in words:
            #需要计算word的条件概率
            count = word_count_list[i].get(word,0) +1 #采用了拉普拉斯平滑
            probability = count/(totalCount[i]+kindOfAllWord)
            socre[i] += np.log(probability)
            # socre[i] *= probability

    return socre

def main():
    x = np.arange(0, 50)  # 将垃圾邮件和非垃集放在一起，前25为非垃集，后25为垃圾邮件
    y = [0] * 25 + [1] * 25

    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.20)  # 随机分割出来
    # 得到训练集词库
    hamSet, spamSet = creatEmailSet(x_train, y_train)
    hamtCount = len(hamSet)
    spamtCount = len(spamSet)
    totalSet = hamSet + spamSet

    # 计算先验概率
    PriorP_ham = y_train.count(0) / len(y_train)  # 非垃圾邮件的先验概率
    PriorP_spam = y_train.count(1) / len(y_train)  # 垃圾邮件的先验概率

    # 计算词频
    hamWord_count_dict = CountTokens(hamSet)
    spamWord_count_dict = CountTokens(spamSet)
    totalWord = CountTokens(totalSet)

    kindOfAllWord = len(totalWord)  #得到所有单词的种类

    hamPath = '../data/email/ham/{}.txt'
    spamPath = '../data/email/spam/{}.txt'

    accuracy = 0.
    for i, j in zip(x_test, y_test):
        i = i % 25 + 1  # 因为垃圾邮件和非垃圾邮件各有25封
        if j == 0:  # 说明是非垃集邮件
            path = hamPath.format(i)

        else:
            path = spamPath.format(i)

        words = Parser_email(path)
        # words = ['Chinese', 'Chinese', 'Chinese', 'Tokyo', 'Japan']
        prior = [PriorP_ham, PriorP_spam]
        word_count_list = [hamWord_count_dict, spamWord_count_dict]
        totalCount = [hamtCount, spamtCount]

        socre = classifyNB(words, prior, word_count_list, totalCount, kindOfAllWord)
        # print(socre)
        # print(max(socre))
        # print(j)
        # 如果是0就是非垃圾邮件，1是垃圾邮件
        if socre.index(max(socre)) == j:
            accuracy += 1
    accuracy = accuracy / len(y_test)
    print('准确度为：',accuracy)

if __name__ == '__main__':
    k = 5
    for i in range(k):
        main()