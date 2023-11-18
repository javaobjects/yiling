package com.yiling.goods.medicine.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupRelationDTO;
import com.yiling.goods.medicine.dto.request.QueryRecommendGoodsGroupPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateRecommendGoodsGroupRequest;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupApi
 * @描述 推荐商品组api
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
public interface RecommendGoodsGroupApi {
    /**
     * 分页查询商品组
     * @param request
     * @return
     */
    Page<RecommendGoodsGroupDTO> queryGroupPage(QueryRecommendGoodsGroupPageRequest request);

    /**
     * id获取商品组
     * @param id
     * @return
     */
    RecommendGoodsGroupDTO getGroupById(Long id);

    /**
     * 查询商品组列表
     * @param request
     * @return
     */
    List<RecommendGoodsGroupDTO> queryGroupList(QueryRecommendGoodsGroupPageRequest request);

    /**
     * 商品id和组id查询关联商品列表
     * @param groupIds
     * @param goodsIds
     * @return
     */
    List<RecommendGoodsGroupRelationDTO> getGroupRelationByGroupIdsAndGoodsIds(List<Long> groupIds, List<Long> goodsIds);

    /**
     * 保存或者修改商品组信息
     * @param request
     * @return
     */
    Long saveOrUpdateGroup(SaveOrUpdateRecommendGoodsGroupRequest request);

    /**
     * 删除推荐组
     * @param groupId
     * @param opUserId
     * @return
     */
    int deleteGroup(Long groupId,Long opUserId);

    /**
     * 批量添加商品组商品
     * @param groupId
     * @param goodsIds
     * @return
     */
    int batchSaveGroupRelation(Long groupId, List<Long> goodsIds, Long opUserId);

    /**
     * 批量删除商品组商品
     * @param groupId
     * @param goodsIds
     * @return
     */
    int batchDeleteGroupRelation(Long groupId,List<Long> goodsIds,Long opUserId);
}
