package com.yiling.sjms.gb.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.gb.dto.CustomerDTO;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.dto.request.SaveCustomerRequest;

/**
 * 团购单位
 *
 * @author: wei.wang
 * @date: 2022/11/30
 */
public interface GbCustomerApi {
    /**
     * 增加团购单位
     * @param request
     * @return
     */
    Long addCustomer(SaveCustomerRequest request);

    /**
     * 获取团购单位
     * @param request
     * @return
     */
    Page<CustomerDTO> getCustomer(QueryGBGoodsInfoPageRequest request);

    /**
     * 通过名称获取团购单位
     * @param name
     * @return
     */
    CustomerDTO getCustomerByName(String name);

    /**
     * 通过统一信用代码获取团购单位
     * @param CreditCode
     * @return
     */
    CustomerDTO getCustomerByCreditCode(String CreditCode);
}
