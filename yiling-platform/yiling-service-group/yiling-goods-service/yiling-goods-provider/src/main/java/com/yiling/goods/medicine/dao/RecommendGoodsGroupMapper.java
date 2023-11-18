package com.yiling.goods.medicine.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupDTO;
import com.yiling.goods.medicine.dto.request.QueryRecommendGoodsGroupPageRequest;
import com.yiling.goods.medicine.entity.RecommendGoodsGroupDO;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupMapper
 * @描述
 * @创建时间 2023/1/6
 * @修改人 shichen
 * @修改时间 2023/1/6
 **/
@Repository
public interface RecommendGoodsGroupMapper extends BaseMapper<RecommendGoodsGroupDO> {

    /**
     * 分页查询推荐商品组
     * @param page
     * @param request
     * @return
     */
    Page<RecommendGoodsGroupDTO> queryGroupPage(Page<RecommendGoodsGroupDTO> page,@Param("request") QueryRecommendGoodsGroupPageRequest request);
}
