package com.horseloft.jpa.utils;

/**
 * Date: 2020/1/22 下午5:10
 * User: YHC
 * Desc:
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    //是否为大于0的Long数据
    public static boolean isActiveLong(Long params) {
        return params != null && params > 0;
    }

    //是否为大于0的Integer
    public static boolean isActiveInteger(Integer params) {
        return params != null && params > 0;
    }

    //是否为 自然数
    public static boolean isNaturalNumber(Long params) {
        return params != null && params >= 0;
    }

    //是否为 自然数
    public static boolean isNaturalNumber(Integer params) {
        return params != null && params >= 0;
    }
}
