package com.horseloft.jpa.utils;

import org.apache.commons.lang3.RandomUtils;

import javax.validation.constraints.NotNull;

/**
 * Date: 2020/1/26 上午11:15
 * User: YHC
 * Desc:
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 唯一索引的数据删除后，数据转换
     * @param string
     * @return
     */
    public static String appendRandom(String string) {
        return string + "-" + RandomUtils.nextInt(1000, 9999);
    }

    /**
     * 移除字符串最后的逗号
     * @param string
     * @return
     */
    public static String trimWithEnd(@NotNull String string, @NotNull String endString) {
        if (string.isEmpty()) {
            return string;
        }
        if (string.endsWith(endString)) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }
}
