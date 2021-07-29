package com.horseloft.jpa.vo.role;

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
 * Date: 2020/1/6 14:44
 * User: YHC
 * Desc: 新增角色
 */
@Getter
@Setter
@ApiModel("新增|编辑角色")
public class RoleAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|新增时为0", example = "0", required = true)
    @NotNull
    @Min(value = 0, message = "ID格式错误")
    private Long id;

    @ApiModelProperty(value = "角色名称|10个字，的汉字或大小写字母", example = "厂长", required = true)
    @NotNull
    @Pattern(regexp = "([a-zA-Z]|[\\u4E00-\\u9FA5]){1,10}", message = "角色名称格式错误")
    private String roleName;

    @ApiModelProperty(value = "读写状态|0只读，1读写", example = "1", required = true)
    @NotNull
    @Range(min = 0, max = 1, message = "读写状态错误")
    private Integer rolePower;

    @ApiModelProperty(value = "资源节点ID,逗号分隔的字符串", example = "1,2,3")
    private String roleNode;

    @ApiModelProperty(value = "头像id", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "头像错误")
    private Long rolePictureId;
}
