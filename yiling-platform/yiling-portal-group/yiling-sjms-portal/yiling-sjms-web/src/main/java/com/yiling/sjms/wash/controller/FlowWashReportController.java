package com.yiling.sjms.wash.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashHospitalStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashPharmacyPurchaseReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSupplyStockReportPageRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryFlowWashHospitalStockReportPageForm;
import com.yiling.sjms.wash.form.QueryFlowWashInventoryReportPageForm;
import com.yiling.sjms.wash.form.QueryFlowWashPharmacyPurchaseReportPageForm;
import com.yiling.sjms.wash.form.QueryFlowWashSaleReportPageForm;
import com.yiling.sjms.wash.form.QueryFlowWashSupplyStockReportPageForm;
import com.yiling.sjms.wash.vo.FlowWashHospitalStockReportVO;
import com.yiling.sjms.wash.vo.FlowWashInventoryReportVO;
import com.yiling.sjms.wash.vo.FlowWashPharmacyPurchaseReportVO;
import com.yiling.sjms.wash.vo.FlowWashSaleReportVO;
import com.yiling.sjms.wash.vo.FlowWashSupplyStockReportVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 流量清洗报表
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-01
 */
@RestController
@Api(tags = "流向合并报表")
@RequestMapping("/flowWash/report/")
public class FlowWashReportController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FlowWashReportController.class);

    @DubboReference
    FlowWashReportApi flowWashReportApi;
    @DubboReference
    SjmsUserDatascopeApi userDatascopeApi;


    // 报表权限数据构建
    private Page buildReportPermission(String empId, QueryPageListForm form, Function<SjmsUserDatascopeBO.OrgPartDatascopeBO, Page> dataFunction) {

        SjmsUserDatascopeBO datascopeBO = userDatascopeApi.getByEmpId(empId);

        logger.debug(() -> "当前用户:" + empId + "可查看的企业有:" + JSONUtil.toJsonStr(datascopeBO));

        if (datascopeBO == null) {

            return new Page(1, form.getSize());
        }

        Page result;

        switch (OrgDatascopeEnum.getFromCode(datascopeBO.getOrgDatascope())) {
            case ALL:
                result = dataFunction.apply(null);
                break;
            case PORTION:
                result = dataFunction.apply(datascopeBO.getOrgPartDatascopeBO());
                break;
            default:
                result = new Page(1, form.getSize());
                break;
        }

        return result;
    }


    @ApiOperation(value = "销售流向合并报表")
    @PostMapping("/sale/list")
    public Result<Page<FlowWashSaleReportVO>> queryFlowWashSaleReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowWashSaleReportPageForm form) {

        FlowWashSaleReportPageRequest request = PojoUtils.map(form, FlowWashSaleReportPageRequest.class);
        Date date = DateUtil.parse(form.getSoMonth(), "yyyy-MM");
        request.setYear(StrUtil.toString(DateUtil.year(date)));
        request.setMonth(StrUtil.toString(DateUtil.month(date) + 1));

        Function<SjmsUserDatascopeBO.OrgPartDatascopeBO, Page> dataFunction = (t) -> {

            request.setSupplierProvinceNameList(Optional.ofNullable(t).map(z -> z.getProvinceNames()).orElse(null));
            request.setCrmIdList(Optional.ofNullable(t).map(z -> z.getCrmEids()).orElse(null));

            Page<FlowWashSaleReportDTO> result = flowWashReportApi.saleReportPageList(request);

            if (result == null || result.getSize() == 0) {

                return new Page<FlowWashSaleReportVO> (request.getCurrent(),request.getSize(),result.getTotal());
            }

            List<FlowWashSaleReportVO> reportVOList = result.getRecords().stream().map(z -> {

                FlowWashSaleReportVO vo = PojoUtils.map(z,FlowWashSaleReportVO.class);
                vo.setFlowSaleWashId(z.getFlowKey());

                return vo;

            }).collect(Collectors.toList());

            Page<FlowWashSaleReportVO> reportPage = new Page<>(request.getCurrent(), request.getSize(),result.getTotal());
            reportPage.setRecords(reportVOList);

            return reportPage;
        };

        return Result.success(this.buildReportPermission(userInfo.getCurrentUserCode(), form, dataFunction));

    }

    @ApiOperation(value = "库存流向合并报表查询")
    @PostMapping("/inventory/list")
    public Result<Page<FlowWashInventoryReportVO>> queryFlowWashInventoryReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowWashInventoryReportPageForm form) {

        FlowWashInventoryReportPageRequest request = PojoUtils.map(form, FlowWashInventoryReportPageRequest.class);
        Function<SjmsUserDatascopeBO.OrgPartDatascopeBO, Page> dataFunction = (t) -> {

            request.setProvinceNameList(Optional.ofNullable(t).map(z -> z.getProvinceNames()).orElse(null));
            request.setCrmIdList(Optional.ofNullable(t).map(z -> z.getCrmEids()).orElse(null));

            Page result = flowWashReportApi.inventoryReportPageList(request);

            return PojoUtils.map(result, FlowWashInventoryReportVO.class);
        };

        return Result.success(this.buildReportPermission(userInfo.getCurrentUserCode(), form, dataFunction));

    }

    @ApiOperation(value = "商业进销存报表")
    @PostMapping("/supply/stock/list")
    public Result<Page<FlowWashSupplyStockReportVO>> queryFlowWashSupplyStockReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowWashSupplyStockReportPageForm form) {
        FlowWashSupplyStockReportPageRequest request = PojoUtils.map(form, FlowWashSupplyStockReportPageRequest.class);
        request.setHideFlag(Constants.IS_NO);

        Function<SjmsUserDatascopeBO.OrgPartDatascopeBO, Page> dataFunction = (t) -> {

            request.setProvinceNameList(Optional.ofNullable(t).map(z -> z.getProvinceNames()).orElse(null));
            request.setCrmIdList(Optional.ofNullable(t).map(z -> z.getCrmEids()).orElse(null));

            Page result = flowWashReportApi.supplyStockReportPageList(request);

            return PojoUtils.map(result, FlowWashSupplyStockReportVO.class);
        };

        return Result.success(this.buildReportPermission(userInfo.getCurrentUserCode(), form, dataFunction));
    }


    @ApiOperation(value = "医疗进销存报表")
    @PostMapping("/hospital/stock/list")
    public Result<Page<FlowWashHospitalStockReportVO>> queryFlowWashHospitalStockReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowWashHospitalStockReportPageForm form) {

        FlowWashHospitalStockReportPageRequest request = PojoUtils.map(form, FlowWashHospitalStockReportPageRequest.class);
        request.setHideFlag(Constants.IS_NO);

        Function<SjmsUserDatascopeBO.OrgPartDatascopeBO, Page> dataFunction = (t) -> {

            request.setProvinceNameList(Optional.ofNullable(t).map(z -> z.getProvinceNames()).orElse(null));
            request.setCrmIdList(Optional.ofNullable(t).map(z -> z.getCrmEids()).orElse(null));

            Page result = flowWashReportApi.hospitalStockReportPageList(request);

            return PojoUtils.map(result, FlowWashHospitalStockReportVO.class);
        };

        return Result.success(this.buildReportPermission(userInfo.getCurrentUserCode(), form, dataFunction));
    }

    @ApiOperation(value = "零售购进报表")
    @PostMapping("/pharmacy/purchase/list")
    public Result<Page<FlowWashPharmacyPurchaseReportVO>> queryFlowWashPharmacyPurchaseReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowWashPharmacyPurchaseReportPageForm form) {

        FlowWashPharmacyPurchaseReportPageRequest request = PojoUtils.map(form, FlowWashPharmacyPurchaseReportPageRequest.class);

        Function<SjmsUserDatascopeBO.OrgPartDatascopeBO, Page> dataFunction = (t) -> {

            request.setProvinceNameList(Optional.ofNullable(t).map(z -> z.getProvinceNames()).orElse(null));
            request.setCrmIdList(Optional.ofNullable(t).map(z -> z.getCrmEids()).orElse(null));

            Page result = flowWashReportApi.pharmacyPurchaseReportPageList(request);

            return PojoUtils.map(result, FlowWashPharmacyPurchaseReportVO.class);
        };

        return Result.success(this.buildReportPermission(userInfo.getCurrentUserCode(), form, dataFunction));
    }

}
