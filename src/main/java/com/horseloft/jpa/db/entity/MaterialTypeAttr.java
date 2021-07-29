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
 * Date: 2020/2/8 下午3:42
 * User: YHC
 * Desc: 物料类型属性
 */
@Getter
@Setter
@Entity
@ToString
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_material_type_attr")
public class MaterialTypeAttr implements Serializable {

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

    // 物料类型属性名称
    private String attrName;
}
