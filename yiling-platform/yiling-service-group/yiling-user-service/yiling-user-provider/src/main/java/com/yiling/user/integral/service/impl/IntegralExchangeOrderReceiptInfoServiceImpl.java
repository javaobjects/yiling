package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.request.UpdateReceiptAddressRequest;
import com.yiling.user.integral.entity.IntegralExchangeOrderReceiptInfoDO;
import com.yiling.user.integral.dao.IntegralExchangeOrderReceiptInfoMapper;
import com.yiling.user.integral.service.IntegralExchangeOrderReceiptInfoService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换订单收货信息表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class IntegralExchangeOrderReceiptInfoServiceImpl extends BaseServiceImpl<IntegralExchangeOrderReceiptInfoMapper, IntegralExchangeOrderReceiptInfoDO> implements IntegralExchangeOrderReceiptInfoService {

    @DubboReference
    LocationApi locationApi;

    @Override
    public IntegralExchangeOrderReceiptInfoDTO getByExchangeOrderId(Long exchangeOrderId) {
        LambdaQueryWrapper<IntegralExchangeOrderReceiptInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralExchangeOrderReceiptInfoDO::getOrderId, exchangeOrderId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralExchangeOrderReceiptInfoDTO.class);
    }

    @Override
    public Map<Long, IntegralExchangeOrderReceiptInfoDTO> getByExchangeOrderIdList(List<Long> exchangeOrderIdList) {
        if (CollUtil.isEmpty(exchangeOrderIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<IntegralExchangeOrderReceiptInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralExchangeOrderReceiptInfoDO::getOrderId, exchangeOrderIdList);
        List<IntegralExchangeOrderReceiptInfoDTO> receiptInfoDTOList = PojoUtils.map(this.list(wrapper), IntegralExchangeOrderReceiptInfoDTO.class);
        return receiptInfoDTOList.stream().collect(Collectors.toMap(IntegralExchangeOrderReceiptInfoDTO::getOrderId, Function.identity()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(UpdateReceiptAddressRequest request) {
        LambdaQueryWrapper<IntegralExchangeOrderReceiptInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralExchangeOrderReceiptInfoDO::getOrderId, request.getId());
        wrapper.last("limit 1");
        IntegralExchangeOrderReceiptInfoDO orderReceiptInfoDO = this.getOne(wrapper);

        IntegralExchangeOrderReceiptInfoDO receiptInfoDO = PojoUtils.map(request, IntegralExchangeOrderReceiptInfoDO.class);
        receiptInfoDO.setId(orderReceiptInfoDO.getId());
        // 省市区
        String[] namesByCodes = locationApi.getNamesByCodes(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        receiptInfoDO.setProvinceName(namesByCodes[0]);
        receiptInfoDO.setCityName(namesByCodes[1]);
        receiptInfoDO.setRegionName(namesByCodes[2]);
        return this.updateById(receiptInfoDO);
    }
}
