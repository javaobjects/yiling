package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDTO;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchRequest;
import com.yiling.dataflow.order.enums.FlowGoodsBatchStatisticsStatusEnum;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpGoodsBatchFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;
import com.yiling.open.erp.entity.ErpGoodsBatchFlowDO;
import com.yiling.open.erp.enums.FlowGoodsBatchOrderSourceEnum;
import com.yiling.open.erp.enums.FlowPurchaseOrderSourceEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.handler.ErpGoodsBatchFlowHandler;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;
import com.yiling.open.erp.service.ErpGoodsBatchFlowService;
import com.yiling.open.erp.service.ErpSaleFlowService;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.open.erp.util.OpenStringUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存流向服务实现类
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Slf4j
@Service(value = "erpGoodsBatchFlowService")
public class ErpGoodsBatchFlowServiceImpl extends ErpEntityServiceImpl implements ErpGoodsBatchFlowService {

    @Autowired
    private   ErpGoodsBatchFlowMapper   erpGoodsBatchFlowMapper;
    @Autowired
    private   ErpClientService          erpClientService;
    @Autowired
    private   ErpFlowGoodsConfigService erpFlowGoodsConfigService;
    @Autowired
    private   ErpGoodsBatchFlowHandler  erpGoodsBatchFlowHandler;
    @Autowired(required = false)
    private   RocketMqProducerService   rocketMqProducerService;
    @Autowired
    protected RedisDistributedLock      redisDistributedLock;
    @Autowired
    private   ErpSaleFlowService        erpSaleFlowService;
    @DubboReference
    private   GoodsApi                  goodsApi;
    @DubboReference
    private   EnterpriseApi             enterpriseApi;
    @DubboReference
    private   InventoryApi              inventoryApi;
    @DubboReference
    private   FlowGoodsBatchApi         flowGoodsBatchApi;
    @DubboReference(timeout = 10 * 1000)
    private   FlowPurchaseApi           flowPurchaseApi;
    @DubboReference(timeout = 10 * 1000)
    private   FlowGoodsRelationApi      flowGoodsRelationApi;
    @DubboReference
    private   FlowGoodsSpecMappingApi   flowGoodsSpecMappingApi;
    @DubboReference
    private CrmEnterpriseApi        crmEnterpriseApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Lazy
    ErpGoodsBatchFlowServiceImpl _this;
    @DubboReference
    MqMessageSendApi             mqMessageSendApi;

    private static final long PURCHASE_SOURCE_EXPIRE = 5;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpGoodsBatchFlowDO erpGoodsBatchFlowDO = (ErpGoodsBatchFlowDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpGoodsBatchFlowDO.getSuId(),
                erpGoodsBatchFlowDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpGoodsBatchFlowMapper.updateSyncStatusAndMsg(erpGoodsBatchFlowDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }

        if (erpGoodsBatchFlowDO.getOperType() == 3) {
            erpGoodsBatchFlowDO = erpGoodsBatchFlowMapper.selectById(erpGoodsBatchFlowDO.getId());
            if (erpGoodsBatchFlowDO == null) {
                // 如果是删除商品库存信息，但是查询op库数据为空
                erpGoodsBatchFlowMapper.updateSyncStatusAndMsg(erpGoodsBatchFlowDO.getId(), 3, "查询流向库存信息为空");
                return false;
            }
        } else {
            // 校验药品内码
            String inSn = erpGoodsBatchFlowDO.getInSn();
            if (StrUtil.isBlank(inSn)) {
                erpGoodsBatchFlowMapper.updateSyncStatusAndMsg(erpGoodsBatchFlowDO.getId(), 3, "同步失败：in_sn 药品内码为空");
                return false;
            }
        }

        return synErpGoodsBatchFlow(erpGoodsBatchFlowDO, erpClient);
    }

    @Override
    public void syncGoodsBatchFlow() {
        List<ErpGoodsBatchFlowDO> goodsBatchList = erpGoodsBatchFlowMapper.syncGoodsBatchFlow();
        for (ErpGoodsBatchFlowDO erpGoodsBatchFlow : goodsBatchList) {
            int i = erpGoodsBatchFlowMapper.updateSyncStatusByStatusAndId(erpGoodsBatchFlow.getId(), SyncStatus.SYNCING.getCode(),
                    SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                erpGoodsBatchFlowHandler.onlineData(erpGoodsBatchFlow);
            }
        }
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsBatchFlowMapper.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

    public boolean synErpGoodsBatchFlow(ErpGoodsBatchFlowDO erpGoodsBatchFlowDO, ErpClientDTO erpClient) {
        Long id = erpGoodsBatchFlowDO.getId();
        try {
            // 流向非以岭商品配置
            ErpFlowGoodsConfigDO erpFlowGoodsConfigDO = erpFlowGoodsConfigService.getByEidAndGoodsInSn(erpClient.getRkSuId(), erpGoodsBatchFlowDO.getInSn());

            if (erpGoodsBatchFlowDO.getGbManufacturer().contains(ErpConstants.YI_LING) || ObjectUtil.isNotNull(erpFlowGoodsConfigDO)) {
                // 只同步生产厂家包含以岭、或有配置流向非以岭商品的
                syncYilingGoodsBatchFlow(erpGoodsBatchFlowDO, erpClient);
            } else {
                //封存流向
                erpGoodsBatchFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "生产厂家不包含以岭、且未配置此流向非以岭商品");
            }
            return true;
        } catch (Exception e) {
            erpGoodsBatchFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
            log.error("库存流向同步出现错误", e);
        }
        return false;
    }

    private void syncYilingGoodsBatchFlow(ErpGoodsBatchFlowDO erpGoodsBatchFlowDO, ErpClientDTO erpClient) {
        long eid = erpClient.getRkSuId().longValue();
        QueryFlowGoodsBatchRequest flowGoodsBatchRequest = new QueryFlowGoodsBatchRequest();
        flowGoodsBatchRequest.setEid(eid);
        flowGoodsBatchRequest.setGbIdNo(erpGoodsBatchFlowDO.getGbIdNo());
        List<FlowGoodsBatchDTO> oldFlowGoodsBatchList = flowGoodsBatchApi.getFlowGoodsBatchDTOByGbIdNoAndEid(flowGoodsBatchRequest);

        if (erpGoodsBatchFlowDO.getOperType() == 3) {
            //删除逻辑
            if (CollUtil.isNotEmpty(oldFlowGoodsBatchList)) {
                List<Long> ids = oldFlowGoodsBatchList.stream().map(FlowGoodsBatchDTO::getId).distinct().collect(Collectors.toList());
                flowGoodsBatchApi.deleteByIdList(ids);
            }
        } else {
            //修改逻辑
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(erpClient.getRkSuId().longValue());
            //匹配商业公司crm编码
            CrmEnterpriseDTO crmEnterpriseDTO = null;
            CrmSupplierDTO crmSupplierDTO = null;
            if (erpClient.getCrmEnterpriseId() != null && erpClient.getCrmEnterpriseId() != 0) {
                crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(erpClient.getCrmEnterpriseId());
                //  匹配经销商级别
                crmSupplierDTO = crmSupplierApi.getCrmSupplierByCrmEnterId(crmEnterpriseDTO.getId());
            }

            QueryFlowGoodsBatchRequest request = new QueryFlowGoodsBatchRequest();
            request.setEid(erpClient.getRkSuId().longValue());
            request.setGbIdNo(erpGoodsBatchFlowDO.getGbIdNo());
            List<FlowGoodsBatchDTO> flowGoodsBatchDTOList = flowGoodsBatchApi.getFlowGoodsBatchDTOByGbIdNoAndEid(request);
            SaveOrUpdateFlowGoodsBatchRequest updateRequest = PojoUtils.map(erpGoodsBatchFlowDO, SaveOrUpdateFlowGoodsBatchRequest.class);
            updateRequest.setOpUserId(0L);

            // 订单来源，匹配采购流向的订单来源
            String source = buildSource(erpClient.getRkSuId(), erpGoodsBatchFlowDO.getInSn());
            updateRequest.setGbSource(source);

            // 匹配以岭品关系
            getYlGoodsId(erpClient, erpGoodsBatchFlowDO);

            // 匹配商品+规格id
            updateRequest.setSpecificationId(this.mathGoodsSpec(updateRequest.getGbName(), updateRequest.getGbSpecifications(), updateRequest.getGbManufacturer()));
            if (crmEnterpriseDTO != null) {
                updateRequest.setCrmGoodsCode(erpSaleFlowService.mathGoodsCrmCode(updateRequest.getGbName(), updateRequest.getGbSpecifications(), updateRequest.getGbManufacturer(), updateRequest.getGbUnit(), updateRequest.getInSn(), crmEnterpriseDTO));
            }

            if (CollUtil.isNotEmpty(flowGoodsBatchDTOList)) {
                if (crmEnterpriseDTO != null) {
                    updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                    updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                    updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                    updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                    updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                    updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                }
                if (crmSupplierDTO != null) {
                    updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                }
                updateRequest.setEid(enterpriseDTO.getId());
                updateRequest.setEname(enterpriseDTO.getName());
                updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                updateRequest.setStatisticsStatus(FlowGoodsBatchStatisticsStatusEnum.TODO.getCode());
                updateRequest.setOpUserId(0L);
                updateRequest.setOpTime(new Date());
                for (FlowGoodsBatchDTO flowGoodsBatch : flowGoodsBatchDTOList) {
                    updateRequest.setId(flowGoodsBatch.getId());
                    flowGoodsBatchApi.updateFlowGoodsBatchById(updateRequest);
                }
            } else {
                //新增
                Integer cnt = erpGoodsBatchFlowDO.getCnt();
                for (int i = 0; i < cnt; i++) {
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                        updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                        updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                        updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                        updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                        updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                    }
                    if (crmSupplierDTO != null) {
                        updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                    }
                    updateRequest.setEid(enterpriseDTO.getId());
                    updateRequest.setEname(enterpriseDTO.getName());
                    updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                    updateRequest.setStatisticsStatus(FlowGoodsBatchStatisticsStatusEnum.TODO.getCode());
                    flowGoodsBatchApi.insertFlowGoodsBatch(updateRequest);
                }
            }
        }
        //重新统计数据，先查询商品内码,统计维度到规格
//        statisticsFlowGoodsBatchTotalNumber(erpGoodsBatchFlowDO, erpClient);
        //封存流向
        erpGoodsBatchFlowMapper.updateSyncStatusAndMsg(erpGoodsBatchFlowDO.getId(), SyncStatus.SUCCESS.getCode(), "同步成功");
    }

    private String buildSource(Long eid, String goodsInSn) {
        String source = FlowGoodsBatchOrderSourceEnum.OTHERS.getCode();
        String key = RedisKey.generate("mph-erp-online-lock-flow-goods-batch", "purchaseSource", eid.toString(), goodsInSn);
        String sourceCache = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(sourceCache)) {
            return sourceCache;
        } else {
            QueryFlowPurchaseByGoodsInSnRequest flowPurchaseRequest = new QueryFlowPurchaseByGoodsInSnRequest();
            flowPurchaseRequest.setEid(eid);
            flowPurchaseRequest.setGoodsInSn(goodsInSn);
            List<String> poSourceList = flowPurchaseApi.getByEidAndGoodsInSn(flowPurchaseRequest);
            if (CollUtil.isEmpty(poSourceList)) {
                return source;
            }
            // 有一个订单来源匹配就是POP
            boolean poSourceFlag = poSourceList.stream().anyMatch(o -> ObjectUtil.equal(FlowPurchaseOrderSourceEnum.POP.getCode(), OpenStringUtils.clearAllSpace(o)));
            if (poSourceFlag) {
                source = FlowGoodsBatchOrderSourceEnum.POP.getCode();
                stringRedisTemplate.opsForValue().set(key, source, PURCHASE_SOURCE_EXPIRE, TimeUnit.MINUTES);
                return source;
            }
        }
        return source;
    }

    /**
     * 按规格统计库存流向的库存总数
     *
     * @param erpGoodsBatchFlowDO
     * @param erpClient
     */
    private void statisticsFlowGoodsBatchTotalNumber(ErpGoodsBatchFlowDO erpGoodsBatchFlowDO, ErpClientDO erpClient) {
        QueryFlowGoodsBatchRequest request = new QueryFlowGoodsBatchRequest();
        request.setInSn(erpGoodsBatchFlowDO.getInSn());
        request.setEid(erpClient.getRkSuId().longValue());
        List<FlowGoodsBatchDTO> flowGoodsBatchDTOList = flowGoodsBatchApi.getFlowGoodsBatchDTOByInSnAndEid(request);
        if (CollUtil.isEmpty(flowGoodsBatchDTOList)) {
            return;
        }
        List<FlowGoodsBatchDTO> flowGoodsBatchList = flowGoodsBatchDTOList.stream().filter(o -> ObjectUtil.isNotNull(o.getGbSpecifications()) && ObjectUtil.equal(o.getGbSpecifications(), erpGoodsBatchFlowDO.getGbSpecifications())).collect(Collectors.toList());
        if (CollUtil.isEmpty(flowGoodsBatchList)) {
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (FlowGoodsBatchDTO flowGoodsBatchDTO : flowGoodsBatchList) {
            total = total.add(flowGoodsBatchDTO.getGbNumber());
        }
        List<Long> flowGoodsBatchIdList = flowGoodsBatchList.stream().map(FlowGoodsBatchDTO::getId).distinct().collect(Collectors.toList());
        flowGoodsBatchApi.updateFlowGoodsBatchTotalNumberByIdList(flowGoodsBatchIdList, total);
    }

    private void getYlGoodsId(ErpClientDTO erpClient, ErpGoodsBatchFlowDO erpGoodsBatchFlowDO) {

        if (ObjectUtil.isNotNull(erpClient) && ObjectUtil.isNotNull(erpGoodsBatchFlowDO) &&
                ObjectUtil.isNotNull(erpClient.getRkSuId()) && StrUtil.isNotBlank(erpClient.getClientName()) && StrUtil.isNotBlank(erpGoodsBatchFlowDO.getInSn())
                && StrUtil.isNotBlank(erpGoodsBatchFlowDO.getGbName()) && StrUtil.isNotBlank(erpGoodsBatchFlowDO.getGbSpecifications())) {
            QueryFlowGoodsRelationYlGoodsIdRequest ylGoodsIdRequest = new QueryFlowGoodsRelationYlGoodsIdRequest();
            ylGoodsIdRequest.setEid(erpClient.getRkSuId());
            ylGoodsIdRequest.setEname(erpClient.getClientName());
            ylGoodsIdRequest.setGoodsInSn(erpGoodsBatchFlowDO.getInSn());
            ylGoodsIdRequest.setGoodsName(erpGoodsBatchFlowDO.getGbName());
            ylGoodsIdRequest.setGoodsSpecifications(erpGoodsBatchFlowDO.getGbSpecifications());
            MqMessageBO mqMessageBO = _this.sendPrepare(Constants.TOPIC_ERP_FLOW_GOODS_RELATION_SAVEORUPDATE, Constants.TAG_ERP_FLOW_GOODS_RELATION_SAVEORUPDATE, JSON.toJSONString(ylGoodsIdRequest));
            mqMessageSendApi.send(mqMessageBO);
        }
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

    private Long mathGoodsSpec(String goodsName, String spec, String manufacturer) {
        if (StrUtil.isEmpty(goodsName.trim()) || StrUtil.isEmpty(spec.trim())) {
            return 0L;
        }
        goodsName = goodsName.trim();
        spec = spec.trim();
        manufacturer = manufacturer.trim();
        String lockName = StrUtil.format("mathGoodsSpec_{}_{}_{}", goodsName, spec, manufacturer);
        Long specificationId = 0L;
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 3, 3, TimeUnit.SECONDS);
            FlowGoodsSpecMappingDTO flowGoodsSpecMappingDTO = flowGoodsSpecMappingApi.findByGoodsNameAndSpec(goodsName, spec, manufacturer);
            if (flowGoodsSpecMappingDTO != null) {
                specificationId = flowGoodsSpecMappingDTO.getSpecificationId();
            } else {
                SaveFlowGoodsSpecMappingRequest request = new SaveFlowGoodsSpecMappingRequest();
                request.setGoodsName(goodsName);
                request.setSpec(spec);
                request.setManufacturer(manufacturer);
                request.setOpUserId(0L);
                request.setOpTime(new Date());
                flowGoodsSpecMappingApi.saveFlowGoodsSpecMapping(request);
            }
        } catch (Exception e) {
            log.warn("获取锁超时", e);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return specificationId;
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpGoodsBatchFlowMapper;
    }
}
