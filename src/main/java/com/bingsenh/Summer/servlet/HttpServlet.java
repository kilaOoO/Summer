package com.bingsenh.Summer.servlet;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.enumeration.RequestMethod;

public abstract class HttpServlet implements Servlet{
    @Override
    public void init() {

    }

    @Override
    public void destory() {

    }

    @Override
    public void service(Request request, Response response) {
        if(request.getMethod() == RequestMethod.GET){
            doGet(request,response);
        }else if(request.getMethod()== RequestMethod.POST){
            doPost(request,response);
        }else if(request.getMethod()==RequestMethod.PUT){
            doPut(request,response);
        }else if(request.getMethod()==RequestMethod.DELETE){
            doDelete(request,response);
        }
    }

    public void doGet(Request request,Response response){

    }

    public void doPost(Request request,Response response){

    }

    public void doPut(Request request,Response response){

    }

    public void doDelete(Request request,Response response){

    }



}
