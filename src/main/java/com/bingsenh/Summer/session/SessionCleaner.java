package com.bingsenh.Summer.session;

import com.bingsenh.Summer.container.context.WebApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by bingsenh on 2019/6/27.
 */
@Slf4j
public class SessionCleaner implements Runnable{

    private ScheduledExecutorService executor;
    public SessionCleaner(){
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"SessionCleaner");
            }
        };
        this.executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }

    public void start(){
        executor.scheduleAtFixedRate(this,5,10, TimeUnit.SECONDS);
    }



    @Override
    public void run() {
        //log.info("开始扫描过期的 session...");
        WebApplication.getServletContext().cleanSessions();
        //log.info("扫描结束...");
    }
}
