package com.yiling.dataflow.crm.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsTagApi;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsTagRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsTagDO;
import com.yiling.dataflow.crm.service.CrmGoodsTagRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsTagService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsTagApiImpl
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@DubboService
@Slf4j
public class CrmGoodsTagApiImpl implements CrmGoodsTagApi {
    @Autowired
    private CrmGoodsTagService crmGoodsTagService;

    @Autowired
    private CrmGoodsTagRelationService crmGoodsTagRelationService;

    @Override
    public Long saveOrUpdateTag(SaveOrUpdateCrmGoodsTagRequest request) {
        return crmGoodsTagService.saveOrUpdateTag(request);
    }

    @Override
    public void deleteTag(Long id, Long opUserId) {
        crmGoodsTagService.deleteTag(id,opUserId);
    }

    @Override
    public CrmGoodsTagDTO findById(Long tagId) {
        CrmGoodsTagDO tagDO = crmGoodsTagService.getById(tagId);
        return PojoUtils.map(tagDO,CrmGoodsTagDTO.class);
    }

    @Override
    public Page<CrmGoodsTagDTO> queryTagPage(QueryCrmGoodsTagPageRequest request) {
        return crmGoodsTagService.queryTagPage(request);
    }

    @Override
    public List<CrmGoodsTagDTO> getTagList(Integer type) {
        return crmGoodsTagService.getTagList(type);
    }

    @Override
    public Long saveTagRelation(Long tagId, Long crmGoodsId, Long opUserId) {
        return crmGoodsTagService.saveTagRelation(tagId,crmGoodsId,opUserId);
    }

    @Override
    public void deleteTagRelation(Long id, Long opUserId) {
        crmGoodsTagService.deleteTagRelation(id,opUserId);
    }

    @Override
    public Boolean batchSaveTagsByGoods(List<Long> tagIds, Long crmGoodsId, Long opUserId) {
        return crmGoodsTagService.batchSaveTagsByGoods(tagIds,crmGoodsId,opUserId);
    }

    @Override
    public Page<CrmGoodsTagRelationBO> queryTagRelationPage(QueryCrmGoodsTagRelationPageRequest request) {
        return crmGoodsTagService.queryTagRelationPage(request);
    }

    @Override
    public List<CrmGoodsTagDTO> findTagByGoodsId(Long crmGoodsId) {
        return crmGoodsTagService.findTagByGoodsId(crmGoodsId);
    }

    @Override
    public List<CrmGoodsTagDTO> findBakTagByGoodsId(Long crmGoodsId, String tableSuffix) {
        return crmGoodsTagService.findBakTagByGoodsId(crmGoodsId,tableSuffix);
    }

    @Override
    public Map<Long,List<CrmGoodsTagRelationBO>> findTagByGoodsIds(List<Long> crmGoodsIds) {
        return crmGoodsTagService.findTagByGoodsIds(crmGoodsIds);
    }

    @Override
    public Map<Long, Long> countTagGoods(List<Long> tagIds) {
        return crmGoodsTagService.countTagGoods(tagIds);
    }

    @Override
    public List<CrmGoodsTagRelationDTO> getGoodsIdByTag(Long tagId, List<Long> goodsIds) {
        return crmGoodsTagRelationService.getGoodsIdByTag(tagId,goodsIds);
    }
}
