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
 * Date: 2020/2/8 下午3:44
 * User: YHC
 * Desc: 物料类型属性值
 */
@Getter
@Setter
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_material_type_attr_value")
public class MaterialTypeAttrValue implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="create_time")
    private Date createTime;

    //物料类型id
    private Long typeId;

    // 物料类型属性id
    private Long attrId;

    // 物料类型属性值名称
    private String valueName;
}
