package com.bingsenh.Summer.connector.nio;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public class NioServerBoss extends AbstractNioSelector implements Boss {
    public NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool nioSelectorRunnablePool){
        super(executor,threadName, nioSelectorRunnablePool);
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select();
    }

    @Override
    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if(selectionKeys.isEmpty()){
            return;
        }

        for(Iterator<SelectionKey> i = selectionKeys.iterator();i.hasNext();){
            SelectionKey key = i.next();
            i.remove();
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            // 新客户端
            SocketChannel channel = server.accept();
            // 设置非阻塞
            channel.configureBlocking(false);
            // 获取一个 worker
            Worker nextworker = getNioSelectorRunnablePool().nextWorker();
            // 注册新客户端接入任务
            nextworker.registerNewChannelTask(channel);
            System.out.println("new client connected ...");
        }
    }

    @Override
    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel) {
        final Selector selector = this.selector;
        registerTask(new Runnable() {
            @Override
            public void run() {
                // 注册 serverChannel 到 selector
                try {
                    serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
