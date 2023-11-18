package com.yiling.dataflow.crm.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsTagRequest;

/**
 * @author shichen
 * @类名 CrmGoodsTagApi
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
public interface CrmGoodsTagApi {

    /**
     * 保存tag
     * @return
     */
    Long saveOrUpdateTag(SaveOrUpdateCrmGoodsTagRequest request);

    /**
     * 删除tag
     * @return
     */
    void deleteTag(Long id,Long opUserId);

    CrmGoodsTagDTO findById(Long tagId);

    /**
     * 查询tag分页
     * @return
     */
    Page<CrmGoodsTagDTO> queryTagPage(QueryCrmGoodsTagPageRequest request);

    /**
     * 查询所有tag
     * @return
     */
    List<CrmGoodsTagDTO> getTagList(Integer type);
    /**
     * 保存tag关联
     * @return
     */
    Long saveTagRelation(Long tagId,Long crmGoodsId,Long opUserId);

    /**
     * 删除tag关联
     * @return
     */
    void deleteTagRelation(Long id,Long opUserId);

    /**
     * 商品批量保存标签
     * @param tagIds
     * @param crmGoodsId
     * @param opUserId
     */
    Boolean batchSaveTagsByGoods(List<Long> tagIds,Long crmGoodsId,Long opUserId);

    /**
     * 查询tag关联分页
     * @return
     */
    Page<CrmGoodsTagRelationBO> queryTagRelationPage(QueryCrmGoodsTagRelationPageRequest request);

    /**
     * 查询商品拥有标签
     * @param crmGoodsId
     * @return
     */
    List<CrmGoodsTagDTO> findTagByGoodsId(Long crmGoodsId);

    /**
     * 查询备份表商品拥有标签
     * @param crmGoodsId
     * @return
     */
    List<CrmGoodsTagDTO> findBakTagByGoodsId(Long crmGoodsId,String tableSuffix);

    /**
     * 批量查询商品拥有标签
     * @param crmGoodsIds
     * @return
     */
    Map<Long,List<CrmGoodsTagRelationBO>> findTagByGoodsIds(List<Long> crmGoodsIds);

    /**
     * 统计标签下商品数量
     * @param tagIds
     * @return
     */
    Map<Long,Long> countTagGoods(List<Long> tagIds);

    /**
     * 根据标签查询指定商品id下的标签商品关联
     * @param tagId
     * @param goodsIds
     * @return
     */
    List<CrmGoodsTagRelationDTO> getGoodsIdByTag(Long tagId, List<Long> goodsIds);
}
