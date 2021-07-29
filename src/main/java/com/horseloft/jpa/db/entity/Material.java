package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Date: 2020/2/5 下午5:17
 * User: YHC
 * Desc: 物料
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "jpa_material")
public class Material extends BaseEntity {

    //父级id
    private Long pid;

    //物料名称
    private String materialName;

    //物料编号
    private String materialCode;

    //物料类型id
    private Long typeId;

    //物料单位id
    private Long unitId;

    //公司id
    private Long companyId;

    // 工厂id
    private Long factoryId;

    // 车间id
    private Long workshopId;

    //物料大类 | 1底层物料、2商品、2SKU、3中间件
    private Integer category;

    //客户物料名称
    private String customerMaterialName;

    //客户物料编号
    private String customerMaterialCode;

    //来源 1生产 2采购
    private Integer source;

}
