package com.zhouning.ui;


import com.zhouning.BaggageCalculator.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class CalculatorGUI extends JFrame {
    JComboBox startCombox =new JComboBox();    //创建 起点相关的 JComboBox
    JComboBox endCombox = new JComboBox();    //创建 终点相关的 JComboBox
    JComboBox cabinCombox = new JComboBox();    //创建 机舱相关的 JComboBox
    JComboBox passengerCombox = new JComboBox();    //创建 乘客类型相关的 JComboBox
    JComboBox vipCombox = new JComboBox();    //创建 会员相关的 JComboBox
    JTextArea bagInfo = new JTextArea();       //行李的信息
    JTextField priceInfo = new JTextField();    //票价信息

    JButton addBagBut = new JButton("添加普通托运行李");
    JButton addSpecialBut = new JButton("添加特殊托运行李");
    JButton calculateBut = new JButton("计算费用");
    JButton clearBtu = new JButton("清空");

    //两个子界面
    BaggageGUI baggageGUI = new BaggageGUI();
    SpecialBagGUI specialBagGUI =new SpecialBagGUI();

    //两个数据
    List<Baggage>baggages = new ArrayList<Baggage>();
    List<SpecialBaggage>specialBaggages = new ArrayList<SpecialBaggage>();

    public CalculatorGUI() throws HeadlessException {
        this.setTitle("国航托运行李计算器");
        this.setSize(720, 550);
        init();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void init(){

        this.setLayout(null);
        baggageGUI.setModal(true);  //模式
        specialBagGUI.setModal(true);
        //设置起点和终点
        for (String regional: Ticket.Regional){
            startCombox.addItem(regional);
            endCombox.addItem(regional);
        }
        //机舱设置
        cabinCombox.addItem("豪华头等舱");
        cabinCombox.addItem("头等舱");
        cabinCombox.addItem("公务舱");
        cabinCombox.addItem("悦享经济舱");
        cabinCombox.addItem("超级经济舱");
        cabinCombox.addItem("经济舱");
        //乘客设置
        passengerCombox.addItem("成人");
        passengerCombox.addItem("儿童");
        passengerCombox.addItem("婴儿");
        //vip设置
        vipCombox.addItem("凤凰知音终身白金卡");
        vipCombox.addItem("白金卡");
        vipCombox.addItem("凤凰知音金卡");
        vipCombox.addItem("银卡");
        vipCombox.addItem("星空联盟金卡");


        JPanel ticketPanel = addTicketPanel();
        ticketPanel.setBounds(0,0,350,500);

        JPanel BagPanel = addBaggagePanel();
        BagPanel.setBounds(350,0,350,500);

        this.add(ticketPanel);
        this.add(BagPanel);
        //清空
        clearBtu.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                bagInfo.setText("");
                baggages.clear();
                specialBaggages.clear();

            }
        });
        //普通包裹
        addBagBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                baggageGUI.setVisible(true);

                Baggage baggage = baggageGUI.getBaggage();

                String strat = (String) startCombox.getSelectedItem();//起点
                String end = (String) endCombox.getSelectedItem();//终点
                if (!CalculatorGUI.isNumeric(priceInfo.getText())){
                    JOptionPane.showMessageDialog(null, "请输入正确价格");
                    return;
                }
                float price = Float.valueOf( priceInfo.getText());//价格


                PassengerClass passengerClass = PassengerClass.values()[passengerCombox.getSelectedIndex()] ;
                CabinType cabinType = CabinType.values()[cabinCombox.getSelectedIndex()];
                VIPType vipType = VIPType.values()[vipCombox.getSelectedIndex()];
                Ticket ticket = new Ticket(strat,end,price,passengerClass, cabinType,vipType);
                //判断是否能够带上机
                if (baggage!=null){
                    if (Baggage.isCanConsign(baggage, ticket)){
                        bagInfo.append(baggage.toString());
                        baggages.add(baggage);
                    }else {
                        JOptionPane.showMessageDialog(null, "请检查行李");
                    }
                    baggageGUI.clear();
                }

            }
        });
        //特殊包裹
        addSpecialBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                specialBagGUI.setVisible(true);


                String strat = (String) startCombox.getSelectedItem();//起点
                String end = (String) endCombox.getSelectedItem();//终点
                if (!CalculatorGUI.isNumeric(priceInfo.getText())){
                    JOptionPane.showMessageDialog(null, "请输入正确价格");
                    return;
                }
                float price = Float.valueOf( priceInfo.getText());//价格


                PassengerClass passengerClass = PassengerClass.values()[passengerCombox.getSelectedIndex()] ;
                CabinType cabinType = CabinType.values()[cabinCombox.getSelectedIndex()];
                VIPType vipType = VIPType.values()[vipCombox.getSelectedIndex()];
                Ticket ticket = new Ticket(strat,end,price,passengerClass, cabinType,vipType);

                SpecialBaggage baggage =specialBagGUI.getSpecialBaggage();
                //判断是否能够带上机
                if (baggage!=null&&SpecialBaggage.isCanConsign(baggage, ticket)){
                    if (SpecialBaggage.isCanConsign(baggage, ticket)){
                        bagInfo.append(baggage.toString());
                        specialBaggages.add(baggage);
                    }else {
                        JOptionPane.showMessageDialog(null, "请检查行李");
                    }
                    baggageGUI.clear();
                }
            }
        });
        //确定计算
        calculateBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String strat = (String) startCombox.getSelectedItem();//起点
                String end = (String) endCombox.getSelectedItem();//终点

                if (endCombox.getSelectedIndex()==5&&startCombox.getSelectedIndex()!=5){
                    JOptionPane.showMessageDialog(null, "无法从国外向国内托运");
                    return;
                }


                if (!CalculatorGUI.isNumeric(priceInfo.getText())){
                    JOptionPane.showMessageDialog(null, "输入价格无效");
                    return;
                }
                float price = Float.valueOf( priceInfo.getText());//价格


                PassengerClass passengerClass = PassengerClass.values()[passengerCombox.getSelectedIndex()] ;
                CabinType cabinType = CabinType.values()[cabinCombox.getSelectedIndex()];
                VIPType vipType = VIPType.values()[vipCombox.getSelectedIndex()];
                Ticket ticket = new Ticket(strat,end,price,passengerClass, cabinType,vipType);
                float cost = Calculator.CalculateValue(baggages, specialBaggages, ticket);
                System.out.println(cost);
                if (cost!=-1)
                    JOptionPane.showMessageDialog(null, "费用："+cost);
                else
                    JOptionPane.showMessageDialog(null, "那里除了问题");

            }
        });
    }

    /**
     * 添加和机票相关的界面
     * @return 机票相关的界面
     */
    private JPanel addTicketPanel(){
        JPanel jp=new JPanel(); //机舱相关的界面部分
        jp.setBorder(BorderFactory.createTitledBorder("机票信息"));
        JLabel jb1 = new JLabel("起点:");//jb1.setOpaque(true);jb1.setBackground(Color.GREEN);
        JLabel jb2 = new JLabel("终点:");
        JLabel jb3 = new JLabel("机舱:");
        JLabel jb4 = new JLabel("乘客:");
        JLabel jb5 = new JLabel("票价：");
        JLabel jb6 = new JLabel("vip：");
        //采用空布局
        jp.setLayout(null);
        jb1.setBounds(10, 30, 50, 30);startCombox.setBounds(100,30,200,30);
        jb2.setBounds(10, 90, 50, 30);endCombox.setBounds(100,90,200,30);
        jb3.setBounds(10, 150, 50, 30);cabinCombox.setBounds(100,150,200,30);
        jb4.setBounds(10, 210, 50, 30);passengerCombox.setBounds(100,210,200,30);
        jb5.setBounds(10, 270, 50, 30);priceInfo.setBounds(100,270,200,30);
        jb6.setBounds(10, 330, 50, 30);vipCombox.setBounds(100,330,200,30);

        jp.add(jb1);jp.add(startCombox);
        jp.add(jb2);jp.add(endCombox);
        jp.add(jb3);jp.add(cabinCombox);
        jp.add(jb4);jp.add(passengerCombox);
        jp.add(jb5);jp.add(priceInfo);
        jp.add(jb6);jp.add(vipCombox);

        return jp;
    }

    /***
     * 添加和行李相关的界面
     * @return  行李相关的界面
     */
    private JPanel addBaggagePanel(){
        JPanel jp=new JPanel(); //机舱相关的界面部分
        jp.setBorder(BorderFactory.createTitledBorder("行李信息"));
        //将上面两个按钮用一个布局框起来
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2,10,5));
        panel.add(addBagBut);
        panel.add(addSpecialBut);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1,2,10,5));
        panel2.add(calculateBut);
        panel2.add(clearBtu);

        jp.setLayout(new BorderLayout(10,5));//默认为0，0；水平间距10，垂直间距5
        jp.add(panel,BorderLayout.NORTH);
        jp.add(bagInfo,BorderLayout.CENTER);
        jp.add(panel2,BorderLayout.SOUTH);
        return jp;
    }


    /**
     * 判断字符串是否是数字,其中负数也是返回false
     * @param str   需要判断的字符串
     * @return
     */
    public static boolean isNumeric(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    public static void main(String[] args) {
        new CalculatorGUI();
    }
}
