package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.Device;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/29 下午5:26
 * User: YHC
 * Desc: 设备
 */
@Repository
public interface DeviceDao extends CrudRepository<Device, Long>, JpaSpecificationExecutor<Device> {

    //device_type_id 查询一条
    @Query(value = "select * from jpa_device where delete_status = 0 and device_type_id = ?", nativeQuery = true)
    Device getDeviceByDeviceTypeId(Long deviceTypeId);

    //device_name 查询| device_name 字段有唯一索引
    Device findDeviceByDeviceNameAndDeleteStatus(String deviceName, Boolean deleteStatus);

    //device_code 查询| device_code 字段有唯一索引
    Device findDeviceByDeviceCodeAndDeleteStatus(String deviceCode, Boolean deleteStatus);

    //id查询一条
    Device findDeviceByIdAndDeleteStatus(Long id, Boolean deleteStatus);

    // device_name or device_code 查询
    @Query(value = "select * from jpa_device where delete_status = 0 and device_name = :deviceName or device_code = :deviceCode", nativeQuery = true)
    List<Device> getDeviceByNameOrCode(@Param("deviceName") String deviceName, @Param("deviceCode") String deviceCode);
}
