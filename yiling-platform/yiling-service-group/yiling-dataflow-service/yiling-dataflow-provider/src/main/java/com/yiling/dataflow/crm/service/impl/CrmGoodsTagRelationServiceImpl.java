package com.yiling.dataflow.crm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dao.CrmGoodsTagRelationMapper;
import com.yiling.dataflow.crm.dto.CrmGoodsTagRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsTagRelationDO;
import com.yiling.dataflow.crm.service.CrmGoodsTagRelationService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author shichen
 * @类名 CrmGoodsTagRelationServiceImpl
 * @描述
 * @创建时间 2023/4/13
 * @修改人 shichen
 * @修改时间 2023/4/13
 **/
@Service
public class CrmGoodsTagRelationServiceImpl extends BaseServiceImpl<CrmGoodsTagRelationMapper, CrmGoodsTagRelationDO> implements CrmGoodsTagRelationService {
    @Override
    public Page<CrmGoodsTagRelationBO> queryTagRelationPage(QueryCrmGoodsTagRelationPageRequest request) {
        return this.baseMapper.queryTagRelationPage(request.getPage(),request);
    }

    @Override
    public List<Map<Long, Long>> countTagGoods(List<Long> tagIds) {
        if(CollectionUtil.isEmpty(tagIds)){
            ListUtil.empty();
        }
        return this.baseMapper.countTagGoods(tagIds);
    }

    @Override
    public List<CrmGoodsTagRelationBO> findRelationByGoodsIds(List<Long> goodsIds) {
        if(CollectionUtil.isEmpty(goodsIds)){
            ListUtil.empty();
        }
        return this.baseMapper.findRelationByGoodsIds(goodsIds);
    }

    @Override
    public List<CrmGoodsTagRelationDTO> getGoodsIdByTag(Long tagId, List<Long> goodsIds) {
        QueryWrapper<CrmGoodsTagRelationDO> relationWrapper = new QueryWrapper();
        relationWrapper.lambda().eq(CrmGoodsTagRelationDO::getTagId,tagId);
        if(CollectionUtil.isNotEmpty(goodsIds)){
            relationWrapper.lambda().in(CrmGoodsTagRelationDO::getCrmGoodsId,goodsIds);
        }
        List<CrmGoodsTagRelationDO> list = this.list(relationWrapper);
        return PojoUtils.map(list,CrmGoodsTagRelationDTO.class);
    }

    @Override
    public CrmGoodsTagRelationDTO findRelationByTagIdAndGoodsId(Long tagId, Long crmGoodsId) {
        QueryWrapper<CrmGoodsTagRelationDO> relationWrapper = new QueryWrapper();
        relationWrapper.lambda().eq(CrmGoodsTagRelationDO::getTagId,tagId);
        relationWrapper.lambda().eq(CrmGoodsTagRelationDO::getCrmGoodsId,crmGoodsId);
        relationWrapper.lambda().last("limit 1");
        return PojoUtils.map(this.getOne(relationWrapper),CrmGoodsTagRelationDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsTagRelationDTO> getBakRelationByGoodsId(Long goodsId, String tableSuffix) {
        QueryWrapper<CrmGoodsTagRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsTagRelationDO::getCrmGoodsId,goodsId);
        return PojoUtils.map(this.list(queryWrapper),CrmGoodsTagRelationDTO.class);
    }
}
