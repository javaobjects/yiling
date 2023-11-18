package com.yiling.user.enterprise.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.DeliveryAddressMapper;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.SetDefaultDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.enterprise.entity.DeliveryAddressDO;
import com.yiling.user.enterprise.service.DeliveryAddressService;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 配送地址表 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-05-20
 */
@Slf4j
@Service
public class DeliveryAddressServiceImpl extends BaseServiceImpl<DeliveryAddressMapper, DeliveryAddressDO> implements DeliveryAddressService {

    @Override
    public DeliveryAddressDO getDefaultAddressByEid(Long eid) {
        QueryWrapper<DeliveryAddressDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DeliveryAddressDO::getEid, eid).eq(DeliveryAddressDO::getDefaultFlag, 1).last("limit 1");

        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(AddDeliveryAddressRequest request) {
        // 校验不能超过50个地址
        LambdaQueryWrapper<DeliveryAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeliveryAddressDO::getEid, request.getEid());
        int count = this.count(wrapper);
        if (count >= 50) {
            throw new BusinessException(UserErrorCode.DELIVERY_ADDRESS_OVER_MAX_NUM);
        }
        // 设置默认地址
        setDefaultAddress(request.getDefaultFlag(), request.getEid());

        return this.save(PojoUtils.map(request, DeliveryAddressDO.class));
    }

    @Override
    public boolean update(UpdateDeliveryAddressRequest request) {
        DeliveryAddressDO deliveryAddressDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.DELIVERY_ADDRESS_NOT_EXIST));
        // 设置默认地址
        setDefaultAddress(request.getDefaultFlag(), deliveryAddressDO.getEid());

        return this.updateById(PojoUtils.map(request, DeliveryAddressDO.class));
    }

    /**
     * 取消已经存在的默认收货地址
     *
     * @param defaultFlag
     * @param eid
     */
    private void setDefaultAddress(Integer defaultFlag, Long eid) {
        if (defaultFlag != null && defaultFlag == 1) {

            DeliveryAddressDO defaultAddress = this.getDefaultAddressByEid(eid);
            if (defaultAddress != null) {

                defaultAddress.setDefaultFlag(0);
                this.updateById(defaultAddress);
            }
        }
    }


    @Override
    public List<DeliveryAddressDO> selectDeliveryAddressPageList(QueryDeliveryAddressRequest request) {
        QueryWrapper<DeliveryAddressDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DeliveryAddressDO::getEid, request.getEid());
        queryWrapper.lambda().orderByDesc(DeliveryAddressDO::getDefaultFlag, DeliveryAddressDO::getUpdateTime);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(SetDefaultDeliveryAddressRequest request) {
        // 取消已经存在的默认收货地址
        setDefaultAddress(1, request.getEid());
        // 设置默认地址
        DeliveryAddressDO deliveryAddressDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.DELIVERY_ADDRESS_NOT_EXIST));
        DeliveryAddressDO addressDO = new DeliveryAddressDO();
        addressDO.setId(deliveryAddressDO.getId());
        addressDO.setDefaultFlag(1);
        addressDO.setOpUserId(request.getOpUserId());

        return this.updateById(addressDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long currentUserId, Long id) {
        DeliveryAddressDO addressDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.DELIVERY_ADDRESS_NOT_EXIST));
        if (addressDO.getDefaultFlag() == 1) {
            // 如果删除的是默认地址，那么重新设置一个默认地址
            QueryDeliveryAddressRequest request = new QueryDeliveryAddressRequest();
            request.setEid(addressDO.getEid());
            List<DeliveryAddressDO> addressDOList = this.selectDeliveryAddressPageList(request);
            if (addressDOList.size() > 1) {
                DeliveryAddressDO defaultAddress = addressDOList.get(1);
                defaultAddress.setDefaultFlag(1);
                defaultAddress.setOpUserId(currentUserId);
                this.updateById(defaultAddress);
            }

        }

        // 删除选择的地址
        DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
        deliveryAddressDO.setId(id);
        deliveryAddressDO.setOpUserId(currentUserId);
        this.deleteByIdWithFill(deliveryAddressDO);

        return true;
    }
}
