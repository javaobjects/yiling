package com.yiling.admin.b2b.integral.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.b2b.integral.form.SaveIntegralInstructionConfigForm;
import com.yiling.admin.b2b.integral.vo.IntegralInstructionConfigVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralInstructionConfigApi;
import com.yiling.user.integral.dto.IntegralInstructionConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralInstructionConfigRequest;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分说明配置 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-11
 */
@Slf4j
@RestController
@RequestMapping("/integralInstructionConfig")
@Api(tags = "积分说明配置接口")
public class IntegralInstructionConfigController extends BaseController {

    @DubboReference
    IntegralInstructionConfigApi integralInstructionConfigApi;

    @ApiOperation(value = "保存积分说明配置")
    @PostMapping("/saveConfig")
    public Result<Boolean> saveConfig(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveIntegralInstructionConfigForm form) {
        SaveIntegralInstructionConfigRequest request = PojoUtils.map(form, SaveIntegralInstructionConfigRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralInstructionConfigApi.saveConfig(request));
    }

    @ApiOperation(value = "获取积分说明配置")
    @GetMapping("/get")
    public Result<IntegralInstructionConfigVO> get(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam(value = "id", required = false) Long id) {
        IntegralInstructionConfigDTO instructionConfigDTO = integralInstructionConfigApi.get(id, IntegralRulePlatformEnum.B2B.getCode());
        return Result.success(PojoUtils.map(instructionConfigDTO, IntegralInstructionConfigVO.class));
    }

}
