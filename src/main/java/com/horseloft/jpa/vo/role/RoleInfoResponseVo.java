package com.horseloft.jpa.vo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/7 10:12
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("角色信息")
public class RoleInfoResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "角色名称", example = "厂长")
    private String roleName;

    @ApiModelProperty(value = "权限|0只读，1读写", example = "0")
    private Integer rolePower;

    @ApiModelProperty(value = "是否可以删除|0不可删除，1可以删除", example = "1")
    private Integer removeStatus;

    @ApiModelProperty(value = "角色图片ID", example = "1")
    private Long rolePictureId;

}
