package com.horseloft.jpa.utils;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2020/1/20 下午2:01
 * User: YHC
 * Desc: 数据转换
 */
public class ConvertUtils {

    /**
     * 逗号分隔的数字字符串转List<Long>|失败返回空
     * @param string
     * @param separator
     * @return
     */
    public static List<Long> strToListLong(String string, String separator) {
        if (StringUtils.isBlank(string)) {
            return new ArrayList<>();
        }
        String[] strings = StringUtils.split(string, separator);
        List<Long> longList = new ArrayList<>();
        try {
            for (String s : strings) {
                Long value = Long.valueOf(s);
                //移除0值
                if (value.equals(0L)) {
                    continue;
                }
                longList.add(value);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return longList;
    }

    /**
     * 逗号分隔的数字字符串后面追加逗号
     * @param strNumber
     * @return
     */
    public static String strNumberAppendComma(@NotNull String strNumber) {
        if (strNumber.lastIndexOf(",") != strNumber.length()) {
            strNumber = strNumber + ",";
        }
        return strNumber;
    }
}
