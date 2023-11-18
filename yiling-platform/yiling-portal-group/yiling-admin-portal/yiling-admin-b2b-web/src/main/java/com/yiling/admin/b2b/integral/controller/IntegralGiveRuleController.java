package com.yiling.admin.b2b.integral.controller;

import java.util.List;

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
import com.yiling.admin.b2b.integral.form.SaveIntegralMultipleConfigForm;
import com.yiling.admin.b2b.integral.form.SaveOrderGiveIntegralForm;
import com.yiling.admin.b2b.integral.form.SaveSignPeriodForm;
import com.yiling.admin.b2b.integral.vo.GenerateMultipleConfigVO;
import com.yiling.admin.b2b.integral.vo.IntegralGiveRuleDetailVO;
import com.yiling.admin.b2b.integral.vo.IntegralRuleListItemVO;
import com.yiling.admin.b2b.integral.vo.IntegralRuleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.integral.api.IntegralGiveRuleApi;
import com.yiling.user.integral.bo.IntegralGiveRuleDetailBO;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.SaveIntegralMultipleConfigRequest;
import com.yiling.user.integral.dto.request.SaveOrderGiveIntegralRequest;
import com.yiling.user.integral.dto.request.SaveSignPeriodRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分发放规则 Controller
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
@Slf4j
@RestController
@RequestMapping("/integralGiveRule")
@Api(tags = "积分发放规则接口")
public class IntegralGiveRuleController extends BaseController {

    @DubboReference
    IntegralGiveRuleApi integralGiveRuleApi;

    @ApiOperation(value = "积分发放规则分页")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralRuleListItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralRulePageForm form) {
        QueryIntegralRulePageRequest request = PojoUtils.map(form, QueryIntegralRulePageRequest.class);
        Page<IntegralRuleListItemVO> page = PojoUtils.map(integralGiveRuleApi.queryListPage(request), IntegralRuleListItemVO.class);
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
        return Result.success(integralGiveRuleApi.updateStatus(request));
    }

    @ApiOperation(value = "保存积分发放规则基本信息")
    @PostMapping("/saveBasic")
    @Log(title = "保存积分发放规则基本信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<IntegralRuleVO> saveBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralGiveRuleBasicForm form) {
        SaveIntegralRuleBasicRequest request = PojoUtils.map(form, SaveIntegralRuleBasicRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(integralGiveRuleApi.saveBasic(request), IntegralRuleVO.class));
    }

    @ApiOperation(value = "提交-保存签到送积分")
    @PostMapping("/saveSignPeriod")
    @Log(title = "保存签到送积分", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveSignPeriod(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveSignPeriodForm form) {
        SaveSignPeriodRequest request = PojoUtils.map(form, SaveSignPeriodRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralGiveRuleApi.saveSignPeriod(request));
    }

    @ApiOperation(value = "提交-保存订单送积分")
    @PostMapping("/saveOrderGiveIntegral")
    @Log(title = "保存订单送积分", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveOrderGiveIntegral(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveOrderGiveIntegralForm form) {
        SaveOrderGiveIntegralRequest request = PojoUtils.map(form, SaveOrderGiveIntegralRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralGiveRuleApi.saveOrderGiveIntegral(request));
    }

    @ApiOperation(value = "生成订单积分倍数配置表格")
    @GetMapping("/generateMultipleConfig")
    public Result<CollectionObject<GenerateMultipleConfigVO>> generateMultipleConfig(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("发放规则ID") @RequestParam("giveRuleId") Long giveRuleId) {
        List<GenerateMultipleConfigDTO> configDTOList = integralGiveRuleApi.generateMultipleConfig(giveRuleId);
        List<GenerateMultipleConfigVO> configVOList = PojoUtils.map(configDTOList, GenerateMultipleConfigVO.class);
        return Result.success(new CollectionObject<>(configVOList));
    }

    @ApiOperation(value = "保存订单积分倍数配置")
    @PostMapping("/saveMultipleConfig")
    @Log(title = "保存订单积分倍数配置", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveMultipleConfig(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralMultipleConfigForm form) {
        SaveIntegralMultipleConfigRequest request = PojoUtils.map(form, SaveIntegralMultipleConfigRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralGiveRuleApi.saveMultipleConfig(request));
    }

    @ApiOperation(value = "查看")
    @GetMapping("/get")
    public Result<IntegralGiveRuleDetailVO> get(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("发放规则ID") @RequestParam("id") Long id) {
        IntegralGiveRuleDetailBO giveRuleDetailBO  = integralGiveRuleApi.get(id);
        return Result.success(PojoUtils.map(giveRuleDetailBO, IntegralGiveRuleDetailVO.class));
    }

    @ApiOperation(value = "复制")
    @GetMapping("/copy")
    public Result<Long> copy(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "发放规则ID", required = true) @RequestParam("id") Long id) {
        Long newGiveRuleId = integralGiveRuleApi.copy(id, adminInfo.getCurrentUserId());
        return Result.success(newGiveRuleId);
    }

}
