package com.horseloft.jpa.vo.station;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Date: 2020/2/3 下午4:41
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("工位编辑-添加")
public class StationEditAddVo extends RequestVo {

    @NotNull
    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1", required = true)
    @Min(value = 0, message = "ID错误")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "公司id", example = "1", required = true)
    @Min(value = 1, message = "公司id格式错误")
    private Long companyId;

    @NotNull
    @ApiModelProperty(value = "工厂id", example = "1", required = true)
    @Min(value = 0, message = "工厂id格式错误")
    private Long factoryId;

    @NotNull
    @ApiModelProperty(value = "车间id", example = "1", required = true)
    @Min(value = 0, message = "车间id格式错误")
    private Long workshopId;

    @NotNull
    @ApiModelProperty(value = "工位编号", example = "123456", required = true)
    @Pattern(regexp = "^[0-9]{6}$", message = "工位编号错误")
    private String stationCode;

    @NotNull
    @ApiModelProperty(value = "下方是否可通行|true/false", example = "false", required = true)
    private Boolean passStatus;

    @NotNull
    @ApiModelProperty(value = "工位类型id|1-8", example = "1", required = true)
    @Range(min = 1, max = 8, message = "工位类型错误")
    private Long stationTypeId;
}
