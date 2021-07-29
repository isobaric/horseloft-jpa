package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.MaterialUintService;
import com.horseloft.jpa.vo.*;
import com.horseloft.jpa.vo.material.MaterialUnitAddVo;
import com.horseloft.jpa.vo.material.MaterialUnitListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Date: 2020/2/9 上午10:45
 * User: YHC
 * Desc: 物料主单位
 */
@RestController
@RequestMapping("/materialUnit")
@Api(tags = "物料主单位")
public class MaterialUnitController {

    @Autowired
    private MaterialUintService materialUintService;

    @PostMapping("/add")
    @ApiOperation(value = "添加-编辑 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> materialUnitAddOrEdit(@Valid @RequestBody MaterialUnitAddVo params) {
        return materialUintService.materialUnitBuild(params);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "详情", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<MaterialUnitAddVo> getMaterialUnitDetail(@Valid @RequestBody RequestIdVo params) {
        return materialUintService.getMaterialUnitDetailById(params);
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> materialUnitDelete(@Valid @RequestBody RequestIdVo params) {
        return materialUintService.materialUnitDeleteById(params);
    }

    @PostMapping("/list")
    @ApiOperation(value = "列表", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseListVo<MaterialUnitListVo>> getMaterialUnitList(@Valid @RequestBody RequestVo params) {
        return materialUintService.getMaterialUnitList(params);
    }
}
