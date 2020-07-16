package com.zhouning.BaggageCalculator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SpecialBaggageTest {
    @DataProvider(name = "DataForIsCanConsign")
    public static Object[][] DataForIsCanConsign() {
        return new Object[][]{
                //免费托运特殊行李
                {new SpecialBaggage(100, SpecialBaggage.allType[0]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},
                //"高尔夫球包",运动器材类之一
                {new SpecialBaggage(100, SpecialBaggage.allType[1]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},
                {new SpecialBaggage(100, SpecialBaggage.allType[1]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(30, SpecialBaggage.allType[1]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},
                //睡袋、背包、野营用具、渔具、乐器、辅助设备
                {new SpecialBaggage(100, SpecialBaggage.allType[4]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},
                {new SpecialBaggage(100, SpecialBaggage.allType[4]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(30, SpecialBaggage.allType[4]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},

                //皮划艇
                {new SpecialBaggage(1, SpecialBaggage.allType[2]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(46, SpecialBaggage.allType[2]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                //撑杆
                {new SpecialBaggage(1, SpecialBaggage.allType[3]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(46, SpecialBaggage.allType[3]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},

                //小型电器或仪器
                {new SpecialBaggage(1, SpecialBaggage.allType[5]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(46, SpecialBaggage.allType[5]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},

                //可作为行李运输的枪支
                {new SpecialBaggage(1, SpecialBaggage.allType[6]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(46, SpecialBaggage.allType[6]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
//                可作为行李运输的子弹
                {new SpecialBaggage(1, SpecialBaggage.allType[7]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(46, SpecialBaggage.allType[7]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
//                小动物（仅限家庭驯养的猫、狗）
                {new SpecialBaggage(1, SpecialBaggage.allType[8]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new SpecialBaggage(46, SpecialBaggage.allType[8]),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},


        };
    }

    /**
     * 测试特殊行李是否能够携带
     */
    @Test(dataProvider = "DataForIsCanConsign")
    public void testIsCanConsign(SpecialBaggage specialBaggage,Ticket ticket,boolean expectedResult) {
        Assert.assertEquals(SpecialBaggage.isCanConsign(specialBaggage, ticket),expectedResult);
    }
}