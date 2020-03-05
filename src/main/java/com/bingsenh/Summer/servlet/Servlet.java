package com.bingsenh.Summer.servlet;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;

public interface Servlet {
    void init();
    void destory();
    void service(Request request, Response response);
}
