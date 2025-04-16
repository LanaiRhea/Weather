package com.lanai.weather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanai.weather.Utils.ResData;
import com.lanai.weather.entity.WarningInfo;
import com.lanai.weather.mapper.WarningInfoMapper;
import com.lanai.weather.service.WarningInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarningInfoServiceImpl implements WarningInfoService {
    private final WarningInfoMapper warningInfoMapper;

    @Override
    public ResData createWarning(WarningInfo warningInfo) {
        try {
            // 设置初始状态为0
            warningInfo.setStatus(0);
            int result = warningInfoMapper.insert(warningInfo);
            return ResData.resultInt(result);
        } catch (Exception e) {
            log.error("创建预警信息失败", e);
            return ResData.err("创建预警信息失败：" + e.getMessage());
        }
    }

    @Override
    public ResData updateWarning(WarningInfo warningInfo) {
        try {
            int result = warningInfoMapper.updateById(warningInfo);
            return ResData.resultInt(result);
        } catch (Exception e) {
            log.error("更新预警信息失败", e);
            return ResData.err("更新预警信息失败：" + e.getMessage());
        }
    }

    @Override
    public ResData deleteWarning(Long id) {
        try {
            int result = warningInfoMapper.deleteById(id);
            return ResData.resultInt(result);
        } catch (Exception e) {
            log.error("删除预警信息失败", e);
            return ResData.err("删除预警信息失败：" + e.getMessage());
        }
    }

    @Override
    public ResData getWarningList(Integer pageNum, Integer pageSize) {
        try {
            // 创建分页对象
            Page<WarningInfo> page = new Page<>(pageNum, pageSize);

            // 创建查询条件，按创建时间降序排序
            QueryWrapper<WarningInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("create_time");

            // 执行分页查询
            Page<WarningInfo> warningPage = warningInfoMapper.selectPage(page, queryWrapper);

            log.info("查询到总记录数：{}", warningPage.getTotal());

            return ResData.ok(warningPage);
        } catch (Exception e) {
            log.error("查询预警信息失败", e);
            return ResData.err("查询预警信息失败：" + e.getMessage());
        }
    }
}