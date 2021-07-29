package com.horseloft.jpa.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2020/1/4 14:27
 * User: YHC
 * Desc: 列表响应|long类型默认为0
 */
@Getter
@Setter
@ApiModel("列表响应")
public class ResponseListVo<T> implements Serializable {

    @ApiModelProperty(value = "当前页码", example = "1")
    private long page;

    @ApiModelProperty(value = "每页条数", example = "20")
    private long pageSize;

    @ApiModelProperty(value = "数据总条数", example = "1000")
    private long total;

    @ApiModelProperty(value = "数据总分页数", example = "100")
    private long totalPage;

    @ApiModelProperty(value = "数据列表")
    private List<T> list;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
