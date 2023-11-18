package com.yiling.sales.assistant.app.order.controller;

import java.util.Date;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.dto.request.B2BOrderReturnApplyRequest;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.sales.assistant.app.order.form.OrderReturnApplyForm;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 退货单模块
 * 
 * @author: yong.zhang
 * @date: 2022/1/6
 */
@RestController
@RequestMapping("/return")
@Api(tags = "退货单模块")
@Slf4j
public class OrderReturnController extends BaseController {
    @DubboReference
    OrderProcessApi orderProcessApi;

    @ApiOperation(value = "退货单申请")
    @PostMapping("/apply")
    public Result<Object> apply(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderReturnApplyForm form) {
        B2BOrderReturnApplyRequest request = PojoUtils.map(form, B2BOrderReturnApplyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setReturnSource(ReturnSourceEnum.SA.getCode());
        Boolean result = orderProcessApi.applySaOrderReturn(request);
        return Result.success(result);
    }
}
