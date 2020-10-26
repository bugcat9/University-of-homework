package com.example.emotion_recognition.model;

import java.util.HashMap;

public class ClientInfo {
    //labels: 0=Angry, 1=Disgust, 2=Fear, 3=Happy, 4=Sad, 5=Surprise, 6=Neutral
    public static String IP = "192.168.43.22";
    public static int Port = 3030;
    public static HashMap<Integer,String> emotionMap = new HashMap<>();

    static
    {
        emotionMap.put(0,"Angry");
        emotionMap.put(1,"Disgust");
        emotionMap.put(2,"Fear");
        emotionMap.put(3,"Happy");
        emotionMap.put(4,"Sad");
        emotionMap.put(5,"Surprise");
        emotionMap.put(6,"Neutral");
    }
}
