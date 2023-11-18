package com.yiling.pricing.goods.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerGroupPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;

/**
 * <p>
 * 客户分组定价 api
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
public interface GoodsPriceCustomerGroupApi {

    /**
     * 获取客户分组定价分页列表
     * @param request
     * @return
     */
    Page<GoodsPriceCustomerGroupDTO> pageList(QueryGoodsPriceCustomerGroupPageListRequest request);

    /**
     * 保存或更新客户分组定价
     * @param request
     * @return
     */
    Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerGroupRequest request);

    /**
     * 移除客户分组定价
     * @param goodsPriceCustomerGroupId 客户分组定价ID
     * @return
     */
    Boolean removeById(Long goodsPriceCustomerGroupId);

    /**
     * 获取客户分组定价明细
     * @param goodsPriceCustomerGroupId 客户分组定价ID
     * @return
     */
    GoodsPriceCustomerDTO getById(Long goodsPriceCustomerGroupId);

    /**
     * 获取客户分组定价信息
     * @param queryGoodsPriceGroupRequest 客户分组定价
     * @return
     */
    GoodsPriceCustomerGroupDTO get(QueryGoodsPriceGroupRequest queryGoodsPriceGroupRequest);

    /**
     * 统计商品客户分组定价价格信息
     * @param goodsIds  商品ID列表
     * @return
     */
    List<CountGoodsPriceBO> countGoodsCustomerGroupPrice(List<Long> goodsIds, Integer goodsLine);

    /**
     * 根据商品id查询商品客户分组定价信息
     * @param goodsId  商品ID
     * @return
     */
    List<GoodsPriceCustomerGroupDTO> getByGoodsId(Long goodsId);
}
