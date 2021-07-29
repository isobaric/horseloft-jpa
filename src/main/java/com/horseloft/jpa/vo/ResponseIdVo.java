package com.horseloft.jpa.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/28 下午5:52
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("ID返回值")
public class ResponseIdVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1", required = true)
    private Long id;
}
