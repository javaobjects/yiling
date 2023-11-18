package com.yiling.bi.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.yiling.bi.BaseTest;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2022/9/22
 */
public class CombinationTest extends BaseTest {

    // TODO 方法1
//    public static void main(String[] args) {
//        String[] array = new String[] {"1", "2", "3", "4", "5", "6", "7","8", "9", "0"};
//        arrangeAll(Arrays.asList(array), "");
//    }
//
//    public static void arrangeAll(List<String> array, String prefix){
//        System.out.println(prefix);
//        for (int i = 0; i < array.size(); i++) {
//            List<String> temp = new LinkedList<>(array);
//            arrangeAll(temp, prefix + temp.remove(i));
//        }
//    }


    private static List<Integer> outArr = new ArrayList<Integer>();
    /*outArr内存放的数量*/
    private static int index=0;
    /**
     *
     * @param inArr 待组合的数组
     * @param n 表示inArr位置，如n=0则从inArr[0]-inArr[length-1]选取数字；n=1，则从inArr[1]-inArr[length-1]选取数字，length为数组长度
     * @param num 要选出数字的数量
     */
    public static void combNoRep(int[] inArr, int n , int num) {
        if (num <= 0) { //num=0 递归出口
            for (int i : outArr) {
                System.out.print(i);
            }
            System.out.println();//打印数组
        } else {
            for (int i = n; i <= inArr.length - num; i++) {
                if (outArr.size() < index + 1) {
                    outArr.add(index++, inArr[i]);
                } /*此处两行为防止集合越界空指针异常，如果使用数组存放也可，即此处实现了选取一个元素放进输出数组里，对应上述文字说明2的每一步的a操作*/
                else {
                    outArr.set(index++, inArr[i]);
                }
                combNoRep(inArr, i + 1, num - 1); /*在选取的元素后面的元素中选取num-1个元素，对应文字说明2中每一步的b操作*/
                index--; //重新给输出数组定位，选取一下个序号的元素
            }
        }
    }

    public static void combination(List<Goods> goodsList, int n , int num) {
        if (num <= 0) { //num=0 递归出口
            for (Goods goods : goodsCombinationList) {
                System.out.print(goods.getId());
            }

            System.out.println();//打印数组
        } else {
            for (int i = n; i <= goodsList.size() - num; i++) {
                if (goodsCombinationList.size() < index + 1) {
                    goodsCombinationList.add(index++, goodsList.get(i));
                } /*此处两行为防止集合越界空指针异常，如果使用数组存放也可，即此处实现了选取一个元素放进输出数组里，对应上述文字说明2的每一步的a操作*/
                else {
                    goodsCombinationList.set(index++, goodsList.get(i));
                }
                combination(goodsList, i + 1, num - 1); /*在选取的元素后面的元素中选取num-1个元素，对应文字说明2中每一步的b操作*/
                index--; //重新给输出数组定位，选取一下个序号的元素
            }
        }

    }

    private static List<Goods> goodsCombinationList = new ArrayList<>();

    public static void main(String[] arg) {
//        int[] arr = {3, 2, 6, 4, 5};
//        combNoRep(arr,0,4);

        List<Goods> goodsList = Arrays.asList(
                new Goods(1, 3),
                new Goods(2, 2),
                new Goods(3, 6),
                new Goods(4, 4),
                new Goods(5, 5)
        );
        for (int i = 1; i <= 6; i ++) {
            combination(goodsList, 0, i);
        }

//        combination(goodsList, 0, 4);


    }

    @Data
    @AllArgsConstructor
    public static class Goods {
        private Integer id;

        private Integer total;
    }


}
