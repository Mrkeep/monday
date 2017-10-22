package com.hyb.backstage.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@ComponentScan(
        basePackages = {"com.hyb.backstage"},
        excludeFilters = @ComponentScan.Filter(value = {Controller.class, ControllerAdvice.class})
)
public class BeanConfig {
}
