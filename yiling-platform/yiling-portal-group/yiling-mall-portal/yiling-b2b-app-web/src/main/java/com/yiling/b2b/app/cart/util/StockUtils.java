package com.yiling.b2b.app.cart.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 库存工具类
 *
 * @author: xuan.zhou
 * @date: 2021/6/22
 */
@UtilityClass
@Slf4j
public class StockUtils {

    /**
     * 库存比较值
     */
    public  final Long STOCK_COMPARE_VALUE = 3000L;

    /**
     * 库存数量转文本
     *
     * @param stockNum 库存数量
     * @return
     */
    public  String getStockText(Long stockNum) {
        if (stockNum > STOCK_COMPARE_VALUE) {
            return "库存 > 3000";
        } else if (stockNum == 0L) {
            return "无货";
        } else {
            return "库存 <= 3000";
        }
    }
}
