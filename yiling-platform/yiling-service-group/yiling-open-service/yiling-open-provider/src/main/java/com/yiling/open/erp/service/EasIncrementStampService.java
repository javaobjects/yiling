package com.yiling.open.erp.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.open.erp.dto.EasIncrementStampDTO;
import com.yiling.open.erp.dto.request.SaveEasIncrementStampRequest;
import com.yiling.open.erp.entity.EasIncrementStampDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-02
 */
public interface EasIncrementStampService extends BaseService<EasIncrementStampDO> {

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
