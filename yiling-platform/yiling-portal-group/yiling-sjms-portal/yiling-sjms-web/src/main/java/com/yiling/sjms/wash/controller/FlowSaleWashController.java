package com.yiling.sjms.wash.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowSaleWashApi;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryFlowSaleWashPageForm;
import com.yiling.sjms.wash.vo.FlowSaleWashListVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Slf4j
@RestController
@RequestMapping("/flowSaleWash")
@Api(tags = "月销售清洗明细")
public class FlowSaleWashController extends BaseController {

    @DubboReference
    private FlowSaleWashApi flowSaleWashApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @ApiOperation(value = "月销售清洗明细列表")
    @PostMapping("/list")
    public Result<Page<FlowSaleWashListVO>> queryFlowSaleWashListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowSaleWashPageForm form) {
        if (userInfo == null) {
            return Result.success(new Page<>());
        }

        //  查询权限
        SjmsUserDatascopeBO datascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        if (datascopeBO == null || OrgDatascopeEnum.NONE.getCode().equals(datascopeBO.getOrgDatascope())) {
            return Result.success(new Page<>());
        }

        QueryFlowSaleWashPageRequest request = PojoUtils.map(form, QueryFlowSaleWashPageRequest.class);
        // 查看是否查询经销商参数
//        if (OrgDatascopeEnum.PORTION.getCode().equals(datascopeBO.getOrgDatascope())) {
//            request.setProvinceCodes(datascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
//            request.setCrmEnterpriseIdList(datascopeBO.getOrgPartDatascopeBO().getCrmEids());
//        }

        Page<FlowSaleWashListVO> resultPage = PojoUtils.map(flowSaleWashApi.listPage(request), FlowSaleWashListVO.class);
        for (FlowSaleWashListVO flowSaleWashListVO : resultPage.getRecords()) {
            BigDecimal soQuantity = flowSaleWashListVO.getSoQuantity();
            BigDecimal soPrice = flowSaleWashListVO.getSoPrice();
            flowSaleWashListVO.setPoTotalAmount(soQuantity.multiply(soPrice));
        }
        return Result.success(PojoUtils.map(resultPage, FlowSaleWashListVO.class));
    }
}
