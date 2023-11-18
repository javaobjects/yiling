package com.yiling.admin.data.center.report.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.data.center.report.form.QueryFlowPurchaseListForm;
import com.yiling.admin.data.center.report.form.QueryPurchaseGoodsListForm;
import com.yiling.admin.data.center.report.vo.EnterpriseInfoVO;
import com.yiling.admin.data.center.report.vo.FlowPurchaseDetailVO;
import com.yiling.admin.data.center.report.vo.FlowPurchaseGoodsMonthVO;
import com.yiling.admin.data.center.report.vo.FlowPurchaseMonthVO;
import com.yiling.admin.data.center.report.vo.StorageInfoVO;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseGoodsDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseMonthDTO;
import com.yiling.dataflow.order.dto.StorageInfoDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/7/1
 */
@RestController
@RequestMapping("/report/flow/purchase")
@Api(tags = "ERP采购分析数据大屏")
@Slf4j
public class FlowPurchaseController extends BaseController {

    @DubboReference(timeout = 30 * 1000)
    FlowPurchaseApi flowPurchaseApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    DictDataApi dictDataApi;

    @ApiOperation(value = "获取采购数据中所有商业公司列表")
    @GetMapping(value = "/enterprise/list")
    public Result<List<EnterpriseInfoVO>> listPurchaseEnterprise(@RequestParam(value = "channelId", required = false) Integer channelId) {
        List<EnterpriseInfoVO> enterpriseInfoVOList = new ArrayList<>();

        List<FlowPurchaseDTO> flowPurchaseDTOList = flowPurchaseApi.getFlowPurchaseEnterpriseList(channelId);
        for (FlowPurchaseDTO flowPurchaseDTO : flowPurchaseDTOList) {
            EnterpriseInfoVO enterpriseInfoVO = new EnterpriseInfoVO();
            enterpriseInfoVO.setSuId(flowPurchaseDTO.getEid());
            enterpriseInfoVO.setClientName(flowPurchaseDTO.getEname());
            enterpriseInfoVOList.add(enterpriseInfoVO);
        }
        return Result.success(enterpriseInfoVOList);
    }


    @ApiOperation(value = "获取采购数据中所有供应商的列表")
    @GetMapping(value = "/supplier/list")
    public Result<List<EnterpriseInfoVO>> listSupplier(@RequestParam(value = "channelId", required = false) Integer channelId) {
        List<EnterpriseInfoVO> enterpriseInfoVOList = new ArrayList<>();

        List<FlowPurchaseDTO> flowPurchaseDTOList = flowPurchaseApi.getFlowPurchaseSupplierList(channelId);
        for (FlowPurchaseDTO flowPurchaseDTO : flowPurchaseDTOList) {
            EnterpriseInfoVO enterpriseInfoVO = new EnterpriseInfoVO();
            enterpriseInfoVO.setSuId(flowPurchaseDTO.getSupplierId());
            enterpriseInfoVO.setClientName(flowPurchaseDTO.getEnterpriseName());
            enterpriseInfoVOList.add(enterpriseInfoVO);
        }
        return Result.success(enterpriseInfoVOList);
    }

    @ApiOperation(value = "获取ERP采购商采购入库表")
    @PostMapping (value = "/list")
    public Result<Map<String, Object>> list(@RequestBody QueryFlowPurchaseListForm from) {

        QueryFlowPurchaseListRequest request = PojoUtils.map(from, QueryFlowPurchaseListRequest.class);
        if (StringUtils.isEmpty(request.getTime())) {
            request.setPoMonthList(generatePoMonthList());
        } else {
            List<String> poMonthList = new ArrayList<>();
            poMonthList.add(request.getTime());
            request.setPoMonthList(poMonthList);
        }

        List<FlowPurchaseMonthDTO> flowPurchaseMonthDTOList = flowPurchaseApi.getFlowPurchaseMonthList(request);

        List<FlowPurchaseMonthVO> list = new ArrayList<>();

        // 匹配渠道类型
        if (flowPurchaseMonthDTOList != null && flowPurchaseMonthDTOList.size() != 0) {
            List<DictDataBO> dictDataBOList = dictDataApi.getEnabledByTypeIdList(1L);
            for (FlowPurchaseMonthDTO flowPurchaseMonthDTO : flowPurchaseMonthDTOList) {
                FlowPurchaseMonthVO flowPurchaseMonthVO = PojoUtils.map(flowPurchaseMonthDTO, FlowPurchaseMonthVO.class);
                dictDataBOList.stream().filter(d -> flowPurchaseMonthDTO.getPurchaseChannelId() != null && d.getValue().equals(String.valueOf(flowPurchaseMonthDTO.getPurchaseChannelId())))
                        .findAny()
                        .ifPresent(dictDataBO -> flowPurchaseMonthVO.setPurchaseChannelDesc(dictDataBO.getLabel()));
                dictDataBOList.stream().filter(d -> flowPurchaseMonthDTO.getPurchaseChannelId() != null && d.getValue().equals(String.valueOf(flowPurchaseMonthDTO.getSupplierChannelId())))
                        .findAny()
                        .ifPresent(dictDataBO -> flowPurchaseMonthVO.setSupplierChannelDesc(dictDataBO.getLabel()));

                //  金额单位换算为百万
                for (Map.Entry<String, StorageInfoVO> entry : flowPurchaseMonthVO.getStorageInfoMap().entrySet()) {
                    StorageInfoVO storageInfoVO= entry.getValue();
                    if (storageInfoVO.getStorageMoney() != null) {
                        storageInfoVO.setStorageMoney(storageInfoVO.getStorageMoney().divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP));
                    }
                }
                if (flowPurchaseMonthVO.getStorageMoneySum() != null) {
                    flowPurchaseMonthVO.setStorageMoneySum(flowPurchaseMonthVO.getStorageMoneySum().divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP));
                }

                list.add(flowPurchaseMonthVO);
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("monthList", request.getPoMonthList());
        resultMap.put("dataList", list);

        return Result.success(resultMap);
    }

    @ApiOperation(value = "获取商业采购入库明细表")
    @GetMapping (value = "/detail")
    public Result<FlowPurchaseDetailVO> flowPurchaseDetail(
            @RequestParam("purchaseEnterpriseId") Long purchaseEnterpriseId,
            @RequestParam(value = "supplierEnterpriseId", required = false) Long supplierEnterpriseId,
            @RequestParam(value = "time", required = false) String time) {
        FlowPurchaseDetailVO flowPurchaseDetailVO = new FlowPurchaseDetailVO();

        // 获取企业信息
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(purchaseEnterpriseId);
        flowPurchaseDetailVO.setPurchaseEnterpriseId(enterpriseDTO.getId());
        flowPurchaseDetailVO.setPurchaseEnterpriseName(enterpriseDTO.getName());

        List<DictDataBO> dictDataBOList = dictDataApi.getEnabledByTypeIdList(1L);
        dictDataBOList.stream().filter(d -> d.getValue().equals(String.valueOf(enterpriseDTO.getChannelId()))).findAny()
                .ifPresent(dictDataBO -> {
                    flowPurchaseDetailVO.setChannelId(enterpriseDTO.getChannelId());
                    flowPurchaseDetailVO.setChannelDesc(dictDataBO.getLabel());
                });

        // 查询明细
        List<FlowPurchaseDetailDTO> flowPurchaseDetailDTOList = flowPurchaseApi.getFlowPurchaseDetail(purchaseEnterpriseId, supplierEnterpriseId, time);

        BigDecimal quantitySum = new BigDecimal(0);
        BigDecimal moneySum = new BigDecimal(0);
        List<StorageInfoVO> storageInfoVOList = new ArrayList<>();
        for (FlowPurchaseDetailDTO flowPurchaseDetailDTO : flowPurchaseDetailDTOList) {
            StorageInfoVO storageInfoVO = new StorageInfoVO();
            storageInfoVO.setTime(flowPurchaseDetailDTO.getPoDay());
            storageInfoVO.setStorageQuantity(flowPurchaseDetailDTO.getPoQuantity());
            storageInfoVO.setStorageMoney(flowPurchaseDetailDTO.getPoMoney());

            quantitySum = quantitySum.add(flowPurchaseDetailDTO.getPoQuantity());
            moneySum =  moneySum.add(flowPurchaseDetailDTO.getPoMoney());

            // 金额单位换算为万
            if (storageInfoVO.getStorageMoney() != null) {
                storageInfoVO.setStorageMoney(storageInfoVO.getStorageMoney().divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP));
            }

            storageInfoVOList.add(storageInfoVO);
        }
        StorageInfoVO storageInfoVO = new StorageInfoVO();
        storageInfoVO.setStorageMoney(moneySum);
        if (storageInfoVO.getStorageMoney() != null) {
            storageInfoVO.setStorageMoney(storageInfoVO.getStorageMoney().divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP));
        }
        storageInfoVO.setStorageQuantity(quantitySum);
        storageInfoVOList.add(0, storageInfoVO);

        flowPurchaseDetailVO.setStorageInfoList(storageInfoVOList);

        return Result.success(flowPurchaseDetailVO);
    }

    @ApiOperation(value = "获取采购商品名称列表")
    @PostMapping(value = "/goodsName/list")
    public Result<List<String>> listPurchaseGoodsName() {
        return Result.success(flowPurchaseApi.getPurchaseGoodsNameList());
    }

    @ApiOperation(value = "采购商品入库表")
    @PostMapping(value = "/goods/list")
    public Result<Map<String, Object>> listFlowPurchaseGoods(@RequestBody QueryPurchaseGoodsListForm queryPurchaseGoodsListForm) {
        QueryPurchaseGoodsListRequest request = PojoUtils.map(queryPurchaseGoodsListForm, QueryPurchaseGoodsListRequest.class);
        if (StringUtils.isEmpty(request.getTime())) {
            // 生成时间格式，以当前月份往前推6个月，取6个月的数
            request.setPoMonthList(generatePoMonthList());
        } else {
            List<String> poMonthList = new ArrayList<>();
            poMonthList.add(request.getTime());
            request.setPoMonthList(poMonthList);
        }

        List<FlowPurchaseGoodsDTO> flowPurchaseGoodsDTOList = flowPurchaseApi.getFlowPurchaseGoodsList(request);

        List<FlowPurchaseGoodsMonthVO> flowPurchaseGoodsMonthVOList = new ArrayList<>();

        if (flowPurchaseGoodsDTOList != null && flowPurchaseGoodsDTOList.size() != 0) {
            for (FlowPurchaseGoodsDTO flowPurchaseGoodsDTO : flowPurchaseGoodsDTOList) {
                FlowPurchaseGoodsMonthVO flowPurchaseGoodsMonthVO = PojoUtils.map(flowPurchaseGoodsDTO, FlowPurchaseGoodsMonthVO.class);
                flowPurchaseGoodsMonthVOList.add(flowPurchaseGoodsMonthVO);
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("monthList", request.getPoMonthList());
        resultMap.put("dataList", flowPurchaseGoodsMonthVOList);

        return Result.success(resultMap);
    }

    private List<String> generatePoMonthList() {
        List<Date> dateMonthList = new ArrayList<>();
        Date dateMonth = getMonthDate();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        for (int i = 0; i < 6; i++) {
            calendar.setTime(dateMonth);
            calendar.add(Calendar.MONTH, -i);
            dateMonthList.add(calendar.getTime());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return dateMonthList.stream().map(sdf::format).collect(Collectors.toList());
    }

    private Date getMonthDate() {
        return getMonthDate(null);
    }

    private Date getMonthDate(String monthStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date monthDate = null;
        if (StringUtils.isEmpty(monthStr)) {
            Date date = new Date();
            monthStr = sdf.format(date);
        }

        try {
            monthDate = sdf.parse(monthStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return monthDate;
    }


}
