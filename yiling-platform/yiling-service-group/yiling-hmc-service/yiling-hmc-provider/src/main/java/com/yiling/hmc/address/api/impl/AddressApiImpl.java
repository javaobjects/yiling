package com.yiling.hmc.address.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.address.api.AddressApi;
import com.yiling.hmc.address.dto.AddressDTO;
import com.yiling.hmc.address.dto.request.AddressSaveOrUpdateRequest;
import com.yiling.hmc.address.dto.request.DeleteAddressRequest;
import com.yiling.hmc.address.service.AddressService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
@Slf4j
@DubboService
public class AddressApiImpl implements AddressApi {

    @Autowired
    private AddressService addressService;

    @Override
    public Boolean saveOrUpdateAddress(AddressSaveOrUpdateRequest request) {
        return addressService.saveOrUpdateAddress(request);
    }

    @Override
    public Boolean deleteAddress(DeleteAddressRequest request) {
        return addressService.deleteAddress(request);
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        return PojoUtils.map(addressService.getById(id), AddressDTO.class);
    }

    @Override
    public Page<AddressDTO> queryAddressPage(QueryPageListRequest request) {
        return addressService.queryAddressPage(request);
    }
}
