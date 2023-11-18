package com.yiling.bi.protocol.service;

import com.yiling.bi.protocol.dto.request.InputTthreelsflLhauxdisplayRecordRequest;
import com.yiling.bi.protocol.entity.InputTthreelsflLhauxdisplayRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
public interface InputTthreelsflLhauxdisplayRecordService extends BaseService<InputTthreelsflLhauxdisplayRecordDO> {

    boolean saveOrUpdateByUnique(InputTthreelsflLhauxdisplayRecordRequest request);
}
