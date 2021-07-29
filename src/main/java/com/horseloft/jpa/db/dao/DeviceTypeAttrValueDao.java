package com.horseloft.jpa.db.dao;

import com.horseloft.jpa.db.entity.DeviceTypeAttrValue;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 2020/2/1 下午2:23
 * User: YHC
 * Desc: 设备的类型的值
 */
@Repository
public interface DeviceTypeAttrValueDao extends CrudRepository<DeviceTypeAttrValue, Long> {

    //device_id 删除
    @Modifying
    @Transactional
    @Query(value = "delete from jpa_device_type_attr_value where device_id = ?", nativeQuery = true)
    int deleteAttrValueByDeviceId(Long deviceId);

    //device_id 查询
    @Query(value = "select * from jpa_device_type_attr_value where delete_status = 0 and device_id = :deviceId and device_type_attr_id in (:attrIds)", nativeQuery = true)
    List<DeviceTypeAttrValue> getValuesByDeviceIdAndAttrIds(@Param("deviceId") Long deviceId, @Param("attrIds") List<Long> attrIds);
}
