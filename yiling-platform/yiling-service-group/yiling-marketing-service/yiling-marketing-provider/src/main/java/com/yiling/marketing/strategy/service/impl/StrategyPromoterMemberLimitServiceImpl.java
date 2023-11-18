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
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayPromoterMemberLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionActivityService;
import com.yiling.marketing.presale.entity.MarketingPresalePromoterMemberLimitDO;
import com.yiling.marketing.presale.service.MarketingPresaleMemberLimitService;
import com.yiling.marketing.presale.service.MarketingPresalePromoterMemberLimitService;
import com.yiling.marketing.strategy.dao.StrategyPromoterMemberLimitMapper;
import com.yiling.marketing.strategy.dto.request.AddPresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresalePromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyPromoterMemberLimitDO;
import com.yiling.marketing.strategy.service.StrategyPromoterMemberLimitService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 策略满赠推广方会员表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-29
 */
@Service
public class StrategyPromoterMemberLimitServiceImpl extends BaseServiceImpl<StrategyPromoterMemberLimitMapper, StrategyPromoterMemberLimitDO> implements StrategyPromoterMemberLimitService {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @Autowired
    MarketingPayPromoterMemberLimitService payPromoterMemberLimitService;
    @Autowired
    MarketingPresalePromoterMemberLimitService promoterMemberLimitService;

    @Override
    public boolean add(AddStrategyPromoterMemberLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            List<StrategyPromoterMemberLimitDO> limitDOList = this.listByActivityIdAndEidList(request.getMarketingStrategyId(), new ArrayList<Long>() {{
                add(request.getEid());
            }});
            if (CollUtil.isNotEmpty(limitDOList)) {
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            StrategyPromoterMemberLimitDO promoterMemberLimitDO = PojoUtils.map(request, StrategyPromoterMemberLimitDO.class);
            promoterMemberLimitDO.setEname(enterpriseDTO.getName());
            return this.save(promoterMemberLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<StrategyPromoterMemberLimitDO> promoterMemberLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                List<StrategyPromoterMemberLimitDO> limitDOList = this.listByActivityIdAndEidList(request.getMarketingStrategyId(), new ArrayList<Long>() {{
                    add(enterpriseDTO.getId());
                }});
                if (CollUtil.isNotEmpty(limitDOList)) {
                    continue;
                }
                StrategyPromoterMemberLimitDO promoterMemberLimitDO = new StrategyPromoterMemberLimitDO();
                promoterMemberLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                promoterMemberLimitDO.setEid(enterpriseDTO.getId());
                promoterMemberLimitDO.setEname(enterpriseDTO.getName());
                promoterMemberLimitDO.setOpUserId(request.getOpUserId());
                promoterMemberLimitDO.setOpTime(request.getOpTime());
                promoterMemberLimitDOList.add(promoterMemberLimitDO);
            }
            if (CollUtil.isNotEmpty(promoterMemberLimitDOList)) {
                return this.saveBatch(promoterMemberLimitDOList);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addForPresale(AddPresalePromoterMemberLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            List<MarketingPresalePromoterMemberLimitDO> limitDOList = this.listByActivityIdAndEidListForPresale(request.getMarketingPresaleId(), new ArrayList<Long>() {{
                add(request.getEid());
            }});
            if (CollUtil.isNotEmpty(limitDOList)) {
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            MarketingPresalePromoterMemberLimitDO promoterMemberLimitDO = PojoUtils.map(request, MarketingPresalePromoterMemberLimitDO.class);
            promoterMemberLimitDO.setEname(enterpriseDTO.getName());
            return promoterMemberLimitService.save(promoterMemberLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<MarketingPresalePromoterMemberLimitDO> promoterMemberLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                List<MarketingPresalePromoterMemberLimitDO> limitDOList = this.listByActivityIdAndEidListForPresale(request.getMarketingPresaleId(), new ArrayList<Long>() {{
                    add(enterpriseDTO.getId());
                }});
                if (CollUtil.isNotEmpty(limitDOList)) {
                    continue;
                }
                MarketingPresalePromoterMemberLimitDO promoterMemberLimitDO = new MarketingPresalePromoterMemberLimitDO();
                promoterMemberLimitDO.setMarketingPresaleId(request.getMarketingPresaleId());
                promoterMemberLimitDO.setEid(enterpriseDTO.getId());
                promoterMemberLimitDO.setEname(enterpriseDTO.getName());
                promoterMemberLimitDO.setOpUserId(request.getOpUserId());
                promoterMemberLimitDO.setOpTime(request.getOpTime());
                promoterMemberLimitDOList.add(promoterMemberLimitDO);
            }
            if (CollUtil.isNotEmpty(promoterMemberLimitDOList)) {
                return promoterMemberLimitService.saveBatch(promoterMemberLimitDOList);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addForPayPromotion(AddStrategyPromoterMemberLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            ArrayList<Long> eidList = new ArrayList<Long>() {{
                add(request.getEid());
            }};
            QueryWrapper<MarketingPayPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            if (CollUtil.isNotEmpty(eidList)) {
                wrapper.lambda().in(MarketingPayPromoterMemberLimitDO::getEid, eidList);
            }
            List<MarketingPayPromoterMemberLimitDO> limitDOList =  payPromoterMemberLimitService.list(wrapper);
            if (CollUtil.isNotEmpty(limitDOList)) {
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            MarketingPayPromoterMemberLimitDO promoterMemberLimitDO = PojoUtils.map(request, MarketingPayPromoterMemberLimitDO.class);
            promoterMemberLimitDO.setMarketingPayId(request.getMarketingStrategyId());
            promoterMemberLimitDO.setEname(enterpriseDTO.getName());
            return payPromoterMemberLimitService.save(promoterMemberLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<MarketingPayPromoterMemberLimitDO> promoterMemberLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                ArrayList<Long> eidList = new ArrayList<Long>() {{
                    add(request.getEid());
                }};
                QueryWrapper<MarketingPayPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
                if (CollUtil.isNotEmpty(eidList)) {
                    wrapper.lambda().in(MarketingPayPromoterMemberLimitDO::getEid, eidList);
                }
                List<MarketingPayPromoterMemberLimitDO> limitDOList =  payPromoterMemberLimitService.list(wrapper);
                if (CollUtil.isNotEmpty(limitDOList)) {
                    continue;
                }
                MarketingPayPromoterMemberLimitDO promoterMemberLimitDO = new MarketingPayPromoterMemberLimitDO();
                promoterMemberLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                promoterMemberLimitDO.setEid(enterpriseDTO.getId());
                promoterMemberLimitDO.setEname(enterpriseDTO.getName());
                promoterMemberLimitDO.setOpUserId(request.getOpUserId());
                promoterMemberLimitDO.setOpTime(request.getOpTime());
                promoterMemberLimitDOList.add(promoterMemberLimitDO);
            }
            if (CollUtil.isNotEmpty(promoterMemberLimitDOList)) {
                return payPromoterMemberLimitService.saveBatch(promoterMemberLimitDOList);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        List<StrategyPromoterMemberLimitDO> promoterMemberLimitDOList = this.listByActivityIdAndEidList(oldId, null);
        for (StrategyPromoterMemberLimitDO promoterMemberLimitDO : promoterMemberLimitDOList) {
            promoterMemberLimitDO.setId(null);
            promoterMemberLimitDO.setMarketingStrategyId(strategyActivityDO.getId());
            promoterMemberLimitDO.setOpUserId(opUserId);
            promoterMemberLimitDO.setOpTime(opTime);
        }
        this.saveBatch(promoterMemberLimitDOList);
    }

    @Override
    public boolean delete(DeleteStrategyPromoterMemberLimitRequest request) {
        StrategyPromoterMemberLimitDO promoterMemberLimitDO = new StrategyPromoterMemberLimitDO();
        promoterMemberLimitDO.setDelFlag(1);
        promoterMemberLimitDO.setOpUserId(request.getOpUserId());
        promoterMemberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<StrategyPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPromoterMemberLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyPromoterMemberLimitDO::getEid, request.getEid());
            this.batchDeleteWithFill(promoterMemberLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(StrategyPromoterMemberLimitDO::getEid, request.getEidList());
            this.batchDeleteWithFill(promoterMemberLimitDO, wrapper);
        }
        return true;
    }

    @Override
    public boolean deleteForPresale(DeletePresalePromoterMemberLimitRequest request) {
        MarketingPresalePromoterMemberLimitDO promoterMemberLimitDO = new MarketingPresalePromoterMemberLimitDO();
        promoterMemberLimitDO.setDelFlag(1);
        promoterMemberLimitDO.setOpUserId(request.getOpUserId());
        promoterMemberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPresalePromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getMarketingPresaleId, request.getMarketingPresaleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getEid, request.getEid());
            promoterMemberLimitService.batchDeleteWithFill(promoterMemberLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPresalePromoterMemberLimitDO::getEid, request.getEidList());
            promoterMemberLimitService.batchDeleteWithFill(promoterMemberLimitDO, wrapper);
        }
        return true;
    }

    @Override
    public boolean deleteForPayPromotion(DeleteStrategyPromoterMemberLimitRequest request) {
        MarketingPayPromoterMemberLimitDO promoterMemberLimitDO = new MarketingPayPromoterMemberLimitDO();
        promoterMemberLimitDO.setDelFlag(1);
        promoterMemberLimitDO.setOpUserId(request.getOpUserId());
        promoterMemberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPayPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getEid, request.getEid());
            payPromoterMemberLimitService.batchDeleteWithFill(promoterMemberLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPayPromoterMemberLimitDO::getEid, request.getEidList());
            payPromoterMemberLimitService.batchDeleteWithFill(promoterMemberLimitDO, wrapper);
        }
        return true;
    }

    @Override
    public Page<MarketingPayPromoterMemberLimitDO> pageListForPayPromotion(QueryStrategyPromoterMemberLimitPageRequest request) {
        QueryWrapper<MarketingPayPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPayPromoterMemberLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(MarketingPayPromoterMemberLimitDO::getEname, request.getEname());
        }
        return payPromoterMemberLimitService.page(request.getPage(), wrapper);
    }

    @Override
    public Page<StrategyPromoterMemberLimitDO> pageList(QueryStrategyPromoterMemberLimitPageRequest request) {
        QueryWrapper<StrategyPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPromoterMemberLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyPromoterMemberLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(StrategyPromoterMemberLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(StrategyPromoterMemberLimitDO::getEname, request.getEname());
        }
        Page<StrategyPromoterMemberLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public Page<MarketingPresalePromoterMemberLimitDO> pageListForPresale(QueryPresalePromoterMemberLimitPageRequest request) {
        QueryWrapper<MarketingPresalePromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getMarketingPresaleId, request.getMarketingPresaleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPresalePromoterMemberLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(MarketingPresalePromoterMemberLimitDO::getEname, request.getEname());
        }
        Page<MarketingPresalePromoterMemberLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return promoterMemberLimitService.page(objectPage, wrapper);
    }

    @Override
    public Integer countPromoterMemberByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPromoterMemberLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategyPromoterMemberLimitDO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList) {
        QueryWrapper<StrategyPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyPromoterMemberLimitDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.lambda().in(StrategyPromoterMemberLimitDO::getEid, eidList);
        }
        return this.list(wrapper);
    }

    public List<MarketingPresalePromoterMemberLimitDO> listByActivityIdAndEidListForPresale(Long strategyActivityId, List<Long> eidList) {
        QueryWrapper<MarketingPresalePromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getMarketingPresaleId, strategyActivityId);
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.lambda().in(MarketingPresalePromoterMemberLimitDO::getEid, eidList);
        }
        return promoterMemberLimitService.list(wrapper);
    }
}
