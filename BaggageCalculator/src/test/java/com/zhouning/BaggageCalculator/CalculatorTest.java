package com.zhouning.BaggageCalculator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class CalculatorTest {

    @DataProvider(name = "DataForCalculateDomestic")
    public static Object[][] DataForCalculateDomestic(){
//        测试普通行李和特殊行李都为0时的场景
        List<Baggage> baggages1 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages1=new ArrayList<SpecialBaggage>();
        Ticket ticket1 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary);

        //测试享悦经济舱和凤凰知音终身白金卡带一件普通行李和 一件免费的特殊行李。
        List<Baggage> baggages2 = new ArrayList<Baggage>();
        baggages2.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages2=new ArrayList<SpecialBaggage>();
        specialBaggages2.add(new SpecialBaggage(5, SpecialBaggage.allType[0]));
        Ticket ticket2 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Yuexiang_Economy_Class,VIPType.Phoenix_Companion_Gold_Card);

        //超级经济舱的和白金卡一次测试
        List<Baggage> baggages3 = new ArrayList<Baggage>();
        baggages3.add(new Baggage(60,38,61,24));
        List<SpecialBaggage>specialBaggages3=new ArrayList<SpecialBaggage>();
        specialBaggages3.add(new SpecialBaggage(5, SpecialBaggage.allType[1]));
        Ticket ticket3 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Super_Economy_Class,VIPType.Platinum_Card);

        //对于经济舱和凤凰知音金卡的一次测试
        List<Baggage> baggages4 = new ArrayList<Baggage>();
        baggages4.add(new Baggage(60,38,61,24));
        List<SpecialBaggage>specialBaggages4=new ArrayList<SpecialBaggage>();
        specialBaggages4.add(new SpecialBaggage(5, SpecialBaggage.allType[2]));
        Ticket ticket4 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Super_Economy_Class,VIPType.Phoenix_Companion_Gold_Card);

        //豪华头等舱和银卡的测试
        List<Baggage> baggages5 = new ArrayList<Baggage>();
        baggages5.add(new Baggage(60,38,61,24));
        List<SpecialBaggage>specialBaggages5=new ArrayList<SpecialBaggage>();
        specialBaggages5.add(new SpecialBaggage(24, SpecialBaggage.allType[2]));
        Ticket ticket5 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.CHILDREN,CabinType.Deluxe_First_Class,VIPType.Silver_Card);

        //头等舱和星空联盟金卡以及婴儿测试
        List<Baggage> baggages6 = new ArrayList<Baggage>();
        baggages6.add(new Baggage(60,38,61,24));
        List<SpecialBaggage>specialBaggages6=new ArrayList<SpecialBaggage>();
        specialBaggages6.add(new SpecialBaggage(24, SpecialBaggage.allType[2]));
        Ticket ticket6 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.IMFANT,CabinType.First_Class,VIPType.Star_Alliance_Gold_Card);

        //对于特殊行李第二种32<w<=45情况的测试
        List<Baggage> baggages7 = new ArrayList<Baggage>();
        baggages7.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages7=new ArrayList<SpecialBaggage>();
        specialBaggages7.add(new SpecialBaggage(33, SpecialBaggage.allType[2]));
        Ticket ticket7 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary);

        //对于特殊行李撑杆的第三件进行对应的测试
        List<Baggage> baggages8 = new ArrayList<Baggage>();
        baggages8.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages8=new ArrayList<SpecialBaggage>();
        specialBaggages8.add(new SpecialBaggage(2, SpecialBaggage.allType[3]));
        specialBaggages8.add(new SpecialBaggage(24, SpecialBaggage.allType[3]));
        specialBaggages8.add(new SpecialBaggage(33, SpecialBaggage.allType[3]));
        Ticket ticket8 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第四种（睡袋）进行测试，测试重量w=100
        List<Baggage> baggages9 = new ArrayList<Baggage>();
        baggages9.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages9=new ArrayList<SpecialBaggage>();
        specialBaggages9.add(new SpecialBaggage(100, SpecialBaggage.allType[4]));
        Ticket ticket9 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);


        //对于特殊行李第五种(小型电器或仪器)进行测试
        List<Baggage> baggages10 = new ArrayList<Baggage>();
        baggages10.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages10=new ArrayList<SpecialBaggage>();
        specialBaggages10.add(new SpecialBaggage(2, SpecialBaggage.allType[5]));
        specialBaggages10.add(new SpecialBaggage(24, SpecialBaggage.allType[5]));
        Ticket ticket10 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第六种(可作为行李运输的枪支 )进行测试
        List<Baggage> baggages11 = new ArrayList<Baggage>();
        baggages11.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages11=new ArrayList<SpecialBaggage>();
        specialBaggages11.add(new SpecialBaggage(2, SpecialBaggage.allType[6]));
        specialBaggages11.add(new SpecialBaggage(24, SpecialBaggage.allType[6]));
        Ticket ticket11 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第七种(可作为行李运输的子弹  )进行测试
        List<Baggage> baggages12 = new ArrayList<Baggage>();
        baggages12.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages12=new ArrayList<SpecialBaggage>();
        specialBaggages12.add(new SpecialBaggage(2, SpecialBaggage.allType[7]));
        Ticket ticket12 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第八种(小动物)进行测试
        List<Baggage> baggages13 = new ArrayList<Baggage>();
        baggages13.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages13=new ArrayList<SpecialBaggage>();
        specialBaggages13.add(new SpecialBaggage(2, SpecialBaggage.allType[8]));
        specialBaggages13.add(new SpecialBaggage(9, SpecialBaggage.allType[8]));
        specialBaggages13.add(new SpecialBaggage(24, SpecialBaggage.allType[8]));
        Ticket ticket13 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);


        return new Object[][]{
                {baggages1,specialBaggages1,ticket1,0.f},
                {baggages2,specialBaggages2,ticket2,15},
                {baggages3,specialBaggages3,ticket3,115},
                {baggages4,specialBaggages4,ticket4,2715.f},
                {baggages5,specialBaggages5,ticket5,4000},
                {baggages6,specialBaggages6,ticket6,4000},
                {baggages7,specialBaggages7,ticket7,5275},
                {baggages8,specialBaggages8,ticket8,7800},
                {baggages9,specialBaggages9,ticket9,1260},
                {baggages10,specialBaggages10,ticket10,4390},
                {baggages11,specialBaggages11,ticket11,3900},
                {baggages12,specialBaggages12,ticket12,1300},
                {baggages13,specialBaggages13,ticket12,16900.f}
        };
    }
    /**
     * 国内托运计算
     */
    @Test(dataProvider = "DataForCalculateDomestic")
    public void testCalculateDomestic(List<Baggage> baggages, List<SpecialBaggage>specialBaggages, Ticket ticket,float expectedResult) {
        Assert.assertEquals(Calculator.CalculateDomestic(baggages, specialBaggages,ticket),expectedResult);
    }


    @DataProvider(name = "DataCalculateInterbational")
    public static Object[][] DataCalculateInterbational(){
//        测试普通行李和特殊行李都为0时的场景
        List<Baggage> baggages1 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages1=new ArrayList<SpecialBaggage>();
        //中国到区域1
        Ticket ticket1 =new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Deluxe_First_Class,VIPType.Ordinary);

        //测试头等舱，其中普通行李和特殊行李都在免费的额度内
        List<Baggage> baggages2 = new ArrayList<Baggage>();
        baggages2.add(new Baggage(20,20,20,23));
        List<SpecialBaggage>specialBaggages2=new ArrayList<SpecialBaggage>();
        specialBaggages2.add(new SpecialBaggage(30, SpecialBaggage.allType[0]));
        Ticket ticket2 =new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Phoenix_Companion_Gold_Card);

        //测试头等舱的婴儿票
        List<Baggage> baggages3 = new ArrayList<Baggage>();
        baggages3.add(new Baggage(60,20,20,23));
        baggages3.add(new Baggage(60,20,20,24));
        baggages3.add(new Baggage(60,20,20,25));
        List<SpecialBaggage>specialBaggages3=new ArrayList<SpecialBaggage>();
        specialBaggages3.add(new SpecialBaggage(5, SpecialBaggage.allType[0]));
        Ticket ticket3 =new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.IMFANT,CabinType.First_Class,VIPType.Platinum_Card);

        //测试公务舱
        List<Baggage> baggages4 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages4=new ArrayList<SpecialBaggage>();
        specialBaggages4.add(new SpecialBaggage(23, SpecialBaggage.allType[1]));
        specialBaggages4.add(new SpecialBaggage(24, SpecialBaggage.allType[1]));
        specialBaggages4.add(new SpecialBaggage(25, SpecialBaggage.allType[4]));
        Ticket ticket4 =new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.Business_Class,VIPType.Phoenix_Companion_Gold_Card);

        //测试享悦经济舱
        List<Baggage> baggages5 = new ArrayList<Baggage>();
        baggages5.add(new Baggage(20,20,20,30));
        baggages5.add(new Baggage(60,60,40,24));
        baggages5.add(new Baggage(60,60,40,25));
        List<SpecialBaggage>specialBaggages5=new ArrayList<SpecialBaggage>();
        specialBaggages5.add(new SpecialBaggage(23, SpecialBaggage.allType[2]));
        specialBaggages5.add(new SpecialBaggage(24, SpecialBaggage.allType[2]));
        specialBaggages5.add(new SpecialBaggage(33, SpecialBaggage.allType[2]));
        Ticket ticket5 =new Ticket(Ticket.Regional[5],Ticket.Regional[1],1000,PassengerClass.CHILDREN,CabinType.Yuexiang_Economy_Class,VIPType.Ordinary);

        //测试经济舱
        Ticket ticket6 =new Ticket(Ticket.Regional[5],Ticket.Regional[2],1000,PassengerClass.ADULT,CabinType.Economy_Class,VIPType.Ordinary);

        //测试超级经济舱
        Ticket ticket7 =new Ticket(Ticket.Regional[5],Ticket.Regional[3],1000,PassengerClass.ADULT,CabinType.Super_Economy_Class,VIPType.Ordinary);

        //对于特殊行李撑杆的第三件进行对应的测试
        List<Baggage> baggages8 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages8=new ArrayList<SpecialBaggage>();
        specialBaggages8.add(new SpecialBaggage(2, SpecialBaggage.allType[3]));
        specialBaggages8.add(new SpecialBaggage(24, SpecialBaggage.allType[3]));
        specialBaggages8.add(new SpecialBaggage(33, SpecialBaggage.allType[3]));
        Ticket ticket8 =new Ticket(Ticket.Regional[5],Ticket.Regional[4],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第四种（睡袋）进行测试，
        List<Baggage> baggages9 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages9=new ArrayList<SpecialBaggage>();
        specialBaggages9.add(new SpecialBaggage(3, SpecialBaggage.allType[4]));
        specialBaggages9.add(new SpecialBaggage(24, SpecialBaggage.allType[4]));
        specialBaggages9.add(new SpecialBaggage(30, SpecialBaggage.allType[4]));
        Ticket ticket9 =new Ticket(Ticket.Regional[5],Ticket.Regional[4],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);


        //对于特殊行李第五种(小型电器或仪器)进行测试
        List<Baggage> baggages10 = new ArrayList<Baggage>();
        baggages10.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages10=new ArrayList<SpecialBaggage>();
        specialBaggages10.add(new SpecialBaggage(2, SpecialBaggage.allType[5]));
        specialBaggages10.add(new SpecialBaggage(24, SpecialBaggage.allType[5]));
        Ticket ticket10 =new Ticket(Ticket.Regional[5],Ticket.Regional[4],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第六种(可作为行李运输的枪支 )进行测试
        List<Baggage> baggages11 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages11=new ArrayList<SpecialBaggage>();
        specialBaggages11.add(new SpecialBaggage(2, SpecialBaggage.allType[6]));
        specialBaggages11.add(new SpecialBaggage(24, SpecialBaggage.allType[6]));
        Ticket ticket11 =new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第七种(可作为行李运输的子弹  )进行测试
        List<Baggage> baggages12 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages12=new ArrayList<SpecialBaggage>();
        specialBaggages12.add(new SpecialBaggage(2, SpecialBaggage.allType[7]));
        Ticket ticket12 =new Ticket(Ticket.Regional[5],Ticket.Regional[0],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);

        //对于特殊行李第八种(小动物)进行测试
        List<Baggage> baggages13 = new ArrayList<Baggage>();
        List<SpecialBaggage>specialBaggages13=new ArrayList<SpecialBaggage>();
        specialBaggages13.add(new SpecialBaggage(2, SpecialBaggage.allType[8]));
        specialBaggages13.add(new SpecialBaggage(9, SpecialBaggage.allType[8]));
        specialBaggages13.add(new SpecialBaggage(24, SpecialBaggage.allType[8]));
        Ticket ticket13 =new Ticket(Ticket.Regional[0],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.First_Class,VIPType.Ordinary);


        return new Object[][]{
                {baggages1,specialBaggages1,ticket1,0.f},
                {baggages2,specialBaggages2,ticket2,0},
                {baggages3,specialBaggages3,ticket3,7160},
                {baggages4,specialBaggages4,ticket4,1780.f},
                {baggages5,specialBaggages5,ticket5,11790},
                {baggages5,specialBaggages5,ticket6,10530},
                {baggages5,specialBaggages5,ticket7,14320},
                {baggages8,specialBaggages8,ticket8,7800},
                {baggages9,specialBaggages9,ticket9,1350},
                {baggages10,specialBaggages10,ticket10,4390},
                {baggages11,specialBaggages11,ticket11,3900},
                {baggages12,specialBaggages12,ticket12,1300},
                {baggages13,specialBaggages13,ticket12,16900.f}
        };
    }

    /***
     * 国际托运计算
     */
    @Test(dataProvider = "DataCalculateInterbational")
    public void testCalculateInterbational(List<Baggage> baggages, List<SpecialBaggage>specialBaggages, Ticket ticket,float expectedResult) {
        Assert.assertEquals(Calculator.CalculateInterbational(baggages, specialBaggages,ticket),expectedResult);
    }


    @DataProvider(name = "DataForExcessCalculate")
    public static Object[][] DataForExcessCalculate(){
        return new Object[][]{
                //有效用例,这部分为黑盒
                {new Baggage(10.f, 20.f, 50.f, 20.f),0,0.f},
//                Baggage重量w<2
                {new Baggage(10.f, 20.f, 50.f, 1.f),1,-1.f},
//                Baggage重量w>32
                {new Baggage(10.f, 20.f, 50.f, 33.f),0,-1.f},
//                Baggage总长度L<60
                {new Baggage(10.f, 20.f, 20.f, 33.f),0,-1.f},
                //Baggage总长度L>203f
                {new Baggage(100.f, 100.f, 100.f, 33.f),0,-1.f},
//                输入的regionIndex<0
                {new Baggage(100.f, 100.f, 100.f, 33.f),-1,-1.f},
//                输入的regionIndex>=5
                {new Baggage(100.f, 100.f, 100.f, 33.f),0,-1.f},


        };
    }
    /**
     * 对于国际托运超额的测试，测试的为黑盒测试
     */
    @Test(dataProvider = "DataForExcessCalculate")
    public void testExcessCalculate(Baggage baggage,int regionIndex,float expectedResult) {
        Assert.assertEquals(Calculator.excessCalculate(baggage, regionIndex),expectedResult);
    }
}