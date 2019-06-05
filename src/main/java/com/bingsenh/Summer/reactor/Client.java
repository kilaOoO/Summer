package com.bingsenh.Summer.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
public class Client {

    public static void main(String[] args) {
        SocketChannel socketChannel;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost",1234));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            buffer.put("hello hbs".getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
