import numpy as np

# #数据的行是特征向量
# def SVD(data,k):
#     #np.linalg.eigh用于对称矩阵
#     #求A^T*A
#     eig_vals1,eig_vects1 = np.linalg.eigh((data.T).dot(data))
#     # 需要进行排序argsort是从小到大排序，所以需要加个负号
#     index1 = np.argsort(-eig_vals1)  # 按照Value进行从大到小排序
#     V = eig_vects1[:,index1[:k]]    #得到V
#     print('V',V.T)
#
#     eig_vals2, eig_vects2 = np.linalg.eigh(np.dot(data,data.T))
#     index2 = np.argsort(-eig_vals2)  # 按照Value进行从大到小排序
#     U = eig_vects2[:, index2[:k]]
#     print('U',U)
#
#     sigma = np.zeros((len(U), len(V)))#得到sigma
#     if len(index1)<len(index2):
#         index = index1
#         eig_vals = eig_vals1
#     else:
#         index = index2
#         eig_vals = eig_vals2
#
#     for i in range(len(index)):
#         sigma[i][i] = eig_vals[index[i]] ** 0.5
#     print('sigma',sigma)
#     sigma = sigma[:k,:]
#     finaldata = np.dot(U,sigma).dot(V.T)
#     return finaldata

#最终版本
def SVD(data,k):
    U, Sigma, VT = np.linalg.svd(data)
    V = np.transpose(VT[:k,:])
    return np.dot(data,V)



if __name__ == '__main__':
    a = [0,1]
    b = [1,1]
    c = [1,0]
    data = np.vstack((a,b,c))
    print('A',SVD(data,3))
    U, Sigma, VT = np.linalg.svd(data)
    print('U',U)
    print('V',VT)
    sigma = np.zeros((len(U), len(VT)))  # 得到sigma
    for i in range(len(Sigma)):
        sigma[i][i] = Sigma[i]
    print('Sigma',sigma)

    sval_nums = 3
    finaldata = np.dot(U, sigma).dot(VT)
    print('A',finaldata)


