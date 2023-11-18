package com.yiling.goods.medicine.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditPageRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditRecordPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.SaveSellSpecificationsRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/5/21
 */
public interface GoodsAuditApi {

    /**
     * 新增商品待审核信息
     *
     * @param request
     * @return
     */
    Boolean saveGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    /**
     * 待审核商品变成驳回状态
     * @param request
     * @return
     */
    Boolean notPassGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    /**
     * 商品驳回
     * @param request
     * @return
     */
    Boolean rejectGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    /**
     * 获取在待审核的驳回记录
     * @param gid
     * @return
     */
    GoodsAuditDTO getGoodsAuditByGidAndAuditStatus(Long gid);

    /**
     * 待审核商品列表信息
     *
     * @param request
     * @return
     */
    Page<GoodsAuditDTO> queryPageListGoodsAudit(QueryGoodsAuditPageRequest request);

    /**
     * 通过商品ID获取审核记录
     *
     * @param request
     * @return
     */
    Page<GoodsAuditDTO> queryPageListGoodsAuditRecord(QueryGoodsAuditRecordPageRequest request);

    /**
     * 获取待审核详细信息
     *
     * @param id
     * @return
     */
    GoodsAuditDTO getById(Long id);

    /**
     * 人工选择匹配到对应的规格ID
     *
     * @param request
     * @return
     */
    Boolean linkSellSpecifications(SaveSellSpecificationsRequest request);

}
