package com.horseloft.jpa.vo.shelf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/2/7 上午9:52
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("货架列表响应")
public class ShelfListResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "货架编号", example = "123456")
    private String shelfCode;

    @ApiModelProperty(value = "货架坐标X", example = "1")
    private Integer axisX;

    @ApiModelProperty(value = "货架坐标Y", example = "2")
    private Integer axisY;

    @ApiModelProperty(value = "是否在地图上|true/false", example = "false")
    private Boolean mapStatus;

    @ApiModelProperty(value = "是否锁定|true/false", example = "false")
    private Boolean lockStatus;

    @ApiModelProperty(value = "是否正在移动|true/false", example = "false")
    private Boolean walkingStatus;

    @ApiModelProperty(value = "货箱列表")
    private List<ShelfListSlotVo> list;
}
