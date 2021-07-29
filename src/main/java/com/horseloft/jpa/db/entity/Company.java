package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/7 15:19
 * User: YHC
 * Desc: 公司
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate //使数据库 update_time 自动更新
@Table(name = "jpa_structure_company")
public class Company extends BaseEntity {

    //公司名称
    private String comName;

    //公司编号
    private String comCode;

    // 公司简称
    private String shortName;

    // 公司法人
    private String legalPerson;

    // 公司网址
    private String website;

    // 公司地址
    private String address;

    // 联系方式
    private String telephone;

    // 规模/员工人数
    private Integer employeeNumber;

    // 行业
    private String industry;

    // 邮箱
    private String email;

    // 省ID
    private Long provinceId;

    // 市ID
    private Long cityId;

    // 区县ID
    private Long areaId;

    // 备注
    private String remark;

}
