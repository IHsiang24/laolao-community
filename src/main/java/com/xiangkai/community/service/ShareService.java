package com.xiangkai.community.service;

import com.xiangkai.community.util.CommunityUtil;
import com.xiangkai.community.util.LocalCommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Service
public class ShareService {

    private static final String BLANK = " ";

    private static final ThreadPoolExecutor threadPool =
            new ThreadPoolExecutor(
                    3,
                    5,
                    300,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<>(),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());

    private static final Logger LOGGER = LoggerFactory.getLogger(ShareService.class);

    @Value("${wkhtmltopdf.image.command}")
    private String imageCommand;

    @Value("${wkhtmltopdf.image.save-path}")
    private String imageSavePath;

    @Value("${com.community.domain}")
    private String domain;

    public String share(String mode, String pageUrl) {
        if ("image".equals(mode)) {
            return generateImage(pageUrl);
        }
        return null;
    }

    public void getShareContent(String filename, HttpServletResponse response) {
        String suffix = ".png";
        String filePath = imageSavePath + "/" + filename + suffix;

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("参数错误：分享文件不存在！");
        }

        // 浏览器响应设置content-type，Cache-Control
        response.setContentType("image/" + suffix.substring(suffix.indexOf(".") + 1));
        response.setHeader("Cache-Control", "no-store, no-cache");

        try (ServletOutputStream os = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

        } catch (Exception e) {
            LOGGER.error("分享文件显示错误：" + e);
        }
    }

    private String generateImage(String pageUrl) {
        String fileName = CommunityUtil.generateUUID();
        String suffix = ".png";
        String filePath = imageSavePath + "/" + fileName + suffix;
        String command = imageCommand + BLANK + pageUrl + BLANK + filePath;
        CompletableFuture<LocalCommandExecutor.CommandExecuteResult> future = CompletableFuture.supplyAsync(
                new Supplier<LocalCommandExecutor.CommandExecuteResult>() {
                    @Override
                    public LocalCommandExecutor.CommandExecuteResult get() {
                        return LocalCommandExecutor.execute(command,
                                new LocalCommandExecutor.CommandCallback<LocalCommandExecutor.CommandExecuteResult>() {
                                    @Override
                                    public LocalCommandExecutor.CommandExecuteResult doWithProcessor(
                                            LocalCommandExecutor.Processor processor) throws Exception {

                                        return processor.result();
                                    }
                                });
                    }
                }, threadPool);

        LocalCommandExecutor.CommandExecuteResult result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("CompletableFuture 异常：\n" + e);
        }

        if (result != null) {
            LOGGER.info("command exec input:\n" + result.getInput());
            LOGGER.info("command exec error:\n" + result.getError());
        }
        return domain + "/share/" + fileName;
    }

    private void generatePdf(String pageUrl) {
        // TODO 实现方式同generateImage
    }
}
