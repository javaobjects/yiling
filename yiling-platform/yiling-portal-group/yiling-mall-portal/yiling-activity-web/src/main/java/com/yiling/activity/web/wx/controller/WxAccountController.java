package com.yiling.activity.web.wx.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.yiling.activity.web.wx.form.GetWxAccessTokenForm;
import com.yiling.activity.web.wx.form.WxSignForm;
import com.yiling.basic.wx.api.WxAccountApi;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信公众号基本信息
 *
 * @author zhigang.guo
 * @date: 2022/3/1
 */
@Slf4j
@RestController
@RequestMapping("wx")
@RefreshScope
@Api(tags = "微信公众号基本信息")
public class WxAccountController extends BaseController {

    @DubboReference
    WxAccountApi wxAccountApi;

    @Autowired
    WxMpService wxMpService;

    @Value("${basic.wx.appSource:[{\"app\":\"b2b-app\",\"appKey\":\"d9bb1c61b59b4d3bad86aa15ef490fac\"}]}")
    private String appSource;

    @ApiOperation(value = "获取签名")
    @PostMapping(value = "/sign")
    public Result<Map<String, String>> sign(@RequestBody @Valid WxSignForm form) {

        Map<String, String> map = Maps.newHashMap();
        map.put("url", form.getUrl());

        Map<String, String> resultMap;
        if (StringUtils.isBlank(form.getType())) {
            resultMap = wxAccountApi.wxSign(map, "jsapi");
        } else {
            resultMap = wxAccountApi.wxSign(map, form.getType());
        }

        return Result.success(resultMap);
    }

    @ApiOperation(value = "获取accessToken")
    @PostMapping(value = "/getAccessToken")
    public Result<String> getAccessToken(@RequestBody @Valid GetWxAccessTokenForm form) {

        log.info("获取AccessToken 应用名称:{}", form);

        List<GetWxAccessTokenForm> appSources = JSONArray.parseArray(appSource, GetWxAccessTokenForm.class);

        if (appSources.stream().anyMatch(t -> t.getApp().equals(form.getApp()))) {

            return Result.success(wxAccountApi.getAccessToken());
        }

        return Result.failed("应用不存在!");
    }

    @ApiOperation(value = "获取公众号获取api_ticket")
    @GetMapping(value = "/getApiTicket")
    public Result<WxJsapiSignature> getApiTicket(String url) {
        try {
            WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(url);
            return Result.success(jsapiSignature);
        } catch (WxErrorException e) {
            log.error("获取公众号获取api_ticket异常,错误信息：{}", e.getMessage(), e);
            return Result.failed("获取公众号获取api_ticket异常");
        }
    }


}
