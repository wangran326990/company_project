package com.demo.enums;

public enum BizCodeEnum {
    UNKNOW_EXCEPTION(10000,"unknown system error"),
    VALIDATION_EXCEPTION(10001,"Request parameter validation failed.");

    private int code;
    private String msg;
    BizCodeEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
