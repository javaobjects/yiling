package com.yiling.open.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/5/10
 */
@Service("guoYaoAnHuiService")
@Slf4j
public class GuoYaoAnHuiServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String PURCHASE = "purchase";
    private static final String SALE = "sale";
    private static final String GOODS_BATCH = "goodsBatch";
    private static final String URL = "url";
    private static final String TOKEN = "token";
    private static final String USERID = "userid";
    private static final String PASSWORD = "password";
    private static final String PURCHASE_METHOD = "purchaseMethod";
    private static final String SALE_METHOD = "saleMethod";
    private static final String GOODS_BATCH_METHOD = "goodsBatchMethod";


    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        // 方法名称
        String methodName = param.get(PURCHASE_METHOD);
        String responseName = methodName.concat("Response");
        String resultName = methodName.concat("Result");

        // 查询接口获取密文
        JSONArray jsonArrayResult = new JSONArray();
        String ciphertext = doRequest(PURCHASE, param, methodName, responseName, resultName);
        if (StrUtil.isNotBlank(ciphertext)) {
            // 解密获取明文
            String plaintext = decrypt(ciphertext);
            // 获取列表数据
            jsonArrayResult = getDataList(plaintext);
        }
        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        // 方法名称
        String methodName = param.get(SALE_METHOD);
        String responseName = methodName.concat("Response");
        String resultName = methodName.concat("Result");

        // 查询接口获取密文
        JSONArray jsonArrayResult = new JSONArray();
        String ciphertext = doRequest(SALE, param, methodName, responseName, resultName);
        if (StrUtil.isNotBlank(ciphertext)) {
            // 解密获取明文
            String plaintext = decrypt(ciphertext);
            // 获取列表数据
            jsonArrayResult = getDataList(plaintext);
        }
        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        // 方法名称
        String methodName = param.get(GOODS_BATCH_METHOD);
        String responseName = methodName.concat("Response");
        String resultName = methodName.concat("Result");

        // 查询接口获取密文
        JSONArray jsonArrayResult = new JSONArray();
        String ciphertext = doRequest(GOODS_BATCH, param, methodName, responseName, resultName);
        if (StrUtil.isNotBlank(ciphertext)) {
            // 解密获取明文
            String plaintext = decrypt(ciphertext);
            // 获取列表数据
            jsonArrayResult = getDataList(plaintext);
        }
        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    /**
     * 查询三方接口
     *
     * @param type purchase-采购，sale-销售，goodsBatch-库存
     * @param param 配置的接口基本参数
     * @param methodName 服务方法名称
     * @param responseName 接口响应名称
     * @param resultName 接口响应结果
     * @return
     */
    private String doRequest(String type, Map<String, String> param, String methodName, String responseName, String resultName) {
        if (StrUtil.isBlank(param.get(FlowAbstractTemplate.startTime)) || StrUtil.isBlank(param.get(FlowAbstractTemplate.endTime))) {
            return null;
        }
        // 组装查询请求参数
        Map<String, Object> params = new LinkedMap<>();
        params.put(TOKEN, param.get(TOKEN));
        params.put(USERID, param.get(USERID));
        params.put(PASSWORD, param.get(PASSWORD));
        // 采购、销售查询时间
        if (!ObjectUtil.equal(GOODS_BATCH, type)) {
            params.put("begindate", StrUtil.sub(param.get(FlowAbstractTemplate.startTime), 0, 10));
            params.put("enddate", StrUtil.sub(param.get(FlowAbstractTemplate.endTime), 0, 10));
        }
        params.put("isfilter", 0);
        String jsonInput = JSONUtil.toJsonStr(params);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("context", jsonInput);

        String url = param.get(URL);
        // 查询
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_2, "http://tempuri.org/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName("http://tempuri.org/", methodName, "")).setParams(inputParams, false).setConnectionTimeout(1000 * 60 * 5).setReadTimeout(1000 * 60 * 5);
        String message = client.getMsgStr(true);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (ObjectUtil.isNull(jsonObject)) {
            log.info("国控安徽, 查询流向接口数据为空, jsonObject is null, methodName:{}",methodName);
            return null;
        }
        String encryptionResult = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName).getStr(resultName, "");
        if (StrUtil.isBlank(encryptionResult)) {
            log.info("国控安徽, 查询流向接口数据获取秘文为空, encryptionResult is blank, methodName:{}",methodName);
            return null;
        }
        return encryptionResult;
    }

    private static String decrypt(String ciphertext) {
        String plaintext = "";
        String methodName = "Decrypt";

        Map<String, Object> params = new LinkedMap<>();
        params.put("inputString", ciphertext);
        SoapClient client = SoapClient.create("http://120.211.109.111:8282/", SoapProtocol.SOAP_1_2, "http://tempuri.org/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName("http://tempuri.org/", methodName, "")).setParams(params, false);
        String message = client.getMsgStr(true);
        plaintext = client.send();
        if (StrUtil.isBlank(plaintext)) {
            log.info("国控安徽, 解析密文异常, plaintext is blank");
            return null;
        }
        return plaintext;
    }

    /**
     * 获取结果数据
     *
     * @param plaintext 明文
     * @return
     * @throws Exception
     */
    private JSONArray getDataList(String plaintext) {
        JSONArray jsonArray = new JSONArray();

        JSONObject plaintextObject = JSONUtil.xmlToJson(plaintext);
        if (ObjectUtil.isNull(plaintextObject)) {
            log.info("国控安徽, 解析获取明文为空, plaintextObject is null");
            return jsonArray;
        }

        String decryptResponse = "DecryptResponse";
        String decryptResult = "DecryptResult";
        String result = plaintextObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(decryptResponse).getStr(decryptResult, "");
        //        System.out.println(">>>>> result:" + result);
        if (StrUtil.isBlank(result)) {
            log.info("查询第三方接口数据为空, 国控安徽, result:{}", result);
            return jsonArray;
        }
        JSONObject resultObject = (JSONObject) JSONUtil.parse(result);
        String code = resultObject.getStr("Code", "");
        if (!ObjectUtil.equal("0", code)) {
            String content = resultObject.getStr("Content", "");
            log.info(">>>>> 查询第三方接口报错, 国控安徽, Code:{}, Content:{}", code, content);
        }
        jsonArray = resultObject.getJSONArray("Table");
        if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
            log.info("查询第三方接口数据为空, 国控安徽, esponse Table is null");
            return jsonArray;
        }
        // 库存主键，全字段拼接
        buildGbIdNo(jsonArray);
        return jsonArray;
    }

    /**
     * 库存主键
     * 公司名称+商品编码+批号+生产日期+效期+可分配数量+商品名称+批准文号+规格+单位+生产商
     *
     * @param jsonArray
     */
    private void buildGbIdNo(JSONArray jsonArray) {
        if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
            return;
        }
        for (Object o : jsonArray) {
            JSONObject jsonObj = (JSONObject) o;
            // 公司名称
            String sudeptno = jsonObj.getStr("公司名称", "");
            if (StrUtil.isNotBlank(sudeptno)) {
                sudeptno = sudeptno.trim();
                jsonObj.set("公司名称", sudeptno);
            }
            // 商品编码
            String inSn = jsonObj.getStr("商品编码", "");
            // 批号
            String gbBatchNo = jsonObj.getStr("批号", "");
            // 生产日期
            String gbProduceTime = jsonObj.getStr("生产日期", "1970-01-01 00:00:00");
            // 效期
            String gbEndTime = jsonObj.getStr("效期", "1970-01-01 00:00:00");
            // 可分配数量
            String gbNumber = jsonObj.getStr("可分配数量", "0");
            // 商品名称
            String gbName = jsonObj.getStr("商品名称", "");
            // 批准文号
            String gbLicense = jsonObj.getStr("批准文号", "");
            // 规格
            String gbSpecifications = jsonObj.getStr("规格", "");
            // 单位
            String gbUnit = jsonObj.getStr("单位", "");
            // 生产商
            String gbManufacturer = jsonObj.getStr("生产商", "");

            StringBuffer sb = new StringBuffer();
            sb.append(sudeptno).append(inSn).append(gbBatchNo).append(gbProduceTime).append(gbEndTime).append(gbNumber).append(gbName).append(gbLicense).append(gbSpecifications).append(gbUnit).append(gbManufacturer);
            String key = SecureUtil.md5(sb.toString());
            jsonObj.set("全字段拼接主键", key);
        }
    }


    //    public static void main(String[] args) {
    //        JSONArray jsonArrayResult = new JSONArray();
    //        JSONArray jsonArray = null;
    //        try {
    //            Map<String, Object> params = new LinkedMap<>();
    //            params.put("token", "pumjc3dhyxq0lb5t1z4nraeg76i8k9fwv");
    //            params.put("userid", "045163-01");
    //            params.put("password", "abcd_123456");
    //            // 采购、销售时间
    //            params.put("begindate", "2023-04-01");
    //            params.put("enddate", "2023-04-30");
    //            params.put("isfilter", 0);
    //            String jsonInput = JSONUtil.toJsonStr(params);
    //            Map<String, Object> inputParams = new HashMap<>();
    //            inputParams.put("context", jsonInput);
    //
    ////                    String methodName = "WebDDI_GetPurBill";
    ////                    String methodName = "WebDDI_GetSalBill";
    //            String methodName = "WebDDI_GetInv";
    //            String responseName = methodName.concat("Response");
    //            String resultName = methodName.concat("Result");
    //
    //
    //            SoapClient client = SoapClient.create("http://www.gykgah.com:8067/WebDDIService.asmx", SoapProtocol.SOAP_1_2, "http://tempuri.org/")
    //                    // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
    //                    .setMethod(new QName("http://tempuri.org/", methodName, "")).setParams(inputParams, false);
    //            String message = client.getMsgStr(true);
    //            String str = client.send();
    //            //            String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" + "    <soap:Body>\n" + "        <gksd_webddi_purbillResponse xmlns=\"http://tempuri.org/\">\n" + "            <gksd_webddi_purbillResult>GaAqV+EjGC+trEmGCNq0X7GJODyIOWMjg0WyZr2y2/hl1UAB3b4O/VL7oYkEhd9cu+ctzD760CLmlRi1tmicRpXa4UnRrQqlyvKe34BHmiw9rojO842egQsecFm2wV4ucXm23hD1tHTiFi2psRwTjY+OPcD/9FQWIkD1qeKMvu0=</gksd_webddi_purbillResult>\n" + "        </gksd_webddi_purbillResponse>\n" + "    </soap:Body>\n" + "</soap:Envelope>";
    //            System.out.println(">>>>> client.send result is " + str);
    //            JSONObject jsonObject = JSONUtil.xmlToJson(str);
    //            if (jsonObject != null) {
    //                 // 密文
    //                String ciphertext = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName).getStr(resultName, "");
    //                System.out.println(">>>>> encryptionResult:" + ciphertext);
    //
    //            // todo
    ////                    String ciphertext = RsaUtil.getContent("E:\\content.txt");
    ////                    System.out.println(">>>>> encryptionResult:" + ciphertext);
    //
    //                // 解密获取明文
    //                String plaintext = decrypt(ciphertext);
    //                System.out.println(">>>>> plaintext:" + plaintext);
    //                JSONObject plaintextObject = JSONUtil.xmlToJson(plaintext);
    //                if (plaintextObject != null) {
    //                    String decryptResponse = "DecryptResponse";
    //                    String decryptResult = "DecryptResult";
    //
    //                    String result = plaintextObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(decryptResponse).getStr(decryptResult, "");
    //                    System.out.println(">>>>> result:" + result);
    //
    //                    if (StrUtil.isBlank(result)) {
    //                        System.out.println(">>>>> result is null");
    //                    }
    //                    JSONObject resultObject = (JSONObject) JSONUtil.parse(result);
    //                    String code = resultObject.getStr("Code", "");
    //                    if (!ObjectUtil.equal("0", code)) {
    //                        String content = resultObject.getStr("Content", "");
    //                        System.out.println(">>>>> response error msg, Code:" + code + ", Content:" + content);
    //                    }
    //                    jsonArray = resultObject.getJSONArray("Table");
    //                    if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
    //                        System.out.println(">>>>> response Table is null");
    //                    }
    //
    //                    jsonArrayResult.addAll(jsonArray);
    //                }
    //
    //
    //            }
    //        } catch (Exception e) {
    //            System.err.println(e.toString());
    //        }
    //
    //        System.out.println(">>>>> jsonArrayResult:" + JSONUtil.toJsonStr(jsonArrayResult));
    //
    ////            for (Object o: jsonArrayResult) {
    ////                JSONObject jo = (JSONObject)o;
    ////                for (String key : jo.keySet()) {
    ////                    String str = jo.get(key).toString();
    ////                    if (str.contains("国药控股安庆有限公司")) {
    ////                        System.out.println("********** 有子公司数据 国药控股安庆有限公司 :" + JSONUtil.toJsonStr(jo));
    ////                    }
    ////                }
    ////            }
    //    }


}
