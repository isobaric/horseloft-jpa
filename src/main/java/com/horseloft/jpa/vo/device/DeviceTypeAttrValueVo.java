package com.horseloft.jpa.vo.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Date: 2020/1/28 下午4:38
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("设备类型属性值")
public class DeviceTypeAttrValueVo implements Serializable {

    @ApiModelProperty(value = "id", example = "1")
    @Min(value = 1, message = "设备属性值id错误")
    private Long id;

    @ApiModelProperty(value = "属性值", example = "ABC")
    @NotNull(message = "属性值不能为NULL")
    private String attrValue;
}
