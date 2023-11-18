package com.yiling.pricing.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerGroupDO;

/**
 * <p>
 * 客户分组定价 Dao 接口
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Repository
public interface GoodsPriceCustomerGroupMapper extends BaseMapper<GoodsPriceCustomerGroupDO> {

    List<CountGoodsPriceBO> countGoodsCustomerGroupPrice(@Param("goodsIds") List<Long> goodsId, @Param("goodsLine") Integer goodsLine);

}
