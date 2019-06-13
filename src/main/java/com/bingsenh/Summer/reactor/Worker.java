package com.bingsenh.Summer.reactor;

import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.connector.context.WebApplication;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
public class Worker implements Runnable {
    private SelectionKey sk;
    private ByteBuffer byteBuffer;
    private ServletContext servletContext;
    public Worker(SelectionKey sk,ByteBuffer byteBuffer){
        this.servletContext = WebApplication.getServletContext();
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
