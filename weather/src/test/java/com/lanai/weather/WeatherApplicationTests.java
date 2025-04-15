package com.lanai.weather;

import com.lanai.weather.Utils.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class WeatherApplicationTests {

    @Autowired
    private CacheService cacheService;


    @Test
    void test(){
        Date date = new Date();
        SimpleDateFormat hh = new SimpleDateFormat("HH");
        String format = hh.format(date.getTime());
        System.out.println(format);

    }

    @Test
    void contextLoads() {
            String host = "https://jisutqybmf.market.alicloudapi.com";
            String path = "/weather/query";
            String method = "GET";//GET/POST 任意
            String appcode = "你自己的AppCode";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", "application/json; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("city", "安顺");
//            querys.put("citycode", "citycode");
//            querys.put("cityid", "cityid");
//            querys.put("ip", "ip");
//            querys.put("location", "location");


            try {
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */

//                System.out.println(response.toString());
                //获取response的body
//
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @Test
    void redisTest(){
//        User zhangsan = new User("1", "zhangsan", "123456");
//        cacheService.add("user",zhangsan,24, TimeUnit.HOURS);
        
    }
    
    @Test
    void getUser(){
        System.out.println("=================>"+cacheService.get("武汉"));
    }
}
