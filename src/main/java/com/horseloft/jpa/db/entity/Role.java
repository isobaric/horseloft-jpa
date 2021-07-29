package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/20 上午11:34
 * User: YHC
 * Desc: 角色
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate //使数据库 update_time 自动更新
@Table(name = "jpa_role")
public class Role extends BaseEntity {

    //角色名称
    private String roleName;

    // 角色权限
    private Integer rolePower;

    // 资源节点id
    private String roleNode;

    //角色头像
    private Long rolePictureId;
}
