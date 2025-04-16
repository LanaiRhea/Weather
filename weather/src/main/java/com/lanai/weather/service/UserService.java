package com.lanai.weather.service;

import com.lanai.weather.Utils.ResData;
import com.lanai.weather.entity.User;


public interface UserService {
    ResData UserLogin(User user);

    ResData UserLike(String id);

    ResData editPwd(User user);

    ResData editInfo(User user);

    ResData addLike(String name, String userId);

    ResData delLike(String name, String userId);

    ResData addUser(User user);

    ResData delUser(String id);

    ResData getAll();
}
