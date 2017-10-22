package com.hyb.backstage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config.properties")
@EnableJpaRepositories(basePackages = "com.hyb.common.dal.dao")
public class JpaConfig {
    private static final String ENTITY_PACKAGE = "com.hyb.common.dal.entity";

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        Properties properties = new Properties();
        properties.setProperty(org.hibernate.cfg.Environment.SHOW_SQL, "false");
        properties.setProperty(org.hibernate.cfg.Environment.FORMAT_SQL, "false");
        properties.setProperty(org.hibernate.cfg.Environment.DIALECT, env.getProperty("hibernate.dialect"));
        properties.setProperty(org.hibernate.cfg.Environment.QUERY_SUBSTITUTIONS, env.getProperty("hibernate.query.substitutions"));
        properties.setProperty(org.hibernate.cfg.Environment.AUTOCOMMIT, env.getProperty("hibernate.connection.autocommit"));
        properties.setProperty(org.hibernate.cfg.Environment.STATEMENT_BATCH_SIZE, env.getProperty("hibernate.jdbc.batch_size"));
        properties.setProperty(org.hibernate.cfg.Environment.CURRENT_SESSION_CONTEXT_CLASS, env.getProperty("hibernate.current_session_context_class"));
        properties.setProperty(org.hibernate.cfg.Environment.HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource);
        entityManager.setJpaProperties(properties);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setPackagesToScan(ENTITY_PACKAGE);

        return entityManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        EntityManagerFactory factory = entityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
