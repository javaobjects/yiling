package com.yiling.goods.standard.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.dao.StandardGoodsTagMapper;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.CreateStandardTagRequest;
import com.yiling.goods.standard.dto.request.QueryStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.UpdateStandardTagRequest;
import com.yiling.goods.standard.entity.StandardGoodsTagDO;
import com.yiling.goods.standard.enums.StandardResultCode;
import com.yiling.goods.standard.service.StandardGoodsTagRelService;
import com.yiling.goods.standard.service.StandardGoodsTagService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author shichen
 * @类名 StandardGoodsTagServiceImpl
 * @描述
 * @创建时间 2022/10/19
 * @修改人 shichen
 * @修改时间 2022/10/19
 **/
@Service
public class StandardGoodsTagServiceImpl extends BaseServiceImpl<StandardGoodsTagMapper, StandardGoodsTagDO> implements StandardGoodsTagService {
    @Autowired
    private StandardGoodsTagRelService standardGoodsTagRelService;

    @Override
    public List<StandardGoodsTagDTO> listAll(EnableStatusEnum statusEnum) {
        LambdaQueryWrapper<StandardGoodsTagDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (statusEnum != EnableStatusEnum.ALL) {
            lambdaQueryWrapper.eq(StandardGoodsTagDO::getStatus, statusEnum.getCode());
        }

        List<StandardGoodsTagDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list,StandardGoodsTagDTO.class);
    }

    @Override
    public List<StandardGoodsTagDTO> listByStandardId(Long standardId) {
        List<Long> tagIds = standardGoodsTagRelService.listByStandardId(standardId);
        if (CollUtil.isEmpty(tagIds)) {
            return ListUtil.empty();
        }

        List<StandardGoodsTagDO> list = this.listByIds(tagIds);
        return PojoUtils.map(list,StandardGoodsTagDTO.class);
    }

    @Override
    public Map<Long, List<StandardGoodsTagDTO>> listByStandardIdList(List<Long> standardIdList) {
        Map<Long,List<Long>> map = standardGoodsTagRelService.listByStandardIdList(standardIdList);
        if (CollUtil.isEmpty(map)) {
            return MapUtil.newHashMap();
        }

        Map<Long, List<StandardGoodsTagDTO>> listMap = MapUtil.newHashMap();

        map.forEach((eid,tagIds) -> {
            List<StandardGoodsTagDO> list = this.listByIds(tagIds);
            listMap.put(eid,PojoUtils.map(list,StandardGoodsTagDTO.class));
        });

        return listMap;
    }

    @Override
    public Page<StandardGoodsTagDTO> queryTagsListPage(QueryStandardGoodsTagsRequest request) {
        LambdaQueryWrapper<StandardGoodsTagDO> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(request.getName())){
            queryWrapper.like(StandardGoodsTagDO::getName,request.getName());
        }

        return PojoUtils.map(this.page(request.getPage(),queryWrapper),StandardGoodsTagDTO.class);
    }

    @Override
    public Boolean createTags(CreateStandardTagRequest request) {
        LambdaQueryWrapper<StandardGoodsTagDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StandardGoodsTagDO::getName,request.getName());
        int count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(StandardResultCode.STANDARD_TAG_NAME_EXIST);
        }

        StandardGoodsTagDO standardGoodsTagDO = PojoUtils.map(request,StandardGoodsTagDO.class);
        standardGoodsTagDO.setStatus(EnableStatusEnum.ENABLED.getCode());

        return this.save(standardGoodsTagDO);
    }

    @Override
    public Boolean updateTags(UpdateStandardTagRequest request) {
        LambdaQueryWrapper<StandardGoodsTagDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StandardGoodsTagDO::getName,request.getName());
        StandardGoodsTagDO count = this.getOne(queryWrapper);
        if(Objects.nonNull(count) && count.getId().compareTo(request.getId()) != 0){
            throw new BusinessException(StandardResultCode.STANDARD_TAG_NAME_EXIST);
        }

        StandardGoodsTagDO standardGoodsTagDO = PojoUtils.map(request,StandardGoodsTagDO.class);

        return this.updateById(standardGoodsTagDO);
    }

    @Override
    public Boolean batchDeleteTags(List<Long> idList, Long opUserId) {
        StandardGoodsTagDO standardGoodsTagDO = new StandardGoodsTagDO();
        standardGoodsTagDO.setOpUserId(opUserId);

        LambdaQueryWrapper<StandardGoodsTagDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(StandardGoodsTagDO::getId,idList);

        return this.batchDeleteWithFill(standardGoodsTagDO,queryWrapper) > 0;
    }

    @Override
    public List<Long> getStandardIdListByTagsName(String name) {
        if(StrUtil.isEmpty(name)){
            return ListUtil.toList();
        }

        LambdaQueryWrapper<StandardGoodsTagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StandardGoodsTagDO::getName,name);

        List<StandardGoodsTagDO> tagDOList = this.list(wrapper);
        List<Long> tagIdList = tagDOList.stream().map(BaseDO::getId).collect(Collectors.toList());

        if(CollUtil.isNotEmpty(tagIdList)){
            return standardGoodsTagRelService.getStandardIdListByTagIdList(tagIdList);
        }

        return ListUtil.toList();
    }

    @Override
    public List<Long> getStandardIdListByTagsNameList(List<String> tagNameList) {
        if(CollUtil.isEmpty(tagNameList)){
            return ListUtil.toList();
        }

        LambdaQueryWrapper<StandardGoodsTagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StandardGoodsTagDO::getName, tagNameList);
        List<Long> tagIdList = this.list(wrapper).stream().map(BaseDO::getId).distinct().collect(Collectors.toList());

        if (CollUtil.isEmpty(tagIdList)) {
            return ListUtil.toList();
        }

        return standardGoodsTagRelService.getStandardIdListByTagIdList(tagIdList);
    }

    @Override
    public StandardGoodsTagDTO getTagByTagsName(String name) {
        if(StringUtils.isBlank(name)){
            return null;
        }
        LambdaQueryWrapper<StandardGoodsTagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StandardGoodsTagDO::getName, name);
        wrapper.last(" limit 1");
        StandardGoodsTagDO tagDO = this.getOne(wrapper);
        return PojoUtils.map(tagDO,StandardGoodsTagDTO.class);
    }
}
