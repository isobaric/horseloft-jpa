package com.horseloft.jpa.vo.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/6 15:34
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("资源节点")
public class NodeInfoResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "0")
    private Long id;

    @ApiModelProperty(value = "pid", example = "1")
    private Long pid;

    @ApiModelProperty(value = "节点名称", example = "物料管理")
    private String nodeName;

    @ApiModelProperty(value = "节点路由", example = "/user/add")
    private String webRoute;

    @ApiModelProperty(value = "节点权限|0只读，1读写", example = "1")
    private Integer rolePowerStatus;

    @ApiModelProperty(value = "下一级节点")
    List<NodeInfoResponseVo> list;
}
