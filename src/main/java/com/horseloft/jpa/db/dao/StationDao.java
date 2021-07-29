package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Station;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Date: 2020/2/3 下午5:11
 * User: YHC
 * Desc: 工位
 */
public interface StationDao extends CrudRepository<Station, Long>, JpaSpecificationExecutor<Station> {

    //编号获取一条 stationCode有唯一索引
    Station findStationByStationCode(String stationCode);
}
