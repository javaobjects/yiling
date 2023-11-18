package com.yiling.settlement.b2b.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.dto.B2BSettlementDTO;
import com.yiling.order.order.dto.B2BSettlementDetailDTO;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.settlement.b2b.bo.OrderGiftBO;
import com.yiling.settlement.b2b.dto.SettlementOrderCancelDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.entity.SettlementOrderCancelDO;
import com.yiling.settlement.b2b.dao.SettlementOrderCancelMapper;
import com.yiling.settlement.b2b.entity.SettlementOrderSyncDO;
import com.yiling.settlement.b2b.enums.OrderSyncDataStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncStatusEnum;
import com.yiling.settlement.b2b.service.SettlementOrderCancelService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 结算单的预付款订单违约订单同步表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-11
 */
@Slf4j
@Service
public class SettlementOrderCancelServiceImpl extends BaseServiceImpl<SettlementOrderCancelMapper, SettlementOrderCancelDO> implements SettlementOrderCancelService {

    @DubboReference
    OrderB2BApi orderB2BApi;

    @Override
    public Boolean createSettOrderCancelSync(String orderCode) {
        if (StrUtil.isBlank(orderCode)) {
            return Boolean.TRUE;
        }
        SettlementOrderCancelDTO dbOrder = querySettOrderSyncByOrderCode(orderCode);
        //如果订单数据已存在
        if (ObjectUtil.isNotNull(dbOrder)) {
            return Boolean.TRUE;
        }

        B2BSettlementDTO orderDTO = orderB2BApi.getB2bSettlementOne(orderCode);
        if (ObjectUtil.isNull(orderDTO)) {
            log.error("收货时根据订单号查询的订单的结算单数据为空，orderCode={}", orderCode);
            //存盘-同步状态记为失败
            SettlementOrderCancelDO var = new SettlementOrderCancelDO();
            var.setOrderNo(orderCode);
            var.setStatus(OrderSyncStatusEnum.FAIL.getCode());
            var.setDataStatus(OrderSyncDataStatusEnum.UNAUTHORIZED.getCode());
            return save(var);

        }
        //生成订单数据
        SettlementOrderCancelDO orderDataDO = generateSettOrderCancelData(orderDTO);

        return save(orderDataDO);
    }

    @Override
    public SettlementOrderCancelDTO querySettOrderSyncByOrderCode(String orderCode) {
        if (StrUtil.isBlank(orderCode)) {
            return null;
        }
        LambdaQueryWrapper<SettlementOrderCancelDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SettlementOrderCancelDO::getOrderNo, orderCode);
        SettlementOrderCancelDO orderDataDO = getOne(wrapper);

        return PojoUtils.map(orderDataDO, SettlementOrderCancelDTO.class);
    }

    /**
     * 生成SettlementOrderCancelDO
     *
     * @param order
     * @return
     */
    private SettlementOrderCancelDO generateSettOrderCancelData(B2BSettlementDTO order) {


        SettlementOrderCancelDO orderDataDO = new SettlementOrderCancelDO();
        PojoUtils.map(order, orderDataDO);
        orderDataDO.setOrderId(order.getId());
        orderDataDO.setCancelTime(new Date());
//        orderDataDO.setOrderCreatTime(order.get)
//        orderDataDO.setPresaleAmount(order.get)

        orderDataDO.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
        orderDataDO.setStatus(OrderSyncStatusEnum.SUCCESS.getCode());

        return orderDataDO;
    }
}
