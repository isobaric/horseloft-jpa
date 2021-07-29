package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.MaterialTypeAttrValue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/2/8 下午3:47
 * User: YHC
 * Desc: 物料类型属性值
 */
@Repository
public interface MaterialTypeAttrValueDao extends CrudRepository<MaterialTypeAttrValue, Long> {

    //type_id 查询列表
    List<MaterialTypeAttrValue> findMaterialTypeAttrValuesByTypeId(Long typeId);
}
