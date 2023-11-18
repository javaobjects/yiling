package com.yiling.bi.order.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fucheng.bai
 * @date 2022/10/10
 */
public class CollectionUtils {


    public static <T> List<List<T>> subList(List<T> list) {
        return subList(list, 1000);
    }
    public static <T> List<List<T>> subList(List<T> list, int splitSize) {
        List<List<T>> lists = new ArrayList<>();
        int size = list.size();
        if (size <= splitSize) {
            lists.add(list);
            return lists;
        }
        int number = size / splitSize;
        //完整的分隔部分
        for (int i = 0; i < number; i++) {
            int startIndex = i * splitSize;
            int endIndex = (i +1) * splitSize;
            lists.add(list.subList(startIndex, endIndex));
        }
        //最后分隔剩下的部分直接放入list
        if (number * splitSize == size) {
            return lists;
        }
        lists.add(list.subList(number * splitSize, size));
        return lists;
    }
}
