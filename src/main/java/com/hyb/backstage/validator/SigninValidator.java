package com.hyb.backstage.validator;

import com.hyb.backstage.base.IValidator;
import com.hyb.backstage.base.ValidateFailedException;
import com.hyb.backstage.form.AdminForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SigninValidator implements IValidator<AdminForm> {
    private static final Logger logger = LoggerFactory.getLogger(SigninValidator.class);

    private String captchaExpected;

    @Override
    public boolean validate(AdminForm adminForm) throws ValidateFailedException {
        logger.info("开始验证userForm参数");

        if (adminForm == null || adminForm.getAdmin() == null) {
            String msg = String.format("userForm参数不能为空");
            logger.error(msg);
            throw new ValidateFailedException("adminForm", msg);
        }

        if (StringUtils.isEmpty(adminForm.getAdmin().getUsername())) {
            String msg = String.format("用户名不能为空");
            logger.error(msg);
            throw new ValidateFailedException("user.username", msg);
        }

        if (adminForm.getAdmin().getUsername().length() < 4 || adminForm.getAdmin().getUsername().length() > 20) {
            String msg = String.format("用户名长度必须在4-20个字符之间");
            logger.error(msg);
            throw new ValidateFailedException("user.username", msg);
        }

        if (StringUtils.isEmpty(adminForm.getAdmin().getPassword())) {
            String msg = String.format("密码不能为空");
            logger.error(msg);
            throw new ValidateFailedException("user.password", msg);
        }

        if (StringUtils.isEmpty(adminForm.getCaptcha())) {
            String msg = String.format("验证码不能为空");
            logger.error(msg);
            throw new ValidateFailedException("captcha", msg);
        }

        if (adminForm.getCaptcha().length() != 5) {
            String msg = String.format("验证码长度必须是5个字符");
            logger.error(msg);
            throw new ValidateFailedException("captcha", msg);
        }

        if (!adminForm.getCaptcha().equals(captchaExpected)) {
            String msg = String.format("验证码不匹配");
            logger.error(msg);
            throw new ValidateFailedException("captcha", msg);
        }

        return true;
    }

    public void setCaptchaExpected(String captchaExpected) {
        this.captchaExpected = captchaExpected;
    }
}
