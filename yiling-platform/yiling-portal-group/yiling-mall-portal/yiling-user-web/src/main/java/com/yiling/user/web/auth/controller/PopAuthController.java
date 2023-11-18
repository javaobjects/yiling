package com.yiling.user.web.auth.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.captcha.api.ImageCaptchaApi;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.Constants;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.web.auth.enums.LoginErrorCode;
import com.yiling.user.web.auth.form.CheckResetPasswordVerifyCodeForm;
import com.yiling.user.web.auth.form.GetSmsVerifyCodeForm;
import com.yiling.user.web.auth.form.PopPasswordLoginForm;
import com.yiling.user.web.auth.form.ResetPasswordForm;
import com.yiling.user.web.auth.form.SmsLoginForm;
import com.yiling.user.web.auth.vo.LoginInfoVO;
import com.yiling.user.web.auth.vo.StaffVO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * POP 认证 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "POP认证接口")
public class PopAuthController extends BaseAuthController {

    @Value("${jwt.expiration}")
    private Long    expiration;
    @Value("${auth.login.image-captcha.required:true}")
    private Boolean imageCaptchaRequired;
    
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    RedisService  redisService;

    @DubboReference
    UserApi       userApi;
    @DubboReference
    StaffApi      staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    SmsApi        smsApi;
    @DubboReference
    ImageCaptchaApi imageCaptchaApi;
    @DubboReference(async = true)
    SysLoginLogApi sysLoginLogApi;

    @ApiOperation(value = "手机号+密码登录")
    @PostMapping("/loginPassword")
    public Result<LoginInfoVO> loginPassword(@RequestBody @Valid PopPasswordLoginForm form) {
        if (imageCaptchaRequired) {
            boolean checkCaptchaResult = imageCaptchaApi.checkCaptcha(form.getValidateCode(), form.getSerialNo());
            if (!checkCaptchaResult) {
                // 保存登录日志
                this.saveLoginLog(0L, "", form.getMobile(), Constants.LOGIN_STATUS_FAIL, UserErrorCode.IMAGE_CAPTCHA_ERROR.getMessage(), Constants.GRANT_TYPE_PASSWORD);
                return Result.failed(UserErrorCode.IMAGE_CAPTCHA_ERROR);
            }
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        // 校验用户信息
        IErrorCode errorCode = this.validateStaff(staff, form.getPassword());
        if (errorCode != null) {
            Long userId = staff != null ? staff.getId() : 0L;
            String name = staff != null ? staff.getName() : "";
            // 保存登录日志
            this.saveLoginLog(userId, name, form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            return Result.failed(errorCode);
        }

        // 获取用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ENABLED);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
        if (validateResult.isSuccessful()) {
            userEnterpriseList = validateResult.getData();
        } else {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            return Result.failed(validateResult.getCode(),validateResult.getMessage());
        }

        LoginInfoVO loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.MALL_ADMIN);
        //保存登录信息
        this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_PASSWORD);

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "手机号+短信验证码登录")
    @PostMapping("/loginSms")
    public Result<LoginInfoVO> loginSms(@RequestBody @Valid SmsLoginForm form) {
        // 短信验证码校验
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.LOGIN);
        if (!checkVerifyCodeResult) {
            this.saveLoginLog(0L, "", form.getMobile(), Constants.LOGIN_STATUS_FAIL, UserErrorCode.VERIFY_CODE_ERROR.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(UserErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        // 校验用户信息
        IErrorCode errorCode = this.validateStaff(staff, null);
        if (errorCode != null) {
            Long userId = staff != null ? staff.getId() : 0L;
            String name = staff != null ? staff.getName() : "";
            // 保存登录日志
            this.saveLoginLog(userId, name, form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(errorCode);
        }

        // 获取用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ENABLED);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
        if (validateResult.isSuccessful()) {
            userEnterpriseList = validateResult.getData();
        } else {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(validateResult.getCode(),validateResult.getMessage());
        }

        LoginInfoVO loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.MALL_ADMIN);
        //保存登录信息
        this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_VERIFY_CODE);
        // 清理短信验证码
        smsApi.cleanVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.LOGIN);

        return Result.success(loginInfo);
    }

    private Result<List<EnterpriseDTO>> validateUserEnterpriseList(List<EnterpriseDTO> userEnterpriseList) {
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_EMPTY);
        }

        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseStatusEnum.getByCode(e.getStatus()) == EnterpriseStatusEnum.ENABLED).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_DISABLED);
        }

        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_UNAUDITED);
        }

        List<Long> eids = userEnterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        List<EnterprisePlatformDTO> enterprisePlatformDTOList = enterpriseApi.getEnterprisePlatforms(eids);
        if (CollUtil.isEmpty(enterprisePlatformDTOList)) {
            return Result.failed(LoginErrorCode.PLEASE_LOGIN_AT_DYH_APP);
        }

        Map<Long, EnterprisePlatformDTO> enterprisePlatformDTOMap = enterprisePlatformDTOList.stream().collect(Collectors.toMap(EnterprisePlatformDTO::getEid, Function.identity()));
        userEnterpriseList = userEnterpriseList.stream().filter(e -> {
            EnterprisePlatformDTO enterprisePlatformDTO = enterprisePlatformDTOMap.get(e.getId());
            if (enterprisePlatformDTO == null) {
                return false;
            }
            return enterprisePlatformDTO.getPopFlag() == 1 || enterprisePlatformDTO.getMallFlag() == 1 || enterprisePlatformDTO.getSalesAssistFlag() == 1 || enterprisePlatformDTO.getHmcFlag() == 1;
        }).collect(Collectors.toList());

        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.PLEASE_LOGIN_AT_DYH_APP);
        }

        return Result.success(userEnterpriseList);
    }

    @ApiOperation(value = "获取登录验证码")
    @PostMapping("/getLoginVerifyCode")
    public Result<BoolObject> getLoginVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        IErrorCode errorCode = this.validateStaff(staff, null);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ENABLED);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
        if (!validateResult.isSuccessful()) {
            return Result.failed(validateResult.getCode(),validateResult.getMessage());
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.LOGIN);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "获取重置密码验证码")
    @PostMapping("/getResetPasswordVerifyCode")
    public Result<BoolObject> getResetPasswordVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        IErrorCode errorCode = this.validateStaff(staff, null);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.RESET_PASSWORD);
        return Result.success(new BoolObject(result));
    }


    @ApiOperation(value = "校验重置密码验证码")
    @PostMapping("/checkResetPasswordVerifyCode")
    public Result<BoolObject> checkResetPasswordVerifyCode(@RequestBody @Valid CheckResetPasswordVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.RESET_PASSWORD);
        if (!result) {
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/resetPassword")
    public Result<BoolObject> resetPassword(@RequestBody @Valid ResetPasswordForm form) {
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.RESET_PASSWORD);
        if (!checkVerifyCodeResult) {
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        boolean result = staffApi.updatePassword(staff.getId(), form.getPassword(), staff.getId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    public Result<BoolObject> logout(@CurrentUser CurrentStaffInfo staffInfo) {
        if (staffInfo == null) {
            return Result.success();
        }

//        String userAppTokenKey = RedisKey.generate("token", staffInfo.getAppEnum().getAppCode(), "user", staffInfo.getCurrentUserId().toString());
//        if (redisService.hasKey(userAppTokenKey)) {
//            redisService.del(userAppTokenKey);
//        }
        return Result.success(new BoolObject(true));
    }

    private LoginInfoVO getLoginInfoVO(Staff staff, List<EnterpriseDTO> userEnterpriseList, AppEnum appEnum) {
        LoginInfoVO loginInfo = new LoginInfoVO();
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));
        loginInfo.setUserEnterpriseList(PojoUtils.map(userEnterpriseList, SimpleEnterpriseVO.class));

        // 默认企业信息
        EnterpriseDTO defaultEnterpriseDTO = userEnterpriseList.get(0);
        LoginInfoVO.CurrentEnterpriseVO currentEnterpriseVO = PojoUtils.map(defaultEnterpriseDTO, LoginInfoVO.CurrentEnterpriseVO.class);
        // 默认企业对应的员工信息
        EnterpriseEmployeeDTO defaultEnterpriseEmployeeDTO = employeeApi.getByEidUserId(defaultEnterpriseDTO.getId(), staff.getId());
        Long employeeId = defaultEnterpriseEmployeeDTO.getId();
        currentEnterpriseVO.setEmployeeId(employeeId);
        // 用户是否为企业管理员
        boolean isAdmin = defaultEnterpriseEmployeeDTO.getAdminFlag() == 1;
        currentEnterpriseVO.setAdminFlag(isAdmin);
        loginInfo.setCurrentEnterpriseInfo(currentEnterpriseVO);

        // 设置token
        loginInfo.setToken(jwtTokenUtils.generateToken(new JwtDataModel(appEnum.getAppId(), staff.getId(), defaultEnterpriseDTO.getId(), defaultEnterpriseDTO.getType(), defaultEnterpriseDTO.getChannelId(), employeeId, isAdmin)));

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", appEnum.getAppCode(), "user", staff.getId().toString());
        redisService.set(userAppTokenKey, loginInfo.getToken(), expiration);

        return loginInfo;
    }

    /**
     * 插入登录日志记录
     * @param userId 用户ID
     * @param name 用户名
     * @param loginAccount 用户登录账号
     * @param status 登录状态
     * @param content 返回内容
     * @param grantType 登录方式
     */
    private void saveLoginLog(Long userId , String name , String loginAccount, String status , String content , String grantType){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));

        SysLoginLogDTO loginLogDTO = new SysLoginLogDTO();
        loginLogDTO.setAppId(AppEnum.MALL_ADMIN.getAppCode());
        loginLogDTO.setUserId(userId);
        loginLogDTO.setUserName(name);
        loginLogDTO.setLoginTime(new Date());
        loginLogDTO.setGrantType(grantType);
        loginLogDTO.setGrantTerminal(userAgent.isMobile() ? "mobile" : "pc");
        loginLogDTO.setUserAgent(JSONObject.toJSONString(userAgent));
        loginLogDTO.setLoginBrowser(userAgent.getBrowser().getName() + " " + userAgent.getVersion());
        loginLogDTO.setOsInfo(userAgent.getOs().getName());
        loginLogDTO.setIpAddress(IPUtils.getIp(request));
        loginLogDTO.setLoginStatus(status);
        loginLogDTO.setReturnContent(StrUtil.sub(content, 0, 2000));
        loginLogDTO.setLoginAccount(loginAccount);
        sysLoginLogApi.save(loginLogDTO);
        DubboUtils.quickAsyncCall("sysLoginLogApi", "save");
    }

}
