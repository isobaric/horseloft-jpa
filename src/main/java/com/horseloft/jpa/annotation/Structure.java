package com.horseloft.jpa.annotation;

import com.horseloft.jpa.enums.JpaDao;

import java.lang.annotation.*;

/**
 * Date: 2020/2/3 下午3:37
 * User: YHC
 * Desc: 用于验证当前用户是否有权限操作请求参数中的组织结构
 *       并验证参数中的组织结构是否正确
 *       使用当前注解后 无需再次验证公司 工厂 车间
 *       编辑接口使用 JpaDao.DEFAULT
 *       使用id查询 编辑 删除等接口使用 JpaDao.XXX | 使用对应数据的dao
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Structure {

    JpaDao value();
}
