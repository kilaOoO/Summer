package com.bingsenh.Summer.connector.context;

import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.holder.ServletHolder;
import com.bingsenh.Summer.cookie.Cookie;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.servlet.Servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bingsenh.Summer.session.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@Slf4j
public class ServletContext {

    private final static String DEFAULT_SERVLET = "DefaultServlet";
    /**
     * servlet
     * 别名-> 类名
     * URL Pattern -> 别名
     * 一个Servlet类只能有一个Servlet别名，一个Servlet别名只能对应一个Servlet类
     * 一个Servlet可以对应多个URL，一个URL只能对应一个Servlet
     */
    private Map<String, ServletHolder> servlsts;
    private Map<String,String> servlstMapping;

    /**
     * Context域
     */
    private Map<String,Object> attributes;

    private Map<String,HttpSession> sessions;

    public ServletContext(){
        init();
    }

    /**
     * 应用初始化
     */
    private void init(){
        this.servlsts = new HashMap<>();
        this.servlstMapping = new HashMap<>();
        this.attributes = new ConcurrentHashMap<>();
        parseXml();
    }

    /**
     * 应用销毁
     */
    public void destory(){
        servlsts.values().forEach(servletHolder -> {
            if(servletHolder.getServlet()!=null){
                servletHolder.getServlet().destory();
            }
        });
    }


    /**
     * 根据路由获取对应的servlet实例
     */
    private Servlet initServletInstance(String servletAlias) throws ServletException {
        ServletHolder servletHolder = servlsts.get(servletAlias);
        if(servletHolder == null){
            throw new ServletException();
        }

        if(servletHolder.getServlet() == null){
            synchronized (ServletContext.class) {
                if(servletHolder.getServlet()==null) {
                    try {
                        Servlet servlet = (Servlet) Class.forName(servletHolder.getServletClass()).newInstance();
                        servlet.init();
                        servletHolder.setServlet(servlet);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return servletHolder.getServlet();
    }

    public Servlet mapServlet(String url) throws ServletException {

        //精准匹配，暂时不考虑路径匹配
        String servletAlias = servlstMapping.get(url);
        if(servletAlias!=null){
            log.info("别名:"+servletAlias);
            return initServletInstance(servletAlias);
        }

        return initServletInstance(DEFAULT_SERVLET);
    }

    /**
     * web.xml解析
     */
    private void parseXml(){
        SAXReader reader = new SAXReader();
        Document doc = null;

        try {
            doc = reader.read(ServletContext.class.getResourceAsStream("web.xml"));
        } catch (DocumentException e) {
            System.out.println("加载失败");
            e.printStackTrace();
        }

        Element root = doc.getRootElement();
        //解析servlet
        List<Element> servlets = root.elements("servlet");
        for(Element servletEle:servlets){
            String key = servletEle.element("servlet-name").getText();
            String value = servletEle.element("servlet-class").getText();
            this.servlsts.put(key,new ServletHolder(value));
        }

        List<Element> servletMapping = root.elements("servlet-mapping");
        for(Element mapping :servletMapping){
            List<Element> urlPatterns = mapping.elements("url-pattern");
            String value = mapping.element("servlet-name").getText();
            for(Element urlPattern:urlPatterns){
                String key = urlPattern.getText();
                this.servlstMapping.put(key,value);
            }

        }
    }


    /**
     * Session
     */

    /**
     * 创建session
     * @param response
     * @return
     */
    public HttpSession createSession(Response response){
        HttpSession httpSession = new HttpSession(UUID.randomUUID().toString());
        sessions.put(httpSession.getSessionId(),httpSession);
        response.addCookie(new Cookie("JSESSIONID",httpSession.getSessionId()));
        return httpSession;
    }

    public HttpSession getSession(String key){
        return sessions.get(key);
    }

    public Object getAttrubite(String key){
        return attributes.get(key);
    }

    public void setAttributes(String key,Object value){
        attributes.put(key,value);
    }

}
