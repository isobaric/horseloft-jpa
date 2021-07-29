package com.horseloft.jpa.vo.structure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/1/6 18:37
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("车间列表信息")
public class WorkshopListResponseVo implements Serializable {

    @ApiModelProperty(value = "车间ID", example = "1")
    private Long workshopId;

    @ApiModelProperty(value = "电话", example = "13533332222")
    private String telephone;

    @ApiModelProperty(value = "车间名称", example = "A车间")
    private String workshopName;

    @ApiModelProperty(value = "是否为最后一项|0否，1是", example = "1")
    private Integer finallyStatus;

    @ApiModelProperty(value = "是否可以删除|0否，1是", example = "1")
    private Integer removeStatus;
}
