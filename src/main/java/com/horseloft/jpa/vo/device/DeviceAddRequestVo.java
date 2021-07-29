package com.horseloft.jpa.vo.device;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Date: 2020/1/28 下午2:50
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("设备新增、编辑")
public class DeviceAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "ID格式错误")
    private Long id;

    @ApiModelProperty(value = "公司id", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "公司id格式错误")
    private Long companyId;

    @ApiModelProperty(value = "工厂id", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "工厂id格式错误")
    private Long factoryId;

    @ApiModelProperty(value = "车间id", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "车间id格式错误")
    private Long workshopId;

    @ApiModelProperty(value = "编号|5位的数字", example = "12345", required = true)
    @NotNull
    @Pattern(regexp = "^[0-9]{5}$", message = "编号格式错误")
    private String deviceCode;

    @ApiModelProperty(value = "名称", example = "一号设备", required = true)
    @NotNull
    @Length(min = 1, max = 20, message = "设备名称长度错误")
    private String deviceName;

    @ApiModelProperty(value = "描述", example = "abc", required = true)
    @NotNull
    @Length(max = 250, message = "描述不能超过250个字符")
    private String remark;

    @ApiModelProperty(value = "下方是否通行|是：true, 否：false", example = "true", required = true)
    @NotNull
    private Boolean passStatus;

    @ApiModelProperty(value = "是否可移动|是：true, 否：false", example = "true", required = true)
    @NotNull
    private Boolean moveStatus;

    @ApiModelProperty(value = "设备状态|0不可用，1正常，2待维修", example = "1", required = true)
    @NotNull
    private Integer deviceState;

    @ApiModelProperty(value = "设备类型id", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "设备类型id格式错误")
    private Long deviceTypeId;

    @ApiModelProperty(value = "设备类型属性", required = true)
    @NotNull
    private List<DeviceTypeAttrValueVo> deviceTypeAttr;
}
