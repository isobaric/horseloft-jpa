package com.horseloft.jpa.utils;

import com.horseloft.jpa.constant.RoleConstant;

import javax.validation.constraints.NotNull;

/**
 * Date: 2020/1/27 下午5:54
 * User: YHC
 * Desc: 数据相关
 */
public class DataUtils {

    /**
     * 是否为默认角色
     * @param roleId
     * @return
     */
    public static boolean isDefaultRole(@NotNull Long roleId) {
        //管理员、工人
        return roleId.equals(RoleConstant.ADMIN_ROLE_ID) || roleId.equals(RoleConstant.WORKER_ROLE_ID);
    }
}
