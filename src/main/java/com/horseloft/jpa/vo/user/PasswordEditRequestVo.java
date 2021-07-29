package com.horseloft.jpa.vo.user;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Date: 2020/1/7 10:24
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("密码修改")
public class PasswordEditRequestVo extends RequestVo {

    @ApiModelProperty(value = "密码|工人密码为6位数字", example = "123456", required = true)
    @NotNull
    @Length(min = 6, message = "密码不符合规范，请重新输入")
    private String password;
}
