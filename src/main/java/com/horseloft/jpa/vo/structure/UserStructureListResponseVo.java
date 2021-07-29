package com.horseloft.jpa.vo.structure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/19 下午4:54
 * User: YHC
 * Desc: 当前用户的组织结构列表
 */
@Setter
@Getter
@ToString
@ApiModel("用户组织结构列表")
public class UserStructureListResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "名称", example = "张三的公司")
    private String name;

    @ApiModelProperty(value = "下一级")
    private List<UserStructureListResponseVo> list;
}
