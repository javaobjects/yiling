package com.yiling.admin.system.system.controller;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yiling.admin.system.system.form.LoginForm;
import com.yiling.admin.system.system.vo.AdminVO;
import com.yiling.admin.system.system.vo.LoginInfoVO;
import com.yiling.basic.captcha.api.ImageCaptchaApi;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.enums.UserStatusEnum;

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
 * @date: 2021/5/13
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/auth")
@Api(tags = "认证接口")
public class AuthController extends BaseController {

    @Autowired
    JwtTokenUtils   jwtTokenUtils;
    @Autowired
    RedisService    redisService;
    
    @DubboReference
    AdminApi        adminApi;
    @DubboReference
    ImageCaptchaApi imageCaptchaApi;
    @DubboReference
    SysLoginLogApi sysLoginLogApi;

    @Value("${jwt.expiration}")
    private Long    expiration;
    @Value("${auth.login.image-captcha.required:true}")
    private Boolean imageCaptchaRequired;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result<LoginInfoVO> login(@RequestBody @Valid LoginForm form) {
        LoginInfoVO loginInfo;
        try {
            if (imageCaptchaRequired) {
                boolean checkCaptchaResult = imageCaptchaApi.checkCaptcha(form.getValidateCode(), form.getSerialNo());
                if (!checkCaptchaResult) {
                    throw new BusinessException(UserErrorCode.IMAGE_CAPTCHA_ERROR);
                }
            }

            Admin admin = adminApi.getByUsername(form.getUsername());
            if (admin == null) {
                throw new BusinessException(UserErrorCode.PASSWORD_ERROR);
            }

            if (UserStatusEnum.getByCode(admin.getStatus()) == UserStatusEnum.DISABLED) {
                throw new BusinessException(UserErrorCode.ACCOUNT_STOP);
            }

            boolean result = adminApi.checkPassword(admin.getId(), form.getPassword());
            if (!result) {
                throw new BusinessException(UserErrorCode.PASSWORD_ERROR);
            }

            // 生成token
            String token = jwtTokenUtils.generateToken(new JwtDataModel(AppEnum.ADMIN.getAppId(), admin.getId()));
            // 服务端保存的用户Token
            String userAppTokenKey = RedisKey.generate("token", AppEnum.ADMIN.getAppCode(), "user", admin.getId().toString());
            redisService.set(userAppTokenKey, token, expiration);

            loginInfo = new LoginInfoVO();
            loginInfo.setUserInfo(PojoUtils.map(admin, AdminVO.class));
            loginInfo.setToken(token);

            //保存登录信息
            addLoginInfo(admin.getId(),admin.getName(),form.getUsername(), Constants.LOGIN_STATUS_SUCCESS,JSONObject.toJSONString(loginInfo));

        } catch (BusinessException e) {
            Admin admin = Optional.ofNullable(adminApi.getByUsername(form.getUsername())).orElse(new Admin());
            //保存登录信息
            addLoginInfo(admin.getId(),admin.getName(),form.getUsername(),Constants.LOGIN_STATUS_FAIL,e.getMessage());
            throw new BusinessException(e.getResultCode(),e.getMessage());
        }

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    public Result<BoolObject> logout(@CurrentUser CurrentAdminInfo adminInfo) {
        if (adminInfo == null) {
            return Result.success(new BoolObject(true));
        }

//        String userAppTokenKey = RedisKey.generate("token", AppEnum.ADMIN.getAppCode(), "user", adminInfo.getCurrentUserId().toString());
//        if (redisService.hasKey(userAppTokenKey)) {
//            redisService.del(userAppTokenKey);
//        }
        return Result.success(new BoolObject(true));
    }

    /**
     * 插入登录日志记录
     * @param userId 用户ID
     * @param name 用户名
     * @param loginAccount 用户登录账号
     * @param status 登录状态
     * @param content 返回内容
     */
    private void addLoginInfo(Long userId , String name , String loginAccount, String status ,String content){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));

            SysLoginLogDTO loginLogDTO = new SysLoginLogDTO();
            loginLogDTO.setAppId(AppEnum.ADMIN.getAppCode());
            loginLogDTO.setUserId(userId);
            loginLogDTO.setUserName(name);
            loginLogDTO.setLoginTime(new Date());
            loginLogDTO.setGrantType("password");
            loginLogDTO.setGrantTerminal(userAgent.isMobile() ? "mobile" : "pc");
            loginLogDTO.setUserAgent(JSONObject.toJSONString(userAgent));
            loginLogDTO.setLoginBrowser(userAgent.getBrowser().getName() + " " + userAgent.getVersion());
            loginLogDTO.setOsInfo(userAgent.getOs().getName());
            loginLogDTO.setIpAddress(IPUtils.getIp(request));
            loginLogDTO.setLoginStatus(status);
            loginLogDTO.setReturnContent(StrUtil.sub(content, 0, 2000));
            loginLogDTO.setLoginAccount(loginAccount);
            sysLoginLogApi.save(loginLogDTO);
        }catch (Exception e){
            log.error("运营后台登录日志插入出错：{}",e.getMessage());
        }

    }

}
