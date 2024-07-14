package com.xiangkai.community.controller;

import com.xiangkai.community.annotation.LoginRequired;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.entity.HostHolder;
import com.xiangkai.community.model.entity.User;
import com.xiangkai.community.service.UserService;
import com.xiangkai.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    @Value("${com.community.upload-path}")
    private String uploadPath;

    @Value("${com.community.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @LoginRequired
    @RequestMapping(path = "/getSettingPage", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/uploadHeader", method = RequestMethod.POST)
    public String upload(MultipartFile headerFile, Model model) {

        // 图片文件校验
        if (headerFile == null) {
            model.addAttribute("headerMsg", "上传的图片文件无效，请重新上传！");
            return "/site/setting";
        }

        String originalFilename = headerFile.getOriginalFilename();
        if (originalFilename == null) {
            model.addAttribute("headerMsg", "上传的文件名有误，请重新上传！");
            return "/site/setting";
        }

        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (StringUtils.isBlank(suffix) || !Pattern.matches(IMG_SUFFIX_PATTERN, suffix)) {
            model.addAttribute("headerMsg", "上传的文件后缀有误，请重新上传！");
            return "/site/setting";
        }

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 存储图片
        String savedFilename = CommunityUtil.generateUUID() + "." + suffix;

        String dest = uploadPath + "/" + savedFilename;

        try {
            headerFile.transferTo(new File(dest));
        } catch (IOException e) {
            LOGGER.error("保存头像出错！" + e);
        }

        // 更新用户信息
        User user = hostHolder.get();

        // http://ip:8888/community/user/header/filename
        String headerUrl = domain + "/user/header" + "/" + savedFilename;
        userService.updateHeader(user.getId(), headerUrl);

        model.addAttribute("msg", "头像上传成功！");
        model.addAttribute("target", "/index");
        return "/site/operate-result";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void header(@PathVariable("fileName") String fileName,
                       HttpServletResponse response) {

        String headerImgFilePath = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("jpg".equals(suffix)) {
            suffix = "jpeg";
        }

        File headerImgFile = new File(headerImgFilePath);
        if (!headerImgFile.exists()) {
            throw new IllegalArgumentException("参数错误：头像文件不存在！");
        }

        // 浏览器响应设置content-type，Cache-Control
        response.setContentType("image/" + suffix);
        response.setHeader("Cache-Control", "no-store, no-cache");

        try (ServletOutputStream os = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(headerImgFile)) {

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

        } catch (Exception e) {
            LOGGER.error("用户头像显示错误：" + e);
        }
    }

}
