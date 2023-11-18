package com.yiling.open.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 * @author: shuang.zhang
 * @date: 2022/11/28
 */
public class CmsClientTest {
    public static void main(String[] args) {
        System.out.println(requestOpen("http://localhost:9981/user/api/v1/contract/viewUrl?contractId=3037227943741367093", "888888", ""));
    }

    public static String requestOpen(String url, String secret, String body) {
        String returnJson = "失败";
        try {
            Date now = new Date();
            String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sf = new SimpleDateFormat(format);
            String bodyString = encryptHex(body, secret);

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("appKey", "dev-ih");
            hashMap.put("timestamp", sf.format(now));
            hashMap.put("queryParams", getQueryParams(url));
            hashMap.put("body", bodyString);
            hashMap.put("sign", sign(hashMap, secret, "hmac"));
            hashMap.remove("body");
            hashMap.remove("queryParams");
            returnJson = getPost(url, bodyString, hashMap);
        } catch (Exception e) {
            returnJson = e.getMessage();
            e.printStackTrace();
        }
        return returnJson;
    }

    public static String getQueryParams(String urlStr) {
        try {
            Map<String, String> map = new HashMap<>();
            URL url = new URL(urlStr);
            String requestStr = url.getQuery();
            if (isEmpty(requestStr)) {
                return "";
            }
            String[] strings = requestStr.split("&");
            for (String str : strings) {
                String[] valueStr = str.split("=");
                map.put(valueStr[0], valueStr[1]);
            }
            String[] keys = map.keySet().toArray(new String[0]);
            Arrays.sort(keys);

            StringBuilder sb = new StringBuilder();
            for (String key : keys) {
                String value = map.get(key);
                if (!isEmpty(key) && !isEmpty(value)) {
                    sb.append(key).append(value);
                }
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getPost(String url, String json, Map<String, String> headParams) throws Exception {
        StringBuffer re = new StringBuffer();
        String jsonStr = "";
        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader responseBuffer = null;
        try {
            URL targetUrl = new URL(url);
            httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
//            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Connection", "Keep-Alive");
            httpConnection.setRequestProperty("User-Agent", "Mozilla/4.76");
            for (Map.Entry entry : headParams.entrySet()) {
                httpConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
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
                jsonStr = re.toString();
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

    public static String sign(Map<String, String> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder sb = new StringBuilder();
        if ("md5".equals(signMethod)) { //签名的摘要算法，可选值为：hmac，md5，hmac-sha256
            sb.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                sb.append(key).append(value);
            }
        }
        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if ("hmac".equals(signMethod)) {
            bytes = encryptHMAC(sb.toString(), secret);
        } else {
            sb.append(secret);
            bytes = encryptMD5(sb.toString());
        }
        // 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
        return byte2hex(bytes);
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(new String(data.getBytes("UTF-8")));
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

    /**
     * 加密
     *
     * @param data
     * @param secret
     * @return
     */
    public static String encryptHex(String data, String secret) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(secret.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = data.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteResult = cipher.doFinal(byteContent);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteResult.length; i++) {
                String hex = Integer.toHexString(byteResult[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
