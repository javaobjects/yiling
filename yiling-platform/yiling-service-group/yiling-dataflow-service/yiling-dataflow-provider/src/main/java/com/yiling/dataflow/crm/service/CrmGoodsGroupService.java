package com.yiling.dataflow.crm.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsGroupRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsGroupDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-16
 */
public interface CrmGoodsGroupService extends BaseService<CrmGoodsGroupDO> {
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
     * ids查询商品组
     * @param groupIds
     * @param status 0：有效 ，1：无效  不传查所有
     * @return
     */
    List<CrmGoodsGroupDTO> findByIdsAndStatus(List<Long> groupIds,Integer status);

    /**
     * 分组ids 查询商品组
     * @param groupIds
     * @return
     */
    List<CrmGoodsGroupDTO> findGroupByIds(List<Long> groupIds);

    /**
     * 分组ids 查询备份商品组
     * @param groupIds
     * @param tableSuffix
     * @return
     */
    List<CrmGoodsGroupDTO> findBakGroupByIds(List<Long> groupIds,String tableSuffix);

    /**
     * 商品code查询部门产品组关联id列表
     * @param goodsCode
     * @return
     */
    List<Long> findCrmDepartProductGroupByGoodsCode(Long goodsCode);

    /**
     * 商品code查询部门产品组关联id列表 备份
     * @param goodsCode
     * @return
     */
    List<Long> findBakCrmDepartProductByGoodsCode(Long goodsCode,String tableSuffix);

    /**
     * 商品code列表查询部门产品组关联id列表 Map
     * @param goodsCodeList
     * @return key ：goodscode  value :部门产品组关联idlist
     */
    Map<Long,List<Long>> findCrmDepartProductByGoodsCodeList(List<Long> goodsCodeList);
}
