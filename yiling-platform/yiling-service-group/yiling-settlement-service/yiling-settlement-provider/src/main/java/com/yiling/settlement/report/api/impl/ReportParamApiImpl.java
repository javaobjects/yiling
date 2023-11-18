package com.yiling.settlement.report.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.ylprice.dto.request.AddGoodsYilingPriceLogRequest;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.ReportLadderGoodsInfoDTO;
import com.yiling.settlement.report.dto.ReportParamDTO;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.ReportYlGoodsCategoryDTO;
import com.yiling.settlement.report.dto.request.AddReportSubParamRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsCategoryRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsInfoRequest;
import com.yiling.settlement.report.dto.request.QueryParamSubGoodsPageListRequest;
import com.yiling.settlement.report.dto.request.QueryReportParPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportParamSubPageListRequest;
import com.yiling.settlement.report.dto.request.SaveOrUpdateParamSubGoodsRequest;
import com.yiling.settlement.report.entity.ParamDO;
import com.yiling.settlement.report.entity.ParamSubDO;
import com.yiling.settlement.report.entity.ParamSubGoodsDO;
import com.yiling.settlement.report.service.ParamLogService;
import com.yiling.settlement.report.service.ParamService;
import com.yiling.settlement.report.service.ParamSubGoodsService;
import com.yiling.settlement.report.service.ParamSubService;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@DubboService
public class ReportParamApiImpl implements ReportParamApi {

    @Autowired
    ParamService paramService;
    @Autowired
    ParamSubService paramSubService;
    @Autowired
    ParamSubGoodsService paramSubGoodsService;
    @Autowired
    ParamLogService paramLogService;

    @Override
    public Page<ReportParamDTO> queryReportParamPage(QueryReportParPageRequest queryReportParPageRequest) {
        Page<ParamDO> page = paramService.page(queryReportParPageRequest.getPage());
        return PojoUtils.map(page, ReportParamDTO.class);
    }

    @Override
    public Page<ReportParamSubDTO> queryReportParamSubPageList(QueryReportParamSubPageListRequest request) {
        return paramSubService.queryReportParamSubPageList(request);
    }

    @Override
    public ReportParamSubDTO queryReportParamSubById(Long id) {
        return PojoUtils.map(paramSubService.getById(id), ReportParamSubDTO.class);
    }

    @Override
    public Boolean saveOrUpdateReportSubParam(AddReportSubParamRequest request) {
        return paramSubService.saveOrUpdate(PojoUtils.map(request, ParamSubDO.class));
    }

    @Override
    public Boolean saveOrUpdateReportSubMemberParam(AddReportSubParamRequest request) {
        return paramSubService.saveOrUpdateReportSubMemberParam(request);
    }

    @Override
    public Page<ReportParamSubGoodsDTO> queryParamSubGoodsPageList(QueryParamSubGoodsPageListRequest request) {
        return paramSubGoodsService.queryParamSubGoodsPageList(request);
    }

    @Override
    public ReportParamSubGoodsDTO queryParamSubGoodsById(Long id) {
        ParamSubGoodsDO subGoodsDO = paramSubGoodsService.getById(id);
        return PojoUtils.map(subGoodsDO, ReportParamSubGoodsDTO.class);
    }

    @Override
    public Boolean saveOrUpdateParamSubCategoryGoods(SaveOrUpdateParamSubGoodsRequest request) {
        return paramSubGoodsService.saveOrUpdateParamSubCategoryGoods(request);
    }

    @Override
    public Boolean saveOrUpdateParamSubActivityGoods(SaveOrUpdateParamSubGoodsRequest request) {
        return paramSubGoodsService.saveOrUpdateParamSubActivityGoods(request);
    }

    @Override
    public Boolean saveOrUpdateParamSubLadderGoods(SaveOrUpdateParamSubGoodsRequest request) {
        return paramSubGoodsService.saveOrUpdateParamSubLadderGoods(request);
    }

    @Override
    public Boolean deleteParSubGoods(Long id, Long opUser) {
        return paramSubGoodsService.deleteParSubGoods(id,opUser);
    }

    @Override
    public List<ReportYlGoodsCategoryDTO> queryCategoryByYlGoodsIds(QueryGoodsCategoryRequest request) {
        return paramSubGoodsService.queryCategoryByYlGoodsIds(request);
    }

    @Override
    public List<ReportLadderGoodsInfoDTO> queryLadderInfo(QueryGoodsInfoRequest request) {
        return PojoUtils.map(paramSubGoodsService.queryLadderInfo(request),ReportLadderGoodsInfoDTO.class);
    }

    @Override
    public List<ReportParamSubGoodsDTO> queryActivityGoodsInfo(QueryGoodsInfoRequest request) {
        return paramSubGoodsService.queryActivityGoodsInfo(request);
    }

    @Override
    public void addPriceLog(AddGoodsYilingPriceLogRequest before, AddGoodsYilingPriceLogRequest after, Long userId) {
        paramLogService.addPriceLog(before,after,userId);
    }
}