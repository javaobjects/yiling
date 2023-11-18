package com.yiling.open.erp.util;

/**
 * 字符随机数生成工具类
 *
 * @author: houjie.sun
 * @date: 2022/1/14
 */
public class RandomStringUtil {

    public static final char[] codes = { '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J',
            'K','L','M','N','O','P','Q','R','S','T',
            'U','V','W','X','Y','Z','a','b','c','d',
            'e','f','g','h','i','j','k','l','m','n',
            'o','p','q','r','s','t','u','v','w','x',
            'y','z'};


    /**
     * 返回随机字符串，同时包含数字、大小写字母
     * @param len 字符串长度，大于等于32
     * @return String 随机字符串
     */
    public static String randomStr(int len){
        if(len < 32){
            throw new IllegalArgumentException("字符串长度不能小于32");
        }
        char[] chArr = new char[len];
        // 保证必须包含数字、大小写字母
        chArr[0] = (char)('0' + RandomBase.uniform(0,10));
        chArr[1] = (char)('A' + RandomBase.uniform(0,26));
        chArr[2] = (char)('a' + RandomBase.uniform(0,26));

        //charArr[3..len-1]随机生成codes中的字符
        for(int i = 3; i < len; i++){
            chArr[i] = codes[RandomBase.uniform(0,codes.length)];
        }
        //将数组chArr随机排序
        for(int i = 0; i < len; i++){
            int r = i + RandomBase.uniform(len - i);
            char temp = chArr[i];
            chArr[i] = chArr[r];
            chArr[r] = temp;
        }
        return new String(chArr);
    }

}
