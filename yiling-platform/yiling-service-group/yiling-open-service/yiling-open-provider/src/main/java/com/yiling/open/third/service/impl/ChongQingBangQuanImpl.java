package com.yiling.open.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

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
 * 重庆邦权对接流向接口
 *
 * @author: houjie.sun
 * @date: 2022/5/13
 */
@Slf4j
@Service("chongQingBangQuanService")
public class ChongQingBangQuanImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {


    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String URL = "url";
    private static final String PURCHASE_METHED = "purchaseMethed";
    private static final String SALE_METHED = "saleMethed";
    private static final String GOODS_BATCH_METHED = "goodsBatchMethed"; // todo 库存也需要传日期吗？？？


    /**
     * 获取采购流向
     *
     * @param param
     * @return
     */
    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String purchaseMethed = param.get(PURCHASE_METHED);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");

        Map<String,String> map = new HashMap<>();
        map.put("app_id", userName);
        map.put("app_secred", passWord);
        map.put("methed", purchaseMethed);
        map.put("datestr", startDate);
        map.put("dateend", endDate);
        String requestBody = JSONUtil.toJsonStr(map);

        JSONArray returnJsonArray = new JSONArray();
        doRequestFlowData(returnJsonArray, url, requestBody);
//        purchaseAndSaleTrimHandler(returnJsonArray);
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
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String saleMethed = param.get(SALE_METHED);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");

        Map<String,String> map = new HashMap<>();
        map.put("app_id", userName);
        map.put("app_secred", passWord);
        map.put("methed", saleMethed);
        map.put("datestr", startDate);
        map.put("dateend", endDate);
        String requestBody = JSONUtil.toJsonStr(map);

        JSONArray returnJsonArray = new JSONArray();
        doRequestFlowData(returnJsonArray, url, requestBody);
        //        purchaseAndSaleTrimHandler(returnJsonArray);
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
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String url = param.get(URL);
        String goodsBatchMethed = param.get(GOODS_BATCH_METHED);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");

        Map<String,String> map = new HashMap<>();
        map.put("app_id", userName);
        map.put("app_secred", passWord);
        map.put("methed", goodsBatchMethed);
//        map.put("datestr", startDate);
//        map.put("dateend", endDate);
        String requestBody = JSONUtil.toJsonStr(map);

        JSONArray returnJsonArray = new JSONArray();
        doRequestFlowData(returnJsonArray, url, requestBody);
        //        purchaseAndSaleTrimHandler(returnJsonArray);
        return JSONUtil.toJsonStr(returnJsonArray);
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
    private void doRequestFlowData(JSONArray returnJsonArray, String url, String json) {
        String returnJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").body(json).execute().body();
        if (StrUtil.isNotEmpty(returnJson)) {
            JSONObject jsonObject = JSON.parseObject(returnJson);
            if (jsonObject != null) {
                Object status = jsonObject.get("status");
                if(!(Boolean)status){
                    log.warn(">>>>> 重庆邦权, response:{}", returnJson);
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                if (jsonArray != null && jsonArray.size() > 0) {
                    returnJsonArray.addAll(jsonArray);
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
