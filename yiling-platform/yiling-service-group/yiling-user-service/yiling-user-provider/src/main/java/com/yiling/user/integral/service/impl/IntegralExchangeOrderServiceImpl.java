package com.yiling.user.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.bo.IntegralExchangeOrderItemBO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeOrderRequest;
import com.yiling.user.integral.dto.request.UpdateExpressRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeOrderRequest;
import com.yiling.user.integral.entity.IntegralExchangeOrderDO;
import com.yiling.user.integral.dao.IntegralExchangeOrderMapper;
import com.yiling.user.integral.entity.IntegralExchangeOrderReceiptInfoDO;
import com.yiling.user.integral.enums.IntegralExchangeOrderStatusEnum;
import com.yiling.user.integral.enums.IntegralExchangeOrderTypeEnum;
import com.yiling.user.integral.enums.IntegralGoodsTypeEnum;
import com.yiling.user.integral.service.IntegralExchangeOrderReceiptInfoService;
import com.yiling.user.integral.service.IntegralExchangeOrderService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换订单表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class IntegralExchangeOrderServiceImpl extends BaseServiceImpl<IntegralExchangeOrderMapper, IntegralExchangeOrderDO> implements IntegralExchangeOrderService {

    @Autowired
    StaffService staffService;
    @Autowired
    UserService userService;
    @Autowired
    IntegralExchangeOrderReceiptInfoService integralExchangeOrderReceiptInfoService;

    @DubboReference
    LocationApi locationApi;

    @Override
    public Page<IntegralExchangeOrderItemBO> queryListPage(QueryIntegralExchangeOrderPageRequest request) {
        QueryWrapper<IntegralExchangeOrderDO> wrapper = WrapperUtils.getWrapper(request);
        if (StrUtil.isNotEmpty(request.getMobile())) {
            Staff staff = staffService.getByMobile(request.getMobile());
            if (Objects.nonNull(staff)) {
                wrapper.lambda().eq(IntegralExchangeOrderDO::getCreateUser, staff.getId());
            }
        }
        wrapper.lambda().orderByDesc(IntegralExchangeOrderDO::getSubmitTime);

        Page<IntegralExchangeOrderDO> exchangeOrderDOPage = this.page(request.getPage(), wrapper);
        // 用户信息
        List<Long> userIdList = exchangeOrderDOPage.getRecords().stream().map(IntegralExchangeOrderDO::getCreateUser).distinct().collect(Collectors.toList());
        Map<Long, UserDO> userMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userIdList)) {
            userMap = userService.listByIds(userIdList).stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));
        }
        // 收货地址
        List<Long> exchangeOrderIdList = exchangeOrderDOPage.getRecords().stream().map(BaseDO::getId).collect(Collectors.toList());
        Map<Long, IntegralExchangeOrderReceiptInfoDTO> receiptInfoMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(exchangeOrderIdList)) {
            receiptInfoMap = integralExchangeOrderReceiptInfoService.getByExchangeOrderIdList(exchangeOrderIdList);
        }

        Page<IntegralExchangeOrderItemBO> page = PojoUtils.map(exchangeOrderDOPage, IntegralExchangeOrderItemBO.class);
        Map<Long, UserDO> finalUserMap = userMap;
        Map<Long, IntegralExchangeOrderReceiptInfoDTO> finalReceiptInfoMap = receiptInfoMap;

        page.getRecords().forEach(integralExchangeOrderItemBO -> {
            integralExchangeOrderItemBO.setMobile(finalUserMap.getOrDefault(integralExchangeOrderItemBO.getCreateUser(), new UserDO()).getMobile());
            // 设置收货信息
            IntegralExchangeOrderReceiptInfoDTO receiptInfoDTO = finalReceiptInfoMap.getOrDefault(integralExchangeOrderItemBO.getId(), new IntegralExchangeOrderReceiptInfoDTO());
            integralExchangeOrderItemBO.setContactor(receiptInfoDTO.getContactor());
            integralExchangeOrderItemBO.setContactorPhone(receiptInfoDTO.getContactorPhone());
            integralExchangeOrderItemBO.setProvinceName(receiptInfoDTO.getProvinceName());
            integralExchangeOrderItemBO.setProvinceCode(receiptInfoDTO.getProvinceCode());
            integralExchangeOrderItemBO.setCityName(receiptInfoDTO.getCityName());
            integralExchangeOrderItemBO.setCityCode(receiptInfoDTO.getCityCode());
            integralExchangeOrderItemBO.setRegionName(receiptInfoDTO.getRegionName());
            integralExchangeOrderItemBO.setRegionCode(receiptInfoDTO.getRegionCode());
            integralExchangeOrderItemBO.setAddress(receiptInfoDTO.getAddress());
            integralExchangeOrderItemBO.setExpressCompany(receiptInfoDTO.getExpressCompany());
            integralExchangeOrderItemBO.setExpressOrderNo(receiptInfoDTO.getExpressOrderNo());
        });

        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exchange(UpdateIntegralExchangeOrderRequest request) {
        // 全部兑付
        if (IntegralExchangeOrderTypeEnum.ALL == IntegralExchangeOrderTypeEnum.getByCode(request.getExchangeType())) {
            LambdaQueryWrapper<IntegralExchangeOrderDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(IntegralExchangeOrderDO::getStatus, IntegralExchangeOrderStatusEnum.UN_CASH.getCode());
            List<IntegralExchangeOrderDO> exchangeOrderDOList = this.list(wrapper);
            this.updateExchangeStatus(exchangeOrderDOList, request.getOpUserId());

        } else if (IntegralExchangeOrderTypeEnum.CURRENT == IntegralExchangeOrderTypeEnum.getByCode(request.getExchangeType())) {
            // 兑付当前页
            LambdaQueryWrapper<IntegralExchangeOrderDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(IntegralExchangeOrderDO::getId, request.getIdList());
            wrapper.eq(IntegralExchangeOrderDO::getStatus, IntegralExchangeOrderStatusEnum.UN_CASH.getCode());
            List<IntegralExchangeOrderDO> exchangeOrderDOList = this.list(wrapper);
            this.updateExchangeStatus(exchangeOrderDOList, request.getOpUserId());

        } else {
            // 单个兑付
            if (Objects.isNull(request.getId()) || request.getId() == 0 ) {
                return true;
            }

            IntegralExchangeOrderDO exchangeOrderDO = this.getById(request.getId());

            // 真实物品
            if (IntegralGoodsTypeEnum.getByCode(exchangeOrderDO.getGoodsType()) == IntegralGoodsTypeEnum.REAL_GOODS) {
                if (Objects.isNull(request.getOrderReceiptInfo())) {
                    throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_REAL_GOODS_RECEIPT);
                }
                IntegralExchangeOrderReceiptInfoDTO receiptInfoDTO = integralExchangeOrderReceiptInfoService.getByExchangeOrderId(request.getId());
                IntegralExchangeOrderReceiptInfoDO receiptInfoDO = PojoUtils.map(request.getOrderReceiptInfo(), IntegralExchangeOrderReceiptInfoDO.class);
                receiptInfoDO.setOpUserId(request.getOpUserId());
                receiptInfoDO.setOrderId(exchangeOrderDO.getId());
                receiptInfoDO.setOrderNo(exchangeOrderDO.getOrderNo());
                // 省市区
                String[] namesByCodes = locationApi.getNamesByCodes(request.getOrderReceiptInfo().getProvinceCode(), request.getOrderReceiptInfo().getCityCode(), request.getOrderReceiptInfo().getRegionCode());
                receiptInfoDO.setProvinceName(namesByCodes[0]);
                receiptInfoDO.setCityName(namesByCodes[1]);
                receiptInfoDO.setRegionName(namesByCodes[2]);
                if (Objects.isNull(receiptInfoDTO)) {
                    integralExchangeOrderReceiptInfoService.save(receiptInfoDO);
                } else {
                    receiptInfoDO.setId(receiptInfoDTO.getId());
                    integralExchangeOrderReceiptInfoService.updateById(receiptInfoDO);
                }

            }
            if (!exchangeOrderDO.getStatus().equals(IntegralExchangeOrderStatusEnum.HAD_CASH.getCode())) {
                this.updateExchangeStatus(ListUtil.toList(exchangeOrderDO), request.getOpUserId());
            }

        }

        return true;
    }

    @Override
    public Map<Long, List<IntegralExchangeOrderDO>> getByExchangeGoodsIdList(QueryIntegralExchangeOrderRequest request) {
        if (Objects.isNull(request)) {
            return MapUtil.newHashMap();
        }

        QueryWrapper<IntegralExchangeOrderDO> wrapper = WrapperUtils.getWrapper(request);
        List<IntegralExchangeOrderDO> list = this.list(wrapper);
        return list.stream().collect(Collectors.groupingBy(IntegralExchangeOrderDO::getExchangeGoodsId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean atOnceExchange(UpdateExpressRequest request) {
        IntegralExchangeOrderReceiptInfoDTO orderReceiptInfoDTO = integralExchangeOrderReceiptInfoService.getByExchangeOrderId(request.getId());
        // 更新快递信息
        IntegralExchangeOrderReceiptInfoDO receiptInfoDO = PojoUtils.map(request, IntegralExchangeOrderReceiptInfoDO.class);
        receiptInfoDO.setId(orderReceiptInfoDTO.getId());
        integralExchangeOrderReceiptInfoService.updateById(receiptInfoDO);

        // 更新兑付状态
        IntegralExchangeOrderDO exchangeOrderDO = new IntegralExchangeOrderDO();
        exchangeOrderDO.setId(request.getId());
        exchangeOrderDO.setStatus(IntegralExchangeOrderStatusEnum.HAD_CASH.getCode());
        exchangeOrderDO.setExchangeTime(new Date());
        exchangeOrderDO.setOpUserId(request.getOpUserId());
        return this.updateById(exchangeOrderDO);
    }

    /**
     * 批量更新兑付状态
     *
     * @param exchangeOrderDOList
     * @param opUserId
     */
    private void updateExchangeStatus(List<IntegralExchangeOrderDO> exchangeOrderDOList, Long opUserId) {
        if (CollUtil.isEmpty(exchangeOrderDOList)) {
            return;
        }

        exchangeOrderDOList.forEach(integralExchangeOrderDO -> {
            // 更新兑付状态
            IntegralExchangeOrderDO exchangeOrderDO = new IntegralExchangeOrderDO();
            exchangeOrderDO.setId(integralExchangeOrderDO.getId());
            exchangeOrderDO.setStatus(IntegralExchangeOrderStatusEnum.HAD_CASH.getCode());
            exchangeOrderDO.setExchangeTime(new Date());
            exchangeOrderDO.setOpUserId(opUserId);
            this.updateById(exchangeOrderDO);
        });

    }


}
