package com.yiling.open.third.service;

import java.util.Map;

/**
 * @author: shuang.zhang
 * @date: 2022/4/11
 */
public interface BaseFlowInterfaceService {

    /**
     * 抓取数据接口
     *
     * @param param 请求参数
     * @param purchaseMapping 映射json
     * @param saleMapping 映射json
     * @param goodsBatchMapping 映射json
     * @param suId 处理商业公司Id
     */
    void process(Map<String, String> param, String purchaseMapping, String saleMapping, String goodsBatchMapping, Long suId, Integer flowDay);
}
