package com.yiling.user.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.SetDefaultDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.enterprise.entity.DeliveryAddressDO;
import com.yiling.user.enterprise.service.DeliveryAddressService;

/**
 * 收货地址 API 实现
 * 
 * @author xuan.zhou
 * @date 2021/6/23
 */
@DubboService
public class DeliveryAddressApiImpl implements DeliveryAddressApi {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Override
    public DeliveryAddressDTO getDefaultAddressByEid(Long eid) {
        DeliveryAddressDO deliveryAddressDO = deliveryAddressService.getDefaultAddressByEid(eid);
        return PojoUtils.map(deliveryAddressDO, DeliveryAddressDTO.class);
    }

    @Override
    public DeliveryAddressDTO getById(Long id) {
        DeliveryAddressDO deliveryAddressDO = deliveryAddressService.getById(id);
        return PojoUtils.map(deliveryAddressDO, DeliveryAddressDTO.class);
    }

    @Override
    public boolean add(AddDeliveryAddressRequest request) {
        return deliveryAddressService.add(request);
    }

    @Override
    public boolean update(UpdateDeliveryAddressRequest request) {
        return deliveryAddressService.update(request);
    }

    @Override
    public List<DeliveryAddressDTO> selectDeliveryAddressList(QueryDeliveryAddressRequest request) {

        List<DeliveryAddressDO> list =  deliveryAddressService.selectDeliveryAddressPageList(request);

        return PojoUtils.map(list,DeliveryAddressDTO.class);
    }

    @Override
    public boolean delete(Long currentUserId, Long id) {
        return deliveryAddressService.delete(currentUserId, id);
    }

    @Override
    public boolean setDefault(SetDefaultDeliveryAddressRequest request) {
        return deliveryAddressService.setDefault(request);
    }
}
