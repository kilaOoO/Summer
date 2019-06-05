package com.bingsenh.Summer.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author hbs
 * @Date 2019/6/5
 */
@Slf4j
public class TCPHandler {
    private SelectionKey sk;
    private SocketChannel sc;
    private static final int THREAD_COUNTING = 10;
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNTING, THREAD_COUNTING, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    public TCPHandler(SelectionKey sk,SocketChannel sc){
        this.sk = sk;
        this.sc =sc;
        pool.setMaximumPoolSize(32);
    }

    public void dispatch() throws IOException {
        log.info("handle the request");
        if(sk.isReadable()){
            readHandle();
        }else if(sk.isWritable()){
            writeHandle();
        }
    }

    private void readHandle() throws IOException {
        byte[] arr = new byte[1024];
        ByteBuffer buf = ByteBuffer.wrap(arr);
        int numBytes = sc.read(buf);
        if(numBytes == -1){
            closeChannel();
        }else {
            String str = new String(arr);
            System.out.println(str);
            pool.execute(new Worker(sk,buf));
        }
    }

    private void writeHandle() throws IOException {
        String str = "hello client";
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes());
        while(buf.hasRemaining()){
            sc.write(buf);
        }

        //长连接需要更改为read状态
        //sk.interestOps(SelectionKey.OP_WRITE);
        //sk.selector().wakeup();
    }


    public void closeChannel(){
        sk.cancel();
        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
