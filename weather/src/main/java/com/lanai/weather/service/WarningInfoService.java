package com.lanai.weather.service;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.entity.WarningInfo;

public interface WarningInfoService {
    /**
     * 创建预警信息
     */
    ResData createWarning(WarningInfo warningInfo);

    /**
     * 更新预警信息
     */
    ResData updateWarning(WarningInfo warningInfo);

    /**
     * 删除预警信息
     */
    ResData deleteWarning(Long id);

    /**
     * 分页查询预警信息
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param status 预警状态（可选）
     * @param type 预警类型（可选）
     * @return 分页结果
     */
    ResData getWarningList(Integer pageNum, Integer pageSize, Integer status, String type);
}