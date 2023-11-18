package com.yiling.pricing.goods.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsLinePriceRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerDO;

/**
 * <p>
 * 客户定价 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
public interface GoodsPriceCustomerService extends BaseService<GoodsPriceCustomerDO> {

    /**
     * 查询客户定价分页列表
     * @param request
     * @return
     */
    Page<GoodsPriceCustomerDO> pageList(QueryGoodsPriceCustomerPageListRequest request);

    /**
     * 保存或更新客户定价
     * @param request
     * @return  客户定价ID
     */
    Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerRequest request);

    /**
     * 统计商品客户定价价格信息
     * @param goodsIds  商品ID列表
     * @return
     */
    List<CountGoodsPriceBO> countGoodsCustomerPrice(List<Long> goodsIds, Integer goodsLine);

    /**
     * 批量获取商品客户定价信息
     *
     * @param request
     * @return key:商品ID
     */
    Map<Long, GoodsPriceCustomerDO> listGoodsCustomerPriceInfos(QueryGoodsLinePriceRequest request);

    /**
     * 获取客户商品定价
     * @param request
     * @return
     */
    GoodsPriceCustomerDO get(QueryGoodsPriceCustomerRequest request);

    /**
     * 根据商品id查询商品客户定价信息
     * @param goodsId  商品ID
     * @return
     */
    List<GoodsPriceCustomerDTO> getByGoodsId(Long goodsId);
}
