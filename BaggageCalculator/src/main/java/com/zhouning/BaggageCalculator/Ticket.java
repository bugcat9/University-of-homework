package com.zhouning.BaggageCalculator;

public class Ticket {
    public static String[] Regional = {
            //区域一
            "区域一：美洲（除美国/加拿大外）/加勒比海地区与欧洲/非洲/中东/亚洲/西南太平洋",
            //区域二
            "区域二：欧洲/中东与非洲/亚洲/西南太平洋之间航线；日本与西南太平洋之间航线；日本/西南太平洋与亚洲（不含日本及西南太平洋）/非洲之间航线",
            //区域三
            "区域三：加拿大与美洲（除美国/加拿大外）/加勒比海地区/欧洲/非洲/中东/亚洲/西南太平洋之间航线；",
            //区域四
            "区域四：美国（含夏威夷）与美洲（除美国外）/加勒比海地区/欧洲/非洲/中东/亚洲/西南太平洋之间航线；",
            //区域五
            "区域五：非洲与亚洲（除日本外)之间航线；欧洲与中东之间航线；亚洲（除日本)内航线；美洲（除美国/加拿大）及加勒比海地区内航线；上述未列明的航线;",
            //区域六
            "中国"
    };

    String start;   //起点
    String  end;    //终点
    float price;  //票价
    PassengerClass passengerClass;    //旅客的类型
    FlightType flightType;  //航班类型
    CabinType cabinType;    //机舱类型
    VIPType vipType; //会员类型

    public FlightType getFlightType() {
        return flightType;
    }

    public void setFlightType(FlightType flightType) {
        this.flightType = flightType;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public void setCabinType(CabinType cabinType) {
        this.cabinType = cabinType;
    }

    public VIPType getVipType() {
        return vipType;
    }

    public void setVipType(VIPType vipType) {
        this.vipType = vipType;
    }

    public Ticket(String start, String end, float price, PassengerClass passengerClass, CabinType cabinType, VIPType vipType) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.passengerClass = passengerClass;
        this.cabinType = cabinType;
        this.vipType = vipType;

        if (start.equals(Regional[5])&&end.equals(Regional[5])){
            this.flightType = FlightType.DOMESTIC;
        }else {
            this.flightType = FlightType.INTERNATIONAL;
        }
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public float getPrice() {
        return price;
    }

    public PassengerClass getPassengerClass() {
        return passengerClass;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPassengerClass(PassengerClass PassengerClass) {
        this.passengerClass = PassengerClass;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", price=" + price +
                ", passengerClass=" + passengerClass +
                ", flightType=" + flightType +
                ", cabinType=" + cabinType +
                ", vipType=" + vipType +
                '}';
    }
}





/***
 * 航班类型
 */
enum FlightType{
    DOMESTIC,INTERNATIONAL
}



