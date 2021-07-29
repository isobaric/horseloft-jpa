package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/7 16:21
 * User: YHC
 * Desc: 工厂
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate //使数据库 update_time 自动更新
@Table(name = "jpa_structure_factory")
public class Factory extends BaseEntity {

    //工厂ID
    private Long companyId;

    // 车间名称
    private String factoryName;

    // 车间编号
    private String factoryCode;

    // 电话
    private String telephone;

    // 地址
    private String address;

    // 描述
    private String remark;

    //是否已编辑
    private Integer editStatus;

}
