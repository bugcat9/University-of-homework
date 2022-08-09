import numpy as np

#k为选取几个主成分
# data数据是行代表特征向量
def PCA(data,k):
    #求不求均值没有影响

    #rowvar:默认为True,此时每一行代表一个变量（属性），每一列代表一个观测；为False时，则反之
    covMat = np.cov(data,rowvar=0)   #得到协方差矩阵
    featValue, featVec = np.linalg.eig(covMat) #得到协方差矩阵的特征值和特征向量

    #需要进行排序argsort是从小到大排序，所以需要加个负号
    index = np.argsort(-featValue)  # 按照featValue进行从大到小排序

    # 注意特征向量时列向量，而numpy的二维矩阵(数组)a[m][n]中，a[1]表示第1行值
    selectVec = featVec[:,index[:k]]
    # 这里需要进行转置
    finalData = np.dot(data,selectVec)

    return finalData

if __name__ == '__main__':
    a = [1,2]
    b = [2,1]
    c = [3,4]
    d = [4,3]
    data = np.vstack((a,b,c,d))
    print(np.shape(data))
    finaldata=PCA(data, 1)
    print(np.shape(finaldata))
    print(finaldata)