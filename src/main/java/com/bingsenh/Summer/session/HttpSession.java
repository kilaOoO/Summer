package com.bingsenh.Summer.session;

import com.bingsenh.Summer.connector.context.WebApplication;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author hbs
 * @Date 2019/6/26
 */
public class HttpSession {
    private String sessionId;
    private Map<String,Object> attributes;
    private boolean isVaild;

    //用来判断 session 是否过期,当前时间 - 上次访问时间 > 阈值
    private Instant lastAccessed;

    public HttpSession(String sessionId){
        this.sessionId = sessionId;
        this.attributes = new ConcurrentHashMap<>();
        this.isVaild = true;
        this.lastAccessed = Instant.now();
    }

    //使当前 session 失效
    public void Invalidate(){
        this.isVaild = false;
        this.attributes.clear();
        WebApplication.getServletContext().invalidateSession(this);
    }

    public Object getAtrribute(String key){
        if(isVaild){
            this.lastAccessed = Instant.now();
            return attributes.get(key);
        }
        throw new IllegalStateException("session is invalid");
    }

    public Object setAtrribute(String key,Object value){
        if(isVaild){
            this.lastAccessed = Instant.now();
            attributes.put(key,value);
        }
        throw new IllegalStateException("session is invalid");
    }

    public void removeAtrribute(String key){
        if(attributes.containsKey(key)){
            attributes.remove(key);
        }
    }

    public String getSessionId(){
        return sessionId;
    }

    public Instant getLastAccessed(){
        return lastAccessed;
    }




}
