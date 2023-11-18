package com.yiling.open.erp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shichen
 * @类名 RegexMatcheUtils
 * @描述 正则匹配工具类
 * @创建时间 2022/1/12
 * @修改人 shichen
 * @修改时间 2022/1/12
 **/
public class RegexMatcheUtils {


    /**
     * 匹配医疗机构执业许可证（国家标准）
     * 第1-9位：主体标识码（组织机构代码或者卫生部门颁布的临时组织机构代码），使用阿拉伯数字或大写英文字母（临时组织机构代码8~9位之间有连接符"-"）表示。（参照《全国组织机构代码编制规则》〔GB 11714〕）
     *
     * 第10-15位：登记管理机关行政区划码，使用阿拉伯数字表示。（参照《中华人民共和国行政区划代码》〔GB/T 2260〕）
     *
     * 第16-17位：经济类型代码，使用阿拉伯数字表示。卫生行业经历类型分类与代码（参照《经济类型分类与代码》〔GB/T12402-2000〕）
     *
     * 第18-21位：卫生机构（组织）类别代码，18位使用大写英文字母，19-21位使用阿拉伯数字表示。（参照《卫生机构（组织）类别代码表》）
     *
     * 第22位：机构分类管理代码 使用阿拉伯数字1，2，9
     * @param str
     * @return
     */
    public static boolean matchPracticeLicenseOfMedicalInstitution(String str){
        String pattern = "[0-9A-Z]{8}[-]{0,1}[0-9A-Z]\\d{8}[A-Z]\\d{3}(1|2|3|9)";
        Matcher m = Pattern.compile(pattern).matcher(str);
        return m.matches();
    }

    /**
     * 部分地方提供临时医疗许可证由16位大写字母和数字组成
     * @param str
     * @return
     */
    public static boolean matchTempMedicalInstitution(String str){
        String pattern = "[0-9A-Z]{16}";
        Matcher m = Pattern.compile(pattern).matcher(str);
        return m.matches();
    }
}
