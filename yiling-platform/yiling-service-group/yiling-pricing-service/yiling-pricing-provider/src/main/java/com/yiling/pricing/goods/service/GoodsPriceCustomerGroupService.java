package com.yiling.pricing.goods.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsLinePriceRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerGroupPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerGroupDO;

/**
 * <p>
 * 客户分组定价 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
public interface GoodsPriceCustomerGroupService extends BaseService<GoodsPriceCustomerGroupDO> {

    /**
     * 查询客户分组定价分页列表
     * @param request
     * @return
     */
    Page<GoodsPriceCustomerGroupDO> pageList(QueryGoodsPriceCustomerGroupPageListRequest request);

    /**
     * 保存或更新客户分组定价
     * @param request
     * @return 客户分组定价ID
     */
    Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerGroupRequest request);

    /**
     * 获取客户分组定价
     * @param request
     * @return
     */
    GoodsPriceCustomerGroupDO get(QueryGoodsPriceGroupRequest request);

    /**
     * 统计商品客户分组定价价格信息
     * @param goodsIds  商品ID列表
     * @return
     */
    List<CountGoodsPriceBO> countGoodsCustomerGroupPrice(List<Long> goodsIds, Integer goodsLine);

    /**
     * 批量获取商品客户分组定价信息
     *
     * @param request
     * @return key:商品ID
     */
    Map<Long, GoodsPriceCustomerGroupDO> listGoodsCustomerGroupPriceInfos(QueryGoodsLinePriceRequest request);

    /**
     * 根据商品id查询商品客户分组定价信息
     * @param goodsId  商品ID
     * @return
     */
    List<GoodsPriceCustomerGroupDTO> getByGoodsId(Long goodsId);
}
