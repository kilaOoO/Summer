package com.bingsenh.Summer.network;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.exception.ServletException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author hbs
 * @Date 2019/6/2
 */
public class BioRequestHandler extends AbstractRequestHandler {
    public BioRequestHandler(SocketWrapper socketWrapper, ServletContext servletContext, Request request, Response response) throws ServletException {
        super(socketWrapper, servletContext, request, response);
    }

    @Override
    public void flushResponse() {
        isFinished = true;
        BioSocketWrapper bioSocketWrapper = (BioSocketWrapper) socketWrapper;
        byte[] bytes = response.getResponseBytes();
        OutputStream os = null;
        try {
            os = bioSocketWrapper.getSocket().getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            {
                try {
                    os.close();
                    bioSocketWrapper.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
