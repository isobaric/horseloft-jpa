package com.horseloft.jpa.vo.structure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/6 18:35
 * User: YHC
 * Desc: 车间
 */
@Getter
@Setter
@ToString
@ApiModel("工厂列表信息")
public class FactoryListResponseVo implements Serializable {

    @ApiModelProperty(value = "工厂ID", example = "1")
    private Long factoryId;

    @ApiModelProperty(value = "工厂名称", example = "中国移动工厂")
    private String factoryName;

    @ApiModelProperty(value = "电话", example = "13533332222")
    private String telephone;

    @ApiModelProperty(value = "地址", example = "北京朝阳")
    private String address;

    @ApiModelProperty(value = "是否为最后一项|0否，1是", example = "1")
    private Integer finallyStatus;

    @ApiModelProperty(value = "是否可以删除|0否，1是", example = "1")
    private Integer removeStatus;

    @ApiModelProperty(value = "车间列表")
    List<WorkshopListResponseVo> workshopList;
}
