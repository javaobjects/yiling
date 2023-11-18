package com.yiling.open.third.service.impl;

import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/7/27
 */
@Slf4j
@Service("shangYaoLeiYunShangService")
public class ShangYaoLeiYunShangServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String URL = "url";
    private static final String NAME_SPACE_URL = "http://tempuri.org/";
    private static final String PURCHASE_METHOD_NAME = "purchaseMethodName";
    private static final String SALE_METHOD_NAME = "saleMethodName";
    private static final String GOODS_BATCH_METHOD_NAME = "goodsBatchMethodName";

    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        String information = "";
        Map<String, Object> params = new LinkedMap<>();
        params.put("UserName", userName);
        params.put("Pwd", passWord);
        params.put("BeginDate", startDate);
        params.put("EndDate", endDate);
        params.put("Information", information);

        String purchaseMethodName = param.get(PURCHASE_METHOD_NAME);
        String responseName = purchaseMethodName.concat("Response");
        String resultName = purchaseMethodName.concat("Result");
        return doRequest(params, url, purchaseMethodName, responseName, resultName);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        String information = "";
        Map<String, Object> params = new LinkedMap<>();
        params.put("UserName", userName);
        params.put("Pwd", passWord);
        params.put("BeginDate", startDate);
        params.put("EndDate", endDate);
        params.put("Information", information);

        String saleMethodName = param.get(SALE_METHOD_NAME);
        String responseName = saleMethodName.concat("Response");
        String resultName = saleMethodName.concat("Result");
        return doRequest(params, url, saleMethodName, responseName, resultName);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String information = "";
        Map<String, Object> params = new LinkedMap<>();
        params.put("UserName", userName);
        params.put("Pwd", passWord);
        params.put("Information", information);

        String goodsBatchMethodName = param.get(GOODS_BATCH_METHOD_NAME);
        String responseName = goodsBatchMethodName.concat("Response");
        String resultName = goodsBatchMethodName.concat("Result");
        return doRequest(params, url, goodsBatchMethodName, responseName, resultName);
    }

    private String doRequest(Map<String, Object> params, String url, String methodName, String responseName, String resultName) {
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, methodName, "")).setParams(params, false).setConnectionTimeout(1000 * 60 * 5);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (jsonObject != null) {
            log.warn("查询第三方采购数据为空, 上海上药雷允上医药有限公司, jsonObject:{}", jsonObject.toString());
            JSONObject getRKOrderResponse = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName);
            String resultStr = getRKOrderResponse.getStr(resultName, "");
            if (StrUtil.isBlank(resultStr)) {
                String informationResponse = getRKOrderResponse.getStr("Information", "");
                if (StrUtil.isBlank(informationResponse)) {
                    log.warn("查询第三方采购数据为空, 上海上药雷允上医药有限公司, resultStr and Information is blank");
                    return null;
                }
                log.error("查询第三方采购数据返回异常信息, 上海上药雷允上医药有限公司, informationResponse:{}", informationResponse);
                return null;
            }
            JSONObject resultObject = JSONUtil.xmlToJson(resultStr);
            if (ObjectUtil.isNull(resultObject)) {
                log.warn("查询第三方采购数据为空, 上海上药雷允上医药有限公司, resultObject is null:{}", resultObject.toString());
                return null;
            }
            JSONObject newDataSet = resultObject.getJSONObject("NewDataSet");
            JSONArray rows = newDataSet.getJSONArray("Table");
            rows.forEach(o -> {
                JSONObject jsonObject1 = (JSONObject) o;
                strTrim(jsonObject1);
                if (ObjectUtil.equal(methodName, "GetRKOrder")) {
                    handlerDateField(jsonObject1, "InvoiceDate");
                }
                if (ObjectUtil.equal(methodName, "GetXSOrderX")) {
                    handlerDateField(jsonObject1, "date");
                }
                if (ObjectUtil.equal(methodName, "KCEX")) {
                    handlerDateField(jsonObject1, "date", "yxrq", "scrq");
                }
            });
            return JSONUtil.toJsonStr(rows);
        }
        return null;
    }

    public void handlerDateField(JSONObject jsonObject, String... fieldNames) {
        if (ObjectUtil.isNull(jsonObject) || ArrayUtil.isEmpty(fieldNames)) {
            return;
        }
        for (String fieldName : fieldNames) {
            String value = (String) jsonObject.get(fieldName);
            if (StrUtil.isBlank(value)) {
                jsonObject.set(fieldName, null);
            } else {
                jsonObject.set(fieldName, DateUtil.parse(value));
            }
        }
    }

    public void strTrim(JSONObject jsonObject) {
        if (ObjectUtil.isNull(jsonObject)) {
            return;
        }
        Set<String> keys = jsonObject.keySet();
        if (CollUtil.isEmpty(keys)) {
            return;
        }
        keys.forEach(key -> {
            Object object = jsonObject.get(key);
            if (ObjectUtil.isNotNull(object) && object instanceof String) {
                jsonObject.set(key, ((String) object).trim());
            }
        });
    }

/*
    public static void main(String[] args) {
        String url = "https://www.lysxsfgs.com.cn/gys/everyone/gys.asmx";
        String userName = "SJiaZYL";
        String passWord = "132911@@";
        String beginDate = "2022-07-01";
        String endDate = "2022-07-28";
        String information = "";
        Map<String, Object> params = new LinkedMap<>();
        params.put("UserName", userName);
        params.put("Pwd", passWord);
        params.put("BeginDate", beginDate);
        params.put("EndDate", endDate);
        params.put("Information", information);

        String methodName = "KCEX";
        String responseName = methodName.concat("Response");
        String resultName = methodName.concat("Result");
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, methodName, "")).setParams(params, false).setConnectionTimeout(1000 * 60 * 5);
        String str = client.send();
        cn.hutool.json.JSONObject jsonObject = JSONUtil.xmlToJson(str);
        System.out.println(">>>>> response:" + jsonObject.toString());
        if (jsonObject != null) {
            JSONObject getRKOrderResponse = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName);
            String resultStr = getRKOrderResponse.getStr(resultName, "");
            if (StrUtil.isBlank(resultStr)) {
                String informationResponse = getRKOrderResponse.getStr("Information", "");
                if (StrUtil.isBlank(informationResponse)) {
                    log.warn("查询第三方采购数据为空, 上海上药雷允上医药有限公司, resultStr and Information is blank");
                    return;
                }
                log.error("查询第三方采购数据返回异常信息, 上海上药雷允上医药有限公司, informationResponse:{}", informationResponse);
                return;
            }
            cn.hutool.json.JSONObject resultObject = JSONUtil.xmlToJson(resultStr);
            System.out.println(">>>>> resultObject:" + resultObject.toString());
            if (ObjectUtil.isNull(resultObject)) {
                log.warn("查询第三方采购数据为空, 上海上药雷允上医药有限公司, resultObject is null");
                return;
            }
            JSONObject newDataSet = resultObject.getJSONObject("NewDataSet");
            JSONArray rows = newDataSet.getJSONArray("Table");
            System.out.println(">>>>> rows:" + rows.toString());
        }
    }
*/

}
