package com.yiling.basic.dict.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.dict.bo.request.SaveDictDataRequest;
import com.yiling.basic.dict.bo.request.UpdateDictDataRequest;
import com.yiling.basic.dict.dao.DictDataMapper;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.service.DictDataService;
import com.yiling.basic.location.enums.BasicErrorCode;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 字典内容表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
@CacheConfig(cacheNames = "system:dictData")
public class DictDataServiceImpl extends BaseServiceImpl<DictDataMapper, DictDataDO> implements DictDataService {

    @Override
    @Cacheable
    public List<DictDataDO> getEnabledList() {
        QueryWrapper<DictDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictDataDO::getStatus, 1);
        queryWrapper.lambda().orderByAsc(DictDataDO::getSort);
        List<DictDataDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    /**
     * 根据typeId获取字典信息
     *
     * @param typeId
     * @return
     */
    @Override
    @Cacheable(key="#typeId+'+getEnabledByTypeIdList'")
    public List<DictDataDO> getEnabledByTypeIdList(Long typeId) {
        QueryWrapper<DictDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictDataDO::getTypeId,typeId);
        queryWrapper.lambda().orderByAsc(DictDataDO::getSort);
        return list(queryWrapper);
    }

    /**
     * 根据id修改字典内容信息
     *
     * @param request
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateDictDataById(UpdateDictDataRequest request) {
        QueryWrapper<DictDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictDataDO::getTypeId,request.getTypeId())
                .eq(DictDataDO::getValue,request.getValue());
        DictDataDO one = getOne(queryWrapper);
        if(one != null && !request.getId().equals(one.getId())){
            throw new BusinessException(BasicErrorCode.DICT_DATA_REPEAT);
        }
        return updateById(PojoUtils.map(request,DictDataDO.class));
    }

    /**
     * 保存字典内容
     *
     * @param request
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean saveDictData(SaveDictDataRequest request) {
        QueryWrapper<DictDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictDataDO::getTypeId,request.getTypeId())
                .eq(DictDataDO::getValue,request.getValue());
        DictDataDO one = getOne(queryWrapper);
        if(one != null){
            throw new BusinessException(BasicErrorCode.DICT_DATA_REPEAT);
        }
        return save(PojoUtils.map(request,DictDataDO.class));
    }

    /**
     * 停用字典内容
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateStopDictData(Long id, Long opUserId) {
        DictDataDO one = new DictDataDO();
        one.setId(id);
        one.setStatus(2);
        one.setOpUserId(opUserId);
        return updateById(one);
    }

    /**
     * 停用字典内容根据类型id
     *
     * @param type
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateStopDictDataByTypeId(Long type, Long opUserId) {
        QueryWrapper<DictDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictDataDO::getTypeId,type)
                .eq(DictDataDO::getStatus, 1);
        List<DictDataDO> list = list(queryWrapper);
        if(CollectionUtils.isNotEmpty(list)){
            for(DictDataDO one : list){
                one.setStatus(2);
                one.setOpUserId(opUserId);
            }
        }
        return updateBatchById(list);
    }

    /**
     * 启用字典内容根据id
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(allEntries = true)
    public Boolean enabledDictData(Long id, Long opUserId) {
        DictDataDO one = new DictDataDO();
        one.setId(id);
        one.setStatus(1);
        one.setOpUserId(opUserId);
        return updateById(one);
    }

    /**
     * 删除字典数据
     * @param idList
     * @return
     */
    @Override
    public Boolean deleteData(List<Long> idList, Long opUserId) {
        DictDataDO entity = new DictDataDO();
        entity.setOpUserId(opUserId);
        return batchDeleteWithFill(entity, new LambdaQueryWrapper<DictDataDO>().in(BaseDO::getId,idList)) > 0;
    }
}
