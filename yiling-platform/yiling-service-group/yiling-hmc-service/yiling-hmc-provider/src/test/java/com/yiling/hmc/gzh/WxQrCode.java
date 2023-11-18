package com.yiling.hmc.gzh;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.util.WxUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;


public class WxQrCode {
    private static final String GETWXACODEUNLIMIT_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";// 生成小程序码地址

    public static final String PATH_HOME = "";

    /**
     * 生成小程序码返回base64字符串
     *
     * @param sceneStr    要携带的参数
     * @param accessToken
     * @param page        要跳转的小程序页面（必须是已发布的）
     * @return
     */
    public static String getminiqrQr(String sceneStr, String accessToken, String page) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(String.format(GETWXACODEUNLIMIT_URL, accessToken));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", sceneStr);
            paramJson.put("page", page);
            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据

            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            try (InputStream is = httpURLConnection.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return Base64.getEncoder().encodeToString(baos.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    /**
     * 模拟调用
     *
     * @param args
     */
    public static void main(String[] args) {
//        String accessToken = WxUtils.getAccessToken("wx8d664694a8571620", "e735e1de7db5f0addf26b247a5cfb620");
        String accessToken = "";
        String qrBase64 = getminiqrQr("1024", accessToken, PATH_HOME);
        System.out.println(qrBase64);
    }
}