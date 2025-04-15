package com.lanai.weather.service.impl;

import com.lanai.weather.Utils.CacheService;
import com.lanai.weather.Utils.ResData;
import com.lanai.weather.common.WeatherQuery;
import com.lanai.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 */
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherQuery weatherQuery;
    private final CacheService cacheService;

    @Override
//    @Async
    public ResData getWeatherData(String cityName) {
        //首先从redis查询
        ResData redisResult = cacheService.getObject(cityName,ResData.class);
        if (redisResult != null){//缓存有直接返回
            return redisResult;
        }
        //查询天气
        ResData weather = weatherQuery.getWeather(cityName);
        //将结果放入缓存
        cacheService.add(cityName,weather,23 - new Date().getHours(), TimeUnit.HOURS);
        return weather;
    }
}
