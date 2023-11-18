package com.yiling.hmc.address.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.hmc.address.dto.AddressDTO;
import com.yiling.hmc.address.dto.request.AddressSaveOrUpdateRequest;
import com.yiling.hmc.address.dto.request.DeleteAddressRequest;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
public interface AddressApi {
    /**
     * 收货地址添加或修改
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateAddress(AddressSaveOrUpdateRequest request);

    /**
     * 删除收货地址
     *
     * @param request
     * @return
     */
    Boolean deleteAddress(DeleteAddressRequest request);

    /**
     * 根据id获取收货地址
     *
     * @param id
     * @return
     */
    AddressDTO getAddressById(Long id);

    /**
     * 分页查询收货地址
     *
     * @param request
     * @return
     */
    Page<AddressDTO> queryAddressPage(QueryPageListRequest request);
}
