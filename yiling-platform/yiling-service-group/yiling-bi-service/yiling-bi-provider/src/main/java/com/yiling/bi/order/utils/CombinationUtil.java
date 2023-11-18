package com.yiling.bi.order.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2022/9/26
 */
public class CombinationUtil {

    private final List<Combination> combinationList = new ArrayList<>();

    private final Map<String, Integer> resultMap = new LinkedHashMap<>();

    private int index = 0;


    public void combination(List<Combination> targetList, int n , int num) {
        if (num <= 0) { //num=0 递归出口
            StringBuilder sb = new StringBuilder();
            int quantitySum = 0;

            for (Combination combination : combinationList) {
                sb.append(combination.getId()).append(",");
                quantitySum = quantitySum + combination.getQuantity();

            }

            if (sb.toString().endsWith(",")) {
                String ids = sb.substring(0, sb.lastIndexOf(","));
                resultMap.put(ids, quantitySum);
            }

            System.out.println();//打印数组
        } else {
            for (int i = n; i <= targetList.size() - num; i++) {
                if (combinationList.size() < index + 1) {
                    combinationList.add(index++, targetList.get(i));
                } /*此处两行为防止集合越界空指针异常，如果使用数组存放也可，即此处实现了选取一个元素放进输出数组里*/
                else {
                    combinationList.set(index++, targetList.get(i));
                }
                combination(targetList, i + 1, num - 1); /*在选取的元素后面的元素中选取num-1个元素*/
                index--; //重新给输出数组定位，选取一下个序号的元素
            }
        }

    }

    public Map<String, Integer> getResultMap() {
        return resultMap;
    }

    @Data
    public static class Combination {

        private Long id;

        private Integer quantity;
    }


}
