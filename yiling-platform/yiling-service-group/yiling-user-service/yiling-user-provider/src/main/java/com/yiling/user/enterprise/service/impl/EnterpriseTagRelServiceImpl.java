package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.dao.EnterpriseTagRelMapper;
import com.yiling.user.enterprise.dto.request.SaveEnterpriseTagsRequest;
import com.yiling.user.enterprise.entity.EnterpriseTagRelDO;
import com.yiling.user.enterprise.service.EnterpriseTagRelService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 企业关联的标签信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-10-14
 */
@Service
public class EnterpriseTagRelServiceImpl extends BaseServiceImpl<EnterpriseTagRelMapper, EnterpriseTagRelDO> implements EnterpriseTagRelService {

    @Override
    public List<Long> listByEid(Long eid) {
        LambdaQueryWrapper<EnterpriseTagRelDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(EnterpriseTagRelDO::getEid, eid);

        List<EnterpriseTagRelDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(EnterpriseTagRelDO::getTagId).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<Long,List<Long>> listByEidList(List<Long> eidList) {
        LambdaQueryWrapper<EnterpriseTagRelDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.in(EnterpriseTagRelDO::getEid, eidList);

        List<EnterpriseTagRelDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.newHashMap();
        }

        Map<Long,List<Long>> listMap = MapUtil.newHashMap();

        Map<Long, List<EnterpriseTagRelDO>> map = list.stream().collect(Collectors.groupingBy(EnterpriseTagRelDO::getEid));
        map.forEach((eid, enterpriseTagRelList) -> {
            listMap.put(eid,enterpriseTagRelList.stream().map(EnterpriseTagRelDO::getTagId).collect(Collectors.toList()));
        });

        return listMap;
    }

    @Override
    public boolean addEnterpriseTags(Long eid, List<Long> tagIds, Integer type, Long opUserId) {
        if (CollUtil.isEmpty(tagIds)) {
            return true;
        }

        List<EnterpriseTagRelDO> list = CollUtil.newArrayList();
        tagIds.forEach(e -> {
            EnterpriseTagRelDO entity = new EnterpriseTagRelDO();
            entity.setEid(eid);
            entity.setTagId(e);
            entity.setType(type);
            entity.setOpUserId(opUserId);
            list.add(entity);
        });

        int n = this.baseMapper.addEnterpriseTags(list);
        return n > 0;
    }

    @Override
    public boolean removeEnterpriseTags(Long eid, List<Long> tagIds, Long opUserId) {
        if (CollUtil.isEmpty(tagIds)) {
            return true;
        }

        QueryWrapper<EnterpriseTagRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseTagRelDO::getEid, eid)
                .in(EnterpriseTagRelDO::getTagId, tagIds);

        EnterpriseTagRelDO entity = new EnterpriseTagRelDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEnterpriseTags(SaveEnterpriseTagsRequest request) {
        Long eid = request.getEid();
        List<Long> tagIds = request.getTagIds();
        Integer type = request.getType();
        Long opUserId = request.getOpUserId();

        // 企业原有的标签ID列表
        List<Long> enterpriseTagIds = this.listByEid(eid);

        // 如果之前没有任何标签，则将现在设置的标签直接保存
        if (CollUtil.isEmpty(enterpriseTagIds)) {
            return this.addEnterpriseTags(eid, tagIds, type, opUserId);
        }

        // 如果之前有标签，且现在不设置任何标签，则移除原有的所有标签
        if (CollUtil.isEmpty(tagIds)) {
            return this.removeEnterpriseTags(eid, enterpriseTagIds, opUserId);
        }

        // 如果之前有标签，现在也设置了标签，则进行更新
        {
            // 移除
            List<Long> removeTagIds = enterpriseTagIds.stream().filter(e -> !tagIds.contains(e)).distinct().collect(Collectors.toList());
            this.removeEnterpriseTags(eid, removeTagIds, opUserId);

            // 新增
            List<Long> addTagIds = tagIds.stream().filter(e -> !enterpriseTagIds.contains(e)).distinct().collect(Collectors.toList());
            this.addEnterpriseTags(eid, addTagIds, type, opUserId);
        }

        return true;
    }

    @Override
    public List<Long> getEidListByTagId(Long tagsId) {
        LambdaQueryWrapper<EnterpriseTagRelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseTagRelDO::getTagId,tagsId);

        return this.list(queryWrapper).stream().map(EnterpriseTagRelDO::getEid).collect(Collectors.toList());
    }

    @Override
    public List<Long> getEidListByTagIdList(List<Long> tagsIdList) {
        LambdaQueryWrapper<EnterpriseTagRelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EnterpriseTagRelDO::getTagId,tagsIdList);

        return this.list(queryWrapper).stream().map(EnterpriseTagRelDO::getEid).distinct().collect(Collectors.toList());
    }
}
