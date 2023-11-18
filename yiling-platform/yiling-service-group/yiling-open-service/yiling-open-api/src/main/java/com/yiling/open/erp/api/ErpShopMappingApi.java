package com.yiling.open.erp.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.QueryErpShopMappingPageRequest;
import com.yiling.open.erp.dto.request.SaveErpShopMappingRequest;

/**
 * @author shichen
 * @类名 ErpShopMappingApi
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
public interface ErpShopMappingApi {

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<ErpShopMappingDTO> queryPage(QueryErpShopMappingPageRequest request);

    ErpShopMappingDTO findById(Long id);

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
     * 修改同步状态
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
     * id 删除数据
     * @param id
     * @return
     */
    Boolean deleteById(Long id,Long opUserId);

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
