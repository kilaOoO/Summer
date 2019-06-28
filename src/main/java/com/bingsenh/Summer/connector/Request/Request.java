package com.bingsenh.Summer.connector.Request;

import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.cookie.Cookie;
import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.connector.enumeration.RequestMethod;
import com.bingsenh.Summer.exception.ServletException;;
import com.bingsenh.Summer.session.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
@Data
@Slf4j
public class Request {
    private String url;
    private RequestMethod method;
    private Map<String,String> params;
    private Map<String,String> headers;
    private Map<String,Object> attributes;
    private ServletContext servletContext;
    private Cookie[] cookies;
    private HttpSession session;
    private Response response;

    public Request(byte[] data) throws ServletException {
        this.attributes = new HashMap<>();
        this.params = new HashMap<>();
        this.headers = new HashMap<>();

        //获取 http 信息并解析
        String[] messages = null;
        try {
            messages = URLDecoder.decode(new String(data, Charset.forName("UTF-8")),"UTF-8").split("\r\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(messages ==null||messages.length<=1){
            return;
        }

        parseHeaders(messages);
        if(headers.containsKey("Content-Length")&&!headers.get("Content-Length").equals("0")) {
            parseBody(messages[messages.length - 1]);
        }
    }


    //解析请求行和请求头
    private void parseHeaders(String[] messages){
        log.info("解析请求行和请求头");
        //解析方法
        String requestLine = messages[0];
        String[] requestLineSlices = requestLine.split(" ");
        this.method =  RequestMethod.valueOf(requestLineSlices[0]);
        log.info("请求方法:"+this.method);

        //解析URL和URL参数
        String url =requestLineSlices[1];
        String[] urlSlices = url.split("\\?");
        if(urlSlices.length>1){
            this.url = urlSlices[0];
            parseParams(urlSlices[1]);
        }else {
            this.url = url;
        }
        log.info("请求url:"+this.url);

        //解析请求头
        String header;
        for(int i =1;i<messages.length;i++){
            header = messages[i];
            if(header.equals("")){
                break;
            }
            String[] kv = header.split(": ");
            if(kv.length>1) {
                String key = kv[0];
                String value = kv[1];
                headers.put(key,value);
            }
        }

        //解析cookie
        if(headers.containsKey("cookie")){
            String[] rawCookies = headers.get("Cookies").split("; ");
            this.cookies = new Cookie[rawCookies.length];
            for(int i=0;i<rawCookies.length;i++){
                String[] kv = rawCookies[i].split("=");
                this.cookies[i] = new Cookie(kv[0],kv[1]);

            }
            headers.remove("cookie");
        }else {
            this.cookies = new Cookie[0];
        }
    }


    //解析请求体
    private void parseBody(String body){
        byte[] bytes = body.getBytes(Charset.forName("UTF-8"));
        String contentlength = headers.get("Content-length");
        if(contentlength!=null){
            int length = Integer.parseInt(contentlength);
            parseParams(new String(bytes,0,Math.min(length,bytes.length),Charset.forName("UTF-8")).trim());
        }else {
            parseParams(body.trim());
        }
    }

    /**
     * 从cookie 中获取 JSESSIONID,根据 JSESSIONID 获取相应 session
     * cookie 若无 JSESSIONID 则创建一个,并在响应头中设置 Set-Cookie：“JSESSIONID=XXXXXXX”
     * @param createIfNotExists
     * @return
     */

    public HttpSession getSession(boolean createIfNotExists){
        if(session!=null){
            return session;
        }

        for(Cookie cookie:cookies){
            if(cookie.getKey().equals("JSESSIONID")){
                HttpSession currentSession = servletContext.getSession(cookie.getValue());
                if(currentSession!=null){
                    return session;
                }
            }
        }

        if(!createIfNotExists){
            return null;
        }

        session = servletContext.createSession(response);
        return session;
    }

    public HttpSession getSession(){
        return getSession(true);
    }

    //解析请求参数
    private void parseParams(String params){
        String[] urlParams =params.split("&");
        for(String param:urlParams){
            String[] kv = param.split("=");
            this.params.put(kv[0],kv[1]);
        }
    }

    public void setAttribute(String key,Object value){
        attributes.put(key,value);
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }

    public String getParameter(String key){
        String value = this.params.get(key);
        return value;
    }


}
