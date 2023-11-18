package com.yiling.hmc.wechat.controller;

import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.hmc.config.HmcWebProperties;
import com.yiling.hmc.config.TencentServiceConfig;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.hmc.tencent.dto.PortraitGetDTO;
import com.yiling.hmc.tencent.dto.TencentImSigDTO;
import com.yiling.hmc.tencent.dto.UserProfileItemDTO;
import com.yiling.hmc.wechat.enums.HmcActivitySourceEnum;
import com.yiling.hmc.wechat.enums.SourceEnum;
import com.yiling.hmc.wechat.form.WxSignatureForm;
import com.yiling.hmc.wechat.utils.GenerateUserSignature;
import com.yiling.hmc.wechat.vo.TencentIMSigVO;
import com.yiling.user.system.bo.CurrentUserInfo;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.pojo.bo.TerminalInfo;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.wechat.form.WeChatLoginForm;
import com.yiling.hmc.wechat.form.WxMpLoginForm;
import com.yiling.hmc.wechat.form.WxSessionForm;
import com.yiling.hmc.wechat.vo.LoginInfoVO;
import com.yiling.hmc.wechat.vo.SessionVO;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.HmcUserAppApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.CreateHmcUserAppRequest;
import com.yiling.user.system.dto.request.CreateHmcUserRequest;
import com.yiling.user.system.dto.request.SaveAppLoginInfoRequest;
import com.yiling.user.system.dto.request.UpdateHmcUserRequest;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * 微信登录控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */
@Slf4j
@RestController
@RequestMapping("/wx/auth")
@Api(tags = "微信登录控制器")
public class WeChatAuthController {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private RedisService redisService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    HmcUserAppApi hmcUserAppApi;

    @DubboReference(async = true)
    AppLoginInfoApi appLoginInfoApi;

    @DubboReference(async = true)
    SysLoginLogApi sysLoginLogApi;

    @Autowired
    HmcWebProperties hmcWebProperties;

    @Autowired
    TencentServiceConfig tencentServiceConfig;

    @DubboReference
    TencentIMApi tencentIMApi;

    @ApiOperation(value = "获取会话信息")
    @PostMapping("/getSession")
    @Log(title = "获取会话信息", businessType = BusinessTypeEnum.OTHER)
    public Result<SessionVO> getSession(@RequestBody @Valid WxSessionForm form) {
        log.info("微信获取会话信息,请求参数 ： {}", JSONUtil.toJsonStr(form));

        WxMaJscode2SessionResult session = null;
        try {
            // 1、获取微信用户session
            session = wxMaService.getUserService().getSessionInfo(form.getCode());
            log.info("session 信息：{},token信息：{}", session, wxMaService.getAccessToken());
        } catch (WxErrorException e) {
            log.error("调用微信会话接口报错:{}", ExceptionUtils.getStackTrace(e));
            return Result.failed("获取微信会话失败，请重试");
        }
        if (Objects.isNull(session)) {
            return Result.failed("微信获取会话失败，请重试");
        }

        SessionVO sessionVO = new SessionVO();

        // 查询账号信息
        HmcUser hmcUser = hmcUserApi.getByUnionId(session.getUnionid());

        if (Objects.isNull(hmcUser)) {
            String uk = "uk_" + session.getOpenid();
            String ukKey = RedisKey.generate("wechat", "uk", uk);
            redisService.set(ukKey, JSONUtil.toJsonStr(session), 86400);
            sessionVO.setUk(uk);
            return Result.success(sessionVO);
        }

        HmcUserApp hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), wxMaService.getWxMaConfig().getAppid());
        if (Objects.isNull(hmcUserApp)) {
            String uk = "uk_" + session.getOpenid();
            String ukKey = RedisKey.generate("wechat", "uk", uk);
            redisService.set(ukKey, JSONUtil.toJsonStr(session), 86400);
            sessionVO.setUk(uk);
            return Result.success(sessionVO);
        }
        sessionVO.setLoginInfo(this.getLoginInfo(hmcUser, hmcUserApp, form.getTerminalInfo(), AppEnum.HMC_MINI_PROGRAM));
        return Result.success(sessionVO);
    }


    @ApiOperation(value = "微信登录")
    @ApiResponses({
            @ApiResponse(code = 100000, message = "uk或sessionKey过期，请重新发起登录流程")
    })
    @PostMapping("/login")
    @Log(title = "微信登录", businessType = BusinessTypeEnum.OTHER)
    public Result<LoginInfoVO> login(@RequestBody @Valid WeChatLoginForm form) {
        log.info("微信登录,请求参数 ： {}", JSONUtil.toJsonStr(form));

        if (StrUtil.isBlank(form.getUk())) {
            return Result.failed(10002, "登录参数UK为空，请重新获取会话");
        }

        form.convertQrCodeValue();

        String ukKey = RedisKey.generate("wechat", "uk", form.getUk());
        if (!redisService.hasKey(ukKey)) {
            return Result.failed(100000, "uk已过期，请重新发起登录流程");
        }

        String sessionJsonStr = (String) redisService.get(ukKey);
        log.info("sessionJsonStr: {}", sessionJsonStr);
        WxMaJscode2SessionResult session = JSONUtil.toBean(sessionJsonStr, WxMaJscode2SessionResult.class);

//        WxMaUserInfo userInfo;
//        try {
//            // 解密用户信息
//            userInfo = wxMaService.getUserService().getUserInfo(session.getSessionKey(), form.getEncryptedData(), form.getIv());
//            log.info("微信 userInfo 信息：{}", userInfo);
//        } catch (Exception e) {
//            log.error("解密用户信息失败：{}", e.getMessage(), e);
//            return Result.failed(100000, "解密用户信息失败");
//        }

        WxMaPhoneNumberInfo phoneInfo;
        try {
            // 解密手机号码信息
            phoneInfo = wxMaService.getUserService().getPhoneNoInfo(session.getSessionKey(), form.getPhoneEncryptedData(), form.getPhoneIv());
            log.info("微信 phoneInfo 信息：{}", phoneInfo);
        } catch (Exception e) {
            log.error("解密手机信息失败：{}", e.getMessage(), e);
            return Result.failed(100000, "解密手机信息失败");
        }

        HmcUser hmcUser = hmcUserApi.getByUnionId(session.getUnionid());
        if (hmcUser == null) {
            CreateHmcUserRequest request = new CreateHmcUserRequest();
            String nickName = Constants.HZ_NICKNAME_PREFIX + RandomUtil.randomNumbers(8);
            request.setNickName(nickName);
            request.setMobile(phoneInfo.getPhoneNumber());
            request.setOpenId(session.getOpenid());
            request.setUnionId(session.getUnionid());
            request.setRegisterSource(form.getSource());
            request.setInviteEid(form.getSellerEid());
            request.setInviteUserId(form.getSellerUserId());
            request.setActivityId(form.getActivityId());
            request.setActivitySource(form.getActivitySource());
            request.setGender(0);
            request.setOpUserId(0L);
            hmcUserApi.create(request);
        }
//        else {
//            UpdateHmcUserRequest request = new UpdateHmcUserRequest();
//            request.setUserId(hmcUser.getId());
//            request.setNickName(userInfo.getNickName());
//            request.setAvatarUrl(userInfo.getAvatarUrl());
//            request.setGender(0);
//            request.setOpUserId(0L);
//            hmcUserApi.update(request);
//        }

        hmcUser = hmcUserApi.getByUnionId(session.getUnionid());

        HmcUserApp hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), wxMaService.getWxMaConfig().getAppid());
        if (Objects.isNull(hmcUserApp)) {
            CreateHmcUserAppRequest userAppRequest = new CreateHmcUserAppRequest();
            userAppRequest.setUserId(hmcUser.getUserId());
            userAppRequest.setAppId(wxMaService.getWxMaConfig().getAppid());
            userAppRequest.setOpenId(session.getOpenid());
            hmcUserAppApi.createHmcUserApp(userAppRequest);
            hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), wxMaService.getWxMaConfig().getAppid());
        }

        LoginInfoVO loginInfo = this.getLoginInfo(hmcUser, hmcUserApp, form.getTerminalInfo(), AppEnum.HMC_MINI_PROGRAM);
        return Result.success(loginInfo);
    }

    @ApiOperation(value = "微信公众号登录")
    @PostMapping("/mpLogin")
    @Log(title = "微信公众号登录", businessType = BusinessTypeEnum.OTHER)
    public Result<LoginInfoVO> mpLogin(@RequestBody @Valid WxMpLoginForm form) {
        log.info("微信公众号登录,请求参数 ： {}", JSONUtil.toJsonStr(form));

        // 1、根据授权码code获取oAuth2 授权token
        WxOAuth2AccessToken accessToken = null;
        try {
            accessToken = wxMpService.getOAuth2Service().getAccessToken(form.getCode());
        } catch (WxErrorException e) {
            log.error("调用微信公众号授权获取accessToken接口报错:{}", ExceptionUtils.getStackTrace(e));
        }
        if (Objects.isNull(accessToken)) {
            return Result.failed("获取微信公众号授权失败，请重试");
        }

        // 2、根据accessToken获取用户信息
        WxOAuth2UserInfo userInfo = null;
        try {
            userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
            log.info("微信授权获取用户信息返回：{}", JSONUtil.toJsonStr(userInfo));
        } catch (WxErrorException e) {
            log.error("调用微信公众号获取用户信息接口报错:{}", ExceptionUtils.getStackTrace(e));
            return Result.failed("调用微信公众号获取用户信息失败");
        }
        if (Objects.isNull(userInfo)) {
            return Result.failed("获取微信公众号用户信息失败，请重试");
        }

        HmcUser hmcUser = hmcUserApi.getByUnionId(userInfo.getUnionId());
        if (hmcUser == null) {
            CreateHmcUserRequest request = new CreateHmcUserRequest();
            request.setNickName(userInfo.getNickname());
            request.setAvatarUrl(userInfo.getHeadImgUrl());
            request.setUnionId(userInfo.getUnionId());
            request.setRegisterSource(SourceEnum.NATURE.getType());
            request.setOpUserId(0L);
            // 如果来自于八子补肾活动
            if (HmcActivitySourceEnum.BA_ZI_BU_SHEN.getType().equals(form.getActivitySource())) {
                request.setActivitySource(HmcActivitySourceEnum.BA_ZI_BU_SHEN.getType());
                request.setRegisterSource(SourceEnum.DOCTOR.getType());
                request.setInviteUserId(form.getDoctorId());
                request.setActivityId(form.getActivityId());
            }
            hmcUserApi.create(request);
        } else {
            UpdateHmcUserRequest request = new UpdateHmcUserRequest();
            request.setUserId(hmcUser.getId());
            request.setNickName(userInfo.getNickname());
            request.setAvatarUrl(userInfo.getHeadImgUrl());
            request.setOpUserId(0L);
            hmcUserApi.update(request);
        }

        hmcUser = hmcUserApi.getByUnionId(userInfo.getUnionId());

        HmcUserApp hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), wxMpService.getWxMpConfigStorage().getAppId());
        if (Objects.isNull(hmcUserApp)) {
            CreateHmcUserAppRequest userAppRequest = new CreateHmcUserAppRequest();
            userAppRequest.setUserId(hmcUser.getUserId());
            userAppRequest.setAppId(wxMpService.getWxMpConfigStorage().getAppId());
            userAppRequest.setOpenId(userInfo.getOpenid());
            hmcUserAppApi.createHmcUserApp(userAppRequest);
            hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), wxMpService.getWxMpConfigStorage().getAppId());
        }

        LoginInfoVO loginInfo = this.getLoginInfo(hmcUser, hmcUserApp, form.getTerminalInfo(), AppEnum.HMC_MINI_PROGRAM);
        return Result.success(loginInfo);

    }

    @ApiOperation(value = "获取微信公众号签名")
    @PostMapping("/mpSignature")
    @Log(title = "微信公众号授权", businessType = BusinessTypeEnum.OTHER)
    public Result<WxJsapiSignature> mpSignature(@RequestBody @Valid WxSignatureForm form) {
        WxJsapiSignature resDto = new WxJsapiSignature();
        try {
            WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(form.getUrl());
            resDto.setAppId(jsapiSignature.getAppId());
            resDto.setNonceStr(jsapiSignature.getNonceStr());
            resDto.setSignature(jsapiSignature.getSignature());
            resDto.setTimestamp(jsapiSignature.getTimestamp());
            resDto.setUrl(jsapiSignature.getUrl());
        } catch (WxErrorException e) {
            log.error("获取微信公众号签名失败：{}", e.getMessage(), e);
            return Result.failed("获取微信公众号签名失败");
        }
        return Result.success(resDto);
    }

    @ApiOperation(value = "获取腾讯IM通讯密码")
    @GetMapping("/getTencentIMSig")
    @Log(title = "获取腾讯IM通讯密码", businessType = BusinessTypeEnum.OTHER)
    public Result<TencentIMSigVO> getTencentIMSig(@CurrentUser CurrentUserInfo currentUser) {
        // 生成腾讯IM密码
        TencentImSigDTO tencentImSigDTO = tencentIMApi.getTencentIMSig(currentUser.getCurrentUserId(), 2);

        // 获取资料
        UserProfileItemDTO profileItemDTO = tencentIMApi.portraitGet(currentUser.getCurrentUserId(), 2);
        // 如果或者结果为空 -> 设置头像、昵称属性
        if (Objects.isNull(profileItemDTO) || CollUtil.isEmpty(profileItemDTO.getProfileItem())) {
            tencentIMApi.portraitSet(currentUser.getCurrentUserId(), 2);
        }
        return Result.success(PojoUtils.map(tencentImSigDTO, TencentIMSigVO.class));
    }

    private LoginInfoVO getLoginInfo(HmcUser hmcUser, HmcUserApp hmcUserApp, TerminalInfo terminalInfo, AppEnum appEnum) {
        // 如果昵称是HZ开头，则返回默认头像
        String avatarUrl = hmcUser.getAvatarUrl();
        if (StrUtil.isNotBlank(hmcUser.getNickName()) && hmcUser.getNickName().startsWith(Constants.HZ_NICKNAME_PREFIX)) {
            avatarUrl = Constants.HZ_DEFAULT_AVATAR;
        }
        // 设置登录用户信息
        LoginInfoVO loginInfoVO = LoginInfoVO.builder()
                .userId(hmcUser.getUserId())
                .nickName(hmcUser.getNickName())
                .avatarUrl(avatarUrl)
                .mobile(hmcUser.getMobile())
                .appId(hmcUserApp.getAppId())
                .openId(hmcUserApp.getOpenId())
                .miniProgramOpenId(hmcUserApp.getOpenId())
                .unionId(hmcUser.getUnionId())
                .inviteUserId(hmcUser.getInviteUserId())
                .build();

        // 设置token
        String token = jwtTokenUtils.generateToken(new JwtDataModel(appEnum.getAppId(), hmcUser.getId()));
        String tokenKey = RedisKey.generate("token", appEnum.getAppCode(), "user", hmcUser.getId().toString());
        redisService.set(tokenKey, token, expiration);
        loginInfoVO.setToken(token);

        // 保存登录日志
        this.saveLoginLog(hmcUser.getId(), hmcUser.getNickName(), hmcUser.getUnionId(), Constants.LOGIN_STATUS_SUCCESS, JSONObject.toJSONString(loginInfoVO), Constants.GRANT_TYPE_WECHAT, appEnum);
        // 保存登录信息
        this.saveLoginInfo(terminalInfo, appEnum, hmcUser.getId(), Constants.GRANT_TYPE_WECHAT);
        // 登录成功消息
        this.sendLoginMessage(hmcUser.getId());
        // 生成腾讯IM密码
        String signature = tencentIMApi.getTencentIMSig(hmcUser.getId(), 2).getTencentIMUserSig();
        loginInfoVO.setTencentIMUserSig(signature);

        return loginInfoVO;
    }

    private void saveLoginLog(Long userId, String name, String loginAccount, String status, String content, String grantType, AppEnum appEnum) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String version = StrUtil.isNotEmpty(userAgent.getVersion()) ? " " + userAgent.getVersion() : "";

        SysLoginLogDTO loginLogDTO = new SysLoginLogDTO();
        loginLogDTO.setAppId(appEnum.getAppCode());
        loginLogDTO.setUserId(userId);
        loginLogDTO.setUserName(name);
        loginLogDTO.setLoginTime(new Date());
        loginLogDTO.setGrantType(grantType);
        loginLogDTO.setGrantTerminal("mobile");
        loginLogDTO.setUserAgent(JSONObject.toJSONString(userAgent));
        loginLogDTO.setLoginBrowser(userAgent.getBrowser().getName() + version);
        loginLogDTO.setOsInfo(userAgent.getOs().getName());
        loginLogDTO.setIpAddress(IPUtils.getIp(request));
        loginLogDTO.setLoginStatus(status);
        loginLogDTO.setReturnContent(content);
        loginLogDTO.setLoginAccount(loginAccount);

        sysLoginLogApi.save(loginLogDTO);
        DubboUtils.quickAsyncCall("sysLoginLogApi", "save");
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

    private void sendLoginMessage(Long userId) {
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_HMC_LOGIN, "", userId.toString());
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }


}
