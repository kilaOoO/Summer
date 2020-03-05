package com.bingsenh.Summer.container.context;

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
