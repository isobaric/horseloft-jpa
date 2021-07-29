package com.horseloft.jpa.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Date: 2020/1/6 11:30
 * User: YHC
 * Desc: ID列表请求
 */
@Getter
@Setter
@ApiModel("列表ID参数")
public class RequestIdListVo extends RequestVo {

    @ApiModelProperty(value = "数组格式的ID，最小值为：1", example = "[1, 2]", required = true)
    @NotNull
    @Size(min = 1, message = "ID格式错误")
    private List<Long> id;
}
