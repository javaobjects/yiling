package com.yiling.goods.standard.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardGoodsTagRelMapper;
import com.yiling.goods.standard.dto.request.SaveStandardGoodsTagsRequest;
import com.yiling.goods.standard.entity.StandardGoodsTagRelDO;
import com.yiling.goods.standard.service.StandardGoodsTagRelService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * @author shichen
 * @类名 StandardGoodsTagRelServiceImpl
 * @描述
 * @创建时间 2022/10/19
 * @修改人 shichen
 * @修改时间 2022/10/19
 **/
@Service
public class StandardGoodsTagRelServiceImpl extends BaseServiceImpl<StandardGoodsTagRelMapper, StandardGoodsTagRelDO> implements StandardGoodsTagRelService {

    @Override
    public List<Long> listByStandardId(Long StandardId) {
        LambdaQueryWrapper<StandardGoodsTagRelDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StandardGoodsTagRelDO::getStandardId, StandardId);

        List<StandardGoodsTagRelDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(StandardGoodsTagRelDO::getTagId).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Long>> listByStandardIdList(List<Long> standardIdList) {
        LambdaQueryWrapper<StandardGoodsTagRelDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.in(StandardGoodsTagRelDO::getStandardId, standardIdList);

        List<StandardGoodsTagRelDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.newHashMap();
        }
        Map<Long,List<Long>> listMap = list.stream().collect(Collectors.groupingBy(StandardGoodsTagRelDO::getStandardId,Collectors.mapping(StandardGoodsTagRelDO::getTagId,Collectors.toList())));
        return listMap;
    }

    @Override
    public List<Long> getStandardIdListByTagId(Long tagId) {
        LambdaQueryWrapper<StandardGoodsTagRelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StandardGoodsTagRelDO::getTagId,tagId);
        return this.list(queryWrapper).stream().map(StandardGoodsTagRelDO::getStandardId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getStandardIdListByTagIdList(List<Long> tagIdList) {
        LambdaQueryWrapper<StandardGoodsTagRelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(StandardGoodsTagRelDO::getTagId,tagIdList);
        return this.list(queryWrapper).stream().map(StandardGoodsTagRelDO::getStandardId).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStandardGoodsTags(SaveStandardGoodsTagsRequest request) {
        Long standardId = request.getStandardId();
        List<Long> tagIds = request.getTagIds();
        Integer type = request.getType();
        Long opUserId = request.getOpUserId();

        // 标准库原有的标签ID列表
        List<Long> standardGoodsTagIds = this.listByStandardId(standardId);

        // 如果之前没有任何标签，则将现在设置的标签直接保存
        if (CollUtil.isEmpty(standardGoodsTagIds)) {
            return this.addStandardGoodsTags(standardId, tagIds, type, opUserId);
        }

        // 如果之前有标签，且现在不设置任何标签，则移除原有的所有标签
        if (CollUtil.isEmpty(tagIds)) {
            return this.removeStandardGoodsTags(standardId, standardGoodsTagIds, opUserId);
        }

        // 如果之前有标签，现在也设置了标签，则进行更新
        {
            // 移除
            List<Long> removeTagIds = standardGoodsTagIds.stream().filter(e -> !tagIds.contains(e)).distinct().collect(Collectors.toList());
            this.removeStandardGoodsTags(standardId, removeTagIds, opUserId);

            // 新增
            List<Long> addTagIds = tagIds.stream().filter(e -> !standardGoodsTagIds.contains(e)).distinct().collect(Collectors.toList());
            this.addStandardGoodsTags(standardId, addTagIds, type, opUserId);
        }
        return true;
    }

    @Override
    public boolean removeStandardGoodsTags(Long standardId, List<Long> tagIds, Long opUserId) {
        if (CollUtil.isEmpty(tagIds)) {
            return true;
        }

        QueryWrapper<StandardGoodsTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StandardGoodsTagRelDO::getStandardId, standardId)
                .in(StandardGoodsTagRelDO::getTagId, tagIds);

        StandardGoodsTagRelDO entity = new StandardGoodsTagRelDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    @Override
    public boolean addStandardGoodsTags(Long standardId, List<Long> tagIds, Integer type, Long opUserId) {
        if (CollUtil.isEmpty(tagIds)) {
            return true;
        }
        List<StandardGoodsTagRelDO> list = tagIds.stream().map(tagId->{
            StandardGoodsTagRelDO entity = new StandardGoodsTagRelDO();
            entity.setStandardId(standardId);
            entity.setTagId(tagId);
            entity.setType(type);
            entity.setOpUserId(opUserId);
            return entity;
        }).collect(Collectors.toList());
        return this.saveBatch(list);
    }
}
