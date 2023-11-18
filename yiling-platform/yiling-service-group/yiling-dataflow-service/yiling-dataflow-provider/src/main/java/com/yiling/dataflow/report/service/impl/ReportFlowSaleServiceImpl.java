package com.yiling.dataflow.report.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dao.ReportFlowSaleMapper;
import com.yiling.dataflow.report.dto.ReportFlowSaleDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportSaleRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowSaleRequest;
import com.yiling.dataflow.report.entity.ReportFlowSaleDO;
import com.yiling.dataflow.report.service.ReportFlowSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
@Service
public class ReportFlowSaleServiceImpl extends BaseServiceImpl<ReportFlowSaleMapper, ReportFlowSaleDO> implements ReportFlowSaleService {

    @Override
    public Integer deleteReportFlowSale(DeteleTimeRequest request) {
        return this.baseMapper.deleteReportFlowSale(request.getTime());
    }

    @Override
    public List<ReportFlowSaleDTO> getReportFlowSaleList(QueryReportSaleRequest request) {
        QueryWrapper<ReportFlowSaleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportFlowSaleDO::getEid, request.getEid());
        queryWrapper.lambda().eq(ReportFlowSaleDO::getGoodsCategory, request.getGoodsCategory());
        queryWrapper.lambda().eq(ReportFlowSaleDO::getSoTime, DateUtil.parse(DateUtil.format(request.getSoTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        List<ReportFlowSaleDO> reportFlowSaleDOList = this.list(queryWrapper);
        return PojoUtils.map(reportFlowSaleDOList, ReportFlowSaleDTO.class);
    }

    @Override
    public Integer saveReportFlowSale(SaveReportFlowSaleRequest request) {
        return this.save(PojoUtils.map(request,ReportFlowSaleDO.class))?1:0;
    }

    @Override
    public Integer saveReportFlowSaleList(List<SaveReportFlowSaleRequest> requestList) {
        return this.saveBatch(PojoUtils.map(requestList,ReportFlowSaleDO.class),2000)?1:0;
    }

    @Override
    public Integer updateReportFlowSaleById(SaveReportFlowSaleRequest request) {
        return this.updateById(PojoUtils.map(request,ReportFlowSaleDO.class))?1:0;
    }

    @Override
    public List<ReportFlowBO> statisticsLhSaleAmount(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsLhSaleAmount(request);
    }

    @Override
    public List<ReportFlowBO> statisticsLhSaleNumber(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsLhSaleNumber(request);
    }

    @Override
    public List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsLhAmountAndNumber(request);
    }


}
