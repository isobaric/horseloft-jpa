package com.horseloft.jpa.vo.material;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Date: 2020/2/9 上午10:53
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("物料主单位添加")
public class MaterialUnitAddVo extends RequestVo {

    @NotNull
    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "0", required = true)
    @Min(value = 0, message = "ID错误")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "计量单位", example = "KG", required = true)
    @Length(min = 1, max = 10, message = "计量单位格式错误")
    private String unitCode;

    @NotNull
    @ApiModelProperty(value = "单位名称", example = "千克", required = true)
    @Length(min = 1, max = 100, message = "单位名称格式错误")
    private String unitName;

    @NotNull
    @ApiModelProperty(value = "单位类型|1数量、2长度、3体积，4重量，5金钱", example = "1", required = true)
    @Range(min = 1, max = 5, message = "单位类型格式错误")
    private Integer type;
}
