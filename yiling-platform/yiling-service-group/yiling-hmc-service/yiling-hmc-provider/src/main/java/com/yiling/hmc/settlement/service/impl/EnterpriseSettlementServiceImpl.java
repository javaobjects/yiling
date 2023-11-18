package com.yiling.hmc.settlement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageResultBO;
import com.yiling.hmc.settlement.bo.SettlementEnterprisePageBO;
import com.yiling.hmc.settlement.dao.EnterpriseSettlementMapper;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SettlementEnterprisePageRequest;
import com.yiling.hmc.settlement.entity.EnterpriseSettlementDO;
import com.yiling.hmc.settlement.enums.HmcSettlementErrorCode;
import com.yiling.hmc.settlement.enums.TerminalSettlementStatusEnum;
import com.yiling.hmc.settlement.service.EnterpriseSettlementService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商家结账表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseSettlementServiceImpl extends BaseServiceImpl<EnterpriseSettlementMapper, EnterpriseSettlementDO> implements EnterpriseSettlementService {

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    @Override
    public EnterpriseSettlementDO queryByDetailId(Long detailId) {
        QueryWrapper<EnterpriseSettlementDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EnterpriseSettlementDO::getDetailId, detailId);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<EnterpriseSettlementDO> listByOrderId(Long orderId) {
        QueryWrapper<EnterpriseSettlementDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EnterpriseSettlementDO::getOrderId, orderId);
        return this.list(wrapper);
    }

    @Override
    public List<SettlementEnterprisePageBO> pageSettlement(SettlementEnterprisePageRequest request) {
        List<SettlementEnterprisePageBO> enSettlement = this.getBaseMapper().pageEnSettlement(request);
        List<SettlementEnterprisePageBO> list = new ArrayList<>(enSettlement);
        List<SettlementEnterprisePageBO> unSettlement = this.getBaseMapper().pageUnSettlement(request);
        unSettlement.forEach(e -> e.setTerminalSettleStatus(1));
        list.addAll(unSettlement);
        return list;
    }

    @Override
    public List<SettlementEnterprisePageBO> countUnSettlementOrder(SettlementEnterprisePageRequest request) {
        return this.getBaseMapper().countUnSettlementOrder(request);
    }

    @Override
    public List<SettlementEnterprisePageBO> countEnSettlementOrder(SettlementEnterprisePageRequest request) {
        return this.getBaseMapper().countEnSettlementOrder(request);
    }

    @Override
    public Page<EnterpriseSettlementPageBO> pageList(EnterpriseSettlementPageRequest request) {
        if (null != request.getStartTime()) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (null != request.getStopTime()) {
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        if (null != request.getPayStartTime()) {
            request.setPayStartTime(DateUtil.beginOfDay(request.getPayStartTime()));
        }
        if (null != request.getPayStopTime()) {
            request.setPayStopTime(DateUtil.endOfDay(request.getPayStopTime()));
        }
        Page<EnterpriseSettlementDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().pageList(objectPage, request);
    }

    @Override
    public EnterpriseSettlementPageResultBO pageResult(EnterpriseSettlementPageRequest request) {
        if (null != request.getStartTime()) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (null != request.getStopTime()) {
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        if (null != request.getPayStartTime()) {
            request.setPayStartTime(DateUtil.beginOfDay(request.getPayStartTime()));
        }
        if (null != request.getPayStopTime()) {
            request.setPayStopTime(DateUtil.endOfDay(request.getPayStopTime()));
        }
        Page<EnterpriseSettlementDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        Page<EnterpriseSettlementPageBO> page = this.getBaseMapper().pageList(objectPage, request);

        EnterpriseSettlementPageResultBO resultBO = this.getBaseMapper().pageResult(request);
        resultBO.setPage(page);
        return resultBO;
    }

    @Override
    public List<EnterpriseSettlementBO> settlement(List<EnterpriseSettlementRequest> requestList, Long opUserId, Date opTime) {
        List<EnterpriseSettlementBO> resultList = new ArrayList<>();
        List<Long> detailIdList = requestList.stream().map(EnterpriseSettlementRequest::getDetailId).collect(Collectors.toList());
        if (CollUtil.isEmpty(detailIdList)) {
            return resultList;
        }
        Map<Long, EnterpriseSettlementRequest> settlementRequestMap = requestList.stream().collect(Collectors.toMap(EnterpriseSettlementRequest::getDetailId, e -> e, (k1, k2) -> k1));
        List<OrderDetailDO> orderDetailDOList = orderDetailService.listByIds(detailIdList);
        List<Long> falseList = new ArrayList<>();
        List<Long> trueList = new ArrayList<>();
        List<EnterpriseSettlementDO> addList = new ArrayList<>();
        for (OrderDetailDO orderDetailDO : orderDetailDOList) {
            EnterpriseSettlementDO enterpriseSettlementDO = this.queryByDetailId(orderDetailDO.getId());
            if (null != enterpriseSettlementDO) {
                falseList.add(orderDetailDO.getId());
                continue;
            }
            OrderDO orderDO = orderService.getById(orderDetailDO.getOrderId());
            if (null == orderDO) {
                falseList.add(orderDetailDO.getId());
                continue;
            }
            EnterpriseSettlementRequest enterpriseSettlementRequest = settlementRequestMap.get(orderDetailDO.getId());
            enterpriseSettlementDO = new EnterpriseSettlementDO();
            enterpriseSettlementDO.setEid(orderDO.getEid());
            enterpriseSettlementDO.setEname(orderDO.getEname());
            enterpriseSettlementDO.setOrderId(orderDetailDO.getOrderId());
            enterpriseSettlementDO.setDetailId(orderDetailDO.getId());
            enterpriseSettlementDO.setTerminalSettleStatus(TerminalSettlementStatusEnum.SETTLEMENT.getCode());
            enterpriseSettlementDO.setExecutionTime(enterpriseSettlementRequest.getExecutionTime());
            enterpriseSettlementDO.setSettleTime(enterpriseSettlementRequest.getSettleTime());
            enterpriseSettlementDO.setGoodsAmount(orderDetailDO.getTerminalSettleAmount());
            enterpriseSettlementDO.setSettleAmount(enterpriseSettlementRequest.getSettleAmount());
            enterpriseSettlementDO.setOpUserId(opUserId);
            enterpriseSettlementDO.setOpTime(opTime);
            addList.add(enterpriseSettlementDO);
            trueList.add(orderDetailDO.getId());

            OrderDO order = new OrderDO();
            order.setId(orderDO.getId());
            order.setTerminalSettleStatus(TerminalSettlementStatusEnum.SETTLEMENT.getCode());
            order.setTerminalSettleAmount(enterpriseSettlementRequest.getSettleAmount());
            order.setOpUserId(opUserId);
            order.setOpTime(opTime);
            orderService.updateById(order);
        }

        if (CollUtil.isNotEmpty(addList)) {
            this.saveBatch(addList);
            trueList.forEach(e -> resultList.add(EnterpriseSettlementBO.builder().type(1).detailId(e).result("此明细编号的商品结算成功！").build()));
        }

        if (CollUtil.isNotEmpty(falseList)) {
            falseList.forEach(e -> resultList.add(EnterpriseSettlementBO.builder().type(2).detailId(e).result("此明细编号的商品是已经结算的！").build()));
        }
        return resultList;
    }

    @Override
    public boolean importSettlementData(EnterpriseSettlementRequest request) {
        log.info("终端导入已结算订单,请求数据为:[{}]", JSON.toJSONString(request));
        OrderDetailDO orderDetailDO = orderDetailService.getById(request.getDetailId());
        if (null == orderDetailDO) {
            log.info("导入结算单的数据对应的订单明细不存在，请求数据为:[{}]", request);
            return false;
        }

        EnterpriseSettlementDO enterpriseSettlementDO = this.queryByDetailId(orderDetailDO.getId());
        if (null != enterpriseSettlementDO) {
            log.info("导入结算单的数据已经存在结算单，结算单信息为:[{}]", enterpriseSettlementDO);
            throw new BusinessException(HmcSettlementErrorCode.SETTLEMENT_EXISTS_ERROR);
        }

        OrderDO orderDO = orderService.getById(orderDetailDO.getOrderId());
        if (null == orderDO) {
            return false;
        }
        enterpriseSettlementDO = new EnterpriseSettlementDO();
        enterpriseSettlementDO.setEid(orderDO.getEid());
        enterpriseSettlementDO.setEname(orderDO.getEname());
        enterpriseSettlementDO.setOrderId(orderDetailDO.getOrderId());
        enterpriseSettlementDO.setDetailId(orderDetailDO.getId());
        enterpriseSettlementDO.setTerminalSettleStatus(TerminalSettlementStatusEnum.SETTLEMENT.getCode());
        enterpriseSettlementDO.setExecutionTime(request.getExecutionTime());
        enterpriseSettlementDO.setSettleTime(request.getSettleTime());
        enterpriseSettlementDO.setGoodsAmount(orderDetailDO.getTerminalSettleAmount());
        enterpriseSettlementDO.setSettleAmount(request.getSettleAmount());
        enterpriseSettlementDO.setOpUserId(request.getOpUserId());
        enterpriseSettlementDO.setOpTime(request.getOpTime());
        this.save(enterpriseSettlementDO);


        List<EnterpriseSettlementDO> enterpriseSettlementDOList = this.listByOrderId(orderDO.getId());
        if (CollUtil.isEmpty(enterpriseSettlementDOList)) {
            return true;
        }

        List<Long> settleDetailIdList = enterpriseSettlementDOList.stream().map(EnterpriseSettlementDO::getDetailId).collect(Collectors.toList());

        List<OrderDetailDO> orderDetailDOList = orderDetailService.listByOrderId(orderDO.getId());
        List<Long> detailIdList = orderDetailDOList.stream().map(OrderDetailDO::getId).collect(Collectors.toList());
        List<Long> noSettleDetailIdList = detailIdList.stream().filter(e -> !settleDetailIdList.contains(e)).collect(Collectors.toList());
        if (CollUtil.isEmpty(noSettleDetailIdList)) {
            OrderDO order = new OrderDO();
            order.setId(orderDO.getId());
            order.setTerminalSettleStatus(TerminalSettlementStatusEnum.SETTLEMENT.getCode());
            order.setOpUserId(request.getOpUserId());
            order.setOpTime(request.getOpTime());
            orderService.updateById(order);
        }
        return true;
    }
}
