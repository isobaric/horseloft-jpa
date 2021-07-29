package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Lattice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Date: 2020/2/5 下午2:24
 * User: YHC
 * Desc: 货位
 */
public interface LatticeDao extends CrudRepository<Lattice, Long> {

    //shelf_id查询
    List<Lattice> findLatticesByShelfId(Long shelfId);

    //shelf_id 删除
    void deleteLatticesByShelfId(Long shelfId);
}
