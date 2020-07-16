package com.zhouning.BaggageCalculator;

/***
 * 包裹类
 */
public class Baggage {

    protected float width;    //以厘米为单位
    protected float height;
    protected float length;
    protected float weight;   //以千克为单位


    public Baggage(){}
    //构造函数
    public Baggage(float width, float height, float length, float weight) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }



    public float getWeight() {
        return weight;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Baggage{" +
                "width=" + width +
                ", height=" + height +
                ", length=" + length +
                ", weight=" + weight +
                "}\n";
    }

    /**
     * 判断普通行李是否能够托运
     * @param baggage  需要进行托运的行李
     * @param ticket  乘客的机票
     * @return boolean 类型，如果为true则为能够托运，否则为不能托运
     */
    public static boolean isCanConsign(Baggage baggage,Ticket ticket){
        //三边总和
        float s = baggage.getHeight()+baggage.getWidth()+baggage.getLength();
        //每件重量
        float w = baggage.getWeight();

        //满足总体的标准
        if (60<=s && s<=203 && 2<=w && w<=32){
            //判断是国际托运还是国内托运
            if (ticket.getFlightType()==FlightType.DOMESTIC){
                //国内托运
                //每件行李长、宽、高分别不得超过 100 厘米（40 英寸）、60 厘米（24 英寸）、40 厘米
                if (baggage.getLength()<=100 &&
                        baggage.getWidth()<=60 &&
                        baggage.getHeight()<=40)
                    return true;
                else
                    return false;
            }
//            else if (ticket.getFlightType()==FlightType.INTERNATIONAL){
//                //国际托运
//                //每件普通行李的三边之和须小于或等于 158 厘米
//                if (s<=158)return true;
//            }
            return true;
        }
        return false;
    }
}

