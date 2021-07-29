package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.CompanyService;
import com.horseloft.jpa.service.FactoryService;
import com.horseloft.jpa.service.WorkshopService;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.structure.CompanyAddRequestVo;
import com.horseloft.jpa.vo.structure.FactoryAddRequestVo;
import com.horseloft.jpa.vo.structure.WorkshopAddRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Date: 2020/1/6 17:08
 * User: YHC
 * Desc: 组织管理
 */
@RestController
@RequestMapping("/structure")
@Api(tags = "组织管理")
public class StructureController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private WorkshopService workshopService;

    @PostMapping("/companyAdd")
    @ApiOperation(value = "新增/编辑公司 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> companyAdd(@Valid @RequestBody CompanyAddRequestVo params) {
        return companyService.companyBuild(params);
    }

    @PostMapping("/company")
    @ApiOperation(value = "公司详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<CompanyAddRequestVo> getCompanyInfoById(@Valid @RequestBody RequestIdVo params) {
        return companyService.companyBaseInfoById(params);
    }

    @PostMapping("/companyDel")
    @ApiOperation(value = "删除公司 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> companyDelete(@Valid @RequestBody RequestIdVo params) {
        return companyService.companyDeleteById(params);
    }

    @PostMapping("/factoryAdd")
    @ApiOperation(value = "新增/编辑工厂 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> factoryAdd(@Valid @RequestBody FactoryAddRequestVo params) {
        return factoryService.factoryAddAndEdit(params);
    }

    @PostMapping("/factory")
    @ApiOperation(value = "工厂详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<FactoryAddRequestVo> getFactoryInfo(@Valid @RequestBody RequestIdVo params) {
        return factoryService.factoryInfoById(params);
    }

    @PostMapping("/factoryDel")
    @ApiOperation(value = "删除工厂 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> factoryDelete(@Valid @RequestBody RequestIdVo params) {
        return factoryService.factoryDeleteById(params);
    }

    @PostMapping("/workshopAdd")
    @ApiOperation(value = "新增/编辑车间 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> workshopAdd(@Valid @RequestBody WorkshopAddRequestVo params) {
        return workshopService.workshopEditAndAdd(params);
    }

    @PostMapping("/workshop")
    @ApiOperation(value = "车间详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<WorkshopAddRequestVo> getWorkshopInfo(@Valid @RequestBody WorkshopAddRequestVo params) {
        return workshopService.workshopInfoById(params);
    }

    @PostMapping("/workshopDel")
    @ApiOperation(value = "删除车间 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> workshopDelete(@Valid @RequestBody RequestIdVo params) {
        return workshopService.workshopDeleteById(params);
    }
}
