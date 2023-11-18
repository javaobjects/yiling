package com.yiling.open.third.service.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/11/18
 */
@Slf4j
@Service("chengDeBaiJianService")
public class ChengDeBaiJianImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String PREFIX_SOAPENV = "soapenv";
    private static final String NAME_SPACE = "web";
    private static final String NAME_SPACE_URI = "http://webservice.datazl.honghui.com/";
    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String URL = "url";
    private static final String METHOD_NAME = "methodName";
    private static final String PURCHASE_TYPE = "purchaseType";
    private static final String SALE_TYPE = "saleType";
    private static final String GOODS_BATCH_TYPE = "goodsBatchType";

    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String methodName = param.get(METHOD_NAME);
        String businessType = param.get(PURCHASE_TYPE);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy/MM/dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy/MM/dd");

        Map<String, Object> params = new LinkedMap<>();
        params.put("userName", userName);
        params.put("passWord", passWord);
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        params.put("businessType", businessType);

        return doRequest(params, url, methodName, businessType);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String methodName = param.get(METHOD_NAME);
        String businessType = param.get(SALE_TYPE);

        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy/MM/dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy/MM/dd");
        Map<String, Object> params = new LinkedMap<>();
        params.put("userName", userName);
        params.put("passWord", passWord);
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        params.put("businessType", businessType);

        String responseName = methodName.concat("Response");
        String resultName = methodName.concat("Result");
        return doRequest(params, url, methodName, businessType);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String methodName = param.get(METHOD_NAME);
        String businessType = param.get(GOODS_BATCH_TYPE);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy/MM/dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy/MM/dd");

        Map<String, Object> params = new LinkedMap<>();
        params.put("userName", userName);
        params.put("passWord", passWord);
        params.put("businessType", businessType);
        params.put("beginDate", startDate);
        params.put("endDate", endDate);

        String responseName = methodName.concat("Response");
        String resultName = methodName.concat("Result");
        return doRequest(params, url, methodName, businessType);
    }

    private String doRequest(Map<String, Object> params, String url, String methodName, String businessType) {
        SOAPMessage soapResponse = sendRequest(url, methodName, params);
        JSONArray rows = handleResponse(soapResponse, businessType);
        if (ObjectUtil.isNull(rows) || rows.size() == 0) {
            return "";
        }
        rows.forEach(o -> {
            JSONObject jsonObject1 = (JSONObject) o;
            strTrim(jsonObject1);
            if (ObjectUtil.equal(businessType, "CG")) {
                handlerDateField(jsonObject1, "credate", "invaliddate");
            }
            if (ObjectUtil.equal(businessType, "XS")) {
                handlerDateField(jsonObject1, "credate", "proddate", "invaliddate");
            }
            if (ObjectUtil.equal(businessType, "KC")) {
                handlerDateField(jsonObject1, "credate", "proddate", "invaliddate");
            }
        });
        return JSONUtil.toJsonStr(rows);
    }

    private SOAPMessage sendRequest(String url, String methodName, Map<String, Object> params) {
        SOAPMessage soapResponse = null;
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            // Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // Header
            SOAPHeader header = soapMessage.getSOAPHeader();
            // Body
            SOAPBody body = soapMessage.getSOAPBody();
            // 移除xmlns:SOAP-ENV
            envelope.removeNamespaceDeclaration(envelope.getPrefix());
            // 设置前缀 soapenv
            envelope.setPrefix(PREFIX_SOAPENV);
            header.setPrefix(PREFIX_SOAPENV);
            body.setPrefix(PREFIX_SOAPENV);

            envelope.addNamespaceDeclaration(PREFIX_SOAPENV, "http://schemas.xmlsoap.org/soap/envelope/");
            envelope.addNamespaceDeclaration(NAME_SPACE, NAME_SPACE_URI);
            // 定义soap body
            SOAPElement soapBodyElement = body.addChildElement(methodName, NAME_SPACE);
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            // 构建请求参数
            List<SOAPElement> soapElements = buildSoapElement(soapFactory, params);
            for (SOAPElement soapElement : soapElements) {
                soapBodyElement.addChildElement(soapElement);
            }
            soapMessage.saveChanges();
            // soapMessage.writeTo(System.out+"\r\n");
            // 发送请求
            soapResponse = soapConnection.call(soapMessage, url);
            // soapResponse.writeTo(System.out+"\r\n");
        } catch (SOAPException e) {
            log.error("查询第三方流向数据异常, 承德柏健医药有限责任公司, 构建请求参数失败，SOAPException:{}", e.getMessage());
            e.printStackTrace();
        }
        return soapResponse;
    }

    /**
     * 构建请求参数
     *
     * @param soapFactory soapFactory
     * @param params 请求参数
     * @return
     * @throws SOAPException
     */
    public static List<SOAPElement> buildSoapElement(SOAPFactory soapFactory, Map<String, Object> params) throws SOAPException {
        String userName = params.get("userName").toString();
        String passWord = params.get("passWord").toString();
        String beginDate = ObjectUtil.isNotNull(params.get("beginDate")) ? params.get("beginDate").toString() : "";
        String endDate = ObjectUtil.isNotNull(params.get("endDate")) ? params.get("endDate").toString() : "";
        String businessType = params.get("businessType").toString();
        List<SOAPElement> soapElementList = new ArrayList<>();

        SOAPElement arg0 = soapFactory.createElement("arg0");
        arg0.setTextContent(userName);
        soapElementList.add(arg0);

        SOAPElement arg1 = soapFactory.createElement("arg1");
        arg1.setTextContent(passWord);
        soapElementList.add(arg1);

        if (StrUtil.isNotBlank(beginDate)) {
            SOAPElement arg2 = soapFactory.createElement("arg2");
            arg2.setTextContent(beginDate);
            soapElementList.add(arg2);
        }

        if (StrUtil.isNotBlank(endDate)) {
            SOAPElement arg3 = soapFactory.createElement("arg3");
            arg3.setTextContent(endDate);
            soapElementList.add(arg3);
        }

        SOAPElement arg4 = soapFactory.createElement("arg4");
        arg4.setTextContent(businessType);
        soapElementList.add(arg4);

        return soapElementList;
    }

    /**
     * 解析响应结果
     *
     * @param soapResponse soap响应结果
     * @return
     */
    private JSONArray handleResponse(SOAPMessage soapResponse, String businessType) {
        JSONArray rows = new JSONArray();
        // 解析soap结果
        SOAPBody soapBodyResponse = null;
        try {
            soapBodyResponse = soapResponse.getSOAPBody();
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        if (ObjectUtil.isNull(soapBodyResponse)) {
            log.error("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, {}, soapBodyResponse is null", businessType);
            return rows;
        }

        //        Iterator childElements = soapBodyResponse.getChildElements();
        //        if (CollUtil.isEmpty(childElements)) {
        //            log.error("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, childElements is empty");
        //            return rows;
        //        }
        getResponseData(soapBodyResponse, rows, businessType);
        //        if (ObjectUtil.isNotNull(rows) && rows.size() > 0) {
        //            System.out.println(">>>>> rows:" + rows.toString());
        //        }
        return rows;
    }


    /**
     * 解析xml内容
     *
     * @param iterator xml节点列表
     * @param rows 返回数据列表
     * @return
     */
    private static void getResponseData(SOAPBody soapBodyResponse, JSONArray rows, String businessType) {

        NodeList nodeList = soapBodyResponse.getElementsByTagName("return");
        if (ObjectUtil.isNull(nodeList) || nodeList.getLength() == 0) {
            log.warn("查询第三方数据为空, 承德柏健医药有限责任公司{}, nodeList is empty", businessType);
            return;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList itemList = nodeList.item(i).getChildNodes();
            if (ObjectUtil.isNull(itemList) || itemList.getLength() == 0) {
                log.warn("查询第三方数据为空, 承德柏健医药有限责任公司{}, itemList is empty", businessType);
                return;
            }
            for (int j = 0; j < itemList.getLength(); j++) {
                String itemname = itemList.item(j).getNodeName();
                String textContent = itemList.item(j).getTextContent().trim();
                if (StrUtil.isBlank(textContent)) {
                    log.error("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, {}, textContent is blank", businessType);
                    return;
                }
                if (textContent.contains("code") || textContent.contains("time")) {
                    String json = JSONUtil.toJsonStr(textContent);
                    JSONObject jsonObject = JSONUtil.parseObj(json);
                    Object code = jsonObject.get("code");
                    if (ObjectUtil.isNotNull(code)) {
                        log.warn("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, {}, code:{}", businessType, code.toString());
                        return;
                    }
                    Object time = jsonObject.get("time");
                    if (ObjectUtil.isNotNull(time)) {
                        log.warn("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, {}, time:{}", businessType, time.toString());
                        return;
                    }
                }
                if (StrUtil.isNotBlank(textContent) && textContent.contains("null")) {
                    textContent = textContent.substring(4);
                }
                if (textContent.contains("\r")) {
                    // 列表
                    textContent = textContent.replaceAll("\r", ",");
                    String result = "[".concat(textContent).concat("]");
                    String json = JSONUtil.toJsonStr(result);
                    JSONArray jsonArray = JSONUtil.parseArray(json);
                    rows.addAll(jsonArray);
                } else {
                    // 单条
                    JSONObject jsonObject = JSONUtil.parseObj(textContent);
                    rows.add(jsonObject);
                }
            }
        }
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
        String url = "http://47.94.105.63:8080/DataZLWebService/services/DataZLService";
        String methodName = "getData";
        String responseName = methodName.concat("Response");
        String resultName = "return";

        SOAPMessage soapResponse = null;
        // 构建soap请求
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            // Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            // Header
            SOAPHeader header = soapMessage.getSOAPHeader();
            // Body
            SOAPBody body = soapMessage.getSOAPBody();
            // 移除xmlns:SOAP-ENV
            envelope.removeNamespaceDeclaration(envelope.getPrefix());
            // 设置前缀 soapenv
            envelope.setPrefix(PREFIX_SOAPENV);
            header.setPrefix(PREFIX_SOAPENV);
            body.setPrefix(PREFIX_SOAPENV);

            envelope.addNamespaceDeclaration(PREFIX_SOAPENV, "http://schemas.xmlsoap.org/soap/envelope/");
            envelope.addNamespaceDeclaration(NAME_SPACE, NAME_SPACE_URI);
            // 定义soap body
            SOAPElement soapBodyElement = body.addChildElement(methodName, NAME_SPACE);
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            // 构建请求参数
            Map<String, Object> params = new LinkedMap<>();
            params.put("userName", "以岭药业");
            params.put("passWord", "ylyy");
            params.put("beginDate", "2022/11/01");
            params.put("endDate", "2022/11/22");
            params.put("businessType", "CG");

            List<SOAPElement> soapElements = buildSoapElement(soapFactory, params);
            for (SOAPElement soapElement : soapElements) {
                soapBodyElement.addChildElement(soapElement);
            }
            soapMessage.saveChanges();
            //            soapMessage.writeTo(System.out);
            // 发送请求
            soapResponse = soapConnection.call(soapMessage, url);
            // soapResponse.writeTo(System.out);
        } catch (SOAPException e) {
            log.error("查询第三方流向数据异常, 承德柏健医药有限责任公司, 构建请求参数失败，SOAPException:{}", e.getMessage());
            e.printStackTrace();
        }

        // 解析soap结果
        try {
            // SOAPBody soapBodyResponse = soapResponse.getSOAPBody();

            // todo 模拟返回数据进行测试
            String response = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<soap:Body>" + "<ns2:getDataResponse xmlns:ns2=\"http://webservice.datazl.honghui.com/\">" + "<return>{code:请在早8点之前或下午6点之后调用}</return>" + "</ns2:getDataResponse>" + "</soap:Body>" + "</soap:Envelope>";
            //            String response = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:getDataResponse xmlns:ns2=\"http://webservice.datazl.honghui.com/\"><return>null{\"costingmoney\":\"54138.05\",\"costingprice\":\"22.5575208333\",\"credate\":\"2022-11-09 15:30:29\",\"factoryname\":\"北京以岭药业有限公司\",\"goodsid\":\"651\",\"goodsname\":\"参松养心胶囊\",\"goodsno\":\"651\",\"goodsqty\":\"2400\",\"goodstype\":\"0.4g*36粒\",\"goodsunit\":\"盒\",\"invaliddate\":\"2025-09-30 00:00:00\",\"lotno\":\"2210007\",\"medicinetypename\":\"胶囊剂\",\"sudocid\":\"3050\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"53223.12\",\"unitprice\":22.1763}&#xD;{\"costingmoney\":\"6365.31\",\"costingprice\":\"26.522125\",\"credate\":\"2022-11-09 16:00:47\",\"factoryname\":\"石家庄以岭药业股份有限公司 \",\"goodsid\":\"1005\",\"goodsname\":\"津力达颗粒\",\"goodsno\":\"1005\",\"goodsqty\":\"240\",\"goodstype\":\"9g*9袋\",\"goodsunit\":\"盒\",\"invaliddate\":\"2025-07-31 00:00:00\",\"lotno\":\"B2208002\",\"medicinetypename\":\"颗粒剂\",\"sudocid\":\"3056\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"6257.736\",\"unitprice\":26.0739}&#xD;{\"costingmoney\":\"16396.46\",\"costingprice\":\"40.99115\",\"credate\":\"2022-11-09 16:02:12\",\"factoryname\":\"石家庄以岭药业股份有限公司 \",\"goodsid\":\"1270\",\"goodsname\":\"连花清咳片\",\"goodsno\":\"1270\",\"goodsqty\":\"400\",\"goodstype\":\"每片重0.46g（相当于饮片1.84g）*36片\",\"goodsunit\":\"盒\",\"invaliddate\":\"2024-05-31 00:00:00\",\"lotno\":\"A2106001\",\"medicinetypename\":\"片剂\",\"sudocid\":\"3057\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"16119.36\",\"unitprice\":40.2984}&#xD;{\"costingmoney\":\"56028.32\",\"costingprice\":\"23.3451333333\",\"credate\":\"2022-11-09 16:06:33\",\"factoryname\":\"石家庄以岭药业股份有限公司 \",\"goodsid\":\"961\",\"goodsname\":\"通心络胶囊\",\"goodsno\":\"961\",\"goodsqty\":\"2400\",\"goodstype\":\"0.26g*30粒\",\"goodsunit\":\"盒\",\"invaliddate\":\"2025-05-31 00:00:00\",\"lotno\":\"B2206002\",\"medicinetypename\":\"胶囊剂\",\"sudocid\":\"3058\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"55081.44\",\"unitprice\":22.9506}&#xD;{\"costingmoney\":\"20132.74\",\"costingprice\":\"20.13274\",\"credate\":\"2022-11-16 12:02:30\",\"factoryname\":\"北京以岭药业有限公司\",\"goodsid\":\"969\",\"goodsname\":\"连花清瘟颗粒\",\"goodsno\":\"969\",\"goodsqty\":\"1000\",\"goodstype\":\"6g*10袋\",\"goodsunit\":\"盒\",\"invaliddate\":\"2024-09-30 00:00:00\",\"lotno\":\"A2204019H\",\"medicinetypename\":\"颗粒剂\",\"sudocid\":\"3097\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"19792.5\",\"unitprice\":19.7925}&#xD;{\"costingmoney\":\"32212.39\",\"costingprice\":\"20.13274375\",\"credate\":\"2022-11-16 12:02:30\",\"factoryname\":\"北京以岭药业有限公司\",\"goodsid\":\"969\",\"goodsname\":\"连花清瘟颗粒\",\"goodsno\":\"969\",\"goodsqty\":\"1600\",\"goodstype\":\"6g*10袋\",\"goodsunit\":\"盒\",\"invaliddate\":\"2024-09-30 00:00:00\",\"lotno\":\"A2204019H\",\"medicinetypename\":\"颗粒剂\",\"sudocid\":\"3097\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"31668\",\"unitprice\":19.7925}&#xD;{\"costingmoney\":\"37352.21\",\"costingprice\":\"23.34513125\",\"credate\":\"2022-11-16 13:47:47\",\"factoryname\":\"石家庄以岭药业股份有限公司 \",\"goodsid\":\"961\",\"goodsname\":\"通心络胶囊\",\"goodsno\":\"961\",\"goodsqty\":\"1600\",\"goodstype\":\"0.26g*30粒\",\"goodsunit\":\"盒\",\"invaliddate\":\"2025-05-31 00:00:00\",\"lotno\":\"B2206002\",\"medicinetypename\":\"胶囊剂\",\"sudocid\":\"3098\",\"supplyid\":\"36\",\"supplyname\":\"红惠医药有限公司\",\"sutypeid\":\"采购单\",\"total_line\":\"36720.96\",\"unitprice\":22.9506}&#xD;</return></ns2:getDataResponse></soap:Body></soap:Envelope>";
            SOAPMessage soapBodyMessage = formatSoapString(response);
            SOAPBody soapBodyResponse = soapBodyMessage.getSOAPBody();
            // todo 模拟返回数据进行测试

            if (ObjectUtil.isNull(soapBodyResponse)) {
                log.error("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, soapBodyResponse is null");
                return;
            }

            //            Iterator childElements = soapBodyResponse.getChildElements();

            // Iterator childElements = soapBodyResponse.getChildElements();
            //            if (CollUtil.isEmpty(childElements)) {
            //                log.error("查询第三方采购数据返回异常信息, 承德柏健医药有限责任公司, childElements is empty");
            //                return;
            //            }
            JSONArray rows = new JSONArray();
            getResponseData(soapBodyResponse, rows, "businessType");
            if (ObjectUtil.isNotNull(rows) && rows.size() > 0) {
                System.out.println(">>>>> rows:" + rows.toString());
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return;
    }


    public static SOAPMessage formatSoapString(String soapString) {
        MessageFactory msgFactory;
        try {
            msgFactory = MessageFactory.newInstance();
            SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(soapString.getBytes("UTF-8")));
            reqMsg.saveChanges();
            return reqMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
