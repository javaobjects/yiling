package com.yiling.order.order.service;

import com.yiling.order.order.enums.NoEnum;

/**
 * 单据号 Service
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
public interface NoService {

    /**
     * 生成单据号
     *
     * @param noEnum
     * @return
     */
    String gen(NoEnum noEnum);
}
