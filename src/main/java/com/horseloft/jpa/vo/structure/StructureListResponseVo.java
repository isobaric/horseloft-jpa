package com.horseloft.jpa.vo.structure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/6 18:31
 * User: YHC
 * Desc: 组织管理三级列表
 */
@Getter
@Setter
@ToString
@ApiModel("组织管理列表")
public class StructureListResponseVo implements Serializable {

    @ApiModelProperty(value = "公司ID", example = "1")
    private Long companyId;

    @ApiModelProperty(value = "公司名称", example = "中国移动通信有限责任公司")
    private String comName;

    @ApiModelProperty(value = "联系方式", example = "010-99997777")
    private String telephone;

    @ApiModelProperty(value = "是否为最后一项|0否，1是", example = "1")
    private Integer finallyStatus;

    @ApiModelProperty(value = "是否可以删除|0否，1是", example = "1")
    private Integer removeStatus;

    @ApiModelProperty(value = "工厂列表")
    List<FactoryListResponseVo> factoryList;
}
