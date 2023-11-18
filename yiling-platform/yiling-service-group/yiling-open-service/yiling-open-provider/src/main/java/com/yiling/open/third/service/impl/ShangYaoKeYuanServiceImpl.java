package com.yiling.open.third.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/19
 */
@Service("shangYaoKeYuanService")
@Slf4j
public class ShangYaoKeYuanServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String userName = param.get("userName");
        String passWord = param.get("passWord");
        String url = param.get("url");
        String startTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        SoapClient client = SoapClient.create(url)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("GetPurchaseInfo", "http://tempuri.org/").setParam("userID", userName).setParam("userPwd", passWord).setParam("beginDate", startTime).setParam("endDate", endTime);
        String str = client.send(true);
        Document document = XmlUtil.parseXml(str);
        Element element = document.getDocumentElement();
        Element bodyElement = XmlUtil.getElement(element, "soap:Body");
        Element bodyElement1 = XmlUtil.getElement(bodyElement, "GetPurchaseInfoResponse");
        String xmlStr = XmlUtil.getElement(bodyElement1, "GetPurchaseInfoResult").getFirstChild().getNodeValue();
        JSONObject jsonObject = JSONUtil.xmlToJson(xmlStr);
        Object object = jsonObject.getObj("采购", null);
        if (object != null && object instanceof JSONObject) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("采购");
            if (jsonObject1 != null) {
                return this.getJSONArrayByKey(jsonObject1, "rows");
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String userName = param.get("userName");
        String passWord = param.get("passWord");
        String url = param.get("url");
        String startTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endTime = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        SoapClient client = SoapClient.create(url)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("GetSaleInfo", "http://tempuri.org/").setParam("userID", userName).setParam("userPwd", passWord).setParam("beginDate", startTime).setParam("endDate", endTime);
        String str = client.send(true);
        Document document = XmlUtil.parseXml(str);
        Element element = document.getDocumentElement();
        Element bodyElement = XmlUtil.getElement(element, "soap:Body");
        Element bodyElement1 = XmlUtil.getElement(bodyElement, "GetSaleInfoResponse");
        String xmlStr = XmlUtil.getElement(bodyElement1, "GetSaleInfoResult").getFirstChild().getNodeValue();
        JSONObject jsonObject = JSONUtil.xmlToJson(xmlStr);
        Object object = jsonObject.getObj("销售", null);
        if (object != null && object instanceof JSONObject) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("销售");
            if (jsonObject1 != null) {
                return this.getJSONArrayByKey(jsonObject1, "rows");
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String userName = param.get("userName");
        String passWord = param.get("passWord");
        String url = param.get("url");
        String nowTime = DateUtil.format(new Date(), "yyyy-MM-dd");
        SoapClient client = SoapClient.create(url)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("GetStockInfo", "http://tempuri.org/").setParam("userID", userName).setParam("userPwd", passWord).setParam("nowDate", nowTime);
        String str = client.send(true);
        Document document = XmlUtil.parseXml(str);
        Element element = document.getDocumentElement();
        Element bodyElement = XmlUtil.getElement(element, "soap:Body");
        Element bodyElement1 = XmlUtil.getElement(bodyElement, "GetStockInfoResponse");
        String xmlStr = XmlUtil.getElement(bodyElement1, "GetStockInfoResult").getFirstChild().getNodeValue();
        JSONObject jsonObject = JSONUtil.xmlToJson(xmlStr);
        Object object = jsonObject.getObj("库存", null);
        if (object != null && object instanceof JSONObject) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("库存");
            if (jsonObject1 != null) {
                return this.getJSONArrayByKey(jsonObject1, "rows");
            } else {
                return null;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        SoapClient client = SoapClient.create("http://kyddi.sphkeyuan.com/Service.asmx")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("GetStockInfo", "http://tempuri.org/").setParam("userID", "G_ZHANGXJ_01").setParam("userPwd", "bE3v^SkS").setParam("nowDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        String str = client.send(true);
        Document document = XmlUtil.parseXml(str);
        Element element = document.getDocumentElement();
        Element bodyElement = XmlUtil.getElement(element, "soap:Body");
        Element bodyElement1 = XmlUtil.getElement(bodyElement, "GetStockInfoResponse");
        String xmlStr = XmlUtil.getElement(bodyElement1, "GetStockInfoResult").getFirstChild().getNodeValue();
        JSONObject jsonObject = JSONUtil.xmlToJson(xmlStr);
        JSONArray jsonArray = jsonObject.getJSONObject("库存").getJSONArray("rows");
        System.out.println(jsonArray);
    }
}
