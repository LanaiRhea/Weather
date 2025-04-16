package com.lanai.weather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanai.weather.Utils.ResData;
import com.lanai.weather.entity.DailyWeather;
import com.lanai.weather.mapper.DailyWeatherMapper;
import com.lanai.weather.service.DailyWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyWeatherServiceImpl implements DailyWeatherService {
    private final DailyWeatherMapper dailyWeatherMapper;

    @Override
    public ResData getWeatherDataByDateRange(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
        try {
            // 创建分页对象
            Page<DailyWeather> page = new Page<>(pageNum, pageSize);
            
            // 创建查询条件
            QueryWrapper<DailyWeather> queryWrapper = new QueryWrapper<>();
            
            // 如果有时间范围，添加时间条件
            if (startDate != null && endDate != null) {
                // 使用 between 确保包含边界值
                queryWrapper.between("time", startDate, endDate);
                
                // 添加日志
                log.info("查询条件 - 开始时间：{}，结束时间：{}", 
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
            } else if (startDate != null) {
                queryWrapper.ge("time", startDate);
            } else if (endDate != null) {
                queryWrapper.le("time", endDate);
            }
            
            // 按时间升序排序
            queryWrapper.orderByAsc("time");
            
            // 执行分页查询
            Page<DailyWeather> weatherPage = dailyWeatherMapper.selectPage(page, queryWrapper);
            
            // 添加日志输出总记录数
            log.info("查询到总记录数：{}", weatherPage.getTotal());
            
            return ResData.ok(weatherPage);
        } catch (Exception e) {
            log.error("查询天气数据失败", e);
            return ResData.err("查询天气数据失败：" + e.getMessage());
        }
    }
} 