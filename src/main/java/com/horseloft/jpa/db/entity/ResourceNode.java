package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/19 下午6:44
 * User: YHC
 * Desc: 资源节点/侧边栏节点
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate //使数据库 update_time 自动更新
@Table(name = "jpa_resource_node")
public class ResourceNode extends BaseEntity {

    //父级ID
    private Long pid;

    //节点名称
    private String nodeName;

    //后端路由
    private String backendRoute;

    //前端路由
    private String webRoute;

    //排序
    private Integer sort;

    //层级
    private Integer rank;

}
