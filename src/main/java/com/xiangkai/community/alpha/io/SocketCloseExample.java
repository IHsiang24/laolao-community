package com.xiangkai.community.alpha.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class SocketCloseExample {

    private static final Logger logger = LoggerFactory.getLogger(SocketCloseExample.class);

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("www.baidu.com", 80);
            // 进行网络通信
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    // 调用 close() 方法关闭套接字
                    socket.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
