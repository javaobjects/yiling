package com.yiling.dataflow.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.crm.dao.CrmGoodsCategoryMapper;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsCategoryDO;
import com.yiling.dataflow.crm.enums.CrmGoodsErrorCode;
import com.yiling.dataflow.crm.service.CrmGoodsCategoryService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryServiceImpl
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Slf4j
@Service
public class CrmGoodsCategoryServiceImpl extends BaseServiceImpl<CrmGoodsCategoryMapper, CrmGoodsCategoryDO> implements CrmGoodsCategoryService {
    @Override
    public Long saveOrUpdateCategory(SaveOrUpdateCrmGoodsCategoryRequest request) {
        log.info("保存商品品类参数,request:{}", request);
        if (null == request.getId() || request.getId() == 0) {
            CrmGoodsCategoryDTO categoryDTO = this.findByCodeOrName(request.getCode(), null);
            if (null != categoryDTO) {
                throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR, "品类编码:" + request.getCode() + "已存在");
            }
            categoryDTO = this.findByCodeOrName(null, request.getName());
            if (null != categoryDTO) {
                throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR, "品类名称:" + request.getName() + "已存在");
            }
            if (request.getParentId() > 0) {
                CrmGoodsCategoryDO parentDO = this.getById(request.getParentId());
                if (null == parentDO) {
                    throw new BusinessException(CrmGoodsErrorCode.EMPTY_ERROR, "上级品类不存在");
                }
                if (parentDO.getFinalStageFlag() == 1) {
                    throw new BusinessException(CrmGoodsErrorCode.DATA_ERROR, "上级分类" + parentDO.getName() + "是末级");
                }
                request.setCategoryLevel(parentDO.getCategoryLevel() + 1);
            }
            CrmGoodsCategoryDO saveDO = PojoUtils.map(request, CrmGoodsCategoryDO.class);
            this.save(saveDO);
            return saveDO.getId();
        }
        CrmGoodsCategoryDO categoryDO = this.getById(request.getId());
        if (null == categoryDO) {
            throw new BusinessException(CrmGoodsErrorCode.EMPTY_ERROR, "编辑的品类不存在");
        }
        if (request.getFinalStageFlag() == 1) {
            List<CrmGoodsCategoryDTO> childrenList = this.findByParentId(request.getId());
            if (CollectionUtil.isNotEmpty(childrenList)) {
                throw new BusinessException(CrmGoodsErrorCode.DATA_ERROR, "存在子级,无法设为末级");
            }
        } else {
            Map<Long, Long> goodsCountMap = this.getGoodsCountByCategory(ListUtil.toList(request.getId()));
            if (goodsCountMap.getOrDefault(request.getId(), 0L) > 0) {
                throw new BusinessException(CrmGoodsErrorCode.DATA_ERROR, "存在产品绑定,无法设为非末级");
            }
        }
        QueryWrapper<CrmGoodsCategoryDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsCategoryDO::getName, request.getName());
        queryWrapper.lambda().ne(CrmGoodsCategoryDO::getId, request.getId());
        queryWrapper.lambda().last("limit 1");
        CrmGoodsCategoryDO one = this.getOne(queryWrapper);
        if (null != one) {
            throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR, "品类名称:" + request.getName() + "已存在");
        }
        this.updateById(PojoUtils.map(request, CrmGoodsCategoryDO.class));
        return request.getId();
    }

    @Override
    public CrmGoodsCategoryDTO findByCodeOrName(String code, String name) {
        if (StringUtils.isBlank(code) && StringUtils.isBlank(name)) {
            return null;
        }
        QueryWrapper<CrmGoodsCategoryDO> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(code)) {
            queryWrapper.lambda().eq(CrmGoodsCategoryDO::getCode, code);
        }
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(CrmGoodsCategoryDO::getName, name);
        }
        queryWrapper.lambda().last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), CrmGoodsCategoryDTO.class);
    }

    @Override
    public List<CrmGoodsCategoryDTO> getFinalStageCategory(String category) {
        QueryWrapper<CrmGoodsCategoryDO> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(category)) {
            queryWrapper.lambda().like(CrmGoodsCategoryDO::getName, category);
        }
        queryWrapper.lambda().eq(CrmGoodsCategoryDO::getFinalStageFlag, 1);
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsCategoryDTO.class);
    }

    @Override
    public List<Integer> getAllLevel() {
        return this.baseMapper.getAllLevel();
    }

    @Override
    public List<CrmGoodsCategoryDTO> queryCategoryList(QueryCrmGoodsCategoryRequest request) {
        QueryWrapper<CrmGoodsCategoryDO> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(request.getCode())) {
            queryWrapper.lambda().like(CrmGoodsCategoryDO::getCode, request.getCode());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().like(CrmGoodsCategoryDO::getName, request.getName());
        }
        if (null != request.getCategoryLevel() && request.getCategoryLevel() > 0) {
            queryWrapper.lambda().eq(CrmGoodsCategoryDO::getCategoryLevel, request.getCategoryLevel());
        }
        if (StringUtils.isNotBlank(request.getParentName())) {
            QueryWrapper<CrmGoodsCategoryDO> parentWrapper = new QueryWrapper();
            parentWrapper.lambda().like(CrmGoodsCategoryDO::getName, request.getParentName());
            List<CrmGoodsCategoryDO> parentList = this.list(parentWrapper);
            List<Long> parentIds = parentList.stream().map(CrmGoodsCategoryDO::getId).collect(Collectors.toList());
            queryWrapper.lambda().in(CrmGoodsCategoryDO::getParentId, parentIds);
        }
        List<CrmGoodsCategoryDO> list = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, CrmGoodsCategoryDTO.class);
    }

    @Override
    public Map<Long, Long> getGoodsCountByCategory(List<Long> categoryIds) {

        List<Map<Long, Long>> listMap = this.baseMapper.getGoodsCountByCategory(categoryIds);
        if (CollectionUtil.isEmpty(listMap)) {
            return MapUtil.empty();
        }
        Map<Long, Long> map = new HashMap<>(listMap.size());
        listMap.forEach(
                item -> {
                    map.put(item.get("categoryId"), item.get("goodsCount"));
                }
        );
        return map;
    }

    @Override
    public List<CrmGoodsCategoryDTO> findByParentId(Long parentId) {
        QueryWrapper<CrmGoodsCategoryDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsCategoryDO::getParentId, parentId);
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsCategoryDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsCategoryDTO> findBakByParentId(Long parentId, String tableSuffix) {
        QueryWrapper<CrmGoodsCategoryDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsCategoryDO::getParentId, parentId);
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsCategoryDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsCategoryDTO> findBakByCategoryIds(List<Long> categoryIds, String tableSuffix) {
        if (CollectionUtil.isEmpty(categoryIds)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CrmGoodsCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmGoodsCategoryDO::getId, categoryIds);
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsCategoryDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Long findFirstCategoryByFinal(Long categoryId, String tableSuffix) {
        CrmGoodsCategoryDO crmGoodsCategoryDO = this.getById(categoryId);
        if (crmGoodsCategoryDO == null) {
            return null;
        }
        Long id = crmGoodsCategoryDO.getId();
        if (crmGoodsCategoryDO != null && crmGoodsCategoryDO.getParentId() != 0) {
            id = this.findFirstCategoryByFinal(crmGoodsCategoryDO.getParentId(), tableSuffix);
        }
        return id;
    }

    @Override
    public List<CrmGoodsCategoryDTO> findAllCategoryList() {
        return PojoUtils.map(this.list(), CrmGoodsCategoryDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsCategoryDTO> findBakAllCategoryList(String tableSuffix) {
        return PojoUtils.map(this.list(), CrmGoodsCategoryDTO.class);
    }

    @Override
    public Map<Long, CrmGoodsCategoryDTO> getFinalAndFirstMapping() {
        List<CrmGoodsCategoryDTO> allCategoryList = this.findAllCategoryList();
        return this.getFinalAndFirstMappingByAllCategory(allCategoryList);
    }

    @Override
    public Map<Long, CrmGoodsCategoryDTO> getBakFinalAndFirstMapping(String tableSuffix) {
        List<CrmGoodsCategoryDTO> allCategoryList = this.findBakAllCategoryList(tableSuffix);
        return this.getFinalAndFirstMappingByAllCategory(allCategoryList);
    }

    private Map<Long, CrmGoodsCategoryDTO> getFinalAndFirstMappingByAllCategory(List<CrmGoodsCategoryDTO> allCategory) {
        if (CollectionUtil.isEmpty(allCategory)) {
            return MapUtil.empty();
        }
        Map<Long, CrmGoodsCategoryDTO> allCategoryMap = allCategory.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, Function.identity()));
        Map<Long, CrmGoodsCategoryDTO> map = MapUtil.newHashMap();
        allCategory.forEach(category -> {
            if (category.getFinalStageFlag() == 1) {
                if (category.getCategoryLevel() == 1) {
                    map.put(category.getId(), category);
                } else {
                    CrmGoodsCategoryDTO firstCategory = this.findFirstByAllMap(category, allCategoryMap);
                    map.put(category.getId(), firstCategory);
                }
            }
        });
        return map;
    }

    private CrmGoodsCategoryDTO findFirstByAllMap(CrmGoodsCategoryDTO category, Map<Long, CrmGoodsCategoryDTO> allCategoryMap) {
        if (category.getParentId() != 0) {
            CrmGoodsCategoryDTO parent = allCategoryMap.get(category.getParentId());
            if (parent.getParentId() != 0) {
                return this.findFirstByAllMap(parent, allCategoryMap);
            } else {
                return parent;
            }
        } else {
            return category;
        }
    }
}
