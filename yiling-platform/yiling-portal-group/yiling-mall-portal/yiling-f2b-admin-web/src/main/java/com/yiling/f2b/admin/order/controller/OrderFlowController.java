package com.yiling.f2b.admin.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.order.form.QueryOrderFlowForm;
import com.yiling.f2b.admin.order.vo.OrderFlowVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.api.OrderFlowApi;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: shuang.zhang
 * @date: 2021/9/13
 */
@RestController
@Api(tags = "流向模块")
@RequestMapping("/flow")
public class OrderFlowController {

    @DubboReference
    OrderFlowApi orderFlowApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "以岭流向列表")
    @PostMapping("/yiling/page")
    public Result<Page<OrderFlowVO>> getYilingPage(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryOrderFlowForm form) {
        QueryOrderFlowRequest request = PojoUtils.map(form, QueryOrderFlowRequest.class);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        Page<OrderFlowBO> page = orderFlowApi.getPageList(request);
        return Result.success(PojoUtils.map(page, OrderFlowVO.class));
    }

    @ApiOperation(value = "供应商流向列表")
    @PostMapping("/seller/page")
    public Result<Page<OrderFlowVO>> getSellerPage(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryOrderFlowForm form) {
        QueryOrderFlowRequest request = PojoUtils.map(form, QueryOrderFlowRequest.class);
        List<Long> eidList = new ArrayList<>();
        List<Long> industryIds = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        eidList.addAll(industryIds);
        List<Long> level1Ids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.LEVEL_1);
        eidList.addAll(level1Ids);
        List<Long> level2Ids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.LEVEL_2);
        eidList.addAll(level2Ids);
        request.setEidList(eidList);
        Page<OrderFlowBO> page = orderFlowApi.getPageList(request);
        return Result.success(PojoUtils.map(page, OrderFlowVO.class));
    }
}
