package com.bingsenh.Summer.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
public class Worker implements Runnable {
    private SelectionKey sk;
    private ByteBuffer byteBuffer;
    public Worker(SelectionKey sk,ByteBuffer byteBuffer){
        this.sk = sk;
        this.byteBuffer = byteBuffer;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"do something");
        this.sk.interestOps(SelectionKey.OP_WRITE);
        this.sk.selector().wakeup();

    }
}
