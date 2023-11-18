package com.yiling.open.third;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.open.BaseTest;

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
 * @author fucheng.bai
 * @date 2023/3/27
 */
@Slf4j
public class FuJianXinTianJianTest extends BaseTest {

    public static void main(String[] args) {
        String url = "http://link.fjxtj.com/tjapi.aspx";
        String userName = "ylyy";
        String passWord = "ud9n2d0pk";
        String queryKey = "prodProducer";
        String saleTableName = "stockdoc";
        //        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyy-MM-dd");
        //        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyy-MM-dd");
        String sql = "select * from ".concat(saleTableName).concat(" where ").concat(queryKey).concat(" like '%").concat("以岭药业").concat("%';");

        JSONArray jsonArrayResult = doRequest(url, userName, passWord, sql, saleTableName);
        System.out.println(jsonArrayResult);
    }

    public static JSONArray doRequest(String url, String user, String pass, String sql, String tableName) {
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
        System.out.println("responseJson = " + responseJson);
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

    public static void handlerDateField(JSONObject jsonObject, String... fieldNames){
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

    public static void strTrim(JSONObject jsonObject){
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
}
