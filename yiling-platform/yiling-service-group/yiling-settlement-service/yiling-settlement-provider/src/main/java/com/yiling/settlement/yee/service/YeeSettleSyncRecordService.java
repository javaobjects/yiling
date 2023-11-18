package com.yiling.settlement.yee.service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.yee.dto.YeeSettleSyncRecordDTO;
import com.yiling.settlement.yee.dto.request.QueryYeeSettleListByPageRequest;
import com.yiling.settlement.yee.entity.YeeSettleSyncRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 易宝结算记录同步表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-04
 */
public interface YeeSettleSyncRecordService extends BaseService<YeeSettleSyncRecordDO> {

    /**
     * 首次初始化数据
     */
    void initData();

    /**
     * 同步今日的会员结算单
     */
    void syncMemberSettleForToday();

    /**
     * 根据子账号和起止日期查询子账号的结算记录
     *
     * @param merchantNo
     * @param beginTime
     * @param endTime
     * @return
     */
    Map<String, String> queryYeeSettleRecord(String merchantNo, Date beginTime, Date endTime);

    /**
     * 分页查询结算记录
     *
     * @param request
     * @return
     */
    Page<YeeSettleSyncRecordDTO> queryListByPage(QueryYeeSettleListByPageRequest request);
}
