package com.yiling.open.erp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.QueryErpShopMappingPageRequest;
import com.yiling.open.erp.dto.request.SaveErpShopMappingRequest;
import com.yiling.open.erp.entity.ErpShopMappingDO;

/**
 * @author shichen
 * @类名 ErpShopMappingService
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
public interface ErpShopMappingService extends BaseService<ErpShopMappingDO> {

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<ErpShopMappingDTO> queryPage(QueryErpShopMappingPageRequest request);

    /**
     * 通过总店和门店查找对应关系
     * @param mainShopEid
     * @param shopEid
     * @param shopCode
     * @return
     */
    ErpShopMappingDTO findByMainShopAndShop(Long mainShopEid,Long shopEid,String shopCode);

    /**
     * 通过总店和门店code查找对应关系
     * @param mainShopEid
     * @param shopCode
     * @return
     */
    ErpShopMappingDTO findByMainShopAndShopCode(Long mainShopEid,String shopCode);

    /**
     * id修改同步状态
     * @param id
     * @param syncStatus
     * @return
     */
    Boolean updateSyncById(Long id, Integer syncStatus,Long opUserId);

    /**
     * 查询条件批量修改同步状态
     * @param request
     * @param syncStatus
     * @return
     */
    Boolean batchUpdateSyncByQuery(QueryErpShopMappingPageRequest request, Integer syncStatus);

    /**
     * 查询条件批量删除
     * @param request
     * @return
     */
    Boolean batchDeleteByQuery(QueryErpShopMappingPageRequest request);

    /**
     * 保存连锁门店对应
     * @param request
     * @return
     */
    Long saveShopMapping(SaveErpShopMappingRequest request);
}
