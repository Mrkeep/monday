package com.hyb.backstage.controller;

import com.hyb.common.util.error.ErrorCode;
import com.hyb.common.util.error.HybException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by popant on 2017/2/21.
 */
@Controller
@RequestMapping("/util")
public class DemoController{

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() throws HybException {
        throw new HybException(ErrorCode.UnknownError);
        //test
//        return "demo/list";
    }
}
