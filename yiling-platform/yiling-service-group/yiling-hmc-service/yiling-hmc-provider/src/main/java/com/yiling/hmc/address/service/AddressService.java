package com.yiling.hmc.address.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.hmc.address.dto.AddressDTO;
import com.yiling.hmc.address.dto.request.AddressSaveOrUpdateRequest;
import com.yiling.hmc.address.dto.request.DeleteAddressRequest;
import com.yiling.hmc.address.entity.AddressDO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2023-02-16
 */
public interface AddressService extends BaseService<AddressDO> {

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
     * 分页查询收货地址
     *
     * @param request
     * @return
     */
    Page<AddressDTO> queryAddressPage(QueryPageListRequest request);
}
