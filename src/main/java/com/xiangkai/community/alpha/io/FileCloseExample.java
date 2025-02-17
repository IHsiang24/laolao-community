package com.xiangkai.community.alpha.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class FileCloseExample {

    private static final Logger logger = LoggerFactory.getLogger(FileCloseExample.class);

    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("D:\\Data\\OpenResourceProjects\\nowcoder-community\\README.md");
            // 进行文件读取操作
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    // 调用 close() 方法关闭文件流
                    fis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
