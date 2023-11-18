package com.yiling.goods.standard.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.dao.StandardGoodsCategoryMapper;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.dto.request.SaveCategoryInfoRequest;
import com.yiling.goods.standard.dto.request.UpdateCategoryNameRequest;
import com.yiling.goods.standard.entity.StandardGoodsCategoryDO;
import com.yiling.goods.standard.enums.StandardResultCode;
import com.yiling.goods.standard.service.StandardGoodsCategoryService;
import com.yiling.goods.standard.service.StandardGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 标准库商品分类表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
@Slf4j
public class StandardGoodsCategoryServiceImpl extends BaseServiceImpl<StandardGoodsCategoryMapper, StandardGoodsCategoryDO> implements StandardGoodsCategoryService {

    @Autowired
    private StandardGoodsService standardGoodsService;

    /**
     * 根据id批量获取药品类型
     *
     * @param ids
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDO> getStandardGoodsCateList(Set<Long> ids) {
        QueryWrapper<StandardGoodsCategoryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardGoodsCategoryDO::getId, ids);
        List<StandardGoodsCategoryDO> list = this.list(wrapper);
        return list;
    }

    /**
     * 获取一级分类信息
     *
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDO> getFirstCateInfo() {
        QueryWrapper<StandardGoodsCategoryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsCategoryDO::getParentId, 0);
        List<StandardGoodsCategoryDO> list = this.list(wrapper);
        return list;
    }

    /**
     * 获取所有二级分类信息
     *
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDO> getSecondCateInfo() {
        QueryWrapper<StandardGoodsCategoryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsCategoryDO::getParentId, 0);
        List<StandardGoodsCategoryDO> list = this.list(wrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            Set<Long> normalSet = list.stream().map(StandardGoodsCategoryDO::getId).collect(Collectors.toSet());
            QueryWrapper<StandardGoodsCategoryDO> wrapperTwo = new QueryWrapper<>();
            wrapperTwo.lambda().in(StandardGoodsCategoryDO::getParentId, normalSet);
            List<StandardGoodsCategoryDO> listTwo = this.list(wrapperTwo);
            return listTwo;
        }
        return list;
    }

    /**
     * 根据父id获取分类
     *
     * @param parentId
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDO> getSecondCateOne(Long parentId) {
        QueryWrapper<StandardGoodsCategoryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsCategoryDO::getParentId, parentId);
        return list(wrapper);
    }

    /**
     * 获取所有分类信息
     *
     * @return
     */
    @Override
    public List<StandardGoodsCategoryInfoAllDTO> getAllCateInfo() {
        QueryWrapper<StandardGoodsCategoryDO> wrapper = new QueryWrapper<>();
        List<StandardGoodsCategoryDO> list = this.list(wrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            List<StandardGoodsCategoryInfoAllDTO> dtoAllList = PojoUtils.map(list, StandardGoodsCategoryInfoAllDTO.class);
            List<StandardGoodsCategoryInfoAllDTO> cateTree = getCateTree(0L, dtoAllList);
            return cateTree;
        }

        return null;
    }

    /**
     * 根据id修改药品名称
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCateName(UpdateCategoryNameRequest request) {
        StandardGoodsCategoryDO standardGoodsCateDO = PojoUtils.map(request, StandardGoodsCategoryDO.class);
        StandardGoodsCategoryDO one = getById(request.getId());
        if (one == null) {
            throw new BusinessException(StandardResultCode.STANDARD_DATA_CATEGORY_UPDATE);
        }
        if (0 == one.getParentId()) {
            //修改一级分类
            standardGoodsService.updateCategoryName1ById(request.getName(), request.getId(), request.getOpUserId());
        } else {
            standardGoodsService.updateCategoryName2ById(request.getName(), request.getId(), request.getOpUserId());
        }
        standardGoodsCateDO.setUpdateTime(request.getOpTime());
        standardGoodsCateDO.setUpdateUser(request.getOpUserId());
        boolean result = this.updateById(standardGoodsCateDO);
        return result;

    }

    /**
     * 修改分类id
     *
     * @param parentId
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCateParentId(Long parentId, Long id, Long opUserId) {
        StandardGoodsCategoryDO one = getById(id);
        StandardGoodsCategoryDO parent = getById(parentId);
        if (one == null || parent == null) {
            throw new BusinessException(StandardResultCode.STANDARD_DATA_CATEGORY_UPDATE);
        }
        one.setParentId(parentId);
        one.setUpdateUser(opUserId);
        standardGoodsService.updateCategoryId(parentId, id, parent.getName(), opUserId);
        return updateById(one);
    }

    /**
     * 新增分类
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveCateInfo(SaveCategoryInfoRequest request) {
        StandardGoodsCategoryDO standardGoodsCateDO = PojoUtils.map(request, StandardGoodsCategoryDO.class);
        standardGoodsCateDO.setSort(1);
        boolean save = this.save(standardGoodsCateDO);
        return save;
    }

    /**
     * 根据类型获取
     *
     * @param type 1-一级分类 2-二级分类
     * @param name 分类名称
     * @return
     */
    @Override
    public StandardGoodsCategoryDO getCategoryByName(Integer type, String name) {
        QueryWrapper<StandardGoodsCategoryDO> wrapper = new QueryWrapper<>();
        if (1 == type) {
            //一级分类
            wrapper.lambda().eq(StandardGoodsCategoryDO::getParentId, 0)
                    .eq(StandardGoodsCategoryDO::getName, name)
                    .last(" limit 1 ");
            StandardGoodsCategoryDO one = getOne(wrapper);
            return one;
        } else if (2 == type) {
            //二级分类
            wrapper.lambda().gt(StandardGoodsCategoryDO::getParentId, 0)
                    .eq(StandardGoodsCategoryDO::getName, name)
                    .last(" limit 1 ");
            StandardGoodsCategoryDO one = getOne(wrapper);
            return one;
        }
        return null;
    }




    private List<StandardGoodsCategoryInfoAllDTO> getCateTree(Long parentId, List<StandardGoodsCategoryInfoAllDTO> list) {
        List<StandardGoodsCategoryInfoAllDTO> listDto = new ArrayList<>();
        for (StandardGoodsCategoryInfoAllDTO one : list) {
            if (parentId.equals(one.getParentId())) {
                List<StandardGoodsCategoryInfoAllDTO> children = getCateTree(one.getId(), list);
                if (CollectionUtils.isNotEmpty(children)) {
                    one.setChildren(children);
                }
                listDto.add(one);
            }
        }
        return listDto;
    }
}
