package com.yiling.goods.medicine.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.entity.RecommendGoodsGroupRelationDO;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupRelationMapper
 * @描述
 * @创建时间 2023/1/6
 * @修改人 shichen
 * @修改时间 2023/1/6
 **/
@Repository
public interface RecommendGoodsGroupRelationMapper extends BaseMapper<RecommendGoodsGroupRelationDO> {
    /**
     * 批量添加商品组关联商品
     * @param groupId
     * @param goodsIds
     * @param opUserId
     * @return
     */
    int batchSaveGroupRelation(@Param("groupId") Long groupId, @Param("goodsIds")List<Long> goodsIds, @Param("opUserId")Long opUserId);
}
