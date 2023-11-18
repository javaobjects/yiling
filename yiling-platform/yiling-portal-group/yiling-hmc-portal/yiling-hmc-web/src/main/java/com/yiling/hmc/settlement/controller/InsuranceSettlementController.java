package com.yiling.hmc.settlement.controller;


import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.settlement.api.InsuranceSettlementApi;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementCallbackRequest;
import com.yiling.hmc.settlement.form.InsuranceSettlementCallbackForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 保司结账表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Api(tags = "保司结算接口")
@Slf4j
@RestController
@RequestMapping("/settlement/insurance")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceSettlementController extends BaseController {

    @DubboReference
    InsuranceSettlementApi insuranceSettlementApi;

    private final RedisDistributedLock redisDistributedLock;

    @ApiOperation(value = "保司结算回调以岭接口")
    @PostMapping("/callback")
    public Result callback(@RequestBody @Valid InsuranceSettlementCallbackForm form) {
        InsuranceSettlementCallbackRequest request = PojoUtils.map(form, InsuranceSettlementCallbackRequest.class);
        String lockName = RedisKey.generate("tkCallback", request.getOrderNo());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            boolean isSuccess = insuranceSettlementApi.callback(request);
            if (isSuccess) {
                return Result.success();
            } else {
                return Result.failed("结算失败");
            }
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

}
