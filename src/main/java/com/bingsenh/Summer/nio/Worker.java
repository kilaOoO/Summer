package com.bingsenh.Summer.nio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.connector.context.WebApplication;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Created by bingsenh on 2019/6/11.
 */
@Slf4j
public class Worker implements Runnable{
    private SelectionKey sk;
    private ByteBuffer byteBuffer;
    private Response response;
    private Request request;
    private Servlet servlet;
    private ServletContext servletContext;
    private SocketWrapper socketWrapper;
    public Worker(SocketWrapper socketWrapper, ByteBuffer byteBuffer){
        this.socketWrapper = socketWrapper;
        this.sk = socketWrapper.getSk();
        this.byteBuffer = byteBuffer;
        this.servletContext = WebApplication.getServletContext();
    }

    @Override
    public void run() {
        try {
            //构建request && response
            response = new Response();
            request = new Request(byteBuffer.array());
            request.setResponse(response);
            servlet = servletContext.mapServlet(request.getUrl());
            //执行业务逻辑
            service();
            socketWrapper.setResponse(response);
            this.sk.interestOps(SelectionKey.OP_WRITE);
            this.sk.selector().wakeup();


        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void service(){
        //执行业务逻辑
        log.info("执行业务...");
        servlet.service(request,response);
    }
}
