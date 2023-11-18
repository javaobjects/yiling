package com.yiling.sjms.flow.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.search.flow.api.EsFlowPurchaseApi;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;
import com.yiling.search.flow.dto.request.EsFlowPurchaseSearchRequest;
import com.yiling.sjms.flow.form.QueryFlowPageForm;
import com.yiling.sjms.flow.vo.FlowPurchasePageVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author fucheng.bai
 * @date 2023/2/6
 */
@RestController
@RequestMapping(value = "/flowPurchase")
@Api(tags = "日采购详情")
public class FlowPurchaseController {

    @DubboReference
    private FlowPurchaseApi flowPurchaseApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @DubboReference(timeout = 1000*30)
    private EsFlowPurchaseApi esFlowPurchaseApi;

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    ErpClientApi erpClientApi;

    @ApiOperation(value = "采购流向信息列表分页", httpMethod = "POST")
    @PostMapping("/listPage")
    public Result<Page<FlowPurchasePageVO>> listPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowPageForm form) {
        if (userInfo == null) {
            return Result.success(new Page<>());
        }

        //  查询权限
        SjmsUserDatascopeBO datascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        if (datascopeBO == null || OrgDatascopeEnum.NONE.getCode().equals(datascopeBO.getOrgDatascope())) {
            return Result.success(new Page<>());
        }

        List<String> provinceCodeList = new ArrayList<>();
        List<Long> eidList = new ArrayList<>();
        if (OrgDatascopeEnum.PORTION.getCode().equals(datascopeBO.getOrgDatascope())) {
            provinceCodeList = datascopeBO.getOrgPartDatascopeBO().getProvinceCodes();  // 省区
            List<Long> crmEidList = datascopeBO.getOrgPartDatascopeBO().getCrmEids();   // 机构id
            if (CollUtil.isNotEmpty(crmEidList)) {
                List<ErpClientDTO> erpClientList = erpClientApi.getWithDatascopeByCrmEnterpriseIdList(crmEidList);
                List<Long> eids = erpClientList.stream().filter(o -> ObjectUtil.isNotNull(o.getRkSuId()) && o.getRkSuId().intValue() > 0).map(ErpClientDTO::getRkSuId).distinct().collect(Collectors.toList());
                if (CollUtil.isEmpty(provinceCodeList) && CollUtil.isEmpty(eids)) {
                    // 如果省区列表为空，并且映射后的eid为空，则判定无权限，返回空
                    return Result.success(new Page<>());
                }
                eidList.addAll(eids);
            }
        }

        // 若有经销商查询条件，将crmid转化为eid
        Long eid = null;
        if (form.getCrmEnterpriseId() != null) {
            ErpClientDTO erpClientDTO = erpClientApi.getByCrmEnterpriseId(form.getCrmEnterpriseId());
            if (erpClientDTO == null || erpClientDTO.getRkSuId() <= 0) {
                return Result.success(new Page<>());
            }
            eid = erpClientDTO.getRkSuId();
        }

        EsFlowPurchaseSearchRequest request = new EsFlowPurchaseSearchRequest();
        request.setEid(eid);
        request.setEids(eidList);
        request.setProvinceCodes(provinceCodeList);
        request.setSupplierLevel(form.getSupplierLevel());
        request.setStartTime(DateUtil.beginOfDay(form.getStartTime()));
        request.setEndTime(DateUtil.endOfDay(form.getEndTime()));
        request.setEnterpriseName(form.getEnterpriseName());
        request.setGoodsName(form.getGoodsName());
        request.setPoSpecifications(form.getGoodsSpec());
        request.setDataTags(form.getDataTags());
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
//        request.setDelFlag(0);
        EsAggregationDTO<EsFlowPurchaseDTO> result = esFlowPurchaseApi.searchFlowPurchase(request);
        List<FlowPurchasePageVO> list = PojoUtils.map(result.getData(), FlowPurchasePageVO.class);
        Page<FlowPurchasePageVO> page = new Page<>(result.getCurrent(), request.getSize(), result.getTotal());
        page.setRecords(list);

        for (FlowPurchasePageVO flowPurchasePageVO : page.getRecords()) {
            BigDecimal poQuantity = flowPurchasePageVO.getPoQuantity();
            BigDecimal poPrice = flowPurchasePageVO.getPoPrice();
            if (poQuantity != null && poPrice != null) {
                flowPurchasePageVO.setPoTotalAmount(poQuantity.multiply(poPrice));
            }
        }
        return Result.success(page);
    }


    private void checkStartAndEndDate(Date startTime, Date endTime) {
        DateTime startDate = DateUtil.beginOfDay(startTime);
        DateTime endDate = DateUtil.endOfDay(endTime);
        long offset = DateUtil.betweenDay(startDate, endDate, false) + 1;
        if (offset > 93) {
            throw new BusinessException(ResultCode.FAILED, "开始日期、结束日期，时间范围不能大于90天");
        }
    }


}
