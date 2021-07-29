package com.horseloft.jpa.vo.device;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Date: 2020/1/28 下午5:00
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("设备列表请求")
public class DeviceListRequestVo extends RequestVo {

    @ApiModelProperty(value = "页码|最小值1", example = "1")
    @Min(value = 1, message = "页码格式错误")
    @NotNull
    private Integer page;

    @ApiModelProperty(value = "每页显示的条数|最小值1", example = "1")
    @Min(value = 1, message = "每页显示的条数格式错误")
    @NotNull
    private Integer pageSize;

    @ApiModelProperty(value = "搜索值null全部|设备编号或名称", example = "abc")
    private String searchValue;

    @ApiModelProperty(value = "设备类型id|null全部", example = "1")
    private Long deviceTypeId;

    @ApiModelProperty(value = "公司id|null全部", example = "1")
    private Long companyId;

    @ApiModelProperty(value = "工厂id|null全部", example = "1")
    private Long factoryId;

    @ApiModelProperty(value = "车间id|null全部", example = "1")
    private Long workshopId;

    @ApiModelProperty(value = "设备状态|null全部，0不可用，1正常，2待维修", example = "1")
    private Integer deviceState;
}
