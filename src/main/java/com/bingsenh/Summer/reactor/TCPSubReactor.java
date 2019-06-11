package com.bingsenh.Summer.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
@Slf4j
public class TCPSubReactor implements Runnable {

    private Selector selector;
    private boolean restart = false;
    private int num;

    public TCPSubReactor(Selector selector,int num){
            this.selector = selector;
            this.num = num;


    }
    @Override
    public void run() {
        log.info("the subReactor is running");
        while (!Thread.interrupted()) {
            while (!Thread.interrupted() && !restart) {
                try {
                    if (selector.select() <= 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    TCPHandler handler = (TCPHandler) key.attachment();
                    try {
                        handler.dispatch();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void setRestart(boolean restart){
        this.restart =restart;
    }
}
