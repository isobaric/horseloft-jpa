package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.MaterialTypeService;
import com.horseloft.jpa.vo.*;
import com.horseloft.jpa.vo.material.MaterialTypeAddVo;
import com.horseloft.jpa.vo.material.MaterialTypeListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Date: 2020/2/8 下午2:23
 * User: YHC
 * Desc: 物料类型
 */
@RestController
@RequestMapping("/materialType")
@Api(tags = "物料类型")
public class MaterialTypeController {

    @Autowired
    private MaterialTypeService materialTypeService;

    @PostMapping("/add")
    @ApiOperation(value = "物料类型添加-编辑 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> materialTypeAddOrEdit(@Valid @RequestBody MaterialTypeAddVo params) {
        return materialTypeService.materialTypeAddOrEdit(params);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "物料类型详情", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<MaterialTypeAddVo> getMaterialTypeDetail(@Valid @RequestBody RequestIdVo params) {
        return materialTypeService.getMaterialTypeDetailById(params);
    }

    @PostMapping("/del")
    @ApiOperation(value = "物料类型删除", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> materialTypeDelete(@Valid @RequestBody RequestIdVo params) {
        return materialTypeService.materialTypeDeleteById(params);
    }

    @PostMapping("/list")
    @ApiOperation(value = "物料类型列表", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseListVo<MaterialTypeListVo>> getMaterialTypeList(@Valid @RequestBody RequestVo params) {
        return materialTypeService.getAllMaterialTypeList(params);
    }
}
