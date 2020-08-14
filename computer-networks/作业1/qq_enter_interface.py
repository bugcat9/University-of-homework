'''
Created on 2019年3月11日

@author: zhouning
'''

from tkinter import *
import tkinter.messagebox #重要

ADDR = ('202.114.196.97', 21568)
class  EnterWidget(Frame):
    
    def __init__(self,parent):
        super().__init__(parent)
        
        self.EnterWidget=Frame(parent)
        
        self.EnterWidget.grid()
        #用户名密码的标签
        self.parent=parent
        self.parent.title('登录界面')
        self.nameLable=Label(self.EnterWidget,text='用户名:')
        self.passwordLabel=Label( self.EnterWidget,text='密码:')

        #用户名，密码的输入
        self.name=Entry(self.EnterWidget)
        self.password=Entry( self.EnterWidget,show='*')

        #登录按钮
        self.loginButton=Button(self.EnterWidget,text='登录',command=self.Log_in)

        #进行布局
        self.nameLable.grid(row=0,column=0,padx=10,pady=10)
        self.name.grid(row=0,column=1,padx=10,pady=10)

        self.passwordLabel.grid(row=1,column=0,padx=10,pady=10)
        self.password.grid(row=1,column=1,padx=10,pady=10)
        self.loginButton.grid(row=2)
        
        #判断是否登录了
        self.Tag=False
        
    def Log_in(self):
        #print(self.name.get(),self.password.get())
        a=self.name.get()
        b=self.password.get()
        c='02#'+a+'#'+b+'#'
        self.user=a
        self.password=b
        print(c)
        self.parent.udpCliSock.sendto(c.encode('utf-8'), ADDR)
        data, ADDR1 = self.parent.udpCliSock.recvfrom(1024)
        print(data.decode(),ADDR1)
        
        if data.decode()=='02:01' or data.decode()=='02:04':
            self.Tag=True
            self.EnterWidget.destroy()
            self.destroy()
            
        elif data.decode()=='02:02':
            tkinter.messagebox.showinfo(title='登陆失败',message='密码错误')    #提示信息对话窗
        else:
            tkinter.messagebox.showinfo(title='登陆失败',message='用户不存在')    #提示信息对话窗
    
    
if __name__=='__main__':
    pass
    #EnterWidget().mainloop()