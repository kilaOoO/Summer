package sample;

import com.bingsenh.Summer.connector.Request.Request;
import com.bingsenh.Summer.connector.Response.Response;
import com.bingsenh.Summer.servlet.HttpServlet;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author hbs
 * @Date 2019/6/2
 */
@Slf4j
public class LoginServlet extends HttpServlet {
    @Override
    public void init() {
        log.info("LoginServlet init...");
    }

    @Override
    public void destory() {
        log.info("LoginServlet destroy");
    }

    @Override
    public void doPost(Request request, Response response) {
        Map<String,String> params = request.getParams();
        String username = params.get("username");
        String password = params.get("password");
        System.out.println(username);
        System.out.println(password);
        String message = "Hello world";
        response.setBody(message.getBytes());
    }
}
