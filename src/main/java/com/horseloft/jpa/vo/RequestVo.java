package com.horseloft.jpa.vo;

import com.horseloft.jpa.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Date: 2020/1/4 14:30
 * User: YHC
 * Desc: 基础请求VO，用于被请求接口的VO集成或独立使用
 */
@Getter
@Setter
@ApiModel("请求")
public class RequestVo implements Serializable {

    @ApiModelProperty(value = "解析TOKEN获得的当前登录用户的ID", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "当前用户信息,jpa_user表数据", hidden = true)
    private User user;

    @ApiModelProperty(value = "远程地址", hidden = true)
    private String remoteAddr;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
