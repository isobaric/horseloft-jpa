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
 * Date: 2020/1/6 17:41
 * User: YHC
 * Desc: 新增编辑工厂信息
 */
@Setter
@Getter
@ApiModel("工厂信息")
public class FactoryAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "ID格式错误")
    private Long id;

    @ApiModelProperty(value = "公司ID", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "工厂ID错误")
    private Long companyId;

    @ApiModelProperty(value = "工厂名称|长度>4", example = "第一车间", required = true)
    @NotNull
    @Length(min = 4, max = 250, message = "工厂名称不能少于4个字符，不超过250个字符")
    private String factoryName;

    @ApiModelProperty(value = "工厂编号", example = "ABC", required = true)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,}$", message = "工厂编号不少于2个字母")
    @Length(max = 20, message = "工厂编号不能超过20个字母")
    private String factoryCode;

    @ApiModelProperty(value = "联系方式", example = "13533332222", required = true)
    @NotBlank(message = "联系方式不能为空")
    private String telephone;

    @ApiModelProperty(value = "地址", example = "北京朝阳", required = true)
    @NotBlank(message = "地址不能为空")
    @Length(max = 100, message = "地址不能超过100个字符")
    private String address;

    @ApiModelProperty(value = "描述", example = "abc", required = true)
    @NotNull
    @Length(max = 150, message = "描述不能超过150个字符")
    private String remark;

}
