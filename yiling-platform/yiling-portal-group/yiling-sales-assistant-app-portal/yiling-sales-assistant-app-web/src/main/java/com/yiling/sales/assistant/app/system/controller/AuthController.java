package com.yiling.sales.assistant.app.system.controller;

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
import com.yiling.sales.assistant.app.system.enums.LoginErrorCode;
import com.yiling.sales.assistant.app.system.form.GetSmsVerifyCodeForm;
import com.yiling.sales.assistant.app.system.form.PasswordLoginForm;
import com.yiling.sales.assistant.app.system.form.SmsLoginForm;
import com.yiling.sales.assistant.app.system.vo.LoginInfoVO;
import com.yiling.sales.assistant.app.system.vo.StaffVO;
import com.yiling.user.common.util.Constants;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.StaffExternaAuditApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.StaffExternaAuditDTO;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/9/22
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
    StaffExternaAuditApi staffExternaAuditApi;
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

    @ApiOperation(value = "获取登录验证码")
    @PostMapping("/getLoginVerifyCode")
    public Result getLoginVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        if (staff != null) {
            if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
                return Result.failed(LoginErrorCode.ACCOUNT_DISABLED);
            }

            // 校验注销账号不可登录
            if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DEREGISTER) {
                return Result.failed(LoginErrorCode.ACCOUNT_HAD_LOGOUT);
            }

            UserDeregisterAccountDTO deregisterAccountDTO = userDeregisterAccountApi.getByUserId(staff.getId());
            if (Objects.nonNull(deregisterAccountDTO)) {
                if (deregisterAccountDTO.getStatus().equals(UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode()) ) {
                    return Result.failed(LoginErrorCode.ACCOUNT_DEREGISTERING);
                }
            }
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_LOGIN);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "手机号+密码登录", response = Result.class)
    @ApiResponses({
            @ApiResponse(code = 100100, message = "手机号不存在")
    })
    @PostMapping("/loginPassword")
    public Result<LoginInfoVO> loginPassword(@RequestBody @Valid PasswordLoginForm form) {
        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff == null) {
            return Result.failed(LoginErrorCode.ACCOUNT_NOT_EXIST);
        }

        // 验证用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ALL);
        // 是否为自然人
        boolean naturalPersonFlag = CollUtil.isEmpty(userEnterpriseList);

        // 验证用户信息
        IErrorCode errorCode = this.validateStaffInfo(staff, form.getPassword(), naturalPersonFlag);
        if (errorCode != null) {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            return Result.failed(errorCode);
        }

        // 非自然人（小三元）需要验证企业信息
        if (!naturalPersonFlag) {
            Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
            if (validateResult.isSuccessful()) {
                userEnterpriseList = validateResult.getData();
            } else {
                // 保存登录日志
                this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_PASSWORD);
                return Result.failed(validateResult.getCode(),validateResult.getMessage());
            }
        }

        // 生成登录信息
        LoginInfoVO loginInfo;
        if (naturalPersonFlag) {
            loginInfo = this.getLoginInfoVO(staff, AppEnum.SALES_ASSISTANT_APP);
        } else {
            loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.SALES_ASSISTANT_APP);
        }
        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), AppEnum.SALES_ASSISTANT_APP, staff.getId(), AppLoginInfoApi.GRANT_TYPE_PASSWORD);
        // 保存登录日志
        this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_PASSWORD);

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "手机号+短信验证码登录")
    @ApiResponses({
            @ApiResponse(code = 100100, message = "手机号不存在 -> 'data':{'token':'asdfaskdlfjasjflaadsfaoiopw'}")
    })
    @PostMapping("/loginSms")
    public Result<LoginInfoVO> loginSms(@RequestBody @Valid SmsLoginForm form) {
        String mobile = form.getMobile();
        String verifyCode = form.getVerifyCode();

        // 短信验证码校验
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(mobile, verifyCode, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_LOGIN);
        if (!checkVerifyCodeResult) {
            // 保存登录日志
            this.saveLoginLog(0L, "", form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, LoginErrorCode.VERIFY_CODE_ERROR.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff == null) {
            String verifyCodeToken = smsApi.getVerifyCodeToken(mobile, verifyCode, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_LOGIN);
            return Result.failed(LoginErrorCode.ACCOUNT_NOT_EXIST.getCode(), LoginErrorCode.ACCOUNT_NOT_EXIST.getMessage(), new LoginInfoVO(verifyCodeToken));
        }

        // 验证用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ALL);
        // 是否为自然人
        boolean naturalPersonFlag = CollUtil.isEmpty(userEnterpriseList);

        // 验证用户信息
        IErrorCode errorCode = this.validateStaffInfo(staff, null, naturalPersonFlag);
        if (errorCode != null) {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(errorCode);
        }

        // 非自然人（小三元）需要验证企业信息
        if (!naturalPersonFlag) {
            Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
            if (validateResult.isSuccessful()) {
                userEnterpriseList = validateResult.getData();
            } else {
                // 保存登录日志
                this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
                return Result.failed(validateResult.getCode(),validateResult.getMessage());
            }
        }

        // 清理短信验证码
        smsApi.cleanVerifyCode(mobile, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_LOGIN);

        // 生成登录信息
        LoginInfoVO loginInfo;
        if (naturalPersonFlag) {
            loginInfo = this.getLoginInfoVO(staff, AppEnum.SALES_ASSISTANT_APP);
        } else {
            loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.SALES_ASSISTANT_APP);
        }
        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), AppEnum.SALES_ASSISTANT_APP, staff.getId(), AppLoginInfoApi.GRANT_TYPE_VERIFY_CODE);
        // 保存登录日志
        this.saveLoginLog(staff.getId(), staff.getName(), form.getTerminalInfo(), form.getMobile(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_VERIFY_CODE);

        return Result.success(loginInfo);
    }

    /**
     * 验证 staff 信息
     *
     * @param staff
     * @return
     */
    private IErrorCode validateStaffInfo(Staff staff, String inputPassword, boolean naturalPersonFlag) {
        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return LoginErrorCode.ACCOUNT_DISABLED;
        }

        if (StrUtil.isNotEmpty(inputPassword)) {
            boolean result = staffApi.checkPassword(staff.getId(), inputPassword);
            if (!result) {
                return LoginErrorCode.PASSWORD_ERROR;
            }
        }

        if (naturalPersonFlag) {
            StaffExternaAuditDTO staffExternaAuditDTO = staffExternaAuditApi.getUserLatestAuditInfo(staff.getId());
            if (staffExternaAuditDTO != null && staffExternaAuditDTO.getAuditStatus() == 1) {
                return LoginErrorCode.PERSONAL_INFO_AUDITING;
            }
        }

        // 校验注销账号不可登录
        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DEREGISTER) {
            return LoginErrorCode.ACCOUNT_HAD_LOGOUT;
        }

        UserDeregisterAccountDTO deregisterAccountDTO = userDeregisterAccountApi.getByUserId(staff.getId());
        if (Objects.nonNull(deregisterAccountDTO)) {
            if (deregisterAccountDTO.getStatus().equals(UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode()) ) {
                return LoginErrorCode.ACCOUNT_DEREGISTERING;
            }
        }

        return null;
    }

    private Result<List<EnterpriseDTO>> validateUserEnterpriseList(List<EnterpriseDTO> userEnterpriseList) {
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

        List<Long> eids = userEnterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        List<EnterprisePlatformDTO> enterprisePlatformDTOList = enterpriseApi.getEnterprisePlatforms(eids);
        Map<Long, EnterprisePlatformDTO> enterprisePlatformDTOMap = enterprisePlatformDTOList.stream().collect(Collectors.toMap(EnterprisePlatformDTO::getEid, Function.identity()));
        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> enterprisePlatformDTOMap.getOrDefault(e.getId(), EnterprisePlatformDTO.newEmptyInstance()).getSalesAssistFlag() == 1).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.PLATFORM_NOT_OPEN);
        }

        List<EnterpriseSalesAreaDTO> enterpriseSalesAreaDTOList = enterpriseApi.listEnterpriseSalesArea(eids);
        Map<Long, EnterpriseSalesAreaDTO> enterpriseSalesAreaDTOMap = enterpriseSalesAreaDTOList.stream().collect(Collectors.toMap(EnterpriseSalesAreaDTO::getEid, Function.identity()));
        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> com.yiling.framework.common.util.Constants.YILING_EID.equals(e.getId()) || enterpriseSalesAreaDTOMap.containsKey(e.getId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_NO_SALESAREA);
        }

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
        // 用户类型
        Integer userType = com.yiling.framework.common.util.Constants.YILING_EID.equals(userEnterpriseDTO.getId()) ? 1 : 2;
        loginInfo.setUserType(userType);
        // 员工类型
        loginInfo.setEmployeeType(userEnterpriseEmployeeDTO.getType());
        // 设置token
        loginInfo.setToken(jwtTokenUtils.generateToken(new JwtDataModel(appEnum.getAppId(), staff.getId(), userType, userEnterpriseDTO.getId(), userEnterpriseDTO.getType(), userEnterpriseDTO.getChannelId(), employeeId, isAdmin)));

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", appEnum.getAppCode(), "user", staff.getId().toString());
        redisService.set(userAppTokenKey, loginInfo.getToken(), expiration);

        return loginInfo;
    }

    /**
     * 生成自然人登录信息
     *
     * @param staff
     * @param appEnum
     * @return
     */
    private LoginInfoVO getLoginInfoVO(Staff staff, AppEnum appEnum) {
        LoginInfoVO loginInfo = new LoginInfoVO();
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));
        loginInfo.setUserType(3);
        loginInfo.setCurrentEnterpriseInfo(null);
        loginInfo.setToken(jwtTokenUtils.generateToken(new JwtDataModel(appEnum.getAppId(), staff.getId(), 3)));

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
            loginLogDTO.setAppId(AppEnum.SALES_ASSISTANT_APP.getAppCode());
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
                loginLogDTO.setLoginBrowser(terminalInfo.getAppVersion() + " " + terminalInfo.getChannelCode() );
                loginLogDTO.setOsInfo(terminalInfo.getBrand() + " " + terminalInfo.getModel() + " " + terminalInfo.getOsVersion() + " " + terminalInfo.getSdkVersion());
            } else if (Objects.nonNull(terminalInfo) && terminalInfo.getTerminalType() == 2){
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
