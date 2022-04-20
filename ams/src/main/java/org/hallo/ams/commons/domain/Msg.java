package org.hallo.ams.commons.domain;

/**
 *
 * 返回给前端的数据封装类
 * @author hallo
 * @create 2022-04-03 17:04
 */
public class Msg {

    /**
     * 处理成功或者失败的标记  100 失败  200 成功
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 返回的其他属性
     */
    private Object retData;

    public Msg() {
    }

    public Msg(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Msg(String code, String message, Object retData) {
        this.code = code;
        this.message = message;
        this.retData = retData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", retData=" + retData +
                '}';
    }
}
