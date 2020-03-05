package com.bingsenh.Summer.connector.bio;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;
import com.bingsenh.Summer.container.context.ServletContext;
import com.bingsenh.Summer.exception.ServletException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author hbs
 * @Date 2019/6/2
 */
@Slf4j
public class BioRequestHandler extends AbstractRequestHandler {
    public BioRequestHandler(SocketWrapper socketWrapper, ServletContext servletContext, Request request, Response response) throws ServletException {
        super(socketWrapper, servletContext, request, response);
        log.info("BioRequestHandler初始化成功");
    }

    @Override
    public void flushResponse() {
        isFinished = true;
        BioSocketWrapper bioSocketWrapper = (BioSocketWrapper) socketWrapper;
        byte[] bytes = response.getResponseBytes();
        log.info(""+bytes.length);
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
