package com.yiling.open.erp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.yiling.open.erp.entity.ErpCustomerDO;

import cn.hutool.core.util.StrUtil;

/**
 * 字符处理工具类
 *
 * @author: houjie.sun
 * @date: 2022/1/6
 */
public class OpenStringUtils {

    private static final String SPACE_REGEX = "\\s*|\t|\r|\n";
    private static final int MIN_LENGTH = 18;
    private static final int MAX_LENGTH = 22;

    /**
     * 去除字符串中的全部空格、回车、换行符、制表符
     *
     * @param source
     * @return
     */
    public static String clearAllSpace(String source) {
        Pattern p = Pattern.compile(SPACE_REGEX);
        Matcher m = p.matcher(source);
        return m.replaceAll("");
    }

    /**
     * 校验证书编号长度
     * 最小18位，最大22位
     *
     * @param source
     * @return
     */
    public static boolean checkLicenseNoLength(String source){
        if(StrUtil.isBlank(source)){
            return false;
        }
        int length = source.length();
        if(length < MIN_LENGTH || length > MAX_LENGTH){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ErpCustomerDO erpCustomerDO = new ErpCustomerDO();
        erpCustomerDO.setLicenseNo("\n  1234567890000  000000000 ");
        erpCustomerDO.setName("159");
        String nameMsg = nameCheck(erpCustomerDO);
        String licenseNoMsg = licenseNoCheck(erpCustomerDO);
        System.out.println(">>>>> nameMsg:"+nameMsg);
        System.out.println(">>>>> licenseNoMsg:"+licenseNoMsg);

        if(StrUtil.isBlank(nameMsg) && StrUtil.isBlank(licenseNoMsg)){
            String name = OpenStringUtils.clearAllSpace(erpCustomerDO.getName());
            String licenseNo = OpenStringUtils.clearAllSpace(erpCustomerDO.getLicenseNo());
            System.out.println(">>>>> name:"+name);
            System.out.println(">>>>> licenseNo:"+licenseNo);
        }
    }

    private static String nameCheck(ErpCustomerDO erpCustomerDO){
        if (StringUtils.isBlank(erpCustomerDO.getName())) {
            return "企业名称不能为空";
        }
        String name = OpenStringUtils.clearAllSpace(erpCustomerDO.getName());
        if (StringUtils.isBlank(name)) {
            return "企业名称不能为空格、回车、换行符等空字符";
        }
        return null;
    }

    private static String licenseNoCheck(ErpCustomerDO erpCustomerDO){
        if (StringUtils.isBlank(erpCustomerDO.getLicenseNo())) {
            return "执业许可证号/社会信用统一代码不能为空";
        }

        String licenseNo = OpenStringUtils.clearAllSpace(erpCustomerDO.getLicenseNo());
        if (StringUtils.isBlank(licenseNo)) {
            return "执业许可证号/社会信用统一代码不能为空格、回车、换行符等空字符";
        }
        boolean lengthResult = OpenStringUtils.checkLicenseNoLength(licenseNo);
        if(!lengthResult){
            return "执业许可证号/社会信用统一代码，长度不能小于18位、且不能大于22位";
        }
        return null;
    }


}
