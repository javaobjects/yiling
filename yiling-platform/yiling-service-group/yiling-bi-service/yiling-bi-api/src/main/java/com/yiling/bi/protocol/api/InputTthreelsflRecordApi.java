package com.yiling.bi.protocol.api;

import com.yiling.bi.protocol.dto.InputTthreelsflRecordDTO;
import com.yiling.bi.protocol.dto.request.InputTthreelsflRecordRequest;

/**
 * @author fucheng.bai
 * @date 2023/1/5
 */
public interface InputTthreelsflRecordApi {

    boolean saveOrUpdateByUnique(InputTthreelsflRecordRequest request);
}
