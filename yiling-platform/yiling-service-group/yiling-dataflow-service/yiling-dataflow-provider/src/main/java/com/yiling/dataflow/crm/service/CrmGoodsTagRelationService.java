package com.yiling.dataflow.crm.service;

import java.util.List;
import java.util.Map;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsTagRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 CrmGoodsTagRelationService
 * @描述
 * @创建时间 2023/4/13
 * @修改人 shichen
 * @修改时间 2023/4/13
 **/
public interface CrmGoodsTagRelationService extends BaseService<CrmGoodsTagRelationDO> {

    /**
     * 查询tag关联分页
     * @return
     */
    Page<CrmGoodsTagRelationBO> queryTagRelationPage(QueryCrmGoodsTagRelationPageRequest request);

    /**
     * 标签ids
     * @param tagIds
     * @return
     */
    List<Map<Long, Long>> countTagGoods(List<Long> tagIds);

    /**
     * 商品ids查询所有标签关联
     * @param goodsIds
     * @return
     */
    List<CrmGoodsTagRelationBO> findRelationByGoodsIds(List<Long> goodsIds);

    /**
     * 根据标签查询指定商品id下的标签商品关联
     * @param tagId
     * @param goodsIds
     * @return
     */
    List<CrmGoodsTagRelationDTO> getGoodsIdByTag(Long tagId, List<Long> goodsIds);

    /**
     * 标签和商品查询 标签商品关联
     * @param tagId
     * @param crmGoodsId
     * @return
     */
    CrmGoodsTagRelationDTO findRelationByTagIdAndGoodsId(Long tagId, Long crmGoodsId);


    /**
     * 查询备份商品id关联标签
     * @param goodsId
     * @return
     */
    List<CrmGoodsTagRelationDTO> getBakRelationByGoodsId(Long goodsId,String tableSuffix);
}
