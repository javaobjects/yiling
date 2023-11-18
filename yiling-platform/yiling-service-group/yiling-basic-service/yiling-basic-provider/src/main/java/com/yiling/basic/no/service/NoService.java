package com.yiling.basic.no.service;

import org.springframework.beans.factory.annotation.Qualifier;

import com.yiling.basic.no.enums.INoEnum;

/**
 * 单据号 Service
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Qualifier("commonNoService")
public interface NoService {

    /**
     * 生成单据号
     *
     * @param noEnum
     * @return
     */
    String gen(INoEnum noEnum);
}
