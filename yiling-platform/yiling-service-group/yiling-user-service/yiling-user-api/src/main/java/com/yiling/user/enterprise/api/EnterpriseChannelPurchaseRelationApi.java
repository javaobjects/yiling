package com.yiling.user.enterprise.api;

import java.util.List;

import com.yiling.user.enterprise.dto.EnterpriseChannelPurchaseRelationDTO;
import com.yiling.user.enterprise.dto.request.QueryChannelPurchaseRelationRequest;

/**
 * 渠道采购关系 Api
 *
 * @author yuecheng.chen
 * @date 2021-06-09
 */
public interface EnterpriseChannelPurchaseRelationApi {
    /**
     * 查询渠道采购关系
     *
     * @param request
     * @return
     */
    List<EnterpriseChannelPurchaseRelationDTO> listByChannelId(QueryChannelPurchaseRelationRequest request);
}
