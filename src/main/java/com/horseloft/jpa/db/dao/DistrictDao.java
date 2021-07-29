package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.District;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Date: 2020/1/7 15:50
 * User: YHC
 * Desc: 省市县
 */
@Repository
public interface DistrictDao extends CrudRepository<District, Long>, JpaSpecificationExecutor<District> {

    //区县id获取详情
    @Query(value = "select * from jpa_district where id = :id and rank = :rank", nativeQuery = true)
    District getBaseInfoById(@Param("id") Long id, @Param("rank") Integer rank);
}
