package com.yiling.framework.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.NumberUtil;

/**
 * @author: xuan.zhou
 * @date: 2020-6-8
 */
public class VersionUtils {

    /**
     * 比较APP版本号的大小
     * <p>
     * 1、前者大则返回一个正数
     * 2、后者大返回一个负数
     * 3、相等则返回0
     *
     * @param version1 app版本号
     * @param version2 app版本号
     * @return int
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            throw new RuntimeException("版本号不能为空");
        }

        // 注意此处为正则匹配，不能用.
        List<String> versionList1 = new ArrayList<>(Arrays.asList(version1.split("\\.")));
        List<String> versionList2 = new ArrayList<>(Arrays.asList(version2.split("\\.")));

        // 如果版本号位数不等，则先凑成相同位数的版本号
        int n = versionList1.size() - versionList2.size();
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                versionList2.add("0");
            }
        } else if (n < 0) {
            for (int i = 0; i < 0 - n; i++) {
                versionList1.add("0");
            }
        }

        for (int i = 0, m = versionList1.size(); i < m; i++) {
            int result = NumberUtil.compare(Integer.parseInt(versionList1.get(i)), Integer.parseInt(versionList2.get(i)));
            if (result != 0) {
                return result;
            }
        }

        return 0;
    }

    public static void main (String[] args) {
        System.out.println(compareVersion("1.0.1", "1.1"));
        System.out.println(compareVersion("1.1.1.1", "1.1.1"));
    }
}
