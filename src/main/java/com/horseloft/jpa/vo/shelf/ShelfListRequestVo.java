package com.horseloft.jpa.vo.shelf;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Date: 2020/2/7 上午9:41
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("货架列表请求")
public class ShelfListRequestVo extends RequestVo {

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

    @ApiModelProperty(value = "搜索类型|1货架编号，2物料编号，3生产订单编号，4物料名称", example = "1")
    private Integer searchType;

    @ApiModelProperty(value = "是否在地图上", example = "false")
    private Boolean mapStatus;

    @ApiModelProperty(value = "是否锁定", example = "false")
    private Boolean lockStatus;

    @ApiModelProperty(value = "是否空货箱", example = "false")
    private Boolean emptyStatus;
}
