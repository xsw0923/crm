package com.xsw.crm.settings.service;

import com.xsw.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByloginActAndPwd(Map<String,Object> map);

    List<User> queryAllUsers();
}
