package com.yiling.open.third.service.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.redis.RedisKey;
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
 * @author: houjie.sun
 * @date: 2022/5/11
 */
@Service("shanXiPaiAngService")
@Slf4j
public class ShanXiPaiAngServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN = "token";
    private static final long TOKEN_EXPIRE = 23;
    private static final String FLOW_TOKEN = "flow_third_token:shanxipaiang";

    private static final String TOKEN_URL = "tokenUrl";
    private static final String PURCHASE_URL = "purchaseUrl";
    private static final String SALE_URL = "saleUrl";
    private static final String GOODS_BATCH_URL = "goodsBatchUrl";
    private static final String FLOW_DAY_UPDATE_FLAG = "flowDayUpdateFlag";
    private static final String FLOW_DAY_URL = "flowDayUrl";

    private static final String ACCOUNT_LIST = "accountList";
    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";


    /**
     * 获取采购流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String tokenUrl = param.get(TOKEN_URL);
        String purchaseUrl = param.get(PURCHASE_URL);
        String accountList = param.get(ACCOUNT_LIST);
        JSONArray accountArray = JSONArray.parseArray(accountList);
        if (ObjectUtil.isNull(accountArray) || accountArray.size() == 0) {
            return null;
        }
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");

        JSONArray returnJsonArray = new JSONArray();
        for (Object o : accountArray) {
            Map accountMap = (Map) o;
            String userCode = (String) accountMap.get(USER_NAME);
            String license = (String) accountMap.get(PASS_WORD);
            String token = this.getToken(userCode, license, tokenUrl);
            String messageId = UUID.randomUUID().toString().replace("-", "");
            doRequestFlowData(returnJsonArray, token, purchaseUrl, startDate, endDate, messageId);
        }
        purchaseAndSaleTrimHandler(returnJsonArray);
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
        String tokenUrl = param.get(TOKEN_URL);
        String saleUrl = param.get(SALE_URL);
        String accountList = param.get(ACCOUNT_LIST);
        JSONArray accountArray = JSONArray.parseArray(accountList);
        if (ObjectUtil.isNull(accountArray) || accountArray.size() == 0) {
            return null;
        }
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");

        JSONArray returnJsonArray = new JSONArray();
        for (Object o : accountArray) {
            Map accountMap = (Map) o;
            String userCode = (String) accountMap.get(USER_NAME);
            String license = (String) accountMap.get(PASS_WORD);
            String token = this.getToken(userCode, license, tokenUrl);
            String messageId = UUID.randomUUID().toString().replace("-", "");
            doRequestFlowData(returnJsonArray, token, saleUrl, startDate, endDate, messageId);
        }
        purchaseAndSaleTrimHandler(returnJsonArray);
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
        String tokenUrl = param.get(TOKEN_URL);
        String goodsBatchUrl = param.get(GOODS_BATCH_URL);
        String accountList = param.get(ACCOUNT_LIST);
        JSONArray accountArray = JSONArray.parseArray(accountList);
        if (ObjectUtil.isNull(accountArray) || accountArray.size() == 0) {
            return null;
        }

        JSONArray returnJsonArray = new JSONArray();
        for (Object o : accountArray) {
            Map accountMap = (Map) o;
            String userCode = (String) accountMap.get(USER_NAME);
            String license = (String) accountMap.get(PASS_WORD);
            String token = this.getToken(userCode, license, tokenUrl);
            String messageId = UUID.randomUUID().toString().replace("-", "");
            doRequestFlowData(returnJsonArray, token, goodsBatchUrl, null, null, messageId);
        }
        goodsBatchTrimHandler(returnJsonArray);
        return JSONUtil.toJsonStr(returnJsonArray);
    }

    /**
     * 修改回滚天数
     *
     * @param param
     * @param flowDay
     * @return
     */
    protected void requestFlowDayUpdate(Map<String, String> param, Integer flowDay){
        String tokenUrl = param.get(TOKEN_URL);
        String flowDayUrl = param.get(FLOW_DAY_URL);
        String accountList = param.get(ACCOUNT_LIST);
        JSONArray accountArray = JSONArray.parseArray(accountList);
        if (ObjectUtil.isNull(accountArray) || accountArray.size() == 0) {
            return;
        }

        for (Object o : accountArray) {
            Map accountMap = (Map) o;
            String flowDayUpdateFlag = (String) accountMap.get(FLOW_DAY_UPDATE_FLAG);
            if(ObjectUtil.equal(flowDayUpdateFlag, "0")){
                continue;
            }
            String userCode = (String) accountMap.get(USER_NAME);
            String license = (String) accountMap.get(PASS_WORD);
            String token = this.getToken(userCode, license, tokenUrl);
            String messageId = UUID.randomUUID().toString().replace("-", "");
            doRequestFlowDayUpdate(token, flowDayUrl, messageId, flowDay.toString());
        }
        return;
    }

    /**
     * 获取token
     *
     * @param userCode 用户编号
     * @param license 授权码
     * @param tokenUrl 接口地址
     * @return
     */
    private String getToken(String userCode, String license, String tokenUrl){
        if(StrUtil.isBlank(userCode) || StrUtil.isBlank(license)  || StrUtil.isBlank(tokenUrl)){
            log.warn(">>>>> 陕西派昂, 获取token的账号、密码、地址，都不能为空");
            return null;
        }
        String key = RedisKey.generate(FLOW_TOKEN, userCode);
        String token = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(token)){
            return token;
        }
        String json = "{\"userCode\": \"" + userCode + "\",\"license\": \"" + license + "\"}";
        String returnJson = HttpRequest.post(tokenUrl).header(Header.CONTENT_TYPE, "application/json").body(json).execute().body();
        if(StrUtil.isBlank(returnJson)){
            log.warn(">>>>> 陕西派昂, 获取token的接口返回空");
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(returnJson);
        if (ObjectUtil.isNotNull(jsonObject)) {
            token = jsonObject.getString("result");
            log.info(">>>>> 陕西派昂, token:{}", token);
            stringRedisTemplate.opsForValue().set(key, token, TOKEN_EXPIRE, TimeUnit.HOURS);
            return token;
        }
        return null;
    }

    /**
     * 查询
     *
     * @param returnJsonArray 结果数据列表
     * @param token token
     * @param url 接口地址
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param messageId 接口查询的唯一标识
     */
    private void doRequestFlowData(JSONArray returnJsonArray, String token, String url, String startDate, String endDate, String messageId) {
        String json;
        if(StrUtil.isBlank(startDate) && StrUtil.isBlank(endDate)){
            // 库存流向无日期参数
            json = "{\"messageId\":\"" + messageId + "\"}";
        } else  {
            json = "{\"messageId\":\"" + messageId + "\",\"startDate\": \"" + startDate + "\",\"endDate\": \"" + endDate + "\"}";
        }
        String returnJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").header(TOKEN, token).body(json).execute().body();
        if (StrUtil.isNotEmpty(returnJson)) {
            JSONObject jsonObject = JSON.parseObject(returnJson);
            if (jsonObject != null) {
                Object code = jsonObject.get("code");
                if(!ObjectUtil.equal("200", code.toString())){
                    log.warn(">>>>> 陕西派昂, response:{}", returnJson);
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray != null && jsonArray.size() > 0) {
                    returnJsonArray.addAll(jsonArray);
                }
            }
        }
    }

    /**
     * 修改回滚天数
     *
     * @param token token
     * @param url 接口地址
     * @param messageId 接口查询的唯一标识
     * @param flowDay 回滚天数
     */
    private void doRequestFlowDayUpdate(String token, String url, String messageId, String flowDay) {
        String json = "{\"messageId\":\"" + messageId + "\",\"day\":\"" + flowDay + "\"}";
        String returnJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").header(TOKEN, token).body(json).execute().body();
        if (StrUtil.isNotEmpty(returnJson)) {
            JSONObject jsonObject = JSON.parseObject(returnJson);
            if (jsonObject != null) {
                Object code = jsonObject.get("code");
                if(!ObjectUtil.equal("200", code.toString())){
                    log.warn(">>>>> 陕西派昂, 修改回滚天数失败, response:{}", returnJson);
                }
            }
        }
    }

    private void purchaseAndSaleTrimHandler(JSONArray returnJsonArray) {
        if(ObjectUtil.isNotNull(returnJsonArray) && returnJsonArray.size() >0){
            for (int i = 0; i < returnJsonArray.size(); i++) {
                JSONObject object = (JSONObject) returnJsonArray.get(i);
                Object orderNo = object.get("orderNo");
                if(ObjectUtil.isNotNull(orderNo) && orderNo instanceof String){
                    object.put("orderNo", orderNo.toString().trim());
                }
                Object sNo = object.get("sNo");
                if(ObjectUtil.isNotNull(sNo) && sNo instanceof String){
                    object.put("sNo", sNo.toString().trim());
                }
                Object date = object.get("date");
                if(ObjectUtil.isNotNull(date) && date instanceof String){
                    object.put("date", date.toString().trim());
                }
                Object userCode = object.get("userCode");
                if(ObjectUtil.isNotNull(userCode) && userCode instanceof String){
                    object.put("userCode", userCode.toString().trim());
                }
                Object userName = object.get("userName");
                if(ObjectUtil.isNotNull(userName) && userName instanceof String){
                    object.put("userName", userName.toString().trim());
                }
                Object productCode = object.get("productCode");
                if(ObjectUtil.isNotNull(productCode) && productCode instanceof String){
                    object.put("productCode", productCode.toString().trim());
                }
                Object productName = object.get("productName");
                if(ObjectUtil.isNotNull(productName) && productName instanceof String){
                    object.put("productName", productName.toString().trim());
                }
                Object unit = object.get("unit");
                if(ObjectUtil.isNotNull(unit) && unit instanceof String){
                    object.put("unit", unit.toString().trim());
                }
                Object spec = object.get("spec");
                if(ObjectUtil.isNotNull(spec) && spec instanceof String){
                    object.put("spec", spec.toString().trim());
                }
                Object qtySpec = object.get("qtySpec");
                if(ObjectUtil.isNotNull(qtySpec) && qtySpec instanceof String){
                    object.put("qtySpec", qtySpec.toString().trim());
                }
                Object factoryName = object.get("factoryName");
                if(ObjectUtil.isNotNull(factoryName) && factoryName instanceof String){
                    object.put("factoryName", factoryName.toString().trim());
                }
                Object manufacturer = object.get("manufacturer");
                if(ObjectUtil.isNotNull(manufacturer) && manufacturer instanceof String){
                    object.put("manufacturer", manufacturer.toString().trim());
                }
                Object amount = object.get("amount");
                if(ObjectUtil.isNotNull(amount) && amount instanceof String){
                    object.put("amount", amount.toString().trim());
                }
                Object price = object.get("price");
                if(ObjectUtil.isNotNull(price) && price instanceof String){
                    object.put("price", price.toString().trim());
                }
                Object totalPrice = object.get("totalPrice");
                if(ObjectUtil.isNotNull(totalPrice) && totalPrice instanceof String){
                    object.put("totalPrice", totalPrice.toString().trim());
                }
                Object expDate = object.get("expDate");
                if(ObjectUtil.isNotNull(expDate) && expDate instanceof String){
                    object.put("expDate", expDate.toString().trim());
                }
                Object prodDate = object.get("prodDate");
                if(ObjectUtil.isNotNull(prodDate) && prodDate instanceof String){
                    object.put("prodDate", prodDate.toString().trim());
                }
                Object batchNo = object.get("batchNo");
                if(ObjectUtil.isNotNull(batchNo) && batchNo instanceof String){
                    object.put("batchNo", batchNo.toString().trim());
                }
                Object orgIdentity = object.get("orgIdentity");
                if(ObjectUtil.isNotNull(orgIdentity) && orgIdentity instanceof String){
                    object.put("orgIdentity", orgIdentity.toString().trim());
                }
            }
        }
    }

    private void goodsBatchTrimHandler(JSONArray returnJsonArray) {
        if(ObjectUtil.isNotNull(returnJsonArray) && returnJsonArray.size() >0){
            for (int i = 0; i < returnJsonArray.size(); i++) {
                JSONObject object = (JSONObject) returnJsonArray.get(i);
                Object date = object.get("date");
                if(ObjectUtil.isNotNull(date) && date instanceof String){
                    object.put("date", date.toString().trim());
                }
                Object productCode = object.get("productCode");
                if(ObjectUtil.isNotNull(productCode) && productCode instanceof String){
                    object.put("productCode", productCode.toString().trim());
                }
                Object productName = object.get("productName");
                if(ObjectUtil.isNotNull(productName) && productName instanceof String){
                    object.put("productName", productName.toString().trim());
                }
                Object unit = object.get("unit");
                if(ObjectUtil.isNotNull(unit) && unit instanceof String){
                    object.put("unit", unit.toString().trim());
                }
                Object spec = object.get("spec");
                if(ObjectUtil.isNotNull(spec) && spec instanceof String){
                    object.put("spec", spec.toString().trim());
                }
                Object qtySpec = object.get("qtySpec");
                if(ObjectUtil.isNotNull(qtySpec) && qtySpec instanceof String){
                    object.put("qtySpec", qtySpec.toString().trim());
                }
                Object factoryName = object.get("factoryName");
                if(ObjectUtil.isNotNull(factoryName) && factoryName instanceof String){
                    object.put("factoryName", factoryName.toString().trim());
                }
                Object manufacturer = object.get("manufacturer");
                if(ObjectUtil.isNotNull(manufacturer) && manufacturer instanceof String){
                    object.put("manufacturer", manufacturer.toString().trim());
                }
                Object amount = object.get("amount");
                if(ObjectUtil.isNotNull(amount) && amount instanceof String){
                    object.put("amount", amount.toString().trim());
                }
                Object expDate = object.get("expDate");
                if(ObjectUtil.isNotNull(expDate) && expDate instanceof String){
                    object.put("expDate", expDate.toString().trim());
                }
                Object prodDate = object.get("prodDate");
                if(ObjectUtil.isNotNull(prodDate) && prodDate instanceof String){
                    object.put("prodDate", prodDate.toString().trim());
                }
                Object batchNo = object.get("batchNo");
                if(ObjectUtil.isNotNull(batchNo) && batchNo instanceof String){
                    object.put("batchNo", batchNo.toString().trim());
                }
                Object orgIdentity = object.get("orgIdentity");
                if(ObjectUtil.isNotNull(orgIdentity) && orgIdentity instanceof String){
                    object.put("orgIdentity", orgIdentity.toString().trim());
                }
            }
        }
    }

//    public static void main(String[] args) {
        //        String accountList = "[{\"userName\":\"GY03892\",\"passWord\": \"ecb7c8421d72991e34a8547e231fc290\"},{\"userName\":\"GY04540\",\"passWord\": \"18ad772060fa90eee391b16079d59c48\"},{\"userName\":\"GY07207\",\"passWord\": \"e309996ee427436d8c3ffae4509eb93a\"}]";
        //        JSONArray accountArray = JSONArray.parseArray(accountList);
        //        for (Object o : accountArray) {
        //            Map accountMap = (Map) o;
        //            String userCode = (String) accountMap.get(USER_NAME);
        //            String license = (String) accountMap.get(PASS_WORD);
        //            System.out.println(">>>>> userCode:" + userCode);
        //            System.out.println(">>>>> license:" + license);
        //        }

//        String str = "    *12   *   35  *   ";
//        String trim = str.trim();
//        System.out.println(">>>>> trim:" + trim);
//    }

}
