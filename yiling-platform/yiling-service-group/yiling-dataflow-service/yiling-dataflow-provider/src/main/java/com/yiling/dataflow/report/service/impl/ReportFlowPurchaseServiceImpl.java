package com.yiling.dataflow.report.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowPurchaseDTO;
import com.yiling.dataflow.report.dto.ReportFlowSaleDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPurchaseRequest;
import com.yiling.dataflow.report.entity.ReportFlowPurchaseDO;
import com.yiling.dataflow.report.dao.ReportFlowPurchaseMapper;
import com.yiling.dataflow.report.entity.ReportFlowSaleDO;
import com.yiling.dataflow.report.service.ReportFlowPurchaseService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
@Service
public class ReportFlowPurchaseServiceImpl extends BaseServiceImpl<ReportFlowPurchaseMapper, ReportFlowPurchaseDO> implements ReportFlowPurchaseService {

    @Override
    public Integer deleteReportFlowPurchase( DeteleTimeRequest deteleTimeRequest) {
        return this.baseMapper.deleteReportFlowPurchase(deteleTimeRequest.getTime());
    }

    @Override
    public List<ReportFlowPurchaseDTO> getReportFlowPurchaseList(QueryReportPurchaseRequest request) {
        QueryWrapper<ReportFlowPurchaseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportFlowPurchaseDO::getEid, request.getEid());
        queryWrapper.lambda().eq(ReportFlowPurchaseDO::getGoodsCategory, request.getGoodsCategory());
        queryWrapper.lambda().eq(ReportFlowPurchaseDO::getPoTime, DateUtil.parse(DateUtil.format(request.getPoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        List<ReportFlowPurchaseDO> reportFlowPurchaseDOList = this.list(queryWrapper);
        return PojoUtils.map(reportFlowPurchaseDOList, ReportFlowPurchaseDTO.class);
    }

    @Override
    public Integer saveReportFlowPurchase(SaveReportFlowPurchaseRequest request) {
        return this.save(PojoUtils.map(request,ReportFlowPurchaseDO.class))?1:0;
    }

    @Override
    public Integer saveReportFlowPurchaseList(List<SaveReportFlowPurchaseRequest> request) {
        return this.saveBatch(PojoUtils.map(request,ReportFlowPurchaseDO.class),2000)?1:0;
    }

    @Override
    public Integer updateReportFlowPurchaseById(SaveReportFlowPurchaseRequest request) {
        return this.updateById(PojoUtils.map(request,ReportFlowPurchaseDO.class))?1:0;
    }

    @Override
    public List<ReportFlowBO> statisticsLhPurchaseAmount(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsLhPurchaseAmount(request);
    }

    @Override
    public List<ReportFlowBO> statisticsLhPurchaseNumber(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsLhPurchaseNumber(request);
    }

    @Override
    public List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsLhAmountAndNumber(request);
    }
}
