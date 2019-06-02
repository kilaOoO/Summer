package com.bingsenh.Summer.connector.context.holder;

import com.bingsenh.Summer.servlet.Servlet;
import lombok.Data;

@Data
public class ServletHolder {
    private Servlet servlet;
    private String servletClass;

    public ServletHolder(String servletClass){
        this.servletClass = servletClass;
    }
}
