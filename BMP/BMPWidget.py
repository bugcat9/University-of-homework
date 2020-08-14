'''
Created on 2019年4月30日

@author: zhouning
'''

import sys
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from BMPCompress  import *

class BmpWidget(QWidget):
    def __init__(self):
        super().__init__()
        self.initUI()
    
    def initUI(self):        
        self.setWindowTitle('BMP')
        self.setFixedSize(600,600)
        self.setAcceptDrops(True)
        
        grid=QGridLayout()
        self.setLayout(grid)
        
        Hbox=QHBoxLayout()
        label=QLabel('文件路径',self)
        self.lineEdit=QLineEdit(self)
        openButton=QPushButton('打开文件',self)
        openButton.clicked.connect(self.openFileDialog)
        Hbox.addWidget(label)
        Hbox.addWidget(self.lineEdit)
        Hbox.addWidget(openButton)
        
        grid.addLayout(Hbox,0,0,Qt.AlignTop)    
    
    
        #进度条
        self.processBar=QProgressBar(self)
        self.processBar.setRange(0,100)
        self.processBar.setValue(0)
        self.processBar.resize(600,20)
        self.processBar.move(0,300)
       
        compressBut=QPushButton('压缩',self)
        compressBut.move(0,500)
        compressBut.clicked.connect(self.compress)
        
        uncompressBut=QPushButton('解压',self) 
        uncompressBut.move(500,500)
        uncompressBut.clicked.connect(self.uncompress)
    #拖拉识别文件
    def dragEnterEvent(self, event):
        a=event.mimeData().text()
        self.lineEdit.setText(a[8:])
    
    
    def openFileDialog(self):
       filename= QFileDialog.getOpenFileName(self,
                                             '选择文件',
                                             '../',
                                             'All Files (*);;BMP(*bmp);;ZNIP(*znip)')
       
    def  compress(self):
        filename=self.lineEdit.text()
        print(filename)
        self.value=0
        self.processBar.setValue(0)
        self.mythread=CompressBmp(filename)
        self.mythread._signal.connect(self.changValue)
        self.mythread.start()
    
    def uncompress(self):
        filename=self.lineEdit.text()
        print(filename)
        self.value=0
        self.processBar.setValue(0)
        self.mythread=unCompressBmp(filename)
        self.mythread._signal.connect(self.changValue)
        self.mythread.start()
        
    def changValue(self):
        self.value+=1 
        self.processBar.setValue(self.value)
if __name__ == '__main__':
     
    app = QApplication(sys.argv)
    ex = BmpWidget()
    ex.show()
    sys.exit(app.exec_())