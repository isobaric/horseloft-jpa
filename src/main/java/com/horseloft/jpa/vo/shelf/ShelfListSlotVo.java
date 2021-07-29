package com.horseloft.jpa.vo.shelf;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Date: 2020/2/7 上午10:01
 * User: YHC
 * Desc:
 */
@Getter
@Setter
public class ShelfListSlotVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "货箱编号", example = "HX-000001-R")
    private String slotCode;

    @ApiModelProperty(value = "物料名称", example = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料编号", example = "ABVDDD")
    private String materialCode;

    @ApiModelProperty(value = "物料数量", example = "100")
    private Integer materialNumber;

    @ApiModelProperty(value = "生产订单编号", example = "123123123123123123")
    private String productionOrderCode;

    //物料状态
    @ApiModelProperty(value = "当前物料状态 0正常 1返修 2换片", example = "0")
    private Integer materialState;
}
