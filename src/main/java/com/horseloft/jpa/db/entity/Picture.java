package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/29 下午2:34
 * User: YHC
 * Desc: 图片
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_picture")
public class Picture extends BaseEntity {

    //归属id；如：type=1，则此id则为jpa_device_type表id
    private Long belongId;

    //类型；1设备类型照片【jpa_device_type表】
    private Integer type;

    //存储路径
    private String path;
}
