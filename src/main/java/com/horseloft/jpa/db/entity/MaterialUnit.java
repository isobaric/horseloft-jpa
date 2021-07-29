package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/2/9 上午10:06
 * User: YHC
 * Desc: 物料单位
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_material_unit")
public class MaterialUnit extends BaseEntity {

    //计量单位
    private String unitCode;

    //单位名称
    private String unitName;

    //计量单位类型 | 1数量、2长度、3体积，4重量，5金钱
    private Integer type;
}
