package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.QueryChannelPurchaseRelationRequest;
import com.yiling.user.enterprise.entity.EnterpriseChannelPurchaseRelationDO;

/**
 * <p>
 * 渠道采购关系 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface EnterpriseChannelPurchaseRelationService extends BaseService<EnterpriseChannelPurchaseRelationDO> {
    /**
     * 查询渠道采购关系
     *
     * @param request
     * @return
     */
    List<EnterpriseChannelPurchaseRelationDO> listByChannelId(QueryChannelPurchaseRelationRequest request);
}
