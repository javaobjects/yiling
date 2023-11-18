package com.yiling.admin.data.center.report.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.data.center.report.form.QueryEnterpriseListForm;
import com.yiling.admin.data.center.report.form.QueryStatisticsFlowForm;
import com.yiling.admin.data.center.report.vo.EnterpriseInfoVO;
import com.yiling.admin.data.center.report.vo.ReportFlowGoodsBatchStatisticsVO;
import com.yiling.admin.data.center.report.vo.ReportFlowPopPurchaseStatisticeVO;
import com.yiling.admin.data.center.report.vo.ReportFlowPopPurchaseVO;
import com.yiling.admin.data.center.report.vo.ReportFlowPurchaseStatisticsVO;
import com.yiling.admin.data.center.report.vo.ReportFlowSaleStatisticsVO;
import com.yiling.admin.data.center.report.vo.ReportFlowStatisticsVO;
import com.yiling.admin.data.center.report.vo.ReportFlowVO;
import com.yiling.dataflow.order.api.FlowSettlementEnterpriseTagApi;
import com.yiling.admin.data.center.report.vo.ReportLhFlowStatisticsVO;
import com.yiling.dataflow.report.api.ReportFlowGoodsBatchApi;
import com.yiling.dataflow.report.api.ReportFlowPopPurchaseApi;
import com.yiling.dataflow.report.api.ReportFlowPurchaseApi;
import com.yiling.dataflow.report.api.ReportFlowSaleApi;
import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowPopPurchaseDTO;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@RestController
@RequestMapping("/report/flow")
@Api(tags = "大屏流向报表")
@Slf4j
public class ReportFlowController extends BaseController {

    @DubboReference
    private ReportFlowSaleApi reportFlowSaleApi;
    @DubboReference
    private ReportFlowPurchaseApi reportFlowPurchaseApi;
    @DubboReference
    private ReportFlowGoodsBatchApi reportFlowGoodsBatchApi;
    @DubboReference
    private ReportFlowPopPurchaseApi reportFlowPopPurchaseApi;
    @DubboReference
    private EnterpriseApi enterpriseApi;
    @DubboReference
    private FlowSettlementEnterpriseTagApi flowSettlementEnterpriseTagApi;
    @DubboReference
    private EnterpriseTagApi enterpriseTagApi;

    @ApiOperation(value = "查询企业信息", httpMethod = "POST")
    @PostMapping("/enterpriseList")
    public Result<List<EnterpriseInfoVO>> enterpriseList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryEnterpriseListForm form) {
        QueryEnterpriseByNameRequest request = PojoUtils.map(form, QueryEnterpriseByNameRequest.class);
        request.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
        List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(request);
        List<EnterpriseInfoVO> list = new ArrayList<>();
        for (EnterpriseDTO enterpriseDTO : enterpriseList) {
            EnterpriseInfoVO enterpriseInfoVO = new EnterpriseInfoVO();
            enterpriseInfoVO.setSuId(enterpriseDTO.getId());
            enterpriseInfoVO.setClientName(enterpriseDTO.getName());
            list.add(enterpriseInfoVO);
        }
        return Result.success(list);
    }

    @ApiOperation(value = "销售单销售额报表")
    @PostMapping("lhSaleAmount")
    public Result<List<ReportFlowVO>> saleAmount(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success(ListUtil.empty());
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }

        List<ReportFlowBO> reportFlowBOList = reportFlowSaleApi.statisticsLhSaleAmount(request);
        return Result.success(PojoUtils.map(reportFlowBOList, ReportFlowVO.class));
    }

    @ApiOperation(value = "销售单销售数量报表")
    @PostMapping("lhSaleNumber")
    public Result<List<ReportFlowVO>> saleNumber(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success(ListUtil.empty());
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        List<ReportFlowBO> reportFlowBOList = reportFlowSaleApi.statisticsLhSaleNumber(request);
        return Result.success(PojoUtils.map(reportFlowBOList, ReportFlowVO.class));
    }

    @ApiOperation(value = "销售单销售列表统计")
    @PostMapping("saleStatistics")
    public Result<ReportFlowSaleStatisticsVO> saleStatistics(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success();
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }

        List<ReportFlowStatisticsBO> reportFlowBOList = reportFlowSaleApi.statisticsLhAmountAndNumber(request);
        Long numberTotal=0L;
        BigDecimal amountTotal=BigDecimal.ZERO;
        Long terminalNumberTotal=0L;
        BigDecimal terminalAmountTotal=BigDecimal.ZERO;
        for(ReportFlowStatisticsBO reportFlowStatisticsBO:reportFlowBOList){
            numberTotal=numberTotal+reportFlowStatisticsBO.getNumber();
            amountTotal=amountTotal.add(reportFlowStatisticsBO.getAmount());
            terminalNumberTotal=terminalNumberTotal+reportFlowStatisticsBO.getTerminalNumber();
            terminalAmountTotal=terminalAmountTotal.add(reportFlowStatisticsBO.getTerminalAmount());
        }
        ReportFlowSaleStatisticsVO reportFlowSaleStatisticsVO=new ReportFlowSaleStatisticsVO();
        reportFlowSaleStatisticsVO.setList(PojoUtils.map(reportFlowBOList, ReportFlowStatisticsVO.class));
        reportFlowSaleStatisticsVO.setAmountTotal(amountTotal.setScale(2,BigDecimal.ROUND_HALF_UP));
        reportFlowSaleStatisticsVO.setNumberTotal(numberTotal);
        reportFlowSaleStatisticsVO.setTerminalAmountTotal(terminalAmountTotal.setScale(2,BigDecimal.ROUND_HALF_UP));
        reportFlowSaleStatisticsVO.setTerminalNumberTotal(terminalNumberTotal);
        return Result.success(reportFlowSaleStatisticsVO);
    }

    @ApiOperation(value = "采购单销售额报表")
    @PostMapping("lhPurchaseAmount")
    public Result<List<ReportFlowVO>> purchaseAmount(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success(ListUtil.empty());
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        List<ReportFlowBO> reportFlowBOList = reportFlowPurchaseApi.statisticsLhPurchaseAmount(request);
        return Result.success(PojoUtils.map(reportFlowBOList, ReportFlowVO.class));
    }

    @ApiOperation(value = "采购单销售数量报表")
    @PostMapping("lhPurchaseNumber")
    public Result<List<ReportFlowVO>> purchaseNumber(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success(ListUtil.empty());
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        List<ReportFlowBO> reportFlowBOList = reportFlowPurchaseApi.statisticsLhPurchaseNumber(request);
        return Result.success(PojoUtils.map(reportFlowBOList, ReportFlowVO.class));
    }

    @ApiOperation(value = "采购单销售列表统计")
    @PostMapping("purchaseStatistics")
    public Result<ReportFlowPurchaseStatisticsVO> purchaseStatistics(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success();
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        List<ReportFlowStatisticsBO> reportFlowBOList = reportFlowPurchaseApi.statisticsLhAmountAndNumber(request);
        Long numberTotal=0L;
        BigDecimal amountTotal=BigDecimal.ZERO;
        for(ReportFlowStatisticsBO reportFlowStatisticsBO:reportFlowBOList){
            numberTotal=numberTotal+reportFlowStatisticsBO.getNumber();
            amountTotal=amountTotal.add(reportFlowStatisticsBO.getAmount());
        }
        ReportFlowPurchaseStatisticsVO reportFlowPurchaseStatisticsVO=new ReportFlowPurchaseStatisticsVO();
        reportFlowPurchaseStatisticsVO.setList(PojoUtils.map(reportFlowBOList,ReportFlowStatisticsVO.class));
        reportFlowPurchaseStatisticsVO.setAmountTotal(amountTotal.setScale(2,BigDecimal.ROUND_HALF_UP));
        reportFlowPurchaseStatisticsVO.setNumberTotal(numberTotal);
        return Result.success(reportFlowPurchaseStatisticsVO);
    }


    @ApiOperation(value = "库存销售数量报表")
    @PostMapping("goodsBatchNumber")
    public Result<List<ReportFlowVO>> goodsBatchNumber(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success(ListUtil.empty());
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        List<ReportFlowBO> reportFlowBOList = reportFlowGoodsBatchApi.statisticsFlowGoodsBatchNumber(request);
        return Result.success(PojoUtils.map(reportFlowBOList, ReportFlowVO.class));
    }

    @ApiOperation(value = "库存销售数量列表统计")
    @PostMapping("goodsBatchStatistics")
    public Result<ReportFlowGoodsBatchStatisticsVO> goodsBatchStatistics(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success();
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        Map<String,ReportFlowStatisticsBO> reportLhFlowBOMap=new HashMap<>();
        Map<String,ReportFlowStatisticsBO> reportUnLhFlowBOMap=new HashMap<>();
        List<ReportFlowStatisticsBO> reportFlowBOList = reportFlowGoodsBatchApi.statisticsFlowGoodsBatchNumberAndCount(request);
        if(request.getGoodsCategory()==null||request.getGoodsCategory()==0) {
            request.setGoodsCategory(1);
            List<ReportFlowStatisticsBO> reportLhFlowBOList =reportFlowGoodsBatchApi.statisticsFlowGoodsBatchNumberAndCount(request);
            for(ReportFlowStatisticsBO reportLhFlowStatisticsBO:reportLhFlowBOList){
                reportLhFlowBOMap.put(DateUtil.format(reportLhFlowStatisticsBO.getTime(),"yyyy-MM-dd"),reportLhFlowStatisticsBO);
            }
            request.setGoodsCategory(2);
            List<ReportFlowStatisticsBO> reportUnLhFlowBOList =reportFlowGoodsBatchApi.statisticsFlowGoodsBatchNumberAndCount(request);
            for(ReportFlowStatisticsBO reportUnLhFlowStatisticsBO:reportUnLhFlowBOList){
                reportUnLhFlowBOMap.put(DateUtil.format(reportUnLhFlowStatisticsBO.getTime(),"yyyy-MM-dd"),reportUnLhFlowStatisticsBO);
            }
        }
        if (CollUtil.isNotEmpty(reportFlowBOList)) {
            ReportFlowStatisticsBO reportFlowBOMax = reportFlowBOList.get(0);
            ReportFlowStatisticsBO reportFlowBOMin = reportFlowBOList.get(reportFlowBOList.size() - 1);
            BigDecimal totalNumber = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (ReportFlowStatisticsBO reportFlowStatisticsBO : reportFlowBOList) {
                totalNumber = totalNumber.add(new BigDecimal(reportFlowStatisticsBO.getNumber()));
                totalAmount = totalAmount.add(reportFlowStatisticsBO.getAmount());
            }

            List<ReportLhFlowStatisticsVO> flowVOList = PojoUtils.map(reportFlowBOList, ReportLhFlowStatisticsVO.class);
            for(ReportLhFlowStatisticsVO reportLhFlowStatisticsVO:flowVOList){
                reportLhFlowStatisticsVO.setAmount(reportLhFlowStatisticsVO.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));

                ReportFlowStatisticsBO reportLhFlowStatisticsBO=reportLhFlowBOMap.get(DateUtil.format(reportLhFlowStatisticsVO.getTime(),"yyyy-MM-dd"));
                if(reportLhFlowStatisticsBO!=null) {
                    reportLhFlowStatisticsVO.setLhAmount(reportLhFlowStatisticsBO.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
                    reportLhFlowStatisticsVO.setLhNumber(reportLhFlowStatisticsBO.getNumber());
                }else{
                    reportLhFlowStatisticsVO.setLhAmount(BigDecimal.ZERO);
                    reportLhFlowStatisticsVO.setLhNumber(0L);
                }
                ReportFlowStatisticsBO reportUnLhFlowStatisticsBO=reportUnLhFlowBOMap.get(DateUtil.format(reportLhFlowStatisticsVO.getTime(),"yyyy-MM-dd"));
                if(reportUnLhFlowStatisticsBO!=null) {
                    reportLhFlowStatisticsVO.setUnLhAmount(reportUnLhFlowStatisticsBO.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
                    reportLhFlowStatisticsVO.setUnLhNumber(reportUnLhFlowStatisticsBO.getNumber());
                }else{
                    reportLhFlowStatisticsVO.setUnLhAmount(BigDecimal.ZERO);
                    reportLhFlowStatisticsVO.setUnLhNumber(0L);
                }
            }
            ReportFlowGoodsBatchStatisticsVO reportFlowGoodsBatchStatisticsVO = new ReportFlowGoodsBatchStatisticsVO();
            reportFlowGoodsBatchStatisticsVO.setList(flowVOList);
            reportFlowGoodsBatchStatisticsVO.setDiffNumber(reportFlowBOMax.getNumber() - reportFlowBOMin.getNumber());
            reportFlowGoodsBatchStatisticsVO.setAvgNumber(totalNumber.divide(new BigDecimal(reportFlowBOList.size()), 2, BigDecimal.ROUND_HALF_UP));
            reportFlowGoodsBatchStatisticsVO.setDiffAmount(reportFlowBOMax.getAmount().subtract(reportFlowBOMin.getAmount()).setScale(2,BigDecimal.ROUND_HALF_UP));
            reportFlowGoodsBatchStatisticsVO.setAvgAmount(totalAmount.divide(new BigDecimal(reportFlowBOList.size()), 2, BigDecimal.ROUND_HALF_UP));
            return Result.success(reportFlowGoodsBatchStatisticsVO);
        }
        return Result.failed("没有统计数据");

    }

    @ApiOperation(value = "ERP采购和POP发货对比")
    @PostMapping("statisticsPopPurchase")
    public Result<ReportFlowPopPurchaseStatisticeVO> statisticsPopPurchase(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody QueryStatisticsFlowForm form) {
        QueryStatisticsFlowRequest request = PojoUtils.map(form, QueryStatisticsFlowRequest.class);
        List<Long> cloudList=null;
        List<Long> eids=null;
        if (form.getIsCloudFlag() != null && form.getIsCloudFlag() == 1) {
            List<Long> eidList=flowSettlementEnterpriseTagApi.getFlowSettlementEnterpriseTagNameList();
            if(CollUtil.isEmpty(eidList)){
                return Result.success();
            }
            cloudList=eidList;
            request.setEids(eidList);
        }
        if (form.getEid() == null || form.getEid() == 0) {
            if (form.getChannelId() != null && form.getChannelId() != 0) {
                QueryEnterpriseByNameRequest queryEnterpriseByNameRequest = new QueryEnterpriseByNameRequest();
                queryEnterpriseByNameRequest.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
                queryEnterpriseByNameRequest.setChannelId(form.getChannelId());
                List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(queryEnterpriseByNameRequest);
                eids=enterpriseList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(cloudList)) {
                    List<Long> finalCloudList = cloudList;
                    request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
                }else{
                    request.setEids(eids);
                }
            }
        } else {
            eids=Arrays.asList(form.getEid());
            if(CollUtil.isNotEmpty(cloudList)) {
                List<Long> finalCloudList = cloudList;
                request.setEids(eids.stream().filter(e -> finalCloudList.contains(e)).collect(Collectors.toList()));
            }else{
                request.setEids(eids);
            }
        }
        List<ReportFlowPopPurchaseDTO> reportFlowBOList = reportFlowPopPurchaseApi.statisticsPopPurchase(request);
        if (CollUtil.isNotEmpty(reportFlowBOList)) {
            Long erpQuantityTotal = 0L;
            BigDecimal erpTotalAmountTotal = BigDecimal.ZERO;
            Long poQuantityTotal = 0L;
            BigDecimal poTotalAmountTotal = BigDecimal.ZERO;
            List<ReportFlowPopPurchaseVO> list = new ArrayList<>();
            for (ReportFlowPopPurchaseDTO reportFlowPopPurchaseDTO : reportFlowBOList) {
                erpQuantityTotal = erpQuantityTotal + reportFlowPopPurchaseDTO.getErpQuantity();
                poQuantityTotal = poQuantityTotal + reportFlowPopPurchaseDTO.getPoQuantity();
                erpTotalAmountTotal = erpTotalAmountTotal.add(reportFlowPopPurchaseDTO.getErpTotalAmount());
                poTotalAmountTotal = poTotalAmountTotal.add(reportFlowPopPurchaseDTO.getPoTotalAmount());
                ReportFlowPopPurchaseVO reportFlowPopPurchaseVO = new ReportFlowPopPurchaseVO();
                reportFlowPopPurchaseVO.setErpQuantity(reportFlowPopPurchaseDTO.getErpQuantity());
                reportFlowPopPurchaseVO.setErpTotalAmount(reportFlowPopPurchaseDTO.getErpTotalAmount());
                reportFlowPopPurchaseVO.setPoQuantity(reportFlowPopPurchaseDTO.getPoQuantity());
                reportFlowPopPurchaseVO.setPoTotalAmount(reportFlowPopPurchaseDTO.getPoTotalAmount());
                reportFlowPopPurchaseVO.setPoTime(reportFlowPopPurchaseDTO.getPoTime());
                list.add(reportFlowPopPurchaseVO);
            }
            ReportFlowPopPurchaseStatisticeVO reportFlowPopPurchaseStatisticeVO = new ReportFlowPopPurchaseStatisticeVO();
            reportFlowPopPurchaseStatisticeVO.setList(list);
            reportFlowPopPurchaseStatisticeVO.setErpQuantityTotal(erpQuantityTotal);
            reportFlowPopPurchaseStatisticeVO.setErpTotalAmountTotal(erpTotalAmountTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            reportFlowPopPurchaseStatisticeVO.setPoQuantityTotal(poQuantityTotal);
            reportFlowPopPurchaseStatisticeVO.setPoTotalAmountTotal(poTotalAmountTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            return Result.success(reportFlowPopPurchaseStatisticeVO);
        }
        return Result.failed("没有统计数据");
    }
}