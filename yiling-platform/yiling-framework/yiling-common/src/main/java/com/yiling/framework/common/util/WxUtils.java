package com.yiling.framework.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.framework.common.pojo.vo.GzhUserInfo;
import com.yiling.framework.common.pojo.vo.WxAccessToken;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Slf4j
@Data
public class WxUtils {

    private RedisService redisService;

    public WxUtils(RedisService redisService){
        this.redisService = redisService;
    }
//
//
//    /**
//     * 获取公众号token
//     *
//     * @param appId
//     * @param secret
//     * @return
//     */
//    public static String getAccessToken(String appId, String secret) {
//        // todo
//        log.info("获取accessToken，appId:{}, secret: {}", appId, secret);
//        String url = String.format(WxConstant.URL_ACCESS_TOKEN_GET, appId, secret);
//        String result = HttpUtil.get(url);
//        log.info("获取accessToken结果：{}", result);
//        if (StrUtil.isNotBlank(result)) {
//            WxAccessToken wxAccessToken = JSONUtil.toBean(result, WxAccessToken.class);
//            log.info("获取accessToken结果: {}", wxAccessToken);
//            return wxAccessToken.getAccessToken();
//        }
//        log.info("获取accessToken失败");
//        return null;
//    }

//    /**
//     * 获取公众号token
//     *
//     * @param appId
//     * @param secret
//     * @return
//     */
//    public String getAccessToken(String appId, String secret) {
//        log.info("获取accessToken，appId:{}, secret: {}", appId, secret);
//
//        String accessToken = "";
//        String accessTokenKey = RedisKey.generate("wechat", "accessTokenKey", appId);
//        if (Objects.nonNull(redisService.get(accessTokenKey))) {
//            accessToken = redisService.get(accessTokenKey).toString();
//        } else {
//            String url = String.format(WxConstant.URL_ACCESS_TOKEN_GET, appId, secret);
//            String result = HttpUtil.get(url);
//            log.info("调用API获取accessToken结果：{}", result);
//            if (StrUtil.isNotBlank(result)) {
//                WxAccessToken wxAccessToken = JSONUtil.toBean(result, WxAccessToken.class);
//                accessToken = wxAccessToken.getAccessToken();
//                redisService.set(accessTokenKey, accessToken, 60 * 60);
//            }
//        }
//
//        log.info("获取accessToken结果: {}", accessToken);
//        return accessToken;
//    }

}
