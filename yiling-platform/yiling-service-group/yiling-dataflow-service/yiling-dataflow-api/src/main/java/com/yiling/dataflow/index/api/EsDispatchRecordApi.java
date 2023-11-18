package com.yiling.dataflow.index.api;

import java.util.Date;

import com.yiling.dataflow.index.dto.request.SaveEsDispatchRecordRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsDispatchRecordApi {
    Date getLastMaxTime();
    boolean insertEsDispatchRecord(SaveEsDispatchRecordRequest recordRequest);
}
