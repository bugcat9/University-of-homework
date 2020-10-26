from PIL import Image
import numpy as np
import h5py
import torch.utils.data as data
import pickle
import cv2

#训练数据集
class TrainSet(data.Dataset):
    def __init__(self,data,lable,transform=None):
        self.transform = transform
        self.train_data = data
        self.train_labels = lable

    def __getitem__(self, index):
        img, target = self.train_data[index], float(self.train_labels[index])
        if self.transform is not None:
            img = self.transform(img)
        return img, target

    def __len__(self):
        return len(self.train_data)
		
#测试数据集
class TestSet(data.Dataset):
    def __init__(self, data, lable, transform=None):
        self.transform = transform
        self.test_data = data
        self.test_labels = lable

    def __getitem__(self, index):
        img, target = self.test_data[index], float(self.test_labels[index])
        if self.transform is not None:
            img = self.transform(img)
        return img, target

    def __len__(self):
        return len(self.test_data)

def pickle_load(output_file):  # 使用pickle从文件中重构python对象
    f = open(output_file, 'rb')
    train_imgs,train_scores,test_imgs,test_scores = pickle.load(f)
    for score in train_scores:
        print(score)
    for img in train_imgs:
        cv2.imshow('121',img)
        cv2.waitKey(1)

    print(type(train_imgs[0]))
    print(type(train_scores))
    print(type(test_imgs))
    print(type(test_scores))
    f.close()




if __name__ =="__main__":
    output_file = "data/feel_beautiful.pkl"
    f = open(output_file, 'rb')
    train_imgs, train_scores, test_imgs, test_scores = pickle.load(f)
    trainset = TrainSet()
    print(np.shape(trainset.train_data))
