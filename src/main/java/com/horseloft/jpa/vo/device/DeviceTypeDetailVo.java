package com.horseloft.jpa.vo.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/28 下午6:14
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("设备类型详情响应")
public class DeviceTypeDetailVo implements Serializable {

    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1")
    private Long id;

    @ApiModelProperty(value = "设备类型名称", example = "abc")
    private String deviceTypeName;

    @ApiModelProperty(value = "描述", example = "abc")
    private String remark;

    @ApiModelProperty(value = "属性值")
    List<DeviceTypeAttrVo> attrValue;
}
