package com.bingsenh.Summer.connector.context;

import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.connector.context.holder.FilterHolder;
import com.bingsenh.Summer.connector.context.holder.ServletHolder;
import com.bingsenh.Summer.cookie.Cookie;
import com.bingsenh.Summer.exception.NotFoundException;
import com.bingsenh.Summer.exception.ServletException;
import com.bingsenh.Summer.filter.Filter;
import com.bingsenh.Summer.servlet.Servlet;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.bingsenh.Summer.session.HttpSession;
import com.bingsenh.Summer.session.SessionCleaner;
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
     * filter
     * 别名-> 类名
     * URL Pattern -> 别名
     * 一个 URL Pattern 能对应多个 filter
     */

    private Map<String, FilterHolder> filters;
    private Map<String,List<String>> filterMapping;

    /**
     * context 域
     */
    private Map<String,Object> attributes;

    private Map<String,HttpSession> sessions;

    private SessionCleaner sessionCleaner;

    public ServletContext(){
        init();
    }

    /**
     * 应用初始化
     */
    private void init(){
        this.servlsts = new HashMap<>();
        this.servlstMapping = new HashMap<>();
        this.filters = new HashMap<>();
        this.filterMapping = new HashMap<>();
        this.attributes = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
        this.sessionCleaner = new SessionCleaner();
        sessionCleaner.start();
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
     * 根据路由获取filter实例
     */

    public List<Filter> mapFilter(String url) throws NotFoundException {
        List<Filter> result = new ArrayList<>();
        List<String> filterAlias = filterMapping.get(url);
        if(filterAlias!=null){
            for(String alias : filterAlias){
                result.add(initFilterInstance(alias));
            }
        }
        return result;
    }

    private Filter initFilterInstance(String filterAlias) throws NotFoundException {
        FilterHolder filterHolder = filters.get(filterAlias);
        if(filterHolder == null){
            throw new NotFoundException();
        }

        if(filterHolder.getFilter() == null){
            try {
                Filter filter = (Filter) Class.forName(filterHolder.getFilterClass()).newInstance();
                filter.init();
                filterHolder.setFilter(filter);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return filterHolder.getFilter();
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


        //解析 filter
        List<Element> filters = root.elements("filter");
        for(Element filterEle:filters){
            String key = filterEle.element("filter-name").getText();
            String value  = filterEle.element("filter-class").getText();
            this.filters.put(key,new FilterHolder(value));
        }

        List<Element> filterMappings = root.elements("filter-mapping");
        for(Element mapping:filterMappings){
            List<Element> urlPatterns = mapping.elements("url-pattern");
            String value = mapping.element("filter-name").getText();
            for(Element urlPattern:urlPatterns){
                List<String> values = this.filterMapping.get(urlPattern.getText());
                if(values == null){
                    values = new ArrayList<>();
                    this.filterMapping.put(urlPattern.getText(),values);
                }
                values.add(value);
            }
        }
    }


    /**
     * Session
     */

    /**
     * 创建 session
     * @param response
     * @return
     */
    public HttpSession createSession(Response response){
        HttpSession httpSession = new HttpSession(UUID.randomUUID().toString());
        sessions.put(httpSession.getSessionId(),httpSession);
        response.addCookie(new Cookie("JSESSIONID",httpSession.getSessionId()));
        return httpSession;
    }

    /**
     * 销毁 session
     * @param session
     */
    public void invalidateSession(HttpSession session){
        sessions.remove(session.getSessionId());
    }

    /**
     * 清除过期的 session
     */
    public void cleanSessions(){
        for(Iterator<Map.Entry<String,HttpSession>> it = sessions.entrySet().iterator();it.hasNext();){
            Map.Entry<String,HttpSession> entry = it.next();
            if(Duration.between(entry.getValue().getLastAccessed(), Instant.now()).getSeconds()>=300){
                it.remove();
            }
        }
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
