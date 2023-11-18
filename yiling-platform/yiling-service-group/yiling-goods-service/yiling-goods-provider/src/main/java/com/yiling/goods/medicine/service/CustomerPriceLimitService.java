package com.yiling.goods.medicine.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.medicine.dto.request.UpdateCustomerLimitRequest;
import com.yiling.goods.medicine.entity.CustomerPriceLimitDO;

/**
 * <p>
 * 客户限价表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-25
 */
public interface CustomerPriceLimitService extends BaseService<CustomerPriceLimitDO> {

    /**
     * 获取限价客户标识
     *
     * @param request
     * @return
     */
    List<CustomerPriceLimitDTO> getCustomerLimitFlagByEidAndCustomerEid(QueryLimitFlagRequest request);

    /**
     * 修改限价客户标识
     *
     * @param request
     * @return
     */
    Boolean updateCustomerLimitByEidAndCustomerEid(UpdateCustomerLimitRequest request);

    /**
     * 获取商业公司推荐标识
     * @param eid
     * @return
     */
    Map<Long, Integer> getRecommendationFlagByCustomerEids(List<Long> customerEids,Long eid);

    /**
     * @param request
     * @return
     */
    Long addCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request);

    /**
     * @param eid
     * @param customerEid
     */
    CustomerPriceLimitDTO getCustomerPriceLimitByCustomerEidAndEid(Long eid, Long customerEid);

    CustomerPriceLimitDTO getCustomerPriceByCustomerEidAndEid(Long eid, Long customerEid);
}
