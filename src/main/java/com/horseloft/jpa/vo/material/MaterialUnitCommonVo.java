package com.horseloft.jpa.vo.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/9 下午4:13
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("通用-物料主单位响应")
public class MaterialUnitCommonVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料主单位名称", example = "克")
    private String unitName;
}
