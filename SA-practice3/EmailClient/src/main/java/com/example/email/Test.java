package com.example.email;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {
    public static void main(String []args){

        String[] a={"hiking", "swimming"};
        JSONObject obj = new JSONObject();
        obj.put("name", "John");
        obj.put("sex", "male");
        obj.put("age", 22);
        obj.put("is_student", true);
        obj.put("hobbies", a);
        //调用toString()方法可直接将其内容打印出来
        System.out.println(obj.toString());

        try{
            URL url = new URL("http://54.198.119.157:8080/emailBatch");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");//设置参数类型是json格式
            con.connect();

//            String body ="{\n" +
//                    "\t\"urls\":[\"1767508581@qq.com\",\"2381679817@qq.com\"],\n" +
//                    "\t\"_payload\":\"1111111111\"\n" +
//                    "}";
            JSONObject  body = new JSONObject();
            body.put("urls", new String[]{"1767508581@qq.com","2381679817@qq.com"});
            body.put("_payload", "111112222");

            StringBuilder response = new StringBuilder();

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                writer.write(body.toString());
                writer.flush();
                writer.close();
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
        }catch (MalformedURLException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
