package com.yiling.dataflow.flowcollect.api.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.SalesAppealConfirmApi;

import com.yiling.dataflow.flowcollect.dto.FlowFleeingGoodsDTO;
import com.yiling.dataflow.flowcollect.dto.SaleAppealGoodsFormInfoDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveSaleAppealFlowRelateRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveSalesAppealDateRequest;
import com.yiling.dataflow.flowcollect.entity.SaleAppealFlowRelateDO;
import com.yiling.dataflow.flowcollect.entity.SaleAppealGoodsFormInfoDO;
import com.yiling.dataflow.flowcollect.enums.TransferTypeEnum;
import com.yiling.dataflow.flowcollect.service.SaleAppealFlowRelateService;
import com.yiling.dataflow.flowcollect.service.SaleAppealGoodsFormInfoService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 确认销量申诉api
 *
 * @author: shixing.sun
 * @date: 2023-03-04
 */
@Slf4j
@DubboService
public class SalesAppealConfirmApiImpl implements SalesAppealConfirmApi {
    @Autowired
    private SaleAppealGoodsFormInfoService saleAppealGoodsFormInfoService;
    @Resource
    private SaleAppealFlowRelateService saleAppealFlowRelateService;

    @Override
    public Boolean saveSalesAppealConfirmDate(List<SaveSalesAppealDateRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return false;
        }
        List<SaleAppealGoodsFormInfoDO> saleAppealGoodsFormInfoDOS = PojoUtils.map(requests, SaleAppealGoodsFormInfoDO.class);
        return saleAppealGoodsFormInfoService.saveBatch(saleAppealGoodsFormInfoDOS);
    }

    @Override
    public Boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId) {
        return saleAppealGoodsFormInfoService.updateTaskIdByRecordId(recordId, taskId, opUserId);
    }

    @Override
    public Page<FlowFleeingGoodsDTO> pageList(QueryFlowMonthPageRequest request) {
        return PojoUtils.map(saleAppealGoodsFormInfoService.pageList(request), FlowFleeingGoodsDTO.class);
    }

    @Override
    public long saveSaveSaleAppealFlowRelate(SaveSaleAppealFlowRelateRequest saveSaleAppealFlowRelateRequest) {
        SaleAppealFlowRelateDO saleAppealFlowRelateDO = PojoUtils.map(saveSaleAppealFlowRelateRequest, SaleAppealFlowRelateDO.class);
        saleAppealFlowRelateService.save(saleAppealFlowRelateDO);
        return saleAppealFlowRelateDO.getId();
    }

    @Override
    public List<SaleAppealGoodsFormInfoDTO> getSaleAppealGoodsFormInfoDTOList(Long formId) {
        LambdaQueryWrapper<SaleAppealGoodsFormInfoDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SaleAppealGoodsFormInfoDO::getFormId, formId);
        queryWrapper.eq(SaleAppealGoodsFormInfoDO::getTaskId, 0);
        queryWrapper.eq(SaleAppealGoodsFormInfoDO::getTransferType, TransferTypeEnum.EXCEL_UPLOAD.getCode());
        return PojoUtils.map(saleAppealGoodsFormInfoService.list(queryWrapper), SaleAppealGoodsFormInfoDTO.class);
    }
}
