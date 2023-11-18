package com.yiling.sales.assistant.app.mr.system.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.mall.userderegister.api.UserDeregisterAccountMallApi;
import com.yiling.sales.assistant.app.mr.system.enums.LoginErrorCode;
import com.yiling.sales.assistant.app.mr.system.form.AddDeregisterAccountForm;
import com.yiling.sales.assistant.app.mr.system.form.CheckLoginPasswordForm;
import com.yiling.sales.assistant.app.mr.system.form.CheckResetPasswordVerifyCodeForm;
import com.yiling.sales.assistant.app.mr.system.form.GetSmsVerifyCodeForm;
import com.yiling.sales.assistant.app.mr.system.form.ResetMobileForm;
import com.yiling.sales.assistant.app.mr.system.form.ResetPasswordForm;
import com.yiling.sales.assistant.app.mr.system.form.UserRegisterForm;
import com.yiling.sales.assistant.app.mr.system.vo.UserDeregisterValidVO;
import com.yiling.user.system.api.MrUserRegisterApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserDeregisterAssistantValidDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.CreateMrUserRegisterRequest;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserDeregisterAccountSourceEnum;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户 Controller
 *
 * @author: lun.yu
 * @date: 2022-12-13
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController extends BaseController {

    @Value("${user.register-enabled}")
    private boolean userRegisterEnabled;

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    SmsApi smsApi;
    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;
    @DubboReference
    UserDeregisterAccountMallApi userDeregisterAccountMallApi;
    @DubboReference
    MrUserRegisterApi mrUserRegisterApi;

    @ApiOperation(value = "校验注销账号", notes = "如果data为空表示校验通过，不为空表示校验不通过并返回错误信息")
    @GetMapping("/checkLogoutAccount")
    public Result<CollectionObject<UserDeregisterValidVO>> checkLogoutAccount(@CurrentUser CurrentStaffInfo staffInfo) {
        List<UserDeregisterAssistantValidDTO> deregisterValidDTOList = userDeregisterAccountMallApi.checkMrLogoutAccount(staffInfo.getCurrentUserId(), staffInfo.getCurrentEid(), staffInfo.getUserType());
        if (CollUtil.isNotEmpty(deregisterValidDTOList)) {
            List<UserDeregisterValidVO> deregisterValidVOList = PojoUtils.map(deregisterValidDTOList, UserDeregisterValidVO.class);
            return Result.success(new CollectionObject<>(deregisterValidVOList));
        }

        return Result.success();
    }

    @ApiOperation(value = "获取注销账号短信验证码")
    @GetMapping("/getDeregisterVerifyCode")
    public Result<Void> getDeregisterVerifyCode(@CurrentUser CurrentStaffInfo staffInfo) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        Staff staff = staffApi.getByMobile(userDTO.getMobile());
        if (Objects.nonNull(staff) && UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return Result.failed("手机号已被停用，请联系平台客服");
        }

        smsApi.sendVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.B2B_DEREGISTER_ACCOUNT);
        return Result.success();
    }

    @ApiOperation(value = "申请注销账号")
    @PostMapping("/applyDeregisterAccount")
    @Log(title = "申请注销账号", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> applyDeregisterAccount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddDeregisterAccountForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_DEREGISTER_ACCOUNT);
        if (!result) {
            return Result.failed("验证码错误或失效，请重新填写");
        }
        // 清理短信验证码
        smsApi.cleanVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.B2B_DEREGISTER_ACCOUNT);

        AddDeregisterAccountRequest request = new AddDeregisterAccountRequest();
        request.setUserId(staffInfo.getCurrentUserId());
        request.setSource(UserDeregisterAccountSourceEnum.DOCTOR_ASSISTANT_APP.getCode());
        request.setTerminalType(form.getTerminalType());
        request.setApplyReason(form.getApplyReason());
        request.setOpUserId(staffInfo.getCurrentUserId());
        userDeregisterAccountApi.applyDeregisterAccount(request);

        return Result.success();
    }

    @ApiOperation(value = "获取重置登录密码验证码")
    @PostMapping("/getResetPasswordVerifyCode")
    public Result getResetPasswordVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        if (staff != null) {
            if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
                return Result.failed(LoginErrorCode.ACCOUNT_DISABLED);
            }

            return this.sendVerifyCode(mobile);
        }

        return Result.failed(LoginErrorCode.ACCOUNT_NOT_EXIST);
    }

    @ApiOperation(value = "校验重置登录密码验证码")
    @PostMapping("/checkResetPasswordVerifyCode")
    public Result checkResetPasswordVerifyCode(@RequestBody @Valid CheckResetPasswordVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.MR_RESET_PASSWORD);
        return result ? Result.success() : Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
    }

    @ApiOperation(value = "重置登录密码")
    @PostMapping("/resetPassword")
    @Log(title = "重置登录密码", businessType = BusinessTypeEnum.UPDATE)
    public Result resetPassword(@RequestBody @Valid ResetPasswordForm form) {
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.MR_RESET_PASSWORD);
        if (!checkVerifyCodeResult) {
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff == null) {
            return Result.failed(LoginErrorCode.ACCOUNT_NOT_EXIST);
        }

        boolean result = staffApi.updatePassword(staff.getId(), form.getPassword(), staff.getId());
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "获取换绑手机号验证码")
    @PostMapping("/getChangeMobileVerifyCode")
    public Result getChangeMobileVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.MR_CHANGE_MOBILE);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("获取换绑手机号验证码失败");
        }
    }

    @ApiOperation(value = "校验换绑原手机号验证码",notes = "点击下一步时调用此接口做校验")
    @PostMapping("/checkChangeMobileVerifyCode")
    public Result checkChangeMobileVerifyCode(@RequestBody @Valid CheckResetPasswordVerifyCodeForm form) {
        String mobile = form.getMobile();

        boolean result = smsApi.checkVerifyCode(mobile, form.getVerifyCode(), SmsVerifyCodeTypeEnum.MR_CHANGE_MOBILE);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("验证码错误或失效，请重新填写");
        }
    }

    @ApiOperation(value = "校验登录密码（原手机号不可接受验证码的方式）",notes = "点击下一步时调用此接口做校验")
    @PostMapping("/checkLoginPassword")
    public Result checkLoginPassword(@RequestBody @Valid CheckLoginPasswordForm form) {
        Staff staff = Optional.ofNullable(staffApi.getByMobile(form.getOldMobile())).orElseThrow(() -> new BusinessException(LoginErrorCode.ACCOUNT_NOT_EXIST));

        boolean result = staffApi.checkPassword(staff.getId(), form.getPassword());
        if (!result) {
            return Result.failed("您填写的登录密码有误，请重新填写");
        } else {
            return Result.success();
        }

    }

    @ApiOperation(value = "确认换绑手机号")
    @PostMapping("/changeMobile")
    @Log(title = "确认换绑手机号", businessType = BusinessTypeEnum.UPDATE)
    public Result changeMobile(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid ResetMobileForm form) {
        if (StrUtil.equals(form.getOldMobile(),form.getNewMobile())) {
            return Result.failed("换绑后的手机号不能与原手机号相同");
        }

        boolean checkNewVerifyCode = smsApi.checkVerifyCode(form.getNewMobile(), form.getNewVerifyCode(), SmsVerifyCodeTypeEnum.MR_CHANGE_MOBILE);
        if (!checkNewVerifyCode) {
            return Result.failed("新手机号验证码错误或失效，请重新填写");
        }

        boolean result = userApi.changeMobile(staffInfo.getCurrentUserId(), UserAuthsAppIdEnum.MALL.getCode(), form.getNewMobile(), staffInfo.getCurrentUserId());
        if (result) {
            smsApi.cleanVerifyCode(form.getNewMobile(), SmsVerifyCodeTypeEnum.MR_CHANGE_MOBILE);
            return Result.success();
        } else {
            return Result.failed("换绑手机号失败");
        }
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserRegisterForm form) {
        if (!this.userRegisterEnabled) {
            return Result.failed("对不起，用户注册功能已关闭");
        }

        CreateMrUserRegisterRequest request = PojoUtils.map(form, CreateMrUserRegisterRequest.class);
        boolean result = mrUserRegisterApi.create(request);
        return result ? Result.success() : Result.failed("注册失败");
    }

    @ApiOperation(value = "是否开启用户注册功能")
    @PostMapping("/isUserRegisterEnabled")
    public Result<BoolObject> isUserRegisterEnabled() {
        return Result.success(new BoolObject(this.userRegisterEnabled));
    }

    private Result sendVerifyCode(String mobile) {
        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.MR_RESET_PASSWORD);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

}
