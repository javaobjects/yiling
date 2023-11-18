package com.yiling.goods.restriction.service;

import java.util.List;

import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.SavePurchaseRestrictionRequest;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionService
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
public interface GoodsPurchaseRestrictionService {

    /**
     * 商品id查询商品限购规则
     * @param goodsId
     * @return
     */
    GoodsPurchaseRestrictionDTO getRestrictionByGoodsId(Long goodsId);

    /**
     * 保存商品限购规则
     * @param request
     * @return
     */
    Boolean saveGoodsPurchaseRestriction(SavePurchaseRestrictionRequest request);


    /**
     * 批量获取有效商品限购
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


}
