package com.xsw.crm.commons.domain;

public class ReturnObject {
    private String code;   //处理成功获取失败的标志，1----成功，0----失败
    private String message;//提示信息
    private Object retDate;//其他数据

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

    public Object getRetDate() {
        return retDate;
    }

    public void setRetDate(Object retDate) {
        this.retDate = retDate;
    }
}
