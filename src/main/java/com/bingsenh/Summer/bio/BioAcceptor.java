package com.bingsenh.Summer.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
@Slf4j
public class BioAcceptor implements Runnable{
    private BioEndPoint server;
    private BioDispatcher dispatcher;

    public BioAcceptor(BioEndPoint server,BioDispatcher dispatcher){
        this.server =server;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        log.info("开始监听请求");
        while(server.isRunning()){
            Socket client;
            try {
                client = server.accept();
                System.out.println("一次请求");
                dispatcher.doDispatch(new BioSocketWrapper(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
