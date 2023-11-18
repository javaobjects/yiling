package com.yiling.open.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.common.base.Joiner;
import com.yiling.framework.common.redis.RateLimiterUtil;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpTaskInterfaceApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;
import com.yiling.open.erp.validation.entity.ValidationResult;
import com.yiling.open.web.util.RequestParametersHandler;
import com.yiling.open.web.util.YilingHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author shuan
 */
@Slf4j
public class OpenBaseController {

    @Resource
    private   StringRedisTemplate stringRedisTemplate;
    @DubboReference
    public    ErpClientApi        erpClientApi;
    @DubboReference
    public    ErpTaskInterfaceApi erpTaskInterfaceApi;
    @Resource
    protected RateLimiterUtil     rateLimiterUtil;
    @DubboReference
    GoodsApi    goodsApi;
    @DubboReference
    PopGoodsApi popGoodsApi;

    /**
     * 获取所有的请求头参数并存放在到Map集合中
     *
     * @param request
     * @return
     */
    public Map<String, String> getAllRequestHeader(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();

        //获取所有的头部参数
        Enumeration<String> headerNames = request.getHeaderNames();
        for (Enumeration<String> e = headerNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement();
            String thisValue = request.getHeader(thisName);
            result.put(thisName, thisValue);
        }
        return result;
    }

    /**
     * 获取所有的请求参数并存放在到Map集合中
     *
     * @param request
     * @return
     */
    public Map<String, String> getAllRequestParams(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();

        Map<String, String[]> map = request.getParameterMap();
        Set<Entry<String, String[]>> keSet = map.entrySet();
        for (Iterator<Entry<String, String[]>> itr = keSet.iterator(); itr.hasNext(); ) {
            Entry<String, String[]> me = itr.next();
            String key = me.getKey();
            String[] value = me.getValue();
            if (value.length == 1) {
                result.put(key, value[0]);
            } else {
                result.put(key, Joiner.on(",").join(value));
            }
        }
        return result;
    }

    /**
     * 获取请求头信息参数并可在到Map集合中
     *
     * @param request
     * @return
     */
    public Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        //获取请求参数
        String sign = StrUtil.nullToEmpty(request.getHeader(OpenConstants.SIGN)).trim();
        String appKey = StrUtil.nullToEmpty(request.getHeader(OpenConstants.APP_KEY)).trim();
        String timestamp = StrUtil.nullToEmpty(request.getHeader(OpenConstants.TIMESTAMP)).trim();
        String method = StrUtil.nullToEmpty(request.getHeader(OpenConstants.METHOD)).trim();
        String v = StrUtil.nullToEmpty(request.getHeader(OpenConstants.VERSION)).trim();

        map.put(OpenConstants.SIGN, sign);
        map.put(OpenConstants.APP_KEY, appKey);
        map.put(OpenConstants.TIMESTAMP, timestamp);
        map.put(OpenConstants.METHOD, method);
        map.put(OpenConstants.VERSION, v);
        return map;
    }


    /**
     * 获取所有的请求参数并存放在到Map集合中
     *
     * @param request
     * @return
     */
    public String getDataRequest(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            request.setCharacterEncoding("UTF-8");
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return URLDecoder.decode(buffer.toString(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 验证签名
     *
     * @param allRequestParams request的请求参数
     * @param appSecret        请求秘钥
     * @return
     * @author wanfei.zhang
     * @date 2018年8月16日
     */
    protected boolean validateSign(Map<String, String> allRequestParams, String appSecret) {
        String sign = allRequestParams.remove(OpenConstants.SIGN);
        try {
            String validateSign = SignatureAlgorithm.signRequestNew(allRequestParams, appSecret, false);
            //验证签名是否正确
            if (StrUtil.isEmpty(validateSign)) {
                return false;
            }

            if (sign.equals(validateSign)) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    /**
     * 验证签名
     *
     * @param sysParams 系统级请求参数
     * @param bizParams 业务级请求参数
     * @return
     */
    public boolean validateSign(Map<String, String> sysParams, Map<String, String> bizParams) {

        String sign = StrUtil.nullToEmpty(sysParams.get(OpenConstants.SIGN)).trim();
        String appKey = StrUtil.nullToEmpty(sysParams.get(OpenConstants.APP_KEY)).trim();
        String signMethod = StrUtil.nullToEmpty(sysParams.get(OpenConstants.SIGN_METHOD)).trim();
        String format = StrUtil.nullToEmpty(sysParams.get(OpenConstants.FORMAT)).trim();
        String accessToken = StrUtil.nullToEmpty(sysParams.get(OpenConstants.ACCESS_TOKEN)).trim();
        String method = StrUtil.nullToEmpty(sysParams.get(OpenConstants.METHOD)).trim();
        String timestamp = StrUtil.nullToEmpty(sysParams.get(OpenConstants.TIMESTAMP)).trim();
        String partnerId = StrUtil.nullToEmpty(sysParams.get(OpenConstants.PARTNER_ID)).trim();

        String appSecret = this.getAppSecret(appKey);
        if (StrUtil.isEmpty(appSecret)) {
            log.error("[OpenBaseController][validateSign]:验证签名appSecret参数不能为空或null！");
            return false;
        }

        //签名验证
        RequestParametersHandler requestHandler = new RequestParametersHandler();
        YilingHashMap appParams = new YilingHashMap(bizParams);
        requestHandler.setApplicationParams(appParams);

        // 添加协议级请求参数
        YilingHashMap protocalMustParams = new YilingHashMap();
        protocalMustParams.put(OpenConstants.METHOD, method);
        protocalMustParams.put(OpenConstants.VERSION, OpenConstants.SDK_VERSION);
        protocalMustParams.put(OpenConstants.APP_KEY, appKey);
        protocalMustParams.put(OpenConstants.SIGN, sign);

        protocalMustParams.put(OpenConstants.TIMESTAMP, DateUtil.parse(timestamp, "yyyy-MM-dd HH:mm:ss"));
        requestHandler.setProtocalMustParams(protocalMustParams);

        // 添加可选级请求参数
        YilingHashMap protocalOptParams = new YilingHashMap();
        protocalOptParams.put(OpenConstants.FORMAT, format);
        protocalOptParams.put(OpenConstants.SIGN_METHOD, signMethod);
        protocalOptParams.put(OpenConstants.ACCESS_TOKEN, accessToken);
        protocalOptParams.put(OpenConstants.PARTNER_ID, partnerId);
        requestHandler.setProtocalOptParams(protocalOptParams);

        return validateSign(requestHandler.getAllParams(), appSecret);
    }

    /**
     * 获取授权应用密钥AppSecret
     *
     * @param appKey 授权应用公钥
     * @return
     */
    public String getAppSecret(String appKey) {
        ErpClientDTO clientDetail = this.getOauthClientDetailByAppKey(appKey);
        if (clientDetail != null) {
            return clientDetail.getClientSecret();
        }
        return "";
    }

    /**
     * 根据授权公钥查询应用授权对象信息
     *
     * @param appKey
     * @return
     */
    public ErpClientDTO getOauthClientDetailByAppKey(String appKey) {
        String json = stringRedisTemplate.opsForValue().get("open:erpClient:" + appKey + "+selectByClientKey");
        ErpClientDTO result = null;
        if (StrUtil.isNotEmpty(json)) {
            try {
                Jackson2JsonRedisSerializer<ErpClientDTO> serializer = new Jackson2JsonRedisSerializer(ErpClientDTO.class);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                //必须设置，否则无法将JSON转化为对象，会转化成Map类型
                objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
                // 反序列化时，有些字段在实体类中找不到则忽略
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                serializer.setObjectMapper(objectMapper);
                result = serializer.deserialize(json.getBytes());
            } catch (Exception e) {
            }
        }
        if (result == null) {
            result = erpClientApi.findErpClientByKey(appKey);
        }
        return result;
    }

    /**
     * 根据授权公钥查询应用授权对象信息
     *
     * @param appKey
     * @return
     */
    public Long getSuIdByAppKey(String appKey) {
        ErpClientDTO result = this.getOauthClientDetailByAppKey(appKey);
        if (result != null) {
            return result.getSuId();
        }
        return null;
    }

    /**
     * 通过请求方法名获取资源对象
     *
     * @param taskNo 资源入口uri,对客户端公开的资源地址
     * @return
     */
    public ErpTaskInterfaceDTO getInterfaceByTaskNo(String taskNo) {
        String json = stringRedisTemplate.opsForValue().get("open:erpTaskInterfaceService:" + taskNo + "+findErpTaskInterfaceByTaskNo");
        ErpTaskInterfaceDTO result = null;
        if (StrUtil.isNotEmpty(json)) {
            try {
                Jackson2JsonRedisSerializer<ErpTaskInterfaceDTO> serializer = new Jackson2JsonRedisSerializer(ErpTaskInterfaceDTO.class);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                //必须设置，否则无法将JSON转化为对象，会转化成Map类型
                objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
                // 反序列化时，有些字段在实体类中找不到则忽略
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                serializer.setObjectMapper(objectMapper);
                result = serializer.deserialize(json.getBytes());
            } catch (Exception e) {
            }
        }
        if (result == null) {
            result = erpTaskInterfaceApi.findErpTaskInterfaceByTaskNo(taskNo);
        }
        return result;
    }

    /**
     * 通过请求方法名获取资源对象
     *
     * @param suId 资源入口uri,对客户端公开的资源地址
     * @return
     */
    public List<ErpClientDTO> selectBySuId(Long suId) {
        String json = stringRedisTemplate.opsForValue().get("open:erpClient:" + suId + "+selectBySuId");
        List<ErpClientDTO> result = null;
        if (StrUtil.isNotEmpty(json)) {
            try {
                Jackson2JsonRedisSerializer<List<ErpClientDTO>> serializer = new Jackson2JsonRedisSerializer(ErpClientDTO.class);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                //必须设置，否则无法将JSON转化为对象，会转化成Map类型
                objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
                // 反序列化时，有些字段在实体类中找不到则忽略
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                serializer.setObjectMapper(objectMapper);
                result = serializer.deserialize(json.getBytes());
            } catch (Exception e) {
            }
        }
        if (result == null) {
            result = erpClientApi.selectBySuId(suId);
        }
        return result;
    }

    /**
     * 判断字符串是否为时间戳
     *
     * @param timestamp 字符串时间戳
     * @return
     */
    protected boolean isTimestamp(String timestamp) {
        if (StrUtil.isEmpty(timestamp)) {
            return false;
        }
        boolean flag = false;
        try {
            Timestamp.valueOf(timestamp);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 获取图片域名   图片为空时，返回默认图片
     *
     * @returngetImgDomainQiNiuPublic
     */
    public String getImgDomainQiNiuPublic(String imgKey, int width, int height) {
        if (StrUtil.isEmpty(imgKey)) {
            return "http://download.mypharma.com/default.jpg";
        }
        String url = "http://download.mypharma.com/" + imgKey;
        if (width != 0 && height != 0) {
            int index = url.lastIndexOf(".");
            url = url.substring(0, index);
            url = url + "_" + width + "-" + height + ".jpg";
        }
        return url;
    }

    /**
     * 获取图片域名   图片为空时，返回默认图片<br/>
     * 七牛空间公有图片访问
     *
     * @return
     */
    public String getImgDomainQiNiuPublic(String imgKey) {
        String url = "http://";
        if (StrUtil.isEmpty(imgKey)) {
            return "http://download.mypharma.com/default.jpg";
        }
        url += "download.mypharma.com/" + imgKey;
        return url;
    }

    /**
     * 根据SellSpecificationsId和Eid获取商品内码
     *
     * @param suIds
     * @param goodsId
     * @return
     */
    public Map<Long, String> getGoodsInfoYlGoodsIdAndEid(List<Long> suIds, List<Long> goodsId) {
        Map<Long, String> map = new HashMap<>();

        List<GoodsInfoDTO> goodsInfoDTOS = popGoodsApi.batchQueryInfo(goodsId);
        Map<Long, Long> mapSellSpecificationsMap = goodsInfoDTOS.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, GoodsInfoDTO::getSellSpecificationsId));
        List<GoodsDTO> goodsInfoList = goodsApi.findGoodsBySellSpecificationsIdAndEid(new ArrayList<>(mapSellSpecificationsMap.values()), suIds);
        Map<Long, List<GoodsDTO>> goodsMap = goodsInfoList.stream().collect(Collectors.groupingBy(GoodsDTO::getSellSpecificationsId));

        goodsId.forEach(e -> {
            map.put(e, "");
            List<GoodsDTO> list = goodsMap.get(mapSellSpecificationsMap.get(e));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> popGoodsIds = list.stream().map(t -> t.getId()).collect(Collectors.toList());
                List<PopGoodsDTO> popGoodsDTOList = popGoodsApi.getPopGoodsListByGoodsIds(popGoodsIds);
                Map<Long, PopGoodsDTO> popGoodsDTOMap = popGoodsDTOList.stream().filter(t -> t.getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())).collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));

                List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(new ArrayList<>(popGoodsDTOMap.keySet()));
                goodsSkuDTOList = goodsSkuDTOList.stream().filter(t -> t.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
                for (GoodsSkuDTO goodsSkuDTO : goodsSkuDTOList) {
                    if (StrUtil.isNotEmpty(goodsSkuDTO.getInSn())) {
                        map.put(e, goodsSkuDTO.getInSn());
                        break;
                    }
                }
            }
        });
        return map;
    }

    /**
     * 回写订单更新状态，组装请求参数
     *
     * @param list
     * @param erpOrderDTO
     * @param validationResult
     * @param errorMsgTitle
     */
    public void buildUpdateOrderRequest(List<UpdateErpOrderPushRequest> list, ErpOrderPurchaseDTO erpOrderDTO,
                                        ValidationResult validationResult, String errorMsgTitle) {
        UpdateErpOrderPushRequest updateOrderPurchaseRequest = new UpdateErpOrderPushRequest();
        updateOrderPurchaseRequest.setOrderId(erpOrderDTO.getOrderId());
        updateOrderPurchaseRequest.setPushType(2);
        updateOrderPurchaseRequest.setErpPushStatus(3);
        // 异常信息
        Map<String, String> errorMap = validationResult.getErrorMap();
        String errorKey = new ArrayList<>(errorMap.keySet()).get(0);
        String errorMsg = errorMap.get(errorKey);
        updateOrderPurchaseRequest.setErpPushRemark(errorMsgTitle.concat(errorKey).concat(errorMsg));
        list.add(updateOrderPurchaseRequest);
    }
}
