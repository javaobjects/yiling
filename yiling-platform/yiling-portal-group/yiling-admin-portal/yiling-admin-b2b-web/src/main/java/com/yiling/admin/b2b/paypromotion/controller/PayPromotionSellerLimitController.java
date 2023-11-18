package com.yiling.admin.b2b.paypromotion.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.paypromotion.from.AddPayPromotionSellerLimitForm;
import com.yiling.admin.b2b.paypromotion.from.DeletePayPromotionSellerLimitForm;
import com.yiling.admin.b2b.strategy.form.AddStrategySellerLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategySellerLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategySellerLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategySellerLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionSellerLimitActivityDTO;
import com.yiling.marketing.paypromotion.dto.request.QueryPaypromotionSellerLimitPageRequest;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 支付促销商家表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "支付促销-商家（卖家）")
@RestController
@RequestMapping("/payPromotion/limit/seller")
public class PayPromotionSellerLimitController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @DubboReference
    PayPromotionActivityApi payPromotionActivityApi;

    @ApiOperation(value = "支付促销指定商家-添加商家-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddPayPromotionSellerLimitForm form) {
        AddStrategySellerLimitRequest request = PojoUtils.map(form, AddStrategySellerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = payPromotionActivityApi.addSellerLimiti(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "支付促销指定商家-删除商家-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeletePayPromotionSellerLimitForm form) {
        DeleteStrategySellerLimitRequest request = PojoUtils.map(form, DeleteStrategySellerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = payPromotionActivityApi.deletePayPromotionSeller(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "支付促销指定商家-已添加商家列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategySellerLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategySellerLimitPageForm form) {
        QueryPaypromotionSellerLimitPageRequest request = PojoUtils.map(form, QueryPaypromotionSellerLimitPageRequest.class);
        request.setMarketingPayId(form.getMarketingStrategyId());
        Page<PayPromotionSellerLimitActivityDTO> dtoPage = payPromotionActivityApi.PayPromotionSellerPageList(request);
        Page<StrategySellerLimitVO> voPage = PojoUtils.map(dtoPage, StrategySellerLimitVO.class);
        for (StrategySellerLimitVO sellerLimitVO : voPage.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(sellerLimitVO.getEid());
            sellerLimitVO.setEname(enterpriseDTO.getName());
            sellerLimitVO.setAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
        }
        return Result.success(voPage);
    }
}
