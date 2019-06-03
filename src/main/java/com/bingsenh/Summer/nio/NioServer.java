package com.bingsenh.Summer.nio;

/**
 * @Author hbs
 * @Date 2019/6/3
 */
public class NioServer {
    private NioAcceptor acceptor;
    private volatile boolean isRunning = true;


    public void start(){
        Thread t = new Thread(acceptor,"nio-acceptor");
        t.start();
    }

    public void close(){
        isRunning = false;
    }

    public boolean isRunning(){return isRunning;}

}
