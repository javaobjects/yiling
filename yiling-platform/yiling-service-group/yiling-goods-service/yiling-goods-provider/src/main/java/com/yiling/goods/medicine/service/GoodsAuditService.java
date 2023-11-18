package com.yiling.goods.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditPageRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditRecordPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.SaveSellSpecificationsRequest;
import com.yiling.goods.medicine.entity.GoodsAuditDO;

/**
 * <p>
 * 商品待审核表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface GoodsAuditService extends BaseService<GoodsAuditDO> {

    Boolean rejectGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    Boolean saveGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    Boolean notPassGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    Boolean passGoodsAudit(SaveOrUpdateGoodsAuditRequest request);

    GoodsAuditDTO getGoodsAuditByGidAndAuditStatus(Long gid);

    Page<GoodsAuditDO> queryPageListGoodsAudit(QueryGoodsAuditPageRequest request);

    Page<GoodsAuditDO> queryPageListGoodsAuditRecord(QueryGoodsAuditRecordPageRequest request);

    Boolean linkSellSpecifications(SaveSellSpecificationsRequest request);

}
