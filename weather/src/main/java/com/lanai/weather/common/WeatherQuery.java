package com.lanai.weather.common;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.Utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class WeatherQuery {

    public ResData getWeather(String cityName){
        String host = "https://jisutqybmf.market.alicloudapi.com";
        String path = "/weather/query";
        String method = "GET";//GET/POST 任意
        String appcode = "e1ecc5a77f794f1f80cd8822d0f65017";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("city", cityName);
        /*querys.put("citycode", "citycode");
        querys.put("cityid", "cityid");
        querys.put("ip", "ip");
        querys.put("location", "location");*/


        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        /*System.out.println(response.toString());
        获取response的body
        System.out.println(EntityUtils.toString(response.getEntity(),"utf-8"));*/
            List<Object> entity = Collections.singletonList(EntityUtils.toString(response.getEntity(), "utf-8"));
            entity.forEach(System.out::println);

            return ResData.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResData.err();
        }
    }
}
