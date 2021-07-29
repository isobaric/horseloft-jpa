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
 * Date: 2020/2/5 下午1:57
 * User: YHC
 * Desc: 货架
 */
@Getter
@Setter
@Entity
@ToString
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_shelf")
public class Shelf implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="create_time")
    private Date createTime;

    //货架编号
    private String shelfCode;

    // 公司id
    private Long companyId;

    // 工厂id
    private Long factoryId;

    // 车间id
    private Long workshopId;

    // 是否锁定
    private Boolean lockStatus;

    // 是否在地图上
    private Boolean mapStatus;

    //是否正在移动
    private Boolean walkingStatus;

    // 货架坐标X
    @Column(name = "axis_x")
    private Integer axisX;

    // 货架坐标Y
    @Column(name = "axis_y")
    private Integer axisY;
}
