package com.lanai.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanai.weather.entity.DailyWeather;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherMapper extends BaseMapper<DailyWeather> {
} 