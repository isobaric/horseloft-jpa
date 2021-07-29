package com.horseloft.jpa.vo.station;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Date: 2020/2/4 下午1:21
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("工位列表请求")
public class StationListRequestVo extends RequestVo {

    @NotNull
    @ApiModelProperty(value = "页码|最小值1", example = "1")
    @Min(value = 1, message = "页码格式错误")
    private Integer page;

    @NotNull
    @ApiModelProperty(value = "每页显示的条数|最小值1", example = "1")
    @Min(value = 1, message = "每页显示的条数格式错误")
    private Integer pageSize;

    @ApiModelProperty(value = "搜索值", example = "abc")
    private String searchValue;

    @ApiModelProperty(value = "工位类型id", example = "1")
    private Long stationTypeId;

    @ApiModelProperty(value = "公司id|null全部", example = "1")
    private Long companyId;

    @ApiModelProperty(value = "工厂id|null全部", example = "1")
    private Long factoryId;

    @ApiModelProperty(value = "车间id|null全部", example = "1")
    private Long workshopId;

    @ApiModelProperty(value = "下方是否可通行|null全部", example = "false")
    private Boolean passStatus;
}
