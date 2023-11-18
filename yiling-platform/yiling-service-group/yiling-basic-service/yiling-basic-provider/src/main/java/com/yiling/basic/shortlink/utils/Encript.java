package com.yiling.basic.shortlink.utils;

import java.security.MessageDigest;

/**
 *
 * @version v1.0.0
 * @author: lun.yu
 * @date: 2022-1-12
 */
public class Encript {
    /**
     * 十六进制下数字到字符的映射数组
     */
    private final static String[] HEX_DIGITS = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    /**
     * 把inputString加密
     * @param inputStr
     * @return
     */
    public static String md5(String inputStr){
        return encodeByMd5(inputStr);
    }

    /**
     * 验证输入的密码是否正确
     * @param password 真正的密码（加密后的真密码）
     * @param inputString 输入的字符串
     * @return 验证结果，boolean类型
     */
    public static boolean authenticatePassword(String password,String inputString){
        return password.equals(encodeByMd5(inputString));
    }

    /**
     * 对字符串进行MD5编码
     * @param originString
     * @return
     */
    private static String encodeByMd5(String originString){
        if (originString!=null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md5.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                return byteArrayToHexString(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 轮换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     *
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b
     * @return
     */
    private static String byteToHexString(byte b){
        int n = b;
        if(n<0) {
            n=256+n;
        }
        int d1 = n/16;
        int d2 = n%16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];

    }
}