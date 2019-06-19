package com.bingsenh.Summer.bio;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.exception.ServletException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @Author hbs
 * @Date 2019/6/1
 */
@Slf4j
public class BioDispatcher extends AbstractDispatcher{

    @Override
    public void doDispatch(SocketWrapper socketWrapper) {
        BioSocketWrapper bioSocketWrapper = (BioSocketWrapper) socketWrapper;
        Socket socket = bioSocketWrapper.getSocket();
        Request request = null;
        Response response = null;
        try {
            BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
            byte[] buf = null;
            buf = new byte[bin.available()];
            int len = bin.read(buf);
            log.info("长度："+String.valueOf(len));
            response = new Response();
            request = new Request(buf);
            pool.execute(new BioRequestHandler(socketWrapper,servletContext,request,response));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
}
