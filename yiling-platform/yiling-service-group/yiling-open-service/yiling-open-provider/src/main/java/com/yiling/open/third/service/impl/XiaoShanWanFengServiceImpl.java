package com.yiling.open.third.service.impl;

import java.util.HashMap;
import java.util.Map;

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

@Slf4j
@Service("xiaoShanWanFengService")
public class XiaoShanWanFengServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String URL = "url";

    private static final String YZID = "yzid";

    private static final String PWD = "pwd";


    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        JSONArray returnJsonArray = new JSONArray();
        handle(param, returnJsonArray, "QueryStockEnterInfo");

        return JSONUtil.toJsonStr(returnJsonArray);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        JSONArray returnJsonArray = new JSONArray();
        handle(param, returnJsonArray, "QuerySellInfo");

        return JSONUtil.toJsonStr(returnJsonArray);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        JSONArray returnJsonArray = new JSONArray();
        handle(param, returnJsonArray, "GoodsStore");

        return JSONUtil.toJsonStr(returnJsonArray);
    }


    private void handle(Map<String, String> param, JSONArray returnJsonArray, String interType) {
        Map<String, Object> params = new HashMap<>();

        String url = param.get(URL);

        params.put("yzid", param.get(YZID));
        params.put("pwd", param.get(PWD));
        params.put("intertype", interType);

        // 该接口需要分页查询，一次50条
        int currentPage = 1;    // 当前页
        int totalpage = 0;      // 总页数
        int lastDataSize = 0;   // 上一批数据个数
        do {
            // 生成参数
            putInputParams(params, param, currentPage);
            // 发起请求
            String responseStr = doRequest(url, params);
            if (responseStr == null) {
                break;
            }

            //  解析response中的dataItem
            String dataItemStr = analysisResponseStrToDataItemStr(responseStr);
            if (StringUtils.isEmpty(dataItemStr)) {
                break;
            }

            if (JSONUtil.isJsonArray(dataItemStr)) {
                JSONArray dataArray = JSONUtil.parseArray(dataItemStr);
                // 当前页
                lastDataSize = dataArray.size();
                // 获取总页数
                JSONObject data = JSONUtil.parseObj(dataArray.get(0));
                totalpage = data.getInt("totalpage");
                returnJsonArray.addAll(dataArray);
            } else if (JSONUtil.isJsonObj(dataItemStr)) {
                lastDataSize = 1;
                returnJsonArray.add(JSONUtil.parseObj(dataItemStr));
            }
            currentPage++;
        } while (lastDataSize == 50 && currentPage <= totalpage);
    }


    private void putInputParams(Map<String, Object> reqMap, Map<String, String> param, int currentPage) {
        Map<String, Object> inputParamMap = new HashMap<>();
        inputParamMap.put("startdate", param.get(startTime));
        inputParamMap.put("enddate", param.get(endTime));
        inputParamMap.put("currentpage", currentPage);

        Map<String, Object> inputStrMap = new HashMap<>();
        inputStrMap.put("inputparas", inputParamMap);

        String xmlStr = JSONUtil.toXmlStr(JSONUtil.parse(inputStrMap));
        reqMap.put("inputstr", xmlStr);
    }

    private String doRequest(String url, Map<String, Object> params) {
        String responseStr = null;
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "http://tempuri.org/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("DownLoadData").setParams(params, false).setConnectionTimeout(1000*60*5);
        responseStr = client.send();
        return responseStr;
    }

    private String analysisResponseStrToDataItemStr(String responseStr) {
        String resultStr = getResponseJson(JSONUtil.xmlToJson(responseStr));
        if (StringUtils.isEmpty(responseStr)) {
            return null;
        }

        JSONObject jsonObject = JSONUtil.xmlToJson(resultStr);
        if (jsonObject.getJSONObject("output") == null) {
            return null;
        }

        JSONObject outputJson = jsonObject.getJSONObject("output");
        Integer retCode = outputJson.getInt("retcode");
        if (retCode == null || !retCode.equals(999)) {
            return null;
        }

        JSONObject dataJson = outputJson.getJSONObject("data");
        if (dataJson == null) {
            return null;
        }

        String dataItemStr = dataJson.getStr("dataitem");
        if (StringUtils.isEmpty(dataItemStr)) {
            return null;
        }
        return dataItemStr;
    }

    private static String getResponseJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        for (String key : jsonObject.keySet()) {
            if ("DownLoadDataResult".equals(key)) {
                return jsonObject.getStr(key);
            }

            String str = jsonObject.getStr(key);
            if (!JSONUtil.isJson(str)) {
                continue;
            }
            JSONObject json = jsonObject.getJSONObject(key);
            if (JSONUtil.isNull(json)) {
                continue;
            }
            String resultStr = getResponseJson(jsonObject.getJSONObject(key));
            if (JSONUtil.isNull(resultStr)) {
                continue;
            }
            return resultStr;
        }
        return null;
    }
}
