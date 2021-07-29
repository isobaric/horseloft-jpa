package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/2/4 上午11:13
 * User: YHC
 * Desc: 设备类型
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_station_type")
public class StationType extends BaseEntity {

    //工位类型
    private String typeName;

    //排序
    private Integer sort;
}
