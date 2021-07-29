package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.MaterialTypeAttr;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Date: 2020/2/8 下午3:47
 * User: YHC
 * Desc: 物料类型属性
 */
@Repository
public interface MaterialTypeAttrDao extends CrudRepository<MaterialTypeAttr, Long> {

    //type_id 查询列表
    List<MaterialTypeAttr> findMaterialTypeAttrsByTypeId(Long typeId);

    //type_id s 查询列表
    List<MaterialTypeAttr> findMaterialsTypeAttrsByTypeIdIn(Set<Long> typeId);
}
