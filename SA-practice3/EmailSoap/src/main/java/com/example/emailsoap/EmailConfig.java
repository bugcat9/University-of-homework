package com.example.emailsoap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class EmailConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext)
    {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/email/*");
    }

    @Bean(name = "Emailwsdl")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema emailSchema)
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("EmailServicePort");
        wsdl11Definition.setServiceName("EmailService");
        wsdl11Definition.setLocationUri("/email/emailservice");
        wsdl11Definition.setTargetNamespace("http://www.howtodoinjava.com/xml/email");
        wsdl11Definition.setSchema(emailSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema emailSchema()
    {
        return new SimpleXsdSchema(new ClassPathResource("Email.xsd"));
    }

}
