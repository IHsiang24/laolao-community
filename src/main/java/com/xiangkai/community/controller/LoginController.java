package com.xiangkai.community.controller;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.entity.User;
import com.xiangkai.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getloginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, String> map = userService.register(user);
        if (CollectionUtils.isEmpty(map)) {
            model.addAttribute("msg", "账号注册成功，我们已经向您发送激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }
        model.addAttribute("usernameMsg", map.get("usernameMsg"));
        model.addAttribute("passwordMsg", map.get("passwordMsg"));
        model.addAttribute("emailMsg", map.get("emailMsg"));

        return "/site/register";
    }

    @RequestMapping(path = "/activation/{userId}/{code}")
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code) {

        Integer activationResult = userService.activation(userId, code);

        if (activationResult.equals(ACTIVATION_SUCCESS)) {
            model.addAttribute("msg", "账号激活成功，您可以正常使用账号！");
            model.addAttribute("target","/login");
        } else if (activationResult.equals(ACTIVATION_FAIL)) {
            model.addAttribute("msg", "账号激活失败，请联系管理员处理！");
            model.addAttribute("target","/index");
        } else {
            model.addAttribute("msg", "账号已激活过，请勿重复激活！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

}
