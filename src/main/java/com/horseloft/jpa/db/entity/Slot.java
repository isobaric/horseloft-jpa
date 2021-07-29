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
 * Date: 2020/2/5 下午2:25
 * User: YHC
 * Desc: 货箱
 */
@Getter
@Setter
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_slot")
public class Slot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="create_time")
    private Date createTime;

    //货箱编号
    private String slotCode;

    // 是否锁定
    private Boolean lockStatus;

    // 货架id
    private Long shelfId;

    // 货位id
    private Long latticeId;

    // 物料id
    private Long materialId;

    // 生产订单id
    private String productionOrderCode;

    //处理换片或返修的生产订单编号
    private String handleOrderCode;

    // 当前物料状态 0正常 1返修 2换片
    private Integer materialState;

    //质检状态  0免检、1已质检、2未质检
    private Integer checkState;

    //当前物料数量
    private Integer materialNumber;

    //提交换片或返修的工人id
    private Long reportUserId;

    //处理换片或返修的工人id
    private Long handleUserId;
}
