package com.zhouning.BaggageCalculator;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    //五个区域对应的需要交的钱数，在国际托运规则上起作用,
    // 感觉理论上写成配置文件读取比较好
    static  RegionalCharges []regionalCharges = new RegionalCharges[]{
      new RegionalCharges(new int[]{380,980,980,1400,1400,2000,3000}),
      new RegionalCharges(new int[]{280,690,690,1100,1100,1100,1590}),
      new RegionalCharges(new int[]{520,520,520,520,1170,1170,1590}),
      new RegionalCharges(new int[]{690,1040,1040,2050,1380 ,1380 ,1590}),
      new RegionalCharges(new int[]{210 ,520,520,830,830,1100 ,1590}),
    };

    /***
     * 计算托运行李费用
     * @param baggages   托运的行李
     * @param ticket    乘客的机票
     * @return  需要支付的费用，如果是 -0.1说明计算出现问题
     */
    public static float CalculateValue(List<Baggage> baggages,List<SpecialBaggage>specialBaggages, Ticket ticket){

        float cost = 0.f;
        //判断是国内托运还是国外托运
        if (ticket.getFlightType()==FlightType.DOMESTIC){
            //国内托运
            cost =  CalculateDomestic(baggages,specialBaggages, ticket);
        }else if (ticket.getFlightType()==FlightType.INTERNATIONAL){
            //国外托运
            cost = CalculateInterbational(baggages,specialBaggages, ticket);
        }
        return cost;
    }

    /**
     * 计算国内行李托运需要的费用
     * @param baggages   需要计算的行李,这里默认行李是符合标准可以托运的
     * @param ticket    乘客的机票
     * @return  需要支付的费用
     */
    public static float CalculateDomestic(List<Baggage> baggages,List<SpecialBaggage>specialBaggages, Ticket ticket){
        if (baggages==null||specialBaggages==null||ticket==null)
            return -1.f;
        float cost =0.f;
        float SumofWeight = 0.f ;
        for (Baggage baggage:baggages) {
            //三边总和
            float s = baggage.getHeight()+baggage.getWidth()+baggage.getLength();
            //每件重量
            float w = baggage.getWeight();

            //超过尺寸
            if (158<s)
                cost+=100;  //自己设置的标准，国航上面没有给出

            //悦享经济舱、超级经济舱、经济舱判断是否超过重量
            if(ticket.getCabinType()==CabinType.Economy_Class.Yuexiang_Economy_Class ||
                ticket.getCabinType()==CabinType.Economy_Class.Super_Economy_Class ||
                ticket.getCabinType()==CabinType.Economy_Class.Economy_Class ){
                //超过重量
                if (w>23)
                    cost +=0.015f*ticket.price;
            }
            //计算总重量
            SumofWeight += baggage.getWeight();
        }
        //特殊行李
        for (SpecialBaggage specialBaggage:specialBaggages ){
            float w = specialBaggage.getWeight();
            if (SpecialBaggage.allType[0].equals(specialBaggage.getType())) {
                //可免费运输的产品，啥都不用做

            }else if (SpecialBaggage.allType[1].equals(specialBaggage.getType())||
                    SpecialBaggage.allType[4].equals(specialBaggage.getType())){
                // "高尔夫球包",运动器材类之一，需要收费
                //"睡袋",//其他类型之一,其中的一个实例
                SumofWeight+=w;//按重量收费
            }else if (SpecialBaggage.allType[2].equals(specialBaggage.getType())){
                //"皮划艇",运动器材类之一，取其中的一个实例
                if (2<=w && w<=23)
                    cost+= 2600;
                else if (23<w && w<=32)
                    cost+=3900;
                else if (32<w && w<=45 )
                    cost+=5200;
                else
                    return -1.f;

            }else if (SpecialBaggage.allType[3].equals(specialBaggage.getType())){
                //"撑杆",运动器材类之一，取其中的一个实例
                if (2<=w && w<=23)
                    cost+= 1300;
                else if (23<w && w<=32)
                    cost+=2600;
                else if (32<w && w<=45 )
                    cost+=3900;
                else
                    return -1.f;
            }else if (SpecialBaggage.allType[5].equals(specialBaggage.getType())){
                //"小型电器或仪器",//其他类型之一，其中的一个实例
                if (2<=w && w<=23)
                    cost+= 490;
                else if (23<w && w<=32)
                    cost+=3900;
                else
                    return -1.f;

            }else if (SpecialBaggage.allType[6].equals(specialBaggage.getType())){
                //"可作为行李运输的枪支 ",//其他的类型之一
                if (2<=w && w<=23)
                    cost+= 1300;
                else if (23<w && w<=32)
                    cost+=2600;
                else
                    return -1.f;
            }else if (SpecialBaggage.allType[7].equals(specialBaggage.getType())){
                //"可作为行李运输的子弹",//其他类型之一，其中的一个实例
                if (2<=w && w<=5)
                    cost+= 1300;
                else
                    return -1.f;
            }else if (SpecialBaggage.allType[8].equals(specialBaggage.getType())){
                //"小动物"//其他类型之一，其中的一个实例
                if (2<=w && w<=8)
                    cost+= 3900;
                else if (8<w && w<=23)
                    cost+= 5200;
                else if (23<w && w<=32)
                    cost+=7800;
                else
                    return -1.f;
            }

        }

        //免费总重量
        float allowWeight = 0.f;

        //免费额度第一部分：
        //判断成员是大人还是小孩
        if (ticket.getPassengerClass()==PassengerClass.ADULT||
                ticket.getPassengerClass()==PassengerClass.CHILDREN){
            //说明是成人或者儿童
            if (ticket.getCabinType()==CabinType.First_Class||ticket.getCabinType()==CabinType.Deluxe_First_Class)//头等舱豪华头等舱都是头等舱
                allowWeight+=40.f;
            else if(ticket.getCabinType()==CabinType.Business_Class)//公务舱
                allowWeight+=30.f;
            else if (ticket.getCabinType()==CabinType.Economy_Class||
                    ticket.getCabinType()==CabinType.Super_Economy_Class||
                    ticket.getCabinType()==CabinType.Yuexiang_Economy_Class)//经济舱超级经济舱享悦经济舱都是经济舱
                allowWeight+=20.f;
        }else{
            //说明为婴儿票
            allowWeight +=10.f;
        }

        //免费额度第二部分：
        switch (ticket.getVipType()){
            //凤凰知音终身白金卡、白金卡旅客
            case Phoenix_CompanionLifetime_Platinum_Card:
            case Platinum_Card:
                allowWeight+=30.f;
                break;
            // 凤凰知音金卡、银卡、星空联盟金卡
            case Phoenix_Companion_Gold_Card:
            case Silver_Card:
            case Star_Alliance_Gold_Card:
                allowWeight+=20.f;
                break;
        }

        if (SumofWeight>allowWeight)
            cost+= ticket.getPrice()*(SumofWeight-allowWeight)*0.015f;
        return cost;
    }

    /***
     * 计算国际行李托运所需要的费用
     * @param baggages   需要计算的行李,行李的重量应该从小到大
     * @param ticket    乘客的机票
     * @return  需要支付的费用
     */
    public static float CalculateInterbational(List<Baggage> baggages,List<SpecialBaggage>specialBaggages, Ticket ticket){
        if (baggages==null||specialBaggages==null||ticket==null)
            return -1.f;

        int allowCount = 0;
        float allweight = 0.f;
        float cost = 0.f;
        int regionIndex = judgeRegionalType(ticket);
        switch (ticket.getCabinType()){
            //持成人或儿童客票的头等舱、公务舱旅客可免费托运两件普通行李
            //每件普通行李的重 量须小于或等于 32 千克（70 磅）
            case Deluxe_First_Class:
            case First_Class:
            case Business_Class:
                if (ticket.getPassengerClass()==PassengerClass.ADULT||
                        ticket.getPassengerClass()==PassengerClass.CHILDREN) {
                    allowCount += 2;
                    allweight += 32.f;
                }
                break;
            //悦享经济舱、超级经济舱客票旅客免费托运两件普通行李 (每件普通行李的重量须小于
            //或等于 23 千克/50 磅）。
            case Yuexiang_Economy_Class:
            case Super_Economy_Class:
                allowCount+=2;
                allweight+=23.f;
                break;
            case Economy_Class:
                allweight+=23.f;
                //分地区进行免费件数的设置
                if (regionIndex==0 ||regionIndex==1){
                    allowCount+=1;
                }else if (regionIndex==2||regionIndex==3||regionIndex==4){
                    allowCount+=2;
                }
                break;
        }

        int i=0;
        for (Baggage baggage:baggages) {
            if (i<allowCount){
                //在允许的免费额度中
                if (baggage.getWeight()>allweight){
                    cost+=excessCalculate(baggage, regionIndex);
                }
            }else if (i<allowCount+1){
                //超过1件
                cost+=regionalCharges[regionIndex].getCharges()[4];
                cost+=excessCalculate(baggage, regionIndex); //(超尺寸或超重量需另行支付并叠加计算费用
            }else if (i<allowCount+2){
                //超过两件
                cost+=regionalCharges[regionIndex].getCharges()[5];
                cost+=excessCalculate(baggage, regionIndex); //(超尺寸或超重量需另行支付并叠加计算费用
            }else {
                //超出的第三件及以上行李
                cost+=regionalCharges[regionIndex].getCharges()[6];
                cost+=excessCalculate(baggage, regionIndex); //(超尺寸或超重量需另行支付并叠加计算费用
            }
            i++;
        }

        //特殊行李
        for (SpecialBaggage specialBaggage:specialBaggages ){
            float w = specialBaggage.getWeight();
            if (SpecialBaggage.allType[0].equals(specialBaggage.getType())) {
                //可免费运输的产品，啥都不用做

            }else if (SpecialBaggage.allType[1].equals(specialBaggage.getType())||
                    SpecialBaggage.allType[4].equals(specialBaggage.getType())){
                // "高尔夫球包",运动器材类之一，需要收费
                //"睡袋",//其他类型之一,其中的一个实例
                if (i<allowCount){
                    if (specialBaggage.getWeight()>allweight){
                        cost+=excessCalculate(specialBaggage, regionIndex);
                    }
                }else if (i<allowCount+1){
                    //超过1件
                    cost+=regionalCharges[regionIndex].getCharges()[4];
                    cost+=excessCalculate(specialBaggage, regionIndex); //(超尺寸或超重量需另行支付并叠加计算费用
                }else if (i<allowCount+2){
                    //超过两件
                    cost+=regionalCharges[regionIndex].getCharges()[5];
                    cost+=excessCalculate(specialBaggage, regionIndex); //(超尺寸或超重量需另行支付并叠加计算费用
                }else {
                    //超出的第三件及以上行李
                    cost+=regionalCharges[regionIndex].getCharges()[6];
                    cost+=excessCalculate(specialBaggage, regionIndex); //(超尺寸或超重量需另行支付并叠加计算费用
                }
                i++;
            }else if (SpecialBaggage.allType[2].equals(specialBaggage.getType())){
                //"皮划艇",运动器材类之一，取其中的一个实例
                if (2<=w && w<=23)
                    cost+= 2600;
                else if (23<w && w<=32)
                    cost+=3900;
                else if (32<w && w<=45 )
                    cost+=1300;
                else
                    return -1;

            }else if (SpecialBaggage.allType[3].equals(specialBaggage.getType())){
                //"撑杆",运动器材类之一，取其中的一个实例
                if (2<=w && w<=23)
                    cost+= 1300;
                else if (23<w && w<=32)
                    cost+=2600;
                else if (32<w && w<=45 )
                    cost+=3900;
                else
                    return -1;
            }else if (SpecialBaggage.allType[5].equals(specialBaggage.getType())){
                //"小型电器或仪器",//其他类型之一，其中的一个实例
                if (2<=w && w<=23)
                    cost+= 490;
                else if (23<w && w<=32)
                    cost+=3900;
                else
                    return -1;
            }else if (SpecialBaggage.allType[6].equals(specialBaggage.getType())){
                //"可作为行李运输的枪支 ",//其他的类型之一
                if (2<=w && w<=23)
                    cost+= 1300;
                else if (23<w && w<=32)
                    cost+=2600;
                else
                    return -1;
            }else if (SpecialBaggage.allType[7].equals(specialBaggage.getType())){
                //"可作为行李运输的子弹",//其他类型之一，其中的一个实例
                if (2<=w && w<=5)
                    cost+= 1300;
            }else if (SpecialBaggage.allType[8].equals(specialBaggage.getType())){
                //"小动物"//其他类型之一，其中的一个实例
                if (2<=w && w<=8)
                    cost+= 3900;
                else if (8<w && w<=23)
                    cost+= 5200;
                else if (23<w && w<=32)
                    cost+=7800;
                else
                    return -1;
            }
        }

        return cost;
    }

    /***
     * 划分区域
     * @param ticket
     * @return
     */
    static int judgeRegionalType(Ticket ticket){

        if (Ticket.Regional[0].equals(ticket.getEnd())) {
            return 0;
        } else if (Ticket.Regional[1].equals(ticket.getEnd())) {
            return 1;
        } else if (Ticket.Regional[2].equals(ticket.getEnd())) {
            return 2;
        } else if (Ticket.Regional[3].equals(ticket.getEnd())) {
            return 3;
        } else if (Ticket.Regional[4].equals(ticket.getEnd())) {
            return 4;
        }
        return -1;
    }



    /***
     * 国际托运计算额外价格
     * @param baggage
     * @param regionIndex   地区下标
     * @return  需要缴纳的费用
     */
    static float excessCalculate(Baggage baggage,int regionIndex){
        //三边总和
        float sumofLong = baggage.getHeight()+baggage.getWidth()+baggage.getLength();
        //每件重量
        float w = baggage.getWeight();
        if (23<baggage.getWeight() && baggage.getWeight()<=28
                && 60<=sumofLong && sumofLong<=158){
            //超重但不超尺寸
            return regionalCharges[regionIndex].getCharges()[0];
        }else if (28<baggage.getWeight() && baggage.getWeight()<=32
                && 60<=sumofLong && sumofLong<=158){
            //超重量但不超尺寸
            return regionalCharges[regionIndex].getCharges()[1];

        }else if (2<baggage.getWeight() && baggage.getWeight()<=23
                && 158<=sumofLong && sumofLong<=203){
            //不超重量但超尺寸
            return regionalCharges[regionIndex].getCharges()[2];

        }else if (23<baggage.getWeight() && baggage.getWeight()<=32
                && 158<=sumofLong && sumofLong<=203){
            //超重量且超尺寸
            return regionalCharges[regionIndex].getCharges()[3];
        }

        //几种特殊情况
        if (w<2||w>32)
            return -1.f;
        if (sumofLong<60||sumofLong>203)
            return -1.f;
        if (regionIndex<0||regionIndex>=5)
            return -1.f;
        return 0.f;
    }

    public static void main(String[] args) {
        List<Baggage> baggages2 = new ArrayList<Baggage>();
        baggages2.add(new Baggage(60,38,60,24));
        List<SpecialBaggage>specialBaggages2=new ArrayList<SpecialBaggage>();
        specialBaggages2.add(new SpecialBaggage(5, SpecialBaggage.allType[0]));
        Ticket ticket2 =new Ticket(Ticket.Regional[5],Ticket.Regional[5],1000,PassengerClass.ADULT,CabinType.Yuexiang_Economy_Class,VIPType.Ordinary);
        CalculateDomestic(baggages2, specialBaggages2, ticket2);
    }
}

class  RegionalCharges{
    int [] Charges ;

    public int[] getCharges() {
        return Charges;
    }

    public void setCharges(int[] charges) {
        Charges = charges;
    }

    public RegionalCharges(int[] charges) {
        Charges = charges;
    }
}

