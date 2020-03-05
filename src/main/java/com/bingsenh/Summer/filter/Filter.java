package com.bingsenh.Summer.filter;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;

/**
 * Created by bingsenh on 2019/6/27.
 * 过滤器
 */
public interface Filter {
    /**
     * 过滤器初始化
     */
    void init();

    /**
     * 过滤
     * @param request
     * @param response
     * @param filterChain
     */
    void doFilter(Request request, Response response,FilterChain filterChain);

    /**
     * 过滤器销毁
     */
    void destory();
}
