package com.hyb.backstage.controller;

import com.hyb.backstage.base.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignoutController extends BaseController {
    @RequestMapping("/signout")
    public String dashboard() {
        logger.info("退出登录");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/signin";
    }
}
