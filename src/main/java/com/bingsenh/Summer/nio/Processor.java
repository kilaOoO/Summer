package com.bingsenh.Summer.nio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.connector.context.WebApplication;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.servlet.Servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;

/**
 * @Author hbs
 * @Date 2019/6/4
 */
public class Processor {
    private SocketChannel socketChannel;
    private ServletContext servletContext;
    private Servlet servlet;
    private Request request;
    private Response response;

    public Processor(SocketChannel socketChannel){
        servletContext = WebApplication.getServletContext();
        this.socketChannel = socketChannel;
    }

    public void process() throws ServletException {
        servlet = servletContext.mapServlet(request.getUrl());
        servlet.service(request,response);
        flushResponse();
    }


    private void flushResponse(){
        byte[] bytes = response.getResponseBytes();
        OutputStream os = null;
        try {
            os = socketChannel.socket().getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
