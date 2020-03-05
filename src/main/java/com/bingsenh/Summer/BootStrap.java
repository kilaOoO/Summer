package com.bingsenh.Summer;


import com.bingsenh.Summer.connector.nio.NioSelectorRunnablePool;
import com.bingsenh.Summer.connector.nio.ServerBootstrap;


import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 服务器启动入口
 */
public class BootStrap
{
    public static void main( String[] args )
    {
        //初始化boss 与 worker线程池，并开启selector
        NioSelectorRunnablePool nioSelectorRunnablePool = new NioSelectorRunnablePool(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

        //获取服务类
        ServerBootstrap bootstrap = new ServerBootstrap(nioSelectorRunnablePool);

        //绑定端口
        bootstrap.bind(new InetSocketAddress(10101));

        System.out.println("webServer start......");

    }
}


