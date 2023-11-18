package com.yiling.mall.category.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.mall.category.bo.CategoryGoodsNumBO;
import com.yiling.mall.category.entity.CategoryGoodsDO;

/**
 * <p>
 * 分类商品表 Dao 接口
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Repository
public interface CategoryGoodsMapper extends BaseMapper<CategoryGoodsDO> {

    List<CategoryGoodsNumBO> countCategoryGoods(@Param("categoryIds") List<Long> categoryIds);
}
