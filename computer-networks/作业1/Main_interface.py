'''
Created on 2019年3月11日

@author: zhouning
'''
from socket import *
from tkinter import *
import qq_chat_interface3 as chat
import qq_enter_interface as entry
import threading
import time
from tkinter.simpledialog import askstring, askinteger, askfloat
import tkinter.messagebox #重要
ADDR = ('202.114.196.97', 21568)

#主界面类，必须在主界面运行程序才能够运行起来程序
class MainWidget(Tk):
    def __init__(self):
        super().__init__()
        #朋友的数量
        self.friendsNum=0
        #朋友字典，是名字和学号的对应，其中名字是key
        self.friendsDict=dict()
        #聊天窗口字典，是名字和窗口的对应，其中学号是key
        self.chatDict=dict()
        #打开登陆界面
        self.Log_in_Widget=entry.EnterWidget(self)
        self.udpCliSock = socket(AF_INET, SOCK_DGRAM)
        self.protocol("WM_DELETE_WINDOW",self.Exit)     #绑定退出事件
        self.wait_window(self.Log_in_Widget)
        
        #这个是防止在登录的时候直接关掉界面而导致的一些错误
        if self.Log_in_Widget.Tag==True:
            self.title('好友列表')
            self.setupui()
            self.user=self.Log_in_Widget.user
            self.password=self.Log_in_Widget.password
       
    
    #设置界面
    def setupui(self):
        
        self.myFrame=Frame(self)
        self.myFrame.grid()
        self.FriensListBox=Listbox(self.myFrame,width=30,height=20)
        
        self.FriensListBox.pack()  #用于显示好友
        #直接加两个好友，这是我在代码里面加的，为了方便免得每次都要运行程序加
        #其实应该实现记住好友列表的功能，但是我觉得记住好友列表要在服务器端写比较好，所以我这边就没有添加了
        self.FriensListBox.insert('end','刘可欣')
        self.FriensListBox.insert('end','董安宁')
        
        self.friendsDict['董安宁']='20171000970'
        self.friendsDict['刘可欣']='20171003996'
        
        self.friendsNum=2
        self.FriensListBox.bind('<Double-Button-1>', self.ChoosFriend)  #绑定使得左键双击跳出聊天窗口
        self.FriensListBox.bind('<Button-3>', self.popupmenu)           #绑定使得右键弹出菜单
        self.AddButton=Button(self.myFrame,text='添加好友',command=self.AddFrinds)  #添加好友
        self.AddButton.pack()
        #改密码的按钮，暂时取这个名字
        self.ModifyButton=Button(self.myFrame,text='修改密码',command=self.ModifyPassword)
        self.ModifyButton.pack()
        
        
    #选择好友，跳出聊天对话框
    def ChoosFriend(self,event):
        print(self.FriensListBox.get(self.FriensListBox.curselection()))
        myFriend=self.FriensListBox.get(self.FriensListBox.curselection())
        
        #将聊天对话框加入到聊天字典当中
        chat_Widget=chat.ChatWidget(self,myFriend)
        self.chatDict[self.friendsDict[myFriend]]=chat_Widget
        #self.wait_window(chat_Widget) # 这一句很重要！！！
        #打开定时器
        timer = threading.Timer(1, self.ReciveFromFriend)
        timer.setDaemon(True)
        timer.start()
    #添加好友
    def AddFrinds(self):
        Add_window = Toplevel(self)

        Add_window.title("添加好友")
        Add_window.minsize(width=230, height=50)
        
        numLabel = Label(Add_window, text='号码:')
        numLabel.grid(row=0,column=0)
        numEntry=Entry(Add_window)
        numEntry.grid(row=0,column=1)
        
        nameLabel= Label(Add_window, text='备注:')
        nameLabel.grid(row=1,column=0)
        nameEntry=Entry(Add_window)
        nameEntry.grid(row=1,column=1)
        
        def add():
            self.friendsNum+=1
            name=nameEntry.get()
            num=numEntry.get()
            self.friendsDict[name]=num
            self.FriensListBox.insert('end',name)
            print(name,num)
            Add_window.destroy()
        b=Button(Add_window,text='添加',command=add)
        b.grid()
        
    #删除好友
    def DelFrineds(self):
        #print(self.FriensListBox.curselection())
        if self.FriensListBox.curselection()!=():
            name=self.FriensListBox.get(self.FriensListBox.curselection())
            self.friendsDict.pop(name)
            print(self.friendsDict)
            self.FriensListBox.delete(self.FriensListBox.curselection())
            print('删除成功')
        
    
    #发送消息，这个是在聊天窗口会调用这个函数
    def SendtoFriend(self,msg):
        # 在聊天内容上方加一行 显示发送人及发送时间
        print(msg)
        self.udpCliSock.sendto(msg.encode('utf-8'), ADDR)
        data, ADDR1 = self.udpCliSock.recvfrom(1024)
        print(data.decode())
        if data.decode()=='03:02':
            return False
        return True
    
    #接受消息，如果好友多了起来可能会慢
    def ReciveFromFriend(self):
        #print('Hello Timer!')
        for num in self.friendsDict.values():
            #  04#账号1(发送者)#
            #不停的像服务器发送上面的协议
            data='04#'+num+'#' 
            #print(data)
            self.udpCliSock.sendto(data.encode('utf-8'), ADDR)
            data, ADDR1 = self.udpCliSock.recvfrom(1024)
            #print(data.decode())
            #发现有消息，就查看这个消息所在的账号是否打开了聊天窗口，如果打开了就接受这个消息然后显示在窗口上
            if data.decode()!='04:0' and  num in self.chatDict.keys():
                widget=self.chatDict[num]
                #  05#账号1(发送者)#
                data='05#'+num+'#'
                #print(data)
                self.udpCliSock.sendto(data.encode('utf-8'), ADDR)
                data, ADDR1 = self.udpCliSock.recvfrom(1024)
                s=data.decode('utf-8')
                msg=s[14:]
                #widget.text_msglist.insert(END, data.decode('utf-8')+'\n')
                widget.ReciveMsg(msg)
                
        global timer
        timer = threading.Timer(2, self.ReciveFromFriend)
        timer.setDaemon(True)
        timer.start()
        
    #弹出菜单
    def popupmenu(self,event):
        menu = Menu(self, tearoff=0)
        menu.add_command(label='删除好友',command=self.DelFrineds)
        menu.add_separator()
        menu.add_command(label='更改备注',command=self.setName)
        menu.post(event.x_root, event.y_root)
        #self.myFriend=self.FriensListBox.get(self.FriensListBox.curselection())
    
    #更改备注，我发现无法直接更改，只能先删除原来的，再加入新的
    def setName(self):
        if self.FriensListBox.curselection()!=():
            index=self.FriensListBox.curselection()
            name=self.FriensListBox.get(self.FriensListBox.curselection())
            res = askstring('修改备注','备注')
            self.FriensListBox.delete(index)
            self.friendsDict[res]=self.friendsDict.pop(name)
            self.FriensListBox.insert(index,res)
            print(self.friendsDict)
        
    #退出函数
    def Exit(self):
        #print('我离开了')
        #发送离线消息并且关闭客户端
        data='06#'
        self.udpCliSock.sendto(data.encode(),ADDR)
        self.udpCliSock.close()
        self.destroy()
    #修改密码
    def ModifyPassword(self):
        Modif_window=Toplevel(self)
        Modif_window.title("更改密码")
        Modif_window.minsize(width=230, height=50)
        
        NewcodeLabel = Label(Modif_window, text='新密码:')
        NewcodeLabel.grid(row=0,column=0)
        NewcodeEntry=Entry(Modif_window)
        NewcodeEntry.grid(row=0,column=1)
        
        SurecodeLabel= Label(Modif_window, text='确认密码:')
        SurecodeLabel.grid(row=1,column=0)
        SurecodeEntry=Entry(Modif_window)
        SurecodeEntry.grid(row=1,column=1)
        
        def modify():
            # 01#账号#初始密码#新密码#确认密码#
            newcode=NewcodeEntry.get()
            surecode=SurecodeEntry.get()
            data='01#'+self.user+'#'+self.password+'#'+newcode+'#'+surecode+'#'
            print(data)
            self.udpCliSock.sendto(data.encode(),ADDR)
            data,ADDR1=self.udpCliSock.recvfrom(1024)
            print(data.decode())
            if data.decode()=='01:03' :
                tkinter.messagebox.showinfo(title='修改密码',message='修改密码成功')    #提示信息对话窗
                self.password=newcode
                Modif_window.destroy()
            elif data.decode()=='01:02':
                tkinter.messagebox.showinfo(title='修改密码',message='两次密码不相同')    #提示信息对话窗
            
        b=Button(Modif_window,text='修改',command=modify)
        b.grid()
if __name__=='__main__':
    w=MainWidget()
    w.mainloop()

