package com.yiling.goods.restriction.api;

import java.util.List;

import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionCustomerDTO;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.DeleteRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.QueryRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.SavePurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.SaveRestrictionCustomerRequest;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionApi
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
public interface GoodsPurchaseRestrictionApi {

    /**
     * 查询商品有效限购信息
     * @param request
     * @return
     */
    List<GoodsPurchaseRestrictionDTO> queryValidPurchaseRestriction(QueryGoodsPurchaseRestrictionRequest request);

    /**
     * 获取有效商品限购
     * @param request
     * @return
     */
    GoodsPurchaseRestrictionDTO getValidPurchaseRestriction(QueryGoodsPurchaseRestrictionRequest request);

    /**
     * goodsIds查询限购
     * @param goodsIds
     * @return
     */
    List<GoodsPurchaseRestrictionDTO> getPurchaseRestrictionByGoodsIds(List<Long> goodsIds);

    /**
     * 保存商品限购规则
     * @param request
     * @return
     */
    Boolean saveGoodsPurchaseRestriction(SavePurchaseRestrictionRequest request);

    /**
     * 商品id查询商品限购规则
     * @param goodsId
     * @return
     */
    GoodsPurchaseRestrictionDTO getRestrictionByGoodsId(Long goodsId);

    /**
     * 限购id查询限购客户eid列表
     */
    List<Long> getCustomerEidByGoodsId(Long goodsId);

    /**
     * 批量保存限购客户
     * @param request
     * @return
     */
    Boolean batchSaveRestrictionCustomer(SaveRestrictionCustomerRequest request);

    /**
     * 保存单条限购客户
     * @param request
     * @return
     */
    Boolean saveRestrictionCustomer(SaveRestrictionCustomerRequest request);


    /**
     * 查询限购客户
     * @param request
     * @return
     */
    List<GoodsPurchaseRestrictionCustomerDTO> queryRestrictionCustomer(QueryRestrictionCustomerRequest request);


    /**
     * 批量删除限购客户
     * @param request
     * @return
     */
    int batchDeleteRestrictionCustomer(DeleteRestrictionCustomerRequest request);


    /**
     * 删除限购客户
     * @param request
     * @return
     */
    int deleteRestrictionCustomer(DeleteRestrictionCustomerRequest request);
}
