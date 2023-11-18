package com.yiling.goods.medicine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.bo.CustomerGoodsPriceLimitBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsLimitPageListRequest;
import com.yiling.goods.medicine.entity.CustomerGoodsPriceLimitDO;

/**
 * <p>
 * 客户商品限价表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-25
 */
@Repository
public interface CustomerGoodsPriceLimitMapper extends BaseMapper<CustomerGoodsPriceLimitDO> {

    /**
     * 商品限价分页
     * @param page
     * @param request
     * @return
     */
    Page<GoodsListItemBO> pageLimitList(Page<GoodsListItemBO> page, @Param("request") QueryGoodsLimitPageListRequest request);

    /**
     * 商品限价信息
     * @param gidList
     * @param customerEidList
     * @param eid
     * @return
     */
    List<CustomerGoodsPriceLimitBO> getLimitByGoodsIdsAndBuyerEids(@Param("gidList")List<Long> gidList, @Param("customerEidList")List<Long> customerEidList, @Param("eid")Long eid);
}
