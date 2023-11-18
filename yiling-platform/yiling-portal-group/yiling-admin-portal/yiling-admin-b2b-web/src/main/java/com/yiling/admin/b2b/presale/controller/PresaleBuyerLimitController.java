package com.yiling.admin.b2b.presale.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.presale.vo.PresaleBuyerLimitVO;
import com.yiling.admin.b2b.strategy.form.AddPresaleBuyerLimitForm;
import com.yiling.admin.b2b.strategy.form.AddStrategyBuyerLimitForm;
import com.yiling.admin.b2b.strategy.form.DeletePresaleBuyerLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyBuyerLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryEnterprisePageListForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyBuyerLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.EnterpriseVO;
import com.yiling.admin.b2b.strategy.vo.StrategyBuyerLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.dto.PresaleBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleBuyerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 策略满赠客户 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "预售-客户")
@RestController
@RequestMapping("/presaleBuyer/limit/buyer")
public class PresaleBuyerLimitController extends BaseController {

    @DubboReference
    StrategyBuyerApi strategyBuyerApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "策略满赠客户-已添加客户列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PresaleBuyerLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryStrategyBuyerLimitPageForm form) {
        QueryPresaleBuyerLimitPageRequest request = PojoUtils.map(form, QueryPresaleBuyerLimitPageRequest.class);
        request.setMarketingPresaleId(form.getMarketingStrategyId());
        Page<PresaleBuyerLimitDTO> dtoPage = strategyBuyerApi.pageListForPresale(request);
        Page<PresaleBuyerLimitVO> voPage = PojoUtils.map(dtoPage, PresaleBuyerLimitVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "预售客户-添加客户-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddPresaleBuyerLimitForm form) {
        AddPresaleBuyerLimitRequest request = PojoUtils.map(form, AddPresaleBuyerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyBuyerApi.addForPresale(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "预售客户-删除客户-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeletePresaleBuyerLimitForm form) {
        DeletePresaleBuyerLimitRequest request = PojoUtils.map(form, DeletePresaleBuyerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyBuyerApi.deleteForPresale(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
