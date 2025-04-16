package com.lanai.weather.controller;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.service.impl.DailyWeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/api/daily-weather")
@RequiredArgsConstructor
public class DailyWeatherController {
    private final static Logger log = LogManager.getLogger(DailyWeatherController.class.getName());
    
    private final DailyWeatherServiceImpl dailyWeatherService;

    @GetMapping("/page")
    public ResData getWeatherDataByPage(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        // 如果有结束日期，将其调整到当天的最后一刻
        if (endDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            endDate = calendar.getTime();
        }
        
        log.info("分页查询天气数据，开始时间：{}，结束时间：{}，页码：{}，每页数量：{}", 
                startDate, endDate, pageNum, pageSize);
        return dailyWeatherService.getWeatherDataByDateRange(startDate, endDate, pageNum, pageSize);
    }
} 