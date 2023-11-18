package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDeliveryAddressDTO;
import com.yiling.marketing.lotteryactivity.dto.request.SaveDeliveryAddressRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDeliveryAddressDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityDeliveryAddressMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityDeliveryAddressService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动-奖品收货地址表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
@Slf4j
@Service
public class LotteryActivityDeliveryAddressServiceImpl extends BaseServiceImpl<LotteryActivityDeliveryAddressMapper, LotteryActivityDeliveryAddressDO> implements LotteryActivityDeliveryAddressService {

    @DubboReference
    LocationApi locationApi;

    @Override
    public List<LotteryActivityDeliveryAddressDTO> queryList(Long uid) {
        LambdaQueryWrapper<LotteryActivityDeliveryAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityDeliveryAddressDO::getUid, uid);
        wrapper.orderByDesc(LotteryActivityDeliveryAddressDO::getUpdateTime, LotteryActivityDeliveryAddressDO::getId);
        return PojoUtils.map(this.list(wrapper), LotteryActivityDeliveryAddressDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDeliveryAddress(SaveDeliveryAddressRequest request) {
        LotteryActivityDeliveryAddressDO deliveryAddressDO = PojoUtils.map(request, LotteryActivityDeliveryAddressDO.class);
        String[] namesByCodes = locationApi.getNamesByCodes(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        deliveryAddressDO.setProvinceName(namesByCodes[0]);
        deliveryAddressDO.setCityName(namesByCodes[1]);
        deliveryAddressDO.setRegionName(namesByCodes[2]);

        if (Objects.isNull(request.getId()) || request.getId() == 0) {
            // 校验不能超过50条
            List<LotteryActivityDeliveryAddressDTO> addressDTOList = this.queryList(request.getUid());
            if (addressDTOList.size() >= 50) {
                throw new BusinessException(LotteryActivityErrorCode.DELIVERY_ADDRESS_NUMBER_OVER);
            }
            return this.save(deliveryAddressDO);

        } else {
            return this.updateById(deliveryAddressDO);
        }

    }

}
