package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.DeviceType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/29 上午10:49
 * User: YHC
 * Desc:
 */
@Repository
public interface DeviceTypeDao extends CrudRepository<DeviceType, Long> {

    //名称查询一条 |device_type_name有唯一索引
    @Query(value = "select * from jpa_device_type where device_type_name = :deviceTypeName and delete_status = :deleteStatus", nativeQuery = true)
    DeviceType getDeviceTypeByName(@Param("deviceTypeName") String deviceTypeName, @Param("deleteStatus") Boolean deleteStatus);

    //id查找一条
    DeviceType findDeviceTypeByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    //全部有效的设备类型
    List<DeviceType> findDeviceTypesByDeleteStatus(Boolean deleteStatus);

    //id in查询
    List<DeviceType> findDeviceTypesByIdInAndDeleteStatus(List<Long> ids, Boolean deleteStatus);
}
