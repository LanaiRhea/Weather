package com.lanai.weather.controller;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.entity.WarningInfo;
import com.lanai.weather.service.impl.WarningInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warning")
@RequiredArgsConstructor
public class WarningInfoController {
    private final static Logger log = LogManager.getLogger(WarningInfoController.class.getName());

    private final WarningInfoServiceImpl warningInfoService;

    @PostMapping("/create")
    public ResData createWarning(@RequestBody WarningInfo warningInfo) {
        log.info("创建预警信息：{}", warningInfo);
        return warningInfoService.createWarning(warningInfo);
    }

    @PutMapping("/update")
    public ResData updateWarning(@RequestBody WarningInfo warningInfo) {
        log.info("更新预警信息：{}", warningInfo);
        return warningInfoService.updateWarning(warningInfo);
    }

    @DeleteMapping("/delete/{id}")
    public ResData deleteWarning(@PathVariable("id") Long id) {
        log.info("删除预警信息，ID：{}", id);
        return warningInfoService.deleteWarning(id);
    }

    @GetMapping("/list")
    public ResData getWarningList(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) Integer status,
                                @RequestParam(required = false) String type) {
        log.info("获取预警列表，页码：{}，每页大小：{}，状态：{}，类型：{}", pageNum, pageSize, status, type);
        return warningInfoService.getWarningList(pageNum, pageSize, status, type);
    }
}