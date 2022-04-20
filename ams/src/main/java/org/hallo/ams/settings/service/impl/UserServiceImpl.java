package org.hallo.ams.settings.service.impl;

import org.hallo.ams.settings.domain.User;
import org.hallo.ams.settings.mapper.UserMapper;
import org.hallo.ams.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hallo
 * @create 2022-04-03 16:58
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据账号密码登录
     * @param map
     * @return
     */
    @Override
    public User queryUserByLoginActAndLoginPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndLoginPwd(map);
    }

    @Override
    public String queryStuNameById(String id) {
        return userMapper.selectStuNameById(id);
    }


}
