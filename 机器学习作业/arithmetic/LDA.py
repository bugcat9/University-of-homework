import numpy as np

 # 特征均值,计算每类的均值，返回一个向量
def class_mean(data,label):
    mean_vectors = []
    clusters = np.unique(label)
    for cl in clusters:
        mean_vectors.append(np.mean(data[label==cl,],axis=0))
    #print mean_vectors
    return mean_vectors

# 计算类内散度
def within_class_SW(data,label):
    m = data.shape[1]
    S_W = np.zeros((m,m))
    mean_vectors = class_mean(data,label)
    clusters = np.unique(label)
    for cl ,mv in zip(clusters,mean_vectors):
        class_sc_mat = ((data[label == cl]-mv).T).dot(data[label == cl]-mv)
        S_W +=class_sc_mat
    # print(S_W)
    return S_W

#计算类间散度
def between_class_SB(data,label):
    m = data.shape[1]
    all_mean =np.mean(data,axis = 0)
    S_B = np.zeros((m,m))
    mean_vectors = class_mean(data,label)
    for cl ,mean_vec in enumerate(mean_vectors):
        n = data[label==cl].shape[0]
        mean_vec = mean_vec.reshape(m,1)
        all_mean = all_mean.reshape(m,1)
        S_B += n * (mean_vec - all_mean).dot((mean_vec - all_mean).T)
    return S_B

#数据的行是特征向量
def LDA(data,label,clusters):
    # data,label=read_iris()
    # clusters = 3
    S_W = within_class_SW(data,label)
    S_B = between_class_SB(data,label)
    print('sw',S_W)
    print('sb',S_B)
    # np.linalg.inv 是 求逆矩阵
    eig_vals, eig_vecs = np.linalg.eig(np.linalg.pinv(S_W).dot(S_B))
    index = np.argsort(-eig_vals)  # 按照featValue进行从大到小排序
    selectVec = eig_vecs[:, index[:clusters]]
    return data.dot(selectVec)


if __name__ == '__main__':
    a = np.array([1,3])
    b = np.array([2,1])
    c = np.array([3,4])
    d = np.array([4,3])
    x = np.vstack((a,b,c,d))
    print(x)
    means = np.mean(x,axis=0)
    print(means)
