package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/29 上午10:46
 * User: YHC
 * Desc: 设备类型
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_device_type")
public class DeviceType extends BaseEntity {

    //设备类型名称 长度20 唯一
    private String deviceTypeName;

    //描述
    private String remark;
}
