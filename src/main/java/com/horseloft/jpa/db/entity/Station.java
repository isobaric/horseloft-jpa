package com.horseloft.jpa.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 2020/2/3 下午5:02
 * User: YHC
 * Desc: 工位
 */
@Getter
@Setter
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_station")
public class Station implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="create_time")
    private Date createTime;

    //编号
    private String stationCode;

    // 工位类型id jpa_station_type表id
    private Long stationTypeId;

    // 坐标X
    @Column(name = "axis_x")
    private Integer axisX;

    // 坐标Y
    @Column(name = "axis_y")
    private Integer axisY;

    // 是否在地图上 1是 0否【1时 axis_x axis_y应有值】
    private Boolean mapStatus;

    // 下方是否可通行
    private Boolean passStatus;

    // 公司id
    private Long companyId;

    //工厂id
    private Long factoryId;

    // 车间id
    private Long workshopId;

    //产成品区
    private String productArea;

    // 设备区
    private String deviceArea;

    // 设备队列区
    private String deviceQueueArea;

    // 组件队列区
    private String componentQueueArea;

}
