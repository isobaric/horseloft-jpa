package com.horseloft.jpa.service;

import com.horseloft.jpa.db.dao.ResourceNodeDao;
import com.horseloft.jpa.db.dao.RoleDao;
import com.horseloft.jpa.db.dao.UserDao;
import com.horseloft.jpa.db.entity.ResourceNode;
import com.horseloft.jpa.db.entity.Role;
import com.horseloft.jpa.db.entity.User;
import com.horseloft.jpa.utils.ConvertUtils;
import com.horseloft.jpa.utils.DataUtils;
import com.horseloft.jpa.utils.StringUtils;
import com.horseloft.jpa.vo.RequestIdVo;
import com.horseloft.jpa.vo.RequestVo;
import com.horseloft.jpa.vo.ResponseIdVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.role.RoleAddRequestVo;
import com.horseloft.jpa.vo.role.RoleInfoResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2020/1/20 上午11:33
 * User: YHC
 * Desc: 角色
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ResourceNodeDao resourceNodeDao;

    /**
     * 角色添加、编辑
     * @param params
     * @return
     */
    public ResponseVo<ResponseIdVo> roleEditAndAdd(RoleAddRequestVo params) {
        Role role = roleDao.getTotalRoleByRoleName(params.getRoleName());
        if (params.getId() > 0) {
            //超级管理员和工人角色不可编辑
            if (DataUtils.isDefaultRole(params.getId())) {
                return ResponseVo.ofError("当前角色不可编辑");
            }
            //超级管理员信息不能编辑
            if (params.getId().equals(1L)) {
                return ResponseVo.ofError("操作异常，请刷新页面重试");
            }
            if (role != null && !role.getId().equals(params.getId())) {
                return ResponseVo.ofError("角色名称已存在");
            }
        } else {
            if (role != null) {
                return ResponseVo.ofError("角色名称已存在");
            }
        }
        //节点 可能为空
        if (!params.getRoleNode().isEmpty()) {
            List<Long> nodeIdList = ConvertUtils.strToListLong(params.getRoleNode(), ",");
            if (nodeIdList.isEmpty()) {
                return ResponseVo.ofError("分配的权限错误，请刷新页面重试");
            }
            List<ResourceNode> resourceNodeList = resourceNodeDao.getNodeByIdList(nodeIdList);
            if (resourceNodeList.size() != nodeIdList.size()) {
                return ResponseVo.ofError("分配的权限异常，请刷新页面重试");
            }
        }

        //角色节点后面需要补充逗号,用于权限验证的模糊查询
        if (!params.getRoleNode().endsWith(",")) {
            params.setRoleNode(params.getRoleNode() + ",");
        }

        Role roleEdit = new Role();
        if (params.getId() > 0) {
            roleEdit = roleDao.getActiveRoleById(params.getId());
            if (roleEdit == null) {
                return ResponseVo.ofError("角色信息异常，请刷新页面重试");
            }
        }
        roleEdit.setRoleName(params.getRoleName());
        roleEdit.setRolePower(params.getRolePower());
        roleEdit.setRolePictureId(params.getRolePictureId());
        roleEdit.setRoleNode(params.getRoleNode());
        roleDao.save(roleEdit);

        ResponseIdVo responseIdVo = new ResponseIdVo();
        responseIdVo.setId(roleEdit.getId());

        return ResponseVo.ofSuccess(responseIdVo);
    }

    /**
     * 角色删除
     * @param params
     * @return
     */
    public ResponseVo<String> roleDeleteById(RequestIdVo params) {
        if (DataUtils.isDefaultRole(params.getId())) {
            return ResponseVo.ofError("默认角色不能删除");
        }
        //查询角色下的用户
        User user = userDao.getActiveSingleUserByRoleId("%" + params.getId() + ",%");
        if (user != null) {
            return ResponseVo.ofError("此角色正在使用,无法删除！");
        }
        Role role = roleDao.getActiveRoleById(params.getId());
        if (role == null) {
            return ResponseVo.ofError("角色信息异常，请刷新页面重试");
        }
        role.setRoleName(StringUtils.appendRandom(role.getRoleName()));
        role.setDeleteStatus(true);
        roleDao.save(role);

        return ResponseVo.ofSuccess();
    }

    /**
     * 角色列表
     * @param params
     * @return
     */
    public ResponseVo<List<RoleInfoResponseVo>> getRoleList(RequestVo params) {
        List<Role> roleList = roleDao.getActiveRoleExcludeAdmins();
        if (roleList.isEmpty()) {
            return ResponseVo.ofSuccess(new ArrayList<>());
        }

        List<RoleInfoResponseVo> voList = new ArrayList<>();
        for(Role role : roleList) {
            RoleInfoResponseVo vo = new RoleInfoResponseVo();
            BeanUtils.copyProperties(role, vo);
            //查询角色下的用户|本角色无相关联的账户可以删除
            User user = userDao.getActiveSingleUserByRoleId("%" + role.getId() + ",%");
            if (user == null) {
                vo.setRemoveStatus(1);
            } else {
                vo.setRemoveStatus(0);
            }
            voList.add(vo);
        }

        return ResponseVo.ofSuccess(voList);
    }

    /**
     * 角色详情
     * @param params
     * @return
     */
    public ResponseVo<RoleAddRequestVo> getRoleDetailById(RequestIdVo params) {
        Role role = roleDao.getActiveRoleById(params.getId());
        if (role == null) {
            return ResponseVo.ofError("角色信息不存在");
        }
        RoleAddRequestVo roleAddRequestVo = new RoleAddRequestVo();
        BeanUtils.copyProperties(role, roleAddRequestVo);
        //去除最后一个逗号
        if (!role.getRoleNode().isEmpty()) {
            roleAddRequestVo.setRoleNode(StringUtils.trimWithEnd(role.getRoleNode(), ","));
        }
        return ResponseVo.ofSuccess(roleAddRequestVo);
    }
}
