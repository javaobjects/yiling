package com.yiling.open.third.service.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;

import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.http.webservice.SoapProtocol;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 国药控股云南有限公司
 *
 * @author: houjie.sun
 * @date: 2022/7/4
 */
@Slf4j
@Service("guoYaoYunNanService")
public class GuoYaoYunNanServiceImpl extends FlowAbstractTemplate implements BaseFlowInterfaceService {

    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String SCRAMBLE_CODE = "scrambleCode";
    private static final String URL = "url";
    private static final String NAME_SPACE_URL = "http://tempuri.org/";
    private static final String GET_TEMP_CODE_METHOD_NAME = "getTempCodeMethodName";
    private static final String SELECTED_METHOD_NAME = "selectedMethodName";
    private static final String PURCHASE_PORT = "purchasePort";
    private static final String SALE_PORT = "salePort";
    private static final String GOODS_BATCH_PORT = "goodsBatchPort";
    private static final String RESPONSE = "Response";
    private static final String RESULT = "Result";


    @Override
    protected String requestPurchaseTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String scrambleCode = param.get(SCRAMBLE_CODE);
        String url = param.get(URL);
        String getTempCodeMethodName = param.get(GET_TEMP_CODE_METHOD_NAME);
        String selectedMethodName = param.get(SELECTED_METHOD_NAME);
        String purchasePort = param.get(PURCHASE_PORT);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyyMMdd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyyMMdd");

        // 查询验证码，GetTempCode
        String tempCode = getTempCode(userName, passWord, url, getTempCodeMethodName);
        // 验证码+扰码 加密处理得到最终验证码
        String verificationCode = getVerificationCode(tempCode, scrambleCode);
        // 查询采购，Select
        return select(userName, passWord, purchasePort, verificationCode, startDate, endDate, url, selectedMethodName);
    }

    @Override
    protected String requestSaleTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String scrambleCode = param.get(SCRAMBLE_CODE);
        String url = param.get(URL);
        String getTempCodeMethodName = param.get(GET_TEMP_CODE_METHOD_NAME);
        String selectedMethodName = param.get(SELECTED_METHOD_NAME);
        String salePort = param.get(SALE_PORT);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyyMMdd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyyMMdd");

        // 查询验证码，GetTempCode
        String tempCode = getTempCode(userName, passWord, url, getTempCodeMethodName);
        // 验证码+扰码 加密处理得到最终验证码
        String verificationCode = getVerificationCode(tempCode, scrambleCode);
        // 查询销售，Select
        return select(userName, passWord, salePort, verificationCode, startDate, endDate, url, selectedMethodName);
    }

    @Override
    protected String requestGoodsBatchTab(Map<String, String> param) {
        String userName = param.get(USER_NAME);
        String passWord = param.get(PASS_WORD);
        String scrambleCode = param.get(SCRAMBLE_CODE);
        String url = param.get(URL);
        String getTempCodeMethodName = param.get(GET_TEMP_CODE_METHOD_NAME);
        String selectedMethodName = param.get(SELECTED_METHOD_NAME);
        String goodsBatchPort = param.get(GOODS_BATCH_PORT);
        String startDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.startTime)), "yyyyMMdd");
        String endDate = DateUtil.format(DateUtil.parse(param.get(FlowAbstractTemplate.endTime)), "yyyyMMdd");

        // 查询验证码，GetTempCode
        String tempCode = getTempCode(userName, passWord, url, getTempCodeMethodName);
        // 验证码+扰码 加密处理得到最终验证码
        String verificationCode = getVerificationCode(tempCode, scrambleCode);
        // 查询库存，Select
        return select(userName, passWord, goodsBatchPort, verificationCode, startDate, endDate, url, selectedMethodName);
    }

    public String getTempCode(String id, String passWord, String url, String methodName) {
        Map<String, Object> paramsTempCode = new LinkedMap<>();
        paramsTempCode.put("id", id);
        paramsTempCode.put("paswd", passWord);
        JSONObject response = doRequest(paramsTempCode, url, methodName);
        if (ObjectUtil.isNull(response)) {
            log.warn(">>>>> 国药控股云南有限公司, getTempCode response is null, paramsTempCode:{}", JSONUtil.toJsonStr(paramsTempCode));
            return null;
        }
        String methodResult = methodName.concat(RESULT);
        String tempCode = response.getStr(methodResult, "");
        if (StrUtil.isBlank(tempCode)) {
            log.warn(">>>>> 国药控股云南有限公司, tempCode is blank, paramsTempCode:{}", JSONUtil.toJsonStr(paramsTempCode));
            return null;
        }
        return tempCode;
    }

    public String select(String id, String passWord, String port, String verificationCode, String startDate, String endDate, String url, String methodName) {
        Map<String, Object> paramsSelected = new LinkedMap<>();
        paramsSelected.put("id", id);
        paramsSelected.put("paswd", passWord);
        paramsSelected.put("port", port);
        paramsSelected.put("code", verificationCode);
        // 对于库存查询，开始、结束日期无意义，但因对方接口校验了日期字段必传
        paramsSelected.put("begindata", startDate);
        paramsSelected.put("enddata", endDate);
        JSONObject response = doRequest(paramsSelected, url, methodName);
        if (ObjectUtil.isNull(response)) {
            log.warn(">>>>> 国药控股云南有限公司, {} {} select response is null, paramsSelected:{}", methodName, port, JSONUtil.toJsonStr(paramsSelected));
            return null;
        }
        String methodResult = methodName.concat("Result");
        String resultStr = response.getStr(methodResult, "");
        if (StrUtil.isBlank(resultStr)) {
            log.warn(">>>>> 国药控股云南有限公司, {} {} resultStr is blank, paramsSelected:{}", methodName, port, JSONUtil.toJsonStr(paramsSelected));
            return null;
        }
        JSONObject cdata = JSONUtil.xmlToJson(resultStr);
        if (ObjectUtil.isNull(cdata)) {
            log.warn(">>>>> 国药控股云南有限公司, {} {} cdata is null, paramsSelected:{}", methodName, port, JSONUtil.toJsonStr(paramsSelected));
            return null;
        }
        JSONObject dataStores = cdata.getJSONObject("reponseEnvelope").getJSONObject("body").getJSONObject("dataStores");
        if (ObjectUtil.isNull(dataStores)) {
            log.warn(">>>>> 国药控股云南有限公司, {} {} dataStores is null, paramsSelected:{}", methodName, port, JSONUtil.toJsonStr(paramsSelected));
            return null;
        }

        JSONArray grids = dataStores.getJSONArray("grid");
        grids.forEach(o -> {
            JSONObject jsonObject1 = (JSONObject) o;
            strTrim(jsonObject1);
            // 处理日期、分公司数据合并进总公司
            if (ObjectUtil.equal(port, "800001")) {
                handlerDateField(jsonObject1, "销售日期", "生产日期", "失效日期");
                convertSuDeptNo(jsonObject1, "销售方代码");
            }
            if (ObjectUtil.equal(port, "800002")) {
                handlerDateField(jsonObject1, "采购日期", "生产日期", "失效日期");
                convertSuDeptNo(jsonObject1, "采购方代码");
            }
            if (ObjectUtil.equal(port, "800003")) {
                handlerDateField(jsonObject1, "库存日期", "生产日期", "失效日期");
                convertSuDeptNo(jsonObject1, "公司代码");
            }
        });
        return JSONUtil.toJsonStr(grids);
    }

    public JSONObject doRequest(Map<String, Object> params, String url, String methodName) {
        SoapClient client = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, methodName, "")).setParams(params, false).setConnectionTimeout(1000 * 60 * 5);
        String str = client.send();
        if (StrUtil.isNotBlank(str) && (str.contains("Error") || str.contains("error"))) {
            log.warn(">>>>> 国药控股云南有限公司, {}查询错误, response str:{}", methodName, str);
            return null;
        }
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        if (ObjectUtil.isNull(jsonObject)) {
            log.warn(">>>>> 国药控股云南有限公司, {} response jsonObject is null", methodName);
            return null;
        }
        String methodResponse = methodName.concat(RESPONSE);
        return jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject(methodResponse);
    }

    /**
     * 根据验证码、扰码获取加密验证码
     *
     * @param tempCode
     * @param scrambleCode
     * @return
     */
    public static String getVerificationCode(String tempCode, String scrambleCode) {
        log.info(">>>>> 国药控股云南有限公司, getVerificationCode, tempCode:{}, scrambleCode:{}", tempCode, scrambleCode);
        if (StrUtil.isBlank(tempCode) && StrUtil.isBlank(scrambleCode)) {
            return "";
        }
        String tempCodeAndScrambleCode = tempCode.concat(scrambleCode);
        // md5 加密, 转小写，并截取：从下标3取6位、从下标19取6位，进行拼接即可
        String digest = getMd5(tempCodeAndScrambleCode).toLowerCase();
        String prefix = digest.substring(3, 9);
        String suffix = digest.substring(19, 25);
        String verificationCode = prefix.concat(suffix);
        log.info(">>>>> 国药控股云南有限公司, getVerificationCode, verificationCode:{}", verificationCode);
        return verificationCode;
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void strTrim(JSONObject jsonObject) {
        if (ObjectUtil.isNull(jsonObject)) {
            return;
        }
        Set<String> keys = jsonObject.keySet();
        if (CollUtil.isEmpty(keys)) {
            return;
        }
        keys.forEach(key -> {
            Object object = jsonObject.get(key);
            if (ObjectUtil.isNotNull(object) && object instanceof String) {
                jsonObject.set(key, ((String) object).trim());
            }
        });
    }

    public static void handlerDateField(JSONObject jsonObject, String... fieldNames) {
        if (ObjectUtil.isNull(jsonObject) || ArrayUtil.isEmpty(fieldNames)) {
            return;
        }
        for (String fieldName : fieldNames) {
            String value = jsonObject.get(fieldName).toString();
            if (StrUtil.isBlank(value)) {
                jsonObject.set(fieldName, null);
            } else {
                jsonObject.set(fieldName, DateUtil.parse(value));
            }
        }
    }

    /**
     * 子公司编码 转换为 总公司编码，子公司数据合并进总公司
     * 总公司：
     * 1：国药控股云南有限公司
     * 子公司需要合并：
     * 10：国药控股曲靖有限公司
     * 20：国药控股云南滇西有限公司
     * 30：国药控股文山有限公司
     * 40：国药控股普洱有限公司
     * 50：国药控股红河有限公司
     * 60：国药控股楚雄有限公司
     * 70：国药控股昆明有限公司
     * 80：玉溪国药医药有限公司
     * 100：国药控股(保山)医药有限公司
     * 200：国药控股瑞康(文山)药业有限公司
     * 300：国药控股德宏梨华有限公司
     *
     * @param jsonObject 查询数据单条
     * @param depuNoFieldName 部门字段名称
     */
    public void convertSuDeptNo(JSONObject jsonObject, String depuNoFieldName) {
        if (ObjectUtil.isNull(jsonObject) || StrUtil.isBlank(depuNoFieldName)) {
            return;
        }
        String baseDepuNo = "1";
        String[] childDeptNoToMerge = { "10", "20", "30", "40", "50", "60", "70", "80", "100", "200", "300" };
        String childDeptNo = jsonObject.get(depuNoFieldName).toString();
        if (ArrayUtil.contains(childDeptNoToMerge, childDeptNo)) {
            jsonObject.set(depuNoFieldName, baseDepuNo);
        }
    }


    /*public static void main(String[] args) {
        // 1656993011992 + dyc67891
        // c25b6a8a1bd7
        // c25b6a8a1bd7
        String tempCode = "1656993011992";
        String scrambleCode = "dyc67891";
        String verificationCode = getVerificationCode(tempCode, scrambleCode);
        System.out.println(">>>>> 加密后验证码:" + verificationCode);
    }*/


    /*
    public static void main(String[] args) {
        String userName = "6013";
        String passWord = "666666";
        String url = "http://112.112.9.205:6666/XxdjWebService.asmx";

        String getTempCodeMethodName = "GetTempCode";
        Map<String, Object> paramsTempCode = new LinkedMap<>();
        paramsTempCode.put("id", userName);
        paramsTempCode.put("paswd", passWord);
        // GetTempCode 查询验证码
        SoapClient clientTempCode = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, getTempCodeMethodName, "")).setParams(paramsTempCode, false).setConnectionTimeout(1000 * 60 * 5);
        String str = clientTempCode.send();
        JSONObject jsonObject = JSONUtil.xmlToJson(str);
        System.out.println(">>>>> 国药控股云南有限公司, [验证码] response:" + jsonObject.toString());
        if (ObjectUtil.isNull(jsonObject)) {
            log.warn(">>>>> 国药控股云南有限公司, [验证码] jsonObject is null");
            return;
        }
        String tempCode = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetTempCodeResponse").getStr("GetTempCodeResult", "");
        System.out.println(">>>>> 国药控股云南有限公司, [验证码] tempCode:" + tempCode);
        if (StrUtil.isBlank(tempCode)) {
            log.warn(">>>>> 国药控股云南有限公司, [验证码] tempCode is blank");
            return;
        }

        // 组装验证码
        // 采用md5加密算法再截取结果，加密算法如下，加密内容为：方法GetTempCode返回结果加扰码（扰码各厂家固定分配）
        // 所需code值加密算法及截取：
        //   加密内容：15052034655661111   红色部分为：验证码，蓝色为扰码（验证码每次做查询操作时获取，扰码每个厂家固定分配）
        // 1505203465566 + 1111
        String scrambleCode = "dyc67891";
        String verificationCode = getVerificationCode(tempCode, scrambleCode);
        log.info(">>>>> 国药控股云南有限公司, [验证码] verificationCode:{}", verificationCode);

        // Selected 查询进销存
        // d        用户名
        // paswd     密码
        // port      交易代码，销售查询：800001 采购查询：800002 库存查询：800003
        // begindata 查询时间段开始日期，string类型，格式如：20170901，库存查询不必输入
        // enddata   查询时间段结束日期，string类型，格式如：20170920，库存查询不必输入
        // code      采用md5加密算法再截取结果
        String selectedMethodName = "Selected";
        String port = "800001";
        Map<String, Object> paramsSelected = new LinkedMap<>();
        paramsSelected.put("id", userName);
        paramsSelected.put("paswd", passWord);
        paramsSelected.put("port", port);
        paramsSelected.put("begindata", "20220606");
        paramsSelected.put("enddata", "20220706");
        paramsSelected.put("code", verificationCode);

        // Selected 查询进销存
        SoapClient clientSelected = SoapClient.create(url, SoapProtocol.SOAP_1_1, NAME_SPACE_URL)
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod(new QName(NAME_SPACE_URL, selectedMethodName, "")).setParams(paramsSelected, false).setConnectionTimeout(1000 * 60 * 5);
        String str2 = clientSelected.send();
        JSONObject jsonObject2 = JSONUtil.xmlToJson(str2);
        System.out.println(">>>>> 国药控股云南有限公司, [进销存] response:" + jsonObject2.toString());
        if (ObjectUtil.isNull(jsonObject2)) {
            log.warn(">>>>> 河南和鼎医药有限公司, [进销存] jsonObject is null");
            return;
        }
        String selected = jsonObject2.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("SelectedResponse").getStr("SelectedResult", "");
        if (StrUtil.isBlank(selected)) {
            log.warn(">>>>> 河南和鼎医药有限公司, [进销存] selected is blank");
            return;
        }
        JSONObject cdata = JSONUtil.xmlToJson(selected);
        System.out.println(">>>>> cdata:" + cdata.toString());
        if (ObjectUtil.isNull(cdata)) {
            log.warn(">>>>> 河南和鼎医药有限公司, [进销存] cdata is null");
            return;
        }
        JSONObject dataStores = cdata.getJSONObject("reponseEnvelope").getJSONObject("body").getJSONObject("dataStores");
        if (ObjectUtil.isNull(dataStores)) {
            log.warn(">>>>> 河南和鼎医药有限公司, [进销存] dataStores is null");
            return;
        }

        JSONArray grids = dataStores.getJSONArray("grid");
        grids.forEach(o -> {
            JSONObject jsonObject1 = (JSONObject) o;
            strTrim(jsonObject1);
            if (ObjectUtil.equal(port, "800001")) {
                handlerDateField(jsonObject1, "销售日期", "生产日期", "失效日期");
            }
            if (ObjectUtil.equal(port, "800002")) {
                handlerDateField(jsonObject1, "采购日期", "生产日期", "失效日期");
            }
            if (ObjectUtil.equal(port, "800003")) {
                handlerDateField(jsonObject1, "库存日期", "生产日期", "失效日期");
            }
        });
    }
    */
}
