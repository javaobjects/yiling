package com.yiling.b2b.app.paymentdays.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.paymentdays.vo.PaymentDaysListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B移动端-账期 Controller
 *
 * @author lun.yu
 * @date 2021/11/9
 */
@RestController
@RequestMapping("/paymentDays")
@Api(tags = "账期接口")
@Slf4j
public class PaymentDaysAccountController extends BaseController {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;

    @ApiOperation(value = "账期额度列表")
    @PostMapping("/queryPaymentDaysList")
    public Result<CollectionObject<PaymentDaysListItemVO>> queryPaymentDaysPage(@CurrentUser CurrentStaffInfo staffInfo){
        List<PaymentDaysListItemVO> list = PojoUtils.map(paymentDaysAccountApi.getByCustomerEid(staffInfo.getCurrentEid()), PaymentDaysListItemVO.class);

        list.forEach(paymentDaysListItemVO -> {
            //待还款额度
            paymentDaysListItemVO.setNeedRepaymentAmount(paymentDaysListItemVO.getUsedAmount().subtract(paymentDaysListItemVO.getRepaymentAmount()));
        });

        return Result.success(new CollectionObject<>(list));
    }



}
