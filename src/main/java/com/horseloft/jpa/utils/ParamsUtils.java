package com.horseloft.jpa.utils;


import cn.hutool.core.codec.Base64;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * Date: 2020/1/7 13:51
 * User: YHC
 * Desc: 参数验证
 */
public class ParamsUtils {

    /**
     * 账号格式
     * @param account
     * @return
     */
    public static boolean isUserAccount(String account) {
        if (account == null) {
            return false;
        }
        String pattern = "^([a-zA-Z]|[0-9]){6,20}$";
        return Pattern.matches(pattern, account);
    }

    /**
     * 生成密码
     * @param password
     * @return
     */
    public static String makeMD5Password(String password) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.reset();
            mdInst.update(password.getBytes(StandardCharsets.UTF_8));
            return Base64.encode(mdInst.digest());
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getCode(), "密码异常");
        }
    }

    /**
     * 密码验证|数字或字母或组合 长度大于等于6
     * @param password
     * @return
     */
    public static boolean isUserPassword(@NotNull String password) {
        return Pattern.matches("^([a-zA-Z0-9]){6,}$", password);
//        if (roleId.contains(RoleConstant.WORKER_ROLE_ID_STR)) {
//            //工人
//            return Pattern.matches("^[0-9]{6}$", password);
//        } else {
//            //非工人
//            String regex = "[`~!@#$^&*()_\\-\\[\\]+=,./<>?;':{}|！￥%…（）—，。《》？；：‘’“”「」【】、]";
//            return Pattern.matches("(?!^[a-zA-Z]+$)(?!^\\d+$)(?!^" + regex + "+$)(^[a-zA-Z0-9`~!@#$^&*()_\\-\\[\\]+=,./<>?;':{}|！￥%…（）—，。《》？；：‘’“”「」【】、]+${6,})", password);
//        }
    }

    /**
     * 手机号+座机验证
     * @param telephone
     * @return
     */
    public static boolean isTelephone(String telephone) {
        if (StringUtils.isBlank(telephone)) {
            return false;
        }
        return isMobile(telephone) || isFixedTelephone(telephone);
    }

    /**
     * 手机号验证
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        return Pattern.matches("^1[0-9]{10}$", mobile);
    }

    /**
     * 固定电话验证|空格和-线分隔长度小于=20
     * @param telephone
     * @return
     */
    public static boolean isFixedTelephone(String telephone) {
        if (StringUtils.isBlank(telephone) || Pattern.matches("^-+$", telephone) || telephone.length() > 20) {
            return false;
        }

        String[] strings = telephone.split("");
        for (String string : strings) {
            if (!Pattern.matches("^[0-9]$", string) && !string.equals(" ") && !string.equals("-")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用户名格式|中英文
     * @param name
     * @return
     */
    public static boolean isPeopleName(String name){
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return isChineseName(name) || isEnglishName(name);
    }

    /**
     * 中文验证
     * @param name
     * @return
     */
    public static boolean isChineseName(String name){
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return Pattern.matches("^[\\u4E00-\\u9FA5]+$", name);
    }

    /**
     * 英文验证
     * @param name
     * @return
     */
    public static boolean isEnglishName(String name){
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return Pattern.matches("^[a-zA-Z]+$", name);
    }

    /**
     * 网址验证
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        return Pattern.matches("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", url);
    }

    /**
     * 允许的图片类型
     * @return
     */
    public static boolean isImageType(String fileType) {
        if (fileType == null ) {
            return false;
        }
        fileType = fileType.toLowerCase();
        return fileType.equals("image/png") || fileType.equals("image/gif") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }
}
