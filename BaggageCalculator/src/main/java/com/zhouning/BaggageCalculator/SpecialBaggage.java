package com.zhouning.BaggageCalculator;

/**
 * 特殊包裹，继承自普通包裹
 * 但是由于特殊包裹的尺寸不计入计算，所以都设置为一个为60，其他的设置为0
 */
public class SpecialBaggage extends Baggage{

    public static String[] allType ={
            "可免费运输的特殊行李",//因为这个类里面都是免费的所以就写一个类
            "高尔夫球包",//运动器材类之一，取其中的一个实例
            "皮划艇",//运动器材类之一，取其中的一个实例
            "撑杆",//运动器材类之一，取其中的一个实例
            "睡袋",//其他类型之一,其中的一个实例
            "小型电器或仪器",//其他类型之一，其中的一个实例
            "可作为行李运输的枪支 ",//其他的类型之一
            "可作为行李运输的子弹",//其他类型之一，其中的一个实例
            "小动物"//其他类型之一，其中的一个实例
    };

    private String type;
    public SpecialBaggage(float weight,String type) {

        super(60.f, 0.f, 0.f, weight);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SpecialBaggage{" +
                "type='" + type + '\'' +
                "width=" + width +
                "}\n";
    }

    /**
     * 判断普通行李是否能够托运
     * @param specialBaggage  需要进行托运的行李
     * @param ticket  乘客的机票
     * @return boolean 类型，如果为true则为能够托运，否则为不能托运
     */
   public static boolean isCanConsign(SpecialBaggage specialBaggage,Ticket ticket){
        float w = specialBaggage.getWeight();
        if (SpecialBaggage.allType[0].equals(specialBaggage.getType())) {
            //可免费运输的产品,不需要管重量
            return true;
        }else if (SpecialBaggage.allType[1].equals(specialBaggage.getType())||
                SpecialBaggage.allType[4].equals(specialBaggage.getType())){
            // "高尔夫球包",运动器材类之一，需要收费
            //"睡袋",//其他类型之一,其中的一个实例，
            // 和普通物件类似只是说超重收费，没说重量限制，国内默认多少都可以托运，但是在国外有一些限制
            if (ticket.flightType==FlightType.INTERNATIONAL){
                if (specialBaggage.getWeight()>32)
                    return false;
            }
            return true;
        }else if (SpecialBaggage.allType[2].equals(specialBaggage.getType())){
            //"皮划艇",运动器材类之一，取其中的一个实例
            if (2<=w&&w<=45)
                return true;

        }else if (SpecialBaggage.allType[3].equals(specialBaggage.getType())){
            //"撑杆",运动器材类之一，取其中的一个实例
            if (2<=w && w<=45)
                return true;

        }else if (SpecialBaggage.allType[5].equals(specialBaggage.getType())){
            //"小型电器或仪器",//其他类型之一，其中的一个实例
            if (2<=w && w<=32)
                return true;

        }else if (SpecialBaggage.allType[6].equals(specialBaggage.getType())){
            //"可作为行李运输的枪支 ",//其他的类型之一
            if (2<=w && w<=32)
                return true;
        }else if (SpecialBaggage.allType[7].equals(specialBaggage.getType())){
            //"可作为行李运输的子弹",//其他类型之一，其中的一个实例
            if (2<=w && w<=5)
                return true;
        }else if (SpecialBaggage.allType[8].equals(specialBaggage.getType())){
            //"小动物"//其他类型之一，其中的一个实例
            if (2<=w && w<=32)
              return true;
        }
        return false;
    }
}
