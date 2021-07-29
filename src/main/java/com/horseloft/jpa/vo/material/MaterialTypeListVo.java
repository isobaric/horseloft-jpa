package com.horseloft.jpa.vo.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/8 下午6:11
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("物料类型列表响应")
public class MaterialTypeListVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料类型名称", example = "abc")
    private String typeName;

    @ApiModelProperty(value = "物料类型编码", example = "abc")
    private String typeCode;

    @ApiModelProperty(value = "属性", example = "颜色、尺寸")
    private String attrText;
}
