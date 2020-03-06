package sample;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;
import com.bingsenh.Summer.filter.Filter;
import com.bingsenh.Summer.filter.FilterChain;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init() {
        log.info("LogFilter init ...");
    }

    @Override
    public void doFilter(Request request, Response response, FilterChain filterChain) {
        log.info("Do something before login... ");
        filterChain.doFliter(request,response);
        log.info("Do something after login...");
    }

    @Override
    public void destory() {
        log.info("LogFilter destory...");
    }
}
