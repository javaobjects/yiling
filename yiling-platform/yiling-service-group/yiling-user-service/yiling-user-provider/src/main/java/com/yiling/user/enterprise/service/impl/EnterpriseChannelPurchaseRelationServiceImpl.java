package com.yiling.user.enterprise.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.dao.EnterpriseChannelPurchaseRelationMapper;
import com.yiling.user.enterprise.dto.request.QueryChannelPurchaseRelationRequest;
import com.yiling.user.enterprise.entity.EnterpriseChannelPurchaseRelationDO;
import com.yiling.user.enterprise.service.EnterpriseChannelPurchaseRelationService;

/**
 * <p>
 * 渠道采购关系 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
public class EnterpriseChannelPurchaseRelationServiceImpl extends BaseServiceImpl<EnterpriseChannelPurchaseRelationMapper, EnterpriseChannelPurchaseRelationDO> implements EnterpriseChannelPurchaseRelationService {
    @Override
    public List<EnterpriseChannelPurchaseRelationDO> listByChannelId(QueryChannelPurchaseRelationRequest request) {
        LambdaQueryWrapper<EnterpriseChannelPurchaseRelationDO> queryWrapper = new LambdaQueryWrapper();
        if (null != request.getBuyerChannelId()) {
            queryWrapper.eq(EnterpriseChannelPurchaseRelationDO::getBuyerChannelId, request.getBuyerChannelId());
        }
        if (null != request.getSellerChannelId()) {
            queryWrapper.eq(EnterpriseChannelPurchaseRelationDO::getSellerChannelId, request.getSellerChannelId());
        }
        return this.baseMapper.selectList(queryWrapper);
    }
}
