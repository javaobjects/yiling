package com.yiling.open.erp.api;

import com.yiling.open.erp.dto.EasIncrementStampDTO;
import com.yiling.open.erp.dto.request.SaveEasIncrementStampRequest;

/**
 * 对接商业公司管理
 * @author shuan
 */
public interface EasIncrementStampApi {

    /**
     * 通过key获取客户信息
     * @param suId
     * @param taskNo
     * @return
     */
    EasIncrementStampDTO findEasIncrementStampBySuIdAndTaskNo(Long suId,String taskNo);

    /**
     * 通过key获取客户信息
     * @param request
     * @return
     */
    boolean saveOrUpdateEasIncrementStamp(SaveEasIncrementStampRequest request);
}
