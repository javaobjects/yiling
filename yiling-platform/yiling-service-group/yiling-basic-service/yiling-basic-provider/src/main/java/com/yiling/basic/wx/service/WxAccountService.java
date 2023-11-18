package com.yiling.basic.wx.service;

import java.util.Map;

/**微信公众号配置信息
 * @author zhigang.guo
 * @date: 2022/3/2
 */
public interface WxAccountService {

    /**
     *  获取微信AccessToken
     * @return
     */
    public String getAccessToken() ;

    /**
     * 获取微信票据 获取ticket类型:默认为"jsapi"
     * @return
     */
    public String getTicket(String type);


    /**
     * @param params 签名参数
     * @param type 获取票证类型：如jsapi_ticket,wx_card
     * 获取微信公众号签名
     * @throws Exception
     */
    public Map<String, String> wxSign(Map<String, String> params,String type);
}
