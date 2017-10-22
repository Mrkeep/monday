package com.hyb.backstage.config;

import com.hyb.backstage.shiro.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
@Import(BeanConfig.class)
public class ShiroConfig {
    @Autowired
    private Environment env;

    @Autowired
    private UserRealm userRealm;

    @PostConstruct
    public void postConstruct() {
        SecurityUtils.setSecurityManager(securityManager());
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(Integer.valueOf(env.getProperty("session.max.age")) * 1000);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(false);
        return defaultWebSessionManager;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(Integer.valueOf(env.getProperty("rememberme.max.age")));
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/signin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        StringBuilder sb = new StringBuilder();
        sb.append("/api/** = anon").append("\n");
        sb.append("/resources/** = anon").append("\n");
        sb.append("/signin = anon").append("\n");
        sb.append("/signup = anon").append("\n");
        sb.append("/user/password/forget = anon").append("\n");
        sb.append("/kaptcha = anon").append("\n");
        sb.append("/signout = logout").append("\n");
        sb.append("/dashboard = user").append("\n");
        sb.append("/** = user");
        shiroFilterFactoryBean.setFilterChainDefinitions(sb.toString());
        return shiroFilterFactoryBean;
    }

    /**
     * 必须注册为static，不然env==null
     * 相关链接：https://issues.apache.org/jira/browse/SHIRO-222
     * http://stackoverflow.com/questions/15539485/specifying-shiros-lifecyclebeanpostprocessor-in-programmatic-spring-configurat
     *
     * LifecycleBeanPostProcessor 用于在实现了 Initializable 接口的 Shiro bean 初始化时调用 Initializable接口回调,在实现了Destroyable接口的Shiro bean销毁时调用Destroyable接 口回调。如 UserRealm 就实现了 Initializable,而 DefaultSecurityManager 实现了 Destroyable。
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
