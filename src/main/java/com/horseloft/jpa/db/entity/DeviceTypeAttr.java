package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/29 上午11:14
 * User: YHC
 * Desc: 设备属性
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_device_type_attr")
public class DeviceTypeAttr extends BaseEntity {

    //设备类型id
    private Long deviceTypeId;

    //属性名称
    private String attrName;

    //修改提示：0不提示，1提示
    private Boolean warnStatus;
}
