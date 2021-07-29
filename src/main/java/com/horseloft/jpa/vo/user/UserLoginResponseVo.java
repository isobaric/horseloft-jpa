package com.horseloft.jpa.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/5 17:27
 * User: YHC
 * Desc: 登陆响应
 */
@Getter
@Setter
@ToString
@ApiModel("登陆响应")
public class UserLoginResponseVo implements Serializable {

    @ApiModelProperty(value = "token", example = "abc")
    private String token;

    @ApiModelProperty(value = "用户名", example = "张三")
    private String realName;

    @ApiModelProperty(value = "组织架构", example = "公司-工厂-车间")
    private String structure;

    @ApiModelProperty(value = "密码是否已修改|0未修改，1已修改", example = "0")
    private Integer passwordStatus;

}
