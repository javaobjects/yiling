package com.yiling.marketing.integralmessage.service.impl;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integralmessage.dao.IntegralExchangeMessageConfigMapper;
import com.yiling.marketing.integralmessage.dto.IntegralExchangeMessageConfigDTO;
import com.yiling.marketing.integralmessage.dto.request.DeleteIntegralMessageRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessageListRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessagePageRequest;
import com.yiling.marketing.integralmessage.dto.request.SaveIntegralExchangeMessageConfigRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageOrderRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageStatusRequest;
import com.yiling.marketing.integralmessage.entity.IntegralExchangeMessageConfigDO;
import com.yiling.marketing.integralmessage.service.IntegralExchangeMessageConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.common.util.WrapperUtils;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换消息配置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Slf4j
@Service
public class IntegralExchangeMessageConfigServiceImpl extends BaseServiceImpl<IntegralExchangeMessageConfigMapper, IntegralExchangeMessageConfigDO> implements IntegralExchangeMessageConfigService {

    @Override
    public Page<IntegralExchangeMessageConfigDTO> queryListPage(QueryIntegralMessagePageRequest request) {
        QueryWrapper<IntegralExchangeMessageConfigDO> wrapper = WrapperUtils.getWrapper(request);
        // 排序规则：排序倒序、结束时间正序、开始时间正序、创建时间正序
        wrapper.lambda().orderByDesc(IntegralExchangeMessageConfigDO::getSort).orderByAsc(IntegralExchangeMessageConfigDO::getEndTime)
                .orderByAsc(IntegralExchangeMessageConfigDO::getStartTime).orderByAsc(IntegralExchangeMessageConfigDO::getCreateTime);

        return PojoUtils.map(this.page(request.getPage(), wrapper), IntegralExchangeMessageConfigDTO.class);
    }

    @Override
    public List<IntegralExchangeMessageConfigDTO> queryList(QueryIntegralMessageListRequest request) {
        QueryWrapper<IntegralExchangeMessageConfigDO> wrapper = WrapperUtils.getWrapper(request);
        // 排序规则：排序倒序、结束时间正序、开始时间正序、创建时间正序
        wrapper.lambda().orderByDesc(IntegralExchangeMessageConfigDO::getSort).orderByAsc(IntegralExchangeMessageConfigDO::getEndTime)
                .orderByAsc(IntegralExchangeMessageConfigDO::getStartTime).orderByAsc(IntegralExchangeMessageConfigDO::getCreateTime);
        if (Objects.nonNull(request.getLimit()) && request.getLimit() > 0) {
            wrapper.last("limit " + request.getLimit());
        }

        return PojoUtils.map(this.list(wrapper), IntegralExchangeMessageConfigDTO.class);
    }

    @Override
    public boolean saveConfig(SaveIntegralExchangeMessageConfigRequest request) {
        IntegralExchangeMessageConfigDO exchangeMessageConfigDO = PojoUtils.map(request, IntegralExchangeMessageConfigDO.class);

        if (Objects.isNull(request.getId()) || request.getId() == 0) {
            return this.save(exchangeMessageConfigDO);
        } else {
            return this.updateById(exchangeMessageConfigDO);

        }

    }

    @Override
    public boolean deleteConfig(DeleteIntegralMessageRequest request) {
        IntegralExchangeMessageConfigDO exchangeMessageConfigDO = new IntegralExchangeMessageConfigDO();
        exchangeMessageConfigDO.setId(request.getId());
        exchangeMessageConfigDO.setOpUserId(request.getOpUserId());
        return this.deleteByIdWithFill(exchangeMessageConfigDO) > 0;
    }

    @Override
    public boolean updateStatus(UpdateIntegralMessageStatusRequest request) {
        IntegralExchangeMessageConfigDO messageConfigDO = PojoUtils.map(request, IntegralExchangeMessageConfigDO.class);
        return this.updateById(messageConfigDO);
    }

    @Override
    public boolean updateOrder(UpdateIntegralMessageOrderRequest request) {
        IntegralExchangeMessageConfigDO messageConfigDO = PojoUtils.map(request, IntegralExchangeMessageConfigDO.class);
        return this.updateById(messageConfigDO);
    }
}
