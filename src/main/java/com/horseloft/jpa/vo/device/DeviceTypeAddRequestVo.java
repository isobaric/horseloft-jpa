package com.horseloft.jpa.vo.device;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Date: 2020/1/28 下午5:19
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("设备类型添加编辑请求")
public class DeviceTypeAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "ID格式错误")
    private Long id;

    @ApiModelProperty(value = "设备类型名称", example = "abc", required = true)
    @NotNull
    @Length(max = 20, message = "设备类型名称格式错误")
    private String deviceTypeName;

    @ApiModelProperty(value = "描述", example = "abc", required = true)
    @NotNull
    @Length(max = 250, message = "描述不能超过250个字符")
    private String remark;

    @ApiModelProperty(value = "属性值", required = true)
    @NotNull
    List<DeviceTypeAttrVo> attrValue;
}
