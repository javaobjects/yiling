package com.yiling.open.third.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 福建鑫天健医药有限公司
 *
 * @author: houjie.sun
 * @date: 2022/6/27
 */
@Slf4j
@Service("fuJianXinTianJianService")
public class FuJianXinTianJianServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String URL = "url";
    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String PURCHASE_TABLE_NAME = "purchaseTableName";
    private static final String SALE_TABLE_NAME = "saleTableName";
    private static final String GOODS_BATCH_TABLE_NAME = "goodsBatchTableName";
    private static final String QUERY_KEY = "queryKey";
    private static final String MANUFACTURER = "以岭药业";


    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String url = param.get(URL);
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String queryKey = param.get(QUERY_KEY);
        String purchaseTableName = param.get(PURCHASE_TABLE_NAME);
        //        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        //        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd"); // (" like '%以岭%'")
        String sql = "select * from ".concat(purchaseTableName).concat(" where ").concat(queryKey).concat(" like '%").concat(MANUFACTURER).concat("%';");

        JSONArray jsonArrayResult = doRequest(url, userName, passWord, sql, purchaseTableName);
        //        JSONArray jsonArrayResult = new JSONArray();
        //        JSONArray jsonArray = null;
        //        int limit = 0;
        //        int size = 500;
        //        do{
        //            int rowStart = limit + 1;
        //            int rowEnd = limit + size;
        //            String sqlStr = sql.concat(" limit ").concat(rowStart + ",").concat(rowEnd + ";");
        //            jsonArray = doRequest(url, userName, passWord, sqlStr);
        //            if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
        //                break;
        //            }
        //
        //            jsonArrayResult.addAll(jsonArray);
        //            if (jsonArray.size() < size) {
        //                break;
        //            }
        //            limit = limit + size;
        //        } while (ObjectUtil.isNotNull(jsonArray) && jsonArray.size() != 0);

        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String url = param.get(URL);
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String queryKey = param.get(QUERY_KEY);
        String saleTableName = param.get(SALE_TABLE_NAME);
        //        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        //        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        String sql = "select * from ".concat(saleTableName).concat(" where ").concat(queryKey).concat(" like '%").concat(MANUFACTURER).concat("%';");

        JSONArray jsonArrayResult = doRequest(url, userName, passWord, sql, saleTableName);
        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String url = param.get(URL);
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String queryKey = param.get(QUERY_KEY);
        String goodsBatchTableName = param.get(GOODS_BATCH_TABLE_NAME);
        //        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        //        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        String sql = "select * from ".concat(goodsBatchTableName).concat(" where ").concat(queryKey).concat(" like '%").concat(MANUFACTURER).concat("%';");

        JSONArray jsonArrayResult = doRequest(url, userName, passWord, sql, goodsBatchTableName);
        return JSONUtil.toJsonStr(jsonArrayResult);
    }

    public JSONArray doRequest(String url, String user, String pass, String sql, String tableName) {
        log.info(">>>>> 福建鑫天健医药有限公司, url:{}, user:{}, pass:{}, sql:{}", url, user, pass, sql);
        Base64.Encoder encoder = Base64.getEncoder();
        String userNameBase64 = new String(encoder.encode(user.getBytes()));
        String passWordBase64 = new String(encoder.encode(pass.getBytes()));
        String sqlstrBase64 = new String(encoder.encode(sql.getBytes()));

        String userNameUrlEncoder = null;
        String passWordUrlEncoder = null;
        String sqlstrUrlEncoder = null;
        try {
            userNameUrlEncoder = URLEncoder.encode(userNameBase64, "UTF-8");
            passWordUrlEncoder = URLEncoder.encode(passWordBase64, "UTF-8");
            sqlstrUrlEncoder = URLEncoder.encode(sqlstrBase64, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String body = "user=".concat(userNameUrlEncoder).concat("&pass=").concat(passWordUrlEncoder).concat("&sqlstr=").concat(sqlstrUrlEncoder);
        String responseJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded").body(body).execute().body();
        if (StrUtil.isBlank(responseJson)) {
            log.info(">>>>> 福建鑫天健医药有限公司, response body is blank");
            return null;
        }
        JSONArray jsonArray = JSONObject.parseArray(responseJson);
        if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
            log.info(">>>>> 福建鑫天健医药有限公司, response jsonArray is null");
            return null;
        }
        if (jsonArray.size() == 1) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            Integer code = jsonObject.getInteger("code");
            String msg = jsonObject.getString("msg");
            if (ObjectUtil.isNotNull(code) && StrUtil.isNotBlank(msg)) {
                byte[] decode = Base64.getDecoder().decode(msg);
                String responseMsg = new String(decode);
                if (code.intValue() == 0) {
                    log.info(">>>>> 福建鑫天健医药有限公司, response success, code:{}, msg:{}", code, responseMsg);
                    return null;
                } else {
                    log.error(">>>>> 福建鑫天健医药有限公司, response error, code:{}, msg:{}", code, responseMsg);
                    return null;
                }
            }
        }

        jsonArray.forEach(o -> {
            JSONObject jsonObject1 = (JSONObject) o;
            strTrim(jsonObject1);
            if (!StringUtils.isEmpty(tableName) && tableName.startsWith("suppdoc")) {
                handlerDateField(jsonObject1, "billDate");
            }
            if (!StringUtils.isEmpty(tableName) && tableName.startsWith("saledoc")) {
                handlerDateField(jsonObject1, "billDate", "expireDate", "storeCode");
            }
            if (!StringUtils.isEmpty(tableName) && tableName.startsWith("stockdoc")){
                handlerDateField(jsonObject1, "stockDate");
            }
        });
        return jsonArray;
    }

    public void handlerDateField(JSONObject jsonObject, String... fieldNames){
        if(ObjectUtil.isNull(jsonObject) || ArrayUtil.isEmpty(fieldNames)){
            return;
        }
        for (String fieldName : fieldNames) {
            String value = (String) jsonObject.get(fieldName);
            if(StrUtil.isBlank(value)){
                // 销售客户内码为空的赋默认值
                if (ObjectUtil.equal("storeCode", fieldName)) {
                    jsonObject.put(fieldName, "0");
                } else {
                    jsonObject.put(fieldName, null);
                }
            } else {
                jsonObject.put(fieldName, DateUtil.parse(value));
            }
        }
    }

    public void strTrim(JSONObject jsonObject){
        if(ObjectUtil.isNull(jsonObject)){
            return;
        }
        Set<String> keys = jsonObject.keySet();
        if(CollUtil.isEmpty(keys)){
            return;
        }
        keys.forEach(key -> {
            Object object = jsonObject.get(key);
            if(ObjectUtil.isNotNull(object) && object instanceof String){
                jsonObject.put(key, ((String) object).trim());
            }
        });
    }

    public static void strTrim2(JSONObject jsonObject){
        if(ObjectUtil.isNull(jsonObject)){
            return;
        }
        Set<String> keys = jsonObject.keySet();
        if(CollUtil.isEmpty(keys)){
            return;
        }
        keys.forEach(key -> {
            Object object = jsonObject.get(key);
            if(ObjectUtil.isNotNull(object) && object instanceof String){
                jsonObject.put(key, ((String) object).trim());
            }
        });
    }

    public static void handlerDateField2(JSONObject jsonObject, String... fieldNames){
        if(ObjectUtil.isNull(jsonObject) || ArrayUtil.isEmpty(fieldNames)){
            return;
        }
        for (String fieldName : fieldNames) {
            String value = (String) jsonObject.get(fieldName);
            if(StrUtil.isBlank(value)){
                jsonObject.put(fieldName, null);
            } else {
                jsonObject.put(fieldName, DateUtil.parse(value));
            }
        }
    }

/*
    public static void main(String[] args) {

        String url = "http://link.fjxtj.com/xtjapi.aspx";
        String userName = "ylyy";
        String passWord = "ud9n2d0pk";
        String tableName = "suppdoc_yl";
        String key = "prodProducer";
        String sqlstr = "select * from ".concat(tableName).concat(" where ").concat(key).concat(" like '%以岭药业%';");
//        sqlstr = "select * from stockdoc where prodProducer like '%以岭药业%'";


        String userNameBase64 = new String(Base64.getEncoder().encode(userName.getBytes()));
        String passWordBase64 = new String(Base64.getEncoder().encode(passWord.getBytes()));
        String sqlstrBase64 = new String(Base64.getEncoder().encode(sqlstr.getBytes()));
        String userNameParam = null;
        String passWordParam = null;
        String sqlstrParam = null;
        try {
            userNameParam = URLEncoder.encode(userNameBase64, "UTF-8");
            passWordParam = URLEncoder.encode(passWordBase64, "UTF-8");
            sqlstrParam = URLEncoder.encode(sqlstrBase64, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        log.info(">>>>> request header, user:{}, pass:{}, sqlstr:{}", userName, passWord, sqlstr);
        log.info(">>>>> request header, userBase64:{}, passBase64:{}, sqlstrBase64:{}", userNameBase64, passWordBase64, sqlstrBase64);
        log.info(">>>>> request header, userEncoder:{}, passEncoder:{}, sqlstrEncoder:{}", userNameParam, passWordParam, sqlstrParam);

        String body = "user=".concat(userNameParam).concat("&pass=").concat(passWordParam).concat("&sqlstr=").concat(sqlstrParam);
        String responseJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded").body(body).execute().body();
        log.info(">>>>> response json:{}", responseJson);
        JSONArray jsonArray = JSONObject.parseArray(responseJson);
        if (ObjectUtil.isNull(jsonArray) || jsonArray.size() == 0) {
            log.info(">>>>> 福建鑫天健医药有限公司, response jsonArray is null");
            return;
        }
        if (jsonArray.size() == 1) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            Integer code = jsonObject.getInteger("code");
            String msg = jsonObject.getString("msg");
            if (ObjectUtil.isNotNull(code) && StrUtil.isNotBlank(msg)) {
                byte[] decode = Base64.getDecoder().decode(msg);
                String responseMsg = new String(decode);
                if (code.intValue() == 0) {
                    log.info(">>>>> 福建鑫天健医药有限公司, response success, code:{}, msg:{}", code, responseMsg);
                    return;
                } else {
                    log.error(">>>>> 福建鑫天健医药有限公司, response error, code:{}, msg:{}", code, responseMsg);
                    return;
                }
            }
        }

        jsonArray.forEach(o -> {
            JSONObject jsonObject1 = (JSONObject) o;
            strTrim2(jsonObject1);
            if(ObjectUtil.equal(tableName, "suppdoc")){
                handlerDateField2(jsonObject1, "billDate");
            }
            if(ObjectUtil.equal(tableName, "saledoc")){
                handlerDateField2(jsonObject1, "billDate", "expireDate");
            }
            if(ObjectUtil.equal(tableName, "stockdoc")){
                handlerDateField2(jsonObject1, "stockDate");
            }
        });
        log.info(">>>>> jsonArray:{}", JSONUtil.toJsonStr(jsonArray));
        return;
    }*/

}
