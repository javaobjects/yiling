package com.yiling.user.web.auth.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.util.Constants;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.SaveAppLoginInfoRequest;
import com.yiling.user.web.auth.enums.LoginErrorCode;
import com.yiling.user.web.auth.form.B2BAppPasswordLoginForm;
import com.yiling.user.web.auth.form.B2BAppSmsLoginForm;
import com.yiling.user.web.auth.form.GetSmsVerifyCodeForm;
import com.yiling.user.web.auth.vo.B2BLoginInfoVO;
import com.yiling.user.web.auth.vo.StaffVO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B APP认证 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/4/7
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/auth/b2b/app")
@Api(tags = "B2B APP认证接口")
public class B2BAppAuthController extends BaseB2BAuthController {

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 虚拟账号过期时间（秒）：默认为40天（3600 * 24 * 40 = 3456000秒）
     */
    @Value("${common.user.virtualAccount.expired:3456000}")
    private Integer virtualExpired;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private RedisService redisService;

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    SmsApi smsApi;
    @DubboReference(async = true)
    AppLoginInfoApi appLoginInfoApi;
    @DubboReference(async = true)
    SysLoginLogApi sysLoginLogApi;

    @ApiOperation(value = "手机号+密码登录")
    @PostMapping("/loginPassword")
    public Result<B2BLoginInfoVO> loginPassword(@RequestBody @Valid B2BAppPasswordLoginForm form) {
        Staff staff = staffApi.getByMobile(form.getMobile());
        // 校验用户信息
        IErrorCode errorCode = this.validateStaff(staff, form.getPassword());
        if (errorCode != null) {
            Long userId = staff != null ? staff.getId() : 0L;
            String name = staff != null ? staff.getName() : "";
            // 保存登录日志
            this.saveLoginLog(userId, name, form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            return Result.failed(errorCode);
        }

        // 获取用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ENABLED);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
        if (validateResult.isSuccessful()) {
            userEnterpriseList = validateResult.getData();
        } else {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_PASSWORD);
            return Result.failed(validateResult.getCode(),validateResult.getMessage());
        }

        // 是否虚拟账号登录
        boolean specialFlag = userApi.checkSpecialPhone(form.getMobile());
        B2BLoginInfoVO loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.B2B_APP, specialFlag);
        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), AppEnum.B2B_APP, staff.getId(), AppLoginInfoApi.GRANT_TYPE_PASSWORD);
        // 保存登录日志
        this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_PASSWORD);

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "获取登录验证码")
    @PostMapping("/getLoginVerifyCode")
    public Result getLoginVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        IErrorCode errorCode = this.validateStaff(staff, null);
        if (errorCode != null) {
            return Result.failed(errorCode);
        }

        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ENABLED);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
        if (!validateResult.isSuccessful()) {
            return Result.failed(validateResult.getCode(), validateResult.getMessage());
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.B2B_LOGIN);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "手机号+短信验证码登录")
    @PostMapping("/loginSms")
    public Result<B2BLoginInfoVO> loginSms(@RequestBody @Valid B2BAppSmsLoginForm form) {
        // 短信验证码校验
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_LOGIN);
        if (!checkVerifyCodeResult) {
            this.saveLoginLog(0L, "", form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_FAIL, LoginErrorCode.VERIFY_CODE_ERROR.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        // 校验用户信息
        IErrorCode errorCode = this.validateStaff(staff, null);
        if (errorCode != null) {
            Long userId = staff != null ? staff.getId() : 0L;
            String name = staff != null ? staff.getName() : "";
            // 保存登录日志
            this.saveLoginLog(userId, name, form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_FAIL, errorCode.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(errorCode);
        }

        // 获取用户企业列表
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staff.getId(), EnableStatusEnum.ENABLED);
        Result<List<EnterpriseDTO>> validateResult = this.validateUserEnterpriseList(userEnterpriseList);
        if (validateResult.isSuccessful()) {
            userEnterpriseList = validateResult.getData();
        } else {
            // 保存登录日志
            this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_FAIL, validateResult.getMessage(), Constants.GRANT_TYPE_VERIFY_CODE);
            return Result.failed(validateResult.getCode(), validateResult.getMessage());
        }

        B2BLoginInfoVO loginInfo = this.getLoginInfoVO(staff, userEnterpriseList, AppEnum.B2B_APP, false);
        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), AppEnum.B2B_APP, staff.getId(), AppLoginInfoApi.GRANT_TYPE_VERIFY_CODE);
        // 保存登录日志
        this.saveLoginLog(staff.getId(), staff.getName(), form.getMobile(), form.getTerminalInfo(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfo), Constants.GRANT_TYPE_VERIFY_CODE);
        // 清理短信验证码
        smsApi.cleanVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.B2B_LOGIN);

        return Result.success(loginInfo);
    }



    private B2BLoginInfoVO getLoginInfoVO(Staff staff, List<EnterpriseDTO> userEnterpriseList, AppEnum appEnum, boolean specialFlag) {
        B2BLoginInfoVO loginInfo = new B2BLoginInfoVO();
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));
        loginInfo.setUserEnterpriseList(PojoUtils.map(userEnterpriseList, SimpleEnterpriseVO.class));

        EnterpriseDTO defaultEnterpriseDTO = userEnterpriseList.get(0);
        B2BLoginInfoVO.CurrentEnterpriseVO currentEnterpriseVO = PojoUtils.map(defaultEnterpriseDTO, B2BLoginInfoVO.CurrentEnterpriseVO.class);
        // 当前企业对应的员工信息
        EnterpriseEmployeeDTO defaultEnterpriseEmployeeDTO = employeeApi.getByEidUserId(defaultEnterpriseDTO.getId(), staff.getId());
        Long employeeId = defaultEnterpriseEmployeeDTO.getId();
        currentEnterpriseVO.setEmployeeId(employeeId);
        // 用户是否为企业管理员
        boolean isAdmin = defaultEnterpriseEmployeeDTO.getAdminFlag() == 1;
        currentEnterpriseVO.setAdminFlag(isAdmin);
        loginInfo.setCurrentEnterpriseInfo(currentEnterpriseVO);

        //如果为虚拟账号登录，校验是否已经超过指定天数：超过需要强制绑定新号码，没有超过则可以继续当做正常号码使用
        if (specialFlag) {
            EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(defaultEnterpriseDTO.getId(), staff.getId());
            // 设置虚拟账号需绑定标识
            Date virtualCreateTime = employeeDTO.getCreateTime();
            loginInfo.setSpecialPhone(true);
            loginInfo.setMustChangeBind(DateUtil.compare(DateUtil.offsetSecond(virtualCreateTime, virtualExpired), new Date()) < 0);
        } else {
            // 设置虚拟账号需绑定标识
            loginInfo.setSpecialPhone(false);
            loginInfo.setMustChangeBind(false);
        }
        // 设置token
        String token = jwtTokenUtils.generateToken(new JwtDataModel(appEnum.getAppId(), staff.getId(), defaultEnterpriseDTO.getId(),
                defaultEnterpriseDTO.getType(), defaultEnterpriseDTO.getChannelId(), employeeId, isAdmin));
        loginInfo.setToken(token);

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", appEnum.getAppCode(), "user", staff.getId().toString());
        redisService.set(userAppTokenKey, loginInfo.getToken(), expiration);

        return loginInfo;
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
            DubboUtils.quickAsyncCall("appLoginInfoApi", "saveOrUpdate");
        }
    }

    /**
     * 插入登录日志记录
     *
     * @param userId 用户ID
     * @param name 用户名
     * @param loginAccount 用户登录账号
     * @param terminalInfo 登录设备信息
     * @param status 登录状态
     * @param content 返回内容
     * @param grantType 登录方式
     */
    private void saveLoginLog(Long userId, String name, String loginAccount, TerminalInfo terminalInfo, String status, String content, String grantType) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        SysLoginLogDTO loginLogDTO = new SysLoginLogDTO();
        loginLogDTO.setAppId(AppEnum.B2B_APP.getAppCode());
        loginLogDTO.setUserId(userId).setUserName(name);
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
        DubboUtils.quickAsyncCall("sysLoginLogApi", "save");
    }

}
