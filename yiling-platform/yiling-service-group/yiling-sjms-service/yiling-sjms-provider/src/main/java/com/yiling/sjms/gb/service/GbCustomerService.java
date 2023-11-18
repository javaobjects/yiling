package com.yiling.sjms.gb.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.dto.request.SaveCustomerRequest;
import com.yiling.sjms.gb.entity.GbCustomerDO;

/**
 * <p>
 * 团购单位 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
public interface GbCustomerService extends BaseService<GbCustomerDO> {
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
    Page<GbCustomerDO> getCustomer(QueryGBGoodsInfoPageRequest request);

    /**
     * 通过名称获取团购单位
     * @param name
     * @return
     */
    GbCustomerDO getCustomerByName(String name);


    /**
     * 通过统一信用代码获取团购单位
     * @param CreditCode
     * @return
     */
    GbCustomerDO getCustomerByCreditCode(String CreditCode);
}
