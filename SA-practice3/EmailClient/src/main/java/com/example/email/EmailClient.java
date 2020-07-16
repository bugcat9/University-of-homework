package com.example.email;

import com.example.email.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EmailClient {

    static String  emailbody ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:gs=\"http://www.howtodoinjava.com/xml/email\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <gs:EmailRequest>\n" +
            "         <gs:url>%s</gs:url>\n" +
            "         <gs:_payload>%s</gs:_payload>\n" +
            "      </gs:EmailRequest>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    static String validateEmailAddressbody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:gs=\"http://www.howtodoinjava.com/xml/email\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <gs:ValidateEmailAddressRequest>\n" +
            "         <gs:url>%s</gs:url>\n" +
            "      </gs:ValidateEmailAddressRequest>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>\n";

    static public String sendEmail(String url,String _payload){
        URL obj;
        HttpURLConnection con;

        StringBuilder response = new StringBuilder();
        String body = String.format(emailbody, url,_payload);   //进行字符串格式化
        BufferedWriter writer = null;
        try {
            obj = new URL("http://localhost:8080/email/emailservice");
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");//设置参数类型是xml格式
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            writer.write(body);
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
        if (response.toString().contains("<ns2:result>Y</ns2:result>"))
            return "Y";
        return "N";
    }

    static public String validateEmailAddress(String url){
        URL obj;
        HttpURLConnection con;
        StringBuilder response = new StringBuilder();
        String body = String.format(validateEmailAddressbody, url);
        BufferedWriter writer = null;
        try {
            obj = new URL("http://localhost:8080/email/emailservice");
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");//设置参数类型是xml格式
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            writer.write(body);
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
        if (response.toString().contains("<ns2:result>Y</ns2:result>"))
            return "Y";
        return "N";
    }


    static public void main(String[] args){
        String r = sendEmail("1767508581@qq.com,2381679817@qq.com","22222222222");
        System.out.println(r);

    }
}
