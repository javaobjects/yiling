package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayBuyerLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayMemberLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromoterMemberLimitService;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyerLimitDO;
import com.yiling.marketing.presale.service.MarketingPresaleBuyerLimitService;
import com.yiling.marketing.strategy.dao.StrategyBuyerLimitMapper;
import com.yiling.marketing.strategy.dto.request.AddPresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleBuyerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyBuyerLimitDO;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.marketing.strategy.service.StrategyBuyerLimitService;
import com.yiling.marketing.strategy.service.StrategyMemberLimitService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 策略满赠客户 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Slf4j
@Service
public class StrategyBuyerLimitServiceImpl extends BaseServiceImpl<StrategyBuyerLimitMapper, StrategyBuyerLimitDO> implements StrategyBuyerLimitService {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @Autowired
    MarketingPayBuyerLimitService payBuyerLimitService;

    @Autowired
    MarketingPresaleBuyerLimitService presaleBuyerLimitService;

    @Autowired
    MarketingPayMemberLimitService payMemberLimitService;

    @Autowired
    MarketingPayPromoterMemberLimitService  payPromoterMemberLimitService;

    @Override
    public boolean add(AddStrategyBuyerLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            StrategyBuyerLimitDO strategyBuyerLimitDO = this.queryByActivityIdAndEid(request.getMarketingStrategyId(), request.getEid());
            if (Objects.nonNull(strategyBuyerLimitDO)) {
                // 已经添加了，不能重复添加
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            StrategyBuyerLimitDO buyerLimitDO = PojoUtils.map(request, StrategyBuyerLimitDO.class);
            buyerLimitDO.setEname(enterpriseDTO.getName());
            return this.save(buyerLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<StrategyBuyerLimitDO> strategyBuyerLimitDOList = this.listByActivityIdAndEidList(request.getMarketingStrategyId(), request.getEidList());
            List<Long> haveEidList = strategyBuyerLimitDOList.stream().map(StrategyBuyerLimitDO::getEid).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<StrategyBuyerLimitDO> buyerLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                if (haveEidList.contains(enterpriseDTO.getId())) {
                    continue;
                }
                StrategyBuyerLimitDO buyerLimitDO = new StrategyBuyerLimitDO();
                buyerLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                buyerLimitDO.setEid(enterpriseDTO.getId());
                buyerLimitDO.setEname(enterpriseDTO.getName());
                buyerLimitDO.setOpUserId(request.getOpUserId());
                buyerLimitDO.setOpTime(request.getOpTime());
                buyerLimitDOList.add(buyerLimitDO);
            }
            this.saveBatch(buyerLimitDOList);
            return true;
        } else {
            QueryEnterprisePageListRequest pageListRequest = new QueryEnterprisePageListRequest();
            pageListRequest.setStatus(1);
            pageListRequest.setAuthStatus(2);
            List<Integer> notInTypeList = new ArrayList<>();
            notInTypeList.add(1);
            notInTypeList.add(2);
            pageListRequest.setNotInTypeList(notInTypeList);
            if (Objects.nonNull(request.getEidPage())) {
                pageListRequest.setId(request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                pageListRequest.setName(request.getEnamePage());
            }
            Page<EnterpriseDTO> page;
            int current = 1;
            do {
                pageListRequest.setCurrent(current);
                pageListRequest.setSize(500);
                page = enterpriseApi.pageList(pageListRequest);
                if (CollUtil.isEmpty(page.getRecords())) {
                    continue;
                }
                if (page.getTotal() > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_BUYER_TO_MANY);
                }
                List<Long> enterpriseIdList = page.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                List<StrategyBuyerLimitDO> strategyBuyerLimitDOList = this.listByActivityIdAndEidList(request.getMarketingStrategyId(), enterpriseIdList);
                List<Long> haveEidList = strategyBuyerLimitDOList.stream().map(StrategyBuyerLimitDO::getEid).collect(Collectors.toList());
                List<StrategyBuyerLimitDO> buyerLimitDOList = new ArrayList<>();
                for (EnterpriseDTO enterpriseDTO : page.getRecords()) {
                    if (haveEidList.contains(enterpriseDTO.getId())) {
                        continue;
                    }
                    StrategyBuyerLimitDO buyerLimitDO = new StrategyBuyerLimitDO();
                    buyerLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                    buyerLimitDO.setEid(enterpriseDTO.getId());
                    buyerLimitDO.setEname(enterpriseDTO.getName());
                    buyerLimitDO.setOpUserId(request.getOpUserId());
                    buyerLimitDO.setOpTime(request.getOpTime());
                    buyerLimitDOList.add(buyerLimitDO);
                }
                if (CollUtil.isNotEmpty(buyerLimitDOList)) {
                    this.saveBatch(buyerLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(page.getRecords()));
            return true;
        }
    }

    @Override
    public boolean addForPayPromotion(AddStrategyBuyerLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayBuyerLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            wrapper.lambda().eq(MarketingPayBuyerLimitDO::getEid, request.getEid());
            wrapper.lambda().last(" limit 1");
            MarketingPayBuyerLimitDO strategyBuyerLimitDO = payBuyerLimitService.getOne(wrapper);
            if (Objects.nonNull(strategyBuyerLimitDO)) {
                // 已经添加了，不能重复添加
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            MarketingPayBuyerLimitDO buyerLimitDO = PojoUtils.map(request, MarketingPayBuyerLimitDO.class);
            buyerLimitDO.setMarketingPayId(request.getMarketingStrategyId());
            buyerLimitDO.setEname(enterpriseDTO.getName());
           return payBuyerLimitService.save(buyerLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayBuyerLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            if (CollUtil.isNotEmpty(request.getEidList())) {
                wrapper.lambda().in(MarketingPayBuyerLimitDO::getEid, request.getEidList());
            }
            List<MarketingPayBuyerLimitDO> strategyBuyerLimitDOList = payBuyerLimitService.list(wrapper);
            List<Long> haveEidList = strategyBuyerLimitDOList.stream().map(MarketingPayBuyerLimitDO::getEid).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<MarketingPayBuyerLimitDO> buyerLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                if (haveEidList.contains(enterpriseDTO.getId())) {
                    continue;
                }
                MarketingPayBuyerLimitDO buyerLimitDO = new MarketingPayBuyerLimitDO();
                buyerLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                buyerLimitDO.setEid(enterpriseDTO.getId());
                buyerLimitDO.setEname(enterpriseDTO.getName());
                buyerLimitDO.setOpUserId(request.getOpUserId());
                buyerLimitDO.setOpTime(request.getOpTime());
                buyerLimitDOList.add(buyerLimitDO);
            }
            return payBuyerLimitService.saveBatch(buyerLimitDOList);
        } else {
            QueryEnterprisePageListRequest pageListRequest = new QueryEnterprisePageListRequest();
            pageListRequest.setStatus(1);
            pageListRequest.setAuthStatus(2);
            List<Integer> notInTypeList = new ArrayList<>();
            notInTypeList.add(1);
            notInTypeList.add(2);
            pageListRequest.setNotInTypeList(notInTypeList);
            if (Objects.nonNull(request.getEidPage())) {
                pageListRequest.setId(request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                pageListRequest.setName(request.getEnamePage());
            }
            Page<EnterpriseDTO> page;
            int current = 1;
            do {
                pageListRequest.setCurrent(current);
                pageListRequest.setSize(500);
                page = enterpriseApi.pageList(pageListRequest);
                if (CollUtil.isEmpty(page.getRecords())) {
                    continue;
                }
                if (page.getTotal() > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_BUYER_TO_MANY);
                }
                List<Long> enterpriseIdList = page.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(MarketingPayBuyerLimitDO::getMarketingPayId, request.getMarketingStrategyId());
                if (CollUtil.isNotEmpty(request.getEidList())) {
                    wrapper.lambda().in(MarketingPayBuyerLimitDO::getEid, request.getEidList());
                }
                List<MarketingPayBuyerLimitDO> strategyBuyerLimitDOList = payBuyerLimitService.list(wrapper);
                List<Long> haveEidList = strategyBuyerLimitDOList.stream().map(MarketingPayBuyerLimitDO::getEid).collect(Collectors.toList());
                List<MarketingPayBuyerLimitDO> buyerLimitDOList = new ArrayList<>();
                for (EnterpriseDTO enterpriseDTO : page.getRecords()) {
                    if (haveEidList.contains(enterpriseDTO.getId())) {
                        continue;
                    }
                    MarketingPayBuyerLimitDO buyerLimitDO = new MarketingPayBuyerLimitDO();
                    buyerLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                    buyerLimitDO.setEid(enterpriseDTO.getId());
                    buyerLimitDO.setEname(enterpriseDTO.getName());
                    buyerLimitDO.setOpUserId(request.getOpUserId());
                    buyerLimitDO.setOpTime(request.getOpTime());
                    buyerLimitDOList.add(buyerLimitDO);
                }
                if (CollUtil.isNotEmpty(buyerLimitDOList)) {
                    payBuyerLimitService.saveBatch(buyerLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(page.getRecords()));
            return true;
        }
    }

    @Override
    public boolean addForPresale(AddPresaleBuyerLimitRequest request) {
        if (Objects.nonNull(request.getEid())) {
            List<Long> eid = new ArrayList<>();
            eid.add(request.getEid());
            List<MarketingPresaleBuyerLimitDO>  strategyBuyerLimitDO = this.listByActivityIdAndEidListForPresale(request.getMarketingPresaleId(), eid);
            if (CollectionUtils.isNotEmpty(strategyBuyerLimitDO)) {
                // 已经添加了，不能重复添加
                return false;
            }
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            MarketingPresaleBuyerLimitDO buyerLimitDO = PojoUtils.map(request, MarketingPresaleBuyerLimitDO.class);
            buyerLimitDO.setEname(enterpriseDTO.getName());
            return presaleBuyerLimitService.save(buyerLimitDO);
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<MarketingPresaleBuyerLimitDO>  strategyBuyerLimitDOList = this.listByActivityIdAndEidListForPresale(request.getMarketingPresaleId(), request.getEidList());
            List<Long> haveEidList = strategyBuyerLimitDOList.stream().map(MarketingPresaleBuyerLimitDO::getEid).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
            List<MarketingPresaleBuyerLimitDO> buyerLimitDOList = new ArrayList<>();
            for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
                if (haveEidList.contains(enterpriseDTO.getId())) {
                    continue;
                }
                MarketingPresaleBuyerLimitDO buyerLimitDO = new MarketingPresaleBuyerLimitDO();
                buyerLimitDO.setMarketingPresaleId(request.getMarketingPresaleId());
                buyerLimitDO.setEid(enterpriseDTO.getId());
                buyerLimitDO.setEname(enterpriseDTO.getName());
                buyerLimitDO.setOpUserId(request.getOpUserId());
                buyerLimitDO.setOpTime(request.getOpTime());
                buyerLimitDOList.add(buyerLimitDO);
            }
            presaleBuyerLimitService.saveBatch(buyerLimitDOList);
            return true;
        } else {
            QueryEnterprisePageListRequest pageListRequest = new QueryEnterprisePageListRequest();
            pageListRequest.setStatus(1);
            pageListRequest.setAuthStatus(2);
            List<Integer> notInTypeList = new ArrayList<>();
            notInTypeList.add(1);
            notInTypeList.add(2);
            pageListRequest.setNotInTypeList(notInTypeList);
            if (Objects.nonNull(request.getEidPage())) {
                pageListRequest.setId(request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                pageListRequest.setName(request.getEnamePage());
            }
            Page<EnterpriseDTO> page;
            int current = 1;
            do {
                pageListRequest.setCurrent(current);
                pageListRequest.setSize(500);
                page = enterpriseApi.pageList(pageListRequest);
                if (CollUtil.isEmpty(page.getRecords())) {
                    continue;
                }
                if (page.getTotal() > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_BUYER_TO_MANY);
                }
                List<Long> enterpriseIdList = page.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                List<MarketingPresaleBuyerLimitDO> strategyBuyerLimitDOList = this.listByActivityIdAndEidListForPresale(request.getMarketingPresaleId(), enterpriseIdList);
                List<Long> haveEidList = strategyBuyerLimitDOList.stream().map(MarketingPresaleBuyerLimitDO::getEid).collect(Collectors.toList());
                List<MarketingPresaleBuyerLimitDO> buyerLimitDOList = new ArrayList<>();
                for (EnterpriseDTO enterpriseDTO : page.getRecords()) {
                    if (haveEidList.contains(enterpriseDTO.getId())) {
                        continue;
                    }
                    MarketingPresaleBuyerLimitDO buyerLimitDO = new MarketingPresaleBuyerLimitDO();
                    buyerLimitDO.setMarketingPresaleId(request.getMarketingPresaleId());
                    buyerLimitDO.setEid(enterpriseDTO.getId());
                    buyerLimitDO.setEname(enterpriseDTO.getName());
                    buyerLimitDO.setOpUserId(request.getOpUserId());
                    buyerLimitDO.setOpTime(request.getOpTime());
                    buyerLimitDOList.add(buyerLimitDO);
                }
                if (CollUtil.isNotEmpty(buyerLimitDOList)) {
                    presaleBuyerLimitService.saveBatch(buyerLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(page.getRecords()));
            return true;
        }
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        Page<StrategyBuyerLimitDO> buyerLimitDOPage;
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, oldId);
        int current = 1;
        do {
            Page<StrategyBuyerLimitDO> objectPage = new Page<>(current, 100);
            buyerLimitDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(buyerLimitDOPage.getRecords())) {
                continue;
            }
            List<StrategyBuyerLimitDO> buyerLimitDOList = buyerLimitDOPage.getRecords();
            for (StrategyBuyerLimitDO buyerLimitDO : buyerLimitDOList) {
                buyerLimitDO.setId(null);
                buyerLimitDO.setMarketingStrategyId(strategyActivityDO.getId());
                buyerLimitDO.setOpUserId(opUserId);
                buyerLimitDO.setOpTime(opTime);
            }
            if (CollUtil.isNotEmpty(buyerLimitDOList)) {
                this.saveBatch(buyerLimitDOList);
            }
            current = current + 1;
        } while (CollUtil.isNotEmpty(buyerLimitDOPage.getRecords()));
        log.info("指定客户 复制完成!");
    }

    @Override
    public boolean delete(DeleteStrategyBuyerLimitRequest request) {
        StrategyBuyerLimitDO buyerLimitDO = new StrategyBuyerLimitDO();
        buyerLimitDO.setDelFlag(1);
        buyerLimitDO.setOpUserId(request.getOpUserId());
        buyerLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyBuyerLimitDO::getEid, request.getEid());
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(StrategyBuyerLimitDO::getEid, request.getEidList());
        } else {
            if (Objects.nonNull(request.getEidPage())) {
                wrapper.lambda().eq(StrategyBuyerLimitDO::getEid, request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                wrapper.lambda().like(StrategyBuyerLimitDO::getEname, request.getEnamePage());
            }
        }
        this.batchDeleteWithFill(buyerLimitDO, wrapper);
        return true;
    }

    @Override
    public boolean deleteForPayPromotion(DeleteStrategyBuyerLimitRequest request) {
        MarketingPayBuyerLimitDO buyerLimitDO = new MarketingPayBuyerLimitDO();
        buyerLimitDO.setDelFlag(1);
        buyerLimitDO.setOpUserId(request.getOpUserId());
        buyerLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayBuyerLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPayBuyerLimitDO::getEid, request.getEid());
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPayBuyerLimitDO::getEid, request.getEidList());
        } else {
            if (Objects.nonNull(request.getEidPage())) {
                wrapper.lambda().eq(MarketingPayBuyerLimitDO::getEid, request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                wrapper.lambda().like(MarketingPayBuyerLimitDO::getEname, request.getEnamePage());
            }
        }
        payBuyerLimitService.batchDeleteWithFill(buyerLimitDO, wrapper);
        return true;
    }

    @Override
    public Page<StrategyBuyerLimitDO> pageList(QueryStrategyBuyerLimitPageRequest request) {
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyBuyerLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(StrategyBuyerLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(StrategyBuyerLimitDO::getEname, request.getEname());
        }
        Page<StrategyBuyerLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public Page<MarketingPayBuyerLimitDO> pageListForPromotion(QueryStrategyBuyerLimitPageRequest request) {
        QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayBuyerLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPayBuyerLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPayBuyerLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(MarketingPayBuyerLimitDO::getEname, request.getEname());
        }
        return payBuyerLimitService.page(request.getPage(), wrapper);
    }

    @Override
    public Page<MarketingPresaleBuyerLimitDO> pageListForPresale(QueryPresaleBuyerLimitPageRequest request) {
        QueryWrapper<MarketingPresaleBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getMarketingPresaleId, request.getMarketingPresaleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPresaleBuyerLimitDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(MarketingPresaleBuyerLimitDO::getEname, request.getEname());
        }
        Page<MarketingPresaleBuyerLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return presaleBuyerLimitService.page(objectPage, wrapper);
    }

    @Override
    public Integer countBuyerByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategyBuyerLimitDO> listBuyerByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public StrategyBuyerLimitDO queryByActivityIdAndEid(Long strategyActivityId, Long eid) {
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, strategyActivityId);
        wrapper.lambda().eq(StrategyBuyerLimitDO::getEid, eid);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<StrategyBuyerLimitDO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList) {
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyBuyerLimitDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.lambda().in(StrategyBuyerLimitDO::getEid, eidList);
        }
        return this.list(wrapper);
    }

    @Override
    public List<MarketingPresaleBuyerLimitDO> listByActivityIdAndEidListForPresale(Long strategyActivityId, List<Long> eidList) {
        QueryWrapper<MarketingPresaleBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getMarketingPresaleId, strategyActivityId);
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.lambda().in(MarketingPresaleBuyerLimitDO::getEid, eidList);
        }
        return presaleBuyerLimitService.list(wrapper);
    }

    @Override
    public List<StrategyBuyerLimitDO> listByActivityIdListAndEid(List<Long> marketingStrategyIdList, Long buyerEid) {
        QueryWrapper<StrategyBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StrategyBuyerLimitDO::getMarketingStrategyId, marketingStrategyIdList);
        wrapper.lambda().eq(StrategyBuyerLimitDO::getEid, buyerEid);
        return this.list(wrapper);
    }

    @Override
    public boolean deleteForPresale(DeletePresaleBuyerLimitRequest request){
        MarketingPresaleBuyerLimitDO buyerLimitDO = new MarketingPresaleBuyerLimitDO();
        buyerLimitDO.setDelFlag(1);
        buyerLimitDO.setOpUserId(request.getOpUserId());
        buyerLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPresaleBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getMarketingPresaleId, request.getMarketingPresaleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getEid, request.getEid());
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(MarketingPresaleBuyerLimitDO::getEid, request.getEidList());
        } else {
            if (Objects.nonNull(request.getEidPage())) {
                wrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getEid, request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                wrapper.lambda().like(MarketingPresaleBuyerLimitDO::getEname, request.getEnamePage());
            }
        }
        presaleBuyerLimitService.batchDeleteWithFill(buyerLimitDO, wrapper);
        return true;
    };

    @Override
    public Integer countBuyerByActivityIdForPayPromotion(Long strategyActivityId) {
        QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayBuyerLimitDO::getMarketingPayId, strategyActivityId);
        return payBuyerLimitService.count(wrapper);
    }

    @Override
    public Integer countMemberByActivityId(Long strategyActivityId) {
        QueryWrapper<MarketingPayMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMarketingPayId, strategyActivityId);
        return payMemberLimitService.count(wrapper);
    }

    @Override
    public Integer countPromoterMemberByActivityId(Long strategyActivityId) {
        QueryWrapper<MarketingPayPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getMarketingPayId, strategyActivityId);
        return payPromoterMemberLimitService.count(wrapper);
    }
}
