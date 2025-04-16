package com.lanai.weather.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeather implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Date time;
    private Double rainfall;
    private Double minTemp;
    private Double maxTemp;
    private Double maxWindSpeed;
} 