package com.example.emailsoap;

import com.example.email.EmailRequest;
import com.example.email.EmailResponse;
import com.example.email.ValidateEmailAddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class EmailEndponit {
    private static final String NAMESPACE_URI = "http://www.howtodoinjava.com/xml/email";

    @Autowired
    private EmailService emailService;

    //发送邮件服务
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "EmailRequest")
    @ResponsePayload
    public EmailResponse sendEmail(@RequestPayload EmailRequest request) {
        EmailResponse response = new EmailResponse();
        response.setResult(emailService.sendEmail(request.getUrl(),request.getPayload()));
        return response;
    }


    //验证邮件服务
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ValidateEmailAddressRequest")
    @ResponsePayload
    public EmailResponse validateEmailAddress(@RequestPayload ValidateEmailAddressRequest request) {
        EmailResponse response = new EmailResponse();
        response.setResult(emailService.validateEmailAddress(request.getUrl()));
        return response;
    }


}
