package com.bingsenh.Summer.connector.context;

import lombok.Getter;

/**
 * @author hbs
 * @date 2019/5/31
 */

/**
 * 静态初始化servletContext
 */
public class WebApplication {
    private static ServletContext servletContext;
    static {
        servletContext = new ServletContext();
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }
}
