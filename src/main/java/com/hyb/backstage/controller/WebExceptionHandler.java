package com.hyb.backstage.controller;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class WebExceptionHandler {
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleUnauthorizedException(HttpServletRequest request, UnauthorizedException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("redirect:/unauthorized");
        return mav;
    }

    /*
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(HttpServletRequest request, Exception exception) {
        ModelAndView mav = new ModelAndView();
        logger.error(exception.getMessage(), exception);
        mav.addObject("message", exception.getMessage());
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        //mav.setViewName("redirect:/error");
        mav.setViewName(Views.LIST_ELEMENTS);
        return mav;
    }
    */
}