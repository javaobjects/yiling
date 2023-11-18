package com.yiling.open.erp.service;

import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.entity.ErpTaskInterfaceDO;

/**
 * @author: shuang.zhang
 * @date: 2022/6/9
 */
public interface ErpTaskInterfaceService {

    /**
     * 获取接口配置信息
     * @param taskNo
     * @return
     */
    ErpTaskInterfaceDTO findErpTaskInterfaceByTaskNo(String taskNo);
}
