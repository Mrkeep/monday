package com.hyb.backstage.config;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Order(2)
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {InitConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {MvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        servletContext.addFilter("characterEncodingFilter", filter).addMappingForUrlPatterns(null, false, "/*");

        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
        servletContext.addFilter("openEntityManagerInViewFilter", openEntityManagerInViewFilter).addMappingForUrlPatterns(null, false, "/*");

        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        delegatingFilterProxy.setTargetFilterLifecycle(true);
        servletContext.addFilter("shiroFilter", delegatingFilterProxy).addMappingForUrlPatterns(null, false, "/*");

        servletContext.addServlet("kaptcha", new KaptchaServlet()).addMapping("/kaptcha");

        super.onStartup(servletContext);
    }
}
