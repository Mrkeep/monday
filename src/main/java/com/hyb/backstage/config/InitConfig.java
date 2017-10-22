package com.hyb.backstage.config;

import com.google.common.collect.Lists;
import com.hyb.backstage.utils.AuthenticatorUtils;
import com.hyb.common.dal.converter.backstage.EnableDisableStatus;
import com.hyb.common.dal.converter.backstage.ResourceType;
import com.hyb.common.dal.converter.backstage.YesNoStatus;
import com.hyb.common.dal.dao.backstage.AdminDao;
import com.hyb.common.dal.dao.backstage.ResourceDao;
import com.hyb.common.dal.dao.backstage.RoleDao;
import com.hyb.common.dal.entity.backstage.Admin;
import com.hyb.common.dal.entity.backstage.Resource;
import com.hyb.common.dal.entity.backstage.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:config.properties")
@Import(value = {BeanConfig.class, ShiroConfig.class, JpaConfig.class})
public class InitConfig {
    private static final Logger logger = LoggerFactory.getLogger(InitConfig.class);
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @PostConstruct
    public void initData() {
//        String imagesTempPath = env.getProperty("images.temp.path");
//        checkState(StringUtils.isNotBlank(imagesTempPath));
//        System.getProperties().setProperty("images.temp.path", imagesTempPath);
//        logger.info("load property images.temp.path[{}]", imagesTempPath);
//

        long count = adminDao.count();
        if (count == 0) {
            List<Resource> adminResources = new ArrayList();
            List<Resource> userResources = new ArrayList();

            // 添加菜单
            createSystemMenu(adminResources);
            createUserInfoMenu(adminResources, userResources);
            createConsumerMenu(adminResources, userResources);

            // 添加角色
            Role adminRole = new Role();
            adminRole.setIdentifier("admin");
            adminRole.setName("系统管理员");
            adminRole.setIsDefault(YesNoStatus.NO);
            adminRole.setValid(EnableDisableStatus.ENABLE);
            adminRole.setResources(adminResources);
            adminRole = roleDao.save(adminRole);

            Role userRole = new Role();
            userRole.setIdentifier("user");
            userRole.setName("普通用户");
            userRole.setIsDefault(YesNoStatus.YES);
            userRole.setValid(EnableDisableStatus.ENABLE);
            userRole.setResources(userResources);
            userRole = roleDao.save(userRole);

            // 创建管理员
            Admin admin = new Admin();
            admin.setRoot(YesNoStatus.YES);
            admin.setValid(EnableDisableStatus.ENABLE);
            admin.setUsername("anning");
            admin.setPassword("anning");
            admin.setEmail("anning@channelsoft.com");
            admin.setMobile("15110079521");
            admin.setEmailValid(YesNoStatus.YES);
            admin.setMobileValid(YesNoStatus.YES);
            admin.setRoles(Lists.newArrayList(adminRole, userRole));
            AuthenticatorUtils.encryptPassword(admin);
            adminDao.save(admin);
        }
    }

    private void createSystemMenu(List<Resource> resources) {
        Resource resource1 = new Resource();
        resource1.setName("系统管理");
        resource1.setTreeLevel(0);
        resource1.setEnd(false);
        resource1.setParent(null);
        resource1.setPriority(0);
        resource1.setType(ResourceType.MENU);
        resource1 = resourceDao.save(resource1);
        resources.add(resource1);

        Resource resource1_1 = new Resource();
        resource1_1.setName("用户管理");
        resource1_1.setUrl("/user/list");
        resource1_1.setTreeLevel(1);
        resource1_1.setEnd(false);
        resource1_1.setParent(resource1);
        resource1_1.setPriority(0);
        resource1_1.setType(ResourceType.MENU);
        resource1_1 = resourceDao.save(resource1_1);
        resources.add(resource1_1);
        createUserFunctions(resource1_1, resources);

        Resource resource1_2 = new Resource();
        resource1_2.setName("角色管理");
        resource1_2.setUrl("/role/list");
        resource1_2.setValid(EnableDisableStatus.ENABLE);
        resource1_2.setTreeLevel(1);
        resource1_2.setEnd(false);
        resource1_2.setParent(resource1);
        resource1_2.setPriority(0);
        resource1_2.setType(ResourceType.MENU);
        resource1_2 = resourceDao.save(resource1_2);
        resources.add(resource1_2);
        createRoleFunctions(resource1_2, resources);

        Resource resource1_3 = new Resource();
        resource1_3.setName("资源管理");
        resource1_3.setUrl("/resource/list");
        resource1_3.setValid(EnableDisableStatus.ENABLE);
        resource1_3.setTreeLevel(1);
        resource1_3.setEnd(false);
        resource1_3.setParent(resource1);
        resource1_3.setPriority(0);
        resource1_3.setType(ResourceType.MENU);
        resource1_3 = resourceDao.save(resource1_3);
        resources.add(resource1_3);
        createResourceFunctions(resource1_3, resources);

        Resource resource1_4 = new Resource();
        resource1_4.setName("日志管理");
        resource1_4.setUrl("/log/list");
        resource1_4.setValid(EnableDisableStatus.ENABLE);
        resource1_4.setTreeLevel(1);
        resource1_4.setEnd(false);
        resource1_4.setParent(resource1);
        resource1_4.setPriority(0);
        resource1_4.setType(ResourceType.MENU);
        resource1_4 = resourceDao.save(resource1_4);
        resources.add(resource1_4);
        createLogFunctions(resource1_4, resources);
    }

    private void createUserFunctions(Resource parent, List<Resource> resources) {
        Resource resource1_1_1 = new Resource();
        resource1_1_1.setIdentifier("sys:user:list");
        resource1_1_1.setName("用户列表");
        resource1_1_1.setUrl("/user/list");
        resource1_1_1.setValid(EnableDisableStatus.ENABLE);
        resource1_1_1.setTreeLevel(2);
        resource1_1_1.setEnd(true);
        resource1_1_1.setParent(parent);
        resource1_1_1.setPriority(0);
        resource1_1_1.setType(ResourceType.FUNCTION);
        resource1_1_1 = resourceDao.save(resource1_1_1);
        resources.add(resource1_1_1);

        Resource resource1_1_2 = new Resource();
        resource1_1_2.setIdentifier("sys:user:create");
        resource1_1_2.setName("用户添加");
        resource1_1_2.setUrl("/user/create");
        resource1_1_2.setValid(EnableDisableStatus.ENABLE);
        resource1_1_2.setTreeLevel(2);
        resource1_1_2.setEnd(true);
        resource1_1_2.setParent(parent);
        resource1_1_2.setPriority(0);
        resource1_1_2.setType(ResourceType.FUNCTION);
        resource1_1_2 = resourceDao.save(resource1_1_2);
        resources.add(resource1_1_2);

        Resource resource1_1_3 = new Resource();
        resource1_1_3.setIdentifier("sys:user:update");
        resource1_1_3.setName("用户修改");
        resource1_1_3.setUrl("/user/update/**");
        resource1_1_3.setValid(EnableDisableStatus.ENABLE);
        resource1_1_3.setTreeLevel(2);
        resource1_1_3.setEnd(true);
        resource1_1_3.setParent(parent);
        resource1_1_3.setPriority(0);
        resource1_1_3.setType(ResourceType.FUNCTION);
        resource1_1_3 = resourceDao.save(resource1_1_3);
        resources.add(resource1_1_3);

        Resource resource1_1_4 = new Resource();
        resource1_1_4.setIdentifier("sys:user:view");
        resource1_1_4.setName("用户查看");
        resource1_1_4.setUrl("/user/view/**");
        resource1_1_4.setValid(EnableDisableStatus.ENABLE);
        resource1_1_4.setTreeLevel(2);
        resource1_1_4.setEnd(true);
        resource1_1_4.setParent(parent);
        resource1_1_4.setPriority(0);
        resource1_1_4.setType(ResourceType.FUNCTION);
        resource1_1_4 = resourceDao.save(resource1_1_4);
        resources.add(resource1_1_4);

        Resource resource1_1_5 = new Resource();
        resource1_1_5.setIdentifier("sys:user:delete");
        resource1_1_5.setName("用户删除");
        resource1_1_5.setUrl("/user/delete/**");
        resource1_1_5.setValid(EnableDisableStatus.ENABLE);
        resource1_1_5.setTreeLevel(2);
        resource1_1_5.setEnd(true);
        resource1_1_5.setParent(parent);
        resource1_1_5.setPriority(0);
        resource1_1_5.setType(ResourceType.FUNCTION);
        resource1_1_5 = resourceDao.save(resource1_1_5);
        resources.add(resource1_1_5);

        Resource resource1_1_6 = new Resource();
        resource1_1_6.setIdentifier("sys:user:allot");
        resource1_1_6.setName("分配角色");
        resource1_1_6.setUrl("/user/allot/**");
        resource1_1_6.setValid(EnableDisableStatus.ENABLE);
        resource1_1_6.setTreeLevel(2);
        resource1_1_6.setEnd(true);
        resource1_1_6.setParent(parent);
        resource1_1_6.setPriority(0);
        resource1_1_6.setType(ResourceType.FUNCTION);
        resource1_1_6 = resourceDao.save(resource1_1_6);
        resources.add(resource1_1_6);

        Resource resource1_1_7 = new Resource();
        resource1_1_7.setIdentifier("sys:user:resetPwd");
        resource1_1_7.setName("重置密码");
        resource1_1_7.setUrl("/user/password/reset/**");
        resource1_1_7.setValid(EnableDisableStatus.ENABLE);
        resource1_1_7.setTreeLevel(2);
        resource1_1_7.setEnd(true);
        resource1_1_7.setParent(parent);
        resource1_1_7.setPriority(0);
        resource1_1_7.setType(ResourceType.FUNCTION);
        resource1_1_7 = resourceDao.save(resource1_1_7);
        resources.add(resource1_1_7);
    }

    public void createRoleFunctions(Resource parent, List<Resource> resources) {
        Resource resource1_2_1 = new Resource();
        resource1_2_1.setIdentifier("sys:role:list");
        resource1_2_1.setName("角色列表");
        resource1_2_1.setUrl("/role/list");
        resource1_2_1.setValid(EnableDisableStatus.ENABLE);
        resource1_2_1.setTreeLevel(2);
        resource1_2_1.setEnd(true);
        resource1_2_1.setParent(parent);
        resource1_2_1.setPriority(0);
        resource1_2_1.setType(ResourceType.FUNCTION);
        resource1_2_1 = resourceDao.save(resource1_2_1);
        resources.add(resource1_2_1);

        Resource resource1_2_2 = new Resource();
        resource1_2_2.setIdentifier("sys:role:create");
        resource1_2_2.setName("角色添加");
        resource1_2_2.setUrl("/role/create");
        resource1_2_2.setValid(EnableDisableStatus.ENABLE);
        resource1_2_2.setTreeLevel(2);
        resource1_2_2.setEnd(true);
        resource1_2_2.setParent(parent);
        resource1_2_2.setPriority(0);
        resource1_2_2.setType(ResourceType.FUNCTION);
        resource1_2_2 = resourceDao.save(resource1_2_2);
        resources.add(resource1_2_2);

        Resource resource1_2_3 = new Resource();
        resource1_2_3.setIdentifier("sys:role:update");
        resource1_2_3.setName("角色修改");
        resource1_2_3.setUrl("/role/update/**");
        resource1_2_3.setValid(EnableDisableStatus.ENABLE);
        resource1_2_3.setTreeLevel(2);
        resource1_2_3.setEnd(true);
        resource1_2_3.setParent(parent);
        resource1_2_3.setPriority(0);
        resource1_2_3.setType(ResourceType.FUNCTION);
        resource1_2_3 = resourceDao.save(resource1_2_3);
        resources.add(resource1_2_3);

        Resource resource1_2_4 = new Resource();
        resource1_2_4.setIdentifier("sys:role:view");
        resource1_2_4.setName("角色查看");
        resource1_2_4.setUrl("/role/view/**");
        resource1_2_4.setValid(EnableDisableStatus.ENABLE);
        resource1_2_4.setTreeLevel(2);
        resource1_2_4.setEnd(true);
        resource1_2_4.setParent(parent);
        resource1_2_4.setPriority(0);
        resource1_2_4.setType(ResourceType.FUNCTION);
        resource1_2_4 = resourceDao.save(resource1_2_4);
        resources.add(resource1_2_4);

        Resource resource1_2_5 = new Resource();
        resource1_2_5.setIdentifier("sys:role:delete");
        resource1_2_5.setName("角色删除");
        resource1_2_5.setUrl("/role/delete/**");
        resource1_2_5.setValid(EnableDisableStatus.ENABLE);
        resource1_2_5.setTreeLevel(2);
        resource1_2_5.setEnd(true);
        resource1_2_5.setParent(parent);
        resource1_2_5.setPriority(0);
        resource1_2_5.setType(ResourceType.FUNCTION);
        resource1_2_5 = resourceDao.save(resource1_2_5);
        resources.add(resource1_2_5);

        Resource resource1_2_6 = new Resource();
        resource1_2_6.setIdentifier("sys:role:allot");
        resource1_2_6.setName("分配资源");
        resource1_2_6.setUrl("/role/allot/**");
        resource1_2_6.setValid(EnableDisableStatus.ENABLE);
        resource1_2_6.setTreeLevel(2);
        resource1_2_6.setEnd(true);
        resource1_2_6.setParent(parent);
        resource1_2_6.setPriority(0);
        resource1_2_6.setType(ResourceType.FUNCTION);
        resource1_2_6 = resourceDao.save(resource1_2_6);
        resources.add(resource1_2_6);
    }

    public void createResourceFunctions(Resource parent, List<Resource> resources) {
        Resource resource1_3_1 = new Resource();
        resource1_3_1.setIdentifier("sys:resource:list");
        resource1_3_1.setName("资源列表");
        resource1_3_1.setUrl("/resource/list");
        resource1_3_1.setValid(EnableDisableStatus.ENABLE);
        resource1_3_1.setTreeLevel(2);
        resource1_3_1.setEnd(true);
        resource1_3_1.setParent(parent);
        resource1_3_1.setPriority(0);
        resource1_3_1.setType(ResourceType.FUNCTION);
        resource1_3_1 = resourceDao.save(resource1_3_1);
        resources.add(resource1_3_1);

        Resource resource1_3_2 = new Resource();
        resource1_3_2.setIdentifier("sys:resource:create");
        resource1_3_2.setName("资源添加");
        resource1_3_2.setUrl("/resource/create");
        resource1_3_2.setValid(EnableDisableStatus.ENABLE);
        resource1_3_2.setTreeLevel(2);
        resource1_3_2.setEnd(true);
        resource1_3_2.setParent(parent);
        resource1_3_2.setPriority(0);
        resource1_3_2.setType(ResourceType.FUNCTION);
        resource1_3_2 = resourceDao.save(resource1_3_2);
        resources.add(resource1_3_2);

        Resource resource1_3_3 = new Resource();
        resource1_3_3.setIdentifier("sys:resource:update");
        resource1_3_3.setName("资源修改");
        resource1_3_3.setUrl("/resource/update/**");
        resource1_3_3.setValid(EnableDisableStatus.ENABLE);
        resource1_3_3.setTreeLevel(2);
        resource1_3_3.setEnd(true);
        resource1_3_3.setParent(parent);
        resource1_3_3.setPriority(0);
        resource1_3_3.setType(ResourceType.FUNCTION);
        resource1_3_3 = resourceDao.save(resource1_3_3);
        resources.add(resource1_3_3);

        Resource resource1_3_4 = new Resource();
        resource1_3_4.setIdentifier("sys:resource:view");
        resource1_3_4.setName("资源查看");
        resource1_3_4.setUrl("/resource/view/**");
        resource1_3_4.setValid(EnableDisableStatus.ENABLE);
        resource1_3_4.setTreeLevel(2);
        resource1_3_4.setEnd(true);
        resource1_3_4.setParent(parent);
        resource1_3_4.setPriority(0);
        resource1_3_4.setType(ResourceType.FUNCTION);
        resource1_3_4 = resourceDao.save(resource1_3_4);
        resources.add(resource1_3_4);

        Resource resource1_3_5 = new Resource();
        resource1_3_5.setIdentifier("sys:resource:delete");
        resource1_3_5.setName("资源删除");
        resource1_3_5.setUrl("/resource/delete/**");
        resource1_3_5.setValid(EnableDisableStatus.ENABLE);
        resource1_3_5.setTreeLevel(2);
        resource1_3_5.setEnd(true);
        resource1_3_5.setParent(parent);
        resource1_3_5.setPriority(0);
        resource1_3_5.setType(ResourceType.FUNCTION);
        resource1_3_5 = resourceDao.save(resource1_3_5);
        resources.add(resource1_3_5);
    }

    public void createLogFunctions(Resource parent, List<Resource> resources) {
        Resource resource1_4_1 = new Resource();
        resource1_4_1.setIdentifier("sys:log:list");
        resource1_4_1.setName("日志列表");
        resource1_4_1.setUrl("/log/list");
        resource1_4_1.setValid(EnableDisableStatus.ENABLE);
        resource1_4_1.setTreeLevel(2);
        resource1_4_1.setEnd(true);
        resource1_4_1.setParent(parent);
        resource1_4_1.setPriority(0);
        resource1_4_1.setType(ResourceType.FUNCTION);
        resource1_4_1 = resourceDao.save(resource1_4_1);
        resources.add(resource1_4_1);

        Resource resource1_4_2 = new Resource();
        resource1_4_2.setIdentifier("sys:log:view");
        resource1_4_2.setName("日志查看");
        resource1_4_2.setUrl("/log/view/**");
        resource1_4_2.setValid(EnableDisableStatus.ENABLE);
        resource1_4_2.setTreeLevel(2);
        resource1_4_2.setEnd(true);
        resource1_4_2.setParent(parent);
        resource1_4_2.setPriority(0);
        resource1_4_2.setType(ResourceType.FUNCTION);
        resource1_4_2 = resourceDao.save(resource1_4_2);
        resources.add(resource1_4_2);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void createUserInfoMenu(List<Resource> role1ResourceList, List<Resource> role2ResourceList) {
        Resource resource2 = new Resource();
        resource2.setName("个人中心");
        resource2.setValid(EnableDisableStatus.ENABLE);
        resource2.setTreeLevel(0);
        resource2.setEnd(false);
        resource2.setParent(null);
        resource2.setPriority(0);
        resource2.setType(ResourceType.MENU);
        resource2 = resourceDao.save(resource2);
        role1ResourceList.add(resource2);
        role2ResourceList.add(resource2);

        Resource resource2_1 = new Resource();
        resource2_1.setName("个人信息");
        resource2_1.setUrl("/profile/info");
        resource2_1.setValid(EnableDisableStatus.ENABLE);
        resource2_1.setTreeLevel(1);
        resource2_1.setEnd(false);
        resource2_1.setParent(resource2);
        resource2_1.setPriority(0);
        resource2_1.setType(ResourceType.MENU);
        resource2_1 = resourceDao.save(resource2_1);
        role1ResourceList.add(resource2_1);
        role2ResourceList.add(resource2_1);

        Resource resource2_1_1 = new Resource();
        resource2_1_1.setIdentifier("user:profile:view");
        resource2_1_1.setName("个人信息查看");
        resource2_1_1.setUrl("/profile/info");
        resource2_1_1.setValid(EnableDisableStatus.ENABLE);
        resource2_1_1.setTreeLevel(2);
        resource2_1_1.setEnd(true);
        resource2_1_1.setParent(resource2_1);
        resource2_1_1.setPriority(0);
        resource2_1_1.setType(ResourceType.FUNCTION);
        resource2_1_1 = resourceDao.save(resource2_1_1);
        role1ResourceList.add(resource2_1_1);
        role2ResourceList.add(resource2_1_1);

        Resource resource2_2 = new Resource();
        resource2_2.setName("更改密码");
        resource2_2.setUrl("/profile/password/change");
        resource2_2.setValid(EnableDisableStatus.ENABLE);
        resource2_2.setTreeLevel(1);
        resource2_2.setEnd(false);
        resource2_2.setParent(resource2);
        resource2_2.setPriority(0);
        resource2_2.setType(ResourceType.MENU);
        resource2_2 = resourceDao.save(resource2_2);
        role1ResourceList.add(resource2_2);
        role2ResourceList.add(resource2_2);

        Resource resource2_2_1 = new Resource();
        resource2_2_1.setIdentifier("user:profile:changePwd");
        resource2_2_1.setName("更改密码");
        resource2_2_1.setUrl("/profile/password/change");
        resource2_2_1.setValid(EnableDisableStatus.ENABLE);
        resource2_2_1.setTreeLevel(2);
        resource2_2_1.setEnd(true);
        resource2_2_1.setParent(resource2_2);
        resource2_2_1.setPriority(0);
        resource2_2_1.setType(ResourceType.FUNCTION);
        resource2_2_1 = resourceDao.save(resource2_2_1);
        role1ResourceList.add(resource2_2_1);
        role2ResourceList.add(resource2_2_1);
    }


    public void createConsumerMenu(List<Resource> adminResources, List<Resource> userResources) {
        Resource resource = new Resource();
        resource.setName("用户管理");
        resource.setValid(EnableDisableStatus.ENABLE);
        resource.setTreeLevel(0);
        resource.setEnd(false);
        resource.setParent(null);
        resource.setPriority(0);
        resource.setType(ResourceType.MENU);
        resource = resourceDao.save(resource);
        adminResources.add(resource);
        userResources.add(resource);

        Resource resource1 = new Resource();
        resource1.setName("注册用户管理");
        resource1.setUrl("/customer/list");
        resource1.setValid(EnableDisableStatus.ENABLE);
        resource1.setTreeLevel(1);
        resource1.setEnd(false);
        resource1.setParent(resource);
        resource1.setPriority(0);
        resource1.setType(ResourceType.MENU);
        resource1 = resourceDao.save(resource1);
        adminResources.add(resource1);
        userResources.add(resource1);
    }


}
