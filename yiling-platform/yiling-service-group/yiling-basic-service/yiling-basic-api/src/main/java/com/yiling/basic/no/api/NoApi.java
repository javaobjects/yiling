package com.yiling.basic.no.api;

import com.yiling.basic.no.enums.INoEnum;

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
    String gen(INoEnum noEnum);
}
