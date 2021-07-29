package com.horseloft.jpa.constant;

/**
 * Date: 2020/1/4 13:53
 * User: YHC
 * Desc: 用户常量
 */
public class UserConstant {

    //超级管理员的用户ID|此用户不可删除、编辑 jpa_user表id
    public static final Long ADMIN_USER_ID = 1L;

    //密码对称加密串
    public static final String PASSWORD_AES_KEY = "f53c7a6977b5a1c3efb2abbc5d6d7669";

    //创建的用户的默认密码 123456
    public static final String USER_DEFAULT_PASSWORD = "123456";
}
