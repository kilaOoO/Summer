package com.bingsenh.Summer.nio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import lombok.Data;

import java.nio.channels.SelectionKey;

/**
 * @Author hbs
 * @Date 2019/6/12
 */
@Data
public class SocketWrapper {
    private SelectionKey sk;
    private Response response;
    public SocketWrapper(SelectionKey sk){
        this.sk = sk;
    }
}
