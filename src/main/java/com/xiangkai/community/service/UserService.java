package com.xiangkai.community.service;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.entity.User;
import com.xiangkai.community.mapper.UserMapper;
import com.xiangkai.community.util.CommunityUtil;
import com.xiangkai.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserService implements CommunityConstant {

    @Value("${com.community.domain}")
    private String domain;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    public Map<String, String> register(User user) {
        Map<String, String> map = new HashMap<>();

        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("非法参数：用户信息为空");
        }

        // 校验用户信息
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        User u1 = userMapper.selectByUserName(user.getUsername());
        if (!Objects.isNull(u1)) {
            map.put("usernameMsg", "用户名已经被注册");
            return map;
        }
        User u2 = userMapper.selectByEmail(user.getEmail());
        if (!Objects.isNull(u2)) {
            map.put("emailMsg", "邮箱已经被注册");
            return map;
        }

        // 保存用户信息
        user.setType(0);
        user.setStatus(0);
        user.setSalt(CommunityUtil.generateUUID().substring(5));
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setPassword(CommunityUtil.generateMD5(user.getPassword() + user.getSalt()));
        String headerUrl = String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeaderUrl(headerUrl);
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String html = templateEngine.process("/mail/activation", context);
        mailClient.send(user.getEmail(), "激活邮件", html);
        return map;
    }

    public Integer activation(int userId, String code) {
        User user = userMapper.selectById(userId);

        if (!code.equals(user.getActivationCode())) {
            return ACTIVATION_FAIL;
        } else if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }
    }

}
