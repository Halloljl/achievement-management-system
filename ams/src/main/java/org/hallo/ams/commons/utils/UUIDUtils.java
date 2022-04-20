package org.hallo.ams.commons.utils;

import java.util.UUID;

/**
 * UUID工具类
 */
public class UUIDUtils {

    /**
     * 获取UUID字符串，无 - 连接
     * @return UUID字符串
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
