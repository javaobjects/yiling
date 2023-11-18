package com.yiling.bi.protocol.service;

import com.yiling.bi.protocol.dto.request.InputTthreelsflRecordRequest;
import com.yiling.bi.protocol.entity.InputTthreelsflRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/1/5
 */
public interface InputTthreelsflRecordService extends BaseService<InputTthreelsflRecordDO> {

    /**
     * 根据唯一索引saveOrUpdate
     * @param inputTthreelsflRecordDO
     */
    boolean saveOrUpdateByUnique(InputTthreelsflRecordRequest request);
}
