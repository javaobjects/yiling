package com.yiling.cms.content.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.yiling.cms.content.enums.LineEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dao.CategoryMapper;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.request.AddCategoryRequest;
import com.yiling.cms.content.dto.request.QueryCategoryPageRequest;
import com.yiling.cms.content.dto.request.UpdateCategoryRequest;
import com.yiling.cms.content.entity.CategoryDO;
import com.yiling.cms.content.service.CategoryDisplayLineService;
import com.yiling.cms.content.service.CategoryService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

/**
 * <p>
 * 栏目 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {

    @Autowired
    private CategoryDisplayLineService categoryDisplayLineService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(AddCategoryRequest request) {
        // 保存分类
        CategoryDO categoryDO = new CategoryDO();
        PojoUtils.map(request, categoryDO);
        categoryDO.setStatus(0);
        this.save(categoryDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        CategoryDO categoryDO = new CategoryDO();
        PojoUtils.map(updateCategoryRequest, categoryDO);
        this.updateById(categoryDO);
        //禁用启用不变动业务线
        if (Objects.nonNull(updateCategoryRequest.getStatus())) {
            return;
        }
        // LambdaQueryWrapper<CategoryDisplayLineDO> wrapper = Wrappers.lambdaQuery();
        // wrapper.eq(CategoryDisplayLineDO::getCategoryId, updateCategoryRequest.getId());
        // List<Object> list = categoryDisplayLineService.listObjs(wrapper);
        // List<Long> lineList = PojoUtils.map(list, Long.class);
        // categoryDisplayLineService.removeByIds(lineList);
        // List<CategoryDisplayLineDO> categoryDisplayLineDOList = Lists.newArrayList();
        // updateCategoryRequest.getDisplayLines().forEach(lineId -> {
        //     CategoryDisplayLineDO categoryDisplayLineDO = new CategoryDisplayLineDO();
        //     categoryDisplayLineDO.setCategoryId(categoryDO.getId());
        //     categoryDisplayLineDO.setLineId(lineId);
        //     categoryDisplayLineDO.setCreateUser(updateCategoryRequest.getOpUserId());
        //     categoryDisplayLineDOList.add(categoryDisplayLineDO);
        // });
        // categoryDisplayLineService.saveBatch(categoryDisplayLineDOList);
    }

    @Override
    public List<CategoryDTO> queryCategoryList(Integer status) {
        LambdaQueryWrapper<CategoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(status), CategoryDO::getStatus, status);
        wrapper.in(CategoryDO::getLineId, Arrays.asList(LineEnum.HMC.getCode(), LineEnum.IH_DOC.getCode(), LineEnum.SA.getCode(), LineEnum.B2B.getCode()));
        wrapper.orderByDesc(CategoryDO::getId);
        List<CategoryDO> categoryDOList = this.list(wrapper);
        return PojoUtils.map(categoryDOList, CategoryDTO.class);
        // if (CollUtil.isEmpty(categoryDOList)) {
        //     return Lists.newArrayList();
        // }
        // List<Long> ids = categoryDOList.stream().map(CategoryDO::getId).collect(Collectors.toList());
        // LambdaQueryWrapper<CategoryDisplayLineDO> lineWrapper = Wrappers.lambdaQuery();
        // lineWrapper.in(CategoryDisplayLineDO::getCategoryId, ids);
        // List<CategoryDisplayLineDO> displayLineDOS = categoryDisplayLineService.list(lineWrapper);
        // Map<Long, List<CategoryDisplayLineDO>> lineDOMap = displayLineDOS.stream().collect(Collectors.groupingBy(CategoryDisplayLineDO::getCategoryId));
        // List<CategoryDTO> categoryDTOS = PojoUtils.map(categoryDOList, CategoryDTO.class);
        //
        // categoryDTOS.forEach(categoryDTO -> {
        //     List<CategoryDisplayLineDO> lineDOS = lineDOMap.get(categoryDTO.getId());
        //     List<Long> list = lineDOS.stream().map(CategoryDisplayLineDO::getLineId).collect(Collectors.toList());
        //     categoryDTO.setDisplayLines(list);
        // });
        // return categoryDTOS;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        CategoryDO categoryDO = this.getById(id);
        CategoryDTO categoryDTO = PojoUtils.map(categoryDO, CategoryDTO.class);
        // LambdaQueryWrapper<CategoryDisplayLineDO> lineWrapper = Wrappers.lambdaQuery();
        // lineWrapper.eq(CategoryDisplayLineDO::getCategoryId, id);
        // List<CategoryDisplayLineDO> displayLineDOS = categoryDisplayLineService.list(lineWrapper);
        // List<Long> list = displayLineDOS.stream().map(CategoryDisplayLineDO::getLineId).collect(Collectors.toList());
        // // categoryDTO.setDisplayLines(list);
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> queryAppCategoryList(Long lineId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getLineId, lineId);
        wrapper.eq(CategoryDO::getStatus, 1);
        wrapper.orderByDesc(CategoryDO::getCategorySort, CategoryDO::getUpdateTime);
        return PojoUtils.map(this.list(wrapper), CategoryDTO.class);
        // LambdaQueryWrapper<CategoryDisplayLineDO> lineWrapper = Wrappers.lambdaQuery();
        // lineWrapper.eq(CategoryDisplayLineDO::getLineId, lineId);
        // List<CategoryDisplayLineDO> list = categoryDisplayLineService.list(lineWrapper);
        // if (CollUtil.isEmpty(list)) {
        //     return Lists.newArrayList();
        // }
        // LambdaQueryWrapper<CategoryDO> wrapper = Wrappers.lambdaQuery();
        // wrapper.in(CategoryDO::getId, list.stream().map(CategoryDisplayLineDO::getCategoryId).collect(Collectors.toList()));
        // wrapper.eq(CategoryDO::getStatus, 1);
        // List<CategoryDO> categoryDOList = this.list(wrapper);
        // List<CategoryDTO> categoryDTOS = PojoUtils.map(categoryDOList, CategoryDTO.class);
        // return categoryDTOS;
    }

    @Override
    public List<CategoryDTO> getCategoryByLineIdAndModuleId(Long lineId, Long moduleId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getLineId, lineId);
        wrapper.eq(CategoryDO::getModuleId, moduleId);
        wrapper.eq(CategoryDO::getStatus, 1);
        wrapper.orderByDesc(CategoryDO::getCategorySort, CategoryDO::getUpdateTime);
        return PojoUtils.map(this.list(wrapper), CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getCategorySaAndB2B(Long lineId, Long moduleId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getLineId, lineId);
        wrapper.eq(CategoryDO::getModuleId, moduleId);
        wrapper.eq(CategoryDO::getStatus, 1);
        wrapper.orderByDesc(CategoryDO::getCategorySort);
        wrapper.orderByAsc(CategoryDO::getCreateTime);
        return PojoUtils.map(this.list(wrapper), CategoryDTO.class);
    }

    @Override
    public Page<CategoryDTO> queryCategoryHospitalList(QueryCategoryPageRequest request) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(request.getLineId()), CategoryDO::getLineId, request.getLineId());
        wrapper.eq(Objects.nonNull(request.getModuleId()), CategoryDO::getModuleId, request.getModuleId());
        wrapper.orderByDesc(CategoryDO::getCategorySort, CategoryDO::getUpdateTime);
        return PojoUtils.map(this.page(request.getPage(), wrapper), CategoryDTO.class);
    }
}
