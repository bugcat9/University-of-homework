package com.example.stockinquiry;

import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

/**
 * 对活动进行控制
 */
public class ActivtyControl {
    private static Stack<AppCompatActivity> m_statck ;

    public static void Add_Activity(AppCompatActivity activity){
        if (m_statck==null){
            m_statck = new Stack<>();
        }
        m_statck.push(activity);
    }

    public static void finish_Activity(AppCompatActivity activity){
        if (activity.isFinishing()==Boolean.FALSE){
            m_statck.remove(activity);
            activity.finish();
        }
    }

    public static void finishAll(){
        for (AppCompatActivity activity :m_statck){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
