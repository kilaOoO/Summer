package com.bingsenh.Summer.network;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
public abstract class EndPoint {
    public abstract void start(int port);
    public abstract void close();
}
