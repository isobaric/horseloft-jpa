package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Date: 2020/1/5 17:44
 * User: YHC
 * Desc: 用户
 */
@Repository
public interface UserDao extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    //ids查找
    List<User> findUsersByIdIn(Set<Long> ids);

    //账号查找删除状态用户
    User findByAccountAndDeleteStatus(String account, Boolean deleteStatus);

    //ids查找
    List<User> findByIdInAndDeleteStatus(List<Long> ids, Boolean deleteStatus);

    //id查找
    User findUserByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //车间id查找
    List<User> findUserByWorkshopIdAndDeleteStatus(Long workshopId, Boolean deleteStatus);

    //工厂id查找
    List<User> findUserByFactoryIdAndDeleteStatus(Long factoryId, Boolean deleteStatus);

    //公司id查找
    List<User> findUserByCompanyIdAndDeleteStatus(Long companyId, Boolean deleteStatus);

    //ID查找可登陆用户
    @Query(value = "select * from jpa_user where id = ? and delete_status = 0 and account_status = 1 and job_status = 1 and role_id != ''", nativeQuery = true)
    User getActiveUserById(Long id);

    //根据账号查找可登陆用户
    @Query(value = "select * from jpa_user where account = ? and delete_status = 0 and account_status = 1 and job_status = 1 and role_id != ''", nativeQuery = true)
    User getActiveInfoByAccount(String account);

    //登录成功更新用户信息
    @Modifying
    @Transactional
    @Query(value = "update jpa_user set secret_key = :key,token = :token,expire_time = :expireTime,auto_login_status = :autoLogin where id = :id", nativeQuery = true)
    int updateUserToken(@Param("key") String key, @Param("token") String token, @Param("id") Long id, @Param("expireTime") Long expireTime, @Param("autoLogin") Integer autoLogin);

    //token获取可登陆用户
    @Query(value = "select * from jpa_user where token = ? and delete_status = 0 and account_status = 1 and job_status = 1 and role_id != ''", nativeQuery = true)
    User getActiveUserByToken(String token);

    //更新用户登录过期时间
    @Modifying
    @Transactional
    @Query(value = "update jpa_user set expire_time = :expireTime where id = :id", nativeQuery = true)
    int updateUserExpireTime(@Param("id") Long id, @Param("expireTime") Long expireTime);

    //退出登录|修改登录过期时间、自动登录状态、token、秘钥
    @Modifying
    @Transactional
    @Query(value = "update jpa_user set expire_time = 0,auto_login_status = 0,token = '',secret_key = '' where id = :id", nativeQuery = true)
    int updateOnUserLogout(@Param("id") Long id);

    //修改密码
    @Modifying
    @Transactional
    @Query(value = "update jpa_user set password = :password,password_status = 1 where id = :id", nativeQuery = true)
    int updatePasswordById(@Param("id") Long id, @Param("password") String password);

    //公司id获取每个公司的一个有效用户
    @Query(value = "select * from jpa_user where delete_status = 0 and account_status = 1 and job_status = 1 and company_id in (:companyIdList) group by company_id", nativeQuery = true)
    List<User> getActiveSingleUserByCompanyIds(@Param(("companyIdList")) List<Long> companyIdList);

    //工厂ID获取每个工厂的一个有效用户
    @Query(value = "select * from jpa_user where delete_status = 0 and account_status = 1 and job_status = 1 and factory_id in (:factoryIdList) group by factory_id", nativeQuery = true)
    List<User> getActiveSingleUserByFactoryIds(@Param("factoryIdList") List<Long> factoryIdList);

    //车间ID获取每个车间的一个有效用户
    @Query(value = "select * from jpa_user where delete_status = 0 and account_status = 1 and job_status = 1 and workshop_id in (:workshopIdList) group by workshop_id", nativeQuery = true)
    List<User> getActiveSingleUserByWorkshopIds(@Param("workshopIdList") List<Long> workshopIdList);

    //获取公司的一个有效员工
    @Query(value = "select * from jpa_user where company_id = ? and delete_status = 0 and account_status = 1 and job_status = 1 limit 1", nativeQuery = true)
    User getActiveSingleUserByCompanyId(Long companyId);

    //获取工厂的一个有效员工
    @Query(value = "select * from jpa_user where factory_id = ? and delete_status = 0 and account_status = 1 and job_status = 1 limit 1", nativeQuery = true)
    User getActiveSingleUserByFactoryId(Long factoryId);

    //获取车间的一个有效员工
    @Query(value = "select * from jpa_user where workshop_id = ? and delete_status = 0 and account_status = 1 and job_status = 1 limit 1", nativeQuery = true)
    User getActiveSingleUserByWorkshopId(Long workshopId);

    //角色ID查询一个用户信息
    @Query(value = "select * from jpa_user where delete_status = 0 and account_status = 1 and job_status = 1 and role_id like ? limit 1", nativeQuery = true)
    User getActiveSingleUserByRoleId(String roleId);
}
