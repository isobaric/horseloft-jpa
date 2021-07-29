package com.horseloft.jpa.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/29 下午3:22
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("图片响应结果")
public class PictureResponseVo implements Serializable {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "路径", example = "/image/11.png")
    private String path;
}
