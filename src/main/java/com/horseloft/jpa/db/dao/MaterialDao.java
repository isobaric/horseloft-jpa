package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Material;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * Date: 2020/2/5 下午5:19
 * User: YHC
 * Desc: 物料
 */
public interface MaterialDao extends CrudRepository<Material, Long>, JpaSpecificationExecutor<Material> {

    //id查询多个
    List<Material> findMaterialsByIdIn(Set<Long> ids);

    //物料编号 查询一个 物料编号有唯一索引
    Material findMaterialByMaterialCode(String code);

    //物料名称 查询一个 物料名称有唯一索引
    Material findMaterialByMaterialName(String name);

    //type_id 查询一条
    @Query(value = "select * from jpa_material where type_id = ? and delete_status = 0 limit 1", nativeQuery = true)
    Material getMaterialByTypeId(Long typeId);

    //unit_id 查询一条
    @Query(value = "select * from jpa_material where unit_id = ? and delete_status = 0 limit 1", nativeQuery = true)
    Material getMaterialByUnitId(Long unitId);
}
