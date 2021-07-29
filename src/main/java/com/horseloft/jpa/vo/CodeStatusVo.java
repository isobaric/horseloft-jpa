package com.horseloft.jpa.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/7 11:18
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("网站状态码")
public class CodeStatusVo implements Serializable {

    @ApiModelProperty(value = "成功|非200全部为失败", example = "200")
    private String successCode;

    @ApiModelProperty(value = "参数错误", example = "400")
    private String parameterErrorCode;

    @ApiModelProperty(value = "未授权", example = "401")
    private String unauthorizedCode;

    @ApiModelProperty(value = "token过期需要重新登录", example = "403")
    private String tokenExpireCode;

    @ApiModelProperty(value = "其他状态码", example = "500")
    private String otherCode;

}
