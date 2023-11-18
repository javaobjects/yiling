package com.yiling.sales.assistant.app.cart.util;

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


    /**
     * 获取分享订单加密地址
     * @param str
     * @return
     */
    public String getEncryptHexStr(String str) {
        AES aes = new AES("CBC","PKCS7Padding",
                //密钥，可以自定义
                "0123456789ABHAEQ".getBytes(),
                //iv加盐，按照实际需求添加
                "DYgjCEIMVrj2W9xN".getBytes());

        Map<String, Object> data = new HashMap<>();

        data.put("content", str);
        // 设置失效时间 24 小时
        data.put("date", DateUtil.formatDateTime(DateUtil.offsetHour(new Date(),24)));
        String encryptHex = aes.encryptHex(JSONUtil.toJsonStr(data));

        return encryptHex;
    }


    /**
     * 获取解密后的订单号，如果时间过期，返回空
     * @param content
     * @return
     */
    public String getDecryptStr (String content) {

        boolean flag = Validator.isHex(content);

        if (!flag) {

            return "";
        }

        Map mapType = null;

        try {
            AES aes4 = new AES("CBC","PKCS7Padding",
                    //密钥，可以自定义
                    "0123456789ABHAEQ".getBytes(),
                    //iv加盐，按照实际需求添加
                    "DYgjCEIMVrj2W9xN".getBytes());

            String decryptStr = aes4.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
            String str = JSONUtil.toJsonPrettyStr(decryptStr);
            mapType = JSON.parseObject(str,Map.class);

        } catch (RuntimeException e) {

            log.debug("getDecryptStr,error",e);

            return "";
        }

        if (mapType.get("date") == null) {

            return null;
        }

        // 上次转发的时间
        Date transferTime = DateUtil.parse(mapType.get("date").toString(), DatePattern.NORM_DATETIME_FORMAT);

        if (transferTime.before(new Date())) {

            return null;
        }
        return Optional.ofNullable(mapType.get("content")).map(e -> e.toString()).orElse(null);
    }
}
