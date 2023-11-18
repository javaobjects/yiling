package com.yiling.basic.wx.api.impl;

import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.wx.api.WxAccountApi;
import com.yiling.basic.wx.service.WxAccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信公众号配置信息
 * @author zhigang.guo
 * @date: 2022/3/1
 */
@DubboService
@Slf4j
public class WxAccountApiImpl implements WxAccountApi {

    @Autowired
    private WxAccountService wxAccountService;

    /**
     * 获取AccessToken地址信息
     *
     * @return
     */
    @Override
    public String getAccessToken() {

        return wxAccountService.getAccessToken();
    }

    @Override
    public String getTicket(String type) {

        return wxAccountService.getTicket(type);
    }


    @Override
    public Map<String, String> wxSign(Map<String, String> params,String type) {

        return wxAccountService.wxSign(params,type);
    }
}
