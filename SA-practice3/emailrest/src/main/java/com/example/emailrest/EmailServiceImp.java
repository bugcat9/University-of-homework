package com.example.emailrest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailServiceImp implements EmailService {

    String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    Pattern regex = Pattern.compile(check);

    @Override
    public String sendEmail(String _url, String _payload) {
        return sendMsg(_url,_payload);
    }

    @Override
    public String sendEmailBatch(String[] _url, String _payload) {
        //System.out.println(_url);
        String result ="Y";
        for (String address:_url){
            String a = sendMsg(address,_payload);
            if (a=="N")
                result ="N";
        }
        return result;
    }

    @Override
    public String validateEmailAddress(String _url) {
        Matcher matcher = regex.matcher(_url);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);
        if(isMatched)
            return "Y";
        else
            return "N";
    }

    //阿里云发送邮件的代码
    private String sendMsg(String email, String msg){
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                "LTAI4FnFAWu8z3VGdGT8AvTn", "VzH6nHOM1wr3jW9fOj5D19hj3Mjioo");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
//       BatchSendMailRequest   batchSendMailRequest =new BatchSendMailRequest();

        try {
            //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            request.setAccountName("zhouning@cugyn.xyz");
            request.setFromAlias("zn");
            request.setAddressType(1);
            //request.setTagName("控制台创建的标签");
            request.setReplyToAddress(true);
            request.setToAddress(email);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            //request.setToAddress("邮箱1,邮箱2");
            request.setSubject("test");
            request.setHtmlBody(msg);
            //开启需要备案，0关闭，1开启
            //request.setClickTrace("0");
            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
            return "Y";
        } catch (ServerException e) {
            //捕获错误异常码
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        }
        catch (ClientException e) {
            //捕获错误异常码

            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        }
        return  "N";
    }
}
