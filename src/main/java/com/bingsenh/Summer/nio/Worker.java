package com.bingsenh.Summer.nio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.connector.context.WebApplication;
import com.bingsenh.Summer.exception.NotFoundException;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.filter.Filter;
import com.bingsenh.Summer.filter.FilterChain;
import com.bingsenh.Summer.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.List;

/**
 * Created by bingsenh on 2019/6/11.
 */
@Slf4j
public class Worker implements Runnable, FilterChain {
    private SelectionKey sk;
    private ByteBuffer byteBuffer;
    private Response response;
    private Request request;
    private Servlet servlet;
    private ServletContext servletContext;
    private SocketWrapper socketWrapper;
    private List<Filter> filters;
    private int filterIndex = 0;
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
            filters = servletContext.mapFilter(request.getUrl());

            //执行业务逻辑
            if(filters.isEmpty()){
                service();
            }else {
                doFliter(request,response);
            }

            socketWrapper.setResponse(response);
            this.sk.interestOps(SelectionKey.OP_WRITE);
            this.sk.selector().wakeup();


        } catch (ServletException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }


    private void service(){
        log.info("执行业务...");
        servlet.service(request,response);
    }

    @Override
    public void doFliter(Request request, Response response) {
        if(filterIndex<filters.size()){
            filters.get(filterIndex++).doFilter(request,response,this);
        }else {
            service();
        }
    }
}
