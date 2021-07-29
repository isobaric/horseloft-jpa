package com.horseloft.jpa.service;

import com.horseloft.jpa.constant.RoleConstant;
import com.horseloft.jpa.db.dao.ResourceNodeDao;
import com.horseloft.jpa.db.dao.RoleDao;
import com.horseloft.jpa.db.entity.ResourceNode;
import com.horseloft.jpa.db.entity.Role;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.utils.ConvertUtils;
import com.horseloft.jpa.vo.RequestVo;
import com.horseloft.jpa.vo.ResponseVo;
import com.horseloft.jpa.vo.node.NodeInfoResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 2020/1/20 上午10:07
 * User: YHC
 * Desc: 资源/节点
 */
@Service
public class ResourceNodeService {

    @Autowired
    private ResourceNodeDao resourceNodeDao;

    @Autowired
    private RoleDao roleDao;

    /**
     * 用户侧边栏/节点列表
     * @param params
     * @return
     */
    public ResponseVo<List<NodeInfoResponseVo>> getUserNodeList(RequestVo params) {
        //区分超级管理员
        if (params.getUser().getRoleId().contains(RoleConstant.ADMIN_ROLE_ID_STR)) {
            return this.getAllResourceNodeList(params);
        }
        //当前用户的角色权限
        List<Long> userRoleIds = ConvertUtils.strToListLong(params.getUser().getRoleId(), ",");
        List<Role> roleList = roleDao.findRolesByIdInAndDeleteStatus(userRoleIds, false);
        if (roleList.isEmpty()) {
            return ResponseVo.ofError(ResponseCode.UNAUTHORIZED);
        }

        //角色可能没有节点|角色可能只有一级节点
        Set<Long> readWriteSet = new HashSet<>();
        Set<Long> nodeIdSet = new HashSet<>();
        roleList.forEach(x -> {
            List<Long> nodeIds = ConvertUtils.strToListLong(x.getRoleNode(), ",");
            if (!nodeIds.isEmpty()) {
                nodeIdSet.addAll(nodeIds);
                if (x.getRolePower() == 1) {
                    //读写权限的节点
                    readWriteSet.addAll(nodeIds);
                }
            }
        });
        if (nodeIdSet.isEmpty()) {
            return ResponseVo.ofSuccess(new ArrayList<>());
        }

        return ResponseVo.ofSuccess(this.getResourceNodeList(0L, new ArrayList<>(nodeIdSet), false, readWriteSet));
    }

    /**
     * 资源节点树形结构列表
     * @param params
     * @return
     */
    public ResponseVo<List<NodeInfoResponseVo>> getAllResourceNodeList(RequestVo params) {
        List<ResourceNode> firstNodeList = resourceNodeDao.getAllRankNode(1);
        if (firstNodeList.isEmpty()) {
            return ResponseVo.ofSuccess(new ArrayList<>());
        }
        return ResponseVo.ofSuccess(this.getResourceNodeList(0L, new ArrayList<>(), true, new HashSet<>()));
    }

    /**
     * 节点循环查询
     * @param pid
     * @return
     */
    private List<NodeInfoResponseVo> getResourceNodeList(Long pid, List<Long> idList, boolean isAdmin, Set<Long> writeNodeList) {
        List<NodeInfoResponseVo> responseVoList = new ArrayList<>();
        List<ResourceNode> nodeList;
        if (idList.isEmpty()) {
            nodeList = resourceNodeDao.getAllChildrenNode(pid);
        } else {
            nodeList = resourceNodeDao.getChildrenNodeByPid(pid, idList);
        }
        if (!nodeList.isEmpty()) {
            List<NodeInfoResponseVo> tmpList = new ArrayList<>();
            nodeList.forEach(x -> {
                NodeInfoResponseVo tmp = new NodeInfoResponseVo();
                BeanUtils.copyProperties(x, tmp);
                //节点的读写权限
                if (isAdmin) {
                    tmp.setRolePowerStatus(1);
                } else if (writeNodeList.contains(x.getId())) {
                    tmp.setRolePowerStatus(1);
                } else {
                    tmp.setRolePowerStatus(0);
                }
                tmpList.add(tmp);
            });
            for(NodeInfoResponseVo node : tmpList) {
                if (idList.isEmpty()) {
                    node.setList(this.getResourceNodeList(node.getId(), new ArrayList<>(), isAdmin, writeNodeList));
                } else {
                    node.setList(this.getResourceNodeList(node.getId(), idList, isAdmin, writeNodeList));
                }
            }
            responseVoList.addAll(tmpList);
        }
        return responseVoList;
    }
}
