package com.yiling.admin.b2b.integral.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.QueryIntegralExchangeGoodsPageForm;
import com.yiling.admin.b2b.integral.form.SaveIntegralExchangeGoodsForm;
import com.yiling.admin.b2b.integral.form.UpdateShelfStatusForm;
import com.yiling.admin.b2b.integral.vo.IntegralExchangeGoodsDetailVO;
import com.yiling.admin.b2b.integral.vo.IntegralExchangeGoodsItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.bo.IntegralExchangeGoodsDetailBO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeGoodsPageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralExchangeGoodsRequest;
import com.yiling.user.integral.dto.request.UpdateShelfStatusRequest;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换商品 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
@Slf4j
@RestController
@RequestMapping("/integralExchangeGoods")
@Api(tags = "积分兑换商品接口")
public class IntegralExchangeGoodsController extends BaseController {

    @DubboReference
    com.yiling.user.integral.api.IntegralExchangeGoodsApi integralExchangeGoodsApi;
    @DubboReference
    com.yiling.marketing.integral.api.IntegralExchangeGoodsApi integralExchangeGoodsMarketingApi;

    @ApiOperation(value = "积分兑换商品分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralExchangeGoodsItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralExchangeGoodsPageForm form) {
        QueryIntegralExchangeGoodsPageRequest request = PojoUtils.map(form, QueryIntegralExchangeGoodsPageRequest.class);
        request.setOrderCond(1);
        Page<IntegralExchangeGoodsItemVO> page = PojoUtils.map(integralExchangeGoodsApi.queryListPage(request), IntegralExchangeGoodsItemVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "积分兑换商品上下架")
    @PostMapping("/updateShelfStatus")
    @Log(title = "积分兑换商品上下架", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateShelfStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateShelfStatusForm form) {
        UpdateShelfStatusRequest request = PojoUtils.map(form, UpdateShelfStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeGoodsApi.updateStatus(request));
    }

    @ApiOperation(value = "保存积分兑换商品")
    @PostMapping("/save")
    @Log(title = "保存积分兑换商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralExchangeGoodsForm form) {
        SaveIntegralExchangeGoodsRequest request = PojoUtils.map(form, SaveIntegralExchangeGoodsRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeGoodsApi.saveExchangeGoods(request));
    }

    @ApiOperation(value = "查看")
    @GetMapping("/get")
    public Result<IntegralExchangeGoodsDetailVO> get(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        IntegralExchangeGoodsDetailBO exchangeGoodsDetailBO  = integralExchangeGoodsMarketingApi.getDetail(id);
        return Result.success(PojoUtils.map(exchangeGoodsDetailBO, IntegralExchangeGoodsDetailVO.class));
    }

}
