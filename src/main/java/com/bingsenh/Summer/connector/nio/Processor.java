package com.bingsenh.Summer.connector.nio;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;
import com.bingsenh.Summer.container.context.ServletContext;
import com.bingsenh.Summer.container.context.WebApplication;
import com.bingsenh.Summer.exception.NotFoundException;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.filter.Filter;
import com.bingsenh.Summer.filter.FilterChain;
import com.bingsenh.Summer.resource.ResourceHandler;
import com.bingsenh.Summer.servlet.Servlet;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Processor implements Runnable , FilterChain {
    private Response response;
    private Request request;
    private Servlet servlet;
    private ServletContext servletContext;
    private ByteBuffer byteBuffer;
    private SocketChannel socketChannel;
    private SelectionKey selectionKey;
    private List<Filter> filters;
    private int filterIndex = 0;

    public  Processor(ByteBuffer byteBuffer,SelectionKey selectionKey) {
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
        } catch (ServletException e) {
            e.printStackTrace();
        }

        if(request.getUrl().contains(".")){
            System.out.println("静态文件。。。。。");
            ResourceHandler.handle(request,response);
        }else {
            try {
                servlet = servletContext.mapServlet(request.getUrl());
                filters = servletContext.mapFilter(request.getUrl());
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            //执行业务逻辑
            if (filters.isEmpty()) {
                service();
            } else {
                doFliter(request, response);
            }
        }

        // 更改监听状态为写
        selectionKey.attach(response.getResponse());
        this.selectionKey.interestOps(SelectionKey.OP_WRITE);
        this.selectionKey.selector().wakeup();


    }

    private void service(){
        System.out.println("执行业务...");
        servlet.service(request,response);
    }

    @Override
    public void doFliter(Request request, Response response) {
        if(filterIndex < filters.size()){
            filters.get(filterIndex++).doFilter(request,response,this);
        }else{
            service();
        }
    }
}
