package com.bingsenh.Summer.connector.nio;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;
import com.bingsenh.Summer.container.context.ServletContext;
import com.bingsenh.Summer.container.context.WebApplication;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.servlet.Servlet;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Processor implements Runnable {
    private Response response;
    private Request request;
    private Servlet servlet;
    private ServletContext servletContext;
    private ByteBuffer byteBuffer;
    private SocketChannel socketChannel;
    private SelectionKey selectionKey;

    public  Processor(ByteBuffer byteBuffer,SelectionKey selectionKey){
        servletContext = WebApplication.getServletContext();
        this.selectionKey = selectionKey;
        this.socketChannel = (SocketChannel) selectionKey.channel();
        this.byteBuffer = byteBuffer;
    }
    @Override
    public void run() {
        // 构建request && response
        try {
            response = new Response();
            request = new Request(byteBuffer.array());
            request.setResponse(response);
            servlet = servletContext.mapServlet(request.getUrl());
        } catch (ServletException e) {
            e.printStackTrace();
        }

        //执行业务逻辑
        service();

        // 更改监听状态为写
        selectionKey.attach(response.getResponse());
        this.selectionKey.interestOps(SelectionKey.OP_WRITE);
        this.selectionKey.selector().wakeup();


    }

    private void service(){
        System.out.println("执行业务...");
        servlet.service(request,response);
    }
}
