package com.yiling.open.third.service.impl;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fucheng.bai
 * @date: 2022/6/24
 */
@Slf4j
@Service("shuYuPharmacyService")
public class ShuYuPharmacyServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    // 接口认证用户名
    private static final String AUTH_NAME = "authName";

    // 接口认证密码
    private static final String AUTH_PWD = "authPwd";

    private static final String URL = "url";

    // 用户名
    private static final String ZZ_USER_ID = "userId";

    // 密码
    private static final String ZZ_PASSWORD = "password";

    // 厂商
    private static final String LIFNR = "manufacturer";

    // 查询范围
    private static final String ZZ_CXFW = "queryScope";



    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        JSONArray returnJsonArray = new JSONArray();

        String resultStr = doRequest(param);
        if (resultStr == null) {
            return JSONUtil.toJsonStr(returnJsonArray);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject =  JSONUtil.xmlToJson(resultStr);
        } catch (Exception e) {
            log.warn(">>>>> 漱玉平民大药房，采购数据同步失败，返回格式转换错误！");
            log.warn("Exception: ", e);
        }
        if (JSONUtil.isNull(jsonObject)) {
            return JSONUtil.toJsonStr(returnJsonArray);
        }

        JSONObject json = getDataJson(jsonObject, "ET_PUR");
        handleResult(returnJsonArray, json);

        return  JSONUtil.toJsonStr(returnJsonArray);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        JSONArray returnJsonArray = new JSONArray();

        String resultStr = doRequest(param);
        if (resultStr == null) {
            return JSONUtil.toJsonStr(returnJsonArray);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject =  JSONUtil.xmlToJson(resultStr);
        } catch (Exception e) {
            log.warn(">>>>> 漱玉平民大药房，销售数据同步失败，返回格式转换错误！");
            log.warn("Exception: ", e);
        }
        if (JSONUtil.isNull(jsonObject)) {
            return JSONUtil.toJsonStr(returnJsonArray);
        }

        JSONObject json = getDataJson(jsonObject, "ET_DIST");
        handleResult(returnJsonArray, json);

        return  JSONUtil.toJsonStr(returnJsonArray);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        JSONArray returnJsonArray = new JSONArray();

        String resultStr = doRequest(param);
        if (resultStr == null) {
            return JSONUtil.toJsonStr(returnJsonArray);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject =  JSONUtil.xmlToJson(resultStr);
        } catch (Exception e) {
            log.warn(">>>>> 漱玉平民大药房，库存数据同步失败，返回格式转换错误！");
            log.warn("Exception: ", e);
        }
        if (JSONUtil.isNull(jsonObject)) {
            return JSONUtil.toJsonStr(returnJsonArray);
        }

        JSONObject json = getDataJson(jsonObject, "ET_INV");
        handleResult(returnJsonArray, json);

        return  JSONUtil.toJsonStr(returnJsonArray);
    }


    private String doRequest(Map<String, String> param) {
        String resultStr = null;

        Map<String, Object> params = new LinkedMap<>();
        Map<String, String> iHeadMap = new HashMap<>();

        iHeadMap.put("ZZUSERID", param.get(ZZ_USER_ID));
        iHeadMap.put("ZZUSERPAS", param.get(ZZ_PASSWORD));
        iHeadMap.put("LIFNR", param.get(LIFNR));
        iHeadMap.put("ZZCXFW", param.get(ZZ_CXFW));
        iHeadMap.put("DATE_FROM", dateTimeFormatCovertToDate(param.get(startTime)));
        iHeadMap.put("DATE_TO", dateTimeFormatCovertToDate(param.get(endTime)));

        // 参数key必须要有，不然接口会报错
        params.put("I_HEAD", iHeadMap);
        params.put("ET_DIST", null);
        params.put("ET_INV", null);
        params.put("ET_PUR", null);
        params.put("IT_BUKRS", null);
        params.put("IT_EKORG", null);

        String url = param.get(URL);
        String authName = param.get(AUTH_NAME);
        String authPwd = param.get(AUTH_PWD);


        try {
            SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "urn:sap-com:document:sap:rfc:functions")
                    // 添加认证头信息
                    .header("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((authName + ":" + authPwd).getBytes()))
                    // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                    .setMethod("urn:ZSCM_DATA_OUT_01").setParams(params, false).setConnectionTimeout(1000*60*5);

            String msg = client.getMsgStr(false);
            log.info(">>>>> 漱玉平民大药房, request: " + msg);

            resultStr = client.send();
            log.info(">>>>> 漱玉平民大药房, response: " + resultStr);
        } catch (Exception e) {
            log.error(">>>>> 漱玉平民大药房， 接口同步失败，请求失败！");
            log.error("Exception: ", e);
        }
        return resultStr;

    }

    private JSONObject getDataJson(JSONObject jsonObject, String flowDataKey) {
        if (jsonObject == null) {
            return null;
        }
        for (String key : jsonObject.keySet()) {
            if (flowDataKey.equals(key)) {
                return jsonObject.getJSONObject(key);
            }

            String str = jsonObject.getStr(key);
            if (!JSONUtil.isJson(str)) {
                continue;
            }
            JSONObject json = jsonObject.getJSONObject(key);
            if (JSONUtil.isNull(json)) {
                continue;
            }
            JSONObject resultJson = getDataJson(jsonObject.getJSONObject(key), flowDataKey);
            if (JSONUtil.isNull(resultJson)) {
                continue;
            }
            return resultJson;
        }
        return null;
    }

    private void handleResult(JSONArray returnJsonArray, JSONObject json) {
        if (json == null) {
            return;
        }

        String jsonStr = json.getStr("item");
        if (StringUtils.isEmpty(jsonStr)) {
            return;
        }

        if (JSONUtil.isJsonArray(jsonStr)) {
            returnJsonArray.addAll(JSONUtil.parseArray(jsonStr));
        } else if (JSONUtil.isJsonObj(jsonStr)) {
            returnJsonArray.add(JSONUtil.parseObj(jsonStr));
        }
    }


}
