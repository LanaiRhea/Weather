package com.lanai.weather.service;

import com.lanai.weather.Utils.ResData;

/**
 */
public interface WeatherService {
    ResData getWeatherData(String cityName);
}
