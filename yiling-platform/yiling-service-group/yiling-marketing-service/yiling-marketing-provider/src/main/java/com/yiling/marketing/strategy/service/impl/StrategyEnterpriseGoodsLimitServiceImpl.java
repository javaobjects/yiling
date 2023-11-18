package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.marketing.payPromotion.entity.MarketingPayEnterpriseGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionActivityDO;
import com.yiling.marketing.payPromotion.service.MarketingPayEnterpriseGoodsLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPlatformGoodsLimitService;
import com.yiling.marketing.paypromotion.dto.PayPromotionEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dao.StrategyEnterpriseGoodsLimitMapper;
import com.yiling.marketing.strategy.dto.request.AddStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyEnterpriseGoodsLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyEnterpriseGoodsLimitDO;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.marketing.strategy.service.StrategyEnterpriseGoodsLimitService;
import com.yiling.marketing.strategy.service.StrategyPlatformGoodsLimitService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 策略满赠店铺SKU 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Slf4j
@Service
public class StrategyEnterpriseGoodsLimitServiceImpl extends BaseServiceImpl<StrategyEnterpriseGoodsLimitMapper, StrategyEnterpriseGoodsLimitDO> implements StrategyEnterpriseGoodsLimitService {

    @DubboReference
    B2bGoodsApi b2bGoodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    MarketingPayEnterpriseGoodsLimitService payEnterpriseGoodsLimitService;

    @Override
    public List<Long> listGoodsIdByStrategyId(Long strategyActivityId, List<Long> goodsIdList) {
        return this.getBaseMapper().listGoodsIdByStrategyId(strategyActivityId, goodsIdList);
    }

    @Override
    public boolean add(AddStrategyEnterpriseGoodsLimitRequest request) {
        if (Objects.nonNull(request.getGoodsId())) {
            StrategyEnterpriseGoodsLimitDO strategyEnterpriseGoodsLimitDO = this.queryByActivityIdAndGoodsId(request.getMarketingStrategyId(), request.getGoodsId());
            if (Objects.nonNull(strategyEnterpriseGoodsLimitDO)) {
                return false;
            }
            GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(request.getGoodsId());
            StrategyEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = PojoUtils.map(request, StrategyEnterpriseGoodsLimitDO.class);
            enterpriseGoodsLimitDO.setGoodsName(goodsInfoDTO.getName());
            return this.save(enterpriseGoodsLimitDO);
        } else if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
            List<StrategyEnterpriseGoodsLimitDO> strategyEnterpriseGoodsLimitDOList = this.listByActivityIdAndGoodsIdList(request.getMarketingStrategyId(), request.getGoodsIdList());
            List<Long> haveGoodsIdList = strategyEnterpriseGoodsLimitDOList.stream().map(StrategyEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
            List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(request.getGoodsIdList());
            List<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = new ArrayList<>();
            List<Long> goodsIdList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getId).collect(Collectors.toList());
            //查询是否以岭品
            List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
            Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, subEids);
            for (GoodsInfoDTO goodsInfoDTO : goodsInfoDTOList) {
                if (haveGoodsIdList.contains(goodsInfoDTO.getId())) {
                    continue;
                }
                StrategyEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = new StrategyEnterpriseGoodsLimitDO();
                enterpriseGoodsLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                enterpriseGoodsLimitDO.setGoodsId(goodsInfoDTO.getId());
                enterpriseGoodsLimitDO.setGoodsName(goodsInfoDTO.getName());
                enterpriseGoodsLimitDO.setEid(goodsInfoDTO.getEid());
                enterpriseGoodsLimitDO.setEname(goodsInfoDTO.getEname());
                enterpriseGoodsLimitDO.setYilingGoodsFlag(2);
                if (goodsMap.get(goodsInfoDTO.getId()) != null && goodsMap.get(goodsInfoDTO.getId()) > 0) {
                    enterpriseGoodsLimitDO.setYilingGoodsFlag(1);
                }
                enterpriseGoodsLimitDO.setOpUserId(request.getOpUserId());
                enterpriseGoodsLimitDO.setOpTime(request.getOpTime());
                enterpriseGoodsLimitDOList.add(enterpriseGoodsLimitDO);
            }
            if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                return this.saveBatch(enterpriseGoodsLimitDOList);
            }
        } else {
            // 添加搜索结果
            QueryGoodsPageListRequest pageListRequest = new QueryGoodsPageListRequest();
            if (ObjectUtil.isNotNull(request.getGoodsId())) {
                pageListRequest.setGoodsId(request.getGoodsId());
            }
            if (StrUtil.isNotBlank(request.getGoodsNamePage())) {
                pageListRequest.setName(request.getGoodsNamePage());
            }
            pageListRequest.setYilingGoodsFlag(request.getYilingGoodsFlag());
            pageListRequest.setGoodsStatus(request.getGoodsStatus());
            // 去掉库存必须大于0限制
            pageListRequest.setIsAvailableQty(0);
            if (!Integer.valueOf(0).equals(pageListRequest.getYilingGoodsFlag())) {
                pageListRequest.setIncludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
            }
            List<Long> sellerEidList = request.getSellerEidList();
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                QueryEnterpriseByNameRequest byNameRequest = new QueryEnterpriseByNameRequest();
                byNameRequest.setName(request.getEnamePage());
                List<Integer> inTypeList = new ArrayList<>();
                inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
                byNameRequest.setTypeList(inTypeList);
                List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(byNameRequest);
                List<Long> eidList = enterpriseListByName.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                //  求交集
                if (CollUtil.isEmpty(sellerEidList)) {
                    sellerEidList = eidList;
                } else {
                    Collection<Long> intersection = CollUtil.intersection(eidList, sellerEidList);
                    if (CollUtil.isEmpty(intersection)) {
                        return false;
                    }
                    sellerEidList = new ArrayList<>(intersection);
                }
            }
            if (CollUtil.isNotEmpty(sellerEidList)) {
                pageListRequest.setEidList(sellerEidList);
            }
            Page<GoodsListItemBO> b2bGoodsPage;
            int current = 1;
            do {
                pageListRequest.setCurrent(current);
                pageListRequest.setSize(500);
                b2bGoodsPage = b2bGoodsApi.queryB2bGoodsPageList(pageListRequest);
                if (CollUtil.isEmpty(b2bGoodsPage.getRecords())) {
                    continue;
                }
                long total = b2bGoodsPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_ENTERPRISE_GOODS_TO_MANY);
                }
                List<Long> goodsIdList = b2bGoodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
                List<StrategyEnterpriseGoodsLimitDO> strategyEnterpriseGoodsLimitDOList = this.listByActivityIdAndGoodsIdList(request.getMarketingStrategyId(), goodsIdList);
                List<Long> haveGoodsIdList = strategyEnterpriseGoodsLimitDOList.stream().map(StrategyEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                List<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = new ArrayList<>();

                //查询是否以岭品
                List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
                Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, subEids);
                for (GoodsListItemBO goodsListItemBO : b2bGoodsPage.getRecords()) {
                    if (haveGoodsIdList.contains(goodsListItemBO.getId())) {
                        continue;
                    }
                    StrategyEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = new StrategyEnterpriseGoodsLimitDO();
                    enterpriseGoodsLimitDO.setMarketingStrategyId(request.getMarketingStrategyId());
                    enterpriseGoodsLimitDO.setGoodsId(goodsListItemBO.getId());
                    enterpriseGoodsLimitDO.setGoodsName(goodsListItemBO.getName());
                    enterpriseGoodsLimitDO.setEid(goodsListItemBO.getEid());
                    enterpriseGoodsLimitDO.setEname(goodsListItemBO.getEname());
                    enterpriseGoodsLimitDO.setYilingGoodsFlag(2);
                    if (goodsMap.get(goodsListItemBO.getId()) != null && goodsMap.get(goodsListItemBO.getId()) > 0) {
                        enterpriseGoodsLimitDO.setYilingGoodsFlag(1);
                    }
                    enterpriseGoodsLimitDO.setOpUserId(request.getOpUserId());
                    enterpriseGoodsLimitDO.setOpTime(request.getOpTime());
                    enterpriseGoodsLimitDOList.add(enterpriseGoodsLimitDO);
                }
                if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                    this.saveBatch(enterpriseGoodsLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(b2bGoodsPage.getRecords()));
            return true;
        }
        return false;
    }

    @Override
    public boolean addForPayPromotion(AddStrategyEnterpriseGoodsLimitRequest request) {
        if (Objects.nonNull(request.getGoodsId())) {
            QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
            wrapper.lambda().last(" limit 1");
            MarketingPayEnterpriseGoodsLimitDO strategyEnterpriseGoodsLimitDO =payEnterpriseGoodsLimitService.getOne(wrapper);
            if (Objects.nonNull(strategyEnterpriseGoodsLimitDO)) {
                return false;
            }
            GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(request.getGoodsId());
            MarketingPayEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = PojoUtils.map(request, MarketingPayEnterpriseGoodsLimitDO.class);
            enterpriseGoodsLimitDO.setMarketingPayId(request.getMarketingStrategyId());
            enterpriseGoodsLimitDO.setGoodsName(goodsInfoDTO.getName());
            return payEnterpriseGoodsLimitService.save(enterpriseGoodsLimitDO);
        } else if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
            QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
            List<MarketingPayEnterpriseGoodsLimitDO> strategyEnterpriseGoodsLimitDOList=payEnterpriseGoodsLimitService.list(wrapper);
            List<Long> haveGoodsIdList = strategyEnterpriseGoodsLimitDOList.stream().map(MarketingPayEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
            List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(request.getGoodsIdList());
            List<MarketingPayEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = new ArrayList<>();
            List<Long> goodsIdList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getId).collect(Collectors.toList());
            //查询是否以岭品
            List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
            Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, subEids);
            for (GoodsInfoDTO goodsInfoDTO : goodsInfoDTOList) {
                if (haveGoodsIdList.contains(goodsInfoDTO.getId())) {
                    continue;
                }
                MarketingPayEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = new MarketingPayEnterpriseGoodsLimitDO();
                enterpriseGoodsLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                enterpriseGoodsLimitDO.setGoodsId(goodsInfoDTO.getId());
                enterpriseGoodsLimitDO.setGoodsName(goodsInfoDTO.getName());
                enterpriseGoodsLimitDO.setEid(goodsInfoDTO.getEid());
                enterpriseGoodsLimitDO.setEname(goodsInfoDTO.getEname());
                enterpriseGoodsLimitDO.setYilingGoodsFlag(2);
                if (goodsMap.get(goodsInfoDTO.getId()) != null && goodsMap.get(goodsInfoDTO.getId()) > 0) {
                    enterpriseGoodsLimitDO.setYilingGoodsFlag(1);
                }
                enterpriseGoodsLimitDO.setOpUserId(request.getOpUserId());
                enterpriseGoodsLimitDO.setOpTime(request.getOpTime());
                enterpriseGoodsLimitDOList.add(enterpriseGoodsLimitDO);
            }
            if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                return payEnterpriseGoodsLimitService.saveBatch(enterpriseGoodsLimitDOList);
            }
        } else {
            // 添加搜索结果
            QueryGoodsPageListRequest pageListRequest = new QueryGoodsPageListRequest();
            if (ObjectUtil.isNotNull(request.getGoodsId())) {
                pageListRequest.setGoodsId(request.getGoodsId());
            }
            if (StrUtil.isNotBlank(request.getGoodsNamePage())) {
                pageListRequest.setName(request.getGoodsNamePage());
            }
            pageListRequest.setYilingGoodsFlag(request.getYilingGoodsFlag());
            pageListRequest.setGoodsStatus(request.getGoodsStatus());
            // 去掉库存必须大于0限制
            pageListRequest.setIsAvailableQty(0);
            if (!Integer.valueOf(0).equals(pageListRequest.getYilingGoodsFlag())) {
                pageListRequest.setIncludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
            }
            List<Long> sellerEidList = request.getSellerEidList();
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                QueryEnterpriseByNameRequest byNameRequest = new QueryEnterpriseByNameRequest();
                byNameRequest.setName(request.getEnamePage());
                List<Integer> inTypeList = new ArrayList<>();
                inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
                byNameRequest.setTypeList(inTypeList);
                List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(byNameRequest);
                List<Long> eidList = enterpriseListByName.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                //  求交集
                if (CollUtil.isEmpty(sellerEidList)) {
                    sellerEidList = eidList;
                } else {
                    Collection<Long> intersection = CollUtil.intersection(eidList, sellerEidList);
                    if (CollUtil.isEmpty(intersection)) {
                        return false;
                    }
                    sellerEidList = new ArrayList<>(intersection);
                }
            }
            if (CollUtil.isNotEmpty(sellerEidList)) {
                pageListRequest.setEidList(sellerEidList);
            }
            Page<GoodsListItemBO> b2bGoodsPage;
            int current = 1;
            do {
                pageListRequest.setCurrent(current);
                pageListRequest.setSize(500);
                b2bGoodsPage = b2bGoodsApi.queryB2bGoodsPageList(pageListRequest);
                if (CollUtil.isEmpty(b2bGoodsPage.getRecords())) {
                    continue;
                }
                long total = b2bGoodsPage.getTotal();
                if (total > 500L) {
                    throw new BusinessException(StrategyErrorCode.STRATEGY_ENTERPRISE_GOODS_TO_MANY);
                }
                List<Long> goodsIdList = b2bGoodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
                QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
                wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
                List<MarketingPayEnterpriseGoodsLimitDO> strategyEnterpriseGoodsLimitDOList=payEnterpriseGoodsLimitService.list(wrapper);
                List<Long> haveGoodsIdList = strategyEnterpriseGoodsLimitDOList.stream().map(MarketingPayEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                List<MarketingPayEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = new ArrayList<>();

                //查询是否以岭品
                List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
                Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, subEids);
                for (GoodsListItemBO goodsListItemBO : b2bGoodsPage.getRecords()) {
                    if (haveGoodsIdList.contains(goodsListItemBO.getId())) {
                        continue;
                    }
                    MarketingPayEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = new MarketingPayEnterpriseGoodsLimitDO();
                    enterpriseGoodsLimitDO.setMarketingPayId(request.getMarketingStrategyId());
                    enterpriseGoodsLimitDO.setGoodsId(goodsListItemBO.getId());
                    enterpriseGoodsLimitDO.setGoodsName(goodsListItemBO.getName());
                    enterpriseGoodsLimitDO.setEid(goodsListItemBO.getEid());
                    enterpriseGoodsLimitDO.setEname(goodsListItemBO.getEname());
                    enterpriseGoodsLimitDO.setYilingGoodsFlag(2);
                    if (goodsMap.get(goodsListItemBO.getId()) != null && goodsMap.get(goodsListItemBO.getId()) > 0) {
                        enterpriseGoodsLimitDO.setYilingGoodsFlag(1);
                    }
                    enterpriseGoodsLimitDO.setOpUserId(request.getOpUserId());
                    enterpriseGoodsLimitDO.setOpTime(request.getOpTime());
                    enterpriseGoodsLimitDOList.add(enterpriseGoodsLimitDO);
                }
                if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                    payEnterpriseGoodsLimitService.saveBatch(enterpriseGoodsLimitDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(b2bGoodsPage.getRecords()));
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteForPromotion(DeleteStrategyEnterpriseGoodsLimitRequest request) {
        MarketingPayEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = new MarketingPayEnterpriseGoodsLimitDO();
        enterpriseGoodsLimitDO.setDelFlag(1);
        enterpriseGoodsLimitDO.setOpUserId(request.getOpUserId());
        enterpriseGoodsLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getGoodsId())) {
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
            payEnterpriseGoodsLimitService.batchDeleteWithFill(enterpriseGoodsLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
            wrapper.lambda().in(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsIdList());
            payEnterpriseGoodsLimitService.batchDeleteWithFill(enterpriseGoodsLimitDO, wrapper);
        } else {
            if (Objects.nonNull(request.getGoodsIdPage())) {
                wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsIdPage());
            }
            if (StringUtils.isNotBlank(request.getGoodsNamePage())) {
                wrapper.lambda().like(MarketingPayEnterpriseGoodsLimitDO::getGoodsName, request.getGoodsNamePage());
            }
            if (Objects.nonNull(request.getEidPage())) {
                wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getEid, request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                wrapper.lambda().like(MarketingPayEnterpriseGoodsLimitDO::getEname, request.getEnamePage());
            }
            if (Objects.nonNull(request.getYilingGoodsFlag()) && 0 != request.getYilingGoodsFlag()) {
                wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getYilingGoodsFlag, request.getYilingGoodsFlag());
            }
            payEnterpriseGoodsLimitService.batchDeleteWithFill(enterpriseGoodsLimitDO, wrapper);
        }
        return true;
    }

    @Override
    public boolean delete(DeleteStrategyEnterpriseGoodsLimitRequest request) {
        StrategyEnterpriseGoodsLimitDO enterpriseGoodsLimitDO = new StrategyEnterpriseGoodsLimitDO();
        enterpriseGoodsLimitDO.setDelFlag(1);
        enterpriseGoodsLimitDO.setOpUserId(request.getOpUserId());
        enterpriseGoodsLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getGoodsId())) {
            wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
            this.batchDeleteWithFill(enterpriseGoodsLimitDO, wrapper);
        } else if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
            wrapper.lambda().in(StrategyEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsIdList());
            this.batchDeleteWithFill(enterpriseGoodsLimitDO, wrapper);
        } else {
            if (Objects.nonNull(request.getGoodsIdPage())) {
                wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsIdPage());
            }
            if (StringUtils.isNotBlank(request.getGoodsNamePage())) {
                wrapper.lambda().like(StrategyEnterpriseGoodsLimitDO::getGoodsName, request.getGoodsNamePage());
            }
            if (Objects.nonNull(request.getEidPage())) {
                wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getEid, request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                wrapper.lambda().like(StrategyEnterpriseGoodsLimitDO::getEname, request.getEnamePage());
            }
            if (Objects.nonNull(request.getYilingGoodsFlag()) && 0 != request.getYilingGoodsFlag()) {
                wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getYilingGoodsFlag, request.getYilingGoodsFlag());
            }
            this.batchDeleteWithFill(enterpriseGoodsLimitDO, wrapper);
        }
        return true;
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        Page<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOPage;
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, oldId);
        int current = 1;
        do {
            Page<StrategyEnterpriseGoodsLimitDO> objectPage = new Page<>(current, 100);
            enterpriseGoodsLimitDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(enterpriseGoodsLimitDOPage.getRecords())) {
                continue;
            }
            List<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = enterpriseGoodsLimitDOPage.getRecords();
            for (StrategyEnterpriseGoodsLimitDO enterpriseGoodsLimitDO : enterpriseGoodsLimitDOList) {
                enterpriseGoodsLimitDO.setId(null);
                enterpriseGoodsLimitDO.setMarketingStrategyId(strategyActivityDO.getId());
                enterpriseGoodsLimitDO.setOpUserId(opUserId);
                enterpriseGoodsLimitDO.setOpTime(opTime);
            }
            if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                this.saveBatch(enterpriseGoodsLimitDOList);
            }
            current = current + 1;
        } while (CollUtil.isNotEmpty(enterpriseGoodsLimitDOPage.getRecords()));
        log.info("指定店铺SKU 复制完成!");
    }

    @Override
    public Page<StrategyEnterpriseGoodsLimitDO> pageList(QueryStrategyEnterpriseGoodsLimitPageRequest request) {
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getGoodsId())) {
            wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
        }
        if (StringUtils.isNotBlank(request.getGoodsName())) {
            wrapper.lambda().like(StrategyEnterpriseGoodsLimitDO::getGoodsName, request.getGoodsName());
        }
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getEid, request.getEid());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(StrategyEnterpriseGoodsLimitDO::getEname, request.getEname());
        }
        if (Objects.nonNull(request.getYilingGoodsFlag()) && 0 != request.getYilingGoodsFlag()) {
            wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getYilingGoodsFlag, request.getYilingGoodsFlag());
        }
        Page<StrategyEnterpriseGoodsLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public Page<PayPromotionEnterpriseGoodsLimitDTO> pageListForPayPromotion(QueryStrategyEnterpriseGoodsLimitPageRequest request) {
        QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getGoodsId())) {
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, request.getGoodsId());
        }
        if (StringUtils.isNotBlank(request.getGoodsName())) {
            wrapper.lambda().like(MarketingPayEnterpriseGoodsLimitDO::getGoodsName, request.getGoodsName());
        }
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getEid, request.getEid());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(MarketingPayEnterpriseGoodsLimitDO::getEname, request.getEname());
        }
        if (Objects.nonNull(request.getYilingGoodsFlag()) && 0 != request.getYilingGoodsFlag()) {
            wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getYilingGoodsFlag, request.getYilingGoodsFlag());
        }
        Page<MarketingPayEnterpriseGoodsLimitDO> page = payEnterpriseGoodsLimitService.page(request.getPage(), wrapper);
        return PojoUtils.map(page,PayPromotionEnterpriseGoodsLimitDTO.class);
    }

    @Override
    public Integer countEnterpriseGoodsByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategyEnterpriseGoodsLimitDO> listEnterpriseGoodsByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public StrategyEnterpriseGoodsLimitDO queryByActivityIdAndGoodsId(Long strategyActivityId, Long goodsId) {
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getGoodsId, goodsId);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<StrategyEnterpriseGoodsLimitDO> listByActivityIdAndGoodsIdList(Long strategyActivityId, List<Long> goodsIdList) {
        QueryWrapper<StrategyEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyEnterpriseGoodsLimitDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(goodsIdList)) {
            wrapper.lambda().in(StrategyEnterpriseGoodsLimitDO::getGoodsId, goodsIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public Integer countEnterpriseGoodsByActivityIdForPayPromotion(Long strategyActivityId) {
        QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, strategyActivityId);
        return payEnterpriseGoodsLimitService.count(wrapper);
    }
}
