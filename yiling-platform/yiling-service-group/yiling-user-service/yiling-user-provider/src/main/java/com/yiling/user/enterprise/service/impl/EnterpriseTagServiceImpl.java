package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.EnterpriseTagMapper;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.CreateTagsRequest;
import com.yiling.user.enterprise.dto.request.QueryTagsRequest;
import com.yiling.user.enterprise.dto.request.UpdateTagsRequest;
import com.yiling.user.enterprise.entity.EnterpriseTagDO;
import com.yiling.user.enterprise.service.EnterpriseTagRelService;
import com.yiling.user.enterprise.service.EnterpriseTagService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业标签信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-10-14
 */
@Service
public class EnterpriseTagServiceImpl extends BaseServiceImpl<EnterpriseTagMapper, EnterpriseTagDO> implements EnterpriseTagService {

    @Autowired
    private EnterpriseTagRelService enterpriseTagRelService;

    @Override
    public List<EnterpriseTagDO> listAll(EnableStatusEnum statusEnum) {
        LambdaQueryWrapper<EnterpriseTagDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (statusEnum != EnableStatusEnum.ALL) {
            lambdaQueryWrapper.eq(EnterpriseTagDO::getStatus, statusEnum.getCode());
        }

        List<EnterpriseTagDO> list = this.list(lambdaQueryWrapper);
        return CollUtil.isNotEmpty(list) ? list : ListUtil.empty();
    }

    @Override
    public List<EnterpriseTagDO> listByEid(Long eid) {
        List<Long> tagIds = enterpriseTagRelService.listByEid(eid);
        if (CollUtil.isEmpty(tagIds)) {
            return ListUtil.empty();
        }

        List<EnterpriseTagDO> list = this.listByIds(tagIds);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public Boolean createTags(CreateTagsRequest request) {
        LambdaQueryWrapper<EnterpriseTagDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseTagDO::getName,request.getName());
        int count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(UserErrorCode.ENTERPRISE_TAG_NAME_EXIST);
        }

        EnterpriseTagDO enterpriseTagDO = PojoUtils.map(request,EnterpriseTagDO.class);
        enterpriseTagDO.setStatus(EnableStatusEnum.ENABLED.getCode());

        return this.save(enterpriseTagDO);
    }

    @Override
    public Boolean updateTags(UpdateTagsRequest request) {
        LambdaQueryWrapper<EnterpriseTagDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseTagDO::getName,request.getName());
        EnterpriseTagDO count = this.getOne(queryWrapper);
        if(Objects.nonNull(count) && count.getId().compareTo(request.getId()) != 0){
            throw new BusinessException(UserErrorCode.ENTERPRISE_TAG_NAME_EXIST);
        }

        EnterpriseTagDO enterpriseTagDO = PojoUtils.map(request,EnterpriseTagDO.class);

        return this.updateById(enterpriseTagDO);

    }

    @Override
    public Boolean batchDeleteTags(List<Long> idList , Long opUserId) {
        EnterpriseTagDO enterpriseTagDO = new EnterpriseTagDO();
        enterpriseTagDO.setOpUserId(opUserId);

        LambdaQueryWrapper<EnterpriseTagDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EnterpriseTagDO::getId,idList);

       return this.batchDeleteWithFill(enterpriseTagDO,queryWrapper) > 0;

    }

    @Override
    public Page<EnterpriseTagDTO> queryTagsListPage(QueryTagsRequest request) {
        LambdaQueryWrapper<EnterpriseTagDO> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(request.getName())){
            queryWrapper.like(EnterpriseTagDO::getName,request.getName());
        }

        return PojoUtils.map(this.page(request.getPage(),queryWrapper),EnterpriseTagDTO.class);
    }

    @Override
    public Map<Long, List<EnterpriseTagDTO>> listByEidList(List<Long> eidList) {
        Map<Long,List<Long>> map = enterpriseTagRelService.listByEidList(eidList);
        if (CollUtil.isEmpty(map)) {
            return MapUtil.newHashMap();
        }

        Map<Long, List<EnterpriseTagDTO>> listMap = MapUtil.newHashMap();

        map.forEach((eid,tagIds) -> {
            List<EnterpriseTagDO> list = this.listByIds(tagIds);
            listMap.put(eid,PojoUtils.map(list,EnterpriseTagDTO.class));
        });

        return listMap;
    }

    @Override
    public List<Long> getEidListByTagsName(String name, boolean fuzzy) {
        if(StrUtil.isEmpty(name)){
            return ListUtil.toList();
        }

        LambdaQueryWrapper<EnterpriseTagDO> wrapper = new LambdaQueryWrapper<>();
        if(fuzzy){
            wrapper.like(EnterpriseTagDO::getName,name);
        }else{
            wrapper.eq(EnterpriseTagDO::getName,name);
        }

        List<EnterpriseTagDO> tagDOList = this.list(wrapper);
        List<Long> tagIdList = tagDOList.stream().map(BaseDO::getId).collect(Collectors.toList());

        if(CollUtil.isNotEmpty(tagIdList)){
            return enterpriseTagRelService.getEidListByTagIdList(tagIdList);
        }

        return ListUtil.toList();
    }

    @Override
    public List<Long> getEidListByTagsNameList(List<String> tagNameList) {
        if(CollUtil.isEmpty(tagNameList)){
            return ListUtil.toList();
        }

        LambdaQueryWrapper<EnterpriseTagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseTagDO::getName, tagNameList);
        List<Long> tagIdList = this.list(wrapper).stream().map(BaseDO::getId).distinct().collect(Collectors.toList());

        if (CollUtil.isEmpty(tagIdList)) {
            return ListUtil.toList();
        }

        return enterpriseTagRelService.getEidListByTagIdList(tagIdList);
    }

}
