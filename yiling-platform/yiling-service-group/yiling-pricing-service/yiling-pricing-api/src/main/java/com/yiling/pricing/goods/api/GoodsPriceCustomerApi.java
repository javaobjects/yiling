package com.yiling.pricing.goods.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;

/**
 * <p>
 * 客户定价 api
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
public interface GoodsPriceCustomerApi {

    /**
     * 获取客户定价分页列表
     * @param request
     * @return
     */
    Page<GoodsPriceCustomerDTO> pageList(QueryGoodsPriceCustomerPageListRequest request);

    /**
     * 保存或更新客户定价
     * @param request
     * @return
     */
    Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerRequest request);

    /**
     * 移除客户定价
     * @param goodsPriceCustomerId 客户定价ID
     * @return
     */
    Boolean removeById(Long goodsPriceCustomerId);

    /**
     * 获取客户定价明细
     * @param goodsPriceCustomerId 客户定价ID
     * @return
     */
    GoodsPriceCustomerDTO getById(Long goodsPriceCustomerId);

    /**
     * 统计商品客户定价价格信息
     * @param goodsIds  商品ID列表
     * @return
     */
    List<CountGoodsPriceBO> countGoodsCustomerPrice(List<Long> goodsIds, Integer goodsLine);

    /**
     * 获取商品客户定价
     * @param request
     * @return
     */
    GoodsPriceCustomerDTO get(QueryGoodsPriceCustomerRequest request);

    /**
     * 根据商品id查询商品客户定价信息
     * @param goodsId  商品ID
     * @return
     */
    List<GoodsPriceCustomerDTO> getByGoodsId(Long goodsId);
}
