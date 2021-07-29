package com.horseloft.jpa.vo.device;

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
 * Date: 2020/1/28 下午4:52
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ColumnWidth(15)
@ApiModel("设备列表响应")
public class DeviceListResponseVo implements Serializable {

    @ExcelIgnore
    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1")
    private Long id;

    @ExcelProperty("设备编号")
    @ApiModelProperty(value = "编号|5位的数字", example = "12345")
    private String deviceCode;

    @ExcelProperty("设备名称")
    @ApiModelProperty(value = "名称", example = "一号设备")
    private String deviceName;

    @ExcelProperty("设备类型")
    @ApiModelProperty(value = "设备类型", example = "缝纫机")
    private String deviceType;

    @ExcelIgnore
    @ApiModelProperty(value = "设备坐标x|默认值0", example = "1")
    private Integer axisX;

    @ExcelIgnore
    @ApiModelProperty(value = "设备坐标y|默认值0", example = "1")
    private Integer axisY;

    @JsonIgnore
    @ExcelProperty("设备坐标")
    @ApiModelProperty(value = "设备坐标", example = "X(1)Y(2)")
    private String deviceAxis;

    @ExcelIgnore
    @ApiModelProperty(value = "是否锁定|是：true, 否：false", example = "false")
    private Boolean lockStatus;

    @JsonIgnore
    @ExcelProperty("是否锁定")
    @ApiModelProperty(hidden = true)
    private String lockStatusText;

    @JsonIgnore
    @ExcelProperty("设备状态")
    @ApiModelProperty(hidden = true)
    private String deviceStateText;

    @JsonIgnore
    @ExcelProperty("是否可移动")
    @ApiModelProperty(hidden = true)
    private String moveStatusText;

    @JsonIgnore
    @ExcelProperty("下方是否可通行")
    @ApiModelProperty(hidden = true)
    private String passStatusText;

    @ExcelIgnore
    @ApiModelProperty(value = "设备状态|0不可用，1正常，2待维修", example = "1")
    private Integer deviceState;

    @ExcelIgnore
    @ApiModelProperty(value = "是否可移动|是：true, 否：false", example = "true")
    private Boolean moveStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "下方是否通行|是：true, 否：false", example = "true")
    private Boolean passStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "是否正在移动|是：true, 否：false", example = "true")
    private Boolean walkingStatus;

    @ExcelProperty("组织架构")
    @ApiModelProperty(value = "组织架构", example = "A车间A工厂A公司")
    private String structure;

}
