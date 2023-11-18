package com.yiling.sjms.system.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.sjms.config.ClientConfig;
import com.yiling.sjms.config.SsoConfig;
import com.yiling.sjms.system.enums.SsoErrorCode;
import com.yiling.sjms.system.form.SsoCallbackForm;
import com.yiling.sjms.util.SsoSignUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.bo.SjmsUser;
import com.yiling.user.system.dto.request.CreateSjmsUserRequest;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 单点登录 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/sso")
@Api(tags = "单点登录接口")
public class SsoController extends BaseController {

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    private SsoConfig ssoConfig;
    @Autowired
    private ClientConfig clientConfig;
    @Autowired
    HttpServletResponse response;
    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @DubboReference
    SjmsUserApi sjmsUserApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @GetMapping("/test")
    public Result test() {
        return Result.success();
    }

    @ApiOperation(value = "SSO回调接口")
    @GetMapping("/callback")
    public void callback(@Valid SsoCallbackForm form) throws IOException {
        try {
            log.debug("SSO回调参数：{}", JSONUtil.toJsonStr(form));

            // 校验签名
            this.verifySign(form);
            // 校验请求回放
            this.verifyReplay(form);

            SjmsUser sjmsUser = sjmsUserApi.getByEmpId(form.getUserCode());
            if (sjmsUser == null) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(form.getUserCode());
                if (esbEmployeeDTO == null) {
                    throw new BusinessException(SsoErrorCode.ESB_EMPLOYEE_NOT_EXIST);
                }

                CreateSjmsUserRequest request = new CreateSjmsUserRequest();
                request.setEmpId(esbEmployeeDTO.getEmpId());
                request.setName(esbEmployeeDTO.getEmpName());
                request.setGender(0);
                request.setBirthday(esbEmployeeDTO.getBirthday());
                request.setMobile(esbEmployeeDTO.getMobilePhone());
                request.setEmail(esbEmployeeDTO.getEmail());
                request.setIdNumber("");
                request.setIgnoreExists(true);
                request.setOpUserId(0L);
                // 创建用户信息
                Long userId = sjmsUserApi.create(request);

                // 重新获取用户信息
                sjmsUser = sjmsUserApi.getById(userId);
            }

            JwtDataModel jwtDataModel = new JwtDataModel(AppEnum.SJMS.getAppId(), sjmsUser.getId(), sjmsUser.getEmpId());
            String token = jwtTokenUtils.generateToken(jwtDataModel);

            if (form.getType() == 1) {
                ClientConfig.Token.Cookie tokenCookie = clientConfig.getToken().getCookie();
                // 新建token的cookie
                Cookie cookie = new Cookie(tokenCookie.getName(), token);
                cookie.setPath(tokenCookie.getPath());
                cookie.setDomain(tokenCookie.getDomain());
                cookie.setMaxAge(tokenCookie.getMaxAge());
                response.addCookie(cookie);
                // 用于前端同学本地调试
                if (Constants.DEBUG_ENV_LIST.contains(env)) {
                    Cookie cookie2 = new Cookie("dev_YL_SMART_LOGIN_TOKEN", token);
                    cookie2.setPath(tokenCookie.getPath());
                    cookie2.setDomain(tokenCookie.getDomain());
                    cookie2.setMaxAge(tokenCookie.getMaxAge());
                    response.addCookie(cookie2);
                }

                String redirectUrl = form.getRedirectUrl();
                if (StrUtil.isEmpty(redirectUrl)) {
                    // 重定向到系统主页
                    response.sendRedirect(clientConfig.getUrl());
                } else {
                    // 转过来的地址：https://sjms-test.59yi.com/#/transfer?type=7&amp;formId=3246&amp;forwardHistoryId=0
                    response.sendRedirect(HtmlUtil.unescape(redirectUrl));
                }
            } else {
                this.gotoAppTokenPage(token, HtmlUtil.unescape(form.getRedirectUrl()));
            }
        } catch (BusinessException be) {
            this.gotoErrorPage(form.getType(), be.getResultCode());
        } catch (Exception e) {
            log.error("SSO回调接口逻辑执行出错", e);
            this.gotoErrorPage(form.getType(), SsoErrorCode.EXCEPTION);
        }
    }

    private boolean verifySign(SsoCallbackForm form) {
        if (ssoConfig.getVerifySign()) {
            String sign = SsoSignUtils.sign(ssoConfig.getSignSecret(), form.getUserCode() + form.getTimestamp()).substring(4, 20);
            if (!sign.equals(form.getMd5())) {
                throw new BusinessException(SsoErrorCode.SIGN_ERROR);
            }
        }

        return true;
    }

    private boolean verifyReplay(SsoCallbackForm form) {
        if (ssoConfig.getVerifyReplay()) {
            Date date = new Date(form.getTimestamp());
            Date expirationTime = DateUtil.offset(date, DateField.SECOND, ssoConfig.getReplayTime());
            if(!expirationTime.after(new Date())) {
                throw new BusinessException(SsoErrorCode.REQUEST_EXPIRED);
            }
        }

        return true;
    }

    private void gotoErrorPage(Integer type, IErrorCode errorCode) throws IOException {
        response.sendRedirect(type == 1 ? ssoConfig.getPcErrorPage() : ssoConfig.getAppErrorPage() + "?code=" + errorCode.getCode() + "&message=" + URLEncoder.createDefault().encode(errorCode.getMessage(), Charset.defaultCharset()));
    }

    private void gotoAppTokenPage(String token, String redirectUrl) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(ssoConfig.getAppTokenPage()).append("?token=").append(URLEncoder.createAll().encode(token, Charset.defaultCharset()));
        if (StrUtil.isNotEmpty(redirectUrl)) {
            sb.append("&redirectUrl=").append(URLEncoder.createAll().encode(redirectUrl, Charset.defaultCharset()));
        }
        response.sendRedirect(sb.toString());
    }
}
