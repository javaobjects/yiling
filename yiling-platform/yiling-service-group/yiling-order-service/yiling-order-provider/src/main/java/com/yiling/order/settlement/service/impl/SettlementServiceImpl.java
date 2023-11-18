package com.yiling.order.settlement.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.settlement.dao.SettlementDeatailMapper;
import com.yiling.order.settlement.dao.SettlementMapper;
import com.yiling.order.settlement.dto.SettlementDetailDTO;
import com.yiling.order.settlement.dto.SettlementFullDTO;
import com.yiling.order.settlement.dto.request.SaveSettlementRequest;
import com.yiling.order.settlement.entity.SettlementDO;
import com.yiling.order.settlement.entity.SettlementDetailDO;
import com.yiling.order.settlement.service.SettlementDetailService;
import com.yiling.order.settlement.service.SettlementService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 结算单 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-10
 */
@Service
public class SettlementServiceImpl extends BaseServiceImpl<SettlementMapper, SettlementDO> implements SettlementService {

    @Autowired
    SettlementDetailService settlementDetailService;

    @Autowired
    SettlementDeatailMapper settlementDeatailMapper;

    @Autowired
    OrderService orderService;

    @Override
    @Transactional
    public Boolean saveSettlement(SaveSettlementRequest request) {
        SettlementDO settlementDO = PojoUtils.map(request, SettlementDO.class);
        settlementDO.setBackTime(new Date());
        this.save(settlementDO);
        List<SettlementDetailDO> list = PojoUtils.map(request.getSaveSettlementDetailRequestList(), SettlementDetailDO.class);
        for (SettlementDetailDO settlementDetailDO : list) {
            settlementDetailDO.setSettlementId(settlementDO.getId());
        }
        return settlementDetailService.saveBatch(list);
    }

    @Override
    public List<SettlementFullDTO> getSettlementDetailByEidAndTime(QueryOrderUseAgreementRequest request) {
        List<SettlementDetailDO> settlementDetailDOList = settlementDeatailMapper.getSettlementDetailByEidAndTime(request);
        Map<Long, List<SettlementDetailDO>> settlementMap = settlementDetailDOList.stream().collect(Collectors.groupingBy(SettlementDetailDO::getSettlementId));
        if (MapUtil.isEmpty(settlementMap)) {
            return ListUtil.empty();
        }

        List<SettlementFullDTO> list = PojoUtils.map(this.listByIds(settlementMap.keySet()), SettlementFullDTO.class);
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(e -> {
                e.setSettlementDetailDTOList(PojoUtils.map(settlementMap.get(e.getId()), SettlementDetailDTO.class));
            });
        }
        return list;
    }

    @Override
    public BigDecimal getsettlementAmountByOrderId(Long orderId) {
        QueryWrapper<SettlementDO> orderWapper = new QueryWrapper<>();
        orderWapper.lambda().eq(SettlementDO::getOrderId, orderId);
        List<SettlementDO> list = this.list(orderWapper);
        BigDecimal total = BigDecimal.ZERO;
        if (CollUtil.isNotEmpty(list)) {
            for (SettlementDO settlementDO : list) {
                total = total.add(settlementDO.getSettlementAmount());
            }
        }
        return total;
    }
}
