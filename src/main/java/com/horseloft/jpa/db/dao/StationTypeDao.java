package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.StationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Date: 2020/2/4 上午11:15
 * User: YHC
 * Desc: 工位类型
 */
@Repository
public interface StationTypeDao extends CrudRepository<StationType, Long> {

}
