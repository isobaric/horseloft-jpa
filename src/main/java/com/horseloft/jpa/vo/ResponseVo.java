package com.horseloft.jpa.vo;

import com.horseloft.jpa.enums.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Date: 2020/1/4 14:22
 * User: YHC
 * Desc: 统一响应包装
 */
@Getter
@Setter
@ApiModel("响应")
public class ResponseVo<T> implements Serializable {

    @ApiModelProperty(value = "响应码|数字字符串", example = "200")
    private String code;

    @ApiModelProperty(value = "提示信息|字符串", example = "成功")
    private String message;

    @ApiModelProperty(value = "返回值")
    private T data;

    public ResponseVo() {

    }

    public ResponseVo(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVo<T> ofError() {
        return new ResponseVo<>(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMessage(), null);
    }

    public static <T> ResponseVo<T> ofError(String message) {
        return new ResponseVo<>(ResponseCode.SERVER_ERROR.getCode(), message, null);
    }

    public static <T> ResponseVo<T> ofError(String code, String message) {
        return new ResponseVo<>(code, message, null);
    }

    public static <T> ResponseVo<T> ofError(ResponseCode responseCode) {
        return new ResponseVo<>(responseCode.getCode(), responseCode.getMessage(), null);
    }

    public static <T> ResponseVo<T> ofSuccess() {
        return new ResponseVo<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    public static <T> ResponseVo<T> ofSuccess(String message) {
        return new ResponseVo<>(ResponseCode.SUCCESS.getCode(), message, null);
    }

    public static <T> ResponseVo<T> ofSuccess(T data) {
        return new ResponseVo<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseVo<T> ofSuccess(T data, String message) {
        return new ResponseVo<>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public void setCodeMessage(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
