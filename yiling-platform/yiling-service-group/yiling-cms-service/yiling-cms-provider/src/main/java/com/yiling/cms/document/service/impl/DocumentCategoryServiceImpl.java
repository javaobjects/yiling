package com.yiling.cms.document.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.yiling.cms.document.dao.DocumentCategoryMapper;
import com.yiling.cms.document.dto.DocumentCategoryDTO;
import com.yiling.cms.document.dto.request.UpdateDocumentCategoryRequest;
import com.yiling.cms.document.entity.DocumentCategoryDO;
import com.yiling.cms.document.service.DocumentCategoryService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 文献栏目 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
@Service
public class DocumentCategoryServiceImpl extends BaseServiceImpl<DocumentCategoryMapper, DocumentCategoryDO> implements DocumentCategoryService {

    @Override
    public void updateCategory(UpdateDocumentCategoryRequest updateCategoryRequest) {
        LambdaQueryWrapper<DocumentCategoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(DocumentCategoryDO::getId,updateCategoryRequest.getId());
        wrapper.eq(DocumentCategoryDO::getCategoryName,updateCategoryRequest.getCategoryName()).last("limit 1");
        DocumentCategoryDO one = this.getOne(wrapper);
        if(Objects.nonNull(one)){
            throw new BusinessException(ResultCode.FAILED,"此栏目已存在");
        }
        DocumentCategoryDO categoryDO = new DocumentCategoryDO();
        PojoUtils.map(updateCategoryRequest, categoryDO);
        this.updateById(categoryDO);
    }

    @Override
    public List<DocumentCategoryDTO> queryCategoryList(Integer status) {
        LambdaQueryWrapper<DocumentCategoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(status),DocumentCategoryDO::getStatus,status);
        wrapper.orderByDesc(DocumentCategoryDO::getId);
        List<DocumentCategoryDO> categoryDOList = this.list(wrapper);
        if (CollUtil.isEmpty(categoryDOList)) {
            return Lists.newArrayList();
        }
        List<DocumentCategoryDTO> result = PojoUtils.map(categoryDOList,DocumentCategoryDTO.class);
        return result;
    }
}
