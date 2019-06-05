package com.bingsenh.Summer.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
@Slf4j
public class Accepter {
    private ServerSocketChannel ssc;
    private int cores = Runtime.getRuntime().availableProcessors();
    private Selector[] selectors = new Selector[cores];
    private TCPSubReactor[] r = new TCPSubReactor[cores];
    private Thread[] t = new Thread[cores];
    private int selIdx = 0;


    public Accepter(ServerSocketChannel ssc) throws IOException {
        this.ssc = ssc;
        for(int i=0;i<cores;i++){
            selectors[i] = Selector.open();
            r[i] = new TCPSubReactor(selectors[i],i);
            t[i] = new Thread(r[i]);
            t[i].start();
        }
    }

    public void dispatch() throws IOException {
        SocketChannel sc = ssc.accept();
        log.info(sc.socket().getRemoteSocketAddress().toString() + " is connected");
        if(sc!=null){
            sc.configureBlocking(false);
            r[selIdx].setRestart(true);
            selectors[selIdx].wakeup();
            SelectionKey sk = sc.register(selectors[selIdx],SelectionKey.OP_READ);
            selectors[selIdx].wakeup();
            r[selIdx].setRestart(false);
            sk.attach(new TCPHandler(sk,sc));
            log.info("finish");
            if(++selIdx == selectors.length){
                selIdx = 0;
            }
        }else {
            log.info("this a bad message");
        }
    }

}
