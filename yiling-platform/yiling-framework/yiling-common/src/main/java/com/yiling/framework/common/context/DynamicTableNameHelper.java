package com.yiling.framework.common.context;
/**
 * 动态表名请求参数传递
 *
 */
public class DynamicTableNameHelper {

    private static final ThreadLocal<String> TABLE_NAMES = new ThreadLocal<>();

    private DynamicTableNameHelper() {

    }


    public static void set(String stationNo) {
        TABLE_NAMES.set(stationNo);
    }

    public static void remove() {
        TABLE_NAMES.remove();
    }

    public static String get() {
        return TABLE_NAMES.get();
    }
}
