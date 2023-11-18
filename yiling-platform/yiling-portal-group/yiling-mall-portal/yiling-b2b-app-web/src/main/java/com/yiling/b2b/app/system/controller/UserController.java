package com.yiling.b2b.app.system.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.config.LoginConfig;
import com.yiling.b2b.app.system.form.AddDeregisterAccountForm;
import com.yiling.b2b.app.system.form.CheckNewMobileNumberVerifyCodeForm;
import com.yiling.b2b.app.system.form.CheckSmsVerifyCodeForm;
import com.yiling.b2b.app.system.form.GetNewMobileNumberVerifyCodeForm;
import com.yiling.b2b.app.system.form.GetSmsVerifyCodeForm;
import com.yiling.b2b.app.system.form.GetSpecialMobileVerifyCodeForm;
import com.yiling.b2b.app.system.form.ResetPasswordForm;
import com.yiling.b2b.app.system.form.UpdatePasswordForm;
import com.yiling.b2b.app.system.vo.LoginInfoVO;
import com.yiling.b2b.app.system.vo.StaffVO;
import com.yiling.b2b.app.system.vo.UserDeregisterValidVO;
import com.yiling.b2b.app.system.vo.UserMobileValidVO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.mall.userderegister.api.UserDeregisterAccountMallApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.UpdateManagerMobileRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserDeregisterValidDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserDeregisterAccountSourceEnum;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/9/26
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController extends BaseController {

    @Value("${jwt.expiration}")
    private Long    expiration;

    /**
     * 虚拟账号过期时间（秒）：默认为40天（3600 * 24 * 40 = 3456000秒）
     */
    @Value("${common.user.virtualAccount.expired:3456000}")
    private Integer virtualExpired;

    @Autowired
    LoginConfig loginConfig;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    RedisService  redisService;

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi      staffApi;
    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    SmsApi smsApi;
    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;
    @DubboReference
    UserDeregisterAccountMallApi userDeregisterAccountMallApi;

    @ApiOperation(value = "获取重置密码验证码")
    @PostMapping("/getResetPasswordVerifyCode")
    public Result getResetPasswordVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        if (staff == null) {
            return Result.failed("您的账号尚未注册");
        }

        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return Result.failed("您的账号已被停用，请联系客服或企业管理员");
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.B2B_RESET_PASSWORD);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("获取重置密码验证码失败");
        }
    }

    @ApiOperation(value = "校验重置登录密码验证码")
    @PostMapping("/checkResetPasswordVerifyCode")
    public Result checkResetPasswordVerifyCode(@RequestBody @Valid CheckSmsVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_RESET_PASSWORD);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("验证码错误或失效，请重新填写");
        }
    }

    @ApiOperation(value = "重置登录密码")
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody @Valid ResetPasswordForm form) {
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_RESET_PASSWORD);
        if (!checkVerifyCodeResult) {
            return Result.failed("验证码错误或失效，请重新填写");
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff == null) {
            return Result.failed("您的账号尚未注册");
        }

        boolean result = staffApi.updatePassword(staff.getId(), form.getPassword(), staff.getId());
        if (result) {
            return Result.success();
        } else {
            return Result.failed("重置登录密码失败");
        }
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/getMyDetails")
    public Result<StaffVO> getMyDetails(@CurrentUser CurrentStaffInfo staffInfo) {
        Staff staff = staffApi.getById(staffInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(staff, StaffVO.class));
    }

    @ApiOperation(value = "获取当前用户隶属的企业列表")
    @GetMapping("/getMyEnterpriseList")
    public Result<CollectionObject<SimpleEnterpriseVO>> getMyEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staffInfo.getCurrentUserId(), EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        // 筛选出审核通过的企业
        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        // 过滤掉“医疗机构”类型的企业
        userEnterpriseList = userEnterpriseList.stream().filter(e -> this.isValidEnterpriseType(e)).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        // 筛选出终端类型的企业
        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseTypeEnum.getByCode(e.getType()).isTerminal()).collect(Collectors.toList());

        return Result.success(new CollectionObject<>(PojoUtils.map(userEnterpriseList, SimpleEnterpriseVO.class)));
    }

    private boolean isValidEnterpriseType(EnterpriseDTO enterpriseDTO) {
        if (EnterpriseTypeEnum.getByCode(enterpriseDTO.getType()) == EnterpriseTypeEnum.HOSPITAL) {
            if (!loginConfig.getEnabledHospitalEids().contains(enterpriseDTO.getId())) {
                return false;
            }
        }

        return true;
    }

    @ApiOperation(value = "切换当前用户的企业")
    @GetMapping("/toggleEnterprise")
    public Result<LoginInfoVO> toggleEnterprise(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("eid") Long eid) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
        if (enterpriseDTO == null) {
            return Result.failed("企业信息不存在");
        } else if (EnterpriseStatusEnum.getByCode(enterpriseDTO.getStatus()) == EnterpriseStatusEnum.DISABLED) {
            return Result.failed("该企业已被停用");
        }

        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staffInfo.getCurrentUserId(), EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed("您的账号尚未加入企业");
        }

        List<EnterpriseDTO> userTerminalEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseTypeEnum.getByCode(e.getType()).isTerminal()).collect(Collectors.toList());
        // 筛选出审核通过的企业
        userTerminalEnterpriseList = userTerminalEnterpriseList.stream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userTerminalEnterpriseList)) {
            return Result.failed("工业、商业用户请登录POP采购");
        }

        EnterpriseDTO userTerminalEnterprise = userTerminalEnterpriseList.stream().filter(e -> e.getId().equals(eid)).findFirst().orElse(null);
        if (userTerminalEnterprise == null) {
            return Result.failed("当前用户不属于该企业");
        }

        LoginInfoVO loginInfo = new LoginInfoVO();
        // 获取用户信息
        Staff staff = staffApi.getById(staffInfo.getCurrentUserId());
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));
        loginInfo.setUserEnterpriseList(PojoUtils.map(userTerminalEnterpriseList, SimpleEnterpriseVO.class));

        LoginInfoVO.CurrentEnterpriseVO currentEnterpriseVO = PojoUtils.map(enterpriseDTO, LoginInfoVO.CurrentEnterpriseVO.class);
        // 获取用户对应当前企业中的员工信息
        EnterpriseEmployeeDTO userEnterpriseEmployeeDTO = employeeApi.getByEidUserId(eid, staff.getId());
        Long employeeId = userEnterpriseEmployeeDTO.getId();
        currentEnterpriseVO.setEmployeeId(employeeId);
        // 用户是否为企业管理员
        boolean isAdmin = userEnterpriseEmployeeDTO.getAdminFlag() == 1;
        currentEnterpriseVO.setAdminFlag(isAdmin);
        loginInfo.setCurrentEnterpriseInfo(currentEnterpriseVO);

        // 是否为虚拟账号
        boolean isVirtualAccount = userApi.checkSpecialPhone(staff.getMobile());
        if (isVirtualAccount) {
            EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(enterpriseDTO.getId(), staff.getId());
            // 设置虚拟账号需绑定标识
            Date virtualCreateTime = employeeDTO.getCreateTime();
            loginInfo.setSpecialPhone(true);
            loginInfo.setMustChangeBind(DateUtil.compare(DateUtil.offsetSecond(virtualCreateTime, virtualExpired), new Date()) < 0);
        } else {
            loginInfo.setSpecialPhone(false);
            loginInfo.setMustChangeBind(false);
        }
        // 设置token
        String token = jwtTokenUtils.generateToken(new JwtDataModel(staffInfo.getAppEnum().getAppId(), staff.getId(), enterpriseDTO.getId(),
                enterpriseDTO.getType(), enterpriseDTO.getChannelId(), employeeId, isAdmin));
        loginInfo.setToken(token);

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", staffInfo.getAppEnum().getAppCode(), "user", staff.getId().toString());
        redisService.set(userAppTokenKey, loginInfo.getToken(), expiration);

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "获取原手机号短信验证码")
    @GetMapping("/getOriginalMobileNumberVerifyCode")
    public Result getOriginalMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.sendVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.B2B_VERIFY_ORIGINAL_MOBILE_NUMBER);
        return result ? Result.success() : Result.failed("获取短信验证码失败");
    }

    @ApiOperation(value = "校验原手机号短信验证码")
    @GetMapping("/checkOriginalMobileNumberVerifyCode")
    public Result checkOriginalMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam String verifyCode) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), verifyCode, SmsVerifyCodeTypeEnum.B2B_VERIFY_ORIGINAL_MOBILE_NUMBER);
        return result ? Result.success() : Result.failed("验证码错误或失效，请重新填写");
    }

    @ApiOperation(value = "获取更换绑定的新手机号短信验证码")
    @PostMapping("/getNewMobileNumberVerifyCode")
    public Result getNewMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid GetNewMobileNumberVerifyCodeForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_VERIFY_ORIGINAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("原手机号短信验证码已失效，请返回上一步重新提交");
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff != null) {
            return Result.failed("该手机号已被绑定，请更换");
        }

        result = smsApi.sendVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.B2B_CHANGE_MOBILE_NUMBER);
        return Result.success();
    }

    @ApiOperation(value = "校验更换绑定的新手机号短信验证码")
    @PostMapping("/checkNewMobileNumberVerifyCode")
    public Result checkNewMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CheckNewMobileNumberVerifyCodeForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getOriginalVerifyCode(), SmsVerifyCodeTypeEnum.B2B_VERIFY_ORIGINAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("原手机号短信验证码已失效，请返回上一步重新提交");
        }

        result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_CHANGE_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("验证码错误或失效，请重新填写");
        }

        UpdateUserRequest request = new UpdateUserRequest();
        request.setAppId(UserAuthsAppIdEnum.MALL.getCode());
        request.setId(staffInfo.getCurrentUserId());
        request.setMobile(form.getMobile());
        request.setOpUserId(staffInfo.getCurrentUserId());
        result = userApi.updateUserInfo(request);

        return Result.success();
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/updatePassword")
    public Result updatePassword(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdatePasswordForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_VERIFY_ORIGINAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("验证码错误或失效，请重新填写");
        }

        // 清理短信验证码
        smsApi.cleanVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.B2B_VERIFY_ORIGINAL_MOBILE_NUMBER);

        // 修改密码
        result = staffApi.updatePassword(userDTO.getId(), form.getPassword(), userDTO.getId());
        return result ? Result.success() : Result.failed("密码修改失败");
    }

    @ApiOperation(value = "获取特殊号码换绑新手机号短信验证码")
    @PostMapping("/getSpecialMobileVerifyCode")
    public Result<Void> getSpecialMobileVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid GetSpecialMobileVerifyCodeForm form) {
        Staff staff = staffApi.getByMobile(form.getMobile());
        if (Objects.nonNull(staff) && UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return Result.failed("手机号已被停用，请联系平台客服");
        }

        smsApi.sendVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.B2B_CHANGE_SPECIAL_MOBILE_NUMBER);
        return Result.success();
    }

    @ApiOperation(value = "校验特殊号码换绑新手机号验证码", notes = "换绑成功后返回新账号的token")
    @PostMapping("/checkSpecialMobileVerifyCode")
    public Result<String> checkSpecialMobileVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CheckSmsVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_CHANGE_SPECIAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("手机号或验证码错误，请重新输入");
        }
        if (!StrUtil.equals(form.getPassword(), form.getRePassword())) {
            return Result.failed("两次输入的密码不一致");
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff == null) {
            staff = staffApi.getById(staffInfo.getCurrentUserId());
        }
        // 清理短信验证码
        smsApi.cleanVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.B2B_CHANGE_SPECIAL_MOBILE_NUMBER);

        UpdateManagerMobileRequest request = new UpdateManagerMobileRequest();
        request.setEid(staffInfo.getCurrentEid());
        request.setUserId(staffInfo.getCurrentUserId());
        request.setNewMobile(form.getMobile());
        request.setName(staff.getName());
        request.setPassword(form.getPassword());
        request.setOpUserId(staffInfo.getCurrentUserId());
        result = enterpriseApi.updateManagerMobile(request);

        // 如果换绑成功，生成新账号的token返回给前端
        if (result) {
            Staff newStaff = staffApi.getByMobile(form.getMobile());
            EnterpriseDTO defaultEnterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
            EnterpriseEmployeeDTO defaultEnterpriseEmployeeDTO = employeeApi.getByEidUserId(defaultEnterpriseDTO.getId(), newStaff.getId());
            boolean isAdmin = employeeApi.isAdmin(request.getEid(), request.getUserId());
            // 设置token
            String token = jwtTokenUtils.generateToken(new JwtDataModel(AppEnum.B2B_APP.getAppId(), newStaff.getId(), defaultEnterpriseDTO.getId(),
                    defaultEnterpriseDTO.getType(), defaultEnterpriseDTO.getChannelId(), defaultEnterpriseEmployeeDTO.getId(), isAdmin));
            // 服务端保存的用户Token
            String userAppTokenKey = RedisKey.generate("token", AppEnum.B2B_APP.getAppCode(), "user", staff.getId().toString());
            redisService.set(userAppTokenKey, token, expiration);

            return Result.success(token);
        }

        return Result.success();
    }

    @ApiOperation(value = "校验当前账号是否为特殊号码")
    @GetMapping("/checkSpecialMobile")
    public Result<UserMobileValidVO> checkSpecialMobile(@CurrentUser CurrentStaffInfo staffInfo) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean specialFlag = userApi.checkSpecialPhone(userDTO.getMobile());

        UserMobileValidVO userMobileValidVO = new UserMobileValidVO();
        //如果为虚拟账号登录，校验是否已经超过指定天数：超过需要强制绑定新号码，没有超过则可以继续当做正常号码使用
        if (specialFlag) {
            EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            // 设置虚拟账号需绑定标识
            Date virtualCreateTime = employeeDTO.getCreateTime();
            userMobileValidVO.setSpecialPhone(true);
            userMobileValidVO.setMustChangeBind(DateUtil.compare(DateUtil.offsetSecond(virtualCreateTime, virtualExpired), new Date()) < 0);
        }

        return Result.success(userMobileValidVO);
    }

    @ApiOperation(value = "校验注销账号", notes = "如果data为空表示校验通过，不为空表示校验不通过并返回错误信息")
    @GetMapping("/checkLogoutAccount")
    public Result<CollectionObject<UserDeregisterValidVO>> checkLogoutAccount(@CurrentUser CurrentStaffInfo staffInfo) {
        List<UserDeregisterValidDTO> deregisterValidDTOList = userDeregisterAccountMallApi.checkLogoutAccount(staffInfo.getCurrentUserId());
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
        request.setSource(UserDeregisterAccountSourceEnum.B2B_APP.getCode());
        request.setTerminalType(form.getTerminalType());
        request.setApplyReason(form.getApplyReason());
        request.setOpUserId(staffInfo.getCurrentUserId());
        userDeregisterAccountApi.applyDeregisterAccount(request);

        return Result.success();
    }

}
