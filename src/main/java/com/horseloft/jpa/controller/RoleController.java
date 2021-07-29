package com.horseloft.jpa.controller;

import com.horseloft.jpa.service.ResourceNodeService;
import com.horseloft.jpa.service.RoleService;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.RequestVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.node.NodeInfoResponseVo;
import com.horseloft.jpa.vo.role.RoleAddRequestVo;
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
 * Date: 2020/1/6 14:02
 * User: YHC
 * Desc: 角色模块
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色模块")
public class RoleController {

    @Autowired
    private ResourceNodeService resourceNodeService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    @ApiOperation(value = "新增/编辑角色 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<ResponseIdVo> roleAdd(@Valid @RequestBody RoleAddRequestVo params) {
        return roleService.roleEditAndAdd(params);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "角色详情 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<RoleAddRequestVo> getRoleDetail(@Valid @RequestBody RequestIdVo params) {
        return roleService.getRoleDetailById(params);
    }

    @PostMapping("/del")
    @ApiOperation(value = "角色删除 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<String> roleDelete(@Valid @RequestBody RequestIdVo params) {
        return roleService.roleDeleteById(params);
    }

    @PostMapping("/resourceList")
    @ApiOperation(value = "角色资源列表 [已完成]", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public ResponseVo<List<NodeInfoResponseVo>> getRoleResourceList(@RequestBody RequestVo params) {
        return resourceNodeService.getAllResourceNodeList(params);
    }
}
