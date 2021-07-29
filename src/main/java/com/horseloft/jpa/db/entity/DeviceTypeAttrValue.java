package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/2/1 下午2:20
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_device_type_attr_value")
public class DeviceTypeAttrValue extends BaseEntity {

    //设备id
    private Long deviceId;

    //设备类型id
    private Long deviceTypeAttrId;

    //设备类型值
    private String attrValue;
}
