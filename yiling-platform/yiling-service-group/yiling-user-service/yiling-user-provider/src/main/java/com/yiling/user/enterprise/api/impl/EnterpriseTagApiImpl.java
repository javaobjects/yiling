package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.CreateTagsRequest;
import com.yiling.user.enterprise.dto.request.QueryTagsRequest;
import com.yiling.user.enterprise.dto.request.SaveEnterpriseTagsRequest;
import com.yiling.user.enterprise.dto.request.UpdateTagsRequest;
import com.yiling.user.enterprise.entity.EnterpriseTagDO;
import com.yiling.user.enterprise.service.EnterpriseTagRelService;
import com.yiling.user.enterprise.service.EnterpriseTagService;

/**
 * 企业标签 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/10/14
 */
@DubboService
public class EnterpriseTagApiImpl implements EnterpriseTagApi {

    @Autowired
    private EnterpriseTagService enterpriseTagService;
    @Autowired
    private EnterpriseTagRelService enterpriseTagRelService;

    @Override
    public List<EnterpriseTagDTO> listAll(EnableStatusEnum statusEnum) {
        Assert.notNull(statusEnum, "参数statusEnum不能为空");
        List<EnterpriseTagDO> list = enterpriseTagService.listAll(statusEnum);
        return PojoUtils.map(list, EnterpriseTagDTO.class);
    }

    @Override
    public List<EnterpriseTagDTO> listByEid(Long eid) {
        Assert.notNull(eid, "参数eid不能为空");
        List<EnterpriseTagDO> list = enterpriseTagService.listByEid(eid);
        return PojoUtils.map(list, EnterpriseTagDTO.class);
    }

    @Override
    public Map<Long, List<EnterpriseTagDTO>> listByEidList(List<Long> eidList) {
        Assert.notEmpty(eidList, "参数eidList不能为空");
        return enterpriseTagService.listByEidList(eidList);
    }

    @Override
    public List<Long> getEidListByTagId(Long tagsId) {
        return enterpriseTagRelService.getEidListByTagId(tagsId);
    }

    @Override
    public List<Long> getEidListByTagIdList(List<Long> tagsIdList) {
        return enterpriseTagRelService.getEidListByTagIdList(tagsIdList);
    }

    @Override
    public boolean saveEnterpriseTags(SaveEnterpriseTagsRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return enterpriseTagRelService.saveEnterpriseTags(request);
    }

    @Override
    public Boolean createTags(CreateTagsRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return enterpriseTagService.createTags(request);
    }

    @Override
    public Boolean updateTags(UpdateTagsRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return enterpriseTagService.updateTags(request);
    }

    @Override
    public Boolean batchDeleteTags(List<Long> tagsIdList, Long currentUserId) {
        Assert.notNull(tagsIdList, "参数tagsIdList不能为空");
        return enterpriseTagService.batchDeleteTags(tagsIdList, currentUserId);
    }

    @Override
    public Page<EnterpriseTagDTO> queryTagsListPage(QueryTagsRequest request) {

        return enterpriseTagService.queryTagsListPage(request);
    }

    @Override
    public List<Long> getEidListByTagsName(String name, boolean fuzzy) {
        return enterpriseTagService.getEidListByTagsName(name, fuzzy);
    }

    @Override
    public List<Long> getEidListByTagsNameList(List<String> tagNameList) {
        return enterpriseTagService.getEidListByTagsNameList(tagNameList);
    }
}
