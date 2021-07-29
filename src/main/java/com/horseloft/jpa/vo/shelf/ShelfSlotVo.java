package com.horseloft.jpa.vo.shelf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Date: 2020/2/5 下午4:46
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("货箱信息")
public class ShelfSlotVo extends ShelfListSlotVo {

    @ApiModelProperty(value = "质检状态 0免检、1已质检、2未质检", example = "1")
    private Integer checkState;

    @ApiModelProperty(value = "提交缺陷工人", example = "张三")
    private String reportUserName;

    @ApiModelProperty(value = "问题工序生产订单编号", example = "123123123123123123")
    private String handleOrderCode;

    @ApiModelProperty(value = "返修工人", example = "张三")
    private String handleUserName;

}
