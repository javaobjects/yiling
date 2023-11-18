package com.yiling.marketing.integral.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.marketing.integral.dao.IntegralOrderEnterpriseGoodsMapper;
import com.yiling.marketing.integral.dto.request.AddIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.DeleteIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.QueryIntegralEnterpriseGoodsPageRequest;
import com.yiling.marketing.integral.entity.IntegralGiveRuleDO;
import com.yiling.marketing.integral.entity.IntegralOrderEnterpriseGoodsDO;
import com.yiling.marketing.integral.service.IntegralOrderEnterpriseGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分店铺SKU表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderEnterpriseGoodsServiceImpl extends BaseServiceImpl<IntegralOrderEnterpriseGoodsMapper, IntegralOrderEnterpriseGoodsDO> implements IntegralOrderEnterpriseGoodsService {

    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsApi goodsApi;

    @Override
    public List<Long> listGoodsIdByRuleId(Long giveRuleId, List<Long> goodsIdList) {
        return this.getBaseMapper().listGoodsIdByRuleId(giveRuleId, goodsIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(AddIntegralGiveEnterpriseGoodsRequest request) {
        if (Objects.nonNull(request.getGoodsId())) {
            IntegralOrderEnterpriseGoodsDO orderEnterpriseGoodsDO = this.queryByRuleIdAndGoodsId(request.getGiveRuleId(), request.getGoodsId());
            if (Objects.nonNull(orderEnterpriseGoodsDO)) {
                return true;
            }
            GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(request.getGoodsId());
            IntegralOrderEnterpriseGoodsDO enterpriseGoodsDO = PojoUtils.map(request, IntegralOrderEnterpriseGoodsDO.class);
            enterpriseGoodsDO.setGoodsName(goodsInfoDTO.getName());
            this.save(enterpriseGoodsDO);

        } else if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
            List<IntegralOrderEnterpriseGoodsDO> orderEnterpriseGoodsDOList = this.listByRuleIdAndGoodsIdList(request.getGiveRuleId(), request.getGoodsIdList());
            List<Long> haveGoodsIdList = orderEnterpriseGoodsDOList.stream().map(IntegralOrderEnterpriseGoodsDO::getGoodsId).collect(Collectors.toList());
            List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(request.getGoodsIdList());
            List<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsLimitDOList = ListUtil.toList();
            List<Long> goodsIdList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getId).collect(Collectors.toList());
            //查询是否以岭品
            List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
            Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, subEids);
            for (GoodsInfoDTO goodsInfoDTO : goodsInfoDTOList) {
                if (haveGoodsIdList.contains(goodsInfoDTO.getId())) {
                    continue;
                }
                IntegralOrderEnterpriseGoodsDO enterpriseGoodsDO = new IntegralOrderEnterpriseGoodsDO();
                enterpriseGoodsDO.setGiveRuleId(request.getGiveRuleId());
                enterpriseGoodsDO.setGoodsId(goodsInfoDTO.getId());
                enterpriseGoodsDO.setGoodsName(goodsInfoDTO.getName());
                enterpriseGoodsDO.setEid(goodsInfoDTO.getEid());
                enterpriseGoodsDO.setEname(goodsInfoDTO.getEname());
                if (goodsMap.get(goodsInfoDTO.getId()) != null && goodsMap.get(goodsInfoDTO.getId()) > 0) {
                    enterpriseGoodsDO.setYilingGoodsFlag(1);
                } else {
                    enterpriseGoodsDO.setYilingGoodsFlag(2);
                }
                enterpriseGoodsDO.setOpUserId(request.getOpUserId());
                enterpriseGoodsLimitDOList.add(enterpriseGoodsDO);
            }

            if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                this.saveBatch(enterpriseGoodsLimitDOList);
            }

        } else {
            // 添加搜索结果
            QueryGoodsPageListRequest pageListRequest = new QueryGoodsPageListRequest();
            if (ObjectUtil.isNotNull(request.getGoodsId()) && request.getGoodsId() != 0) {
                pageListRequest.setGoodsId(request.getGoodsId());
            }
            if (StrUtil.isNotEmpty(request.getGoodsNamePage())) {
                pageListRequest.setName(request.getGoodsNamePage());
            }
            pageListRequest.setYilingGoodsFlag(request.getYilingGoodsFlag());
            pageListRequest.setGoodsStatus(request.getGoodsStatus());
            // 去掉库存必须大于0限制
            pageListRequest.setIsAvailableQty(0);
            if (pageListRequest.getYilingGoodsFlag() != 0) {
                pageListRequest.setIncludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
            }
            List<Long> sellerEidList = request.getSellerEidList();
            if (StrUtil.isNotEmpty(request.getEnamePage())) {
                QueryEnterpriseByNameRequest byNameRequest = new QueryEnterpriseByNameRequest();
                byNameRequest.setName(request.getEnamePage());
                byNameRequest.setTypeList(ListUtil.toList(EnterpriseTypeEnum.BUSINESS.getCode()));
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(byNameRequest);
                List<Long> eidList = enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
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
                    throw new BusinessException(UserErrorCode.ENTERPRISE_GOODS_TO_MANY);
                }
                List<Long> goodsIdList = b2bGoodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
                List<IntegralOrderEnterpriseGoodsDO> integralEnterpriseGoodsDOList = this.listByRuleIdAndGoodsIdList(request.getGiveRuleId(), goodsIdList);
                List<Long> haveGoodsIdList = integralEnterpriseGoodsDOList.stream().map(IntegralOrderEnterpriseGoodsDO::getGoodsId).collect(Collectors.toList());
                List<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsDOList = ListUtil.toList();

                //查询是否以岭品
                List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
                Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, subEids);
                for (GoodsListItemBO goodsListItemBO : b2bGoodsPage.getRecords()) {
                    if (haveGoodsIdList.contains(goodsListItemBO.getId())) {
                        continue;
                    }
                    IntegralOrderEnterpriseGoodsDO enterpriseGoodsDO = new IntegralOrderEnterpriseGoodsDO();
                    enterpriseGoodsDO.setGiveRuleId(request.getGiveRuleId());
                    enterpriseGoodsDO.setGoodsId(goodsListItemBO.getId());
                    enterpriseGoodsDO.setGoodsName(goodsListItemBO.getName());
                    enterpriseGoodsDO.setEid(goodsListItemBO.getEid());
                    enterpriseGoodsDO.setEname(goodsListItemBO.getEname());
                    if (goodsMap.get(goodsListItemBO.getId()) != null && goodsMap.get(goodsListItemBO.getId()) > 0) {
                        enterpriseGoodsDO.setYilingGoodsFlag(1);
                    } else {
                        enterpriseGoodsDO.setYilingGoodsFlag(2);
                    }
                    enterpriseGoodsDO.setOpUserId(request.getOpUserId());
                    enterpriseGoodsDOList.add(enterpriseGoodsDO);
                }
                if (CollUtil.isNotEmpty(enterpriseGoodsDOList)) {
                    this.saveBatch(enterpriseGoodsDOList);
                }
                current = current + 1;

            } while (CollUtil.isNotEmpty(b2bGoodsPage.getRecords()));

        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteIntegralGiveEnterpriseGoodsRequest request) {
        IntegralOrderEnterpriseGoodsDO enterpriseGoodsDO = new IntegralOrderEnterpriseGoodsDO();
        enterpriseGoodsDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, request.getGiveRuleId());

        if (Objects.nonNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGoodsId, request.getGoodsId());
            this.batchDeleteWithFill(enterpriseGoodsDO, wrapper);

        } else if (CollUtil.isNotEmpty(request.getGoodsIdList())) {
            wrapper.in(IntegralOrderEnterpriseGoodsDO::getGoodsId, request.getGoodsIdList());
            this.batchDeleteWithFill(enterpriseGoodsDO, wrapper);

        } else {
            if (Objects.nonNull(request.getGoodsIdPage()) && request.getGoodsIdPage() != 0) {
                wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGoodsId, request.getGoodsIdPage());
            }
            if (StrUtil.isNotEmpty(request.getGoodsNamePage())) {
                wrapper.like(IntegralOrderEnterpriseGoodsDO::getGoodsName, request.getGoodsNamePage());
            }
            if (Objects.nonNull(request.getEidPage()) && request.getEidPage() != 0) {
                wrapper.eq(IntegralOrderEnterpriseGoodsDO::getEid, request.getEidPage());
            }
            if (StrUtil.isNotEmpty(request.getEnamePage())) {
                wrapper.like(IntegralOrderEnterpriseGoodsDO::getEname, request.getEnamePage());
            }
            if (Objects.nonNull(request.getYilingGoodsFlag()) && request.getYilingGoodsFlag() != 0) {
                wrapper.eq(IntegralOrderEnterpriseGoodsDO::getYilingGoodsFlag, request.getYilingGoodsFlag());
            }
            this.batchDeleteWithFill(enterpriseGoodsDO, wrapper);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId) {
        Page<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsDOPage;
        QueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, oldId);
        int current = 1;

        do {
            Page<IntegralOrderEnterpriseGoodsDO> objectPage = new Page<>(current, 100);
            enterpriseGoodsDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(enterpriseGoodsDOPage.getRecords())) {
                break;
            }
            List<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsLimitDOList = enterpriseGoodsDOPage.getRecords();
            for (IntegralOrderEnterpriseGoodsDO enterpriseGoodsLimitDO : enterpriseGoodsLimitDOList) {
                enterpriseGoodsLimitDO.setId(null);
                enterpriseGoodsLimitDO.setGiveRuleId(giveRuleDO.getId());
                enterpriseGoodsLimitDO.setOpUserId(opUserId);
            }
            if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                this.saveBatch(enterpriseGoodsLimitDOList);
            }
            current = current + 1;
            
        } while (CollUtil.isNotEmpty(enterpriseGoodsDOPage.getRecords()));
    }

    @Override
    public Page<IntegralOrderEnterpriseGoodsDO> pageList(QueryIntegralEnterpriseGoodsPageRequest request) {
        QueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, request.getGiveRuleId());
        if (Objects.nonNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            wrapper.lambda().eq(IntegralOrderEnterpriseGoodsDO::getGoodsId, request.getGoodsId());
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            wrapper.lambda().like(IntegralOrderEnterpriseGoodsDO::getGoodsName, request.getGoodsName());
        }
        if (Objects.nonNull(request.getEid()) && request.getEid() != 0) {
            wrapper.lambda().eq(IntegralOrderEnterpriseGoodsDO::getEid, request.getEid());
        }
        if (StrUtil.isNotEmpty(request.getEname())) {
            wrapper.lambda().like(IntegralOrderEnterpriseGoodsDO::getEname, request.getEname());
        }
        if (Objects.nonNull(request.getYilingGoodsFlag()) && 0 != request.getYilingGoodsFlag()) {
            wrapper.lambda().eq(IntegralOrderEnterpriseGoodsDO::getYilingGoodsFlag, request.getYilingGoodsFlag());
        }
        return this.page(request.getPage(), wrapper);
    }

    @Override
    public Integer countEnterpriseGoodsByRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        return this.count(wrapper);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDO> listEnterpriseGoodsByRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper);
    }

    @Override
    public IntegralOrderEnterpriseGoodsDO queryByRuleIdAndGoodsId(Long giveRuleId, Long goodsId) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGoodsId, goodsId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDO> listByRuleIdAndGoodsIdList(Long giveRuleId, List<Long> goodsIdList) {
        LambdaQueryWrapper<IntegralOrderEnterpriseGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderEnterpriseGoodsDO::getGiveRuleId, giveRuleId);
        if (CollUtil.isNotEmpty(goodsIdList)) {
            wrapper.in(IntegralOrderEnterpriseGoodsDO::getGoodsId, goodsIdList);
        }
        return this.list(wrapper);
    }

}
