package com.yiling.common.web.util;

/**
 * 库存工具类
 *
 * @author: xuan.zhou
 * @date: 2021/6/22
 */
public class StockUtils {

    /**
     * 库存比较值
     */
    public static final Long STOCK_COMPARE_VALUE = 3000L;

    /**
     * 库存数量转文本
     *
     * @param stockNum 库存数量
     * @return
     */
    public static String getStockText(Long stockNum) {
        if (stockNum > STOCK_COMPARE_VALUE) {
            return "库存 > 3000";
        } else if (stockNum == 0L) {
            return "无货";
        } else {
            return "库存 <= 3000";
        }
    }
}
