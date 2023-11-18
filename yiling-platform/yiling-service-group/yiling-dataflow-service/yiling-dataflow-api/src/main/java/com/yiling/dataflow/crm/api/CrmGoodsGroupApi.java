package com.yiling.dataflow.crm.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsGroupRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/9/20
 */
public interface CrmGoodsGroupApi {

//    List<CrmGoodsGroupDTO> getCrmGoodsGroupAll();

    /**
     * id集合查询商品组
     * @param groupIds
     * @return
     */
    List<CrmGoodsGroupDTO> findGroupByIds(List<Long> groupIds);

    /**
     * id查询商品组
     * @param groupId
     * @return
     */
    CrmGoodsGroupDTO findGroupById(Long groupId);

    /**
     * 保存商品组
     * @param request
     * @return
     */
    Long saveGroup(SaveOrUpdateCrmGoodsGroupRequest request);

    /**
     * 编辑商品组
     * @param request
     * @return
     */
    Long editGroup(SaveOrUpdateCrmGoodsGroupRequest request);

    /**
     * 分页查询商品组
     * @param request
     * @return
     */
    Page<CrmGoodsGroupDTO> queryGroupPage(QueryCrmGoodsGroupPageRequest request);

    /**
     * code查询分组id列表
     * @param goodsCode
     * @return
     */
    List<Long> findGroupByGoodsCode(Long goodsCode);

    /**
     * code列表查询分组id Map
     * @param goodsCodeList
     * @return key ：goodscode  value :分组idlist
     */
    Map<Long,List<Long>> findGroupByGoodsCodeList(List<Long> goodsCodeList);

    /**
     * groupId查询商品列表
     * @param groupId
     * @return
     */
    List<CrmGoodsGroupRelationDTO> findGoodsRelationByGroupId(Long groupId);

    /**
     * groupIds查询商品Map
     * @param groupIds
     * @return
     */
    Map<Long,List<CrmGoodsGroupRelationDTO>>  findGoodsRelationByGroupIds(List<Long> groupIds);

    /**
     * 商品code列表查询部门产品组关联id列表 Map
     * @param goodsCodeList
     * @return key ：goodscode  value :部门产品组关联idlist
     */
    Map<Long,List<Long>> findCrmDepartProductByGoodsCodeList(List<Long> goodsCodeList);

    /**
     * code查询分组id列表
     * @param goodsCode
     * @param tableSuffix
     * @return
     */
    List<Long> findBakCrmDepartProductByGoodsCode(Long goodsCode,String tableSuffix);
}
