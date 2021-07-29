package com.horseloft.jpa.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/6 10:57
 * User: YHC
 * Desc: 用户身份证列表
 */
@Getter
@Setter
@ToString
@ApiModel("用户身份证列表")
public class UserIdPictureResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "123456")
    private Long id;

    @ApiModelProperty(value = "地址", example = "Http://xxx.com/xxx.png")
    private String path;

}
