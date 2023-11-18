package com.yiling.mall.category.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.mall.category.bo.CategoryGoodsNumBO;
import com.yiling.mall.category.dao.CategoryGoodsMapper;
import com.yiling.mall.category.dto.request.QueryCategoryGoodsPageListRequest;
import com.yiling.mall.category.entity.CategoryGoodsDO;
import com.yiling.mall.category.service.CategoryGoodsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 分类商品表 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Service
public class CategoryGoodsServiceImpl extends BaseServiceImpl<CategoryGoodsMapper, CategoryGoodsDO> implements CategoryGoodsService {
    @Override
    public Page<CategoryGoodsDO> pageList(QueryCategoryGoodsPageListRequest request) {
        LambdaQueryWrapper<CategoryGoodsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryGoodsDO::getCategoryId, request.getCategoryId());
        queryWrapper.orderByDesc(CategoryGoodsDO::getSort);

        return this.baseMapper.selectPage(request.getPage(), queryWrapper);
    }

    @Override
    public Map<Long, Long> countCategoryGoods(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return MapUtil.empty();
        }
        List<CategoryGoodsNumBO> list = this.baseMapper.countCategoryGoods(categoryIds);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }
        return list.stream().collect(Collectors.toMap(CategoryGoodsNumBO::getCategoryId, CategoryGoodsNumBO::getGoodsNum , (k1,k2) -> k2));
    }

    @Override
    public List<CategoryGoodsDO> queryCategoryGoodsByCategoryId(Long categoryId) {
        LambdaQueryWrapper<CategoryGoodsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryGoodsDO::getCategoryId, categoryId);
        queryWrapper.orderByDesc(CategoryGoodsDO::getSort);

        return this.baseMapper.selectList(queryWrapper);
    }
}
