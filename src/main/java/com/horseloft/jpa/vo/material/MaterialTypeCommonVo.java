package com.horseloft.jpa.vo.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Date: 2020/2/9 上午10:31
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("通用-物料类型列表")
public class MaterialTypeCommonVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料类型名称", example = "abc")
    private String typeName;
}
