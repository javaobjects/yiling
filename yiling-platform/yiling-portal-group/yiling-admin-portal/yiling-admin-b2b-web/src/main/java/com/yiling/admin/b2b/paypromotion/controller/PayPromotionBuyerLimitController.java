package com.yiling.admin.b2b.paypromotion.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.strategy.form.AddStrategyBuyerLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyBuyerLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryEnterprisePageListForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyBuyerLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.EnterpriseVO;
import com.yiling.admin.b2b.strategy.vo.StrategyBuyerLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.paypromotion.dto.PayPromotionBuyerLimitDTO;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.dto.StrategyBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 支付促销客户 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "支付促销-客户接口（买家）")
@RestController
@RequestMapping("/payPromption/limit/buyer")
public class PayPromotionBuyerLimitController extends BaseController {

    @DubboReference
    StrategyBuyerApi strategyBuyerApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "支付促销客户-已添加客户列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyBuyerLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryStrategyBuyerLimitPageForm form) {
        QueryStrategyBuyerLimitPageRequest request = PojoUtils.map(form, QueryStrategyBuyerLimitPageRequest.class);
        Page<PayPromotionBuyerLimitDTO> dtoPage = strategyBuyerApi.pageListForPromotion(request);
        Page<StrategyBuyerLimitVO> voPage = PojoUtils.map(dtoPage, StrategyBuyerLimitVO.class);
        if(CollectionUtils.isNotEmpty(voPage.getRecords())){
            voPage.getRecords().forEach(item->{
                item.setMarketingStrategyId(item.getMarketingPayId());
            });
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "支付促销客户-添加客户-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyBuyerLimitForm form) {
        AddStrategyBuyerLimitRequest request = PojoUtils.map(form, AddStrategyBuyerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyBuyerApi.addForPayPromotion(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "支付促销客户-删除客户-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategyBuyerLimitForm form) {
        DeleteStrategyBuyerLimitRequest request = PojoUtils.map(form, DeleteStrategyBuyerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyBuyerApi.deleteForPayPromotion(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
