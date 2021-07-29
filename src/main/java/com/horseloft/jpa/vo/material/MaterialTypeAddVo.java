package com.horseloft.jpa.vo.material;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Date: 2020/2/8 下午3:12
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("物料类型编辑-新增")
public class MaterialTypeAddVo extends RequestVo {

    @NotNull
    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "0", required = true)
    @Min(value = 0, message = "ID错误")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "编号", example = "abc", required = true)
    @Length(min = 1, max = 20, message = "编号格式错误")
    private String typeCode;

    @NotNull
    @ApiModelProperty(value = "名称", example = "abc", required = true)
    @Length(min = 1, max = 20, message = "名称格式错误")
    private String typeName;

    @NotNull
    @ApiModelProperty(value = "描述", example = "abc", required = true)
    @Length(max = 250, message = "描述格式错误")
    private String remark;

    @NotNull
    @ApiModelProperty(value = "属性列表")
    @Size(max = 5, message = "属性不能超过5个")
    List<MaterialTypeAttrAddVo> attrList;

}
