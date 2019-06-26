package com.bingsenh.Summer.connector.Response;

import com.bingsenh.Summer.cookie.Cookie;
import com.bingsenh.Summer.connector.enumeration.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class Response {
    private final static String BLANK = " ";
    private final static String CRLF = "\r\n";
    private final static Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    private StringBuilder headerAppender;
    private List<Cookie> cookies;
    private List<Header> headers;
    private HttpStatus status = HttpStatus.OK;
    private byte[] body = new byte[0];

    public Response(){
        this.headerAppender = new StringBuilder();
        this.cookies = new ArrayList<>();
        this.headers = new ArrayList<>();
    }

    public void setStatus(HttpStatus status){this.status =status;}
    public void setBody(byte[] body){this.body = body;}
    public void addCookie(Cookie cookie){cookies.add(cookie);}
    public void addHeader(Header header){headers.add(header);}

    /**
     * 构建状态行和响应头
     */

    private void buildHeader(){
        //状态行 HTTP/1.1 200 OK
        headerAppender.append("HTTP/1.1").append(BLANK).append(status.getCode()).append(BLANK).append(status).append(CRLF);
        //响应头
        headerAppender.append("Date:").append(BLANK).append(new Date()).append(CRLF);
        headerAppender.append("Content-Type:").append(BLANK).append("text/html;charset=utf-8").append(CRLF);
        if(headers!=null&&headers.size()>0){
            for(Header header:headers){
                headerAppender.append(header.getKey()).append(":").append(BLANK).append(header.getValue()).append(CRLF);
            }
        }
        if(cookies!=null&&cookies.size()>0){
            for(Cookie cookie:cookies){
                headerAppender.append("Set-cookie:").append(BLANK).append(cookie.getKey()).append("=").append(cookie.getValue()).append(CRLF);

            }
        }
        headerAppender.append("Content-Length:").append(BLANK).append(body.length).append(CRLF).append(CRLF);
    }

    public ByteBuffer getResponse(){
        buildHeader();
        byte[] header = this.headerAppender.toString().getBytes(UTF_8_CHARSET);
        byte[] response = new byte[header.length + body.length];
        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);
        return ByteBuffer.wrap(response);
    }

    public byte[] getResponseBytes(){
        buildHeader();
        log.info(this.headerAppender.toString());
        log.info(new String(body,UTF_8_CHARSET));
        byte[] header = this.headerAppender.toString().getBytes(UTF_8_CHARSET);
        byte[] response = new byte[header.length + body.length];
        System.arraycopy(header, 0, response, 0, header.length);
        System.arraycopy(body, 0, response, header.length, body.length);
        return response;
    }


    public void flushResponse(){

    }





}
