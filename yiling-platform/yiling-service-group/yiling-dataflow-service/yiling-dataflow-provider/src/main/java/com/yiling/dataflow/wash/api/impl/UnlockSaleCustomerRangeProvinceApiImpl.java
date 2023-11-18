package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.yiling.dataflow.wash.api.UnlockSaleCustomerRangeProvinceApi;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRangeProvinceRequest;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerRangeProvinceService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-28
 */
@DubboService
public class UnlockSaleCustomerRangeProvinceApiImpl implements UnlockSaleCustomerRangeProvinceApi {

    @Autowired
    private UnlockSaleCustomerRangeProvinceService unlockSaleCustomerRangeProvinceService;

    @Override
    public List<String> getProvinceListByUscId(Long uscId) {
        return unlockSaleCustomerRangeProvinceService.getProvinceListByUscId(uscId);
    }

    @Override
    public boolean save(SaveUnlockSaleCustomerRangeProvinceRequest request) {
        return unlockSaleCustomerRangeProvinceService.save(request);
    }
}
