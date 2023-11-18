package com.yiling.dataflow.crm.service;

import java.util.List;
import java.util.Map;

import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.request.SaveCrmGoodsGroupRelationRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsGroupRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 CrmGoodsGroupRelationService
 * @描述
 * @创建时间 2023/3/7
 * @修改人 shichen
 * @修改时间 2023/3/7
 **/
public interface CrmGoodsGroupRelationService extends BaseService<CrmGoodsGroupRelationDO> {

    /**
     * code查询分组id列表
     * @param goodsCode
     * @return
     */
    List<Long> findGroupByGoodsCode(Long goodsCode);

    /**
     * code查询分组id列表
     * @param goodsCode
     * @return
     */
    List<Long> findBakGroupByGoodsCode(Long goodsCode,String tableSuffix);

    /**
     * code列表查询分组id Map
     * @param goodsCodeList
     * @return key ：goodscode  value :分组idlist
     */
    Map<Long,List<Long>> findGroupByGoodsCodeList(List<Long> goodsCodeList);

    /**
     * groupId查询商品code列表
     * @param groupId
     * @return
     */
    List<CrmGoodsGroupRelationDTO> findGoodsRelationByGroupId(Long groupId);

    /**
     * groupId查询商品列表
     * @param groupIds
     * @return
     */
    Map<Long,List<CrmGoodsGroupRelationDTO>>  findGoodsRelationByGroupIds(List<Long> groupIds);

    /**
     * 批量添加产品组关联
     * @param requestList
     * @return
     */
    Boolean batchSaveGroupRelation(List<SaveCrmGoodsGroupRelationRequest> requestList,Long groupId,Long opUserId);

}
