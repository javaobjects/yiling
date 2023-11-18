package com.yiling.order.order.api;

import com.yiling.order.order.enums.NoEnum;

/**
 * 单据号 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
public interface NoApi {

    /**
     * 生成单据号
     *
     * @param noEnum
     * @return
     */
    String gen(NoEnum noEnum);
}
