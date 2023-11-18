package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRangeProvinceRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeProvinceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-28
 */
public interface UnlockSaleCustomerRangeProvinceService extends BaseService<UnlockSaleCustomerRangeProvinceDO> {

    List<String> getProvinceListByUscId(Long uscId);
    boolean save(SaveUnlockSaleCustomerRangeProvinceRequest request);

}
