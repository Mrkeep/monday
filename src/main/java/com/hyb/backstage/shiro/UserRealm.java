package com.hyb.backstage.shiro;

import com.hyb.backstage.utils.AuthenticatorUtils;
import com.hyb.common.dal.converter.backstage.EnableDisableStatus;
import com.hyb.common.dal.dao.backstage.AdminDao;
import com.hyb.common.dal.entity.backstage.Admin;
import com.hyb.common.dal.entity.backstage.Resource;
import com.hyb.common.dal.entity.backstage.Role;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private AdminDao adminDao;

    @PostConstruct
    public void postConstruct() {
        if (adminDao == null) {
            logger.error("UserService instance can not be null");
            System.exit(-1);
        }

        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);

        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Admin user = (Admin) principals.getPrimaryPrincipal();
        user = adminDao.findOne(user.getId());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        Set<String> stringPermissions = new HashSet();
        if (user.getRoles() != null && user.getRoles().size() > 0) {
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getResources() != null && role.getResources().size() > 0) {
                    for (Resource resource : role.getResources()) {
                        if (resource.getValid() == EnableDisableStatus.ENABLE && !StringUtils.isEmpty(resource.getIdentifier())) {
                            stringPermissions.add(resource.getIdentifier());
                        }
                    }
                }
            }
        }

        authorizationInfo.setStringPermissions(stringPermissions);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        Admin user = adminDao.findByUsername((String) token.getPrincipal());
        if (user == null) {
            user = adminDao.findByEmail((String) token.getPrincipal());
        }
        if (user == null) {
            throw new UnknownAccountException("无效用户名！");
        }
        if (EnableDisableStatus.ENABLE != user.getValid()) {
            throw new LockedAccountException("该用户已经无效！");
        }
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(AuthenticatorUtils.getSalt(user)),
                getName()
        );
    }
}
