package com.yiling.open.third.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/8
 */
@Service("jiuZhouTongService")
@Slf4j
public class JiuZhouTongServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String purchaseUrl = param.get("purchaseUrl");
        String token = param.get("token");
        JSONArray returnJsonArray = new JSONArray();
        String startTime=DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)),"yyyy-MM-dd");
        String endTime=DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)),"yyyy-MM-dd");
        JSONArray jsonArray = null;
        Integer i=1;
        do {
            String json = "{\"filter\": {\"startDate\": \"" + startTime+ "\",\"endDate\": \"" + endTime + "\",\"productCode\":\"\"},\"pageIndex\": "+i+",\"pageSize\": 500}";
            String returnJson = HttpRequest.post(purchaseUrl).header(Header.CONTENT_TYPE, "application/json").header(Header.AUTHORIZATION, token).timeout(1000*60*5).body(json).execute().body();
            if (StrUtil.isNotEmpty(returnJson)) {
                JSONObject JsonObject = JSON.parseObject(returnJson);
                if (JsonObject != null) {
                    JSONObject dataJsonObject = JsonObject.getJSONObject("data");
                    jsonArray = dataJsonObject.getJSONArray("list");
                    if(jsonArray!=null&&jsonArray.size()>0){
                        returnJsonArray.addAll(jsonArray);
                    }
                }
            }
            i=i+1;
        } while (jsonArray != null&&jsonArray.size()==500);
        return JSONObject.toJSONString(returnJsonArray);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String purchaseUrl = param.get("saleUrl");
        String token = param.get("token");
        JSONArray returnJsonArray = new JSONArray();
        String startTime=DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)),"yyyy-MM-dd");
        String endTime=DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)),"yyyy-MM-dd");
        JSONArray jsonArray = null;
        Integer i=1;
        do {
            String json = "{\"filter\": {\"startDate\": \"" + startTime + "\",\"endDate\": \"" + endTime + "\",\"productCode\":\"\"},\"pageIndex\": "+i+",\"pageSize\": 500}";
            String returnJson = HttpRequest.post(purchaseUrl).header(Header.CONTENT_TYPE, "application/json").header(Header.AUTHORIZATION, token).timeout(1000*60*5).body(json).execute().body();
            if (StrUtil.isNotEmpty(returnJson)) {
                JSONObject JsonObject = JSON.parseObject(returnJson);
                if (JsonObject != null) {
                    JSONObject dataJsonObject = JsonObject.getJSONObject("data");
                    jsonArray = dataJsonObject.getJSONArray("list");
                    if(jsonArray!=null&&jsonArray.size()>0){
                        returnJsonArray.addAll(jsonArray);
                    }
                }
            }
            i=i+1;
        } while (jsonArray != null&&jsonArray.size()==500);
        return JSONObject.toJSONString(returnJsonArray);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String purchaseUrl = param.get("goodsBatchUrl");
        String token = param.get("token");
        JSONArray returnJsonArray = new JSONArray();
        String startTime=DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)),"yyyy-MM-dd");
        String endTime=DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)),"yyyy-MM-dd");
        JSONArray jsonArray = null;
        Integer i=1;
        do {
            String json = "{\"filter\": {\"startDate\": \"" + startTime + "\",\"endDate\": \"" + endTime + "\",\"productCode\":\"\"},\"pageIndex\": "+i+",\"pageSize\": 500}";
            String returnJson = HttpRequest.post(purchaseUrl).header(Header.CONTENT_TYPE, "application/json").header(Header.AUTHORIZATION, token).timeout(1000*60*5).body(json).execute().body();
            if (StrUtil.isNotEmpty(returnJson)) {
                JSONObject JsonObject = JSON.parseObject(returnJson);
                if (JsonObject != null) {
                    JSONObject dataJsonObject = JsonObject.getJSONObject("data");
                    jsonArray = dataJsonObject.getJSONArray("list");
                    if(jsonArray!=null&&jsonArray.size()>0){
                        returnJsonArray.addAll(jsonArray);
                    }
                }
            }
            i=i+1;
        } while (jsonArray != null&&jsonArray.size()==500);
        return JSONObject.toJSONString(returnJsonArray);
    }
}
