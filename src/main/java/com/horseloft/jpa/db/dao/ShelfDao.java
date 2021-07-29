package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Shelf;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Date: 2020/2/5 下午2:06
 * User: YHC
 * Desc:
 */
@Repository
public interface ShelfDao extends CrudRepository<Shelf, Long>, JpaSpecificationExecutor<Shelf> {

    //shelf_code 查找一条 | shelf_code有唯一索引
    Shelf findShelfByShelfCode(String code);
}
