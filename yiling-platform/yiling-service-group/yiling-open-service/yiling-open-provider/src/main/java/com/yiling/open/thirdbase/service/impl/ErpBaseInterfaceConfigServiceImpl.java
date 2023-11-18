package com.yiling.open.thirdbase.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.thirdbase.service.ErpBaseInterfaceConfigService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/6/20
 */
@Slf4j
@Service("erpBaseInterfaceConfigService")
public class ErpBaseInterfaceConfigServiceImpl implements ErpBaseInterfaceConfigService {


    private static final String client_id = "bwzd";
    private static final String secret = "57judkd8rt73jdugyjl94";
    private static final long su_id = 684;

    // 商品接口
    private static final String goods_url = "https://third.mingheyaoye.com/?s=/bw/Goods/index";
    // 库存接口
    private static final String goods_batch_url = "https://third.mingheyaoye.com/?s=/bw/Goods/goodsStock";
    // 分组定价接口
    private static final String goods_group_price_url = "https://third.mingheyaoye.com/?s=/bw/Goods/goodsPriceTable";
    // 用户接口
    private static final String customer_url = "https://third.mingheyaoye.com/?s=/bw/User/getUserInfo";


    @DubboReference
    private ErpClientApi erpClientApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public void executeBaseInterface() {
        log.info("任务开始：广西明合药业有限责任公司, 查询基础对接接口信息");

        ErpClientDTO erpClientDTO = erpClientApi.selectByRkSuId(su_id);

        // 商品信息
        queryGoods(erpClientDTO);

        // 库存信息
        queryGoodsBatch(erpClientDTO);

        // 分组定价信息
        queryGoodsGroupPrice(erpClientDTO);

        // 客户信息
        queryErpCustomer(erpClientDTO);

        log.info("任务结束：广西明合药业有限责任公司, 查询基础对接接口信息");
    }

    private void queryGoods(ErpClientDTO erpClientDTO) {
        log.info("同步商品信息开始，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
        // 组装请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("is_on_sale", 1);
        String requestBody = buildParams(paramMap);

        try {
            // 查询数据
            JSONArray jsonArray = queryData(goods_url, requestBody, "查询商品信息接口");
            if (jsonArray != null && jsonArray.size() > 0) {
                // 保存商品信息
                List<ErpGoodsDTO> goodsList = new ArrayList<>();
                ErpGoodsDTO erpGoods;
                Date addTime = new Date();
                for (Object o : jsonArray) {
                    JSONObject jsonObj = (JSONObject) o;
                    erpGoods = new ErpGoodsDTO();
                    buildErpGoods(erpClientDTO, goodsList, erpGoods, jsonObj, addTime);
                }
                if (CollUtil.isNotEmpty(goodsList)) {
                    List<List<ErpGoodsDTO>> partition = Lists.partition(goodsList, 500);
                    for (List<ErpGoodsDTO> erpGoodsDTOS : partition) {
                        String json = JSON.toJSONString(erpGoodsDTOS);
                        SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoods.getTopicName(), erpClientDTO.getSuId() + "", DateUtil.formatDate(new Date()), json);
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            log.error("广西明合药业有限责任公司, 保存商品信息, 消息发送失败, topic:{}, json:{}", ErpTopicName.ErpGoods.getTopicName(), json);
//                            for (ErpGoodsDTO baseErpEntity : goodsList) {
//                                log.error("广西明合药业有限责任公司, 保存商品信息, 消息发送失败, topic:{}, json:{}");
//                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("同步商品信息报错, suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
            e.printStackTrace();
        }
        log.info("同步商品信息结束，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
    }


    private void queryGoodsBatch(ErpClientDTO erpClientDTO) {
        log.info("同步商品库存开始，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
        // 组装请求参数
        String requestBody = buildParams(null);

        try {
            // 查询数据
            JSONArray jsonArray = queryData(goods_batch_url, requestBody, "查询商品库存信息接口");
            if (jsonArray != null && jsonArray.size() > 0) {
                // 保存商品信息
                List<ErpGoodsBatchDTO> goodsBatchList = new ArrayList<>();
                ErpGoodsBatchDTO erpGoodsBatch;
                Date addTime = new Date();
                for (Object o : jsonArray) {
                    JSONObject jsonObj = (JSONObject) o;
                    erpGoodsBatch = new ErpGoodsBatchDTO();
                    buildErpGoodsBatch(erpClientDTO, goodsBatchList, erpGoodsBatch, jsonObj, addTime);
                }
                if (CollUtil.isNotEmpty(goodsBatchList)) {
                    List<List<ErpGoodsBatchDTO>> partition = Lists.partition(goodsBatchList, 500);
                    for (List<ErpGoodsBatchDTO> erpGoodsBatchDTOS : partition) {
                        String json = JSON.toJSONString(erpGoodsBatchDTOS);
                        SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsBatch.getTopicName(), erpClientDTO.getSuId() + "", DateUtil.formatDate(new Date()), json);
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            log.error("广西明合药业有限责任公司, 保存商品库存信息, 消息发送失败, topic:{}, json:{}", ErpTopicName.ErpGoodsBatch.getTopicName(), json);
//                            for (BaseErpEntity baseErpEntity : goodsBatchList) {
//                                log.error("广西明合药业有限责任公司, 保存商品库存信息, 消息发送失败, topic:{}, json:{}");
//                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("同步商品库存报错, suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
            e.printStackTrace();
        }
        log.info("同步商品库存结束，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
    }


    private void queryGoodsGroupPrice(ErpClientDTO erpClientDTO) {
        log.info("同步商品分组定价开始，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
        // 组装请求参数
        String requestBody = buildParams(null);

        try {
            // 查询数据
            JSONArray jsonArray = queryData(goods_group_price_url, requestBody, "查询商品分组定价信息接口");
            if (jsonArray != null && jsonArray.size() > 0) {
                // 保存商品信息
                List<ErpGoodsGroupPriceDTO> goodsGroupPriceList = new ArrayList<>();
                ErpGoodsGroupPriceDTO erpGoodsGroupPrice;
                Date addTime = new Date();
                for (Object o : jsonArray) {
                    JSONObject jsonObj = (JSONObject) o;
                    erpGoodsGroupPrice = new ErpGoodsGroupPriceDTO();
                    buildErpGoodsGroupPrice(erpClientDTO, goodsGroupPriceList, erpGoodsGroupPrice, jsonObj, addTime);
                }
                if (CollUtil.isNotEmpty(goodsGroupPriceList)) {
                    List<List<ErpGoodsGroupPriceDTO>> partition = Lists.partition(goodsGroupPriceList, 500);
                    for (List<ErpGoodsGroupPriceDTO> erpGoodsGroupPriceDTOS : partition) {
                        String json = JSON.toJSONString(erpGoodsGroupPriceDTOS);
                        SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGroupPrice.getTopicName(), erpClientDTO.getSuId() + "", DateUtil.formatDate(new Date()), json);
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            log.error("广西明合药业有限责任公司, 保存商品分组定价信息, 消息发送失败, topic:{}, json:{}", ErpTopicName.ErpGroupPrice.getTopicName(), json);
//                            for (BaseErpEntity baseErpEntity : goodsGroupPriceList) {
//                                log.error("广西明合药业有限责任公司, 保存商品分组定价信息, 消息发送失败, topic:{}, json:{}");
//                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("同步商品分组定价报错, suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
            e.printStackTrace();
        }
        log.info("同步商品分组定价结束，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
    }


    private void queryErpCustomer(ErpClientDTO erpClientDTO) {
        log.info("同步企业客户开始，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
        // 组装请求参数
        String requestBody = buildParams(null);

        try {
            // 查询数据
            JSONArray jsonArray = queryData(customer_url, requestBody, "查询企业客户信息接口");
            if (jsonArray != null && jsonArray.size() > 0) {
                // 保存商品信息
                List<ErpCustomerDTO> erpCustomerList = new ArrayList<>();
                ErpCustomerDTO erpCustomer;
                Date addTime = new Date();
                // 客户去重，key -> su_id + inner_code + su_dept_no
                Set<String> keys = new HashSet<>();
                for (Object o : jsonArray) {
                    JSONObject jsonObj = (JSONObject) o;
                    erpCustomer = new ErpCustomerDTO();
                    buildErpCustomer(erpClientDTO, erpCustomerList, erpCustomer, jsonObj, addTime, keys);
                }
                if (CollUtil.isNotEmpty(erpCustomerList)) {
                    List<List<ErpCustomerDTO>> partition = Lists.partition(erpCustomerList, 500);
                    for (List<ErpCustomerDTO> erpCustomerDTOS : partition) {
                        String json = JSON.toJSONString(erpCustomerDTOS);
                        SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpCustomer.getTopicName(), erpClientDTO.getSuId() + "", DateUtil.formatDate(new Date()), json);
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            log.error("广西明合药业有限责任公司, 保存企业客户信息, 消息发送失败, topic:{}, json:{}", ErpTopicName.ErpCustomer.getTopicName(), json);
//                            for (BaseErpEntity baseErpEntity : erpCustomerList) {
//                                log.error("广西明合药业有限责任公司, 保存企业客户信息, 消息发送失败, topic:{}, json:{}");
//                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("同步企业客户报错, suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
            e.printStackTrace();
        }
        log.info("同步企业客户结束，suId:{}, suDeptNo:{}", erpClientDTO.getSuId(), erpClientDTO.getSuDeptNo());
    }

    private void buildErpCustomer(ErpClientDTO erpClientDTO, List<ErpCustomerDTO> erpCustomerList, ErpCustomerDTO erpCustomer, JSONObject jsonObj, Date addTime, Set<String> keys) {
        Object innerCode = jsonObj.get("inner_code");
        if (ObjectUtil.isNull(innerCode) || StrUtil.isBlank(innerCode.toString())) {
            return;
        }
        // 企业内码去重
        String key = erpClientDTO.getSuId() + "_" + erpClientDTO.getSuDeptNo() + "_" + innerCode.toString();
        if (keys.contains(key)) {
            return;
        }
        keys.add(key);

        erpCustomer.setSuId(erpClientDTO.getSuId());
        erpCustomer.setSuDeptNo(erpClientDTO.getSuDeptNo());
        erpCustomer.setOperType(1);
        erpCustomer.setInnerCode(ObjectUtil.isNotNull(jsonObj.get("inner_code")) ? jsonObj.get("inner_code").toString() : "");
        erpCustomer.setSn(ObjectUtil.isNotNull(jsonObj.get("sn")) ? jsonObj.get("sn").toString() : "");
        erpCustomer.setName(ObjectUtil.isNotNull(jsonObj.get("name")) ? jsonObj.get("name").toString() : "");
        erpCustomer.setGroupName(ObjectUtil.isNotNull(jsonObj.get("group_name")) ? jsonObj.get("group_name").toString() : "");
        erpCustomer.setLicenseNo(ObjectUtil.isNotNull(jsonObj.get("license_no")) ? jsonObj.get("license_no").toString() : "");
        erpCustomer.setCustomerType(ObjectUtil.isNotNull(jsonObj.get("customer_type")) ? jsonObj.get("customer_type").toString() : "");
        erpCustomer.setContact(ObjectUtil.isNotNull(jsonObj.get("contact")) ? jsonObj.get("contact").toString() : "");
        erpCustomer.setPhone(ObjectUtil.isNotNull(jsonObj.get("phone")) ? jsonObj.get("phone").toString() : "");
        erpCustomer.setProvince(ObjectUtil.isNotNull(jsonObj.get("province")) ? jsonObj.get("province").toString() : "");
        erpCustomer.setCity(ObjectUtil.isNotNull(jsonObj.get("city")) ? jsonObj.get("city").toString() : "");
        erpCustomer.setRegion(ObjectUtil.isNotNull(jsonObj.get("region")) ? jsonObj.get("region").toString() : "");
        erpCustomer.setAddress(ObjectUtil.isNotNull(jsonObj.get("address")) ? jsonObj.get("address").toString() : "");
        erpCustomer.setAddTime(addTime);
        erpCustomer.setSyncMsg("查询客户信息接口同步");
        erpCustomerList.add(erpCustomer);
    }

    private void buildErpGoodsGroupPrice(ErpClientDTO erpClientDTO, List<ErpGoodsGroupPriceDTO> goodsGroupPriceList, ErpGoodsGroupPriceDTO erpGoodsGroupPrice, JSONObject jsonObj, Date addTime) {
        String inSn = jsonObj.get("goods_id").toString();
        String groupName = ObjectUtil.isNotNull(jsonObj.get("price_name")) ? jsonObj.get("price_name").toString() : "";
        BigDecimal groupPrice = ObjectUtil.isNotNull(jsonObj.get("price")) ? new BigDecimal(jsonObj.get("price").toString()) : BigDecimal.ZERO;

        erpGoodsGroupPrice.setSuId(erpClientDTO.getSuId());
        erpGoodsGroupPrice.setSuDeptNo(erpClientDTO.getSuDeptNo());
        erpGoodsGroupPrice.setOperType(1);
        erpGoodsGroupPrice.setGgpIdNo(groupName.concat("-").concat(inSn));
        erpGoodsGroupPrice.setInSn(inSn);
        erpGoodsGroupPrice.setGroupName(groupName);
        erpGoodsGroupPrice.setPrice(groupPrice);
        erpGoodsGroupPrice.setAddTime(addTime);
        goodsGroupPriceList.add(erpGoodsGroupPrice);
    }

    private void buildErpGoodsBatch(ErpClientDTO erpClientDTO, List<ErpGoodsBatchDTO> goodsBatchList, ErpGoodsBatchDTO erpGoodsBatch, JSONObject jsonObj, Date addTime) {
        Object batchNo = jsonObj.get("batch_no");
        if (ObjectUtil.isNull(batchNo) || StrUtil.isBlank(batchNo.toString())) {
            return;
        }
        String inSn = jsonObj.get("goods_id").toString();
        String gbBatchNo = batchNo.toString();

        erpGoodsBatch.setSuId(erpClientDTO.getSuId());
        erpGoodsBatch.setSuDeptNo(erpClientDTO.getSuDeptNo());
        erpGoodsBatch.setOperType(1);
        erpGoodsBatch.setInSn(inSn);
        erpGoodsBatch.setGbIdNo(inSn.concat("-").concat(gbBatchNo));
        erpGoodsBatch.setGbBatchNo(gbBatchNo);

        Object produceTime = jsonObj.get("produce_time");
        if (ObjectUtil.isNotNull(produceTime)) {
            erpGoodsBatch.setGbProduceTime(DateUtil.parse(produceTime.toString()));
        }
        Object endTime = jsonObj.get("end_time");
        if (ObjectUtil.isNotNull(endTime)) {
            erpGoodsBatch.setGbEndTime(DateUtil.parse(endTime.toString()));
        }
        erpGoodsBatch.setGbNumber(ObjectUtil.isNotNull(jsonObj.get("stock")) ? new BigDecimal(jsonObj.get("stock").toString()) : BigDecimal.ZERO);
        erpGoodsBatch.setAddTime(addTime);
        goodsBatchList.add(erpGoodsBatch);
    }

    private void buildErpGoods(ErpClientDTO erpClientDTO, List<ErpGoodsDTO> goodsList, ErpGoodsDTO erpGoods, JSONObject jsonObj, Date addTime) {
        Object isOnSale = jsonObj.get("is_on_sale");
        if (ObjectUtil.isNull(isOnSale) || StrUtil.isBlank(isOnSale.toString())) {
            return;
        }
        // 只取上架的
        if (!"1".equals(isOnSale.toString())) {
            return;
        }
        erpGoods.setSuId(erpClientDTO.getSuId());
        erpGoods.setSuDeptNo(erpClientDTO.getSuDeptNo());
        erpGoods.setOperType(1);
        erpGoods.setInSn(jsonObj.get("goods_id").toString());
        erpGoods.setSn(jsonObj.get("goods_id").toString());
        erpGoods.setName(ObjectUtil.isNotNull(jsonObj.get("goods_name")) ? jsonObj.get("goods_name").toString() : "");
        erpGoods.setCommonName(ObjectUtil.isNotNull(jsonObj.get("goods_name")) ? jsonObj.get("goods_name").toString() : "");
        erpGoods.setLicenseNo(ObjectUtil.isNotNull(jsonObj.get("approval")) ? jsonObj.get("approval").toString() : "");
        erpGoods.setSpecifications(ObjectUtil.isNotNull(jsonObj.get("ypgg")) ? jsonObj.get("ypgg").toString() : "");
        erpGoods.setUnit(ObjectUtil.isNotNull(jsonObj.get("unit")) ? jsonObj.get("unit").toString() : "");
        erpGoods.setMiddlePackage(Integer.parseInt(jsonObj.get("midPack").toString()));
        erpGoods.setBigPackage(1);
        erpGoods.setManufacturer(ObjectUtil.isNotNull(jsonObj.get("factory")) ? jsonObj.get("factory").toString() : "");
        erpGoods.setPrice(ObjectUtil.isNotNull(jsonObj.get("price")) ? new BigDecimal(jsonObj.get("price").toString()) : BigDecimal.ZERO);
        erpGoods.setCanSplit(1);
        erpGoods.setGoodsStatus(Integer.parseInt(isOnSale.toString()));
        erpGoods.setAddTime(addTime);
        goodsList.add(erpGoods);
    }

    private JSONArray queryData(String url, String requestBody, String methodName) {
        String returnJson = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").timeout(1000 * 60 * 5).body(requestBody).execute().body();
        if (StrUtil.isBlank(returnJson)) {
            log.warn(">>>>> 广西明合药业有限责任公司, " + methodName + "异常, response is blank");
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(returnJson);
        if (jsonObject == null) {
            log.warn(">>>>> 广西明合药业有限责任公司, " + methodName + "异常, jsonObject is null");
            return null;
        }
        Integer code = jsonObject.getInteger("code");
        if (ObjectUtil.isNull(code)) {
            log.warn(">>>>> 广西明合药业有限责任公司, " + methodName + "异常, response code is null");
            return null;
        }
        if (code.intValue() != 1) {
            String msg = (String) jsonObject.getOrDefault("msg", "");
            log.warn(">>>>> 广西明合药业有限责任公司, " + methodName + "异常, code:{}, msg:{}", code, msg);
            return null;
        }

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray == null || jsonArray.size() == 0) {
            log.warn(">>>>> 广西明合药业有限责任公司, " + methodName + "返回空, response data is empty");
            return null;
        }
        return jsonArray;
    }

    /**
     * 组装请求参数
     *
     * @return
     */
    private String buildParams(Map<String, Object> paramMap) {
        // 当前时间，10位时间戳
        long datetime = System.currentTimeMillis() / 1000;
        // 签名
        String sign = sign(client_id, secret, datetime);

        Map<String, Object> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("datetime", datetime);
        params.put("sign", sign);
        if (MapUtil.isNotEmpty(paramMap)) {
            for (String key : paramMap.keySet()) {
                params.put(key, paramMap.get(key));
            }
        }
        return JSONUtil.toJsonStr(params);
    }


    /**
     * 签名
     *
     * @param clientId
     * @param secret
     * @return
     */
    private String sign(String clientId, String secret, long datetime) {
        // 拼接
        String beforeSign = clientId.concat(secret).concat(datetime + "");
        // MD5加密，32位、小写
        String md532Lower = DigestUtils.md5DigestAsHex(beforeSign.getBytes());
        return md532Lower;
    }

}
