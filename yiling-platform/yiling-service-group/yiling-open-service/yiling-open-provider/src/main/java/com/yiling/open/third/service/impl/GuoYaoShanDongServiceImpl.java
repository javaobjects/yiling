package com.yiling.open.third.service.impl;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.xml.namespace.QName;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.file.FileReader;
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
 * @date: 2022/4/28
 */
@Service("guoYaoShanDongService")
@Slf4j
public class GuoYaoShanDongServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    /**
     * 指定加密算法为RSA
     */
    private static final String RSA = "RSA";

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 采购流向
     */
    private static final String PURCHASE = "purchase";

    /**
     * 销售流向
     */
    private static final String SALE = "sale";

    /**
     * 库存流向
     */
    private static final String GOODS_BATCH = "goodsBatch";


    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray = null;
        // 私匙
        String privateKey = param.get("privateKey");
        // 方法名称
        String methodName = "gksd_webddi_purbill";
        String responseName = "gksd_webddi_purbillResponse";
        String resultName = "gksd_webddi_purbillResult";

        int current = 1;
        int size = 500;
        do {
            // 查询
            String encryptionResult = doRequest(PURCHASE, param, methodName, responseName, resultName, current, size);
            if (StrUtil.isBlank(encryptionResult)) {
                break;
            }
            // 获取列表数据
            try {
                jsonArray = getDataList(privateKey, encryptionResult);
            } catch (Exception e) {
                log.error("查询第三方采购异常, 国控山东, exception:{}", e);
                e.printStackTrace();
            }
            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                break;
            }
            jsonArrayResult.addAll(jsonArray);

            if (jsonArray.size() < size) {
                break;
            }
            current = current + 1;
        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    // todo 待修改
    @Override
    protected String requestSaleTab(Map<String, String> param) {
        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray = null;
        // 私匙
        String privateKey = (String) param.get("privateKey");
        // 方法名称
        String methodName = "gksd_webddi_salebill";
        String responseName = "gksd_webddi_salebillResponse";
        String resultName = "gksd_webddi_salebillResult";

        int current = 1;
        int size = 500;
        do {
            // 查询
            String encryptionResult = doRequest(SALE, param, methodName, responseName, resultName, current, size);
            if (StrUtil.isBlank(encryptionResult)) {
                break;
            }
            // 获取列表数据
            try {
                jsonArray = getDataList(privateKey, encryptionResult);
            } catch (Exception e) {
                log.error("查询第三方销售异常, 国控山东, exception:{}", e);
                e.printStackTrace();
            }
            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                break;
            }
            jsonArrayResult.addAll(jsonArray);

            if (jsonArray.size() < size) {
                break;
            }
            current = current + 1;
        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray = null;
        // 私匙
        String privateKey = (String) param.get("privateKey");
        // 方法名称
        String methodName = "gksd_webddi_inv";
        String responseName = "gksd_webddi_invResponse";
        String resultName = "gksd_webddi_invResult";

        int current = 1;
        int size = 500;
        do {
            // 查询
            String encryptionResult = doRequest(GOODS_BATCH, param, methodName, responseName, resultName, current, size);
            if (StrUtil.isBlank(encryptionResult)) {
                break;
            }
            // 获取列表数据
            try {
                jsonArray = getDataList(privateKey, encryptionResult);
            } catch (Exception e) {
                log.error("查询第三方库存异常, 国控山东, exception:{}", e);
                e.printStackTrace();
            }
            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                break;
            }
            jsonArrayResult.addAll(jsonArray);

            if (jsonArray.size() < size) {
                break;
            }
            current = current + 1;
        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

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
     * @param current 当前页
     * @param size 每页数量
     * @return
     */
    private String doRequest(String type, Map<String, String> param, String methodName, String responseName, String resultName, Integer current, Integer size) {
        if (ObjectUtil.isNull(current) || current <= 0) {
            current = 1;
        }
        if (ObjectUtil.isNull(size) || size <= 0) {
            size = 200;
        }
        if (StrUtil.isBlank(param.get(FlowAbstractTemplate.startTime)) || StrUtil.isBlank(param.get(FlowAbstractTemplate.endTime))) {
            return null;
        }
        // 组装查询请求参数
        Map<String, Object> params = new LinkedMap<>();
        params.put("accesstoken", param.get("token"));
        params.put("account", param.get("userName"));
        params.put("password", param.get("passWord"));
        // 采购、销售查询时间
        if (!ObjectUtil.equal(GOODS_BATCH, type)) {
            params.put("date1", StrUtil.sub(param.get(FlowAbstractTemplate.startTime), 0, 10));
            params.put("date2", StrUtil.sub(param.get(FlowAbstractTemplate.endTime), 0, 10));
        }
        params.put("isfilter", param.get("filter"));
        params.put("curpage", current.toString());
        params.put("pagesize", size.toString());
        String jsonInput = JSONUtil.toJsonStr(params);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input", jsonInput);

        String url = param.get("url");
        // 查询
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_2, "http://tempuri.org/")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName("http://tempuri.org/", methodName, "")).setParams(inputParams, false).setConnectionTimeout(1000 * 60 * 5).setReadTimeout(1000 * 60 * 5);
        String message = client.getMsgStr(true);
        String str = client.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (ObjectUtil.isNull(jsonObject)) {
            return null;
        }
        String encryptionResult = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName).getStr(resultName, "");
        return encryptionResult;
    }

    /**
     * 获取结果数据
     *
     * @param privateKey 私匙
     * @param encryptionResult 密文
     * @return
     * @throws Exception
     */
    private JSONArray getDataList(String privateKey, String encryptionResult) throws Exception {
        JSONArray jsonArray;
        String result = decryptByPrivateKey(privateKey, encryptionResult);
        if (StrUtil.isBlank(result)) {
            return null;
        }
        JSONObject resultObject = (JSONObject) JSONUtil.parse(result);
        String code = resultObject.getStr("Code", "");
        jsonArray = resultObject.getJSONArray("Table");
        if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
            log.info("查询第三方接口数据为空, 国控山东, result:{}", result);
            return null;
        }
        return jsonArray;
    }


    /**
     * 分段解密
     *
     * @param privateKeyStr 私匙
     * @param ciphertext 密文
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKeyStr, String ciphertext) throws Exception {
        // 密文base64解码
        byte[] encryptedData = Base64.decode(ciphertext);
        // 私匙base64解码
        byte[] privateKeyData = Base64.decode(privateKeyStr);
        // 解密后明文
        byte[] decryptedData = new byte[0];
        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(2, privateKey);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
            out.close();
        } catch (Exception e) {
            log.error("国控山东, 返回密文解析失败,exception:{}", e);
            e.printStackTrace();
        }
        return new String(decryptedData);
    }


//        /**
//         * 指定私钥存放文件
//         */
//        private static String PRIVATE_KEY_FILE = "E:/PrivateKey.txt";
//
        public static void main(String[] args) {
            JSONArray jsonArrayResult = new JSONArray();
            JSONArray jsonArray = null;
            int current = 1;
            int size = 500;
            do {
                try {
                    Map<String, Object> params = new LinkedMap<>();
//                    params.put("accesstoken", "86751126842BDE8025FF440205958A4C");
                    params.put("accesstoken", "3D3AF146CF2151B0D61CCE692A16AA14");
                    params.put("account", "shijiazhuangyilingyaoye");
                    params.put("password", "SD#22@590E6S");
//                    params.put("accesstoken", "86751126842BDE8025FF440205958A4C");
//                    params.put("account", "shijiazhuangyilingyaoye");
//                    params.put("password", "SD#22@590E6S");
                    // 采购、销售时间
                    params.put("date1", "2023-02-01");
                    params.put("date2", "2023-02-28");
                    params.put("curpage", current+"");
                    params.put("pagesize", size+"");
                    params.put("isfilter", 0);
                    String jsonInput = JSONUtil.toJsonStr(params);
                    Map<String, Object> inputParams = new HashMap<>();
                    inputParams.put("input", jsonInput);

                    String methodName = "gksd_webddi_purbill";
                    String responseName = "gksd_webddi_purbillResponse";
                    String resultName = "gksd_webddi_purbillResult";


                    SoapClient client = SoapClient.create("http://222.173.24.186:9090/WebService/WebService.asmx", SoapProtocol.SOAP_1_2, "http://tempuri.org/")
                            // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                            .setMethod(new QName("http://tempuri.org/", methodName, "")).setParams(inputParams, false);
                    String message = client.getMsgStr(true);
                    String str = client.send();
                    //            String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" + "    <soap:Body>\n" + "        <gksd_webddi_purbillResponse xmlns=\"http://tempuri.org/\">\n" + "            <gksd_webddi_purbillResult>GaAqV+EjGC+trEmGCNq0X7GJODyIOWMjg0WyZr2y2/hl1UAB3b4O/VL7oYkEhd9cu+ctzD760CLmlRi1tmicRpXa4UnRrQqlyvKe34BHmiw9rojO842egQsecFm2wV4ucXm23hD1tHTiFi2psRwTjY+OPcD/9FQWIkD1qeKMvu0=</gksd_webddi_purbillResult>\n" + "        </gksd_webddi_purbillResponse>\n" + "    </soap:Body>\n" + "</soap:Envelope>";
                    System.out.println(">>>>> client.send result is " + str);
                    JSONObject jsonObject = JSONUtil.xmlToJson(str);
                    if (jsonObject != null) {
                        String encryptionResult = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(responseName).getStr(resultName, "");
                        System.out.println(">>>>> encryptionResult:" + encryptionResult);
                        String privateKeyStr = getKey("E:\\PrivateKey.txt");
                        String result = decryptByPrivateKey(privateKeyStr, encryptionResult);
                        System.out.println(">>>>> result:" + result);
                        if (StrUtil.isBlank(result)) {
                            System.out.println(">>>>> result is null");
                            break;
                        }
                        JSONObject resultObject = (JSONObject) JSONUtil.parse(result);
                        String code = resultObject.getStr("Code", "");
                        jsonArray = resultObject.getJSONArray("Table");
                        if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
                            System.out.println(">>>>> jsonArray is null");
                            break;
                        }

                        jsonArrayResult.addAll(jsonArray);
                        current = current + 1;
                    }
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

            System.out.println(">>>>> jsonArrayResult:" + JSONUtil.toJsonStr(jsonArrayResult));

            for (Object o: jsonArrayResult) {
                JSONObject jo = (JSONObject)o;
                for (String key : jo.keySet()) {
                    String str = jo.get(key).toString();
                    if (str.contains("国药控股烟台有限公司")) {
                        System.out.println("********** 有子公司数据 国药控股烟台有限公司 :" + JSONUtil.toJsonStr(jo));
                    }
                }
            }
        }


        private static String getKey(String fileName){
            String privateKey = "";
            try{
                FileReader fileReader = new FileReader("E:\\PrivateKey.txt");
                privateKey =fileReader.readString();
                System.out.println(">>>>> PrivateKey:" + privateKey);
            } catch (Exception e){
                log.error("国控山东, 获取私匙失败,exception:{}", e);
                e.printStackTrace();
            }
            return privateKey;
        }

}
