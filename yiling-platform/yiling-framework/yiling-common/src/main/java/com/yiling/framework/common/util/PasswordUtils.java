package com.yiling.framework.common.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

public class PasswordUtils {

    /**
     * 生成密码盐
     */
    public static String genPasswordSalt() {
        return RandomUtil.randomString(6);
    }

    /**
     * 加密
     */
    public static String encrypt(String password, String salt) {
        if (StrUtil.isBlank(password)) {
            return "";
        }
        if (StrUtil.isBlank(salt)) {
            return password;
        }

        HMac mac = new HMac(HmacAlgorithm.HmacMD5, salt.getBytes());
        return mac.digestHex(password);
    }

    public static void main (String[] args) {
        String salt = genPasswordSalt();
        System.out.println("salt = " + salt);
        System.out.println("password = " + encrypt("123456", salt));
    }
}
