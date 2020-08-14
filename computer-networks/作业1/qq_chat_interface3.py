from tkinter import *
import datetime
import threading
import time
from tkinter import scrolledtext
ADDR = ('202.114.196.97', 21568)
class ChatWidget(Toplevel):
    def __init__(self,parent,name):
        
        self.parent = parent # 显式地保留父窗口
        self.name=name
        super().__init__()
        self.geometry('580x500')
        self.title(name)
       
        fram_bottom=Frame(self,bg='white',width=70,height=10)
        
        #使用ScrolledText自带滚条
        self.text_msglist = scrolledtext.ScrolledText(self,font=('微软雅黑', 10),width=70,height=13)
        self.text_msg = Text(self,width=80,height=15);
        self.button_sendmsg = Button(fram_bottom, text=('发送'.encode("utf-8").decode("utf-8")), command=self.sendmessage)
        self.button_clear=Button(fram_bottom, text=('清空'.encode("utf-8").decode("utf-8")),command=self.Clear)
        self.e=Label(fram_bottom)
        
        # 创建一个绿色的tag
        self.text_msglist.tag_config('green', foreground='#008B00')
        
        
        
        
        # 把元素填充进frame
        self.text_msglist.grid(row=0,column=0,padx=2,pady=5)
        self.text_msg.grid(row=1,column=0,padx=2,pady=5)
        self.button_sendmsg.grid(row=2, column=0)
        self.e.grid(row=2, column=3)
        self.button_clear.grid(row=2, column=1)
        fram_bottom.grid(row=2,column=0)
        #绑定退出事件
        self.protocol("WM_DELETE_WINDOW",self.Exit)
        
        
        # 发送按钮事件
    def sendmessage(self):
        # 在聊天内容上方加一行 显示发送人及发送时间
        msgcontent = ('我:'.encode("utf-8").decode("utf-8")) + time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()) + '\n '
        self.text_msglist.insert(END, msgcontent, 'green')
        self.text_msglist.insert(END, self.text_msg.get('0.0', END))
        
        print(self.text_msg.get('0.0', END))
        self.msg='03#'+self.parent.friendsDict[self.name]+'#'+self.text_msg.get('0.0', END)+'#'
        #03#账号1(接收者)#消息# 
        self.text_msg.delete('0.0', END)
        tag= self.parent.SendtoFriend(self.msg)
        
        if tag==False:
            self.e.config(text='对方离线')
        else:
            self.e.config(text='发送成功')
        self.update()
        
    def ReciveMsg(self,msg):
        data=self.name+msg+'\n'
        self.text_msglist.insert(END, data)
    #清空聊天对话框
    def Clear(self):
        self.text_msglist.delete('0.0', END)
    #退出函数
    def Exit(self):
        #删除在字典里面的对话
        self.parent.chatDict.pop(self.parent.friendsDict[self.name])
        self.destroy()
    
    
        
if __name__=='__main__':
    # 主事件循环
    root = Tk()
    root.title(('与xxx聊天中'.encode("utf-8").decode("utf-8")))
    widget=ChatWidget(root,'20171004140')
    root.mainloop()
