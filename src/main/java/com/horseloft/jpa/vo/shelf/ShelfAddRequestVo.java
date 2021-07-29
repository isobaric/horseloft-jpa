package com.horseloft.jpa.vo.shelf;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Date: 2020/2/4 下午6:12
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("添加货架")
public class ShelfAddRequestVo extends RequestVo {

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
    @ApiModelProperty(value = "货架编号", example = "123456", required = true)
    @Pattern(regexp = "^[0-9]{6}$", message = "货架编号错误")
    private String shelfCode;
}
