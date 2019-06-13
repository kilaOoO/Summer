package com.bingsenh.Summer.nio;

import com.bingsenh.Summer.reactor.Worker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by bingsenh on 2019/6/11.
 */
public class Processor {
    private SelectionKey sk;
    private SocketChannel sc;
    private static final int THREAD_NUM = 10;
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_NUM,THREAD_NUM,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>()
    );

    public Processor(SelectionKey sk){
            this.sk = sk;
            this.sc = (SocketChannel) sk.channel();
            pool.setCorePoolSize(32);
    }

    public void dispatch() throws IOException {
        if(sk.isReadable()){
            readHandle();
        }else if(sk.isWritable()){
            writeHandle();
        }else {
            System.out.println("other");
        }
    }

    private void readHandle() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = sc.read(buffer);
        if(read<=0){
            closeChannel();
            System.out.println("read is null...");
        }else {
            pool.execute(new Worker(sk,buffer));
        }
    }

    private void writeHandle(){

    }

    private void closeChannel(){
        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sk.cancel();
    }
}
