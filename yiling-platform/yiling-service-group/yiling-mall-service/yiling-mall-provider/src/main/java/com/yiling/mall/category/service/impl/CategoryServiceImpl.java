package com.yiling.mall.category.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.category.dao.CategoryMapper;
import com.yiling.mall.category.dto.request.QueryCategoryPageListRequest;
import com.yiling.mall.category.dto.request.SaveCategoryRequest;
import com.yiling.mall.category.dto.request.UpdateCategoryRequest;
import com.yiling.mall.category.entity.CategoryDO;
import com.yiling.mall.category.entity.CategoryGoodsDO;
import com.yiling.mall.category.service.CategoryGoodsService;
import com.yiling.mall.category.service.CategoryService;
import com.yiling.mall.common.request.GoodsRequest;

import cn.hutool.core.lang.Assert;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {

    @Autowired
    private CategoryGoodsService categoryGoodsService;

    @Override
    public Page<CategoryDO> pageList(QueryCategoryPageListRequest request) {
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.like(CategoryDO::getName, request.getName());
        }
        if (request.getStatus() != null && request.getStatus() != 0) {
            queryWrapper.eq(CategoryDO::getStatus, request.getStatus());
        }
        queryWrapper.orderByDesc(CategoryDO::getSort);
        return this.baseMapper.selectPage(request.getPage(), queryWrapper);
    }

    @Override
    public CategoryDO get(Long CategoryId) {
        Assert.notNull(CategoryId, "获取分类明细：categoryId为空！");
        // 此处不处理商品明细，商品明细单独分页查询接口
        return this.baseMapper.selectById(CategoryId);
    }

    @Override
    public Boolean addCategory(SaveCategoryRequest request) {
        CategoryDO categoryDO = PojoUtils.map(request, CategoryDO.class);
        categoryDO.setStatus(EnableStatusEnum.ENABLED.getCode());
        Assert.notBlank(categoryDO.getName(), "添加分类管理：分类名称为空！");
        Assert.isFalse(request.getName().length() > 6, "添加分类管理：分类名称不能多于6个字符！");
        // 默认eid为以岭ID
        if (categoryDO.getEid() == null || categoryDO.getEid() == 0) {
            categoryDO.setEid(Constants.YILING_EID);
        }
        this.baseMapper.insert(categoryDO);
        Assert.notNull(categoryDO.getId(), "添加分类管理：保存分类异常！");
        Assert.notEmpty(request.getGoodsList(), "添加分类管理：分类商品信息为空！");
        // 保存商品列表信息
        return categoryGoodsService.saveBatch(this.doCategoryGoodsDOList(categoryDO.getId(), request.getGoodsList()));
    }

    @Override
    public Boolean updateCategory(UpdateCategoryRequest request) {
        CategoryDO categoryDO = PojoUtils.map(request, CategoryDO.class);
        Assert.notNull(categoryDO.getId(), "编辑保存分类：CategoryId为空！");
        Assert.notEmpty(request.getGoodsList(), "编辑保存分类：推荐商品信息为空！");
        Assert.isFalse(request.getName().length() > 6, "编辑保存分类：分类名称不能多于6个字符！");
        this.baseMapper.updateById(categoryDO);
        // 保存商品列表信息。先删除，再保存
        categoryGoodsService.remove(new LambdaQueryWrapper<CategoryGoodsDO>().eq(CategoryGoodsDO::getCategoryId, categoryDO.getId()));
        return categoryGoodsService.saveBatch(this.doCategoryGoodsDOList(categoryDO.getId(), request.getGoodsList()));
    }

    @Override
    public List<CategoryDO> queryCategoryList(Long eid, Integer num) {
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        if (eid != null && eid != 0) {
            queryWrapper.eq(CategoryDO::getEid, eid);
        }
        queryWrapper.eq(CategoryDO::getStatus, EnableStatusEnum.ENABLED.getCode())
                .orderByDesc(CategoryDO::getSort);
        // 最多展示8栏
        if (num == null || num == 0) {
            num = 8;
        }
        queryWrapper.last("limit " + num);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 处理推荐商品明细DO
     * @param categoryId        分类ID
     * @param goodsRequestList  推荐商品列表
     * @return
     */
    private List<CategoryGoodsDO> doCategoryGoodsDOList(Long categoryId, List<GoodsRequest> goodsRequestList) {
        // 去重
        List<GoodsRequest> goodsList = goodsRequestList.stream().distinct().collect(Collectors.toList());
        List<CategoryGoodsDO> categoryGoodsDOList = new ArrayList<>();
        for (GoodsRequest goods : goodsList) {
            CategoryGoodsDO categoryGoodsDO = new CategoryGoodsDO();
            categoryGoodsDO.setCategoryId(categoryId);
            categoryGoodsDO.setGoodsId(goods.getGoodsId());
            categoryGoodsDO.setSort(goods.getSort());
            categoryGoodsDOList.add(categoryGoodsDO);
        }
        return categoryGoodsDOList;
    }
}
