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
public class RedirectServlet extends HttpServlet {
    @Override
    public void init() {
        log.info("RedirectServlet init...");
    }

    @Override
    public void destory() {
        log.info("Redirectervlet destroy");
    }

    @Override
    public void doGet(Request request, Response response) {

            String message = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<title>菜鸟教程(runoob.com)</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <p>Redirect success</p>\n" +
                    "</body>\n" +
                    "</html>";

            response.setStatus(HttpStatus.OK);
            response.setBody(message.getBytes());

    }
}
