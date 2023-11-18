package com.yiling.sales.assistant.app.mr.system.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.pojo.bo.TerminalInfo;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.mr.system.enums.LoginErrorCode;
import com.yiling.sales.assistant.app.mr.system.form.GetSmsVerifyCodeForm;
import com.yiling.sales.assistant.app.mr.system.form.PasswordLoginForm;
import com.yiling.sales.assistant.app.mr.system.form.SmsLoginForm;
import com.yiling.sales.assistant.app.mr.system.vo.LoginInfoVO;
import com.yiling.sales.assistant.app.mr.system.vo.StaffVO;
import com.yiling.user.common.util.Constants;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.api.MrUserRegisterApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.MrUserRegisterDTO;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.dto.request.SaveAppLoginInfoRequest;
import com.yiling.user.system.enums.UserDeregisterAccountStatusEnum;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/12/5
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "认证接口")
public class AuthController {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private RedisService redisService;

    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    SmsApi smsApi;
    @DubboReference
    SysLoginLogApi sysLoginLogApi;
    @DubboReference
    AppLoginInfoApi appLoginInfoApi;
    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;
    @DubboReference
    MrUserRegisterApi mrUserRegisterApi;

    @ApiOperation(value = "获取登录验证码")
    @PostMapping("/getLoginVerifyCode")
    public Result getLoginVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        // 校验手机号状态
        IErrorCode errorCode = this.validateMobile(mobile);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        errorCode = this.validateStaffInfo(staff, null);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.MR_LOGIN);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "手机号+密码登录")
    @PostMapping("/loginPassword")
    public Result<LoginInfoVO> loginPassword(@RequestBody @Valid PasswordLoginForm form) {
        String mobile = form.getMobile();

        // 校验手机号状态
        IErrorCode errorCode = this.validateMobile(mobile);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        Staff staff = staffApi.getByMobile(mobile);

        // 验证用户信息
        errorCode = this.validateStaffInfo(staff, form.getPassword());
        if (errorCode != null) {
            // 保存登录日志
            if (errorCode == LoginErrorCode.ACCOUNT_NOT_EXIST) {
                this.saveLoginLog(0L, "", form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            } else {
                this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            }
            return Result.failed(errorCode);
        }

        // 验证用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ALL);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(staff, userEnterpriseList);
        if (validateResult.isSuccessful()) {
            userEnterpriseList = validateResult.getData();
        } else {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            return Result.failed(validateResult.getCode(), validateResult.getMessage());
        }

        // 生成登录信息
        LoginInfoVO loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.MR_APP);
        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), AppEnum.MR_APP, staff.getId(), AppLoginInfoApi.GRANT_TYPE_PASSWORD);
        // 保存登录日志
        this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_PASSWORD);

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "手机号+短信验证码登录")
    @PostMapping("/loginSms")
    public Result<LoginInfoVO> loginSms(@RequestBody @Valid SmsLoginForm form) {
        String mobile = form.getMobile();
        String verifyCode = form.getVerifyCode();

        // 校验手机号状态
        IErrorCode errorCode = this.validateMobile(mobile);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        // 短信验证码校验
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(mobile, verifyCode, SmsVerifyCodeTypeEnum.MR_LOGIN);
        if (!checkVerifyCodeResult) {
            // 保存登录日志
            this.saveLoginLog(0L, "", form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, LoginErrorCode.VERIFY_CODE_ERROR.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());

        // 验证用户信息
        errorCode = this.validateStaffInfo(staff, null);
        if (errorCode != null) {
            // 保存登录日志
            if (errorCode == LoginErrorCode.ACCOUNT_NOT_EXIST) {
                this.saveLoginLog(0L, "", form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            } else {
                this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            }
            return Result.failed(errorCode);
        }

        // 验证用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ALL);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(staff, userEnterpriseList);
        if (validateResult.isSuccessful()) {
            userEnterpriseList = validateResult.getData();
        } else {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(validateResult.getCode(), validateResult.getMessage());
        }

        // 清理短信验证码
        smsApi.cleanVerifyCode(mobile, SmsVerifyCodeTypeEnum.MR_LOGIN);

        // 生成登录信息
        LoginInfoVO loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.MR_APP);
        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), AppEnum.MR_APP, staff.getId(), AppLoginInfoApi.GRANT_TYPE_VERIFY_CODE);
        // 保存登录日志
        this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_VERIFY_CODE);

        return Result.success(loginInfo);
    }

    /**
     * 验证手机号
     *
     * @param mobile
     */
    private IErrorCode validateMobile(String mobile) {
        MrUserRegisterDTO mrUserRegisterDTO = mrUserRegisterApi.getByMobile(mobile);
        if (mrUserRegisterDTO != null) {
            return LoginErrorCode.ACCOUNT_AUDITING;
        }

        return null;
    }

    /**
     * 验证 staff 信息
     *
     * @param staff
     * @return
     */
    private IErrorCode validateStaffInfo(Staff staff, String inputPassword) {
        if (staff == null) {
            return LoginErrorCode.ACCOUNT_NOT_EXIST;
        }

        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return LoginErrorCode.ACCOUNT_DISABLED;
        }

        if (StrUtil.isNotEmpty(inputPassword)) {
            boolean result = staffApi.checkPassword(staff.getId(), inputPassword);
            if (!result) {
                return LoginErrorCode.PASSWORD_ERROR;
            }
        }

        // 校验注销账号不可登录
        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DEREGISTER) {
            return LoginErrorCode.ACCOUNT_HAD_LOGOUT;
        }

        UserDeregisterAccountDTO deregisterAccountDTO = userDeregisterAccountApi.getByUserId(staff.getId());
        if (Objects.nonNull(deregisterAccountDTO)) {
            if (deregisterAccountDTO.getStatus().equals(UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode())) {
                return LoginErrorCode.ACCOUNT_DEREGISTERING;
            }
        }

        return null;
    }

    private Result<List<EnterpriseDTO>> validateUserEnterpriseList(Staff staff, List<EnterpriseDTO> userEnterpriseList) {
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_EMPTY);
        }

        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> EnterpriseStatusEnum.getByCode(e.getStatus()) == EnterpriseStatusEnum.ENABLED).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_DISABLED);
        }

        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_UNAUDITED);
        }

        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> !EnterpriseTypeEnum.getByCode(e.getType()).isTerminal()).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.TERMAINAL_USER);
        }

        // 校验是否为医药代表
        List<Long> eids = userEnterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        List<EnterpriseEmployeeDTO> userEnterpriseEmployeeDTOList = employeeApi.listByUserIdEids(staff.getId(), eids, EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(userEnterpriseEmployeeDTOList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_EMPLOYEE_DISABLED);
        }
        userEnterpriseEmployeeDTOList = userEnterpriseEmployeeDTOList.parallelStream().filter(e -> EmployeeTypeEnum.MR.getCode().equals(e.getType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseEmployeeDTOList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_EMPLOYEE_IS_NOT_MR);
        }

        final List<Long> newEids = userEnterpriseEmployeeDTOList.stream().map(EnterpriseEmployeeDTO::getEid).distinct().collect(Collectors.toList());
        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> newEids.contains(e.getId())).collect(Collectors.toList());

        return Result.success(userEnterpriseList);
    }

    /**
     * 生成小三元登录信息
     *
     * @param staff
     * @param userEnterpriseList
     * @param appEnum
     * @return
     */
    private LoginInfoVO getLoginInfoVO(Staff staff, List<EnterpriseDTO> userEnterpriseList, AppEnum appEnum) {
        LoginInfoVO loginInfo = new LoginInfoVO();
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));

        EnterpriseDTO userEnterpriseDTO = userEnterpriseList.get(0);
        LoginInfoVO.CurrentEnterpriseVO currentEnterpriseVO = PojoUtils.map(userEnterpriseDTO, LoginInfoVO.CurrentEnterpriseVO.class);
        // 获取用户对应当前企业中的员工信息
        EnterpriseEmployeeDTO userEnterpriseEmployeeDTO = employeeApi.getByEidUserId(userEnterpriseDTO.getId(), staff.getId());
        Long employeeId = userEnterpriseEmployeeDTO.getId();
        currentEnterpriseVO.setEmployeeId(employeeId);
        // 用户是否为企业管理员
        boolean isAdmin = userEnterpriseEmployeeDTO.getAdminFlag() == 1;
        currentEnterpriseVO.setAdminFlag(isAdmin);
        loginInfo.setCurrentEnterpriseInfo(currentEnterpriseVO);
        // 设置token
        loginInfo.setToken(jwtTokenUtils.generateToken(new JwtDataModel(appEnum.getAppId(), staff.getId(), userEnterpriseDTO.getId())));

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", appEnum.getAppCode(), "user", staff.getId().toString());
        redisService.set(userAppTokenKey, loginInfo.getToken(), expiration);

        return loginInfo;
    }

    /**
     * 保存登录日志
     *
     * @param userId 用户ID
     * @param name 用户名
     * @param loginAccount 用户登录账号
     * @param status 登录状态
     * @param content 返回内容
     * @param grantType 登录方式
     */
    private void saveLoginLog(Long userId, String name, TerminalInfo terminalInfo, String loginAccount, String status, String content, String grantType) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

            UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
            String version = StrUtil.isNotEmpty(userAgent.getVersion()) ? " " + userAgent.getVersion() : "";

            SysLoginLogDTO loginLogDTO = new SysLoginLogDTO();
            loginLogDTO.setAppId(AppEnum.MR_APP.getAppCode());
            loginLogDTO.setUserId(userId);
            loginLogDTO.setUserName(name);
            loginLogDTO.setLoginTime(new Date());
            loginLogDTO.setGrantType(grantType);
            loginLogDTO.setGrantTerminal("mobile");
            loginLogDTO.setIpAddress(IPUtils.getIp(request));
            loginLogDTO.setLoginStatus(status);
            loginLogDTO.setReturnContent(StrUtil.sub(content, 0, 2000));
            loginLogDTO.setLoginAccount(loginAccount);

            // 登录浏览器：安卓： app版本 + 渠道号 ， IOS：  app版本
            // 操作系统：IOS：model（型号） + os_version（操作系统版本），安卓：brand（品牌） +  model（型号） + os_version（操作系统版本）+ sdk_version （SDK版本）
            // 用户代理：安卓和IOS均取登录设备信息JSON字符串。
            if (Objects.nonNull(terminalInfo) && terminalInfo.getTerminalType() == 1) {
                loginLogDTO.setLoginBrowser(terminalInfo.getAppVersion() + " " + terminalInfo.getChannelCode());
                loginLogDTO.setOsInfo(terminalInfo.getBrand() + " " + terminalInfo.getModel() + " " + terminalInfo.getOsVersion() + " " + terminalInfo.getSdkVersion());
            } else if (Objects.nonNull(terminalInfo) && terminalInfo.getTerminalType() == 2) {
                loginLogDTO.setLoginBrowser(terminalInfo.getAppVersion());
                loginLogDTO.setOsInfo(terminalInfo.getModel() + " " + terminalInfo.getOsVersion());
            }
            loginLogDTO.setUserAgent(JSONObject.toJSONString(terminalInfo));

            sysLoginLogApi.save(loginLogDTO);
        } catch (Exception e) {
            log.error("保存销售助手APP登录日志出错：{}", e.getMessage());
        }
    }

    private void saveLoginInfo(TerminalInfo terminalInfo, AppEnum appEnum, Long userId, String grantType) {
        if (terminalInfo != null) {
            SaveAppLoginInfoRequest request = PojoUtils.map(terminalInfo, SaveAppLoginInfoRequest.class);
            request.setAppId(appEnum.getAppId().longValue());
            request.setUserId(userId);
            request.setGrantType(grantType);
            request.setLoginTime(new Date());
            request.setOpUserId(userId);
            appLoginInfoApi.saveOrUpdate(request);
        }
    }

}
