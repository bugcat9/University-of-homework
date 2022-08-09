import numpy as np

def CUR(A, n):
    A_sq = A ** 2
    sum_A_sq = np.sum(A_sq)
    sum_A_sq_0 = np.sum(A_sq, axis=0)
    sum_A_sq_1 = np.sum(A_sq, axis=1)

    P_x_c = sum_A_sq_0 / sum_A_sq
    P_x_r = sum_A_sq_1 / sum_A_sq

    r, c = A.shape

    c_index = [np.random.choice(np.arange(0, c), p=P_x_c) for i in range(n)]
    r_index = [np.random.choice(np.arange(0, r), p=P_x_r) for i in range(n)]
    #     print(c_index, r_index)
    C = A[:, c_index]
    R = A[r_index, :]
    W = C[r_index]
    #     print(C, R, W)

    def SVD(A, n):
        M = np.dot(A, A.T)
        eigval, eigvec = np.linalg.eig(M)
        indexes = np.argsort(-eigval)[:n]
        U = eigvec[:, indexes]
        sigma_sq = eigval[indexes]
        M = np.dot(A.T, A)
        eigval, eigvec = np.linalg.eig(M)
        indexes = np.argsort(-eigval)[:n]
        V = eigvec[:, indexes]
        sigma = sigma_sq  # not diag and not sqrt
        return U, sigma, V

    X, sigma, Y = SVD(W, n)
    for i in range(len(sigma)):
        if sigma[i] == 0:
            continue
        else:
            sigma[i] = 1 / sigma[i]
    sigma = np.diag(sigma)
    U = np.dot(np.dot(Y, sigma), X.T)
    return np.dot(np.dot(C, U), R)
