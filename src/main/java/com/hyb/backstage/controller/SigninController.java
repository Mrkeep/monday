package com.hyb.backstage.controller;

import com.hyb.backstage.base.BaseController;
import com.hyb.backstage.base.ValidateFailedException;
import com.hyb.backstage.form.AdminForm;
import com.hyb.backstage.validator.SigninValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signin")
@SessionAttributes("KAPTCHA_SESSION_KEY")
public class SigninController extends BaseController {
    private static final String kaptchaSessionKey = "KAPTCHA_SESSION_KEY";

    @Autowired
    private SigninValidator signinValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String signinPage(@ModelAttribute AdminForm adminForm, ModelMap modelMap) {
        if (modelMap.containsKey(BINDING_RESULT_KEY)) {
            modelMap.addAttribute(BindingResult.class.getName().concat(".adminForm"), modelMap.get(BINDING_RESULT_KEY));
        }
        return "user/signin";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signin(@ModelAttribute("command") AdminForm adminForm,BindingResult bindingResult,
                         @ModelAttribute(kaptchaSessionKey) String captchaExpected,
                         RedirectAttributes redirectAttributes) {
        try {
            signinValidator.setCaptchaExpected(captchaExpected);
            signinValidator.validate(adminForm);
        } catch (ValidateFailedException e) {
            logger.error(e.getMessage(), e);
//            bindingResult.addError(new FieldError("admin","username",e.getMessage()));
//            bindingResult.rejectValue("admin.username", "admin.username",e.getMessage());
            bindingResult.reject(null,e.getMessage());
//            bindingResult.addError(new ObjectError("admin.username", e.getMessage()));
        }

        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute(BINDING_RESULT_KEY, bindingResult);
//            redirectAttributes.addFlashAttribute(adminForm);
//            return "redirect:/signin";
            return "user/signin";
        }

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(adminForm.getAdmin().getUsername(), adminForm.getAdmin().getPassword());
        usernamePasswordToken.setRememberMe(adminForm.isRememberMe());

        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            if (e instanceof IncorrectCredentialsException) {
                bindingResult.addError(new ObjectError("user.username", "用户名或密码不正确！"));
            } else {
                bindingResult.addError(new ObjectError("user.username", e.getMessage()));
            }
            logger.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_KEY, bindingResult);
            redirectAttributes.addFlashAttribute(adminForm);
            return "redirect:/signin";
        }
        return "redirect:/dashboard";
    }
}
