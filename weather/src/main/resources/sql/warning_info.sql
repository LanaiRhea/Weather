-- 修改warning_info表结构
ALTER TABLE warning_info
DROP COLUMN title,
ADD COLUMN type VARCHAR(20) COMMENT '预警类型',
ADD COLUMN level VARCHAR(20) COMMENT '预警等级';

-- 更新现有数据（如果有的话）
UPDATE warning_info SET type = '暴雨', level = '蓝色' WHERE type IS NULL; 