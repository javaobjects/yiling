package com.yiling.open.erp.api;

import java.util.List;

import com.yiling.open.erp.dto.ErpDeleteDataDTO;
import com.yiling.open.erp.dto.request.SaveErpDeleteDataRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/8/13
 */
public interface ErpDeleteDataApi {

    /**
     * 通过key获取客户信息
     * @param request
     * @return
     */
    boolean saveOrUpdateErpDeleteData(SaveErpDeleteDataRequest request);

    /**
     * 通过key获取客户信息
     * @param request
     * @return
     */
    boolean updateErpDeleteData(SaveErpDeleteDataRequest request);

    /**
     * 通过key获取客户信息
     * @param suId
     * @param taskNo
     * @return
     */
    List<ErpDeleteDataDTO> findErpDeleteBySuIdAndTaskNo(Long suId, String taskNo);
}
