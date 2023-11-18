package com.yiling.goods.medicine.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.bo.CustomerGoodsPriceLimitBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.BatchAddCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLimitPageListRequest;
import com.yiling.goods.medicine.entity.CustomerGoodsPriceLimitDO;

/**
 * <p>
 * 客户商品限价表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-25
 */
public interface CustomerGoodsPriceLimitService extends BaseService<CustomerGoodsPriceLimitDO> {

    /**
     * 分页
     * @param request
     * @return
     */
    Page<GoodsListItemBO> pageLimitList(QueryGoodsLimitPageListRequest request);
    /**
     * 添加商品
     * @param request
     * @return
     */
    Boolean addCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request);

    /**
     * 批量添加
     * @param request
     * @return
     */
    Boolean batchAddCustomerGoodsLimitByCustomerEid(BatchAddCustomerGoodsLimitRequest request);

    /**
     * 移除商品
     * @param request
     * @return
     */
    Boolean deleteCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request);

    /**
     * 通过cplId获取商品列表
     * @param customerEid
     * @return
     */
    List<Long> getGoodsIdsByCustomerEid(Long eid,Long customerEid);

    /**
     * 获取商品对应客户是否限销
     * @param gidList
     * @param eid
     * @param buyerEid
     * @return
     */
    List<Long> getIsGoodsPriceByGoodsIdsAndBuyerEid(List<Long> gidList, Long eid, Long buyerEid);

    /**
     * 商品ids和客户ids，获取限价商品
     * @param gidList
     * @param eid
     * @param customerEidList
     * @return
     */
    Map<Long,List<Long>> getLimitByGidsAndCustomerEidsGroupByCustomerEid(List<Long> gidList, List<Long> customerEidList, Long eid);

}
