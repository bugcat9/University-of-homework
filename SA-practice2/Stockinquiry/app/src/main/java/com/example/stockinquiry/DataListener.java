package com.example.stockinquiry;

public interface DataListener {

    void onDay(String result);

    void onMonth(String result);

    void onAvg(String result);

    void onYear(String result);
}
