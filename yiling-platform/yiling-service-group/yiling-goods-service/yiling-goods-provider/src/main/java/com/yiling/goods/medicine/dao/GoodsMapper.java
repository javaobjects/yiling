package com.yiling.goods.medicine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.UpdateShelfLifeRequest;
import com.yiling.goods.medicine.entity.GoodsDO;

/**
 * <p>
 * 商品表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Repository
public interface GoodsMapper extends BaseMapper<GoodsDO> {
	/**
	 * 根据搜索条件分页检索供应商商品
	 * @param page
	 * @param request
	 * @return
	 */
	Page<GoodsListItemBO> queryPageListGoods(Page<GoodsListItemBO> page, @Param("request") QueryGoodsPageListRequest request);


   List<QueryStatusCountBO> getCountByEid(@Param("eid")Long eid);

    /**
     * 更新商品有效期
     * @param request
     * @return
     */
    Integer updateShelfLife(@Param("request")UpdateShelfLifeRequest request);
}
