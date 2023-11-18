package com.yiling.user.procrelation.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.QueryRelationGoodsPageRequest;
import com.yiling.user.procrelation.dto.request.QuerySpecByBuyerPageRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationGoodsDO;

/**
 * <p>
 * pop采购关系的可采商品 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
public interface ProcurementRelationGoodsService extends BaseService<ProcurementRelationGoodsDO> {

    /**
     * 根据采购关系查询采购关系的商品
     *
     * @param relationId
     * @return
     */
    List<ProcurementRelationGoodsDO> queryGoodsByRelationId(Long relationId);

    /**
     * 根据采购关系查询采购关系的商品
     *
     * @param relationIds
     * @param goodsName
     * @return
     */
    List<ProcurementRelationGoodsDO> queryGoodsByRelationIdAndGoodsName(List<Long> relationIds,String goodsName);

    /**
     * 为采购关系添加商品
     *
     * @param requestList
     * @return
     */
    Boolean addGoodsForProcRelation(List<AddGoodsForProcRelationRequest> requestList);

    /**
     * 根据采购关系id查询采购关系下的商品
     *
     * @param request
     * @return
     */
    Page<ProcurementRelationGoodsDO> queryProcRelationGoodsPage(QueryRelationGoodsPageRequest request);

    /**
     * 根据购买人分页查询有效以岭商品id
     *
     * @param request
     * @return
     */
    Page<Long> queryGoodsPageByBuyer(QuerySpecByBuyerPageRequest request);

    /**
     * 根据采购关系id清空商品
     *
     * @param relationId
     * @param opUser
     * @return
     */
    Boolean emptyGoodsByRelationId(Long relationId,Long opUser);

}
