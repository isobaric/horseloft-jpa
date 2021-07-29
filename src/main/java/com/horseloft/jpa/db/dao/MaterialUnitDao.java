package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.MaterialUnit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2021/2/9 上午10:14
 * User: YHC
 * Desc:
 */
@Repository
public interface MaterialUnitDao extends CrudRepository<MaterialUnit, Long>, JpaSpecificationExecutor<MaterialUnit> {

    //unit_code 查询一条 | unit_code有唯一索引
    MaterialUnit findMaterialUnitByUnitCode(String unitCode);

    //unit_name 查询一条 | unit_name有唯一索引
    MaterialUnit findMaterialUnitByUnitName(String unitName);

    //全部数据
    List<MaterialUnit> findAllByOrderByIdDesc();

    //全部数据
    List<MaterialUnit> findAllByOrderByIdAsc();
}
