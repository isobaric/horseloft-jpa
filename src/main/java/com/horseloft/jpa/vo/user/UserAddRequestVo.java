package com.horseloft.jpa.vo.user;

import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Date: 2020/1/5 18:28
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ApiModel("添加|编辑用户")
public class UserAddRequestVo extends RequestVo {

    @ApiModelProperty(value = "ID|新增时为0，编辑时>0", example = "0/123456", required = true)
    @NotNull
    @Min(value = 0, message = "ID格式错误")
    private Long id;

    @ApiModelProperty(value = "账号|4-20个字的数字或大小写字母,字母开头", example = "zhangsan", required = true)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{4,20}$", message = "账号格式错误")
    private String account;

    @ApiModelProperty(value = "密码|编辑状态未编辑密码则传空", example = "123456", required = true)
    @NotNull
    private String password;

    @ApiModelProperty(value = "姓名", example = "张三", required = true)
    @NotNull
    @Pattern(regexp = "[a-zA-z0-9\\u4E00-\\u9FA5]{2,20}", message = "姓名格式错误")
    private String realName;

    @ApiModelProperty(value = "账号状态|新增时默认传true|false无效，true正常【无效账号不可登录】", example = "true", required = true)
    private boolean accountStatus = true;

    @ApiModelProperty(value = "工作状态|新增时默认传true|false离职，true在职【离职账号不可登录】", example = "true", required = true)
    private boolean jobStatus = true;

    @ApiModelProperty(value = "角色ID,逗号分隔的数字字符串", example = "1,2,3", required = true)
    @NotNull
    @Length(min = 1, message = "角色ID错误")
    private String roleId;

    @ApiModelProperty(value = "公司ID", example = "1", required = true)
    @NotNull
    @Min(value = 1, message = "公司ID错误")
    private Long companyId;

    @ApiModelProperty(value = "工厂ID|为0则用户属于公司级别", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "工厂ID错误")
    private Long factoryId;

    @ApiModelProperty(value = "车间ID|为0则用户属于工厂级别", example = "1", required = true)
    @NotNull
    @Min(value = 0, message = "车间ID错误")
    private Long workshopId;

    @ApiModelProperty(value = "性别|false女，true男，默认true", example = "true")
    private boolean sex = true;

    @ApiModelProperty(value = "手机号|允许空/1开头的11位数字", example = "13822223333")
    @NotNull
    private String mobile;

    @ApiModelProperty(value = "户籍|false农村，true城镇，默认false", example = "false")
    private boolean householdRegister = false;

    @ApiModelProperty(value = "身份证号|空或18位字符", example = "322456176411097653")
    @NotNull(message = "身份证格式错误")
    private String IdNumber;

    @ApiModelProperty(value = "生日|空或yyyy-MM-dd", example = "2020-12-12")
    private Date birthday;

    @ApiModelProperty(value = "入职日期|空或yyyy-MM-dd", example = "2020-12-12")
    private Date entryDate;

    @ApiModelProperty(value = "住址|长度<=50", example = "北京")
    @NotNull
    @Length(max = 50, message = "住址格式错误")
    private String address;

    @ApiModelProperty(value = "备注|长度<=250", example = "备注信息")
    @NotNull
    @Length(max = 250, message = "备注格式错误")
    private String remark;

    @ApiModelProperty(value = "银行账户|json数组,前端控制数据格式", example = "{}")
    @NotNull
    private String bankAccount;
}
