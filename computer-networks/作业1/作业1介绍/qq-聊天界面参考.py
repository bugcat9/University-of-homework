from Tkinter import *
import datetime
import time
root = Tk()
root.title(unicode('��xxx������','eucgb2312_cn'))
#���Ͱ�ť�¼�
def sendmessage():
  #�����������Ϸ���һ�� ��ʾ�����˼�����ʱ��
  msgcontent = unicode('��:','eucgb2312_cn') + time.strftime("%Y-%m-%d %H:%M:%S",time.localtime()) + '\n '
  text_msglist.insert(END, msgcontent, 'green')
  text_msglist.insert(END, text_msg.get('0.0', END))
  text_msg.delete('0.0', END)
 
#��������frame��Ϊ����
frame_left_top   = Frame(width=380, height=270, bg='white')
frame_left_center  = Frame(width=380, height=100, bg='white')
frame_left_bottom  = Frame(width=380, height=20)
frame_right     = Frame(width=170, height=400, bg='white')
##������Ҫ�ļ���Ԫ��
text_msglist    = Text(frame_left_top)
text_msg      = Text(frame_left_center);
button_sendmsg   = Button(frame_left_bottom, text=unicode('����','eucgb2312_cn'), command=sendmessage)
#����һ����ɫ��tag
text_msglist.tag_config('green', foreground='#008B00')
#ʹ��grid���ø�������λ��
frame_left_top.grid(row=0, column=0, padx=2, pady=5)
frame_left_center.grid(row=1, column=0, padx=2, pady=5)
frame_left_bottom.grid(row=2, column=0)
frame_right.grid(row=0, column=1, rowspan=3, padx=4, pady=5)
frame_left_top.grid_propagate(0)
frame_left_center.grid_propagate(0)
frame_left_bottom.grid_propagate(0)
#��Ԫ������frame
text_msglist.grid()
text_msg.grid()
button_sendmsg.grid(sticky=E)
#���¼�ѭ��
root.mainloop()