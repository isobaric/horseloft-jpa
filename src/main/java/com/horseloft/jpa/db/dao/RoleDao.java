package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.constant.RoleConstant;
import com.horseloft.jpa.db.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/20 上午11:39
 * User: YHC
 * Desc: 角色
 */
@Repository
public interface RoleDao extends CrudRepository<Role, Long> {

    //全部有效角色
    @Query(value = "select * from jpa_role where delete_status = 0", nativeQuery = true)
    List<Role> getAllRole();

    //全部角色|除了超级管理员
    @Query(value = "select * from jpa_role where delete_status = 0 and id > " + RoleConstant.WORKER_ROLE_ID, nativeQuery = true)
    List<Role> getActiveRoleExcludeAdmins();

    //名称查询角色[全部]|名称为唯一索引 所以只有一条数据
    @Query(value = "select * from jpa_role where role_name = ?", nativeQuery = true)
    Role getTotalRoleByRoleName(String roleName);

    //id查询有效用户
    @Query(value = "select * from jpa_role where delete_status = 0 and id = ?", nativeQuery = true)
    Role getActiveRoleById(Long id);

    //id in条件查询
    List<Role> findRolesByIdIn(List<Long> id);

    //id delete_status查询多个
    List<Role> findRolesByIdInAndDeleteStatus(List<Long> id, Boolean deleteStatus);
}
