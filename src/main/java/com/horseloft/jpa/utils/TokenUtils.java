package com.horseloft.jpa.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.horseloft.jpa.enums.ResponseCode;
import com.horseloft.jpa.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * Date: 2020/1/8 09:55
 * User: YHC
 * Desc:
 */
public class TokenUtils {

    /**
     * 生成随机秘钥
     * @return
     */
    public static String encodeKey() {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        return Convert.toHex(key);
    }

    /**
     * 生成token
     * @param userCode
     * @param key
     * @return
     */
    public static String tokenEncode(String userCode, String key) {
        if (StringUtils.isEmpty(userCode) || StringUtils.isEmpty(key)) {
            return null;
        }
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, Convert.hexToBytes(key));
        //加密为16进制表示
        return System.currentTimeMillis() + aes.encryptHex(userCode);
    }

    /**
     * 解密token
     * @param token
     * @param key
     * @return
     */
    public static String tokenDecode(String token, String key) {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(key) || token.length() < 13) {
            throw new BusinessException(ResponseCode.DATA_ERROR.getCode(), "数据解析失败");
        }
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, Convert.hexToBytes(key));
        //加密为16进制表示
        return aes.decryptStr(token.substring(13), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 用户ID补充到20位长度|ID前面补0
     * @param userId
     * @return
     */
    public static String userIdAppend(Long userId) {
        String idString = String.valueOf(userId);
        if (idString.length() < 20) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < 20 - idString.length(); i++) {
                stringBuilder.append("0");
            }
            idString = stringBuilder.toString() + idString;
        }
        return idString;
    }
}
