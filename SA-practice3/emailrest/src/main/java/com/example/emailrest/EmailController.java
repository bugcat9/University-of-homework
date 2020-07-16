package com.example.emailrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/email",method = RequestMethod.POST)
    public String sendEmail(@RequestParam(value = "_url",defaultValue = "1767508581@qq.com") String _url,
                            @RequestParam(value = "_payload",defaultValue = "hellow world") String _payload ){
            System.out.println("email服务被调用："+_url+":"+_payload);
            return emailService.sendEmail(_url,_payload);
    }

//    @RequestMapping(value = "/emailB",method = RequestMethod.POST)
//    public String sendEmailBatch(@RequestParam(value = "_url[]",defaultValue = "1767508581@qq.com") String[] _url,
//                                 @RequestParam(value = "_payload",defaultValue = "hello world1") String _payload){
//            return emailService.sendEmailBatch(_url,_payload);
//    }

    @RequestMapping(value = "/emailBatch",method = RequestMethod.POST)
    public String sendEmailBatch2(@RequestBody EmailBatch email){
        return emailService.sendEmailBatch(email.getUrls(),email.get_payload());
    }

    @RequestMapping(value = "/validateEmail",method = RequestMethod.POST)
    public String validateEmailAddress(@RequestParam(value = "_url",defaultValue = "176") String _url){
            System.out.println("validateEmail服务被调用："+_url);
            return emailService.validateEmailAddress(_url);
    }

}
