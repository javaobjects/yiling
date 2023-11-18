package com.yiling.admin.erp.statistics.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.erp.statistics.form.FlushGoodsSpecIdForm;
import com.yiling.admin.erp.statistics.form.GoodsSpecStatisticsForm;
import com.yiling.admin.erp.statistics.form.QueryBalanceStatisticsForm;
import com.yiling.admin.erp.statistics.form.QueryBalanceStatisticsMonthForm;
import com.yiling.admin.erp.statistics.form.RecommendScoreForm;
import com.yiling.admin.erp.statistics.vo.EnterpriseInfoVO;
import com.yiling.admin.erp.statistics.vo.FlowBalanceStatisticsMonthVO;
import com.yiling.admin.erp.statistics.vo.FlowBalanceStatisticsVO;
import com.yiling.admin.erp.statistics.vo.GoodsSpecInfoVO;
import com.yiling.admin.erp.statistics.vo.GoodsSpecNoMatchedVO;
import com.yiling.admin.erp.statistics.vo.GoodsSpecStatisticsVO;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsApi;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsJobApi;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsDTO;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsMonthDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecInfoDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecNoMatchedDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecStatisticsDTO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.RecommendScoreRequest;
import com.yiling.dataflow.statistics.dto.request.StatisticsFlowBalanceRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fucheng.bai
 * @date 2022/7/26
 */
@RestController
@RequestMapping("/flow/balanceStatistics")
@Api(tags = "月流向监控")
@Slf4j
public class FlowBalanceStatisticsController extends BaseController {

    @DubboReference(timeout = 1000 * 10)
    private FlowBalanceStatisticsApi flowBalanceStatisticsApi;

    @DubboReference(timeout = 1000 * 60)
    private FlowBalanceStatisticsJobApi flowBalanceStatisticsJobApi;

    @DubboReference
    private ErpClientApi erpClientApi;

    @ApiOperation(value = "获取企业信息列表")
    @GetMapping("/enterprise/list")
    public Result<List<EnterpriseInfoVO>> enterpriseList() {
        List<EnterpriseInfoVO> enterpriseInfoVOList = new ArrayList<>();

        List<FlowBalanceStatisticsDTO> list = flowBalanceStatisticsApi.getFlowBalanceStatisticsEnterpriseList();
        for (FlowBalanceStatisticsDTO flowBalanceStatisticsDTO : list) {
            EnterpriseInfoVO enterpriseInfoVO = new EnterpriseInfoVO();
            enterpriseInfoVO.setSuId(flowBalanceStatisticsDTO.getEid());
            enterpriseInfoVO.setClientName(flowBalanceStatisticsDTO.getEname());
            enterpriseInfoVOList.add(enterpriseInfoVO);
        }
        return Result.success(enterpriseInfoVOList);
    }

    @ApiOperation(value = "企业月份统计表")
    @PostMapping("/month/pageList")
    public Result<Page<FlowBalanceStatisticsMonthVO>> monthPageList(@RequestBody QueryBalanceStatisticsMonthForm form) {
        if (StringUtils.isEmpty(form.getTime())) {
            throw new ServiceException(ResultCode.PARAM_MISS);
        }
        QueryBalanceStatisticsMonthRequest request = PojoUtils.map(form, QueryBalanceStatisticsMonthRequest.class);
        Page<FlowBalanceStatisticsMonthDTO> page = flowBalanceStatisticsApi.getMonthPageList(request);

        return Result.success(PojoUtils.map(page, FlowBalanceStatisticsMonthVO.class));
    }

    @ApiOperation(value = "获取企业每天的统计情况表")
    @PostMapping("/list")
    public Result<List<FlowBalanceStatisticsVO>> list(@RequestBody QueryBalanceStatisticsForm form) {
        if (StringUtils.isEmpty(form.getMonthTime()) || StringUtils.isEmpty(form.getEid())) {
            throw new ServiceException(ResultCode.PARAM_MISS);
        }
        QueryBalanceStatisticsRequest request = PojoUtils.map(form, QueryBalanceStatisticsRequest.class);

        List<FlowBalanceStatisticsVO> resultList = new ArrayList<>();

        List<FlowBalanceStatisticsDTO> flowBalanceStatisticsDTOList = flowBalanceStatisticsApi.getFlowBalanceStatisticsList(request);
        for (FlowBalanceStatisticsDTO flowBalanceStatisticsDTO : flowBalanceStatisticsDTOList) {
            FlowBalanceStatisticsVO flowBalanceStatisticsVO = PojoUtils.map(flowBalanceStatisticsDTO, FlowBalanceStatisticsVO.class);
            resultList.add(flowBalanceStatisticsVO);

        }
        return Result.success(resultList);
    }

    @ApiOperation(value = "获取商品规格条件列表")
    @GetMapping("/goodsSpec/info/list")
    public Result<List<GoodsSpecInfoVO>> goodsSpecInfoList(@RequestParam(value = "eid") Long eid, @RequestParam(value = "monthTime") String monthTime) {
        if (eid == null || StringUtils.isEmpty(monthTime)) {
            throw new ServiceException(ResultCode.PARAM_MISS);
        }
        List<GoodsSpecInfoVO> goodsSpecInfoVOList = new ArrayList<>();

        List<GoodsSpecInfoDTO> goodsSpecInfoDTOList = flowBalanceStatisticsApi.getGoodsSpecInfoList(eid, monthTime);
        for (GoodsSpecInfoDTO goodsSpecInfoDTO : goodsSpecInfoDTOList) {
            GoodsSpecInfoVO goodsSpecInfoVO = PojoUtils.map(goodsSpecInfoDTO, GoodsSpecInfoVO.class);
            goodsSpecInfoVO.setGoodsNameSpecDesc(goodsSpecInfoVO.getGoodsName() + "_" + goodsSpecInfoVO.getSpec());
            goodsSpecInfoVOList.add(goodsSpecInfoVO);
        }
        return Result.success(goodsSpecInfoVOList);
    }

    @ApiOperation(value = "重新统计平衡数据")
    @GetMapping("/enterprise/again/statistics")
    public Result<Boolean> enterpriseAgainStatistics(@RequestParam(value = "eid") Long eid, @RequestParam(value = "monthTime") String monthTime) {
        if (eid == null || StringUtils.isEmpty(monthTime)) {
            throw new ServiceException(ResultCode.PARAM_MISS);
        }

        StatisticsFlowBalanceRequest request = new StatisticsFlowBalanceRequest();
        request.setMonthTime(monthTime);
        List<ErpClientDataDTO> erpClientDataDTOList = new ArrayList<>();
        if (eid != null && eid != 0) {
            ErpClientDTO erpClientDTO = erpClientApi.selectByRkSuId(eid);
            ErpClientDataDTO erpClientDataDTO = PojoUtils.map(erpClientDTO, ErpClientDataDTO.class);
            erpClientDataDTOList.add(erpClientDataDTO);
        }
        flowBalanceStatisticsJobApi.statisticsFlowBalanceJob(request, erpClientDataDTOList);
        return Result.success();
    }


    @ApiOperation(value = "获取商品统计详情")
    @PostMapping("/goodsSpec/statistics/list")
    public Result<Page<GoodsSpecStatisticsVO>> goodsSpecStatisticsListPage(@RequestBody @Valid GoodsSpecStatisticsForm form) {
        GoodsSpecStatisticsRequest request = PojoUtils.map(form, GoodsSpecStatisticsRequest.class);
        Page<GoodsSpecStatisticsDTO> page = flowBalanceStatisticsApi.getGoodsSpecStatisticsListPage(request);

        return Result.success(PojoUtils.map(page, GoodsSpecStatisticsVO.class));
    }

    @ApiOperation(value = "获取待匹配商品列表")
    @PostMapping("/goodsSpec/noMatched/list")
    public Result<List<GoodsSpecNoMatchedVO>> goodsSpecNoMatchedList(@RequestBody @Valid GoodsSpecStatisticsForm form) {
        GoodsSpecStatisticsRequest request = PojoUtils.map(form, GoodsSpecStatisticsRequest.class);
        List<GoodsSpecNoMatchedDTO> goodsSpecNoMatchedDTOList = flowBalanceStatisticsApi.goodsSpecNoMatchedList(request);
        return Result.success(PojoUtils.map(goodsSpecNoMatchedDTOList, GoodsSpecNoMatchedVO.class));
    }

    @ApiOperation(value = "获取推荐分数")
    @PostMapping("/getRecommendScore")
    public Result<Integer> getRecommendScore(@RequestBody RecommendScoreForm form) {
        RecommendScoreRequest request = PojoUtils.map(form, RecommendScoreRequest.class);
        Integer recommendScore = flowBalanceStatisticsApi.getRecommendScore(request);
        return Result.success(recommendScore);
    }

    @ApiOperation(value = "更新规格Id")
    @PostMapping("/goodsSpec/specificationId/flush")
    public Result flushGoodsSpecificationId(@RequestBody FlushGoodsSpecIdForm from) {
        FlushGoodsSpecIdRequest request = PojoUtils.map(from, FlushGoodsSpecIdRequest.class);
        flowBalanceStatisticsApi.flushGoodsSpecificationId(request);
        return Result.success();
    }
}
