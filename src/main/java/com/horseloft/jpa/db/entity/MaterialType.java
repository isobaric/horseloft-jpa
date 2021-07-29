package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/2/8 下午3:38
 * User: YHC
 * Desc: 物料类型
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_material_type")
public class MaterialType extends BaseEntity {

    //物料类型名称
    private String typeName;

    // 物料类型编码
    private String typeCode;

    // 描述
    private String remark;
}
