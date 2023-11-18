package com.yiling.open.third.service.impl;

import java.util.Date;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/19
 */
@Service("guoYaoGuangZhouService")
@Slf4j
public class GuoYaoGuangZhouServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String customID = param.get("customID");
        String userName = param.get("userName");
        String passWord = param.get("passWord");
        String url = param.get("url");
        String startTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        Map<String, Object> params = new LinkedMap<>();
        params.put("customID", customID);
        params.put("startDate", startTime);
        params.put("endDate", endTime);
        params.put("accessID", userName);
        params.put("accessPasswords", passWord);
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "http://vendorDDI.sinopharm_gz.com/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName("http://vendorDDI.sinopharm_gz.com/", "getPurchaseInfo", "tns")).setParams(params, false).setConnectionTimeout(1000*60*5);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (jsonObject != null) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:getPurchaseInfoResponse");
            return this.getJSONArrayByKey(jsonObject1,"return");
        }
        return null;
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String customID = param.get("customID");
        String userName = param.get("userName");
        String passWord = param.get("passWord");
        String url = param.get("url");
        String startTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        Map<String, Object> params = new LinkedMap<>();
        params.put("customID", customID);
        params.put("startDate", startTime);
        params.put("endDate", endTime);
        params.put("accessID", userName);
        params.put("accessPasswords", passWord);
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "http://vendorDDI.sinopharm_gz.com/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName("http://vendorDDI.sinopharm_gz.com/", "getSaleInfo", "tns")).setParams(params, false).setConnectionTimeout(1000*60*5);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (jsonObject != null) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:getSaleInfoResponse");
            return this.getJSONArrayByKey(jsonObject1,"return");
        }
        return null;
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String customID = param.get("customID");
        String userName = param.get("userName");
        String passWord = param.get("passWord");
        String url = param.get("url");
        Map<String, Object> params = new LinkedMap<>();
        params.put("customID", customID);
        params.put("accessID", userName);
        params.put("accessPasswords", passWord);
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, "http://vendorDDI.sinopharm_gz.com/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName("http://vendorDDI.sinopharm_gz.com/", "getStockInfo", "tns")).setParams(params, false).setConnectionTimeout(1000*60*5);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (jsonObject != null) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:getStockInfoResponse");
            return this.getJSONArrayByKey(jsonObject1,"return");
        }
        return null;
    }


    public static void main(String[] args) {
        try {
            Map<String, Object> params = new LinkedMap<>();
            params.put("customID", "1");
            params.put("startDate", "2022-04-01");
            params.put("endDate", "2022-04-20");
            params.put("accessID", "yiling");
            params.put("accessPasswords", "yiling12");
            SoapClient client = SoapClient.create("http://59.41.111.229:9000/vendorService?wsdl", SoapProtocol.SOAP_1_1, "http://vendorDDI.sinopharm_gz.com/")
                    // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                    .setMethod(new QName("http://vendorDDI.sinopharm_gz.com/", "getPurchaseInfo", "tns")).setParams(params, false);
            String message = client.getMsgStr(true);
            String str = client.send();
            System.out.println("result is " + str);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
