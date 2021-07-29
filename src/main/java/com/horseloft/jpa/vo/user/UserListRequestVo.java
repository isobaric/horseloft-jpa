package com.horseloft.jpa.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horseloft.jpa.vo.RequestVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Date: 2020/1/6 13:23
 * User: YHC
 * Desc: 用户列表
 */
@Getter
@Setter
@ApiModel("用户列表请求参数")
public class UserListRequestVo extends RequestVo {

    @ApiModelProperty(value = "页码|最小值1", example = "1")
    @NotNull
    @Min(value = 1, message = "页码格式错误")
    private Integer page;

    @ApiModelProperty(value = "每页显示的条数|最小值1", example = "1")
    @NotNull
    @Min(value = 1, message = "每页显示的条数格式错误")
    private Integer pageSize;

    @ApiModelProperty(value = "搜索框内容|默认空/null", example = "张三")
    private String searchValue;

    @ApiModelProperty(value = "角色ID|null查询全部", example = "1")
    private Long roleId;

    @ApiModelProperty(value = "账号状态|true/false null查询全部", example = "1")
    private Boolean accountStatus;

    @ApiModelProperty(value = "在职状态|true/false null查询全部", example = "1")
    private Boolean jobStatus;

    @ApiModelProperty(value = "公司ID|null查询全部", example = "1")
    private Long companyId;

    @ApiModelProperty(value = "工厂ID|null查询全部", example = "1")
    private Long factoryId;

    @ApiModelProperty(value = "车间ID|null查询全部", example = "1")
    private Long workshopId;

    @ApiModelProperty(value = "入职时间开始|null查询全部/yyyy-MM-dd", example = "2020-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date entryDateStart;

    @ApiModelProperty(value = "入职时间结束|null查询全部/yyyy-MM-dd", example = "2020-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date entryDateEnd;
}
