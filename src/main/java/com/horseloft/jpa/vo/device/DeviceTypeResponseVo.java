package com.horseloft.jpa.vo.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/28 下午6:21
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("设备类型列表响应")
public class DeviceTypeResponseVo implements Serializable {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "设备类型名称", example = "ABC")
    private String deviceTypeName;

    @ApiModelProperty(value = "图片", example = "/picture/001.jpg")
    private String picturePath;

}
