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
import com.yiling.admin.b2b.integral.form.QueryIntegralRulePageForm;
import com.yiling.admin.b2b.integral.form.SaveIntegralGiveRuleBasicForm;
import com.yiling.admin.b2b.integral.form.SaveIntegralLotteryConfigForm;
import com.yiling.admin.b2b.integral.vo.IntegralRuleListItemVO;
import com.yiling.admin.b2b.integral.vo.IntegralRuleVO;
import com.yiling.admin.b2b.integral.vo.IntegralUseRuleDetailVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralUseRuleApi;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralLotteryConfigRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分消耗规则 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-06
 */
@Slf4j
@RestController
@RequestMapping("/integralUseRule")
@Api(tags = "积分消耗规则接口")
public class IntegralUseRuleController extends BaseController {

    @DubboReference
    IntegralUseRuleApi integralUseRuleApi;
    @DubboReference
    com.yiling.marketing.integral.api.IntegralUseRuleApi integralUseRuleMarketingApi;

    @ApiOperation(value = "积分消耗规则分页")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralRuleListItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralRulePageForm form) {
        QueryIntegralRulePageRequest request = PojoUtils.map(form, QueryIntegralRulePageRequest.class);
        Page<IntegralRuleListItemVO> page = PojoUtils.map(integralUseRuleApi.queryListPage(request), IntegralRuleListItemVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "停用规则")
    @GetMapping("/updateStatus")
    @Log(title = "停用规则", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateStatus(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        UpdateRuleStatusRequest request = new UpdateRuleStatusRequest();
        request.setId(id);
        request.setStatus(IntegralRuleStatusEnum.DISABLED.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralUseRuleApi.updateStatus(request));
    }

    @ApiOperation(value = "保存积分消耗规则基本信息")
    @PostMapping("/saveBasic")
    @Log(title = "保存积分消耗规则基本信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<IntegralRuleVO> saveBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralGiveRuleBasicForm form) {
        SaveIntegralRuleBasicRequest request = PojoUtils.map(form, SaveIntegralRuleBasicRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(integralUseRuleApi.saveBasic(request), IntegralRuleVO.class));
    }

    @ApiOperation(value = "保存参与抽奖配置")
    @PostMapping("/saveLotteryConfig")
    @Log(title = "保存参与抽奖配置", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveLotteryConfig(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralLotteryConfigForm form) {
        SaveIntegralLotteryConfigRequest request = PojoUtils.map(form, SaveIntegralLotteryConfigRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralUseRuleApi.saveLotteryConfig(request));
    }

    @ApiOperation(value = "查看")
    @GetMapping("/get")
    public Result<IntegralUseRuleDetailVO> get(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("消耗规则ID") @RequestParam("id") Long id) {
        IntegralUseRuleDetailBO useRuleDetailBO  = integralUseRuleMarketingApi.get(id);
        return Result.success(PojoUtils.map(useRuleDetailBO, IntegralUseRuleDetailVO.class));
    }

    @ApiOperation(value = "复制")
    @GetMapping("/copy")
    public Result<Long> copy(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "消耗规则ID", required = true) @RequestParam("id") Long id) {
        Long newUseRuleId = integralUseRuleApi.copy(id, adminInfo.getCurrentUserId());
        return Result.success(newUseRuleId);
    }

}
