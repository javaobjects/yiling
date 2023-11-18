package com.yiling.erp.client.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.util.ErpRunException;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;


/**
 * @author shuan
 */
public class PopRequest {

    public final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String saveDB_ignore_ssl = "saveDB_ignore_ssl";

    public Map<String, String> generateHeadMap(String method, String key) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(OpenConstants.METHOD, method);
        hashMap.put(OpenConstants.APP_KEY, key);
        hashMap.put(OpenConstants.TIMESTAMP, System.currentTimeMillis() + "");
        hashMap.put(OpenConstants.VERSION, "1.0");
        return hashMap;
    }

    public String getPost(String url, String json, Map<String, String> headParams, String secret) throws ErpRunException {
        return httpPost(url, json, headParams, secret, 1);
    }

    public String httpPost(String url, String json, Map<String, String> headParams, String secret, Integer i) {
        StringBuffer re = new StringBuffer();
        String jsonStr = "";
        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader responseBuffer = null;
        Long startTime = System.currentTimeMillis();
        try {
            log.debug("请求的路径:" + url + ", 请求的参数:" + json);

            json = URLEncoder.encode(json, "utf-8");

            URL targetUrl = new URL(url);

            // https请求忽略ssl证书，该部分必须在获取connection前调用
            ignoreSSL ();

            httpConnection = (HttpURLConnection) targetUrl.openConnection();

            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);

            httpConnection.setRequestMethod("POST");

            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Connection", "Keep-Alive");

            for (Entry entry : headParams.entrySet()) {
                httpConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            String sign = SignatureAlgorithm.signRequestNew(headParams, secret, false);
            httpConnection.setRequestProperty("sign", sign);

            httpConnection.setConnectTimeout(1000 * 60 * 5);

            httpConnection.setReadTimeout(1000 * 60 * 5);

            outputStream = httpConnection.getOutputStream();

            outputStream.write(json.getBytes());
            outputStream.flush();

            if (httpConnection.getInputStream() != null) {
                if (httpConnection.getResponseCode() != 200) {
                    throw new com.yiling.open.erp.util.ErpRunException("请求失败，失败原因码:" + httpConnection.getResponseCode());
                }

                responseBuffer = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "utf-8"));

                String output = "";
                while ((output = responseBuffer.readLine()) != null) {
                    re.append(output);
                }

                httpConnection.disconnect();
                jsonStr = re.toString();
                log.debug("返回的结果：" + jsonStr);
            }
        } catch (ConnectException e) {
            if (i <= 3) {
                log.info("请求接口超时重试次数{},耗时:{}", i, (System.currentTimeMillis() - startTime));
                i++;
                httpPost(url, json, headParams, secret, i);
            } else {
                throw new ErpRunException("请求接口超时,耗时:" + (System.currentTimeMillis() - startTime), e);
            }
        } catch (Exception e) {
            throw new ErpRunException("请求接口出错,耗时:" + (System.currentTimeMillis() - startTime), e);
        } finally {
            try {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (responseBuffer != null) {
                    responseBuffer.close();
                }
            } catch (Exception e) {
                throw new ErpRunException("关闭请求接口出错", e);
            }
        }
        return jsonStr;
    }

    //添加心跳信息
    public String addSysHeartLog(String processId, String runPath, String runtaskIds, String versions, String mac, String ip) {
        try {
            SysHeartBeatDTO sysHeartBeat = new SysHeartBeatDTO();
            sysHeartBeat.setProcessId(processId);
            sysHeartBeat.setRunPath(runPath);
            sysHeartBeat.setRuntaskIds(runtaskIds);
            sysHeartBeat.setVersions(versions);
            sysHeartBeat.setMac(mac);
            sysHeartBeat.setIp(ip);
            return JSON.toJSONString(sysHeartBeat);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


    /**
     * @param urlPath 下载路径
     * @return 返回下载文件
     */
    public static InputStream downloadFile(String urlPath) {
        HttpURLConnection httpURLConnection = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setDoOutput(true);// 使用 URL 连接进行输出
            httpURLConnection.setDoInput(true);// 使用 URL 连接进行输入
            httpURLConnection.setUseCaches(false);// 忽略缓存
            httpURLConnection.setRequestMethod("GET");// 设置URL请求方法
            //可设置请求头
            httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            if (httpURLConnection.getInputStream() != null) {
                return httpURLConnection.getInputStream();
            }
            return null;
        } catch (MalformedURLException e) {
            throw new com.yiling.open.erp.util.ErpRunException("关闭请求接口出错", e);
        } catch (IOException e) {
            throw new com.yiling.open.erp.util.ErpRunException("关闭请求接口出错", e);
        }
    }

    private void ignoreSSL () throws Exception{
//        Integer saveDBIgnoreSSL = CacheTaskUtil.getInstance().getCacheData(saveDB_ignore_ssl);
//        if (ObjectUtil.isNotNull(saveDBIgnoreSSL) && 1 == saveDBIgnoreSSL.intValue()) {
//            trustAllHttpsCertificates();
//            HttpsURLConnection.setDefaultHostnameVerifier(getDONotVerify());
//            CacheTaskUtil.getInstance().removeCacheData(saveDB_ignore_ssl);
//        }
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(getDONotVerify());
        CacheTaskUtil.getInstance().removeCacheData(saveDB_ignore_ssl);
    }

    /**
     * 设置不验证主机
     */
    private HostnameVerifier getDONotVerify() {
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        return hv;
    }

    /**
     * 信任所有
     *
     * @throws Exception
     */
    private void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = getTrustAllCerts();
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        SSLSocketFactory newFactory = sc.getSocketFactory();
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(newFactory);
    }

    /**
     * 替换默认的验证
     *
     * @return
     */
    private TrustManager[] getTrustAllCerts(){
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new TrustTrustManagerCerts();
        trustAllCerts[0] = tm;
        return trustAllCerts;
    }

    /**
     * 覆盖java默认的证书验证
     */
    private class TrustTrustManagerCerts implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

}