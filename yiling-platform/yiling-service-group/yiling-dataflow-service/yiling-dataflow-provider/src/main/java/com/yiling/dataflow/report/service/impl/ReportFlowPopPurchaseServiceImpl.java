package com.yiling.dataflow.report.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.report.dto.ReportFlowPopPurchaseDTO;
import com.yiling.dataflow.report.dto.ReportFlowPurchaseDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPopPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPopPurchaseRequest;
import com.yiling.dataflow.report.entity.ReportFlowPopPurchaseDO;
import com.yiling.dataflow.report.dao.ReportFlowPopPurchaseMapper;
import com.yiling.dataflow.report.entity.ReportFlowPurchaseDO;
import com.yiling.dataflow.report.service.ReportFlowPopPurchaseService;
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
public class ReportFlowPopPurchaseServiceImpl extends BaseServiceImpl<ReportFlowPopPurchaseMapper, ReportFlowPopPurchaseDO> implements ReportFlowPopPurchaseService {

    @Override
    public Integer deleteReportFlowPopPurchase(DeteleTimeRequest deteleTimeRequest) {
        return this.baseMapper.deleteReportFlowPopPurchase(deteleTimeRequest.getTime());
    }

    @Override
    public List<ReportFlowPopPurchaseDTO> statisticsPopPurchase(QueryStatisticsFlowRequest request) {
        return PojoUtils.map(this.baseMapper.statisticsPopPurchase(request), ReportFlowPopPurchaseDTO.class);
    }

    @Override
    public List<ReportFlowPopPurchaseDTO> getReportFlowPopPurchaseList(QueryReportPopPurchaseRequest request) {
        QueryWrapper<ReportFlowPopPurchaseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportFlowPopPurchaseDO::getEid, request.getEid());
        queryWrapper.lambda().eq(ReportFlowPopPurchaseDO::getPoTime, DateUtil.parse(DateUtil.format(request.getPoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        List<ReportFlowPopPurchaseDO> reportFlowPurchaseDOList = this.list(queryWrapper);
        return PojoUtils.map(reportFlowPurchaseDOList, ReportFlowPopPurchaseDTO.class);
    }

    @Override
    public Integer saveReportFlowPopPurchase(SaveReportFlowPopPurchaseRequest request) {
        return this.save(PojoUtils.map(request,ReportFlowPopPurchaseDO.class))?1:0;
    }

    @Override
    public Integer saveReportFlowPopPurchaseList(List<SaveReportFlowPopPurchaseRequest> requestList) {
        return this.saveBatch(PojoUtils.map(requestList,ReportFlowPopPurchaseDO.class),2000)?1:0;
    }

    @Override
    public Integer updateReportFlowPopPurchaseById(SaveReportFlowPopPurchaseRequest request) {
        return this.updateById(PojoUtils.map(request,ReportFlowPopPurchaseDO.class))?1:0;
    }
}
