package com.bingsenh.Summer.bio;

import lombok.Data;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
@Data
public class BioSocketWrapper implements SocketWrapper {
    private Socket socket;
    public BioSocketWrapper(Socket socket){
        this.socket = socket;
    }
    @Override
    public void close() throws IOException {

    }
}
