package com.xiangkai.community.alpha.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.*;

public class NioExample {

    private static final Logger logger = LoggerFactory.getLogger(NioExample.class);

    public static void main(String[] args){

        try(Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8888));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            SortedSet<Integer> selectionKeys = new TreeSet<Integer>();
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>(16);
            while (true){
                int select = selector.select();
                if(select == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        // 处理连接事件
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        // 处理可读事件
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        // 读取数据
                    }

                    // 移除处理过的 SelectionKey
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
