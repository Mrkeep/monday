package com.hyb.backstage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @RequestMapping("/user/register")
    String registerNew(){

        return "user/register";
    }
}
