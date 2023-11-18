package com.yiling.open.erp.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dao.ErpShopMappingMapper;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.QueryErpShopMappingPageRequest;
import com.yiling.open.erp.dto.request.SaveErpShopMappingRequest;
import com.yiling.open.erp.entity.ErpShopMappingDO;
import com.yiling.open.erp.service.ErpShopMappingService;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @author shichen
 * @类名 ErpShopMappingServiceImpl
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Service
public class ErpShopMappingServiceImpl extends BaseServiceImpl<ErpShopMappingMapper, ErpShopMappingDO> implements ErpShopMappingService {

    @Override
    public Page<ErpShopMappingDTO> queryPage(QueryErpShopMappingPageRequest request) {
        QueryWrapper<ErpShopMappingDO> queryWrapper = new QueryWrapper();
        if(null!=request.getMainShopEid() && request.getMainShopEid()>0){
            queryWrapper.lambda().eq(ErpShopMappingDO::getMainShopEid,request.getMainShopEid());
        }
        if(null!=request.getShopEid() && request.getShopEid()>0){
            queryWrapper.lambda().eq(ErpShopMappingDO::getShopEid,request.getShopEid());
        }
        Page<ErpShopMappingDO> page = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(page, ErpShopMappingDTO.class);
    }

    @Override
    public ErpShopMappingDTO findByMainShopAndShop(Long mainShopEid,Long shopEid,String shopCode) {
        QueryWrapper<ErpShopMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ErpShopMappingDO::getMainShopEid,mainShopEid)
                .and(wrapper->wrapper.eq(ErpShopMappingDO::getShopEid,shopEid)
                        .or().eq(ErpShopMappingDO::getShopCode,shopCode))
                .last("limit 1");
        ErpShopMappingDO mappingDO = this.getOne(queryWrapper);
        return PojoUtils.map(mappingDO,ErpShopMappingDTO.class);
    }

    @Override
    public ErpShopMappingDTO findByMainShopAndShopCode(Long mainShopEid, String shopCode) {
        QueryWrapper<ErpShopMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ErpShopMappingDO::getMainShopEid,mainShopEid)
                .eq(ErpShopMappingDO::getShopCode,shopCode)
                .last("limit 1");
        ErpShopMappingDO mappingDO = this.getOne(queryWrapper);
        return PojoUtils.map(mappingDO,ErpShopMappingDTO.class);
    }

    @Override
    public Boolean updateSyncById(Long id, Integer syncStatus, Long opUserId) {
        ErpShopMappingDO updateDO = new ErpShopMappingDO();
        updateDO.setId(id);
        updateDO.setOpUserId(opUserId);
        updateDO.setSyncStatus(syncStatus);
        return this.updateById(updateDO);
    }

    @Override
    public Boolean batchUpdateSyncByQuery(QueryErpShopMappingPageRequest request, Integer syncStatus) {
        List<ErpShopMappingDO> doList = this.findByMainShopEidAndShopEid(request.getMainShopEid(),request.getShopEid());
        if(CollectionUtil.isNotEmpty(doList)){
            doList.forEach(mappingDO->{
                mappingDO.setOpUserId(request.getOpUserId());
                mappingDO.setSyncStatus(syncStatus);
                mappingDO.setOpTime(request.getOpTime());
            });
            return this.updateBatchById(doList);
        }
        return true;
    }

    @Override
    public Boolean batchDeleteByQuery(QueryErpShopMappingPageRequest request) {
        QueryWrapper<ErpShopMappingDO> queryWrapper = new QueryWrapper();
        if(null == request.getMainShopEid() || request.getMainShopEid()==0){
            if (null == request.getShopEid()|| request.getShopEid()==0 ) {
                throw new BusinessException(ResultCode.FAILED,"总部企业id和门店企业id不能都为空");
            }
        }else {
            queryWrapper.lambda().eq(ErpShopMappingDO::getMainShopEid,request.getMainShopEid());
        }
        if(null!=request.getShopEid() && request.getShopEid()>0){
            queryWrapper.lambda().eq(ErpShopMappingDO::getShopEid,request.getShopEid());
        }
        ErpShopMappingDO mappingDO = new ErpShopMappingDO();
        mappingDO.setOpUserId(request.getOpUserId());
        mappingDO.setOpTime(request.getOpTime());
        return this.batchDeleteWithFill(mappingDO,queryWrapper)>0;
    }

    @Override
    public Long saveShopMapping(SaveErpShopMappingRequest request) {
        if(null == request.getMainShopEid() || null == request.getShopEid() || StringUtils.isBlank(request.getShopCode())){
            throw new BusinessException(ResultCode.FAILED,"总部企业id或门店企业id或门店编码为空");
        }
        ErpShopMappingDTO mappingDTO = this.findByMainShopAndShop(request.getMainShopEid(),request.getShopEid(),request.getShopCode());
        if(null!=mappingDTO){
            throw new BusinessException(ResultCode.FAILED,"该总店下门店企业id或门店编码已经存在");
        }
        ErpShopMappingDO saveDO = PojoUtils.map(request, ErpShopMappingDO.class);
        this.save(saveDO);
        return saveDO.getId();
    }


    private List<ErpShopMappingDO> findByMainShopEidAndShopEid(Long mainShopEid,Long shopEid){
        QueryWrapper<ErpShopMappingDO> queryWrapper = new QueryWrapper();
        if(null == mainShopEid || mainShopEid==0){
            if (null == shopEid|| shopEid==0 ) {
                throw new BusinessException(ResultCode.FAILED,"总部企业id和门店企业id不能都为空");
            }
        }else {
            queryWrapper.lambda().eq(ErpShopMappingDO::getMainShopEid,mainShopEid);
        }
        if(null!=shopEid && shopEid>0){
            queryWrapper.lambda().eq(ErpShopMappingDO::getShopEid,shopEid);
        }
        return this.list(queryWrapper);
    }
}
