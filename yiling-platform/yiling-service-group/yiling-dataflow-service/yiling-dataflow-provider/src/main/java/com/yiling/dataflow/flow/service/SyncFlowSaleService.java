package com.yiling.dataflow.flow.service;

import java.util.List;

import com.yiling.dataflow.flow.entity.SyncFlowSaleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-21
 */
public interface SyncFlowSaleService extends BaseService<SyncFlowSaleDO> {
    /**
     * 批量插入id
     */
    void insertList(List<Long> ids);

    /**
     * 同步销售流向
     */
    void syncFlowSaleSummary();
}
