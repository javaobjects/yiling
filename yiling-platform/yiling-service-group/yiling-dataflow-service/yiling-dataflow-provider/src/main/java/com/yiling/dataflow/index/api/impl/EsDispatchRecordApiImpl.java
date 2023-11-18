package com.yiling.dataflow.index.api.impl;

import java.util.Date;

import com.yiling.dataflow.index.api.EsDispatchRecordApi;
import com.yiling.dataflow.index.dto.request.SaveEsDispatchRecordRequest;
import com.yiling.dataflow.index.service.EsDispatchRecordService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@DubboService
public class EsDispatchRecordApiImpl implements EsDispatchRecordApi {

    @Autowired
    private EsDispatchRecordService esDispatchRecordService;

    @Override
    public Date getLastMaxTime() {
        return esDispatchRecordService.getLastMaxTime();
    }

    @Override
    public boolean insertEsDispatchRecord(SaveEsDispatchRecordRequest recordRequest) {
        return esDispatchRecordService.insertEsDispatchRecord(recordRequest);
    }
}
