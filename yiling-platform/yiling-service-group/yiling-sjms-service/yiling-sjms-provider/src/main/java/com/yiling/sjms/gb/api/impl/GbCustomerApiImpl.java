package com.yiling.sjms.gb.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbCustomerApi;
import com.yiling.sjms.gb.dto.CustomerDTO;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.dto.request.SaveCustomerRequest;
import com.yiling.sjms.gb.service.GbCustomerService;

/**
 * 团购单位
 *
 * @author: wei.wang
 * @date: 2022/11/30
 */
@DubboService
public class GbCustomerApiImpl implements GbCustomerApi {
    @Autowired
    private GbCustomerService gbCustomerService;
    @Override
    public Long addCustomer(SaveCustomerRequest request) {
        return gbCustomerService.addCustomer(request);
    }

    @Override
    public Page<CustomerDTO> getCustomer(QueryGBGoodsInfoPageRequest request) {
        return PojoUtils.map(gbCustomerService.getCustomer(request),CustomerDTO.class);
    }

    @Override
    public CustomerDTO getCustomerByName(String name) {

        return PojoUtils.map(gbCustomerService.getCustomerByName(name),CustomerDTO.class);
    }

    @Override
    public CustomerDTO getCustomerByCreditCode(String CreditCode) {
        return PojoUtils.map(gbCustomerService.getCustomerByCreditCode(CreditCode),CustomerDTO.class);
    }
}
