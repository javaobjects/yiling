package com.yiling.bi.protocol.service;

import com.yiling.bi.protocol.dto.request.InputTthreelsflAuxdisplayRecordRequest;
import com.yiling.bi.protocol.entity.InputTthreelsflAuxdisplayRecordDO;
import com.yiling.bi.protocol.entity.InputTthreelsflRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
public interface InputTthreelsflAuxdisplayRecordService extends BaseService<InputTthreelsflAuxdisplayRecordDO> {

    boolean saveOrUpdateByUnique(InputTthreelsflAuxdisplayRecordRequest request);
}
