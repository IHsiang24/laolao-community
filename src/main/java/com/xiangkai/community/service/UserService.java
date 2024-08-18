package com.xiangkai.community.service;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.bo.CustomizedCookie;
import com.xiangkai.community.model.dto.UserLoginInfo;
import com.xiangkai.community.model.entity.LoginTicket;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.dao.mapper.LoginTicketMapper;
import com.xiangkai.community.dao.mapper.UserMapper;
import com.xiangkai.community.util.CommunityUtil;
import com.xiangkai.community.util.CookieUtil;
import com.xiangkai.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService implements CommunityConstant {

    @Value("${com.community.domain}")
    private String domain;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @Autowired(required = false)
    private LoginTicketMapper loginTicketMapper;

    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    public User findUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    public User findUserByUsername(String username) {
        return userMapper.selectByUserName(username);
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

    public Map<String, String> login(UserLoginInfo userLoginInfo) {

        Map<String, String> map = new HashMap<>();
        // 校验用户信息
        if (StringUtils.isBlank(userLoginInfo.getUsername())) {
            map.put("usernameMsg", "请输入用户名！");
            return map;
        }

        if (StringUtils.isBlank(userLoginInfo.getPassword())) {
            map.put("passwordMsg", "请输入密码！");
            return map;
        }

        User user = userMapper.selectByUserName(userLoginInfo.getUsername());
        if (Objects.isNull(user)) {
            map.put("usernameMsg", "用户名不正确！");
            return map;
        }

        if (!user.getPassword().equals(CommunityUtil.generateMD5(userLoginInfo.getPassword() + user.getSalt()))) {
            map.put("passwordMsg", "密码不正确！");
            return map;
        }

        if (user.getStatus() == 0) {
            map.put("usernameMsg", "账号未激活，请先激活！");
            return map;
        }

        long currentTimeMillis = System.currentTimeMillis();
        long expiredInMillis = currentTimeMillis + userLoginInfo.getExpiredSeconds() * 1000;
        // 生成登录凭证
        String ticket = CommunityUtil.generateUUID();
        LoginTicket loginTicket = new LoginTicket()
                .setUserId(user.getId())
                .setTicket(ticket)
                .setStatus(0)
                .setFirstLoginTime(new Date())
                .setExpired(new Date(expiredInMillis))
                .build();

        loginTicketMapper.insertTicket(loginTicket);
        map.put("ticket", ticket);
        return map;
    }

    public Integer logout(HttpServletRequest request, HttpServletResponse response) {

        CustomizedCookie customizedCookie = CookieUtil.getCookieByName(request, "ticket");
        if (customizedCookie != null) {
            CookieUtil.invalidCookie(response, customizedCookie);
            String ticket = customizedCookie.getValue();
            invalidLoginTicketByTicket(ticket);
            return 0;
        }

        return -1;
    }

    public LoginTicket findByTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public void updateHeader(Integer userId, String headerUrl) {
        userMapper.updateHeaderUrl(userId, headerUrl);
    }

    public void updatePassword(Integer id, String newPassword) {
        userMapper.updatePassword(id, newPassword);
    }

    public void invalidCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        CookieUtil.invalidCookie(request, response, name);
    }

    public void invalidLoginTicketByUserId(Integer userId) {
        loginTicketMapper.updateStatus(userId, 1);
    }

    public void invalidLoginTicketByTicket(String ticket) {
        loginTicketMapper.updateStatusByTicket(ticket, 1);
    }

    public Map<String, String> getVerificationCode(HttpServletRequest request, String email) {
        Map<String, String> map = new HashMap<>();
        if (email == null) {
            map.put("emailMsg", "邮箱不存在，请输入正确的邮箱！");
            return map;
        }

        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "邮箱不存在，请重新输入！");
            return map;
        }
        String verificationCode = CommunityUtil.generateRandom(6, 10);

        // todo: 改为redis存储并设置有效时长为5分钟
        HttpSession session = request.getSession();
        if (session.getAttribute("verificationCode") != null) {
            map.put("verificationCodeMsg", "验证码已发送，请输入！");
            return map;
        }

        session.setAttribute("verificationCode", verificationCode);
        session.setMaxInactiveInterval(VERIFICATION_CODE_TIMEOUT);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("verificationCode", verificationCode);
        String html = templateEngine.process("/mail/forget", context);
        mailClient.send(email, "忘记密码", html);
        return map;
    }

    /**
     * 获取用户拥有的权限
     */
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(Integer userId) {

        User user = findUserById(userId);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 0:
                        return AUTHORITY_USER;
                    case 1:
                        return AUTHORITY_ADMIN;
                    default:
                        return AUTHORITY_MODERATOR;
                }
            }
        });
        return grantedAuthorities;
    }
}
