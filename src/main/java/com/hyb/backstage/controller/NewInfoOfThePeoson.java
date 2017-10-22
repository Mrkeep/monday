package com.hyb.backstage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personInfo")
public class NewInfoOfThePeoson {
    @RequestMapping("/info")
    String personInfo (){
        return "/info";
    }
}
