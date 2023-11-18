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
 * 河南和鼎医药有限公司接口
 *
 * @author: houjie.sun
 * @date: 2022/6/23
 */
@Slf4j
@Service("heNanHeDingService")
public class HeNanHeDingServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

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
        String purchaseMethodName = param.get(PURCHASE_METHOD_NAME);
        String responseName = purchaseMethodName.concat("Response");
        String resultName = purchaseMethodName.concat("Result");
        return doRequest(userName, passWord, url, purchaseMethodName, responseName, resultName);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String saleMethodName = param.get(SALE_METHOD_NAME);
        String responseName = saleMethodName.concat("Response");
        String resultName = saleMethodName.concat("Result");
        return doRequest(userName, passWord, url, saleMethodName, responseName, resultName);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String goodsBatchMethodName = param.get(GOODS_BATCH_METHOD_NAME);
        String responseName = goodsBatchMethodName.concat("Response");
        String resultName = goodsBatchMethodName.concat("Result");
        return doRequest(userName, passWord, url, goodsBatchMethodName, responseName, resultName);
    }

    private String doRequest(String userName, String passWord, String url, String methodName, String responseName, String resultName) {
        Map<String, Object> params = new LinkedMap<>();
        params.put("UserName", userName);
        params.put("PassWord", passWord);
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, methodName, "")).setParams(params, false).setConnectionTimeout(1000 * 60 * 5);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        System.out.println(">>>>> response:" + jsonObject.toString());
        if (ObjectUtil.isNull(jsonObject)) {
            log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, jsonObject is null, methodName:{}",methodName);
            return null;
        }

        JSONObject jsonObjectFail = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("soap:Fault");
        if (ObjectUtil.isNotNull(jsonObjectFail)) {
            String faultcode = jsonObjectFail.getStr("faultcode", "");
            String faultstring = jsonObjectFail.getStr("faultstring", "");
            log.error("查询第三方采购异常, 河南和鼎医药有限公司, methodName:{}, exception ==> {}", methodName, ("faultcode:[").concat(faultcode).concat("], faultstring:[").concat(faultstring).concat("]"));
            return null;
        }
        String resultStr = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName).getStr(resultName, "");
        if (StrUtil.isBlank(resultStr)) {
            log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, resultStr is blank, methodName:{}", methodName);
            return null;
        }
        JSONObject jsonMonitorData = JSONUtil.xmlToJson(resultStr);
        System.out.println(">>>>> monitorData:" + jsonMonitorData.toString());
        if (ObjectUtil.isNull(jsonMonitorData)) {
            log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, jsonMonitorData is null, methodName:{}", methodName);
            return null;
        }
        if (StrUtil.isBlank(jsonMonitorData.getStr("MonitorData", ""))) {
            log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, monitorData is blank, methodName:{}", methodName);
            return null;
        }
        JSONObject data = jsonMonitorData.getJSONObject("MonitorData");
        if (ObjectUtil.isNull(jsonMonitorData)) {
            log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, data is null, methodName:{}", methodName);
            return null;
        }

        JSONArray rows = data.getJSONArray("rows");
        System.out.println(">>>>> rows:" + rows.toString());
        rows.forEach(o -> {
            JSONObject jsonObject1 = (JSONObject) o;
            strTrim(jsonObject1);
            if (ObjectUtil.equal(methodName, "T_JHCX90")) {
                handlerDateField(jsonObject1, "kscrq", "kxq", "krq", "kcjsj");
            }
            if (ObjectUtil.equal(methodName, "T_XSCX90")) {
                handlerDateField(jsonObject1, "kscrq", "kxq", "krq", "kcjsj");
            }
            if (ObjectUtil.equal(methodName, "T_KCCX")) {
                handlerDateField(jsonObject1, "kscrq", "kxq", "kkcrq", "kcjsj");
            }
        });
        return JSONUtil.toJsonStr(rows);
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

    public static void main(String[] args) {
        // {"userName": "SJZYL","passWord": "8158AFCC179ED84FACC3ADF2971BFAB9","url": "http://125.46.53.82:8089/Service1.asmx",
        // "purchaseMethodName": "T_JHCX90",
        // "saleMethodName": "T_XSCX90",
        // "goodsBatchMethodName": "T_KCCX"}
        String userName = "SJZYL";
        String passWord = "8158AFCC179ED84FACC3ADF2971BFAB9";
        String url = "http://125.46.53.82:8089/Service1.asmx";
        String purchaseMethodName = "T_KCCX";
        Map<String, Object> params = new LinkedMap<>();
        params.put("UserName", userName);
        params.put("PassWord", passWord);
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, purchaseMethodName, "")).setParams(params, false).setConnectionTimeout(1000 * 60 * 5);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        System.out.println(">>>>> response:" + jsonObject.toString());
        if (jsonObject != null) {
            JSONObject jsonObjectFail = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("soap:Fault");
            if (ObjectUtil.isNotNull(jsonObjectFail)) {
                String faultcode = jsonObjectFail.getStr("faultcode", "");
                String faultstring = jsonObjectFail.getStr("faultstring", "");
                log.error("查询第三方采购异常, 河南和鼎医药有限公司, exception ==> {}", ("faultcode:[").concat(faultcode).concat("], faultstring:[").concat(faultstring).concat("]"));
                return;
            }
            String resultStr = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(purchaseMethodName.concat("Response")).getStr(purchaseMethodName.concat("Result"), "");
            if (StrUtil.isBlank(resultStr)) {
                log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, resultStr is blank");
                return;
            }
            JSONObject jsonMonitorData = JSONUtil.xmlToJson(resultStr);
            System.out.println(">>>>> monitorData:" + jsonMonitorData.toString());
            if (ObjectUtil.isNull(jsonMonitorData)) {
                log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, monitorData is null");
                return;
            }
            if (StrUtil.isBlank(jsonMonitorData.getStr("MonitorData", ""))) {
                log.warn("查询第三方采购数据为空, 河南和鼎医药有限公司, monitorData is blank");
                return;
            }
            JSONObject data = jsonMonitorData.getJSONObject("MonitorData");
            JSONArray rows = data.getJSONArray("rows");
            System.out.println(">>>>> rows:" + rows.toString());
        }

    }

}
