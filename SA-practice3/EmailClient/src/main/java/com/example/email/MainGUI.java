package com.example.email;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    private JFrame myClient = new JFrame("邮件管理");
    private JButton sendButton = new JButton("发送邮件");
    private JButton varifyButton = new JButton("验证邮箱");
    Container contentPane = myClient.getContentPane();

    private void setupFrame(){
        myClient.setSize(500,500);
        myClient.setLocationRelativeTo(null);
        myClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myClient.setResizable(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        new sendMailGUI();
                    }
                }).start();
            }
        });
        varifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("12");
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        new varifyMailGUI();
                    }
                }).start();
            }
        });
        contentPane.setLayout(new FlowLayout());
        contentPane.add(sendButton);
        contentPane.add(varifyButton);
        myClient.setVisible(true);
    }



    public static void main(String []args){
        new MainGUI().setupFrame();
    }
}
