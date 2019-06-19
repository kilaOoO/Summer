package com.bingsenh.Summer.bio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
@Slf4j
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

    @Override
    public void run() {
        //service();
        service();

    }

    private  void service(){
        log.info("线程执行");
        servlet.service(request,response);
        if(!isFinished){
            log.info("写回客户端");
            flushResponse();
        }
    }

    public abstract void flushResponse();
}
