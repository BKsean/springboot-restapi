package com.example.springbootrestapi.controller;

import com.example.springbootrestapi.bean.HelloBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorldController {

    @Autowired
    private MessageSource messageSource;
    @GetMapping("/hello-world")
    public String hello(){
        return "hello-world";
    }

    @GetMapping("/bean")
    public HelloBean bean(){
        return new HelloBean("hello-world");
    }

    @GetMapping("/bean/{name}")
    public HelloBean beanWithName(@PathVariable String name){
        return new HelloBean(String.format("Hello World, %s",name));
    }

    @GetMapping("/hello-world-internationalized")
    public String helloString(@RequestHeader(name="Accept-Language",required = false)Locale locale){
        return messageSource.getMessage("greeting.message",null,locale);
    }
}
