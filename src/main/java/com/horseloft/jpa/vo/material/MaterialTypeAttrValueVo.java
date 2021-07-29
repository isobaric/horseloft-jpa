package com.horseloft.jpa.vo.material;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Date: 2020/2/8 下午4:05
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
public class MaterialTypeAttrValueVo implements Serializable {

    @NotNull
    @ApiModelProperty(value = "属性值ID|0为新增，>0为添加", example = "0", required = true)
    @Min(value = 0, message = "属性值ID错误")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "属性值名称", example = "黑色", required = true)
    @Length(min = 1, max = 20, message = "属性值名称格式错误")
    private String valueName;
}
