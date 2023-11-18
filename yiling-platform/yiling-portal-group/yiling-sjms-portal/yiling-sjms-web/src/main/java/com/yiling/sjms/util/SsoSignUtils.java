package com.yiling.sjms.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SSO签名工具类
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
public class SsoSignUtils {

    private static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static String sign(String secret, String source) {
        return encodeMd5(secret, source.getBytes());
    }

    private static String encodeMd5(String secret, byte[] source) {
        source = byteMerger(secret.getBytes(StandardCharsets.UTF_8), source);

        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if ((bytes[i] & 255) < 16) {
                buffer.append("0");
            }

            buffer.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buffer.toString();
    }
}
