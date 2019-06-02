package com.bingsenh.Summer.network;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.servlet.Servlet;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
public abstract class AbstractRequestHandler implements Runnable{
    protected Request request;
    protected Response response;
    protected SocketWrapper socketWrapper;
    protected ServletContext servletContext;
    protected Servlet servlet;
    protected boolean isFinished;

    public AbstractRequestHandler(SocketWrapper socketWrapper,ServletContext servletContext,Request request,Response response) throws ServletException {
        this.socketWrapper = socketWrapper;
        this.servletContext =servletContext;
        this.isFinished = false;
        this.request = request;
        this.response = response;
        request.setServletContext(servletContext);
        servlet = servletContext.mapServlet(request.getUrl());

    }

    public void run(){
        service();
    }


    private  void service(){
        servlet.service(request,response);
        if(!isFinished){
            flushResponse();
        }
    }

    public abstract void flushResponse();
}
