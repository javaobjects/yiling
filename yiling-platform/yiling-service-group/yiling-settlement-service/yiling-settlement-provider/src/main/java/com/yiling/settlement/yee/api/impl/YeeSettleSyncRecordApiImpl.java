package com.yiling.settlement.yee.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.yee.api.YeeSettleSyncRecordApi;
import com.yiling.settlement.yee.dto.YeeSettleSyncRecordDTO;
import com.yiling.settlement.yee.dto.request.QueryYeeSettleListByPageRequest;
import com.yiling.settlement.yee.service.YeeSettleSyncRecordService;

/**
 * @author: dexi.yao
 * @date: 2023-04-04
 */
@DubboService
public class YeeSettleSyncRecordApiImpl implements YeeSettleSyncRecordApi {

    @Autowired
    YeeSettleSyncRecordService settleSyncRecordService;

    @Override
    public void initData() {
        settleSyncRecordService.initData();
    }

    @Override
    public void syncMemberSettleForToday() {
        settleSyncRecordService.syncMemberSettleForToday();
    }

    @Override
    public Page<YeeSettleSyncRecordDTO> queryListByPage(QueryYeeSettleListByPageRequest request) {
        return settleSyncRecordService.queryListByPage(request);
    }
}
