package com.horseloft.jpa.vo.structure;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * Date: 2020/1/6 17:15
 * User: YHC
 * Desc: 添加公司
 */
@Setter
@Getter
@ApiModel("公司信息")
public class CompanyAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|0为新增，>0为添加", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "ID错误")
    private Long id;

    @ApiModelProperty(value = "公司名称", example = "中国移动通信有限责任公司", required = true)
    @Length(min = 4, max = 250, message = "公司名称不能少于4个字符，不超过250个字符")
    @NotNull
    private String comName;

    @ApiModelProperty(value = "公司编号", example = "CMCC", required = true)
    @Pattern(regexp = "^[a-zA-Z]{2,}$", message = "公司编号不少于2个字母")
    @Length(max = 20, message = "公司编号不能超过20个字母")
    @NotNull
    private String comCode;

    @ApiModelProperty(value = "公司简称", example = "中国移动", required = true)
    @NotBlank(message = "公司简称不能为空")
    @Length(max = 6, message = "公司简称不能超过6个字符")
    private String shortName;

    @ApiModelProperty(value = "联系方式", example = "010-55557777")
    @NotBlank(message = "联系方式格式错误")
    @Length(max = 20, message = "联系方式格式错误")
    private String telephone;

    @ApiModelProperty(value = "法人", example = "张三")
    @NotNull
    private String legalPerson;

    @ApiModelProperty(value = "网址", example = "http://www.baidu.com")
    @NotNull
    private String website;

    @ApiModelProperty(value = "公司地址", example = "北京海淀")
    @NotNull
    private String address;

    @ApiModelProperty(value = "邮箱", example = "baidu@qq.com")
    @NotNull
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "规模|默认传0", example = "100")
    @NotNull
    @Min(value = 0, message = "规模格式错误")
    private Integer employeeNumber;

    @ApiModelProperty(value = "行业", example = "互联网")
    @NotNull
    private String industry;

    @ApiModelProperty(value = "省份ID|默认传0", example = "1")
    @NotNull
    @Min(value = 0, message = "省份格式错误")
    private Long provinceId;

    @ApiModelProperty(value = "城市ID|默认传0", example = "2")
    @NotNull
    @Min(value = 0, message = "市格式错误")
    private Long cityId;

    @ApiModelProperty(value = "区县ID|默认传0", example = "3")
    @NotNull
    @Min(value = 0, message = "区县格式错误")
    private Long areaId;

    @ApiModelProperty(value = "备注", example = "备注信息...")
    @NotNull
    private String remark;
}
