package com.yiling.open.cms.user.controller;

import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.cms.user.form.QueryWxUserForm;
import com.yiling.user.system.api.HmcUserAppApi;
import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.TerminalInfo;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.wechat.enums.SourceEnum;
import com.yiling.open.cms.user.form.WxSaveLogForm;
import com.yiling.open.cms.user.form.WxSyncUserInfoForm;
import com.yiling.open.cms.user.vo.HmcUserInfoVO;
import com.yiling.user.common.util.Constants;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * hmcUserController
 *
 * @author: fan.shen
 * @data: 2022/8/26
 */
@Api(tags = "C端用户控制器")
@RestController
@RequestMapping("/hmcUser")
@Slf4j
public class HmcUserController extends BaseController {

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    HmcUserAppApi hmcUserAppApi;

    @DubboReference(async = true)
    AppLoginInfoApi appLoginInfoApi;

    @DubboReference(async = true)
    SysLoginLogApi sysLoginLogApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @ApiOperation(value = "根据userId获取用户信息")
    @GetMapping("/getByUserId")
    public Result<HmcUserInfoVO> getUserInfo(@NotNull @RequestParam Long userId, @NotNull @RequestParam String appId) {
        return Result.success(PojoUtils.map(hmcUserApi.getByIdAndAppId(userId, appId), HmcUserInfoVO.class));
    }

    @ApiOperation(value = "根据openId获取用户信息")
    @GetMapping("/getByOpenId")
    public Result<HmcUserInfoVO> getByOpenId(@NotNull @RequestParam String openId) {
        return Result.success(PojoUtils.map(hmcUserApi.getByOpenId(openId), HmcUserInfoVO.class));
    }

    @ApiOperation(value = "根据unionId获取用户信息")
    @GetMapping("/getByUnionId")
    public Result<HmcUserInfoVO> getByUnionId(@NotNull @RequestParam String appId, @NotNull @RequestParam String unionId) {
        return Result.success(PojoUtils.map(hmcUserApi.getByUnionId(unionId), HmcUserInfoVO.class));
    }

    @ApiOperation(value = "同步用户信息")
    @PostMapping("/syncUserInfo")
    @Log(title = "同步用户信息", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcUserInfoVO> syncUserInfo(@RequestBody @Valid WxSyncUserInfoForm form) {
        log.info("同步用户信息,请求参数 ： {}", JSONUtil.toJsonStr(form));
        HmcUser hmcUser = hmcUserApi.getByUnionId(form.getUnionId());
        if (hmcUser == null) {
            CreateHmcUserRequest request = new CreateHmcUserRequest();
            request.setNickName(form.getNickName());
            request.setAvatarUrl(form.getAvatarUrl());
            request.setMobile(form.getPhoneNumber());
            request.setOpenId(form.getOpenId());
            request.setUnionId(form.getUnionId());
            request.setRegisterSource(SourceEnum.YL_INTERNET_HOSPITAL.getType());
            request.setGender(Convert.toInt(form.getGender(), 0));
            request.setOpUserId(0L);
            hmcUserApi.create(request);
        }
//        else {
//            UpdateHmcUserRequest request = new UpdateHmcUserRequest();
//            request.setUserId(hmcUser.getId());
//            request.setNickName(form.getNickName());
//            request.setAvatarUrl(form.getAvatarUrl());
//            request.setGender(Convert.toInt(form.getGender(), 0));
//            request.setOpUserId(0L);
//            hmcUserApi.update(request);
//        }

        hmcUser = hmcUserApi.getByUnionId(form.getUnionId());
        HmcUserApp hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), form.getAppId());
        if (Objects.isNull(hmcUserApp)) {
            CreateHmcUserAppRequest userAppRequest = new CreateHmcUserAppRequest();
            userAppRequest.setUserId(hmcUser.getUserId());
            userAppRequest.setAppId(form.getAppId());
            userAppRequest.setOpenId(form.getOpenId());
            hmcUserAppApi.createHmcUserApp(userAppRequest);
            hmcUserApp = hmcUserAppApi.getByUserIdAndAppId(hmcUser.getUserId(), form.getAppId());
        }
        hmcUser.setAppId(hmcUserApp.getAppId());
        hmcUser.setMiniProgramOpenId(hmcUserApp.getOpenId());

        // 如果昵称是HZ开头，则返回默认头像
        String avatarUrl = hmcUser.getAvatarUrl();
        if (StrUtil.isNotBlank(hmcUser.getNickName()) && hmcUser.getNickName().startsWith(Constants.HZ_NICKNAME_PREFIX)) {
            avatarUrl = Constants.HZ_DEFAULT_AVATAR;
        }

        HmcUserInfoVO userInfoVO = PojoUtils.map(hmcUser, HmcUserInfoVO.class);
        userInfoVO.setAvatarUrl(avatarUrl);
        log.info("[syncUserInfo]同步用户信息返回参数：{}", JSONUtil.toJsonStr(userInfoVO));
        return Result.success(userInfoVO);
    }

    @ApiOperation(value = "保存登录日志")
    @PostMapping("/saveLog")
    @Log(title = "保存登录日志", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> saveLog(@RequestBody @Valid WxSaveLogForm form) {
        HmcUser hmcUser = hmcUserApi.getByIdAndAppId(form.getUserId(), form.getAppId());
        if (Objects.isNull(hmcUser)) {
            log.info("根据userId未获取到用户信息,参数：{}", JSONUtil.toJsonStr(form));
            return Result.failed("根据userId未获取到用户信息");
        }

        // 保存登录日志
        this.saveLoginLog(hmcUser.getId(), hmcUser.getNickName(), hmcUser.getUnionId(), JSONObject.toJSONString(hmcUser));

        // 保存登录信息
        this.saveLoginInfo(form.getTerminalInfo(), hmcUser.getId());

        // 登录成功消息
        this.sendLoginMessage(hmcUser.getId());

        return Result.success();
    }

    @ApiOperation(value = "查询微信小程序用户")
    @PostMapping("/queryWxUserPage")
    @Log(title = "查询微信小程序用户", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcUserInfoVO>> queryWxUserPage(@RequestBody @Valid QueryWxUserForm form) {
        QueryHmcUserPageListRequest request = PojoUtils.map(form, QueryHmcUserPageListRequest.class);
        if (Objects.nonNull(request.getRegistEndTime())) {
            request.setRegistEndTime(DateUtil.endOfDay(request.getRegistEndTime()));
        }
        Page<HmcUser> hmcUserPage = hmcUserApi.pageList(request);
        return Result.success(PojoUtils.map(hmcUserPage, HmcUserInfoVO.class));
    }

    /**
     * 保存登录日志
     *
     * @param userId
     * @param name
     * @param loginAccount
     * @param content
     */
    private void saveLoginLog(Long userId, String name, String loginAccount, String content) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String version = StrUtil.isNotEmpty(userAgent.getVersion()) ? " " + userAgent.getVersion() : "";

        SysLoginLogDTO loginLogDTO = new SysLoginLogDTO();
        loginLogDTO.setAppId(AppEnum.HMC_MINI_PROGRAM.getAppCode());
        loginLogDTO.setUserId(userId);
        loginLogDTO.setUserName(name);
        loginLogDTO.setLoginTime(new Date());
        loginLogDTO.setGrantType(Constants.GRANT_TYPE_WECHAT);
        loginLogDTO.setGrantTerminal("mobile");
        loginLogDTO.setUserAgent(JSONObject.toJSONString(userAgent));
        loginLogDTO.setLoginBrowser(userAgent.getBrowser().getName() + version);
        loginLogDTO.setOsInfo(userAgent.getOs().getName());
        loginLogDTO.setIpAddress(IPUtils.getIp(request));
        loginLogDTO.setLoginStatus(Constants.LOGIN_STATUS_SUCCESS);
        loginLogDTO.setReturnContent(content);
        loginLogDTO.setLoginAccount(loginAccount);

        sysLoginLogApi.save(loginLogDTO);
        DubboUtils.quickAsyncCall("sysLoginLogApi", "save");
    }

    /**
     * 保存登录信息
     *
     * @param terminalInfo
     * @param userId
     */
    private void saveLoginInfo(TerminalInfo terminalInfo, Long userId) {
        if (terminalInfo != null) {
            SaveAppLoginInfoRequest request = PojoUtils.map(terminalInfo, SaveAppLoginInfoRequest.class);
            request.setAppId(AppEnum.HMC_MINI_PROGRAM.getAppId().longValue());
            request.setUserId(userId);
            request.setGrantType(Constants.GRANT_TYPE_WECHAT);
            request.setLoginTime(new Date());
            request.setOpUserId(userId);
            appLoginInfoApi.saveOrUpdate(request);
            DubboUtils.quickAsyncCall("appLoginInfoApi", "saveOrUpdate");
        }
    }

    /**
     * 发送登录消息
     *
     * @param userId
     */
    private void sendLoginMessage(Long userId) {
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_HMC_LOGIN, "", userId.toString());
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }


}
