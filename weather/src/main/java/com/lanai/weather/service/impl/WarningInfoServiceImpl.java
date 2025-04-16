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

import java.util.Date;

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
            // 设置创建时间和更新时间
            Date now = new Date();
            warningInfo.setCreateTime(now);
            warningInfo.setUpdateTime(now);
            
            // 验证预警类型和等级
            if (!isValidWarningType(warningInfo.getType())) {
                return ResData.err("无效的预警类型");
            }
            if (!isValidWarningLevel(warningInfo.getLevel())) {
                return ResData.err("无效的预警等级");
            }
            
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
            // 获取现有数据
            WarningInfo existingWarning = warningInfoMapper.selectById(warningInfo.getId());
            if (existingWarning == null) {
                return ResData.err("预警信息不存在");
            }

            // 只更新需要更新的字段
            if (warningInfo.getType() != null) {
                if (!isValidWarningType(warningInfo.getType())) {
                    return ResData.err("无效的预警类型");
                }
                existingWarning.setType(warningInfo.getType());
            }
            if (warningInfo.getLevel() != null) {
                if (!isValidWarningLevel(warningInfo.getLevel())) {
                    return ResData.err("无效的预警等级");
                }
                existingWarning.setLevel(warningInfo.getLevel());
            }
            if (warningInfo.getContent() != null) {
                existingWarning.setContent(warningInfo.getContent());
            }
            if (warningInfo.getStatus() != null) {
                existingWarning.setStatus(warningInfo.getStatus());
            }

            // 设置更新时间
            existingWarning.setUpdateTime(new Date());
            
            int result = warningInfoMapper.updateById(existingWarning);
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
    public ResData getWarningList(Integer pageNum, Integer pageSize, Integer status, String type) {
        try {
            // 创建分页对象
            Page<WarningInfo> page = new Page<>(pageNum, pageSize);

            // 创建查询条件，按创建时间降序排序
            QueryWrapper<WarningInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("create_time");
            
            // 如果状态不为空，添加状态筛选条件
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            // 如果预警类型不为空，添加类型筛选条件
            if (type != null && !type.trim().isEmpty()) {
                queryWrapper.eq("type", type);
            }

            // 执行分页查询
            Page<WarningInfo> warningPage = warningInfoMapper.selectPage(page, queryWrapper);
            log.info("查询到总记录数：{}", warningPage.getTotal());

            return ResData.ok(warningPage);
        } catch (Exception e) {
            log.error("查询预警信息失败", e);
            return ResData.err("查询预警信息失败：" + e.getMessage());
        }
    }
    
    // 验证预警类型
    private boolean isValidWarningType(String type) {
        return type != null && !type.trim().isEmpty();
    }
    
    // 验证预警等级
    private boolean isValidWarningLevel(String level) {
        if (level == null) return false;
        return level.matches("蓝色|黄色|橙色|红色");
    }
}