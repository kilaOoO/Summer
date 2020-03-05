package com.bingsenh.Summer.connector.nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractNioSelector implements Runnable {
    /**
     * 线程池
     */
    private final Executor executor;

    /**
     * 选择器
     */
    protected Selector selector;

    /**
     * 选择器 wakenUp 状态标记
     */
    protected final AtomicBoolean wakenUp = new AtomicBoolean();

    /**
     * 任务队列
     */
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedDeque<Runnable>();

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 线程管理对象
     */
    protected NioSelectorRunnablePool nioSelectorRunnablePool;

    public AbstractNioSelector(Executor executor, String threadName, NioSelectorRunnablePool nioSelectorRunnablePool){
        this.executor = executor;
        this.threadName = threadName;
        this.nioSelectorRunnablePool = nioSelectorRunnablePool;
        openSelector();
    }

    private void openSelector(){
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a selector.");
        }
        executor.execute(this);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);
        while(true) {
            try {
                wakenUp.set(false);
                select(selector);
                processTaskQueue();
                process(selector);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册一个任务并激活 selector
     */
    protected final void registerTask(Runnable task){
        taskQueue.add(task);
        Selector selector = this.selector;
        if(selector!=null){
            if(wakenUp.compareAndSet(false,true)){
                selector.wakeup();
            }
        }else {
            taskQueue.remove(task);
        }
    }

    /**
     * 执行队列里的任务
     */
    private void processTaskQueue(){
        for(;;){
            final Runnable task = taskQueue.poll();
            if(task == null){
                break;
            }
            task.run();
        }
    }

    public NioSelectorRunnablePool getNioSelectorRunnablePool(){
        return nioSelectorRunnablePool;
    }

    protected  abstract int select(Selector selector) throws IOException;
    protected abstract void process(Selector selector) throws IOException;
}
