package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionSellerLimitService;
import com.yiling.marketing.strategy.dao.StrategySellerLimitMapper;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategySellerLimitDO;
import com.yiling.marketing.strategy.service.StrategySellerLimitService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 策略满赠商家表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Service
public class StrategySellerLimitServiceImpl extends BaseServiceImpl<StrategySellerLimitMapper, StrategySellerLimitDO> implements StrategySellerLimitService {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Autowired
    MarketingPayPromotionSellerLimitService payPromotionSellerLimitService;

    @Override
    public boolean add(AddStrategySellerLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            List<StrategySellerLimitDO> limitDOList = this.listByActivityIdAndEidList(request.getMarketingStrategyId(), new ArrayList<Long>() {{
                add(request.getEid());
            }});
            if (CollUtil.isNotEmpty(limitDOList)) {
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            StrategySellerLimitDO sellerLimitDO = PojoUtils.map(request, StrategySellerLimitDO.class);
            sellerLimitDO.setEname(enterpriseDTO.getName());
            return this.save(sellerLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<StrategySellerLimitDO> sellerLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                List<StrategySellerLimitDO> limitDOList = this.listByActivityIdAndEidList(request.getMarketingStrategyId(), new ArrayList<Long>() {{
                    add(enterpriseDTO.getId());
                }});
                if (CollUtil.isNotEmpty(limitDOList)) {
                    continue;
                }
                StrategySellerLimitDO sellerLimitDO = new StrategySellerLimitDO();
                sellerLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                sellerLimitDO.setEid(enterpriseDTO.getId());
                sellerLimitDO.setEname(enterpriseDTO.getName());
                sellerLimitDO.setOpUserId(request.getOpUserId());
                sellerLimitDO.setOpTime(request.getOpTime());
                sellerLimitDOList.add(sellerLimitDO);
            }
            if (CollUtil.isNotEmpty(sellerLimitDOList)) {
                return this.saveBatch(sellerLimitDOList);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        List<StrategySellerLimitDO> sellerLimitDOList = this.listSellerByActivityId(oldId);
        for (StrategySellerLimitDO sellerLimitDO : sellerLimitDOList) {
            sellerLimitDO.setId(null);
            sellerLimitDO.setMarketingStrategyId(strategyActivityDO.getId());
            sellerLimitDO.setOpUserId(opUserId);
            sellerLimitDO.setOpTime(opTime);
        }
        this.saveBatch(sellerLimitDOList);
    }

    @Override
    public boolean delete(DeleteStrategySellerLimitRequest request) {
        StrategySellerLimitDO sellerLimitDO = new StrategySellerLimitDO();
        sellerLimitDO.setDelFlag(1);
        sellerLimitDO.setOpUserId(request.getOpUserId());
        sellerLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<StrategySellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategySellerLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategySellerLimitDO::getEid, request.getEid());
            this.batchDeleteWithFill(sellerLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(StrategySellerLimitDO::getEid, request.getEidList());
            this.batchDeleteWithFill(sellerLimitDO, wrapper);
        }
        return true;
    }

    @Override
    public Page<StrategySellerLimitDO> pageList(QueryStrategySellerLimitPageRequest request) {
        QueryWrapper<StrategySellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategySellerLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategySellerLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(StrategySellerLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(StrategySellerLimitDO::getEname, request.getEname());
        }
        Page<StrategySellerLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public Integer countSellerByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategySellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategySellerLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategySellerLimitDO> listSellerByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategySellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategySellerLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public List<StrategySellerLimitDO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList) {
        QueryWrapper<StrategySellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategySellerLimitDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.lambda().in(StrategySellerLimitDO::getEid, eidList);
        }
        return this.list(wrapper);
    }

    @Override
    public Integer countSellerByActivityIdForPayPromotion(Long strategyActivityId) {
        QueryWrapper<MarketingPayPromotionSellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId, strategyActivityId);
        return payPromotionSellerLimitService.count(wrapper);
    }
}
