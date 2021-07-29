package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.MaterialType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/2/8 下午3:41
 * User: YHC
 * Desc: 物料类型
 */
@Repository
public interface MaterialTypeDao extends CrudRepository<MaterialType, Long>, JpaSpecificationExecutor<MaterialType> {

    //type_code 查询一条 | type_code有唯一索引
    MaterialType findMaterialTypeByTypeCode(String typeCode);

    //type_name 查询一条 | type_name有唯一索引
    MaterialType findMaterialTypeByTypeName(String typeName);

    @Query(value = "select * from jpa_material_type order by id desc", nativeQuery = true)
    List<MaterialType> getAllMaterialType();

}
