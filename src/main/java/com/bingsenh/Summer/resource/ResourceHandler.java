package com.bingsenh.Summer.resource;
import com.bingsenh.Summer.container.Request.Request;
import com.bingsenh.Summer.container.Response.Header;
import com.bingsenh.Summer.container.Response.Response;
import com.bingsenh.Summer.container.enumeration.HttpStatus;
import com.bingsenh.Summer.util.IOUtil;
import com.bingsenh.Summer.util.MimeTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class ResourceHandler {
    private static HashMap<String,String>  Etag = new HashMap<>();

    public static void handle(Request request, Response response){
        String url = request.getUrl();
        if(ResourceHandler.class.getResource(url) == null){
            log.info("找不到资源：{}",url);
            throw new RuntimeException();
        }
        if(Etag.containsKey(url) && request.getHeaders().containsKey("If-None-Match") && request.getHeaders().get("If-None-Match").equals(Etag.get(url))){
            // 304 重定向取缓存
            response.setStatus(HttpStatus.CACHE);
            log.info("cache......");
        }else {
            try {
                byte[] body = IOUtil.getBytesFromFile(url);

                // 生成一个文件唯一标识符 Etag
                File file = new File(ResourceHandler.class.getResource(url).getFile());
                FileInputStream fileInputStream = new FileInputStream(file);
                String hex = DigestUtils.sha512Hex(fileInputStream);
                System.out.println(hex);
                Etag.put(url, hex);
                Header cacheHeader = new Header();
                cacheHeader.setKey("Cache-Control");
                cacheHeader.setValue("max-age=30");
                response.addHeader(cacheHeader);

                Header etagHeader = new Header();
                etagHeader.setKey("Etag");
                etagHeader.setValue(hex);
                response.addHeader(etagHeader);

                response.setContentType(MimeTypeUtil.getTypes(url));
                response.setBody(body);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
