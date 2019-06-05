package com.bingsenh.Summer.nio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.exception.ServletException;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * @Author hbs
 * @Date 2019/6/4
 */
@Slf4j
public class NioWorker {
    private ExecutorService readThreadPool;
    private ExecutorService writeThreadPool;
    private SocketChannel socketChannel;




    public NioWorker(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
        initReader();
        initWriter();
    }


    public void executeRead(){
        readThreadPool.execute(new Reader());
    }

    public void executeWrite(){
        writeThreadPool.execute(new Writer());
    }



    private void initReader(){
        ThreadFactory readThreadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"ReadWorker Pool-" + count++);
            }
        };

        readThreadPool = new ThreadPoolExecutor(200,
                200,
                1,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                readThreadFactory);
    }

    private void initWriter(){
        ThreadFactory writeThreadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"WriteWorker Pool-" + count++);
            }
        };

        writeThreadPool = new ThreadPoolExecutor(200,
                200,
                1,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                writeThreadFactory);
    }


    public class Reader implements Runnable{

        @Override
        public void run() {
            Processor processor = new Processor(socketChannel);
            try {
                processor.process();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }


    public class Writer implements Runnable{

        @Override
        public void run() {

        }
    }



}
