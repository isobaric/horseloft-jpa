package com.horseloft.jpa.vo.station;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/4 上午11:08
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ApiModel("工位类型列表")
public class StationTypeResponseVo implements Serializable {

    @ApiModelProperty(value = "ID", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "工位类型名称", example = "车工", required = true)
    private String typeName;
}
