package com.yiling.goods.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.StrUtil;


/** 
 * Levenshtein Distance算法 
 * 
 */
public class Levenshtein {

    public static double getDistance(String s1, String s2) {
        s1 = s1.replaceAll(" ", "");
        s2 = s2.replaceAll(" ", "");
        double dis1 = getStringDistance(s1, s2);
        double dis2 = getStringDistance(getNum(s1), getNum(s2));
        return numAdd(dis1, dis2);
    }

//    public static void main(String[] args) {
//        System.out.println(getDistance("-洗衣替尼胶囊洗衣替尼胶囊洗衣替尼胶囊洗衣替尼胶囊","洗衣替尼胶囊洗衣替尼胶囊洗衣替尼胶囊洗衣替尼胶囊"));
//    }

    /** 
     * @return Levenshtein Distance
     */
    private static int minimum(int a, int b, int c) {
        int mi = a;
        if (b < a) {
            mi = b;
        }

        if (c < mi) {
            mi = c;
        }

        return mi;
    }

    public static int getStringDistance(String s, String t) {
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        } else {
            int[][] d = new int[n + 1][m + 1];

            int i;
            for (i = 0; i <= n; d[i][0] = i++) {
                ;
            }

            int j;
            for (j = 0; j <= m; d[0][j] = j++) {
                ;
            }

            for (i = 1; i <= n; ++i) {
                char s_i = s.charAt(i - 1);

                for (j = 1; j <= m; ++j) {
                    char t_j = t.charAt(j - 1);
                    byte cost;
                    if (s_i == t_j) {
                        cost = 0;
                    } else {
                        cost = 1;
                    }

                    d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
                }
            }

            return d[n][m];
        }
    }

    //获取字符串中的所有数字 并排序
    private static String getNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        char[] s1 = m.replaceAll("").trim().toCharArray();
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < i; j++) {
                if (s1[i] < s1[j]) {
                    char temp = s1[i];
                    s1[i] = s1[j];
                    s1[j] = temp;
                }
            }
        }
        String st = new String(s1);
        return st;
    }

    private static double numAdd(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }

    public static String getParenthesesCon(String msg) {
        List<String> list = new ArrayList<String>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '(') {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (msg.charAt(i) == ')') {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(msg.substring(start + 1, i));
                    break;
                }
            }
        }
        StringBuilder str = new StringBuilder();
        for (String string : list) {
            str.append(string);
        }
        return str.toString();
    }

    public static String replace(String str) {
        if (!str.contains("(")) {
            String reg = "[^\u4e00-\u9fa5]";
            str = str.replaceAll(reg, "");
            return str;
        }
        String bracket = "(" + getParenthesesCon(str) + ")";
        str = str.replace(bracket, "");
        String reg = "[^\u4e00-\u9fa5]";
        str = str.replaceAll(reg, "");
        return str;
    }

    /**
     * <截取字符串所有中文 ,若无中文原样返回>
     *
     * @Author: Zhengbin.jin 金正斌
     * @Email: Zhengbin.jin@rograndec.com
     * @2018/5/4 14:16
     */
    public static String getCnStr(String str) {
        String reg = "[^\u4e00-\u9fa5]";
        String cnstr = str.replaceAll(reg, "");
        if (StrUtil.isEmpty(cnstr)) {
            return str;
        }
        return cnstr;
    }

    /**
     * 去除两符号间内容
     * @param context
     * @return
     */
    public static String clearBracket(String context) {
       return context.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）","");
    }

    
    /**
     * 验证是不是数字
     * @param str
     * @return
     * @author wanfei.zhang
     * @date 2018年5月2日
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 替换中文的符号
     * @return
     * @author wanfei.zhang
     * @date 2018年5月2日
     */
    public static String trimChineseSymbols(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        return str.replaceAll(" ", "").replaceAll("（", "(").replaceAll("）", ")").replaceAll("：", ":")
            .replaceAll("，", ",").replaceAll("。", ".").replaceAll("；", ":").toLowerCase();
    }


}