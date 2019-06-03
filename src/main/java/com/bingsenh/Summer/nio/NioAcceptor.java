package com.bingsenh.Summer.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author hbs
 * @Date 2019/6/3
 */
@Slf4j
public class NioAcceptor implements Runnable {
    private NioServer server;
    private Selector selector;
    private int coreNum;
    private NioPoller[] pollers;

    public NioAcceptor(NioServer server) throws IOException {
        this.server = server;
        coreNum = Runtime.getRuntime().availableProcessors();
        pollers = new NioPoller[coreNum];
        for(int i =0;i<pollers.length;i++){
            pollers[i] = new NioPoller();
        }
    }
    @Override
    public void run() {
        int index = 0;
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(1234));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(server.isRunning()){
            try {
                if(selector.selectNow()<0){
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                for(SelectionKey key:keys){
                    keys.remove(key);
                    if(key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        log.info("Accept request from {}",socketChannel.getRemoteAddress());
                        NioPoller poller = pollers[(int)(index++) % coreNum];
                        //readKey.attach()
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
