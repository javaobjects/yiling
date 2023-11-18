package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderLogMapper;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.entity.OrderLogDO;
import com.yiling.order.order.enums.OrderLogTypeEnum;
import com.yiling.order.order.service.OrderLogService;

/**
 * <p>
 * 订单日志 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Service
public class OrderLogServiceImpl extends BaseServiceImpl<OrderLogMapper, OrderLogDO> implements OrderLogService {

    /**
     * 获取日志信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderLogDTO> getOrderLogInfo(Long orderId) {
        QueryWrapper<OrderLogDO> logWrapper = new QueryWrapper<>();
        logWrapper.lambda().eq(OrderLogDO::getOrderId, orderId)
                .eq(OrderLogDO::getLogType, OrderLogTypeEnum.ORDER_ORDINARY_LOG.getCode() )
                .orderByDesc(OrderLogDO :: getId);
        List<OrderLogDO> logList = list(logWrapper);
        List<OrderLogDTO> logDtoList = PojoUtils.map(logList, OrderLogDTO.class);

        return logDtoList;
    }

    @Override
    public List<OrderLogDTO> getOrderLogInfo(Long orderId, Integer logType) {
        QueryWrapper<OrderLogDO> logWrapper = new QueryWrapper<>();
        logWrapper.lambda().eq(OrderLogDO::getOrderId, orderId).orderByDesc(OrderLogDO :: getLogTime);
        logWrapper.lambda().eq(OrderLogDO::getLogType, logType);
        List<OrderLogDO> logList = list(logWrapper);
        List<OrderLogDTO> logDtoList = PojoUtils.map(logList, OrderLogDTO.class);

        return logDtoList;
    }

    @Override
    public boolean add(Long orderId, String logContent) {
        OrderLogDO entity = new OrderLogDO();
        entity.setOrderId(orderId);
        entity.setLogContent(logContent);
        entity.setLogTime(new Date());
        return this.save(entity);
    }

    @Override
    public boolean add(Long orderId, Integer logType, String logContent) {
        OrderLogDO entity = new OrderLogDO();
        entity.setOrderId(orderId);
        entity.setLogType(logType);
        entity.setLogContent(logContent);
        entity.setLogTime(new Date());
        return this.save(entity);
    }

    @Override
    public boolean add(Long orderId, String logContent, Long opUserId) {
        OrderLogDO entity = new OrderLogDO();
        entity.setOrderId(orderId);
        entity.setLogContent(logContent);
        entity.setLogTime(new Date());
        entity.setOpUserId(opUserId);

        return this.save(entity);
    }
}
