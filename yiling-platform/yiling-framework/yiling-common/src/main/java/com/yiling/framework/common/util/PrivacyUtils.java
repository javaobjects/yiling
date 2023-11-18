package com.yiling.framework.common.util;

import org.springframework.util.StringUtils;

import cn.hutool.core.util.StrUtil;

/**
 *  隐私信息加密工具类
 *
 * @author: xuan.zhou
 * @date: 2020/5/29
 */
public class PrivacyUtils {

    /**
     * 银行账户：只留后四位，范例：**** **** **** 4568
     *
     * @param bankCardNo 银行卡号
     * @return
     */
    public static String encryptBankCardNo(String bankCardNo) {
        if (StrUtil.isEmpty(bankCardNo) || bankCardNo.length() <= 4) {
            return bankCardNo;
        }

        return new StringBuffer("**** **** **** ")
            .append(bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length())).toString();
    }

    /**
     * 身份证号码：显示前六后四，范例：110601********2015
     *
     * @param idCardNo 身份证号
     * @return
     */
    public static String encryptIdCard(String idCardNo) {
        return replaceBetween(idCardNo, 6, idCardNo.length() - 4, "*");
    }

    /**
     * 手机：显示前三后四，范例：189****3684
     *
     * @param phoneNo
     * @return
     */
    public static String encryptPhoneNo(String phoneNo) {
        return replaceBetween(phoneNo, 3, phoneNo.length() - 4, "*");
    }

    /**
     * 将字符串开始位置到结束位置之间的字符用指定字符替换
     *
     * @param str 待处理字符串
     * @param beginIndex 开始位置
     * @param endIndex 结束位置
     * @param replacement 替换字符
     * @return
     */
    private static String replaceBetween(String str, int beginIndex, int endIndex, String replacement) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (StringUtils.isEmpty(replacement)) {
            return str;
        }

        int strLength = str.length();
        if (beginIndex < strLength && endIndex < strLength && beginIndex < endIndex) {
            int replaceLength = endIndex - beginIndex;
            StringBuilder sb = new StringBuilder(str);
            sb.replace(beginIndex, endIndex, StrUtil.repeat(replacement, replaceLength));
            return sb.toString();
        }

        return str;
    }

    public static void main(String[] args) {
        System.out.println(PrivacyUtils.encryptBankCardNo("6228482462893085616"));
        System.out.println(PrivacyUtils.encryptPhoneNo("13228116626"));
        System.out.println(PrivacyUtils.encryptIdCard("510658199107356847"));
    }
}
