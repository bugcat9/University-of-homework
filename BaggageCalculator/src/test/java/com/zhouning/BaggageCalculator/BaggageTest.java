package com.zhouning.BaggageCalculator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BaggageTest {
    /***
     * 测试 Baggage里面的IsCanConsign()函数，
     * 函数的功能：用于判断普通的行李是否能够托运
     */
    @DataProvider(name = "DataForIsCanConsign")
    public static Object[][] DataForIsCanConsign() {
        return new Object[][]{
                //黑盒测试
                {new Baggage(10.f, 20.f, 50.f, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},
                {new Baggage(10.f, 20.f, 50.f, 1.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                {new Baggage(10.f, 20.f, 50.f, 33.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                //三边之和s, S<60
                {new Baggage(10.f, 20.f, 20, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                //三边之和s,s>203
                {new Baggage(100.f, 20.f, 100, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                //Ticket为国内托运，长>100
                {new Baggage(20.f, 20.f, 120, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
                //Ticket为国内托运，宽>60
                {new Baggage(70.f, 20.f, 20, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
//                Ticket为国内托运，高>40
                {new Baggage(20.f, 50.f, 20, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),false},
//                Ticket为国外托运三边之和大于158厘米小于203,可以托运
                {new Baggage(50.f, 50.f, 60, 20.f),
                        new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary),true},
        };
    }


    @Test(dataProvider = "DataForIsCanConsign")
    public void testIsCanConsign(Baggage baggage,Ticket ticket,boolean expectedResult ) {
        Assert.assertEquals(Baggage.isCanConsign(baggage, ticket),expectedResult);
    }
}