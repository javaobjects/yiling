package com.yiling.admin.b2b.strategy.controller;


import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.marketing.strategy.api.StrategyGiftApi;

/**
 * <p>
 * 营销活动赠品表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/strategy/gift")
public class StrategyGiftController extends BaseController {

    @DubboReference
    StrategyGiftApi strategyGiftApi;

//    @ApiOperation(value = "赠品保存")
//    @PostMapping("/save")
//    public Result<Object> save(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveStrategyGiftForm form) {
//        SaveStrategyGiftRequest request = PojoUtils.map(form, SaveStrategyGiftRequest.class);
//        request.setOpUserId(staffInfo.getCurrentUserId());
//        boolean isSuccess = strategyGiftApi.save(request);
//        if (isSuccess) {
//            return Result.success();
//        }
//        return Result.failed("保存失败");
//    }
}
