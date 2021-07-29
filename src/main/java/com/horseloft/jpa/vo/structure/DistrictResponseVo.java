package com.horseloft.jpa.vo.structure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/19 11:57
 * User: YHC
 * Desc: 行政区
 */
@Getter
@Setter
@ToString
@ApiModel("行政区")
public class DistrictResponseVo implements Serializable {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "pid", example = "1")
    private Long pid;

    @ApiModelProperty(value = "pid", example = "1")
    private String name;

    @ApiModelProperty(value = "下一级")
    private List<DistrictResponseVo> list;

}
