package com.horseloft.jpa.vo.user;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Date: 2020/1/5 17:00
 * User: YHC
 * Desc: 用户登陆
 */
@Getter
@Setter
@ApiModel("登陆参数")
public class UserLoginRequestVo extends RequestVo {

    @ApiModelProperty(value = "账号|6-20个字的数字或大小写字母", example = "zhangSan", required = true)
    @NotNull
    @Length(min = 4, max = 20, message = "账号格式错误")
    private String account;

    @ApiModelProperty(value = "密码|长度>=6", example = "123456", required = true)
    @NotNull
    @Length(min = 6, message = "密码格式错误")
    private String password;

    @ApiModelProperty(value = "是否7天自动登录|true/false", example = "false", required = true)
    @NotNull
    private Boolean autoLoginStatus;

    @ApiModelProperty(value = "是否工人登录接口登录|true/false", hidden = true)
    @NotNull
    private boolean isWorkerLogin = false;
}
