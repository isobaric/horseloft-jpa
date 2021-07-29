package com.horseloft.jpa.vo.shelf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/2/5 下午4:39
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("货架详情")
public class ShelfDetailResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "公司id", example = "1")
    private Long companyId;

    @ApiModelProperty(value = "工厂id", example = "1")
    private Long factoryId;

    @ApiModelProperty(value = "车间id", example = "1")
    private Long workshopId;

    @ApiModelProperty(value = "货架编号", example = "000001")
    private String shelfCode;

    @ApiModelProperty(value = "是否锁定", example = "true")
    private Boolean lockStatus;

    @ApiModelProperty(value = "是否在地图上", example = "false")
    private Boolean mapStatus;

    @ApiModelProperty(value = "是否正在移动", example = "false")
    private Boolean walkingStatus;

    @ApiModelProperty(value = "货架坐标X", example = "1")
    private Integer axisX;

    @ApiModelProperty(value = "货架坐标Y", example = "1")
    private Integer axisY;

    @ApiModelProperty(value = "货箱列表")
    private List<ShelfSlotVo> list;
}
