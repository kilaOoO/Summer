package com.bingsenh.Summer.servlet;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;

public interface Servlet {
    void init();
    void destory();
    void service(Request request, Response response);
}
