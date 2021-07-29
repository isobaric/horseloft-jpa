package com.horseloft.jpa.vo.station;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/4 下午12:15
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ColumnWidth(15)
@ApiModel("工位列表响应")
public class StationListResponseVo implements Serializable {

    @ExcelIgnore
    @ApiModelProperty(value = "ID", example = "1", required = true)
    private Long id;

    @ExcelProperty("工位编号")
    @ApiModelProperty(value = "工位编号", example = "123456", required = true)
    private String stationCode;

    @ExcelIgnore
    @ApiModelProperty(value = "下方是否可通行|true/false", example = "false", required = true)
    private Boolean passStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "是否在地图上|true/false", example = "false", required = true)
    private Boolean mapStatus;

    @ExcelProperty("工位类型")
    @ApiModelProperty(value = "工位类型", example = "车工", required = true)
    private String stationTypeName;

    @ExcelProperty("组织架构")
    @ApiModelProperty(value = "组织架构", example = "A车间A工厂A公司")
    private String structure;

    @JsonIgnore
    @ExcelProperty("工位坐标")
    @ApiModelProperty(value = "工位坐标", example = "X（12）Y（13）")
    private String axis;

    @ExcelIgnore
    @ApiModelProperty(value = "坐标x|默认值0", example = "1")
    private Integer axisX;

    @ExcelIgnore
    @ApiModelProperty(value = "坐标y|默认值0", example = "1")
    private Integer axisY;

    @JsonIgnore
    @ExcelProperty("是否在地图上")
    private String mapStatusText;

    @JsonIgnore
    @ExcelProperty("下方是否可通行")
    private String passStatusText;

}
