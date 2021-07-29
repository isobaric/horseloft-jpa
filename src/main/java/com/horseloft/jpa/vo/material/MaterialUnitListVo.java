package com.horseloft.jpa.vo.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/9 下午1:22
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("物料单位列表响应")
public class MaterialUnitListVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "计量单位", example = "KG")
    private String unitCode;

    @ApiModelProperty(value = "单位名称", example = "千克")
    private String unitName;

    @ApiModelProperty(value = "单位类型|1数量、2长度、3体积，4重量，5金钱", example = "金钱")
    private String typeText;
}
