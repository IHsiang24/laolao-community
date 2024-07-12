package com.xiangkai.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public Producer producer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // 设置图片宽度
        properties.setProperty("kaptcha.image.width", "100");

        // 设置图片高度
        properties.setProperty("kaptcha.image.height", "40");

        // 设置边框
        properties.setProperty("kaptcha.border", "yes");

        // 设置边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");

        // 设置字体尺寸
        properties.setProperty("kaptcha.textproducer.font.size", "30");

        // 设置字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");

        // 设置字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,黑体");

        // 设置字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        // 设置验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        //去除干扰线
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
