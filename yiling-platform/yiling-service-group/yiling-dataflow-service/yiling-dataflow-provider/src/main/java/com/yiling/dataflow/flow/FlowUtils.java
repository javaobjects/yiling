package com.yiling.dataflow.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import cn.hutool.core.collection.CollUtil;

/**
 * 流向工具类
 *
 * @author: houjie.sun
 * @date: 2023/1/10
 */
public class FlowUtils {

    public static <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> subList = new ArrayList<>();
        if (CollUtil.isEmpty(list)) {
            return subList;
        }
        list = list.stream().distinct().collect(Collectors.toList());
        if (list.size() > size) {
            subList = Lists.partition(list, size);
        } else {
            subList.add(list);
        }
        return subList;
    }
}
