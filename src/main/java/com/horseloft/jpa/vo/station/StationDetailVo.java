package com.horseloft.jpa.vo.station;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/4 上午10:02
 * User: YHC
 * Desc: 工位详情
 */
@Getter
@Setter
@ToString
@ApiModel("工位详情")
public class StationDetailVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "公司id", example = "1", required = true)
    private Long companyId;

    @ApiModelProperty(value = "工厂id", example = "1", required = true)
    private Long factoryId;

    @ApiModelProperty(value = "车间id", example = "1", required = true)
    private Long workshopId;

    @ApiModelProperty(value = "工位编号", example = "123456", required = true)
    private String stationCode;

    @ApiModelProperty(value = "下方是否可通行|true/false", example = "false", required = true)
    private Boolean passStatus;

    @ApiModelProperty(value = "工位类型id|1-8", example = "1", required = true)
    private Long stationTypeId;

    @ApiModelProperty(value = "产成品区", example = "{'x':1, 'y':2}", required = true)
    private String productArea;

    @ApiModelProperty(value = "设备区", example = "{'x':1, 'y':2}", required = true)
    private String deviceArea;

    @ApiModelProperty(value = "设备队列区", example = "[{'x':1, 'y':2},{'x':1, 'y':2}]", required = true)
    private String deviceQueueArea;

    @ApiModelProperty(value = "组件队列区", example = "[{'x':1, 'y':2},{'x':1, 'y':2}]", required = true)
    private String componentQueueArea;
}
