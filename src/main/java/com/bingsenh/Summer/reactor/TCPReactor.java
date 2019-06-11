package com.bingsenh.Summer.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
@Slf4j
public class TCPReactor extends Thread {

    private ServerSocketChannel ssc;
    private Selector selector;

    public TCPReactor(int port) throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        SelectionKey sk = ssc.register(selector,SelectionKey.OP_ACCEPT);
        sk.attach(new Accepter(ssc));
    }
    @Override
    public void run() {
        while(!Thread.interrupted()){
            log.info("mainReactor waiting for new connection....");
            try {
                if(selector.select() <= 0){
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                Accepter accepter = (Accepter) key.attachment();
                try {
                    accepter.dispatch();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iterator.remove();
            }
        }
    }
}
