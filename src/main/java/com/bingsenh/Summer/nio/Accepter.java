package com.bingsenh.Summer.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by bingsenh on 2019/6/11.
 */
@Slf4j
public class Accepter {

    private ServerSocketChannel ssc;
    private int cores = Runtime.getRuntime().availableProcessors();
    private Selector[] selectors = new Selector[cores];
    private SubReactor[] r = new SubReactor[cores];
    private Thread[] t = new Thread[cores];
    private int coreId = 0;

    public Accepter(ServerSocketChannel ssc) throws IOException {
        this.ssc = ssc;
        for(int i = 0;i<cores;i++){
            selectors[i] = Selector.open();
            r[i] = new SubReactor(selectors[i]);
            t[i] = new Thread(r[i]);
            t[i].start();
        }
    }

    public void dispatch() throws IOException {
        SocketChannel sc = ssc.accept();
        log.info(sc.socket().getRemoteSocketAddress().toString() + "is connected...");
        if(sc!=null){
            sc.configureBlocking(false);
            r[coreId].setRestart(true);
            selectors[coreId].wakeup();
            SelectionKey sk = sc.register(selectors[coreId],SelectionKey.OP_READ);
            sk.attach(new Processor(sk));
            selectors[coreId].wakeup();
            r[coreId].setRestart(false);
            if(++coreId == selectors.length){
                coreId = 0;
            }
        }else {
            log.info("this is a bad socketChannel...");
        }
    }






}
