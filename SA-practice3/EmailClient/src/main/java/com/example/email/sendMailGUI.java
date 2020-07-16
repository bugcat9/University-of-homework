package com.example.email;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class sendMailGUI {

    public  sendMailGUI(){
        this.setFrame();
    }

    //布局组件
    private JFrame frame = new JFrame("发送邮件");
    private GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints constraints = null;
    private JLabel recipientsLable = new JLabel("收件地址：");
    private JTextField address = new JTextField(45);
    private JLabel topicLable = new JLabel("邮件主题：");
    private JTextField topic = new JTextField(45);
    private JTextArea message = new JTextArea("邮件正文：",10,10);
    private JScrollPane scrollPane = new JScrollPane(message,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JButton send = new JButton("发送");

    //初始化界面
    private void setFrame(){
        frame.setSize(600,450);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(gridBagLayout);
        placeComponents();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    //布局组件
    private void placeComponents(){
        constraints = new GridBagConstraints();

        //收件人地址，多个收件人用（英文）分号,隔开
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBagLayout.addLayoutComponent(recipientsLable, constraints);
        frame.add(recipientsLable);

        constraints.gridwidth = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBagLayout.addLayoutComponent(address, constraints);
        frame.add(address);

        //邮件主题
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBagLayout.addLayoutComponent(topicLable, constraints);
        frame.add(topicLable);

        constraints.gridwidth = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBagLayout.addLayoutComponent(topic, constraints);
        frame.add(topic);

        //邮件正文
        constraints.gridwidth = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        message.setEditable(true);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        gridBagLayout.addLayoutComponent(scrollPane, constraints);
        frame.add(scrollPane);

        //发送按钮
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url=address.getText();
                String _payload=message.getText();
                String res = EmailClient.sendEmail(url, _payload);
                if (res.equals("Y"))
                    JOptionPane.showMessageDialog(null, "发送成功");
                else
                    JOptionPane.showMessageDialog(null, "发送失败");
            }
        });
        frame.add(send);
    }

    public static void main(String []args){
        new sendMailGUI();
    }
}
