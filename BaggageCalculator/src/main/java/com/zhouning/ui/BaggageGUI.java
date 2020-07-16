package com.zhouning.ui;

import com.zhouning.BaggageCalculator.Baggage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaggageGUI extends JDialog  implements ActionListener{


    JTextField LengthField = new JTextField();    //长
    JTextField WidthField = new JTextField();    //宽
    JTextField HeighField = new JTextField();    //高
    JTextField WeightField = new JTextField();    //重
    JButton  addbtu =new JButton("确定添加");

    public Baggage getBaggage() {
        return baggage;
    }

    Baggage baggage;
    public BaggageGUI()  {

        this.setTitle("普通行李");
        this.setSize(400, 250);
        init();
        this.setLayout(null);
//        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        this.setVisible(true);
    }

    private void init()
    {
        JLabel jl1 = new JLabel("长:");//jl1.setOpaque(true);jl1.setBackground(Color.GREEN);
        JLabel jl2 = new JLabel("宽:");
        JLabel jl3 = new JLabel("高:");
        JLabel jl4 = new JLabel("重:");
        jl1.setBounds(10, 10, 50, 25);LengthField.setBounds(100,10,200,30);
        jl2.setBounds(10, 50, 50, 25);WidthField.setBounds(100,50,200,30);
        jl3.setBounds(10, 90, 50, 25);HeighField.setBounds(100,90,200,30);
        jl4.setBounds(10, 130, 50, 25);WeightField.setBounds(100,130,200,30);
        addbtu.setBounds(50, 170, 300, 30);
        this.add(jl1);
        this.add(jl2);
        this.add(jl3);
        this.add(jl4);
        this.add(LengthField);
        this.add(WidthField);
        this.add(HeighField);
        this.add(WeightField);
        this.add(addbtu);

        addbtu.addActionListener(this);
    }




    public static void main(String[] args) {
        new BaggageGUI();
    }

    public void actionPerformed(ActionEvent e) {

        if (!CalculatorGUI.isNumeric(LengthField.getText())||
            !CalculatorGUI.isNumeric(WidthField.getText())||
            !CalculatorGUI.isNumeric(HeighField.getText())||
            !CalculatorGUI.isNumeric(WeightField.getText())){
            JOptionPane.showMessageDialog(null, "输入信息有误");
            return;
        }

        float length = Float.valueOf(LengthField.getText());
        float width = Float.valueOf(WidthField.getText());
        float heigh = Float.valueOf(HeighField.getText());
        float weight = Float.valueOf(WeightField.getText());
        baggage = new Baggage(width, heigh,length , weight);
        System.out.println(baggage);
        this.setVisible(false);

    }

    public void clear(){
        baggage =null;
        LengthField.setText("");
        WidthField.setText("");
        HeighField.setText("");
        WeightField.setText("");
    }
}
