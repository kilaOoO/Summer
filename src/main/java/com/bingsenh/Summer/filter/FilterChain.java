package com.bingsenh.Summer.filter;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;

/**
 * Created by bingsenh on 2019/6/27.
 * 拦截器链
 */
public interface FilterChain {
    void doFliter(Request request, Response response);
}
