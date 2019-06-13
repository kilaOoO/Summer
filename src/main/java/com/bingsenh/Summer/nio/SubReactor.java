package com.bingsenh.Summer.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bingsenh on 2019/6/11.
 */
@Slf4j
public class SubReactor implements Runnable {
    private Selector selector;
    private boolean restart = false;

    public SubReactor(Selector selector){
        this.selector = selector;
    }

    @Override
    public void run() {
        log.info("the subReactor is running....");
        while (!Thread.interrupted()){
            if(!restart){
                try {
                    if(selector.select() <= 0){
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                Processor processor = (Processor) key.attachment();
                try {
                    processor.dispatch();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setRestart(boolean restart){
        this.restart = restart;
    }
}
