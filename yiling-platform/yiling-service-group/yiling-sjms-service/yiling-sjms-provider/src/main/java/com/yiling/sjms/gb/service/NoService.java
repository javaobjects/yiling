package com.yiling.sjms.gb.service;

import com.yiling.basic.no.enums.INoEnum;
import com.yiling.user.common.enums.NoEnum;

/**
 * 单据号 Service
 *
 * @author: wei.wang
 * @date: 2022/12/06
 */
public interface NoService {

    /**
     * 生成单据号
     *
     * @param noEnum
     * @return
     */
    String gen(NoEnum noEnum);

    String genNo(INoEnum noEnum);
}
