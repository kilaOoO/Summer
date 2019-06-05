package com.bingsenh.Summer.reactor;

import java.io.IOException;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
public class Main {
    public static void main(String[] args) {
        try {
            TCPReactor reactor = new TCPReactor(1234);
            reactor.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
