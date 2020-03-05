package com.bingsenh.Summer.connector.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NioServerWorker extends AbstractNioSelector implements Worker {
    private static final int THREAD_NUM = 10;
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_NUM,THREAD_NUM,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>()
    );
    public NioServerWorker(Executor executor, String threadName, NioSelectorRunnablePool nioSelectorRunnablePool){
        super(executor,threadName, nioSelectorRunnablePool);

    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select();
    }

    @Override
    protected void process(Selector selector) throws IOException {
        //System.out.println(Thread.currentThread().getName());
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if(selectionKeys.isEmpty()){
            return;
        }
        Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
        while(ite.hasNext()){
            SelectionKey selectionKey = ite.next();
            ite.remove();
            if(selectionKey.isReadable()){
                readHander(selectionKey);
            }else if (selectionKey.isWritable()){
                writeHander(selectionKey);
            }else {
                System.out.println("other");
            }
        }
    }



    @Override
    public void registerNewChannelTask(SocketChannel socketChannel) {
        final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    socketChannel.register(selector,SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void readHander(SelectionKey selectionKey){
        // 数据总长度
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        int ret = 0;
        boolean failure = true;
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取数据
        try {
            ret = socketChannel.read(buffer);
            failure = false;
        }catch (Exception e){

        }

        // 判断是否连接已断开
        if(ret <= 0 || failure){
            selectionKey.cancel();
            System.out.println("客户端断开连接");
        }else {
            pool.execute(new Processor(buffer,selectionKey));
        }

    }

    private  void writeHander(SelectionKey selectionKey){
        ByteBuffer response = (ByteBuffer) selectionKey.attachment();
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        // 将消息回送给客户端
        try {
            socketChannel.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
        selectionKey.selector().wakeup();
    }
}
