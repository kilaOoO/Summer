package com.bingsenh.Summer.connector.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
@Slf4j
public class BioEndPoint extends EndPoint {
    private ServerSocket serverSocket;
    private BioAcceptor acceptor;
    private BioDispatcher dispatcher;
    private volatile boolean isRunning = true;

    public Socket accept() throws IOException {
        return serverSocket.accept();
    }

    public boolean isRunning(){return isRunning;}

    private void initAcceptor(){
        acceptor = new BioAcceptor(this,dispatcher);
        Thread t = new Thread(acceptor,"bio-acceptor");
        t.setDaemon(true);
        t.start();

    }

    @Override
    public void start(int port) {
        try {
            dispatcher = new BioDispatcher();
            serverSocket = new ServerSocket(port);
            initAcceptor();
            log.info("服务器启动成功");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("服务器启动失败");
            close();
        }
    }

    @Override
    public void close() {
        isRunning = false;
        dispatcher.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
