package com.hyb.backstage.config;

import com.hyb.backstage.controller.WebExceptionHandler;
import com.hyb.backstage.shiro.DefaultInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.PostConstruct;
import java.util.List;


@EnableWebMvc
@Import(WebShiroConfig.class)
@EnableSpringDataWebSupport
@ComponentScan("com.hyb.backstage.controller")
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private DefaultInterceptor defaultInterceptor;

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSize(1024 * 1024 * 1024);
        return commonsMultipartResolver;
    }

    /**
     * 配置显示错误信息
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        return messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/dashboard");
        registry.addViewController("/error").setViewName("failure");
        registry.addViewController("/success").setViewName("success");
        registry.addViewController("/unauthorized").setViewName("unauthorized");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        super.configurePathMatch(configurer);
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);

//        registry.addConverter(new YesNoStatusConverter());
//        registry.addConverter(new EnableDisableStatusConverter());
//        registry.addConverter(new ResourceTypeConverter());
//        registry.addConverter(new InsuranceTypeConverter());
//        registry.addConverter(new SecondInsuranceTypeConverter());
//        registry.addConverter(new ChargePeriodTypeConverter());
//        registry.addConverter(new InsuranceCompanyTypeConverter());
//        registry.addConverter(new InsuranceCompanyCategoryConverter());
//        registry.addConverter(new InsuranceStatusConverter());
//        registry.addConverter(new OrderTypeConverter());
//        registry.addConverter(new CertificateTypeConverter());
//        registry.addConverter(new CommonStatusTypeConverter());
//        registry.addConverter(new CommonStatusDefaultTypeConverter());
//        registry.addConverter(new DescriptionTypeConverter());
//        registry.addConverter(new InsurancePlanStatusConverter());
//        registry.addConverter(new PayTypeConverter());
//        registry.addConverter(new GenderConverter());
//        registry.addConverter(new PremiumTypeConverter());
//        registry.addConverter(new PremiumAgeTypeConverter());
//        registry.addConverter(new CoveragePeriodTypeConverter());
//        registry.addConverter(new CoverageRateTypeConverter());
//        registry.addConverter(new InsureViewTypeConverter());
//        registry.addConverter(new CoverageTypeConverter());
//        registry.addConverter(new ChargePeriodTypeConverter());
//        registry.addConverter(new OccupationPerilLevelConverter());
//        registry.addConverter(new PriceConverter());
//        registry.addConverter(new PolicyStatusConverter());
//        registry.addConverter(new PolicyImageSourceTypeConverter());
//        registry.addConverter(new PolicyTypeConverter());
//        registry.addConverter(new InsuranceRecommendPriorityConverter());
//        registry.addConverter(new ElementTypeConverter());
//        registry.addConverter(new TagTypeConverter());
//        registry.addConverter(new InsureTypeConverter());
//        registry.addConverter(new AnnualIncomeTypeConverter());
//        registry.addConverter(new InsuranceSchemeTypeConverter());
//        registry.addConverter(new PayRecordStatusConverter());
//        registry.addConverter(new PolicyPhotoStatusConverter());
//        registry.addConverter(new PolicyPhotoFailureTypeConverter());

        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd HH:mm:ss");
        dateFormatter.setLenient(true);
        registry.addFormatter(dateFormatter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(defaultInterceptor);
        interceptorRegistration.addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns("/resources/**");
        interceptorRegistration.excludePathPatterns("/signin");
        interceptorRegistration.excludePathPatterns("/signup");
        interceptorRegistration.excludePathPatterns("/kaptcha");
        interceptorRegistration.excludePathPatterns("/user/password/forget");
        interceptorRegistration.excludePathPatterns("/signout");
    }

    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return new WebExceptionHandler();
    }
}
