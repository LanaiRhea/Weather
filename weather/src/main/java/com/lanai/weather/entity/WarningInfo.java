package com.lanai.weather.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarningInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String type;    // 预警类型：暴雨、大风、高温、低温
    private String level;   // 预警等级：蓝色、黄色、橙色、红色
    private String content; // 预警内容
    private Integer status; // 预警状态：0-未启用，1-已启用
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
}