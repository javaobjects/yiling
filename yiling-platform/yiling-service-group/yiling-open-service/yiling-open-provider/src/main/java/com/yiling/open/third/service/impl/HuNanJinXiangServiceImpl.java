package com.yiling.open.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fucheng.bai
 * @date: 2022/6/20
 */
@Slf4j
@Service("huNanJinXiangService")
public class HuNanJinXiangServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String OWNER = "owner";

    private static final String SXRQ = "sxrq";

    private static final String TOKEN_URL = "tokenUrl";

    private static final String ACCESS_TOKEN = "access_token";

    private static final String PURCHASE_URL = "purchaseUrl";
    private static final String SALE_URL = "saleUrl";
    private static final String GOODS_BATCH_URL = "goodsBatchUrl";


    private static final String ERR_CODE = "errcode";

    private static final String ERR_MSG = "errmsg";

    private static final String RECORD_LIST = "recordList";



    /**
     * 采购流向
     * @param param
     * @return
     */
    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        // 根据用户名密码获取access_token
        String accessToken = getAccessToken(param);

        if (StrUtil.isEmpty(accessToken)) {
            return JSONUtil.toJsonStr(new JSONArray());
        }
        String url = param.get(PURCHASE_URL) + "?" + ACCESS_TOKEN + "=" + accessToken;

        Map<String, String> requestMap = new HashMap<>();

        requestMap.put("owner", param.get(OWNER));
        requestMap.put("startDate", dateTimeFormatCovertToDate(param.get(FlowAbstractTemplate.startTime)));
        requestMap.put("endDate", dateTimeFormatCovertToDate(param.get(FlowAbstractTemplate.endTime)));
        String requestBody = JSONUtil.toJsonStr(requestMap);

        JSONArray returnJsonArray = new JSONArray();
        doRequest(returnJsonArray, url, requestBody, param.get(OWNER));

        return JSONUtil.toJsonStr(returnJsonArray);
    }

    /**
     * 销售流向
     * @param param
     * @return
     */
    @Override
    protected String requestSaleTab(Map<String, String> param) {
        // 根据用户名密码获取access_token
        String accessToken = getAccessToken(param);

        if (StrUtil.isEmpty(accessToken)) {
            return JSONUtil.toJsonStr(new JSONArray());
        }
        String url = param.get(SALE_URL) + "?" + ACCESS_TOKEN + "=" + accessToken;

        Map<String, String> requestMap = new HashMap<>();

        requestMap.put("owner", param.get(OWNER));
        requestMap.put("startDate", dateTimeFormatCovertToDate(param.get(FlowAbstractTemplate.startTime)));
        requestMap.put("endDate", dateTimeFormatCovertToDate(param.get(FlowAbstractTemplate.endTime)));
        String requestBody = JSONUtil.toJsonStr(requestMap);

        JSONArray returnJsonArray = new JSONArray();
        doRequest(returnJsonArray, url, requestBody, param.get(OWNER));

        return JSONUtil.toJsonStr(returnJsonArray);
    }

    /**
     * 库存流向
     * @param param
     * @return
     */
    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {

        // 根据用户名密码获取access_token
        String accessToken = getAccessToken(param);

        if (StrUtil.isEmpty(accessToken)) {
            return JSONUtil.toJsonStr(new JSONArray());
        }
        String url = param.get(GOODS_BATCH_URL) + "?" + ACCESS_TOKEN + "=" + accessToken;

        Map<String, String> requestMap = new HashMap<>();

        requestMap.put("owner", param.get(OWNER));
        requestMap.put("startDate", dateTimeFormatCovertToDate(param.get(FlowAbstractTemplate.startTime)));
        requestMap.put("endDate", dateTimeFormatCovertToDate(param.get(FlowAbstractTemplate.endTime)));
        String requestBody = JSONUtil.toJsonStr(requestMap);

        JSONArray returnJsonArray = new JSONArray();
        doRequest(returnJsonArray, url, requestBody, param.get(OWNER));

        return JSONUtil.toJsonStr(returnJsonArray);
    }


    // 获取AccessToken
    private String getAccessToken(Map<String, String> param) {
        String accessToken = "";
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String tokenUrl = param.get(TOKEN_URL);
        tokenUrl = tokenUrl + "?username=" + userName + "&password=" + passWord;

        try {
            String returnJson = HttpRequest.get(tokenUrl).execute().body();
            JSONObject jsonObject = JSON.parseObject(returnJson);
            if (jsonObject.getString(ACCESS_TOKEN) == null) {
                log.warn(">>>>> 湖南津湘 获取access_token失败, response:{}", returnJson);
            }
            accessToken = jsonObject.getString(ACCESS_TOKEN);
            return accessToken;
        } catch (Exception e) {
            log.error("Exception:", e);
        }
        return accessToken;
    }


    private void doRequest(JSONArray returnJsonArray, String url, String requestBody, String owner) {
        String returnJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").body(requestBody).execute().body();
        if (StrUtil.isNotEmpty(returnJson)) {
            JSONObject jsonObject = JSON.parseObject(returnJson);
            if (jsonObject != null) {
                Integer errcode = jsonObject.getInteger(ERR_CODE);
                if (errcode != 0) {
                    log.warn(">>>>> 湖南津湘, response:{}", returnJson);
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONArray(RECORD_LIST);

                if (jsonArray == null) {
                    return;
                }
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    json.put(OWNER, owner);
                    // 处理有效期
                    String sxrq = json.getString(SXRQ);
                    if (StringUtils.isNotEmpty(sxrq)) {
                        sxrq = sxrq.trim();
                        if (sxrq.split("-").length == 2) {
                            sxrq = sxrq + "-01";
                        } else if (sxrq.split("-").length == 3) {
                        } else {
                            sxrq = "1970-01-01";
                        }
                        json.put(SXRQ, sxrq);
                    }
                }
                if (jsonArray.size() > 0) {
                    returnJsonArray.addAll(jsonArray);
                }
            }
        }
    }
}
