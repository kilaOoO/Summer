package com.bingsenh.Summer.connector.bio;

import com.bingsenh.Summer.container.context.ServletContext;
import com.bingsenh.Summer.container.context.WebApplication;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
public abstract class AbstractDispatcher {
    protected ThreadPoolExecutor pool;
    protected ServletContext servletContext;

    public AbstractDispatcher(){
        this.servletContext = WebApplication.getServletContext();
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"Work Pool-"+count++);
            }
        };
        this.pool = new ThreadPoolExecutor(100,100,1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(200),threadFactory,new ThreadPoolExecutor.CallerRunsPolicy());
    }
    public void shutdown(){
        pool.shutdown();
        servletContext.destory();
    }

    public abstract void doDispatch(SocketWrapper socketWrapper) throws IOException;
}
