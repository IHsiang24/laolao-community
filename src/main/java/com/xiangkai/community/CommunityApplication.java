package com.xiangkai.community;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan(value = "com.xiangkai.community.dao.mapper")
public class CommunityApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityApplication.class);

    @PostConstruct
    private void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CommunityApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String property = env.getProperty("server.servlet.context-path");
            String path = property == null ? "" :  property;
            LOGGER.info(
                    "\n\t----------------------------------------------------------\n\t" +
                    "Application Sailrui-Boot is running! Access URLs:\n\tLocal: \t\thttp://localhost:{}{}/\n\tExternal: \thttp://{}:{}{}/\n\t" +
                    "----------------------------------------------------------",
                    port, path, ip, port, path);
        } catch (UnknownHostException e) {
            LOGGER.error("获取本机IP失败:", e);
        }
    }

}
