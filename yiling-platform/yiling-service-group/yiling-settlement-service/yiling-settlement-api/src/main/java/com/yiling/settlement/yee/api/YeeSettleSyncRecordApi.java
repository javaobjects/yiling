package com.yiling.settlement.yee.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.yee.dto.YeeSettleSyncRecordDTO;
import com.yiling.settlement.yee.dto.request.QueryYeeSettleListByPageRequest;

/**
 * @author: dexi.yao
 * @date: 2023-04-04
 */
public interface YeeSettleSyncRecordApi {

    /**
     * 首次初始化数据
     */
    void initData();

    /**
     * 同步今日的会员结算单
     */
    void syncMemberSettleForToday();

    /**
     * 分页查询结算记录
     *
     * @param request
     * @return
     */
    Page<YeeSettleSyncRecordDTO> queryListByPage(QueryYeeSettleListByPageRequest request);
}
