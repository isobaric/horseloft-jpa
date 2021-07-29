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
 * Date: 2020/2/5 下午2:14
 * User: YHC
 * Desc: 货位
 */
@Getter
@Setter
@Entity
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_lattice")
public class Lattice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="create_time")
    private Date createTime;

    //货位编码
    private String latticeCode;

    // 是否锁定
    private Boolean lockStatus;

    // 货架id
    private Long shelfId;

    // 货架类型
    private Integer latticeType;

}
