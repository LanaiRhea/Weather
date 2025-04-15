package com.lanai.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanai.weather.entity.UserLike;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public interface LikeMapper extends BaseMapper<UserLike> {
//    @Delete("delete from user_like where u_id = #{args1} and address = #{args0}")
//    int delLike(String name, String userId);
}
