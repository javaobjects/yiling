package com.yiling.bi.protocol.api;

import com.yiling.bi.protocol.dto.request.InputTthreelsflAuxdisplayRecordRequest;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
public interface InputTthreelsflAuxdisplayRecordApi {

    boolean saveOrUpdateByUnique(InputTthreelsflAuxdisplayRecordRequest request);
}
