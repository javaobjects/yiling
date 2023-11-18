package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.openapi.third.OpenApi;
import com.kingdee.bos.openapi.third.OpenApiFactory;
import com.kingdee.bos.openapi.third.OpenApiInfo;
import com.kingdee.bos.openapi.third.ctx.CommonLogin;
import com.kingdee.bos.openapi.third.login.EASLoginContext;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.api.InventorySubscriptionApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;
import com.yiling.goods.inventory.dto.request.SaveSubscriptionRequest;
import com.yiling.goods.inventory.enums.InventorySubscriptionStatusEnum;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.request.UpdateShelfLifeRequest;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpGoodsBatchMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.entity.ErpGoodsBatchDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.handler.ErpGoodsBatchHandler;
import com.yiling.open.erp.service.EasIncrementStampService;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpGoodsBatchService;
import com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator;
import com.yiling.open.webservice.json.InventoryJson;
import com.yiling.open.webservice.login.EASLoginProxyServiceLocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author shuan
 */
@Slf4j
@Service(value = "erpGoodsBatchService")
public class ErpGoodsBatchServiceImpl extends ErpEntityServiceImpl implements ErpGoodsBatchService {

    @Resource
    private ErpClientService erpClientService;

    @Autowired
    private ErpGoodsBatchMapper erpGoodsBatchMapper;

    @Autowired
    private ErpGoodsBatchHandler erpGoodsBatchHandler;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private InventoryApi inventoryApi;

    @DubboReference
    private InventorySubscriptionApi inventorySubscriptionApi;

    @Autowired
    EasIncrementStampService easIncrementStampService;

    @Autowired
    private EASLoginProxyServiceLocator easLoginProxyServiceLocator;

    @Autowired
    private WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator wsEasBasicDataWebserviceFacadeSrvProxyServiceLocator;

    @Value("${eas.login.webservice.ip}")
    private String ip;

    @Value("${eas.login.webservice.port}")
    private String port;

    @Value("${eas.login.webservice.userName}")
    private String userName;

    @Value("${eas.login.webservice.password}")
    private String password;

    @Value("${eas.login.webservice.language}")
    private String language;

    @Value("${eas.login.webservice.dcName}")
    private String dcName;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpGoodsBatchDO erpGoodsBatchDO = (ErpGoodsBatchDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpGoodsBatchDO.getSuId(),
                erpGoodsBatchDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatchDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        if (erpGoodsBatchDO.getOperType() == 3) {
            erpGoodsBatchDO = erpGoodsBatchMapper.getById(erpGoodsBatchDO.getId());
            if (erpGoodsBatchDO == null) {
                // 如果是删除商品库存信息，但是查询op库数据为空
                erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatchDO.getId(), 3, "查询库存信息为空");
                return false;
            }
        }
        return synErpGoodsBatch(erpGoodsBatchDO, erpClient);
    }

    @Override
    public void syncGoodsBatch() {
        List<ErpGoodsBatchDO> goodsBatchList = erpGoodsBatchMapper.syncGoodsBatch();
        for (ErpGoodsBatchDO erpGoodsBatch : goodsBatchList) {
            int i = erpGoodsBatchMapper.updateSyncStatusByStatusAndId(erpGoodsBatch.getId(), SyncStatus.SYNCING.getCode(),
                    SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                erpGoodsBatchHandler.onlineData(erpGoodsBatch);
            }
        }
    }

    @Override
    public void refreshErpInventoryList(List<String> inSnList, Long eid) {
        try {
            ErpClientDTO erpClient = erpClientService.selectByRkSuId(eid);

            List<ErpGoodsBatchDO> erpGoodsBatchDOList = erpGoodsBatchMapper.findBySyncStatusAndInSnList(inSnList, erpClient.getSuDeptNo(), erpClient.getSuId());
            if (CollUtil.isEmpty(erpGoodsBatchDOList)) {
                return;
            }

            for (ErpGoodsBatchDO erpGoodsBatchDO : erpGoodsBatchDOList) {
                erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatchDO.getId(), SyncStatus.SYNCING.getCode(), "商品审核重新处理");
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsBatch.getTopicName(), erpClient.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(Arrays.asList(PojoUtils.map(erpGoodsBatchDO, ErpGoodsBatchDTO.class))));
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatchDO.getId(), SyncStatus.UNSYNC.getCode(), "mq发送失败，未处理");
                }
            }
        } catch (Exception e) {
            log.error("ERP商品库存重新刷新报错:", e);
        }
    }

    public boolean synErpGoodsBatch(ErpGoodsBatchDO erpGoodsBatch, ErpClientDTO erpClient) {
        Long id = erpGoodsBatch.getId();
        try {
            ErpGoodsBatchDO tmpErpGoodsBatch = erpGoodsBatchMapper.getById(erpGoodsBatch.getId());

            // 如果数据已经被处理过，则直接返回
            if (tmpErpGoodsBatch.getSyncStatus() == 2) {
                return true;
            }

            //如果是suId=1不用判断有效期是否存在
            if (!judgeGoodsBatchAvailable(erpGoodsBatch)) {
                erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatch.getId(),
                        SyncStatus.FAIL.getCode(), "有效期不能为空");
                return true;
            }

            List<ErpGoodsBatchDO> erpGoodsBatchList = erpGoodsBatchMapper.findByGInSnAndExcludeOperType(erpGoodsBatch.getSuId(), erpGoodsBatch.getInSn(),
                    erpGoodsBatch.getSuDeptNo());

            ErpGoodsBatchDO egbTemp = calculateInventory(erpGoodsBatchList);

            QueryInventorySubscriptionRequest request = new QueryInventorySubscriptionRequest();
            request.setSubscriptionEid(erpClient.getRkSuId().longValue());
            request.setInSnList(Arrays.asList(erpGoodsBatch.getInSn()));
            request.setIsIncludeSelf(true);
            request.setStatus(InventorySubscriptionStatusEnum.NORMAL.getCode());
            request.setSubscriptionType(SubscriptionTypeEnum.ERP.getType());
            List<InventorySubscriptionDTO> inventorySubscriptionDTOList = inventorySubscriptionApi.getInventorySubscriptionList(request);
            if (CollUtil.isEmpty(inventorySubscriptionDTOList)) {
                if (erpGoodsBatch.getOperType() == 3) {
                    erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatch.getId(),
                            SyncStatus.SUCCESS.getCode(), "同步成功");
                    log.warn("平台药品库数据为空，该次请求为删除。data=" + JSON.toJSONString(erpGoodsBatch));
                    return true;
                }
                erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatch.getId(),
                        SyncStatus.FAIL.getCode(), "平台药品库库存订阅数据为空");
                log.warn("平台药品库库存订阅数据为空，data=" + JSON.toJSONString(erpGoodsBatch));
                return true;
            }

            List<ErpGoodsBatchDO> statisticalList = erpGoodsBatchList.stream().filter(erpGoodsBatchDO -> erpGoodsBatchDO.getGbNumber().compareTo(BigDecimal.ZERO) > 0
                    && erpGoodsBatchDO.getOperType() != 3).collect(Collectors.toList());
            //如果不是以岭需要过滤没有有效期的库存信息
            if (erpGoodsBatch.getSuId() != 1) {
                statisticalList = statisticalList.stream().filter(e -> e.getGbEndTime() != null && erpGoodsBatch.getGbEndTime().getTime() > DateUtil.parseDate("1970-01-01 00:00:00").getTime()).collect(Collectors.toList());
                statisticalList = statisticalList.stream().filter(e -> e.getGbProduceTime() != null && erpGoodsBatch.getGbProduceTime().getTime() > DateUtil.parseDate("1970-01-01 00:00:00").getTime()).collect(Collectors.toList());
            }

            BigDecimal gbNumber = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(statisticalList)) {
                for (ErpGoodsBatchDO erpEntity : statisticalList) {
                    if (null != erpEntity.getGbNumber()) {
                        gbNumber = gbNumber.add(erpEntity.getGbNumber());
                    }
                }
            }

            List<SaveSubscriptionRequest> requestList = new ArrayList<>();
            List<Long> inventoryIds = inventorySubscriptionDTOList.stream().map(InventorySubscriptionDTO::getInventoryId).distinct().collect(Collectors.toList());
            List<InventoryDTO> inventoryList = inventoryApi.getInventoryByIds(inventoryIds);
            Map<Long, InventoryDTO> inventoryMap = inventoryList.stream().collect(Collectors.toMap(InventoryDTO::getId, Function.identity()));

            for (InventorySubscriptionDTO inventorySubscriptionDTO : inventorySubscriptionDTOList) {
                SaveSubscriptionRequest saveSubscriptionRequest = new SaveSubscriptionRequest();
                saveSubscriptionRequest.setId(inventorySubscriptionDTO.getId());
                saveSubscriptionRequest.setInventoryId(inventorySubscriptionDTO.getInventoryId());
                saveSubscriptionRequest.setQty(gbNumber.longValue());
                requestList.add(saveSubscriptionRequest);
                if (inventorySubscriptionDTO.getSubscriptionType().equals(SubscriptionTypeEnum.SELF.getType())) {
                    InventoryDTO inventoryDTO = inventoryMap.get(inventorySubscriptionDTO.getInventoryId());
                    if (null != inventoryDTO && egbTemp != null) {
                        UpdateShelfLifeRequest updateShelfLifeRequest = new UpdateShelfLifeRequest();
                        updateShelfLifeRequest.setGoodsId(inventoryDTO.getGid());
                        updateShelfLifeRequest.setExpiryDate(egbTemp.getGbEndTime());
                        updateShelfLifeRequest.setManufacturingDate(egbTemp.getGbProduceTime());
                        updateShelfLifeRequest.setOpUserId(0L);
                        goodsApi.updateShelfLife(updateShelfLifeRequest);
                    }
                }
            }
            inventorySubscriptionApi.batchUpdateSubscriptionQty(requestList);

            for (ErpGoodsBatchDO erpGoodsBatchDO : erpGoodsBatchList) {
                if (erpGoodsBatch != null && !Objects.equals(erpGoodsBatchDO.getSyncStatus(), SyncStatus.SUCCESS.getCode())) {
                    erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatchDO.getId(),
                            SyncStatus.SUCCESS.getCode(), "同步成功");
                }
            }
            return true;
        } catch (Exception e) {
            erpGoodsBatchMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
            log.error("ERP商品库存同步出现错误", e);
        }
        return false;
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpGoodsBatchMapper;
    }

    public boolean judgeGoodsBatchAvailable(ErpGoodsBatchDO erpGoodsBatch) {
        if (erpGoodsBatch.getSuId() == 1) {
            return true;
        }
        if (erpGoodsBatch.getGbEndTime() == null || erpGoodsBatch.getGbEndTime().getTime() <= DateUtil.parseDate("1970-01-01 00:00:00").getTime()) {
            return false;
        }
        if (erpGoodsBatch.getGbProduceTime() == null || erpGoodsBatch.getGbProduceTime().getTime() <= DateUtil.parseDate("1970-01-01 00:00:00").getTime()) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean syncEasGoodsBatch() {
        String date = "1970-01-01 00:00:00";
        try {
            List<String> suIdStr = new ArrayList<>();
            Set<String> insnStr = new HashSet<>();
            //查询以岭的编号
            List<ErpClientDTO> erpClientDTOList = erpClientService.selectBySuId(Constants.YILING_EID);
            erpClientDTOList.addAll(erpClientService.selectBySuId(Constants.DAYUNHE_EID));
            //查询商品信息
            for (ErpClientDTO erpClientDO : erpClientDTOList) {
                if (StrUtil.isNotEmpty(erpClientDO.getSuDeptNo())) {
                    suIdStr.add(erpClientDO.getSuDeptNo());
                }
                List<GoodsDTO> goodsInfoDTOList = goodsApi.getGoodsListByEid(erpClientDO.getRkSuId().longValue());

                List<Long> goodsIds = goodsInfoDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
                insnStr.addAll(goodsSkuDTOList.stream().filter(e -> StrUtil.isNotEmpty(e.getInSn())).map(e -> e.getInSn()).collect(Collectors.toList()));
            }

            execute(suIdStr, date, new ArrayList<>(insnStr));

        } catch (Exception e) {
            log.error("同步eas库存接口报错{}", e);
        }
        return true;
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsBatchMapper.updateOperTypeGoodsBatchFlowBySuId(suId);
    }


    public void execute(List<String> suIdStr, String date, List<String> sourList) {
        try {
            for (String suSeptNo : suIdStr) {
                if (StrUtil.isNotEmpty(suSeptNo)) {
                    EASLoginContext loginCtx = new EASLoginContext.Builder(ip, Integer.parseInt(port),
                            new CommonLogin.Builder(userName, password, dcName, language)
                                    .build()).https(false)
                            .build();
                    OpenApiInfo info = new OpenApiInfo();
                    //调用api的方法名
                    info.setApi("EasBasicDataWebserviceFacade-InventoryQuery");
                    //调用参数，格式是数组形式
                    log.info("eas执行查询时间点{}", date);

                    info.setData("[\"[" + suSeptNo + "]\",\"" + date + "\",\"" + Arrays.toString(sourList.toArray()).replaceAll(" ", "") + "\"]");
                    OpenApi openApi = OpenApiFactory.getService(loginCtx);
                    //返回数据
                    String result = openApi.invoke(info);
                    log.info("eas返回数据{}", result);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject.getInteger("resultCode").equals(200)) {
                        List<InventoryJson> list = JSON.parseArray(jsonObject.getString("body"), InventoryJson.class);
                        // 保存去重后的请求数据
                        Map<String, BaseErpEntity> dataMap = new LinkedHashMap<>();
                        //汇总库存信息
                        Map<String, BigDecimal> goodsMap = new HashMap<>();
                        for (InventoryJson inventoryJson : list) {
                            BigDecimal lockQty = BigDecimal.ZERO;
                            if (StrUtil.isNotEmpty(inventoryJson.getLockqty())) {
                                lockQty = new BigDecimal(inventoryJson.getLockqty());
                            }
                            BigDecimal reservationbaseqty = BigDecimal.ZERO;
                            if (StrUtil.isNotEmpty(inventoryJson.getReservationbaseQty())) {
                                reservationbaseqty = new BigDecimal(inventoryJson.getReservationbaseQty());
                            }
                            BigDecimal qty = new BigDecimal(inventoryJson.getCurstoreQty()).subtract(lockQty).subtract(reservationbaseqty);
                            if (qty.compareTo(BigDecimal.ZERO) > 0 && isAvailable(inventoryJson, suSeptNo)) {
                                if (goodsMap.containsKey(inventoryJson.getMatNumber())) {
                                    BigDecimal number = goodsMap.get(inventoryJson.getMatNumber()).add(qty);
                                    goodsMap.put(inventoryJson.getMatNumber(), number);
                                } else {
                                    goodsMap.put(inventoryJson.getMatNumber(), qty);
                                }
                            }
                        }

                        for (String inSn : sourList) {
                            ErpGoodsBatchDTO erpGoodsBatch = new ErpGoodsBatchDTO();
                            erpGoodsBatch.setGbNumber(goodsMap.getOrDefault(inSn, BigDecimal.ZERO));
                            erpGoodsBatch.setInSn(inSn);
                            erpGoodsBatch.setSuDeptNo(suSeptNo);
                            erpGoodsBatch.setGbIdNo(suSeptNo + "-" + inSn);
                            erpGoodsBatch.setGbBatchNo("");
                            erpGoodsBatch.setSuId(Constants.YILING_EID);
                            erpGoodsBatch.setOperType(1);
                            dataMap.put(erpGoodsBatch.getErpPrimaryKey(), erpGoodsBatch);
                        }

                        //判断topic是否存在
                        if (!CollectionUtils.isEmpty(dataMap.values())) {
                            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsBatch.getTopicName(), Constants.YILING_EID + "", DateUtil.formatDate(new Date()), JSON.toJSONString(dataMap.values()));
                            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            } else {
                                log.info("同步eas库存接口同步完成，data={}", JSON.toJSONString(dataMap.values()));
                            }
                        }
                    } else {
                        log.error("同步eas库存接口报错{}", result);
                    }
                }
            }
        } catch (Exception e) {
            log.error("同步eas库存接口报错{}", e);
        }
    }

    public boolean isAvailable(InventoryJson inventoryJson, String suSeptNo) {
        if (suSeptNo.equals("04.01") && inventoryJson.getWareHouseNumber().equals("040103")) {
            return true;
        } else if (suSeptNo.equals("01") && inventoryJson.getWareHouseNumber().equals("0103")) {
            return true;
        }
        return false;
    }


    /**
     * 最近效期，批次(库存批次需要判断库存大于零的批次才有效)
     */
    public ErpGoodsBatchDO calculateInventory(List<ErpGoodsBatchDO> erpGoodsBatchList) {
        ErpGoodsBatchDO egbTemp = new ErpGoodsBatchDO();
        //最近批次号
        String gbBatchNo = null;
        //最近的效期
        Date gbEndTime = null;
        //最近的效期对应的生产日期
        Date gbProduceTime = null;

        Date now = new Date();
        for (ErpGoodsBatchDO erpGoodsBatch : erpGoodsBatchList) {
            //goodsBatchBoolean等于true，计算库存批次需要判断库存大于零的批次才有效
            BigDecimal tmpGbNumberBig = erpGoodsBatch.getGbNumber();
            if (tmpGbNumberBig == null || tmpGbNumberBig.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (erpGoodsBatch.getOperType() != null && erpGoodsBatch.getOperType() != 3) {
                Date tmpGbEndTime = erpGoodsBatch.getGbEndTime();
                //计算最近的最小效期
                if (tmpGbEndTime != null && now.before(tmpGbEndTime)) {
                    if (gbEndTime == null) {
                        gbEndTime = tmpGbEndTime;
                        gbBatchNo = erpGoodsBatch.getGbBatchNo();
                        gbProduceTime = erpGoodsBatch.getGbProduceTime();
                    } else if (tmpGbEndTime.before(gbEndTime)) {
                        gbEndTime = tmpGbEndTime;
                        gbBatchNo = erpGoodsBatch.getGbBatchNo();
                        gbProduceTime = erpGoodsBatch.getGbProduceTime();
                    }
                }
            }
        }
        if (gbEndTime != null) {
            Date maxTimestamp = this.getMaxTimestamp();
            if (gbEndTime.after(maxTimestamp)) {
                gbEndTime = maxTimestamp;
            }
        }
        egbTemp.setGbBatchNo(gbBatchNo);
        egbTemp.setGbEndTime(gbEndTime);
        egbTemp.setGbProduceTime(gbProduceTime);
        return egbTemp;
    }

    public Date getMaxTimestamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(2037, Calendar.DECEMBER, 31, 23, 59, 59);
        return cal.getTime();
    }

}
