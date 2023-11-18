package com.yiling.user.integral.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.integral.dto.request.AddIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveEnterprisePageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseDO;
import com.yiling.user.integral.dao.IntegralOrderGiveEnterpriseMapper;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分-指定客户表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderGiveEnterpriseServiceImpl extends BaseServiceImpl<IntegralOrderGiveEnterpriseMapper, IntegralOrderGiveEnterpriseDO> implements IntegralOrderGiveEnterpriseService {

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public boolean add(AddIntegralGiveEnterpriseRequest request) {
        if (Objects.nonNull(request.getEid())) {
            IntegralOrderGiveEnterpriseDO strategyBuyerLimitDO = this.queryByRuleIdAndEid(request.getGiveRuleId(), request.getEid());
            if (Objects.nonNull(strategyBuyerLimitDO)) {
                return true;
            }

            IntegralOrderGiveEnterpriseDO buyerLimitDO = PojoUtils.map(request, IntegralOrderGiveEnterpriseDO.class);
            return this.save(buyerLimitDO);

        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<IntegralOrderGiveEnterpriseDO> giveEnterpriseDOList = this.listByRuleIdAndEidList(request.getGiveRuleId(), request.getEidList());
            List<Long> haveEidList = giveEnterpriseDOList.stream().map(IntegralOrderGiveEnterpriseDO::getEid).collect(Collectors.toList());

            List<IntegralOrderGiveEnterpriseDO> orderGiveEnterpriseDOList = new ArrayList<>();
            for (Long eid : request.getEidList()) {
                if (haveEidList.contains(eid)) {
                    continue;
                }
                IntegralOrderGiveEnterpriseDO buyerLimitDO = new IntegralOrderGiveEnterpriseDO();
                buyerLimitDO.setGiveRuleId(request.getGiveRuleId());
                buyerLimitDO.setEid(eid);
                buyerLimitDO.setOpUserId(request.getOpUserId());
                orderGiveEnterpriseDOList.add(buyerLimitDO);
            }
            if (CollUtil.isNotEmpty(orderGiveEnterpriseDOList)) {
                this.saveBatch(orderGiveEnterpriseDOList);
            }

        } else {
            QueryEnterprisePageListRequest pageListRequest = new QueryEnterprisePageListRequest();
            pageListRequest.setStatus(1);
            pageListRequest.setAuthStatus(2);
            List<Integer> notInTypeList = ListUtil.toList(EnterpriseTypeEnum.INDUSTRY.getCode(), EnterpriseTypeEnum.BUSINESS.getCode());
            pageListRequest.setNotInTypeList(notInTypeList);
            if (Objects.nonNull(request.getEidPage()) && request.getEidPage() != 0) {
                pageListRequest.setId(request.getEidPage());
            }
            if (StrUtil.isNotEmpty(request.getEnamePage())) {
                pageListRequest.setName(request.getEnamePage());
            }
            Page<EnterpriseDO> page;
            int current = 1;
            do {
                pageListRequest.setCurrent(current);
                pageListRequest.setSize(500);
                page = enterpriseService.pageList(pageListRequest);
                if (CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                if (page.getTotal() > 500L) {
                    throw new BusinessException(UserErrorCode.CUSTOMER_TO_MANY);
                }
                List<Long> enterpriseIdList = page.getRecords().stream().map(EnterpriseDO::getId).collect(Collectors.toList());
                List<IntegralOrderGiveEnterpriseDO> integralOrderGiveEnterpriseDOS = this.listByRuleIdAndEidList(request.getGiveRuleId(), enterpriseIdList);
                List<Long> haveEidList = integralOrderGiveEnterpriseDOS.stream().map(IntegralOrderGiveEnterpriseDO::getEid).collect(Collectors.toList());

                List<IntegralOrderGiveEnterpriseDO> orderGiveEnterpriseDOList = ListUtil.toList();
                for (EnterpriseDO enterpriseDTO : page.getRecords()) {
                    if (haveEidList.contains(enterpriseDTO.getId())) {
                        continue;
                    }
                    IntegralOrderGiveEnterpriseDO giveEnterpriseDO = new IntegralOrderGiveEnterpriseDO();
                    giveEnterpriseDO.setGiveRuleId(request.getGiveRuleId());
                    giveEnterpriseDO.setEid(enterpriseDTO.getId());
                    giveEnterpriseDO.setOpUserId(request.getOpUserId());
                    orderGiveEnterpriseDOList.add(giveEnterpriseDO);
                }
                if (CollUtil.isNotEmpty(orderGiveEnterpriseDOList)) {
                    this.saveBatch(orderGiveEnterpriseDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(page.getRecords()));

        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId) {
        Page<IntegralOrderGiveEnterpriseDO> buyerLimitDOPage;
        QueryWrapper<IntegralOrderGiveEnterpriseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getGiveRuleId, oldId);

        int current = 1;
        do {
            Page<IntegralOrderGiveEnterpriseDO> objectPage = new Page<>(current, 100);
            buyerLimitDOPage = this.page(objectPage, wrapper);
            if (CollUtil.isEmpty(buyerLimitDOPage.getRecords())) {
                continue;
            }
            List<IntegralOrderGiveEnterpriseDO> giveEnterpriseDOList = buyerLimitDOPage.getRecords();
            for (IntegralOrderGiveEnterpriseDO buyerLimitDO : giveEnterpriseDOList) {
                buyerLimitDO.setId(null);
                buyerLimitDO.setGiveRuleId(giveRuleDO.getId());
                buyerLimitDO.setOpUserId(opUserId);
            }
            if (CollUtil.isNotEmpty(giveEnterpriseDOList)) {
                this.saveBatch(giveEnterpriseDOList);
            }
            current = current + 1;

        } while (CollUtil.isNotEmpty(buyerLimitDOPage.getRecords()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteIntegralGiveEnterpriseRequest request) {
        IntegralOrderGiveEnterpriseDO giveEnterpriseDO = new IntegralOrderGiveEnterpriseDO();
        giveEnterpriseDO.setOpUserId(request.getOpUserId());

        QueryWrapper<IntegralOrderGiveEnterpriseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getGiveRuleId, request.getGiveRuleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getEid, request.getEid());
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(IntegralOrderGiveEnterpriseDO::getEid, request.getEidList());
        } else {
            if (Objects.nonNull(request.getEidPage())) {
                wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getEid, request.getEidPage());
            }
            if (StringUtils.isNotBlank(request.getEnamePage())) {
                QueryEnterpriseByNameRequest nameRequest = new QueryEnterpriseByNameRequest();
                nameRequest.setName(request.getEnamePage());
                List<Long> eidList = enterpriseService.getEnterpriseListByName(nameRequest).stream().map(BaseDTO::getId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(eidList)) {
                    wrapper.lambda().in(IntegralOrderGiveEnterpriseDO::getEid, eidList);
                }

            }
        }
        this.batchDeleteWithFill(giveEnterpriseDO, wrapper);
        return true;
    }

    @Override
    public Page<IntegralOrderGiveEnterpriseDO> pageList(QueryIntegralGiveEnterprisePageRequest request) {
        QueryWrapper<IntegralOrderGiveEnterpriseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getGiveRuleId, request.getGiveRuleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.lambda().in(IntegralOrderGiveEnterpriseDO::getEid, request.getEidList());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            QueryEnterpriseByNameRequest nameRequest = new QueryEnterpriseByNameRequest();
            nameRequest.setName(request.getEname());
            List<Long> eidList = enterpriseService.getEnterpriseListByName(nameRequest).stream().map(BaseDTO::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(eidList)) {
                wrapper.lambda().in(IntegralOrderGiveEnterpriseDO::getEid, eidList);
            }
        }
        return this.page(request.getPage(), wrapper);
    }

    @Override
    public Integer countGiveEnterpriseByRuleId(Long giveRuleId) {
        QueryWrapper<IntegralOrderGiveEnterpriseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderGiveEnterpriseDO::getGiveRuleId, giveRuleId);
        return this.count(wrapper);
    }

    @Override
    public IntegralOrderGiveEnterpriseDO queryByRuleIdAndEid(Long giveRuleId, Long eid) {
        LambdaQueryWrapper<IntegralOrderGiveEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveEnterpriseDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralOrderGiveEnterpriseDO::getEid, eid);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<IntegralOrderGiveEnterpriseDO> listByRuleIdAndEidList(Long giveRuleId, List<Long> eidList) {
        LambdaQueryWrapper<IntegralOrderGiveEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveEnterpriseDO::getGiveRuleId, giveRuleId);
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.in(IntegralOrderGiveEnterpriseDO::getEid, eidList);
        }
        return this.list(wrapper);
    }



}
