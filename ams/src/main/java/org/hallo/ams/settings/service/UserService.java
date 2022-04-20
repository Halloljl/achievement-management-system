package org.hallo.ams.settings.service;

import org.hallo.ams.settings.domain.User;

import java.util.Map;

/**
 * @author hallo
 * @create 2022-04-03 16:55
 */

public interface UserService {
    User queryUserByLoginActAndLoginPwd(Map<String, Object> map);

    String queryStuNameById(String id);

}
