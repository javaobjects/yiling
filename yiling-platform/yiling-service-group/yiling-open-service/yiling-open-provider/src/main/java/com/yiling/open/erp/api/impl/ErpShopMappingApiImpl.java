package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpShopMappingApi;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.QueryErpShopMappingPageRequest;
import com.yiling.open.erp.dto.request.SaveErpShopMappingRequest;
import com.yiling.open.erp.entity.ErpShopMappingDO;
import com.yiling.open.erp.service.ErpShopMappingService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ErpShopMappingApiImpl
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@DubboService
@Slf4j
public class ErpShopMappingApiImpl implements ErpShopMappingApi {

    @Autowired
    private ErpShopMappingService erpShopMappingService;

    @Override
    public Page<ErpShopMappingDTO> queryPage(QueryErpShopMappingPageRequest request) {
        return erpShopMappingService.queryPage(request);
    }

    @Override
    public ErpShopMappingDTO findById(Long id) {
        return PojoUtils.map(erpShopMappingService.getById(id),ErpShopMappingDTO.class);
    }

    @Override
    public ErpShopMappingDTO findByMainShopAndShop(Long mainShopEid,Long shopEid,String shopCode) {
        return erpShopMappingService.findByMainShopAndShop(mainShopEid,shopEid,shopCode);
    }

    @Override
    public ErpShopMappingDTO findByMainShopAndShopCode(Long mainShopEid, String shopCode) {
        return erpShopMappingService.findByMainShopAndShopCode(mainShopEid,shopCode);
    }

    @Override
    public Boolean updateSyncById(Long id, Integer syncStatus, Long opUserId) {
        log.info("更新同步状态 id：{}",id);
        return erpShopMappingService.updateSyncById(id,syncStatus,opUserId);
    }

    @Override
    public Boolean batchUpdateSyncByQuery(QueryErpShopMappingPageRequest request, Integer syncStatus) {
        log.info("更新同步状态 查询条件：{}", JSON.toJSONString(request));
        return erpShopMappingService.batchUpdateSyncByQuery(request,syncStatus);
    }

    @Override
    public Boolean deleteById(Long id, Long opUserId) {
        log.info("删除连锁总店对应，id：{}",id);
        ErpShopMappingDO deleteDO = new ErpShopMappingDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        return erpShopMappingService.deleteByIdWithFill(deleteDO)>0;
    }

    @Override
    public Boolean batchDeleteByQuery(QueryErpShopMappingPageRequest request) {
        log.info("删除连锁总店对应，查询条件：{}", JSON.toJSONString(request));
        return erpShopMappingService.batchDeleteByQuery(request);
    }

    @Override
    public Long saveShopMapping(SaveErpShopMappingRequest request) {
        return erpShopMappingService.saveShopMapping(request);
    }
}
