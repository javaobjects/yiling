package com.yiling.order.order.util;

import java.util.Map;

import com.yiling.order.order.bo.OrderDetailBatchBO;

import cn.hutool.core.map.MapUtil;
import lombok.experimental.UtilityClass;

/**
 * @author zhigang.guo
 * @date: 2021/8/5
 */
@UtilityClass
public class OrderUtil {

    /**
     * 反审核收货redisKey
     */
    public String UNRECEIVEERPONLINELOCK = "un-receive-erp-online-lock";

    /**
     * 反审发货redisKey
     */
    public String UNRSENDPONLINELOCK = "un-send-erp-online-lock";

    /**
     * 反审获取数量
     * @param resultMap
     * @param orderDetailBatchBo
     * @return
     */
    public Integer getQtyFromResultMap(Map<String,Integer> resultMap, OrderDetailBatchBO orderDetailBatchBo){

        if (MapUtil.isEmpty(resultMap)) {

            return null;
        }

        return  resultMap.get(orderDetailBatchBo.getBatchKey());

    }

}
