package com.yiling.goods.ylprice.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.ylprice.entity.GoodsYilingPriceDO;

/**
 * @author: houjie.sun
 * @date: 2022/8/8
 */
@Repository
public interface GoodsYilingPriceMapper extends BaseMapper<GoodsYilingPriceDO> {

    List<GoodsYilingPriceDO> listBySpecificationIdList( @Param("specificationIdList") List<Long> specificationIdList);

    List<GoodsYilingPriceDO> getPriceBySpecificationIdList(@Param("specificationIdList") List<Long> specificationIdList, @Param("paramId") Long paramId, @Param("date") Date date);
}
