package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.SetDefaultDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.enterprise.entity.DeliveryAddressDO;

/**
 * <p>
 * 配送地址表 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-05-20
 */
public interface DeliveryAddressService extends BaseService<DeliveryAddressDO> {

    /**
     * 获取企业默认收货地址
     *
     * @param eid 企业ID
     * @return
     */
    DeliveryAddressDO getDefaultAddressByEid(Long eid);

    /**
     * 添加收货地址
     *
     * @param request
     */
    boolean add(AddDeliveryAddressRequest request);

    /**
     * 修改收货地址
     *
     * @param request
     */
    boolean update(UpdateDeliveryAddressRequest request);

    /**
     * 查询收货地址信息
     *
     * @param request 企业信息
     * @return
     */
    List<DeliveryAddressDO> selectDeliveryAddressPageList(QueryDeliveryAddressRequest request);

    /**
     * 设置默认收货地址
     *
     * @param request
     * @return
     */
    boolean setDefault(SetDefaultDeliveryAddressRequest request);

    /**
     * 删除地址
     *
     * @param currentUserId
     * @param id
     * @return
     */
    boolean delete(Long currentUserId, Long id);
}
