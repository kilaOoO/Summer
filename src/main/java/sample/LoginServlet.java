package sample;

import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Response;
import com.bingsenh.Summer.container.enumeration.HttpStatus;
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
    public void doGet(Request request, Response response) {
        Map<String,String> params = request.getParams();
        String username = params.get("username");
        String password = params.get("password");
        System.out.println(username);
        System.out.println(password);
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>菜鸟教程(runoob.com)</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>hello</p>\n" +
                "</body>\n" +
                "</html>";

        response.setStatus(HttpStatus.OK);
        response.setBody(message.getBytes());
    }
}
