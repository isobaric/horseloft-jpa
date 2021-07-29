package com.horseloft.jpa.vo.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 2020/1/6 11:48
 * User: YHC
 * Desc: 账号列表
 */
@Setter
@Getter
@ToString
@ColumnWidth(15)
@ApiModel("账号列表")
public class UserListResponseVo implements Serializable {

    @ExcelProperty("ID编号")
    @ApiModelProperty(value = "ID", example = "123456")
    private Long id;

    @ExcelProperty("账号")
    @ApiModelProperty(value = "账号", example = "zhangsan")
    private String account;

    @ExcelProperty("角色")
    @ApiModelProperty(value = "角色", example = "工艺师，厂长")
    private String roleName;

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "姓名", example = "张三")
    private String realName;

    @ColumnWidth(50)
    @ExcelProperty("组织架构所属")
    @ApiModelProperty(value = "组织架构", example = "A车间A工厂A公司")
    private String structure;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("入职时间")
    @ApiModelProperty(value = "入职时间|空或yyyy-MM-dd", example = "2020-12-12")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date entryDate;

    @ExcelIgnore
    @ApiModelProperty(value = "账号状态|false无效，true正常【无效账号不可登录】", example = "true")
    private Boolean accountStatus;

    @ExcelIgnore
    @ApiModelProperty(value = "工作状态|false离职，true在职【离职账号不可登录】", example = "true")
    private Boolean jobStatus;

    @JsonIgnore
    @ExcelProperty("账号状态")
    @ApiModelProperty(hidden = true)
    private String accountStatusTest;

    @JsonIgnore
    @ExcelProperty("工作状态")
    @ApiModelProperty(hidden = true)
    private String jobStatusText;
}

