package com.yiling.sjms.goodsplans.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.statistics.api.FLowAnalyseCommonApi;
import com.yiling.dataflow.statistics.dto.FlowAnalyseEnterpriseDTO;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseEnterpriseRequest;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseGoodsRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.goodsplans.form.FlowAnalyseEnterpriseForm;
import com.yiling.sjms.goodsplans.form.FlowAnalyseGoodsForm;
import com.yiling.sjms.goodsplans.vo.FlowAnalyseEnterpriseVo;
import com.yiling.sjms.goodsplans.vo.FlowAnalyseGoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 补货预测数据接口
 */
@Slf4j
@RestController
@RequestMapping("/gpc")
@Api(tags = "补货预测公共接口")
public class GoodsPlansCommonController extends BaseController {
    @DubboReference
    private FLowAnalyseCommonApi fLowAnalyseCommonApi;
    @DubboReference(timeout = 10* 1000)
    private CrmEnterpriseApi crmEnterpriseApi;
    @ApiOperation("获取商品列表")
    @PostMapping("/goods/list")
    public Result<Page<FlowAnalyseGoodsVo>> getGoodsListByName(@RequestBody FlowAnalyseGoodsForm form) {
        FlowAnalyseGoodsRequest request=new FlowAnalyseGoodsRequest();
        PojoUtils.map(form,request);
        //判断是否供应商等级和供应商体系

        Page<FlowAnalyseGoodsDTO> flowAnalyseGoodsVos = fLowAnalyseCommonApi.getGoodsListByName(request);
        return Result.success(PojoUtils.map(flowAnalyseGoodsVos,FlowAnalyseGoodsVo.class));
    }

    @ApiOperation("获取经销商列表")
    @PostMapping("/enterprise/list")
    public Result<Page<FlowAnalyseEnterpriseVo>> getEnterpriseListByName(@RequestBody FlowAnalyseEnterpriseForm form) {
        QueryCrmEnterpriseByNamePageListRequest request=new QueryCrmEnterpriseByNamePageListRequest();
        PojoUtils.map(request,form);
        request.setName(form.getEname());
//        request.setFlowGoodsPlansFlag(true);
        request.setSupplyChainRole(1);
        Page<CrmEnterpriseDTO> crmEnterpriseSimplePage = crmEnterpriseApi.getCrmEnterpriseByName(request);
        Page<FlowAnalyseEnterpriseVo> page=new Page<>();
        page.setSize(crmEnterpriseSimplePage.getSize());
        page.setTotal(crmEnterpriseSimplePage.getTotal());
        page.setCurrent(crmEnterpriseSimplePage.getCurrent());
        page.setPages(crmEnterpriseSimplePage.getPages());
        List<FlowAnalyseEnterpriseVo> list=crmEnterpriseSimplePage.getRecords().stream().map(c->{
            FlowAnalyseEnterpriseVo vo=new FlowAnalyseEnterpriseVo();
            vo.setEid(c.getEid());
            vo.setCrmEnterpriseId(c.getId());
            vo.setEname(c.getName());
            return vo;
        }).collect(Collectors.toList());
        page.setRecords(list);
        return Result.success(page);
    }
}

