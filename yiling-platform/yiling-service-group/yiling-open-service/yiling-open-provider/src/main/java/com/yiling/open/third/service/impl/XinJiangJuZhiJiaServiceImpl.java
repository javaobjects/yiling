package com.yiling.open.third.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 新疆聚之家医药有限公司，对接流向接口
 *
 * @author: houjie.sun
 * @date: 2023/6/15
 */
@Slf4j
@Service("xinJiangJuZhiJiaService")
public class XinJiangJuZhiJiaServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String APPID = "appId";
    private static final String FLOW_CARD_ID = "flowCardId";
    private static final String SECRET_KEY = "secretKey";
    private static final String URL = "url";
    private static final String PURCHASE_METHED = "purchaseMethod";
    private static final String SALE_METHED = "saleMethod";
    private static final String GOODS_BATCH_METHED = "goodsBatchMethod";


    /**
     * 获取采购流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String appId = param.get(APPID);
        String flowCardId = param.get(FLOW_CARD_ID);
        String secretKey = param.get(SECRET_KEY);
        String url = param.get(URL);
        String purchaseMethod = param.get(PURCHASE_METHED);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd HH:mm:ss");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd HH:mm:ss");

        // 分页获取数据
        JSONArray dataJsonArray = listPage(appId, flowCardId, secretKey, url, purchaseMethod, startDate, endDate);
        if (ObjectUtil.isNotNull(dataJsonArray) && dataJsonArray.size() > 0) {
            return JSONUtil.toJsonStr(dataJsonArray);
        }
        return null;
    }

    /**
     * 获取销售流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String appId = param.get(APPID);
        String flowCardId = param.get(FLOW_CARD_ID);
        String secretKey = param.get(SECRET_KEY);
        String url = param.get(URL);
        String saleMethod = param.get(SALE_METHED);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd HH:mm:ss");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd HH:mm:ss");

        // 分页获取数据
        JSONArray dataJsonArray = listPage(appId, flowCardId, secretKey, url, saleMethod, startDate, endDate);
        if (ObjectUtil.isNotNull(dataJsonArray) && dataJsonArray.size() > 0) {
            return JSONUtil.toJsonStr(dataJsonArray);
        }
        return null;
    }

    /**
     * 获取库存流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String appId = param.get(APPID);
        String flowCardId = param.get(FLOW_CARD_ID);
        String secretKey = param.get(SECRET_KEY);
        String url = param.get(URL);
        String goodsBatchMethod = param.get(GOODS_BATCH_METHED);

        // 分页获取数据
        JSONArray dataJsonArray = listPage(appId, flowCardId, secretKey, url, goodsBatchMethod, null, null);
        if (ObjectUtil.isNotNull(dataJsonArray) && dataJsonArray.size() > 0) {
            return JSONUtil.toJsonStr(dataJsonArray);
        }
        return null;
    }


    private JSONArray listPage(String appId, String flowCardId, String secretKey, String url, String methodName, String startDate, String endDate) {
        JSONArray dataJsonArray = new JSONArray();
        JSONArray returnJsonArray;
        int page = 1;
        // 接口分页大小，最大支持500
        int pageSize = 500;

        do {
            // 当前时间，10位时间戳
            long timestamp = System.currentTimeMillis() / 1000;

            Map<String, Object> map = new HashMap<>();
            map.put("timestamp", timestamp);
            map.put("appId", appId);
            map.put("page", page);
            map.put("pageSize", pageSize);
            map.put("flowCardId", flowCardId);
            if (StrUtil.isNotBlank(startDate)) {
                map.put("storageStartTime", startDate);
            }
            if (StrUtil.isNotBlank(endDate)) {
                map.put("storageEndTime", endDate);
            }

            // 签名
            String sign = sign(map, secretKey);
            //            System.out.println(">>>>> sign:" + sign);

            // 请求参数设置签名
            map.put("sign", sign);
            String requestBody = JSONUtil.toJsonStr(map);
            //            System.out.println(">>>>> requestBody:" + requestBody);

            // post请求
            returnJsonArray = doRequestFlowData(url, methodName, requestBody);
            if (ObjectUtil.isNotNull(returnJsonArray) && returnJsonArray.size() > 0) {
                dataJsonArray.addAll(returnJsonArray);
            }

            page++;
        } while (ObjectUtil.isNotNull(returnJsonArray) && returnJsonArray.size() > 0);
        return dataJsonArray;
    }


    /**
     * 查询
     *
     * @param returnJsonArray 结果数据列表
     * @param url 接口地址
     */
    private JSONArray doRequestFlowData(String url, String methodName, String json) {
        String returnJson = HttpRequest.post(url + methodName).header(Header.CONTENT_TYPE, "application/json").timeout(1000*60*5).body(json).execute().body();
//        String returnJson = "{\"data\":{\"list\":[],\"total\":0},\"msg\":\"success\",\"code\":0}";
        System.out.println(">>>>> returnJson:" + returnJson);
        if (StrUtil.isBlank(returnJson)) {
            log.warn(">>>>> 新疆聚之家, 查询流向接口异常, response is blank");
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(returnJson);
        if (jsonObject == null) {
            log.warn(">>>>> 新疆聚之家, 查询流向接口异常, jsonObject is null");
            return new JSONArray();
        }
        Integer code = jsonObject.getInteger("code");
        if (ObjectUtil.isNull(code)) {
            log.warn(">>>>> 新疆聚之家, 查询流向接口异常, response code is null");
            return null;
        }
        if(code.intValue() != 0){
            String msg = (String)jsonObject.getOrDefault("msg", "");
            log.warn(">>>>> 新疆聚之家, 查询流向接口异常, code:{}, msg:{}", code, msg);
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        if (ObjectUtil.isNull(data)) {
            log.warn(">>>>> 新疆聚之家, 查询流向接口异常, response data is null");
            return null;
        }
        JSONArray jsonArray = data.getJSONArray("list");
        if (jsonArray == null || jsonArray.size() == 0) {
            log.warn(">>>>> 新疆聚之家, 查询流向接口异常, response data.list is empty");
            return null;
        }
        return new JSONArray();
    }



    public static String sign(Map<String, Object> params, String secretKey) {
        // 参数排序，key按ASCII码从小到大排序（字典序）
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 把所有参数名和参数值串在一起
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            sb.append(key).append("=").append(value+"").append("&");
        }

        // 在最后拼接secretKey
        String sbStr = sb.toString();
        String beforeSign = sbStr.concat("secretKey").concat("=").concat(secretKey);
        System.out.println(">>>>> beforeSign:" + beforeSign);

        // MD5加密，32位、小写
        String md532Lower = DigestUtils.md5DigestAsHex(beforeSign.getBytes());
        return md532Lower;
    }

/*
    public static void main(String[] args) {
        String url = "https://xj.api.prod.tyaow.com/open/api/";
//        String method = "purchase";
//        String method = "sales";
        String method = "repertory";

        // 当前时间，取10位时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        String appId = "100009";
        String secretKey = "86252cf3ac4c56d649b1d522a0cd9f27";
        int flowCardId = 234;

        int page = 1;
        int pageSize = 10;
        String storageStartTime = "2023-05-01 00:00:00";
        String storageEndTime = "2023-05-30 23:59:59";

        // 组装参数
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", timestamp);
        map.put("appId", appId);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("flowCardId", flowCardId);
        map.put("storageStartTime", storageStartTime);
        map.put("storageEndTime", storageEndTime);

        // 签名
        String sign = sign(map, secretKey);
        System.out.println(">>>>> sign:" + sign);

        // 设置签名
        map.put("sign", sign);
        String requestBody = JSONUtil.toJsonStr(map);
        System.out.println(">>>>> requestBody:" + requestBody);

        // post请求
        String returnJson = HttpRequest.post(url+method).header(Header.CONTENT_TYPE, "application/json").timeout(1000*60*5).body(requestBody).execute().body();
//        String returnJson = "{\"data\":{\"list\":[],\"total\":0},\"msg\":\"success\",\"code\":0}";
        System.out.println(">>>>> returnJson:" + returnJson);


//        {
//            "timestamp": 1686928904,
//            "appId": "100009",
//            "sign": "c542b35a75921cdb042b8e0c72ee5bd3",
//            "page": 1,
//            "pageSize": 10,
//            "flowCardId": 234, //流向卡ID
//            "orderNumber": "", //单据编号
//            "supplierCode": "", //单位内码
//            "supplierName": "", //单位名称
//            "goodsCode": "", //商品内码
//            "goodsName": "", //商品名称
//            "batchNumber": "", //批号
//            "validStartTime": "", //效期开始时间
//            "validEndTime": "", //效期结束时间
//            "storageEndTime": "2023-05-30 23:59:59", //入库开始时间
//            "storageStartTime": "2023-05-01 00:00:00" //入库结束时间
//        }

    }
*/





}
