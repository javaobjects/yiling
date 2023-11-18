package com.yiling.user.enterprise.api;

import java.util.List;

import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.SetDefaultDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;

/**
 * 收货地址 API
 *
 * @author: gxl
 * @date: 2021/5/21
 */
public interface DeliveryAddressApi {

    /**
     * 获取企业默认收货地址
     *
     * @param eid 企业ID
     * @return
     */
    DeliveryAddressDTO getDefaultAddressByEid(Long eid);

    /**
     * 根据收货地址ID获取收货地址信息
     *
     * @param id 收货地址ID
     * @return
     */
    DeliveryAddressDTO getById(Long id);

    /**
     * 添加收货地址
     *
     * @param request
     * @return
     */
    boolean add(AddDeliveryAddressRequest request);

    /**
     * 修改收货地址
     *
     * @param request
     * @return
     */
    boolean update(UpdateDeliveryAddressRequest request);


    /**
     * 查询收货地址信息
     * @param request 企业信息
     * @return
     */
    List<DeliveryAddressDTO> selectDeliveryAddressList(QueryDeliveryAddressRequest request);

    /**
     * 删除收货地址
     *
     * @param currentUserId
     * @param id
     * @return
     */
    boolean delete(Long currentUserId, Long id);

    /**
     * 设置默认收货地址
     *
     * @param request
     * @return
     */
    boolean setDefault(SetDefaultDeliveryAddressRequest request);
}
