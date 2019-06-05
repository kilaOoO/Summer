package com.bingsenh.Summer.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author hbs
 * @Date 2019/6/3
 */
@Slf4j
public class NioPoller {
    private static final ExecutorService service =
            Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    private Selector selector;

    private int pollerNo;


    public NioPoller(int pollerno) throws IOException {
        this.pollerNo = pollerno;
        this.selector = SelectorProvider.provider().openSelector();
        start();
    }


    public int getPollerno(){
        return pollerNo;
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    public void wakeup(){
        this.selector.wakeup();
    }


    public void start(){
        service.submit(()->{
            while(true) {
                try {
                    if (selector.select() < 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                for(SelectionKey key:keys){
                    keys.remove(key);
                    if(key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                    }else if(key.isWritable()){

                    }
                }
            }
        });
    }


}
