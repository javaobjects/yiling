package com.yiling.basic.dict.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.bo.request.QueryDictTypeRequest;
import com.yiling.basic.dict.bo.request.SaveDictTypeRequest;
import com.yiling.basic.dict.bo.request.UpdateDictTypeRequest;
import com.yiling.basic.dict.dao.DictTypeMapper;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.entity.DictTypeDO;
import com.yiling.basic.dict.service.DictDataService;
import com.yiling.basic.dict.service.DictTypeService;
import com.yiling.basic.location.enums.BasicErrorCode;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
@CacheConfig(cacheNames = "system:dictType")
public class DictTypeServiceImpl extends BaseServiceImpl<DictTypeMapper, DictTypeDO> implements DictTypeService {

    @Autowired
    private DictDataService dictDataService;

    @Override
    @Cacheable
    public List<DictTypeDO> getEnabledList() {
        QueryWrapper<DictTypeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictTypeDO::getStatus, 1);
        List<DictTypeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    /**
     * 获取type分页数据
     *
     * @param request
     * @return
     */
    @Override
    public Page<DictTypeDO> getEnabledDicTypePage(QueryDictTypeRequest request) {
        QueryWrapper<DictTypeDO> queryWrapper = new QueryWrapper<>();

        if(StrUtil.isNotEmpty(request.getName())){
            queryWrapper.lambda().like(DictTypeDO::getName,request.getName()).or().like(DictTypeDO::getDescription,request.getName());
        }

        return this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
    }

    /**
     * 修改字典类型数据
     *
     * @param request
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateDictTypeById(UpdateDictTypeRequest request) {
        QueryWrapper<DictTypeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictTypeDO::getName, request.getName());
        DictTypeDO one = getOne(queryWrapper);
        if (one != null && !one.getId().equals(request.getId())) {
            throw new BusinessException(BasicErrorCode.DICT_TYPE_REPEAT);
        }
        DictTypeDO map = PojoUtils.map(request, DictTypeDO.class);
        return this.updateById(map);
    }

    /**
     * 新增字典类型
     *
     * @param request
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean saveDictType(SaveDictTypeRequest request) {
        QueryWrapper<DictTypeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictTypeDO::getName, request.getName());
        DictTypeDO one = getOne(queryWrapper);
        if (one != null) {
            throw new BusinessException(BasicErrorCode.DICT_TYPE_REPEAT);
        }

        return this.save(PojoUtils.map(request, DictTypeDO.class));
    }

    /**
     * @param id
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateStopDictType(Long id, Long opUserId) {
        DictTypeDO one = getById(id);
        if(one!= null){
            one.setStatus(2);
            one.setOpUserId(opUserId);
            //dictDataService.updateStopDictDataByTypeId(one.getId());
            return updateById(one);
        }
        return true;
    }

    /**
     * 启用字典类型
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean enabledDictType(Long id, Long opUserId) {
        DictTypeDO one = new DictTypeDO();
        one.setStatus(1);
        one.setId(id);
        one.setOpUserId(opUserId);
        return updateById(one);
    }

    @Override
    public Boolean deleteType(List<Long> idList, Long opUserId) {
        DictTypeDO entity = new DictTypeDO();
        entity.setOpUserId(opUserId);
        return batchDeleteWithFill(entity, new LambdaQueryWrapper<DictTypeDO>().in(BaseDO::getId,idList)) > 0;
    }

    /**
     * 根据类型名称获取字段类型
     *
     * @param name
     * @return
     */
    @Override
    public List<DictDataDO> mapByName(String name) {
        QueryWrapper<DictTypeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictTypeDO :: getName,name)
                .last("limit 1");
        DictTypeDO one = getOne(queryWrapper);
        QueryWrapper<DictDataDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DictDataDO :: getTypeId,one.getId());
        List<DictDataDO> list = dictDataService.list(wrapper);

        return list;
    }

}
