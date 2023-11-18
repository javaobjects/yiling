package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRangeProvinceRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-28
 */
public interface UnlockSaleCustomerRangeProvinceApi {

    List<String> getProvinceListByUscId(Long uscId);

    boolean save(SaveUnlockSaleCustomerRangeProvinceRequest request);
}
