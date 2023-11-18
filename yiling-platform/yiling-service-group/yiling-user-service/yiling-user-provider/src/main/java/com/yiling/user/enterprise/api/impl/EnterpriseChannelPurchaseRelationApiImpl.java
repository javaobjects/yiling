package com.yiling.user.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseChannelPurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseChannelPurchaseRelationDTO;
import com.yiling.user.enterprise.dto.request.QueryChannelPurchaseRelationRequest;
import com.yiling.user.enterprise.entity.EnterpriseChannelPurchaseRelationDO;
import com.yiling.user.enterprise.service.EnterpriseChannelPurchaseRelationService;

/**
 * 渠道采购关系apiImpl
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
@DubboService
public class EnterpriseChannelPurchaseRelationApiImpl implements EnterpriseChannelPurchaseRelationApi {

    @Autowired
    private EnterpriseChannelPurchaseRelationService enterpriseChannelPurchaseRelationService;

    @Override
    public List<EnterpriseChannelPurchaseRelationDTO> listByChannelId(QueryChannelPurchaseRelationRequest request) {
        List<EnterpriseChannelPurchaseRelationDO> list = enterpriseChannelPurchaseRelationService.listByChannelId(request);
        return PojoUtils.map(list, EnterpriseChannelPurchaseRelationDTO.class);
    }
}
