package com.xiangkai.community;

import com.xiangkai.community.util.LocalCommandExecutor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocalCommandExecutorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCommandExecutorTests.class);

    @Value("${wkhtmltopdf.image.command}")
    private String imageCommand;

    @Value("${wkhtmltopdf.image.save-path}")
    private String imageSavePath;

    @Test
    public void test() {
        LocalCommandExecutor.CommandExecuteResult result = LocalCommandExecutor.execute(imageCommand + "https://linux.cn/article-8362-1.html" + imageSavePath + "2.png",
                new LocalCommandExecutor.CommandCallback<LocalCommandExecutor.CommandExecuteResult>() {
                    @Override
                    public LocalCommandExecutor.CommandExecuteResult doWithProcessor(LocalCommandExecutor.Processor processor) throws Exception {
                        return processor.result();
                    }
                });
        if (result != null) {
            LOGGER.info("input:" + result.getInput() + "\n");
            LOGGER.info("error:" + result.getError());
        }
    }
}
