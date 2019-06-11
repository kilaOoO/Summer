package com.bingsenh.Summer.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
public class Client {
    private static SocketChannel socketChannel;

    public static void main(String[] args) {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String str = scanner.next();
                System.out.println("输入的数据为：" + str);
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.clear();
                    buffer.put(str.getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
