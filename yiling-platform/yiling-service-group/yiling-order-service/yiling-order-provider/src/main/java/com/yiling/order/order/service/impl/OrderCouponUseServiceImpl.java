package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.order.order.dao.OrderCouponUseMapper;
import com.yiling.order.order.dto.request.QueryOrderCouponUsePageRequest;
import com.yiling.order.order.entity.OrderCouponUseDO;
import com.yiling.order.order.enums.OrderCouponUseReturnTypeEnum;
import com.yiling.order.order.service.OrderCouponUseService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 订单优惠劵使用记录表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-23
 */
@Service
public class OrderCouponUseServiceImpl extends BaseServiceImpl<OrderCouponUseMapper, OrderCouponUseDO> implements OrderCouponUseService {


    @Override
    public List<OrderCouponUseDO> selectOrderCouponList(Long orderId, Integer couponType) {

        QueryWrapper<OrderCouponUseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderCouponUseDO::getOrderId, orderId);

        if (couponType != null) {
            wrapper.lambda().eq(OrderCouponUseDO::getCouponType, couponType);
        }

        return this.list(wrapper);
    }

    /**
     * @param orderId    订单ID
     * @param couponType 购物劵类型
     * @param isReturn   退款类型
     * @return
     */
    @Override
    public List<OrderCouponUseDO> listOrderCouponReturnType(Long orderId, Integer couponType, Integer isReturn) {
        QueryWrapper<OrderCouponUseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderCouponUseDO::getOrderId, orderId);

        if (couponType != null) {
            wrapper.lambda().eq(OrderCouponUseDO::getCouponType, couponType);
        }
        if (isReturn != null) {
            wrapper.lambda().eq(OrderCouponUseDO::getIsReturn, isReturn);
        }

        return this.list(wrapper);
    }

    @Override
    public List<OrderCouponUseDO> listByCouponId(Long couponId) {
        QueryWrapper<OrderCouponUseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderCouponUseDO::getCouponId, couponId);
        return this.list(wrapper);
    }

    @Override
    public List<OrderCouponUseDO> listByCouponActivityId(Long couponActivityId) {
        QueryWrapper<OrderCouponUseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderCouponUseDO::getCouponActivityId, couponActivityId);
        return this.list(wrapper);
    }

    @Override
    public List<OrderCouponUseDO> listByCouponIdList(List<Long> couponIdList) {
        QueryWrapper<OrderCouponUseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderCouponUseDO::getCouponId, couponIdList);
        return this.list(wrapper);
    }

    @Override
    public Page<OrderCouponUseDO> listPage(QueryOrderCouponUsePageRequest request) {
        LambdaQueryWrapper<OrderCouponUseDO> queryWrapper = getCouponActivityWrapper(request);
        return page(request.getPage(), queryWrapper);
    }

    @Override
    public List<Map<String, Long>> getCountByCouponActivityId(List<Long> couponActivityIdList) {
        if(CollUtil.isEmpty(couponActivityIdList)){
            return ListUtil.empty();
        }
        return this.baseMapper.getCountByCouponActivityId(couponActivityIdList);
    }

    @Override
    public Boolean returenCouponByIds(List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<OrderCouponUseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OrderCouponUseDO::getCouponId, ids);
        OrderCouponUseDO entity = new OrderCouponUseDO();
        entity.setIsReturn(OrderCouponUseReturnTypeEnum.RETURNED.getCode());
        entity.setOpTime(new Date());
        return this.update(entity, queryWrapper);
    }

    private LambdaQueryWrapper<OrderCouponUseDO> getCouponActivityWrapper(QueryOrderCouponUsePageRequest request) {
        LambdaQueryWrapper<OrderCouponUseDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getOrderId()) && request.getOrderId() != 0) {
            queryWrapper.eq(OrderCouponUseDO::getOrderId, request.getOrderId());
        }
        if (ObjectUtil.isNotNull(request.getCouponActivityId()) && request.getCouponActivityId() != 0) {
            queryWrapper.eq(OrderCouponUseDO::getCouponActivityId, request.getCouponActivityId());
        }
        if (ObjectUtil.isNotNull(request.getCouponId()) && request.getCouponId() != 0) {
            queryWrapper.eq(OrderCouponUseDO::getCouponId, request.getCouponId());
        }
        if (StrUtil.isNotEmpty(request.getCouponName())) {
            queryWrapper.like(OrderCouponUseDO::getCouponName, request.getCouponName());
        }
        if (ObjectUtil.isNotNull(request.getCouponType()) && request.getCouponType() != 0) {
            queryWrapper.eq(OrderCouponUseDO::getCouponType, request.getCouponType());
        }
        if (ObjectUtil.isNotNull(request.getIsReturn()) && request.getIsReturn() != 0) {
            queryWrapper.eq(OrderCouponUseDO::getIsReturn, request.getIsReturn());
        }
        if (ObjectUtil.isNotNull(request.getCreateTime())) {
            queryWrapper.ge(OrderCouponUseDO::getCreateTime, DateUtil.beginOfDay(request.getCreateTime()));
        }
        queryWrapper.orderByDesc(OrderCouponUseDO::getCreateTime);
        return queryWrapper;
    }

    @Override
    public List<OrderCouponUseDO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderCouponUseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderCouponUseDO::getOrderId, orderIds);
        return this.list(wrapper);
    }

}
