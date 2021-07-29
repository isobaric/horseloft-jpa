package com.horseloft.jpa.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Date: 2020/1/4 14:31
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("请求ID参数")
public class RequestIdVo extends RequestVo{

    @ApiModelProperty(value = "ID，最小值为：1", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "ID格式错误")
    private Long id;
}
