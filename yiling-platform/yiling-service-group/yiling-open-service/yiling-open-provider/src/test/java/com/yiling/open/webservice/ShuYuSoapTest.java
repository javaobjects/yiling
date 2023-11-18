package com.yiling.open.webservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.util.StringUtils;

import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class ShuYuSoapTest {

    public static void main(String[] args) throws Exception {
        Map<String, Object> params = new LinkedMap<>();
        Map<String, String> iHeadMap = new HashMap<>();

        iHeadMap.put("ZZUSERID", "1000000016");
        iHeadMap.put("ZZUSERPAS", "YLYY0016");
        iHeadMap.put("LIFNR", "1000000016");
        iHeadMap.put("ZZCXFW", "D");
        iHeadMap.put("DATE_FROM", "2023-05-01");
        iHeadMap.put("DATE_TO", "2023-05-05");

        params.put("I_HEAD", iHeadMap);
        params.put("ET_DIST", null);
        params.put("ET_INV", null);
        params.put("ET_PUR", null);
        params.put("IT_BUKRS", null);
        params.put("IT_EKORG", null);

        //        String url = "http://60.217.250.254:28000/sap/bc/srt/rfc/sap/zscm_ws/300/zscm_ws/zscm_ws";
        //        String username = "ZRFC_ZL";
        //        String password = "a123456";

        String url = "http://60.217.250.254:18000/sap/bc/srt/rfc/sap/zscm_ws/800/zscm_ws/zscm_ws";
        String username = "ZGTS_RFC";
        String password = "ZGTS_RFC_PS";

        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "urn:sap-com:document:sap:rfc:functions")
                .header("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((username + ":" + password).getBytes()))
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("urn:ZSCM_DATA_OUT_01").setParams(params, false).setConnectionTimeout(1000*60*5);

        String msg =  client.getMsgStr(true);
//        System.out.println("request = " + msg);
        String str = client.send();

//        System.out.println("response = " + str);
        JSONObject jsonObject = JSONUtil.xmlToJson(str);

        JSONArray returnJsonArray = new JSONArray();
//        JSONObject json = getDataJson(jsonObject, "ET_PUR");
        JSONObject json = getDataJson(jsonObject, "ET_INV");
//        JSONObject json = getDataJson(jsonObject, "ET_DIST");
        handleResult(returnJsonArray, json);

        String fileName = "/Users/baifc/Downloads/库存.txt";
        File file = new File(fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        System.out.println(returnJsonArray);

        String result = JSONUtil.toJsonPrettyStr(returnJsonArray);
        bw.write(result);
        bw.close();

    }

    private static JSONObject getDataJson(JSONObject jsonObject, String flowDataKey) {
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

    private static void handleResult(JSONArray returnJsonArray, JSONObject json) {
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
