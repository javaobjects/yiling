package com.yiling.open.webservice;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;

public class OpenApiIndexTest {

    public static void main(String[] args)  {
        Map<String,String> hashMap=new HashMap<>();
        Long time=System.currentTimeMillis();
        System.out.println(time);
        hashMap.put("method", ErpTopicName.ErpShopSaleFlowData.getMethod());
        hashMap.put("app_key","iWB9mW4NbqpnYZYWsaKlBXxu1EsZvqO8");
        hashMap.put("timestamp",time+"");
        hashMap.put("v","1.0");
        String json="[{\"soUnit\":\"瓶\",\"soId\":\"\",\"soSpecifications\":\"50ml/瓶\",\"soProductTime\":\"2022-11-30 11:37:03\",\"suDeptNo\":\"\",\"soNo\":\"2023032101\",\"soTime\":\"2023-03-21 11:35:21\",\"cnt\":1,\"shopName\":\"山东康诺盛世医药有限公连锁门店2\",\"soQuantity\":2,\"controlId\":0,\"goodsInSn\":\"02.20.004.0012\",\"soBatchNo\":\"2022123001-a\",\"dataMd5\":\"B13DDEA765EC682B56E5852B4B51E733\",\"failedCount\":0,\"soEffectiveTime\":\"2023-12-30 11:37:09\",\"opTime\":\"2023-03-23 17:43:01\",\"soManufacturer\":\"诺斯贝尔化妆品股份有限公司\",\"shopNo\":\"100002\",\"enterpriseName\":\"山东康诺盛世医药有限公司\",\"goodsName\":\"连花清蕊抑菌喷雾\",\"soPrice\":10.5,\"operType\":1,\"suId\":7,\"soId\":\"1000022023032101\"},{\"soUnit\":\"瓶\",\"soId\":\"\",\"soSpecifications\":\"50ml/瓶\",\"soProductTime\":\"2022-11-30 11:37:03\",\"suDeptNo\":\"\",\"soNo\":\"2023032102\",\"soTime\":\"2023-03-21 11:35:21\",\"cnt\":1,\"shopName\":\"山东康诺盛世医药有限公连锁门店2\",\"soQuantity\":2,\"controlId\":0,\"goodsInSn\":\"02.20.004.0012\",\"soBatchNo\":\"2022123001-a\",\"dataMd5\":\"2BF59A233C85F6F10B2F5DAAE0AFEF23\",\"failedCount\":0,\"soEffectiveTime\":\"2023-12-30 11:37:09\",\"opTime\":\"2023-03-23 17:43:01\",\"soManufacturer\":\"诺斯贝尔化妆品股份有限公司\",\"shopNo\":\"100002\",\"enterpriseName\":\"山东康诺盛世医药有限公司\",\"goodsName\":\"连花清蕊抑菌喷雾\",\"soPrice\":11,\"operType\":1,\"suId\":7,\"soId\":\"1000022023032102\"}]";
        String returnJson="";
        try {
            //         returnJson = getPost("http://localhost:9982/router/rest", json, hashMap, "qVtZg9FQop2ZNv9RTVOBnwxjvl1X9C1l");
            returnJson = getPost("http://39.103.129.98:9982/router/rest",json,hashMap,"qVtZg9FQop2ZNv9RTVOBnwxjvl1X9C1l");
            //            returnJson = getPost("https://open.59yi.com/router/rest",json,hashMap,"4dqwep7p95jhlnfwptik7hlodj7yosl8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(returnJson);
    }

    public static String getPost(String url, String json, Map<String, String> headParams, String secret) throws Exception {
        StringBuffer re = new StringBuffer();
        String jsonStr = "";
        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader responseBuffer = null;
        try {
            json = URLEncoder.encode(json, "utf-8");
            URL targetUrl = new URL(url);
            httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Connection", "Keep-Alive");

            for (Map.Entry entry : headParams.entrySet()) {
                httpConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            String sign = SignatureAlgorithm.signRequestNew(headParams, secret, false);
            httpConnection.setRequestProperty("sign", sign);

            httpConnection.setConnectTimeout(30000);

            httpConnection.setReadTimeout(30000);

            outputStream = httpConnection.getOutputStream();

            outputStream.write(json.getBytes());
            outputStream.flush();

            if (httpConnection.getInputStream() != null) {
                if (httpConnection.getResponseCode() != 200) {
                    throw new Exception("请求失败，失败原因码:" + httpConnection.getResponseCode());
                }

                responseBuffer = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

                String output = "";
                while ((output = responseBuffer.readLine()) != null) {
                    re.append(new String(output.getBytes(), StandardCharsets.UTF_8));
                }

                httpConnection.disconnect();
//                jsonStr = URLDecoder.decode(re.toString(), "UTF-8");
                jsonStr=re.toString();
            }
        } catch (Exception e) {
            throw new Exception("请求接口出错", e);
        } finally {
            try {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (responseBuffer != null)
                    responseBuffer.close();
            } catch (Exception e) {
                throw new Exception("关闭请求接口出错", e);
            }
        }
        return jsonStr;
    }
}
