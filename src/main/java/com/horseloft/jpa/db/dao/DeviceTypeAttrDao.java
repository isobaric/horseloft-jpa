package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.DeviceTypeAttr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2020/1/29 上午11:22
 * User: YHC
 * Desc: 设备类型属性
 */
@Repository
public interface DeviceTypeAttrDao extends CrudRepository<DeviceTypeAttr, Long> {

    //device_type_id 查询列表
    @Query(value = "select * from jpa_device_type_attr where delete_status = 0 and device_type_id = ?", nativeQuery = true)
    List<DeviceTypeAttr> getAttrsByDeviceTypeIds(Long deviceTypeId);
}
