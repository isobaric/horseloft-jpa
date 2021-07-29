package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/29 下午5:16
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_device")
public class Device extends BaseEntity {

    //设备名称
    private String deviceName;

    //设备编号
    private String deviceCode;

    //设备类型id
    private Long deviceTypeId;

    //公司id
    private Long companyId;

    // 工厂id
    private Long factoryId;

    // 车间id
    private Long workshopId;

    // 下方是否可通信
    private Boolean passStatus;

    // 是否可移动
    private Boolean moveStatus;

    //设备状态 0不可用，1正常，2待维修
    private Integer deviceState;

    // 设备锁定状态
    private Boolean lockStatus;

    // 是否正在移动
    private Boolean walkingStatus;

    // 坐标x
    @Column(name = "axis_x")
    private Integer axisX;

    // 坐标Y
    @Column(name = "axis_y")
    private Integer axisY;

    // 描述
    private String remark;
}
