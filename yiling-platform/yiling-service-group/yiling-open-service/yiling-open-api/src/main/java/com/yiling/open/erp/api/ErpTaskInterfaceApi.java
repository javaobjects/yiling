package com.yiling.open.erp.api;

import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;

/**
 * 任务信息管理
 * @author shuan
 */
public interface ErpTaskInterfaceApi {

    /**
     * 通过任务编号获取任务
     * @param taskNo
     * @return
     */
    ErpTaskInterfaceDTO findErpTaskInterfaceByTaskNo(String taskNo);
}
