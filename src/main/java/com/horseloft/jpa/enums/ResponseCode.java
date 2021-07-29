package com.horseloft.jpa.enums;

import lombok.Getter;

/**
 * Date: 2020/1/4 13:48
 * User: YHC
 * Desc:
 */
@Getter
public enum  ResponseCode {
    //成功
    SUCCESS("200", "success"),
    //参数错误
    PARAMETER_ERROR("400", "请求参数错误"),
    //权限验证失败
    UNAUTHORIZED("401", "未授权的操作"),
    //登录状态失效
    TOKEN_EXPIRE("403", "登录状态已失效，请重新登录"),
    //不支持的请求方式
    METHOD_ERROR("406", "请求方式错误"),
    //服务异常
    SERVER_ERROR("500", "服务异常，请稍后重试"),
    //重复提交
    REPEAT_SUBMIT("510", "请勿重复提交"),
    //数据库异常信息
    DATA_ERROR("601", "数据异常，请稍后重试");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
