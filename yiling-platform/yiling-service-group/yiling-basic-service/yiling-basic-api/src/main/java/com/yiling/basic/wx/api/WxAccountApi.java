package com.yiling.basic.wx.api;

import java.util.Map;

/**
 * 微信公众号配置信息
 * @author zhigang.guo
 * @date: 2022/3/1
 */
public interface WxAccountApi {
    /**
     *  获取微信AccessToken
     * @return
     */
    public String getAccessToken() ;

    /**
     * 获取ticket类型:默认为"jsapi"
     * 获取微信票据
     * @return
     */
    public String getTicket(String type);


    /**
     * 获取微信公众号签名
     * @throws Exception
     */
    public Map<String, String> wxSign(Map<String, String> params,String type);
}
