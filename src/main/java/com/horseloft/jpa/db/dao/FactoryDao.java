package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Factory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 2020/1/7 16:25
 * User: YHC
 * Desc:
 */
@Repository
public interface FactoryDao extends CrudRepository<Factory, Long> {

    //查询一条信息
    Factory findFactoryByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //id in查询
    List<Factory> findFactoriesByIdIn(List<Long> id);

    //工厂id id查询
    @Query(value = "select * from jpa_structure_factory where company_id = :companyId and id = :id and delete_status = 0", nativeQuery = true)
    Factory getFactoryByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

    //全部工厂
    @Query(value = "select * from jpa_structure_factory where delete_status = 0", nativeQuery = true)
    List<Factory> getAllFactory();

    //公司的全部工厂
    @Query(value = "select * from jpa_structure_factory where company_id = ? and delete_status = 0", nativeQuery = true)
    List<Factory> getAllCompanyFactory(Long companyId);

    //公司id删除工厂
    @Modifying
    @Transactional
    @Query(value = "update jpa_structure_factory set delete_status = 1 where company_id = ?", nativeQuery = true)
    int delDistinctByCompanyId(Long companyId);

    //工厂名称获取公司的工厂|因为有唯一索引，所以返回一条数据
    @Query(value = "select * from jpa_structure_factory where delete_status = 0 and company_id = :companyId and factory_name = :factoryName", nativeQuery = true)
    Factory getCompanyFactoryByFactoryName(@Param("companyId") Long companyId, @Param("factoryName") String factoryName);

    //工厂编号获取公司的工厂|因为有唯一索引，所以返回一条数据
    @Query(value = "select * from jpa_structure_factory where delete_status = 0 and company_id = :companyId and factory_code = :factoryCode", nativeQuery = true)
    Factory getCompanyFactoryByFactoryCode(@Param("companyId") Long companyId, @Param("factoryCode") String factoryCode);
}
