package fm.douban.util;


import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.stereotype.Component;


@Component
public class HttpUtil {


    // 构建必要的 http header 。也许爬虫有用
    public Map<String, String> buildHeaderData(String referer, String host){
        HashMap<String, String> Container = new HashMap<>();
        Container.put("referer",referer);
        Container.put("host",host);
        return Container;
    }

    // 根据输入的url，读取页面内容并返回
    public String getContent(String url, Map<String, String> headers){
        // okHttpClient 实例
        OkHttpClient okHttpClient = new OkHttpClient();
        // 定义一个request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                .addHeader("Referer", headers.get("referer"))
                .addHeader("Host",  headers.get("host"))
//                .addHeader("Cookie",readContent("cookie.txt"))
                .build();
        // 返回结果字符串
        String result = null;
        try {
            // 执行请求
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            System.out.println("request " + url + " error . ");
            e.printStackTrace();
        }
        return result;
    }


}
