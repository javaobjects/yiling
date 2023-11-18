package com.yiling.goods.restriction.service;

import java.util.List;

import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionCustomerDTO;
import com.yiling.goods.restriction.dto.request.DeleteRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.QueryRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.SaveRestrictionCustomerRequest;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionCustomerService
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
public interface GoodsPurchaseRestrictionCustomerService {

    /**
     * 保存单条限购客户
     * @param request
     * @return
     */
    Boolean saveRestrictionCustomer(SaveRestrictionCustomerRequest request);

    /**
     * 批量保存限购客户
     * @param request
     * @return
     */
    Boolean batchSaveRestrictionCustomer(SaveRestrictionCustomerRequest request);

    /**
     * 查询客户在商品限购客户列表
     * @param customerEid
     * @param goodsIds
     * @return
     */
    List<GoodsPurchaseRestrictionCustomerDTO> getCustomerByCustomerEidAndGoodsIds(Long customerEid,List<Long> goodsIds);

    /**
     * 商品id查询限购客户列表
     * @param goodsId
     */
    List<Long> getCustomerEidByGoodsId(Long goodsId);

    /**
     * 查询限购客户
     * @param request
     * @return
     */
    List<GoodsPurchaseRestrictionCustomerDTO> getCustomerByGoodsIdAndCustomerEids(QueryRestrictionCustomerRequest request);

    /**
     * 批量删除限购客户
     * @param request
     * @return
     */
    int batchDeleteByCustomerEids(DeleteRestrictionCustomerRequest request);


    /**
     * 批量删除限购客户
     * @param request
     * @return
     */
    int deleteCustomer(DeleteRestrictionCustomerRequest request);
}
