package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/1/25 下午4:32
 * User: YHC
 * Desc: 用户身份证照片
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "jpa_user_picture")
public class UserPicture extends BaseEntity {

    //用户id
    private Long userId;

    //图片路径
    private String path;
}
