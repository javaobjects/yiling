package com.yiling.goods.medicine.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsAuditApi;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditPageRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditRecordPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.SaveSellSpecificationsRequest;
import com.yiling.goods.medicine.service.GoodsAuditService;

/**
 * @author: shuang.zhang
 * @date: 2021/5/21
 */
@DubboService
public class GoodsAuditImpl implements GoodsAuditApi {

    @Autowired
    GoodsAuditService goodsAuditService;

    @Override
    public Boolean saveGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        return goodsAuditService.saveGoodsAudit(request);
    }

    @Override
    public Boolean notPassGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        return goodsAuditService.notPassGoodsAudit(request);
    }

    @Override
    public Boolean rejectGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        return goodsAuditService.rejectGoodsAudit(request);
    }

    @Override
    public GoodsAuditDTO getGoodsAuditByGidAndAuditStatus(Long gid) {
        return goodsAuditService.getGoodsAuditByGidAndAuditStatus(gid);
    }

    @Override
    public Page<GoodsAuditDTO> queryPageListGoodsAudit(QueryGoodsAuditPageRequest request) {
        return PojoUtils.map(goodsAuditService.queryPageListGoodsAudit(request), GoodsAuditDTO.class);
    }

    @Override
    public Page<GoodsAuditDTO> queryPageListGoodsAuditRecord(QueryGoodsAuditRecordPageRequest request) {
        return PojoUtils.map(goodsAuditService.queryPageListGoodsAuditRecord(request), GoodsAuditDTO.class);
    }

    @Override
    public GoodsAuditDTO getById(Long id) {
        return PojoUtils.map(goodsAuditService.getById(id),GoodsAuditDTO.class);
    }

    @Override
    public Boolean linkSellSpecifications(SaveSellSpecificationsRequest request) {
        return goodsAuditService.linkSellSpecifications(request);
    }

}
