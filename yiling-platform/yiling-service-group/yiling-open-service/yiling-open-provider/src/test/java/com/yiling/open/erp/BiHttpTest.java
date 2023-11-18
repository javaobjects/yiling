package com.yiling.open.erp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.yiling.open.BaseTest;
import org.junit.Test;

/**
 * @author: shuang.zhang
 * @date: 2022/10/18
 */
public class BiHttpTest extends BaseTest {

    @Test
    public void Test1() {

    }

    public  void aa(){
        Map<String,String> hashMap=new HashMap<>();
        Long time=System.currentTimeMillis();
        System.out.println(time);
        hashMap.put("method", "50000001");
        hashMap.put("app_key","cPdBWzOP6L1YkHkJ1Ff8sk3WRBcfFCWh");
        hashMap.put("timestamp",time+"");
        hashMap.put("v","1.0");
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
            String sign = signRequestNew(headParams, secret, false);
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

    public static String signRequestNew(Map<String, String> params, String secret, boolean isHmac)
            throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (!isHmac) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (areNotEmpty(key, value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (isHmac) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * HmacMD5加密
     * @param data
     * @param secret
     * @return
     * @throws IOException
     */
    private static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"),
                    "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            String msg = getStringFromException(gse);
            throw new IOException(msg);
        }
        return bytes;
    }

    /**
     * MD5加密
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] encryptMD5(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            String msg = getStringFromException(gse);
            throw new IOException(msg);
        }
        return bytes;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private static String getStringFromException(Throwable e) {
        String result = "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            PrintStream ps = new PrintStream(bos, false, "UTF-8");
            e.printStackTrace(ps);
            result = bos.toString("UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

}
