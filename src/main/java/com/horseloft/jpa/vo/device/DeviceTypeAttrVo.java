package com.horseloft.jpa.vo.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Date: 2020/1/28 下午5:30
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("设备类型属性")
public class DeviceTypeAttrVo implements Serializable {

    @ApiModelProperty(value = "id|新增时为0", example = "1")
    @Min(value = 1, message = "设备属性值id错误")
    private Long id;

    @ApiModelProperty(value = "属性名称", example = "ABC")
    @NotNull(message = "属性名称不能为NULL")
    @Length(min = 1, max = 20, message = "属性名称格式错误")
    private String attrName;

    @ApiModelProperty(value = "修改提示|true提示，false不提示", example = "true")
    @NotNull(message = "修改提示格式错误")
    private Boolean warnStatus;

    @ApiModelProperty(value = "删除状态|true删除，false未删除", example = "true")
    @NotNull(message = "删除状态格式错误")
    private Boolean deleteStatus;
}
