package com.horseloft.jpa.vo.structure;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Date: 2020/1/6 17:57
 * User: YHC
 * Desc: 新增 编辑车间
 */
@Getter
@Setter
@ApiModel("车间信息")
public class WorkshopAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "ID格式错误")
    private Long id;

    @ApiModelProperty(value = "工厂ID", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "工厂ID错误")
    private Long factoryId;

    @ApiModelProperty(value = "车间名称", example = "第一车间", required = true)
    @NotNull
    @Length(min = 4, max = 255, message = "车间名称不能少于4个字符，不超过255个字符")
    private String workshopName;

    @ApiModelProperty(value = "车间编号", example = "ABC", required = true)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,}$", message = "车间编号不少于2个字母")
    @Length(max = 20, message = "车间编号不能超过20个字母")
    private String workshopCode;

    @ApiModelProperty(value = "联系方式", example = "13533332222", required = true)
    @NotBlank(message = "联系方式不能为空")
    private String telephone;

    @ApiModelProperty(value = "描述", example = "abc")
    @NotNull
    @Length(max = 150, message = "描述不能超过150个字符")
    private String remark;
}
