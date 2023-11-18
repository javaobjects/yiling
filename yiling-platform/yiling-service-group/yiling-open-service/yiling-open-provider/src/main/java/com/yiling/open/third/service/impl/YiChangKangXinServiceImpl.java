package com.yiling.open.third.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;
import com.yiling.open.util.StringUtil;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 宜昌市康鑫医药经销有限公司，接口查询
 *
 * @author: houjie.sun
 * @date: 2023/3/28
 */
@Service("yiChangKangXinService")
@Slf4j
public class YiChangKangXinServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String ENT_CODE = "entCode";
    private static final String SECRET_KEY = "secretKey";
    private static final String SERVER_URL = "serverUrl";
    private static final String PURCHASE_METHOD = "purchaseMethod";
    private static final String SALE_METHOD = "saleMethod";
    private static final String GOODS_BATCH_METHOD = "goodsBatchMethod";
    private static final String FLOW_DAY = "flowDay";

    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_MSG = "msg";


    /**
     * 获取采购流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String entCode = param.get(ENT_CODE);
        String secretKey = param.get(SECRET_KEY);
        String serverUrl = param.get(SERVER_URL);
        String purchaseMethod = param.get(PURCHASE_METHOD);
        String startDate = param.get(FlowAbstractTemplate.startTime);
        String endDate = param.get(FlowAbstractTemplate.endTime);

        JSONArray returnJsonArray = new JSONArray();
        buildData(returnJsonArray, serverUrl, purchaseMethod, entCode, secretKey, startDate, endDate);
        return JSONUtil.toJsonStr(returnJsonArray);
    }


    /**
     * 获取销售流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String entCode = param.get(ENT_CODE);
        String secretKey = param.get(SECRET_KEY);
        String serverUrl = param.get(SERVER_URL);
        String saleMethod = param.get(SALE_METHOD);
        String startDate = param.get(FlowAbstractTemplate.startTime);
        String endDate = param.get(FlowAbstractTemplate.endTime);

        JSONArray returnJsonArray = new JSONArray();
        buildData(returnJsonArray, serverUrl, saleMethod, entCode, secretKey, startDate, endDate);
        return JSONUtil.toJsonStr(returnJsonArray);
    }

    /**
     * 获取库存流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String entCode = param.get(ENT_CODE);
        String secretKey = param.get(SECRET_KEY);
        String serverUrl = param.get(SERVER_URL);
        String goodsBatchMethod = param.get(GOODS_BATCH_METHOD);
        // 库存取数的时间，当前时间向前推3天
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, Math.negateExact(3));
        String dateTimeStr = DateUtil.format(dateTime, "yyyy-MM-dd");

        JSONArray returnJsonArray = new JSONArray();
        buildData(returnJsonArray, serverUrl, goodsBatchMethod, entCode, secretKey, dateTimeStr, dateTimeStr);
        return JSONUtil.toJsonStr(returnJsonArray);
    }

    /**
     * 查询并组装数据
     *
     * @param returnJsonArray 结果列表
     * @param serverUrl 接口地址
     * @param method 接口名称
     * @param entCode 账号
     * @param secretKey 秘钥
     * @param flowDay 查询天数
     */
    private void buildData(JSONArray returnJsonArray, String serverUrl, String method, String entCode, String secretKey, String startDate, String endDate) {
        if (StrUtil.isBlank(serverUrl) || StrUtil.isBlank(method) || StrUtil.isBlank(entCode) || StrUtil.isBlank(secretKey) || StrUtil.isBlank(startDate) || StrUtil.isBlank(endDate)) {
            log.error("宜昌市康鑫医药经销有限公司, 配置参数不能为空, serverUrl:{}, method:{}, entCode:{}, secretKey:{}, startDate:{}, endDate:{}", serverUrl, method, entCode, secretKey, startDate, endDate);
        }

        // 每页数量
        int limit = 200;
        // 页码
        int page = 1;

        // 日期列表
        List<String> dateStrList = new ArrayList<>();
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.parse(startDate), DateUtil.parse(endDate), DateField.DAY_OF_MONTH);
        dateTimes.forEach(o -> {
            dateStrList.add(DateUtil.format(o, "yyyy-MM-dd"));
        });

        // 按照查询日期列表循环，每天的查询分页调用
        for (String queryDate : dateStrList) {
            // 分页查询
            JSONArray dataJsonArray;
            do {
                long timeStamp = System.currentTimeMillis();
                dataJsonArray = doRequest(serverUrl, method, entCode, secretKey, queryDate, page, limit, timeStamp);
                if (ObjectUtil.isNull(dataJsonArray) || dataJsonArray.size() == 0) {
                    break;
                }

                returnJsonArray.addAll(dataJsonArray);
                if (dataJsonArray.size() < limit) {
                    break;
                }
                page++;
            } while (ObjectUtil.isNotNull(dataJsonArray) && dataJsonArray.size() > 0);
        }
    }

    /**
     * 发起查询请求
     *
     * @param serverUrl 接口地址
     * @param method 接口名称
     * @param entCode 账号
     * @param secretKey 秘钥
     * @param queryDate 查询日期
     * @param page 页码
     * @param limit 每页数量
     * @param timeStamp 当前时间戳
     * @return
     */
    private JSONArray doRequest(String serverUrl, String method, String entCode, String secretKey, String queryDate, int page, int limit, long timeStamp) {
        // 生成签名
        String sign = sign(entCode, secretKey, queryDate, page, limit, timeStamp);

        // http://61.136.144.247:21120/data/api/wholesale/sales?entCode=B076&timeStamp=1679992080000&sign=23e16843e3c11ed0c985d5456e8f4b35&queryDate=2023-03-02&page=1&limit=1
        String url = serverUrl.concat(method).concat("?entCode=" + entCode).concat("&timeStamp=" + timeStamp).concat("&sign=" + sign).concat("&queryDate=" + queryDate).concat("&page=" + page).concat("&limit=" + limit);

        try {
            String returnJson = HttpRequest.get(url).execute().body();
            JSONObject returnJsonObject = JSON.parseObject(returnJson);
            if (ObjectUtil.isNull(returnJsonObject)) {
                log.warn(">>>>> 宜昌市康鑫医药经销有限公司, 接口响应结果为空, response:{}, url:{}", returnJson, url);
            }

            String code = returnJsonObject.getString(RESPONSE_CODE);
            String msg = returnJsonObject.getString(RESPONSE_MSG);
            if (StrUtil.isNotBlank(code)) {
                if (ObjectUtil.equal("0000", code)) {
                    // 成功
                    JSONArray data = returnJsonObject.getJSONArray("data");
                    if (ObjectUtil.isNull(data) || data.size() == 0) {
                        if (page == 1) {
                            log.warn(">>>>> 宜昌市康鑫医药经销有限公司, 接口响应结果data为空, code:{}, msg:{}, url:{}", code, msg, url);
                        }
                        return new JSONArray();
                    }
                    data.forEach(o -> {
                        JSONObject jsonObject = (JSONObject) o;
                        StringUtil.strTrim(jsonObject);
                    });
                    return data;
                } else {
                    // 失败
                    log.warn(">>>>> 宜昌市康鑫医药经销有限公司, 接口查询失败, code:{}, msg:{}, url:{}", code, msg, url);
                }
            }
        } catch (Exception e) {
            log.error(">>>>> 宜昌市康鑫医药经销有限公司, 接口查询异常, Exception:", e);
        }
        return new JSONArray();
    }

    /**
     * 生成签名
     *
     * @param entCode 账号
     * @param secretKey 秘钥
     * @param queryDate 查询日期
     * @param page 页码
     * @param limit 每页数量
     * @param timeStamp 当前时间戳
     * @return
     */
    private String sign(String entCode, String secretKey, String queryDate, int page, int limit, long timeStamp) {
        if (StrUtil.isBlank(entCode) || StrUtil.isBlank(secretKey) || StrUtil.isBlank(queryDate) || page <= 0 || limit <= 0 || timeStamp <= 0) {
            log.error("宜昌市康鑫医药经销有限公司, 签名参数不能为空, entCode:{}, secretKey:{}, queryDate:{}, page:{}, limit:{}, timeStamp:{}", entCode, secretKey, queryDate, page, limit, timeStamp);
        }
        String beforeSign = ("entCode=" + entCode).concat("&limit=" + limit).concat("&page=" + page).concat("&queryDate=" + queryDate).concat("&timeStamp=" + timeStamp + secretKey);
        String afterSign = "";
        try {
            afterSign = DigestUtils.md5Hex(beforeSign.getBytes(("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.warn("宜昌市康鑫医药经销有限公司, 加签失败, beforeSign:{}", beforeSign);
            e.printStackTrace();
        }
        //        log.warn("宜昌市康鑫医药经销有限公司, 加签之前, beforeSign:{}", beforeSign);
        //        log.warn("宜昌市康鑫医药经销有限公司, 加签之后, afterSign:{}", afterSign);
        return afterSign;
    }


//    public static void main(String[] args) {
//        String entCode = "B076";
//        String secretKey = "EYv2pLHiDyVzYGunJ4jxyhZX4K1qhKsR";
//        String serverUrl = "http://61.136.144.247:21120/data/api/wholesale/";
//        String purchaseMethod = "purchase";
//        String saleMethod = "sales";
//        String goodsBatchMethod = "stock";
//        int flowDay = Integer.parseInt("34");
//        JSONArray returnJsonArray = new JSONArray();
//        buildData2 (returnJsonArray, serverUrl, saleMethod, entCode, secretKey, flowDay);
//        String jsonStr = JSONUtil.toJsonStr(returnJsonArray);
//        System.out.println("宜昌市康鑫医药经销有限公司, 查询结果：" + jsonStr);
//    }

    /**
     * 查询并组装数据
     *
     * @param returnJsonArray 结果列表
     * @param serverUrl 接口地址
     * @param method 接口名称
     * @param entCode 账号
     * @param secretKey 秘钥
     * @param flowDay 查询天数
     */
//    private static void buildData2(JSONArray returnJsonArray, String serverUrl, String method, String entCode, String secretKey, int flowDay) {
//        if (StrUtil.isBlank(serverUrl) || StrUtil.isBlank(method) || StrUtil.isBlank(entCode) || StrUtil.isBlank(secretKey) || flowDay < 1) {
//            log.error("宜昌市康鑫医药经销有限公司, 配置参数不能为空, serverUrl:{}, method:{}, entCode:{}, secretKey:{}, flowDay:{}", serverUrl, method, entCode, secretKey, flowDay);
//        }
//
//        // 每页数量
//        int limit = 200;
//        // 页码
//        int page = 1;
//
//        // 日期列表
//        Date endDate = new Date();
//        List<String> dateStrList = StringUtil.dateList(flowDay, endDate);
//
//        // 按照查询日期列表循环，每天的查询分页调用
//        for (String queryDate : dateStrList) {
//            // 分页查询
//            JSONArray dataJsonArray;
//            do {
//                long timeStamp = System.currentTimeMillis();
//                dataJsonArray = doRequest2(serverUrl, method, entCode, secretKey, queryDate, page, limit, timeStamp);
//                if (ObjectUtil.isNull(dataJsonArray) || dataJsonArray.size() == 0) {
//                    break;
//                }
//                returnJsonArray.addAll(dataJsonArray);
//                page++;
//            } while (ObjectUtil.isNotNull(dataJsonArray) && dataJsonArray.size() > 0);
//        }
//    }

    /**
     * 发起查询请求
     *
     * @param serverUrl 接口地址
     * @param method 接口名称
     * @param entCode 账号
     * @param secretKey 秘钥
     * @param queryDate 查询日期
     * @param page 页码
     * @param limit 每页数量
     * @param timeStamp 当前时间戳
     * @return
     */
//    private static JSONArray doRequest2(String serverUrl, String method, String entCode, String secretKey, String queryDate, int page, int limit, long timeStamp) {
//        // 生成签名
//        String sign = sign2(entCode, secretKey, queryDate, page, limit, timeStamp);
//
//        // http://61.136.144.247:21120/data/api/wholesale/sales?entCode=B076&timeStamp=1679992080000&sign=23e16843e3c11ed0c985d5456e8f4b35&queryDate=2023-03-02&page=1&limit=1
//        String url = serverUrl.concat(method).concat("?entCode=" + entCode).concat("&timeStamp=" + timeStamp).concat("&sign=" + sign).concat("&queryDate=" + queryDate).concat("&page=" + page).concat("&limit=" + limit);
//
//        try {
//            String returnJson = HttpRequest.get(url).execute().body();
//            JSONObject returnJsonObject = JSON.parseObject(returnJson);
//            if (ObjectUtil.isNull(returnJsonObject)) {
//                log.warn(">>>>> 宜昌市康鑫医药经销有限公司, 接口响应结果为空, response:{}, url:{}", returnJson, url);
//            }
//
//            String code = returnJsonObject.getString(RESPONSE_CODE);
//            String msg = returnJsonObject.getString(RESPONSE_MSG);
//            if (StrUtil.isNotBlank(code)) {
//                if (ObjectUtil.equal("0000", code)) {
//                    // 成功
//                    JSONArray data = returnJsonObject.getJSONArray("data");
//                    if (ObjectUtil.isNull(data) || data.size() == 0) {
//                        if (page == 1) {
//                            log.warn(">>>>> 宜昌市康鑫医药经销有限公司, 接口响应结果data为空, code:{}, msg:{}, url:{}", code, msg, url);
//                        }
//                        return new JSONArray();
//                    }
//                    data.forEach(o -> {
//                        JSONObject jsonObject = (JSONObject) o;
//                        StringUtil.strTrim(jsonObject);
//                    });
//                    return data;
//                } else {
//                    // 失败
//                    log.warn(">>>>> 宜昌市康鑫医药经销有限公司, 接口查询失败, code:{}, msg:{}, url:{}", code, msg, url);
//                }
//            }
//        } catch (Exception e) {
//            log.error(">>>>> 宜昌市康鑫医药经销有限公司, 接口查询异常, Exception:", e);
//        }
//        return new JSONArray();
//    }

    /**
     * 生成签名
     *
     * @param entCode 账号
     * @param secretKey 秘钥
     * @param queryDate 查询日期
     * @param page 页码
     * @param limit 每页数量
     * @param timeStamp 当前时间戳
     * @return
     */
//    private static String sign2(String entCode, String secretKey, String queryDate, int page, int limit, long timeStamp) {
//        if (StrUtil.isBlank(entCode) || StrUtil.isBlank(secretKey) || StrUtil.isBlank(queryDate) || page <= 0 || limit <= 0 || timeStamp <= 0) {
//            log.error("宜昌市康鑫医药经销有限公司, 签名参数不能为空, entCode:{}, secretKey:{}, queryDate:{}, page:{}, limit:{}, timeStamp:{}", entCode, secretKey, queryDate, page, limit, timeStamp);
//        }
//        String beforeSign = ("entCode=" + entCode).concat("&limit=" + limit).concat("&page=" + page).concat("&queryDate=" + queryDate).concat("&timeStamp=" + timeStamp + secretKey);
//        String afterSign = "";
//        try {
//            afterSign = DigestUtils.md5Hex(beforeSign.getBytes(("UTF-8")));
//        } catch (UnsupportedEncodingException e) {
//            log.warn("宜昌市康鑫医药经销有限公司, 加签失败, beforeSign:{}", beforeSign);
//            e.printStackTrace();
//        }
//        return afterSign;
//    }

}
