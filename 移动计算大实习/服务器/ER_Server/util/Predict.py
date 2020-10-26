import torch
import torch.nn as nn
import torch.optim as optim
import torch.nn.functional as F
import torch.backends.cudnn as cudnn
import torchvision
from torchvision import transforms
import numpy as np
from PIL import Image, ImageDraw
from models.vggModel import vggModel

test_image = 'images/6.png'
#model_path = 'ck/ck_best_model.pkl'
model_path = '../util/fer/private_model.pkl'
#model_path = 'fer/private_model.pkl'
def predict(image_path):
    net = torch.load(model_path)
    cut_size = 44
    transform_test = transforms.Compose([
    #transforms.CenterCrop(cut_size),
    transforms.ToTensor(),
    ])

    use_cuda = torch.cuda.is_available()    #gpu是否可用
    if use_cuda:
      net.cuda()
    net.eval()

    img = Image.open(image_path)  # 读取图片
    img = img.convert("RGB")

    input = transform_test(img)
    input = torch.unsqueeze(input, 0)
    if use_cuda:
        input = input.cuda()

    with torch.no_grad():
        input = torch.autograd.Variable(input)
        output = net(input)
        print(output)
        print(output.argmax().item())
    return output.argmax().item()
#  0=anger 1=disgust, 2=fear, 3=happy, 4=sadness, 5=surprise , 6=contempt

if __name__ =='__main__':
    predict(test_image)