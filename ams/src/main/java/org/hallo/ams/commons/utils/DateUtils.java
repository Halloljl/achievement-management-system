package org.hallo.ams.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对Date类型数据进行处理
 */
public class DateUtils {

    /**
     * 将日期类转变为字符串 时间日期
     * @param dateTime
     * @return
     */
    public static String formateDateTime(Date dateTime){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
    }

    /**
     * 将日期类转变为字符串  日期
     * @param date
     * @return
     */
    public static String formateDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 将日期类转变为字符串 时间
     * @param time
     * @return
     */
    public static String formateTime(Date time){
        return new SimpleDateFormat("HH:mm:ss").format(time);
    }
}
