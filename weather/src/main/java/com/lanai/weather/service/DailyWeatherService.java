package com.lanai.weather.service;

import com.lanai.weather.Utils.ResData;
import java.util.Date;

public interface DailyWeatherService {
    /**
     * 分页查询天气数据
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    ResData getWeatherDataByDateRange(Date startDate, Date endDate, Integer pageNum, Integer pageSize);
} 