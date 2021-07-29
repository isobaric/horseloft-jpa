package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.ResourceNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/20 上午9:59
 * User: YHC
 * Desc:
 */
@Repository
public interface ResourceNodeDao extends CrudRepository<ResourceNode, Long> {

    //一条信息
    ResourceNode findResourceNodeByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //路由名称获取节点信息
    @Query(value = "select * from jpa_resource_node where delete_status = 0 and backend_route = ?", nativeQuery = true)
    ResourceNode getInfoByBackendRoute(String backendRoute);

    //全部节点
    @Query(value = "select * from jpa_resource_node where delete_status = 0 order by rank asc,id asc", nativeQuery = true)
    List<ResourceNode> getAllNode();

    //指定层级的全部节点
    @Query(value = "select * from jpa_resource_node where delete_status = 0 and rank = ? order by rank asc,id asc", nativeQuery = true)
    List<ResourceNode> getAllRankNode(Integer rank);

    //父节点ID获取全部子节点
    @Query(value = "select * from jpa_resource_node where delete_status = 0 and pid = :pid order by rank asc,id asc", nativeQuery = true)
    List<ResourceNode> getAllChildrenNode(@Param("pid") Long pid);

    //父节点和子节点id获取节点列表
    @Query(value = "select * from jpa_resource_node where delete_status = 0 and pid = :pid and id in (:list) order by rank asc,id asc", nativeQuery = true)
    List<ResourceNode> getChildrenNodeByPid(@Param("pid") Long pid, @Param("list") List<Long> idList);

    //id获取节点列表
    @Query(value = "select * from jpa_resource_node where delete_status = 0 and id in (:idList) order by rank asc,id asc", nativeQuery = true)
    List<ResourceNode> getNodeByIdList(@Param("idList") List<Long> idList);
}
