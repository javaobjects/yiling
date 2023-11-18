package com.yiling.dataflow.report.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowGoodsBatchDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportGoodsBatchRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowGoodsBatchRequest;
import com.yiling.dataflow.report.entity.ReportFlowGoodsBatchDO;
import com.yiling.dataflow.report.dao.ReportFlowGoodsBatchMapper;
import com.yiling.dataflow.report.service.ReportFlowGoodsBatchService;
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
public class ReportFlowGoodsBatchServiceImpl extends BaseServiceImpl<ReportFlowGoodsBatchMapper, ReportFlowGoodsBatchDO> implements ReportFlowGoodsBatchService {

    @Override
    public Integer deleteReportFlowGoodsBatch(DeteleTimeRequest request) {
        return this.baseMapper.deleteReportFlowGoodsBatch(request.getTime());
    }

    @Override
    public List<ReportFlowBO> statisticsFlowGoodsBatchNumber(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsFlowGoodsBatchNumber(request);
    }

    @Override
    public List<ReportFlowStatisticsBO> statisticsFlowGoodsBatchNumberAndCount(QueryStatisticsFlowRequest request) {
        return this.baseMapper.statisticsFlowGoodsBatchNumberAndCount(request);
    }

    @Override
    public List<ReportFlowGoodsBatchDTO> getReportFlowGoodsBatchList(QueryReportGoodsBatchRequest request) {
        QueryWrapper<ReportFlowGoodsBatchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportFlowGoodsBatchDO::getEid, request.getEid());
        queryWrapper.lambda().eq(ReportFlowGoodsBatchDO::getGoodsCategory, request.getGoodsCategory());
        queryWrapper.lambda().eq(ReportFlowGoodsBatchDO::getGbTime, DateUtil.parse(DateUtil.format(request.getGbTime(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        List<ReportFlowGoodsBatchDO> reportFlowSaleDOList = this.list(queryWrapper);
        return PojoUtils.map(reportFlowSaleDOList, ReportFlowGoodsBatchDTO.class);
    }

    @Override
    public Integer saveReportFlowGoodsBatch(SaveReportFlowGoodsBatchRequest request) {
        return this.save(PojoUtils.map(request,ReportFlowGoodsBatchDO.class))?1:0;
    }

    @Override
    public Integer saveReportFlowGoodsBatchList(List<SaveReportFlowGoodsBatchRequest> request) {
        return this.saveBatch(PojoUtils.map(request,ReportFlowGoodsBatchDO.class),2000)?1:0;
    }

    @Override
    public Integer updateReportFlowGoodsBatchById(SaveReportFlowGoodsBatchRequest request) {
        return this.updateById(PojoUtils.map(request,ReportFlowGoodsBatchDO.class))?1:0;
    }
}
