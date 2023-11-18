package com.yiling.basic.wx.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.wx.service.WxAccountService;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信公众号配置信息
 * @author zhigang.guo
 * @date: 2022/3/1
 */
@Component
@Slf4j
@RefreshScope
public class WxAccountServiceImpl implements WxAccountService {

    @Value("${basic.wx.appId}")
    private String appId;
    @Value("${basic.wx.appSecret}")
    private String appSecret;

    @Autowired
    private RedisService redisService;
    /**
     * accessToken地址
     */
    private final String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=";
    /**
     * 票证地址信息
     */
    private final String ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";

    /**
     * 获取AccessToken地址信息
     *
     * @return
     */
    @Override
    public String getAccessToken() {
        String accessTokenKey = RedisKey.generate("basic", "wx", "accessToken");
        Object accessToken = redisService.get(accessTokenKey);
        if (ObjectUtil.isNotNull(accessToken)) {
            return accessToken.toString();
        }
        // 发送请求
        HttpRequest request = HttpUtil.createGet(token_url + appId + "&secret=" + appSecret);
        HttpResponse response = request.execute();
        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(response.body());

        if (log.isDebugEnabled()) {

            log.debug("getAccessToken..result:{}", JSON.toJSONString(json));
        }

        String access_token = json.getString("access_token");

        if (StringUtils.isBlank(access_token)) {

            log.info("getAccessToken..errorMesg" + json.get("errmsg"));
            return null;
        }

        Long time = json.getLong("expires_in");

        redisService.set(accessTokenKey, access_token, time);

        return access_token;
    }


    @Override
    public String getTicket(String type) {
        String ticketKey = RedisKey.generate("basic", "wx", "ticket");
        Object ticket = redisService.get(ticketKey);
        if (ObjectUtil.isNotNull(ticket)) {
            log.info("getTicket..result:{}",JSON.toJSONString(ticket));
            return ticket.toString();
        }
        String accessToken = this.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        HttpRequest request = HttpUtil.createGet(ticket_url + accessToken + "&type=" + type);
        HttpResponse response = request.execute();
        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(response.body());

        if (log.isDebugEnabled()) {
            log.debug("getTicket..result:{}",JSON.toJSONString(json));
        }

        String ticketResult = json.getString("ticket");
        Long time = json.getLong("expires_in");

        if (StringUtils.isBlank(ticketResult)) {
            log.info("getTicket..errorMesg" + json.get("errmsg"));
            return null;
        }
        redisService.set(ticketKey, ticketResult, time);

        return ticketResult.toString();
    }


    @Override
    public Map<String, String> wxSign(Map<String, String> params,String type) {

        log.info("wxsign..params:{},type:{}",JSON.toJSONString(params),type);

        String method = "[sign]";
        // 创建返回对象
        Map<String, String> returnMap = new HashMap<>();
        // 条件判断
        if (MapUtil.isEmpty(params)) {
            log.warn(method + "签名的map原数据为空!");
            return MapUtil.empty();
        }
        String jsapi_ticket = this.getTicket(type);
        if (StringUtils.isBlank(jsapi_ticket)) {
            return params;
        }
        // 如果缓存中不存在, 则重新签名
        String timestamp = this.create_timestamp();// 时间戳
        String nonce = UUID.randomUUID().toString().replace("-", ""); // 随机数
        params.put("noncestr", nonce);
        params.put("timestamp", timestamp);
        params.put("jsapi_ticket", jsapi_ticket);
        // 返回参数处理
        returnMap = params;// 将原参数原本返回
        String signStr = this.preSignStr(params);
        returnMap.put("signature", this.byteToHex(signStr));// 添加签名结果至返回对象
        returnMap.put("appId",appId);
        return returnMap;
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String preSignStr(Map<String, String> params) {
        if (MapUtil.isEmpty(params)) {
            return null;
        }
        // 排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        // 字符串拼接
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


    /**
     * sha1进行签名
     * @param str
     * @return
     */
    private String byteToHex(String str) {
        String result = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : crypt.digest()) {
                formatter.format("%02x", b);
            }
            result = formatter.toString();
            formatter.close();
        } catch (NoSuchAlgorithmException e) {
            log.error("byteToHex: error",e);
        } catch (UnsupportedEncodingException e) {
            log.error("byteToHex: error",e);
        }
        return result;
    }
}
