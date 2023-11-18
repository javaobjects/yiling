package com.yiling.open.third.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.open.backup.util.BackupUtil;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/6/25
 */
@Slf4j
@Service("wuHanKangShiYouMeiService")
public class WuHanKangShiYouMeiServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final long TOKEN_EXPIRE = 1;
    private static final String FLOW_TOKEN = "flow_third_token:wuHanKangShiYouMei";

    private static final String URL = "url";
    private static final String TOKEN_METHOD = "tokenMethod";
    private static final String PURCHASE_METHOD = "purchaseMethod";
    private static final String SALE_METHOD = "saleMethod";
    private static final String GOODS_BATCH_METHOD = "goodsBatchMethod";

    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_NAME = "username";
    private static final String PASS_WORD = "password";
    private static final String SCOPE = "scope";
    private static final String GRANT_TYPE = "grant_type";


    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        // url
        String url = param.get(URL);
        String tokenMethod = param.get(TOKEN_METHOD);
        String purchaseMethod = param.get(PURCHASE_METHOD);
        // 账户
        String username = param.get(USER_NAME);
        String password = param.get(PASS_WORD);
        // 授权码
        String authorization = param.get(AUTHORIZATION);
        String scope = param.get(SCOPE);
        String grantType = param.get(GRANT_TYPE);

        // 查询时间，结束时间减1天 即昨天
        DateTime startDateTime = DateUtil.parse(param.get(FlowAbstractTemplate.startTime));
        DateTime endDateTime = DateUtil.offsetDay(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), -1);
        String startDate = DateUtil.format(startDateTime, "yyyy-MM-dd");
        String endDate = DateUtil.format(endDateTime, "yyyy-MM-dd");

        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray;
        int current = 1;
        int size = 500;
        do {
            // 获取token
            String token = this.getToken(url, tokenMethod, username, password, authorization, scope, grantType);
            // 查询
            jsonArray = doRequestFlowData(token, url, purchaseMethod, startDate, endDate, size, current, PURCHASE_METHOD);
            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                break;
            }
            jsonArrayResult.addAll(jsonArray);

            if (jsonArray.size() < size) {
                break;
            }
            current++;
        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

        if (ObjectUtil.isNotNull(jsonArrayResult) && jsonArrayResult.size() > 0) {
            return JSONUtil.toJsonStr(jsonArrayResult);
        }
        return null;
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        // url
        String url = param.get(URL);
        String tokenMethod = param.get(TOKEN_METHOD);
        String saleMethod = param.get(SALE_METHOD);
        // 账户
        String username = param.get(USER_NAME);
        String password = param.get(PASS_WORD);
        // 授权码
        String authorization = param.get(AUTHORIZATION);
        String scope = param.get(SCOPE);
        String grantType = param.get(GRANT_TYPE);

        // 查询时间
        // 查询时间，结束时间减1天 即昨天
        DateTime startDateTime = DateUtil.parse(param.get(FlowAbstractTemplate.startTime));
        DateTime endDateTime = DateUtil.offsetDay(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), -1);
        String startDate = DateUtil.format(startDateTime, "yyyy-MM-dd");
        String endDate = DateUtil.format(endDateTime, "yyyy-MM-dd");

        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray;
        int current = 1;
        int size = 500;
        do {
            // 获取token
            String token = this.getToken(url, tokenMethod, username, password, authorization, scope, grantType);
            // 查询
            jsonArray = doRequestFlowData(token, url, saleMethod, startDate, endDate, size, current, SALE_METHOD);
            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                break;
            }
            jsonArrayResult.addAll(jsonArray);

            if (jsonArray.size() < size) {
                break;
            }
            current++;
        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

        if (ObjectUtil.isNotNull(jsonArrayResult) && jsonArrayResult.size() > 0) {
            return JSONUtil.toJsonStr(jsonArrayResult);
        }
        return null;
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        // url
        String url = param.get(URL);
        String tokenMethod = param.get(TOKEN_METHOD);
        String goodsBatchMethod = param.get(GOODS_BATCH_METHOD);
        // 账户
        String username = param.get(USER_NAME);
        String password = param.get(PASS_WORD);
        // 授权码
        String authorization = param.get(AUTHORIZATION);
        String scope = param.get(SCOPE);
        String grantType = param.get(GRANT_TYPE);

        // 查询时间，取昨天
        String startDate = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd");

        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray;
        int current = 1;
        int size = 500;
        do {
            // 获取token
            String token = this.getToken(url, tokenMethod, username, password, authorization, scope, grantType);
            // 查询
            jsonArray = doRequestFlowData(token, url, goodsBatchMethod, startDate, null, size, current, GOODS_BATCH_METHOD);
            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                break;
            }
            jsonArrayResult.addAll(jsonArray);

            if (jsonArray.size() < size) {
                break;
            }
            current++;
        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

        if (ObjectUtil.isNotNull(jsonArrayResult) && jsonArrayResult.size() > 0) {
            return JSONUtil.toJsonStr(jsonArrayResult);
        }
        return null;
    }


    /**
     * 查询
     *
     * @param token token
     * @param url 接口地址
     * @param method 接口名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    private JSONArray doRequestFlowData(String token, String url, String method, String startDate, String endDate, int pageSize, int page, String methodType) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("pageSize", pageSize);
        bodyParams.put("page", page);
        if (ObjectUtil.equal(PURCHASE_METHOD, methodType)) {
            bodyParams.put("buyDateStart", startDate);
            bodyParams.put("buyDateEnd", endDate);
        } else if (ObjectUtil.equal(SALE_METHOD, methodType)) {
            bodyParams.put("sellDateStart", startDate);
            bodyParams.put("sellDateEnd", endDate);
        } else if (ObjectUtil.equal(GOODS_BATCH_METHOD, methodType)) {
            bodyParams.put("stockDate", startDate);
        }

        String requestBody = JSONUtil.toJsonStr(bodyParams);
        String returnJson = HttpRequest.post(url.concat(method)).header(Header.CONTENT_TYPE, "application/json").header(AUTHORIZATION, token).body(requestBody).timeout(1000 * 60 * 5).execute().body();
        if (StrUtil.isNotEmpty(returnJson)) {
            JSONObject jsonObject = JSON.parseObject(returnJson);
            if (jsonObject != null) {
                Object code = jsonObject.get("code");
                if (ObjectUtil.isNull(code) || !ObjectUtil.equal("000000", code.toString())) {
                    log.warn(">>>>> 武汉康世佑美, 查询流向接口失败, response:{}", returnJson);
                    return null;
                }

                JSONObject result = jsonObject.getJSONObject("result");
                if (result != null) {
                    JSONArray jsonArray = result.getJSONArray("records");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        // 去除id字段
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            Object id = jsonObj.get("id");
                            if (ObjectUtil.isNotNull(id) && !BackupUtil.isNumber(id.toString())) {
                                jsonObj.remove("id");
                            }
                        }
                        return jsonArray;
                    }
                }

            }
        }
        return null;
    }


    /**
     * 获取token
     *
     * @param username 用户账号
     * @param password 用户账号
     * @param authorization 授权码
     * @param tokenMethod 接口地址
     * @return
     */
    private String getToken(String url, String tokenMethod, String username, String password, String authorization, String scope, String grantType) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(authorization) || StrUtil.isBlank(tokenMethod)) {
            log.warn(">>>>> 武汉康世佑美, 获取token的账号、密码、授权码、地址，都不能为空");
            return null;
        }
        String key = RedisKey.generate(FLOW_TOKEN, username);
        String token = null;
//        String token = stringRedisTemplate.opsForValue().get(key);
//        if (StrUtil.isNotBlank(token)) {
//            return token;
//        }

        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME, username);
        params.put(PASS_WORD, password);
        params.put(SCOPE, scope);
        params.put(GRANT_TYPE, grantType);

        String urlParams = HttpUtil.toParams(params);
        String requestUrl = url.concat(tokenMethod).concat("?").concat(urlParams);
        String returnJson = HttpRequest.post(requestUrl).header(Header.CONTENT_TYPE, "application/json").header("authorization", authorization).timeout(1000 * 60 * 5).execute().body();
        if (StrUtil.isBlank(returnJson)) {
            log.error(">>>>> 武汉康世佑美, 获取token的接口返回空");
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(returnJson);
        if (ObjectUtil.isNotNull(jsonObject)) {
            String accessToken = jsonObject.getString("access_token");
            String tokenType = jsonObject.getString("token_type");
            if (StrUtil.isBlank(accessToken)) {
                log.error(">>>>> 武汉康世佑美, 获取token, access_token为空");
                return null;
            }
            String tokenHeader = "";
            if (StrUtil.isNotBlank(tokenType)) {
                tokenHeader = convertUpperCaseFirst(tokenType.trim()).concat(" ");
            }
            token = tokenHeader.concat(accessToken);
            log.info(">>>>> 武汉康世佑美, token:{}", token);
            stringRedisTemplate.opsForValue().set(key, token, TOKEN_EXPIRE, TimeUnit.HOURS);
            return token;
        }
        return null;
    }

    /**
     * 首字母转换成大写，其它的全部转换成小写
     *
     * @param str
     * @return
     */
    public static String convertUpperCaseFirst(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        String first = str.substring(0, 1);
        String after = str.substring(1);
        first = first.toUpperCase();
        after = after.toLowerCase();
        return first.concat(after);
    }


    //    public static void main(String[] args) {
    //        Map<String,String> params = new HashMap<>();
    //        params.put("username", "ylye");
    //        params.put("password", "ZxydJ.6$");
    //        params.put("scope", "read");
    //        params.put("grant_type", "password");
    //
    //        String urlParams = HttpUtil.toParams(params);
    //        System.out.println("----- urlParams:" + urlParams);
    //
    //    }


}
