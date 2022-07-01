package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.*;
import com.horseloft.jpa.vo.RequestVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.device.DeviceTypeResponseVo;
import com.horseloft.jpa.vo.material.MaterialTypeCommonVo;
import com.horseloft.jpa.vo.material.MaterialUnitCommonVo;
import com.horseloft.jpa.vo.node.NodeInfoResponseVo;
import com.horseloft.jpa.vo.role.RoleInfoResponseVo;
import com.horseloft.jpa.vo.station.StationTypeResponseVo;
import com.horseloft.jpa.vo.structure.DistrictResponseVo;
import com.horseloft.jpa.vo.structure.StructureListResponseVo;
import com.horseloft.jpa.vo.structure.UserStructureListResponseVo;
import com.horseloft.jpa.vo.user.PasswordEditRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Date: 2020/1/7 10:01
 * User: YHC
 * Desc: 通用接口
 */
@RestController
@RequestMapping("/common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceNodeService resourceNodeService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private PublicService publicService;

    @Autowired
    private MaterialTypeService materialTypeService;

    @Autowired
    private MaterialUintService materialUintService;

    @PostMapping("/logout")
    @ApiOperation(value = "退出登录 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> userLogout(@RequestBody RequestVo params) {
        return userService.userLogout(params);
    }

    @PostMapping("/editPassword")
    @ApiOperation(value = "修改密码 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> passwordModify(@Valid @RequestBody PasswordEditRequestVo params) {
        return userService.userPasswordModify(params);
    }

    @PostMapping("/companyList")
    @ApiOperation(value = "公司组织管理列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<StructureListResponseVo>> getCompanyList(@RequestBody RequestVo params) {
        return companyService.structureList(params);
    }

    @PostMapping("/userStructure")
    @ApiOperation(value = "用户组织结构列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<UserStructureListResponseVo>> getUserStructureList(@RequestBody RequestVo params) {
        return userService.userStructureList(params);
    }

    @PostMapping("/nodeList")
    @ApiOperation(value = "用户侧边栏列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<NodeInfoResponseVo>> getNodeList(@RequestBody RequestVo params) {
        return resourceNodeService.getUserNodeList(params);
    }

    @PostMapping("/roleList")
    @ApiOperation(value = "角色列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<RoleInfoResponseVo>> getRoleList(@RequestBody RequestVo params) {
        return roleService.getRoleList(params);
    }

    @PostMapping("/districtList")
    @ApiOperation(value = "行政区列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<DistrictResponseVo>> getDistrictList() {
        return districtService.getDistrictAll();
    }

    @PostMapping("/deviceTypeList")
    @ApiOperation(value = "设备类型列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<DeviceTypeResponseVo>> getDeviceTypeList() {
        return deviceTypeService.getDeviceTypeList();
    }

    @PostMapping("/stationTypeList")
    @ApiOperation(value = "工位类型列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<StationTypeResponseVo>> getStationTypeList() {
        return publicService.getAllStationTypeList();
    }

    @PostMapping("/materialTypeList")
    @ApiOperation(value = "物料类型列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<MaterialTypeCommonVo>> getMaterialTypeList() {
        return materialTypeService.getCommonMaterialTypeList();
    }

    @PostMapping("/materialUnitList")
    @ApiOperation(value = "物料主单位列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<MaterialUnitCommonVo>> getMaterialUnitList() {
        return materialUintService.getCommonMaterialUnitList();
    }
}
