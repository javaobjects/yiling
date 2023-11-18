package com.yiling.dataflow.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsTagRelationDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author shichen
 * @类名 CrmGoodsTagRelationMapper
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Repository
public interface CrmGoodsTagRelationMapper extends BaseMapper<CrmGoodsTagRelationDO> {

    /**
     * 查询tag关联分页
     * @return
     */
    Page<CrmGoodsTagRelationBO> queryTagRelationPage(Page<CrmGoodsTagRelationDO> page,@Param("request") QueryCrmGoodsTagRelationPageRequest request);

    /**
     * 标签ids
     * @param tagIds
     * @return
     */
    List<Map<Long, Long>> countTagGoods(@Param("tagIds")List<Long> tagIds);

    /**
     * 商品ids查询所有标签关联
     * @param goodsIds
     * @return
     */
    List<CrmGoodsTagRelationBO> findRelationByGoodsIds(@Param("goodsIds")List<Long> goodsIds);
}
