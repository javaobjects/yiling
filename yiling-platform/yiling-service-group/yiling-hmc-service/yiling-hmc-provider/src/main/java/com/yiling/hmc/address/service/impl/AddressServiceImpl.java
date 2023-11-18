package com.yiling.hmc.address.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.address.dao.AddressMapper;
import com.yiling.hmc.address.dto.AddressDTO;
import com.yiling.hmc.address.dto.request.AddressSaveOrUpdateRequest;
import com.yiling.hmc.address.dto.request.DeleteAddressRequest;
import com.yiling.hmc.address.entity.AddressDO;
import com.yiling.hmc.address.enums.AddressDefaultFlagEnum;
import com.yiling.hmc.address.service.AddressService;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2023-02-16
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<AddressMapper, AddressDO> implements AddressService {

    @Override
    public Boolean saveOrUpdateAddress(AddressSaveOrUpdateRequest request) {
        if (request.getDefaultFlag().equals(AddressDefaultFlagEnum.IS_DEFAULT.getCode())) {
            //取消其他默认地址设置
            LambdaQueryWrapper<AddressDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AddressDO::getCreateUser, request.getOpUserId()).eq(AddressDO::getDefaultFlag, AddressDefaultFlagEnum.IS_DEFAULT.getCode());
            AddressDO addressDO = new AddressDO();
            addressDO.setUpdateUser(request.getOpUserId());
            addressDO.setUpdateTime(DateUtil.date());
            addressDO.setDefaultFlag(AddressDefaultFlagEnum.NO_DEFAULT.getCode());
            this.update(addressDO, wrapper);
        }
        AddressDO addressDO = PojoUtils.map(request, AddressDO.class);
        addressDO.setUpdateUser(request.getOpUserId());
        addressDO.setCreateUser(request.getOpUserId());
        boolean b = this.saveOrUpdate(addressDO);
        return b;
    }

    @Override
    public Boolean deleteAddress(DeleteAddressRequest request) {
        int i = this.deleteByIdWithFill(PojoUtils.map(request, AddressDO.class));
        if (i > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Page<AddressDTO> queryAddressPage(QueryPageListRequest request) {
        LambdaQueryWrapper<AddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressDO::getCreateUser, request.getOpUserId());
        wrapper.orderByDesc(AddressDO::getDefaultFlag, AddressDO::getCreateTime);
        Page<AddressDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, AddressDTO.class);
    }
}
