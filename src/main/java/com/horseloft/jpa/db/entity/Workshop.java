package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;
/**
 * Date: 2020/1/7 16:38
 * User: YHC
 * Desc: 车间
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate //使数据库 update_time 自动更新
@Table(name = "jpa_structure_workshop")
public class Workshop extends BaseEntity {

    // 车间ID
    private Long companyId;

    // 工厂ID
    private Long factoryId;

    // 车间名称
    private String workshopName;

    // 车间编号
    private String workshopCode;

    // 描述
    private String remark;

    //是否已编辑
    private Integer editStatus;

    // 电话
    private String telephone;
}
