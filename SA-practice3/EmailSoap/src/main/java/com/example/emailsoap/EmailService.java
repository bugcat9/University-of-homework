package com.example.emailsoap;

//EmailService的接口
public interface EmailService {
    public String sendEmail(String _url, String _payload); //邮件地址为_url，内容为_payload
    public String sendEmailBatch(String[] _url, String _payload); //批量发送邮件
    public String validateEmailAddress(String _url);        //正则表达式判断是否为有效邮箱
}
