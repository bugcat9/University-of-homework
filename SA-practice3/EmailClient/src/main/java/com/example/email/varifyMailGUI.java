package com.example.email;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class varifyMailGUI {

    public varifyMailGUI(){
        this.setupFrame();
    }

    private JFrame myVarify = new JFrame("验证邮箱");
    private JLabel addressLlable = new JLabel("邮箱地址:");
    private JTextField address = new JTextField(35);
    private JButton varify = new JButton("验证");
    private GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints constraints = null;

    private void setupFrame(){
        myVarify.setSize(500, 300);
        myVarify.setResizable(false);
        myVarify.setLocationRelativeTo(null);
        myVarify.setLayout(gridBagLayout);
        place();
        //myVarify.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myVarify.setVisible(true);
    }

    private void place(){
        constraints = new GridBagConstraints();

        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBagLayout.addLayoutComponent(addressLlable, constraints);
        myVarify.add(addressLlable);

        constraints.gridwidth = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBagLayout.addLayoutComponent(address, constraints);
        myVarify.add(address);
        varify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url =address.getText();
                String res= EmailClient.validateEmailAddress(url);
                if (res.equals("Y"))
                    JOptionPane.showMessageDialog(null, "邮箱格式正确");
                else
                    JOptionPane.showMessageDialog(null, "邮箱格式错误");
            }
        });

        myVarify.add(varify);

    }

    public static void main(String []args){
        new varifyMailGUI();
    }
}
