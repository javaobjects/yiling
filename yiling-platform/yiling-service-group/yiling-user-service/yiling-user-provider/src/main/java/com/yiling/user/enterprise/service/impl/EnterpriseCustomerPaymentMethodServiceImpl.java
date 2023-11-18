package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.dao.EnterpriseCustomerPaymentMethodMapper;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateOpenPayRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerPaymentMethodDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseChannelPaymentMethodService;
import com.yiling.user.enterprise.service.EnterpriseCustomerPaymentMethodService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.shop.service.ShopPaymentMethodService;
import com.yiling.user.system.entity.PaymentMethodDO;
import com.yiling.user.system.service.PaymentMethodService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Slf4j
@Service
public class EnterpriseCustomerPaymentMethodServiceImpl extends BaseServiceImpl<EnterpriseCustomerPaymentMethodMapper, EnterpriseCustomerPaymentMethodDO> implements EnterpriseCustomerPaymentMethodService {

    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    ShopPaymentMethodService shopPaymentMethodService;
    @Autowired
    EnterpriseChannelPaymentMethodService enterpriseChannelPaymentMethodService;
    @Autowired
    SpringAsyncConfig springAsyncConfig;
    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;

    @Override
    public Map<Long, List<PaymentMethodDO>> listByEidAndCustomerEids(Long eid, List<Long> customerEids, PlatformEnum platformEnum) {
        QueryWrapper<EnterpriseCustomerPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseCustomerPaymentMethodDO::getCustomerEid, customerEids);
        // 针对以岭分公司的特殊处理
        List<Long> yilingSubEids = enterpriseService.listSubEids(Constants.YILING_EID);
        if (yilingSubEids.contains(eid)) {
            queryWrapper.lambda().eq(EnterpriseCustomerPaymentMethodDO::getEid, Constants.YILING_EID);
        } else {
            queryWrapper.lambda().eq(EnterpriseCustomerPaymentMethodDO::getEid, eid);
        }

        List<EnterpriseCustomerPaymentMethodDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        // 获取所有可用支付方式
        List<PaymentMethodDO> paymentMethodDOList = ListUtil.empty();
        if (platformEnum == PlatformEnum.POP) {
            EnterpriseDO enterpriseDO = enterpriseService.getById(eid);
            paymentMethodDOList = enterpriseChannelPaymentMethodService.listByChannelId(enterpriseDO.getChannelId());
        } else if (platformEnum == PlatformEnum.B2B) {
            paymentMethodDOList = shopPaymentMethodService.listByEid(eid);
        }

        if (CollUtil.isEmpty(paymentMethodDOList)) {
            return MapUtil.empty();
        }

        Map<Long, List<PaymentMethodDO>> result = MapUtil.newHashMap();

        Map<Long, List<EnterpriseCustomerPaymentMethodDO>> map = list.stream().collect(Collectors.groupingBy(EnterpriseCustomerPaymentMethodDO::getCustomerEid));
        for (Long customerEid : map.keySet()) {
            List<Long> customerPayMethodIds = map.get(customerEid).stream().map(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId).distinct().collect(Collectors.toList());
            List<PaymentMethodDO> customerPaymentMethodList = paymentMethodDOList.stream().filter(e -> customerPayMethodIds.contains(e.getCode())).collect(Collectors.toList());
            result.put(customerEid, customerPaymentMethodList);
        }

        return result;
    }

    @Override
    public Map<Long, List<PaymentMethodDO>> listByCustomerEidAndEids(Long customerEid, List<Long> eids, PlatformEnum platformEnum) {
        // 针对以岭分公司的特殊处理
        List<Long> yilingSubEids = enterpriseService.listSubEids(Constants.YILING_EID);
        if (yilingSubEids.stream().filter(e -> eids.contains(e)).findAny().isPresent()) {
            eids.add(Constants.YILING_EID);
        }

        QueryWrapper<EnterpriseCustomerPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(EnterpriseCustomerPaymentMethodDO::getEid, eids)
                .eq(EnterpriseCustomerPaymentMethodDO::getCustomerEid, customerEid);

        List<EnterpriseCustomerPaymentMethodDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        // 获取企业可用支付方式
        Map<Long, List<PaymentMethodDO>> paymentMethodDOMap = MapUtil.newHashMap();
        if (platformEnum == PlatformEnum.POP) {
            List<EnterpriseDO> enterpriseDOList = enterpriseService.listByIds(eids);
            Map<Long, EnterpriseDO> enterpriseDOMap = enterpriseDOList.stream().collect(Collectors.toMap(EnterpriseDO::getId, Function.identity()));

            List<Long> channelIds = enterpriseDOList.stream().map(EnterpriseDO::getChannelId).distinct().collect(Collectors.toList());
            Map<Long, List<PaymentMethodDO>> channelPaymentMethodMap = enterpriseChannelPaymentMethodService.mapByChannelIds(channelIds);

            for (Long eid : eids) {
                paymentMethodDOMap.put(eid, channelPaymentMethodMap.getOrDefault(enterpriseDOMap.get(eid).getChannelId(), ListUtil.empty()));
            }
        } else if (platformEnum == PlatformEnum.B2B) {
            paymentMethodDOMap = shopPaymentMethodService.listByEids(eids);
        }

        Map<Long, List<PaymentMethodDO>> result = MapUtil.newHashMap();

        Map<Long, List<EnterpriseCustomerPaymentMethodDO>> map = list.stream().collect(Collectors.groupingBy(EnterpriseCustomerPaymentMethodDO::getEid));
        for (Long eid : map.keySet()) {
            List<Long> customerPayMethodIds = map.get(eid).stream().map(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId).distinct().collect(Collectors.toList());
            List<PaymentMethodDO> customerPaymentMethodList = paymentMethodDOMap.getOrDefault(eid, ListUtil.empty()).stream().filter(e -> customerPayMethodIds.contains(e.getCode())).collect(Collectors.toList());
            result.put(eid, customerPaymentMethodList);
        }

        // 针对以岭分公司的特殊处理
        if (result.keySet().contains(Constants.YILING_EID)) {
            yilingSubEids.forEach(e -> {
                result.put(e, result.get(Constants.YILING_EID));
            });
        }

        return result;
    }

    @Override
    public List<PaymentMethodDO> listByEidAndCustomerEid(Long eid, Long customerEid, PlatformEnum platformEnum) {
        Map<Long, List<PaymentMethodDO>> map = this.listByEidAndCustomerEids(eid, ListUtil.toList(customerEid), platformEnum);

        List<PaymentMethodDO> list = map.get(customerEid);
        if (CollUtil.isEmpty(list)) {
            ListUtil.empty();
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCustomerPaymentMethods(Long eid, Long customerEid, List<Long> paymentMethodIds, PlatformEnum platformEnum, Long opUserId) {
        List<EnterpriseCustomerPaymentMethodDO> originalPaymentMethodList = this.listCustomerPaymentMethods(eid, customerEid, platformEnum);
        if (CollUtil.isEmpty(originalPaymentMethodList)) {
            return this.addCustomerPaymentMethods(eid, customerEid, paymentMethodIds, platformEnum, opUserId);
        }

        // 客户原有的字符方式ID列表
        List<Long> originalIds = originalPaymentMethodList.stream().map(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId).distinct().collect(Collectors.toList());

        if (CollUtil.isEmpty(paymentMethodIds)) {
            return this.removeCustomerPaymentMethods(eid, customerEid, originalIds, platformEnum, opUserId);
        }

        // 本次需要移除的支付方式ID列表
        List<Long> removeIds = originalIds.stream().filter(e -> !paymentMethodIds.contains(e)).collect(Collectors.toList());
        this.removeCustomerPaymentMethods(eid, customerEid, removeIds, platformEnum, opUserId);

        // 本次需要新增的支付方式ID列表
        List<Long> addIds = paymentMethodIds.stream().filter(e -> !originalIds.contains(e)).collect(Collectors.toList());
        this.addCustomerPaymentMethods(eid, customerEid, addIds, platformEnum, opUserId);

        return true;
    }

    @Override
    public Boolean openOfflinePay(UpdateOpenPayRequest request) {
        if (Objects.nonNull(request.getQueryRequest())) {
            List<Long> customerEidList = this.getCustomerEidList(request.getQueryRequest());
            if (CollUtil.isEmpty(customerEidList)) {
                return true;
            }
            request.setCustomerEidList(customerEidList);
        }

        // 获取线下支付的编码
        List<PaymentMethodDO> paymentMethodDOList = paymentMethodService.listByPlatform(request.getPlatformEnum(), EnableStatusEnum.ENABLED);
        Long code = paymentMethodDOList.stream().filter(paymentMethodDO -> StrUtil.equals(paymentMethodDO.getName(), "线下支付")).findAny().orElse(new PaymentMethodDO()).getCode();
        if (Objects.isNull(code)) {
            return false;
        }

        // 批量获取客户支付方式
        Map<Long, List<EnterpriseCustomerPaymentMethodDO>> paymentMethodMap = this.listMapCustomerPaymentMethods(request.getEid(), request.getCustomerEidList(), request.getPlatformEnum());
        // 获取线程池
        Executor executor = Optional.ofNullable(springAsyncConfig.getAsyncExecutor()).orElseThrow(() -> new BusinessException(ResultCode.FAILED));

        // 开通
        if (request.getOpType() == 1) {
            // 店铺必须有线下支付方式，这里才可以开通线下支付
            List<PaymentMethodDO> shopPaymentMethodDOList = shopPaymentMethodService.listByEid(request.getEid());
            List<Long> shopPaymentMethodIdList = shopPaymentMethodDOList.stream().map(PaymentMethodDO::getCode).collect(Collectors.toList());
            if (!shopPaymentMethodIdList.contains(code)) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXIST_PAYMENT_METHOD);
            }

            // 异步执行开通线下支付
            CompletableFuture.runAsync(() -> addCustomerPaymentMethods(request, code, paymentMethodMap, executor), springAsyncConfig.getAsyncExecutor());
            log.info("B2B开通线下支付 企业ID={} 客户数量={}", request.getEid(), request.getCustomerEidList().size());

        } else if (request.getOpType() == 2) {
            // 异步执行关闭线下支付
            CompletableFuture.runAsync(() -> closeCustomerPaymentMethods(request, code, paymentMethodMap, executor), springAsyncConfig.getAsyncExecutor());
            log.info("B2B关闭线下支付 企业ID={} 客户数量={}", request.getEid(), request.getCustomerEidList().size());

        }

        return true;
    }

    public List<Long> getCustomerEidList(QueryCustomerPageListRequest request) {

        Page<EnterpriseCustomerDO> page;
        int current = 1;
        List<Long> eidList = ListUtil.toList();

        do {
            // 获取客户企业ID
            request.setCurrent(current);
            request.setSize(1000);
            page = enterpriseCustomerService.pageList(request);
            if (Objects.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            List<Long> customerEidList = page.getRecords().stream().map(EnterpriseCustomerDO::getCustomerEid).distinct().collect(Collectors.toList());
            eidList.addAll(customerEidList);
            current = current + 1;

        } while (CollUtil.isNotEmpty(page.getRecords()));

        log.info("查询结果开通线下支付的客户数量={}，商家企业ID={}", eidList.size(), request.getEids().get(0));
        return eidList;
    }

    /**
     * 异步执行开通线下支付
     *
     * @param request
     * @param code
     * @param paymentMethodMap
     * @param executor
     */
    private void addCustomerPaymentMethods(UpdateOpenPayRequest request, Long code, Map<Long, List<EnterpriseCustomerPaymentMethodDO>> paymentMethodMap, Executor executor) {
        request.getCustomerEidList().forEach(customerEid -> executor.execute(() -> {
            // 开通线下支付
            if (CollUtil.isEmpty(paymentMethodMap.get(customerEid))) {
                this.addCustomerPaymentMethods(request.getEid(), customerEid, ListUtil.toList(code), request.getPlatformEnum(), request.getOpUserId());
                return;
            }

            List<Long> paymentMethodList = paymentMethodMap.get(customerEid).stream().map(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId).collect(Collectors.toList());
            if (!paymentMethodList.contains(code)) {
                this.addCustomerPaymentMethods(request.getEid(), customerEid, ListUtil.toList(code), request.getPlatformEnum(), request.getOpUserId());
            }
        }));
    }

    /**
     * 异步执行关闭线下支付
     *
     * @param request
     * @param code
     * @param paymentMethodMap
     * @param executor
     */
    private void closeCustomerPaymentMethods(UpdateOpenPayRequest request, Long code, Map<Long, List<EnterpriseCustomerPaymentMethodDO>> paymentMethodMap, Executor executor) {
        request.getCustomerEidList().forEach(customerEid -> executor.execute(() -> {
            // 关闭线下支付
            List<EnterpriseCustomerPaymentMethodDO> customerPaymentMethodDOList = paymentMethodMap.get(customerEid);
            List<Long> paymentMethodList = customerPaymentMethodDOList.stream().map(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId).collect(Collectors.toList());
            if (paymentMethodList.contains(code)) {
                Map<Long, EnterpriseCustomerPaymentMethodDO> paymentMethodDOMap = customerPaymentMethodDOList.stream().collect(Collectors.toMap(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId, Function.identity(), (k1, k2) -> k2));
                EnterpriseCustomerPaymentMethodDO paymentMethodDO = paymentMethodDOMap.get(code);
                paymentMethodDO.setOpUserId(request.getOpUserId());
                this.deleteByIdWithFill(paymentMethodDO);
            }
        }));
    }

    /**
     * 获取批量客户支付方式
     *
     * @param eid 企业ID
     * @param customerEidList 客户企业ID
     * @param platformEnum 平台
     * @return key为客户企业ID，value为对应的支付方式
     */
    public Map<Long, List<EnterpriseCustomerPaymentMethodDO>> listMapCustomerPaymentMethods(Long eid, List<Long> customerEidList, PlatformEnum platformEnum) {
        QueryWrapper<EnterpriseCustomerPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerPaymentMethodDO::getEid, eid)
                .in(EnterpriseCustomerPaymentMethodDO::getCustomerEid, customerEidList)
                .eq(EnterpriseCustomerPaymentMethodDO::getPlatform, platformEnum.getCode());

        List<EnterpriseCustomerPaymentMethodDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.newHashMap();
        }

        return list.stream().collect(Collectors.groupingBy(EnterpriseCustomerPaymentMethodDO::getCustomerEid));
    }

    private List<EnterpriseCustomerPaymentMethodDO> listCustomerPaymentMethods(Long eid, Long customerEid, PlatformEnum platformEnum) {
        QueryWrapper<EnterpriseCustomerPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerPaymentMethodDO::getEid, eid)
                .eq(EnterpriseCustomerPaymentMethodDO::getCustomerEid, customerEid)
                .eq(EnterpriseCustomerPaymentMethodDO::getPlatform, platformEnum.getCode());

        List<EnterpriseCustomerPaymentMethodDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    private boolean addCustomerPaymentMethods(Long eid, Long customerEid, List<Long> paymentMethodIds, PlatformEnum platformEnum, Long opUserId) {
        if (CollUtil.isEmpty(paymentMethodIds)) {
            return true;
        }

        paymentMethodIds.forEach(e -> {
            EnterpriseCustomerPaymentMethodDO entity = new EnterpriseCustomerPaymentMethodDO();
            entity.setEid(eid);
            entity.setCustomerEid(customerEid);
            entity.setPaymentMethodId(e);
            entity.setPlatform(platformEnum.getCode());
            entity.setOpUserId(opUserId);
            this.save(entity);
        });

        return true;
    }

    private boolean removeCustomerPaymentMethods(Long eid, Long customerEid, List<Long> paymentMethodIds, PlatformEnum platformEnum, Long opUserId) {
        if (CollUtil.isEmpty(paymentMethodIds)) {
            return true;
        }

        QueryWrapper<EnterpriseCustomerPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerPaymentMethodDO::getEid, eid)
                .eq(EnterpriseCustomerPaymentMethodDO::getCustomerEid, customerEid)
                .in(EnterpriseCustomerPaymentMethodDO::getPaymentMethodId, paymentMethodIds)
                .eq(EnterpriseCustomerPaymentMethodDO::getPlatform, platformEnum.getCode());

        EnterpriseCustomerPaymentMethodDO entity = new EnterpriseCustomerPaymentMethodDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, queryWrapper) > 0;
    }
}
