package com.yiling.dataflow.crm.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsInfoRequest;

/**
 * @author shichen
 * @类名 CrmGoodsInfoApi
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
public interface CrmGoodsInfoApi {

    /**
     * 商品code和商品名称查询crmgoods
     * @param goodsCode
     * @param goodsName
     * @return
     */
    CrmGoodsInfoDTO findByCodeAndName(Long goodsCode,String goodsName);

    /**
     * 模糊查询crmgoods
     * @param goodsName
     * @return
     */
    List<CrmGoodsInfoDTO> findByNameAndSpec(String goodsName,String goodsSpec,Integer limit);

    /**
     * 精准查询crmgoods
     * @param goodsName 精准查询条件
     * @param goodsSpec 精准查询条件
     * @return
     */
    CrmGoodsInfoDTO getByNameAndSpec(String goodsName, String goodsSpec, Integer goodsStatus);

    /**
     * codeList查询crmgoods
     * @param goodsCodeList
     * @return
     */
    List<CrmGoodsInfoDTO> findByCodeList(List<Long> goodsCodeList);

    /**
     * 全数据查询备份表，
     * @param tableSuffix
     * @return
     */
    List<CrmGoodsInfoDTO> getBakCrmGoodsInfoAll(String tableSuffix);

    /**
     * code批量查询备份表
     * @param goodsCodeList
     * @param tableSuffix
     * @return
     */
    List<CrmGoodsInfoDTO> findBakByCodeList(List<Long> goodsCodeList, String tableSuffix);

    /**
     * 新增商品
     * @param request
     * @return
     */
    Long saveGoodsInfo(SaveOrUpdateCrmGoodsInfoRequest request);

    /**
     * 编辑商品
     * @param request
     * @return
     */
    Long editGoodsInfo(SaveOrUpdateCrmGoodsInfoRequest request);

    /**
     * 商品分页
     * @param request
     * @return
     */
    Page<CrmGoodsInfoDTO> getPage(QueryCrmGoodsInfoPageRequest request);

    /**
     * 备份商品分页
     * @param request
     * @param tableSuffix
     * @return
     */
    Page<CrmGoodsInfoDTO> getBakPage(QueryCrmGoodsInfoPageRequest request, String tableSuffix);

    /**
     * 商品分页 （弹窗页面用）
     * @param request
     * @return
     */
    Page<CrmGoodsInfoDTO> getPopupPage(QueryCrmGoodsInfoPageRequest request);

    /**
     * id 查询
     * @param id
     * @return
     */
    CrmGoodsInfoDTO findById(Long id);

    /**
     * ids 查询列表
     * @param ids
     * @return
     */
    List<CrmGoodsInfoDTO> findByIds(List<Long> ids);

    /**
     * 商品组ids 查询列表
     * @param groupIds
     * @return
     */
    Map<Long ,List<CrmGoodsInfoDTO>> findByGroupIds(List<Long> groupIds);
}
