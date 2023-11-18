package com.yiling.dataflow.order.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * erp流向工具类
 *
 * @author: houjie.sun
 * @date: 2022/2/18
 */
public class FlowCommonUtil {

    /**
     * 检查默认时间
     *
     * @param time
     * @return
     */
    public static Date parseFlowDefaultTime(Date time) {
        if (ObjectUtil.isNotNull(time) && ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"))) {
            return null;
        }
        return time;
    }

    /**
     * 分批拆分集合
     *
     * @param list 源数据集合
     * @param subSize 拆分后集合内的集合长度
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> subList(List<T> list, int subSize) {
        // listSize为集合长度
        int listSize = list.size();
        // 用map存起来新的分组后数据
        List<List<T>> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += subSize) {
            //作用为Index最后没有subSize条数据，则剩余的条数newList中就装几条
            if (i + subSize > listSize) {
                subSize = listSize - i;
            }
            // 使用subList方法，keyToken用来记录循环了多少次或者每个map数据的键值
            List newList = list.subList(i, i + subSize);
            // 每取一次放到map集合里，然后
            returnList.add(newList);
        }
        return returnList;
    }
}
