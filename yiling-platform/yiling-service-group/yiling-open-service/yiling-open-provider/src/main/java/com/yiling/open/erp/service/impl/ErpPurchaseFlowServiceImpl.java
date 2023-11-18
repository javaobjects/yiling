package com.yiling.open.erp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseSupplierMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;
import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowPurchaseRequest;
import com.yiling.dataflow.relation.api.FlowGoodsPriceRelationApi;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.dataflow.relation.api.FlowSupplierMappingApi;
import com.yiling.dataflow.relation.dto.FlowSupplierMappingDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowSupplierMappingRequest;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.backup.util.BackupUtil;
import com.yiling.open.config.ErpFlowOrderSourceConfig;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpPurchaseFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;
import com.yiling.open.erp.entity.ErpFlowSealedDO;
import com.yiling.open.erp.entity.ErpPurchaseFlowDO;
import com.yiling.open.erp.enums.DataTagEnum;
import com.yiling.open.erp.enums.ErpFlowSealedStatusEnum;
import com.yiling.open.erp.enums.ErpFlowSealedTypeEnum;
import com.yiling.open.erp.enums.FlowPurchaseInventoryEnterpriseEnum;
import com.yiling.open.erp.enums.FlowPurchaseOrderSourceEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;
import com.yiling.open.erp.service.ErpFlowSealedService;
import com.yiling.open.erp.service.ErpPurchaseFlowAsyncService;
import com.yiling.open.erp.service.ErpPurchaseFlowService;
import com.yiling.open.erp.service.ErpSaleFlowService;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.open.erp.util.OpenStringUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@Slf4j
@Service(value = "erpPurchaseFlowService")
public class ErpPurchaseFlowServiceImpl extends ErpEntityServiceImpl implements ErpPurchaseFlowService {

    @DubboReference
    private FlowPurchaseApi           flowPurchaseApi;
    @DubboReference
    private EnterpriseApi             enterpriseApi;
    @DubboReference(timeout = 10 * 1000)
    private FlowGoodsRelationApi      flowGoodsRelationApi;
    @DubboReference
    private FlowPurchaseInventoryApi  flowPurchaseInventoryApi;
    @DubboReference
    private FlowGoodsPriceRelationApi flowGoodsPriceRelationApi;
    @DubboReference
    private FlowGoodsSpecMappingApi   flowGoodsSpecMappingApi;
    @DubboReference
    private FlowSupplierMappingApi    flowSupplierMappingApi;
    @DubboReference
    private CrmEnterpriseApi          crmEnterpriseApi;
    @DubboReference
    private FlowEnterpriseSupplierMappingApi flowEnterpriseSupplierMappingApi;

    @DubboReference
    private CrmSupplierApi crmSupplierApi;

    @Autowired
    protected RedisDistributedLock        redisDistributedLock;
    @Autowired
    private   ErpPurchaseFlowMapper       erpPurchaseFlowMapper;
    @Autowired
    private   ErpClientService            erpClientService;
    @Autowired
    private   ErpFlowOrderSourceConfig    erpFlowOrderSourceConfig;
    @Autowired
    private   ErpFlowGoodsConfigService   erpFlowGoodsConfigService;
    @Autowired
    private   ErpFlowSealedService        erpFlowSealedService;
    @Autowired
    private   ErpPurchaseFlowAsyncService erpPurchaseFlowAsyncService;
    @Autowired
    private   ErpSaleFlowService          erpSaleFlowService;
    @Autowired
    @Lazy
    ErpPurchaseFlowServiceImpl _this;
    @DubboReference
    MqMessageSendApi           mqMessageSendApi;


    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpPurchaseFlowMapper;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpPurchaseFlowDO erpPurchaseFlowDO = (ErpPurchaseFlowDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpPurchaseFlowDO.getSuId(), erpPurchaseFlowDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpPurchaseFlowMapper.updateSyncStatusAndMsg(erpPurchaseFlowDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        // 采购时间校验，采购时间不能超过6个月
        if (ObjectUtil.isNotNull(erpClient.getDataInitStatus()) && ObjectUtil.equal(1, erpClient.getDataInitStatus())) {
            // 初始化状态是已完成，则限制同步的采购时间为6个月以内
            // 采购时间
            Date poTime = erpPurchaseFlowDO.getPoTime();
            // 包含当月向前推6个月之前的月份，推前的第7个月及之前的数据为限制同步的
            String monthBackup6 = BackupUtil.monthBackup(6);
            if (DateUtil.beginOfMonth(poTime).getTime() <= DateUtil.beginOfMonth(DateUtil.parse(monthBackup6, "yyyy-MM")).getTime()) {
                erpPurchaseFlowMapper.updateSyncStatusAndMsg(erpPurchaseFlowDO.getId(), SyncStatus.FAIL.getCode(), "采购时间已超过6个月，不能同步");
                return false;
            }
        }

        if (erpPurchaseFlowDO.getOperType() == 3) {
            Long id = erpPurchaseFlowDO.getId();
            erpPurchaseFlowDO = erpPurchaseFlowMapper.selectById(id);
            if (erpPurchaseFlowDO == null) {
                // 如果是删除商品库存信息，但是查询op库数据为空
                erpPurchaseFlowMapper.updateSyncStatusAndMsg(id, 3, "查询采购流向信息为空");
                return false;
            }
        }
        return synErpPurchaseFlow(erpPurchaseFlowDO, erpClient);
    }

    @Override
    public void synPurchaseFlow() {
        List<ErpPurchaseFlowDO> erpPurchaseFlowList = erpPurchaseFlowMapper.syncPurchaseFlow();
        for (ErpPurchaseFlowDO erpPurchaseFlowDO : erpPurchaseFlowList) {
            int i = erpPurchaseFlowMapper.updateSyncStatusByStatusAndId(erpPurchaseFlowDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpPurchaseFlowDO);
            }
        }
    }

    @Override
    public void unLockSynPurchaseFlow(ErpFlowSealedUnLockRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getRkSuId()) || StrUtil.isBlank(request.getStartMonth()) || StrUtil.isBlank(request.getEndMonth())) {
            return;
        }
        // 删除线上数据
        DeleteFlowPurchaseByUnlockRequest deleteFlowPurchaseRequest = new DeleteFlowPurchaseByUnlockRequest();
        deleteFlowPurchaseRequest.setEid(request.getRkSuId());
        deleteFlowPurchaseRequest.setStartMonth(request.getStartMonth());
        deleteFlowPurchaseRequest.setEndMonth(request.getEndMonth());
        flowPurchaseApi.deleteFlowPurchaseBydEidAndPoTime(deleteFlowPurchaseRequest);
        // 同步OP库数据
        List<ErpPurchaseFlowDO> erpPurchaseFlowList = erpPurchaseFlowMapper.unLockSynPurchaseFlow(request);
        if (CollUtil.isEmpty(erpPurchaseFlowList)) {
            return;
        }
        for (ErpPurchaseFlowDO erpPurchaseFlowDO : erpPurchaseFlowList) {
            int i = erpPurchaseFlowMapper.updateSyncStatusByStatusAndId(erpPurchaseFlowDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.SUCCESS.getCode(), "采购流向解封处理");
            if (i > 0) {
                onlineData(erpPurchaseFlowDO);
            }
        }
    }

    public boolean synErpPurchaseFlow(ErpPurchaseFlowDO erpPurchaseFlowDO, ErpClientDTO erpClient) {
        Long id = erpPurchaseFlowDO.getId();
        try {
            // 已封存采购流向
            if (checkErpFlowSealedLock(erpPurchaseFlowDO, erpClient)) {
                erpPurchaseFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功：此月份线上采购流向已封存");
                return true;
            }

            // 流向非以岭商品配置
            ErpFlowGoodsConfigDO erpFlowGoodsConfigDO = erpFlowGoodsConfigService.getByEidAndGoodsInSn(erpClient.getRkSuId(), erpPurchaseFlowDO.getGoodsInSn());

            if (erpPurchaseFlowDO.getPoManufacturer().contains(ErpConstants.YI_LING) || ObjectUtil.isNotNull(erpFlowGoodsConfigDO)) {
                // 只同步生产厂家包含以岭、或有配置流向非以岭商品的
                syncYilingPurchaseFlow(erpPurchaseFlowDO, erpClient);
                // 同步完成
                erpPurchaseFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功");
            } else {
                // 同步失败
                erpPurchaseFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "生产厂家不包含以岭、且未配置此流向非以岭商品");
            }
            return true;
        } catch (Exception e) {
            log.error("[采购流向]同步出现错误", e);
            erpPurchaseFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
        }
        return false;
    }

    private boolean checkErpFlowSealedLock(ErpPurchaseFlowDO erpPurchaseFlowDO, ErpClientDTO erpClient) {
        QueryErpFlowSealedRequest requerst = new QueryErpFlowSealedRequest();
        requerst.setEid(erpClient.getRkSuId());
        requerst.setType(ErpFlowSealedTypeEnum.PURCHASE.getCode());
        requerst.setMonth(DateUtil.format(erpPurchaseFlowDO.getPoTime(), "yyyy-MM"));
        ErpFlowSealedDO erpFlowSealed = erpFlowSealedService.getErpFlowSealedByEidAndTypeAndMonth(requerst);
        if (ObjectUtil.isNotNull(erpFlowSealed) && ObjectUtil.equal(ErpFlowSealedStatusEnum.LOCK.getCode(), erpFlowSealed.getStatus())) {
            return true;
        }
        return false;
    }

    private void syncYilingPurchaseFlow(ErpPurchaseFlowDO erpPurchaseFlowDO, ErpClientDTO erpClient) {
        int operType;
        // 线上变更前数据
        QueryFlowPurchaseRequest request = new QueryFlowPurchaseRequest();
        request.setEid(erpClient.getRkSuId().longValue());
        request.setPoId(erpPurchaseFlowDO.getPoId());
        List<FlowPurchaseDTO> oldFlowPurchaseList = flowPurchaseApi.getFlowPurchaseDTOByPoIdAndEid(request);
        // 线上变更后数据
        List<FlowPurchaseDTO> newFlowPurchaseList = new ArrayList<>();

        if (erpPurchaseFlowDO.getOperType() == 3) {
            operType = 3;
            //删除逻辑
            if (CollUtil.isNotEmpty(oldFlowPurchaseList)) {
                List<Long> ids = oldFlowPurchaseList.stream().map(FlowPurchaseDTO::getId).distinct().collect(Collectors.toList());
                // 洞察系统日流向查询中，也会查软删除的数据，这里先修改数据标签，再软删除
                flowPurchaseApi.updateDataTagByIdList(ids, DataTagEnum.DELETE.getCode());
                flowPurchaseApi.deleteByIdList(ids);
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

            SaveOrUpdateFlowPurchaseRequest updateRequest = PojoUtils.map(erpPurchaseFlowDO, SaveOrUpdateFlowPurchaseRequest.class);
            updateRequest.setOpUserId(0L);
            // 订单来源
            buildOrderSource(erpPurchaseFlowDO, updateRequest);


            // 匹配以岭品关系
            getYlGoodsId(erpClient, erpPurchaseFlowDO);

            //  设置购进月份
            Date poTime = updateRequest.getPoTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            updateRequest.setPoMonth(sdf.format(poTime));

            // 匹配商品+规格id
            updateRequest.setSpecificationId(mathGoodsSpec(updateRequest.getGoodsName(), updateRequest.getPoSpecifications(), updateRequest.getPoManufacturer()));
            if (crmEnterpriseDTO != null) {
                updateRequest.setCrmGoodsCode(erpSaleFlowService.mathGoodsCrmCode(updateRequest.getGoodsName(), updateRequest.getPoSpecifications(), updateRequest.getPoManufacturer(), updateRequest.getPoUnit(), updateRequest.getGoodsInSn(), crmEnterpriseDTO));
                // 匹配供应商ID
                updateRequest.setSupplierId(mathSupplierByName(updateRequest.getEnterpriseName(),crmEnterpriseDTO));
            }
            Integer cnt = erpPurchaseFlowDO.getCnt();
            if (CollUtil.isNotEmpty(oldFlowPurchaseList)) {
                operType = 2;
                //先判断多少条，然后在看cnt数量
                if (cnt != oldFlowPurchaseList.size()) {
                    //  判断数量，新增差值的数据
                    if (cnt > oldFlowPurchaseList.size()) {
                        int diff = cnt - oldFlowPurchaseList.size();
                        // 新增
                        for (int i = 0; i < diff; i++) {
                            if (crmEnterpriseDTO != null) {
                                updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                                updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                                updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                                updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                                updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                                updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                            }
                            updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                            updateRequest.setEid(enterpriseDTO.getId());
                            updateRequest.setEname(enterpriseDTO.getName());
                            updateRequest.setDataTag(DataTagEnum.EXC_ADD.getCode());
                            if (crmSupplierDTO != null) {
                                updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                            }
                            FlowPurchaseDTO newFlowPurchase = flowPurchaseApi.insertFlowPurchase(updateRequest);
                            newFlowPurchaseList.add(newFlowPurchase);
                        }
                    } else {
                        // 删除多余的数据
                        int diff = oldFlowPurchaseList.size() - cnt;
                        oldFlowPurchaseList.sort(Comparator.comparing(FlowPurchaseDTO::getId).reversed());  // id倒序
                        List<Long> deleteIdList = new ArrayList<>();

                        for (FlowPurchaseDTO flowPurchaseDTO : oldFlowPurchaseList) {
                            if (diff == 0) {
                                break;
                            }
                            deleteIdList.add(flowPurchaseDTO.getId());
                            diff--;
                        }
                        flowPurchaseApi.updateDataTagByIdList(deleteIdList, DataTagEnum.DELETE.getCode());
                        flowPurchaseApi.deleteByIdList(deleteIdList);
                    }
                } else {
                    // 业务不会执行该方法块代码
                    operType = 4;
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                        updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                        updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                        updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                        updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                        updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                    }
                    updateRequest.setEid(enterpriseDTO.getId());
                    updateRequest.setEname(enterpriseDTO.getName());
                    updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                    if (crmSupplierDTO != null) {
                        updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                    }
                    updateRequest.setOpUserId(0L);
                    updateRequest.setOpTime(new Date());
                    for (FlowPurchaseDTO flowPurchase : oldFlowPurchaseList) {
                        updateRequest.setId(flowPurchase.getId());
                        flowPurchaseApi.updateFlowPurchaseById(updateRequest);
                    }
                    oldFlowPurchaseList.forEach(o -> {
                        FlowPurchaseDTO newFlowPurchase = PojoUtils.map(updateRequest, FlowPurchaseDTO.class);
                        newFlowPurchase.setId(o.getId());
                        newFlowPurchaseList.add(newFlowPurchase);
                    });

                }
            } else {
                operType = 1;
                //新增
                for (int i = 0; i < cnt; i++) {
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                        updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                        updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                        updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                        updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                        updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                    }
                    updateRequest.setEid(enterpriseDTO.getId());
                    updateRequest.setEname(enterpriseDTO.getName());
                    updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                    if (crmSupplierDTO != null) {
                        updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                    }
                    FlowPurchaseDTO newFlowPurchase = flowPurchaseApi.insertFlowPurchase(updateRequest);
                    newFlowPurchaseList.add(newFlowPurchase);
                }
            }
        }

        // 采购库存
        erpPurchaseFlowAsyncService.handlerFlowPurchaseInventory(operType, erpClient, oldFlowPurchaseList, newFlowPurchaseList);
    }

    private int buildFlowPurchaseInventorySource(String enterpriseName) {
        int poSource = 0;
        if (ObjectUtil.equal(FlowPurchaseInventoryEnterpriseEnum.DA_YUN_HE.getName(), enterpriseName.trim())) {
            poSource = FlowPurchaseInventoryEnterpriseEnum.DA_YUN_HE.getCode();
        } else if (ObjectUtil.equal(FlowPurchaseInventoryEnterpriseEnum.JING_DONG.getName(), enterpriseName.trim())) {
            poSource = FlowPurchaseInventoryEnterpriseEnum.JING_DONG.getCode();
        }
        return poSource;
    }

    private void getYlGoodsId(ErpClientDTO erpClient, ErpPurchaseFlowDO erpPurchaseFlowDO) {
        if (ObjectUtil.isNotNull(erpClient) && ObjectUtil.isNotNull(erpPurchaseFlowDO) &&
                ObjectUtil.isNotNull(erpClient.getRkSuId()) && StrUtil.isNotBlank(erpClient.getClientName()) && StrUtil.isNotBlank(erpPurchaseFlowDO.getGoodsInSn())
                && StrUtil.isNotBlank(erpPurchaseFlowDO.getGoodsName()) && StrUtil.isNotBlank(erpPurchaseFlowDO.getPoSpecifications())) {
            QueryFlowGoodsRelationYlGoodsIdRequest ylGoodsIdRequest = new QueryFlowGoodsRelationYlGoodsIdRequest();
            ylGoodsIdRequest.setEid(erpClient.getRkSuId());
            ylGoodsIdRequest.setEname(erpClient.getClientName());
            ylGoodsIdRequest.setGoodsInSn(erpPurchaseFlowDO.getGoodsInSn());
            ylGoodsIdRequest.setGoodsName(erpPurchaseFlowDO.getGoodsName());
            ylGoodsIdRequest.setGoodsSpecifications(erpPurchaseFlowDO.getPoSpecifications());
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

    private void buildOrderSource(ErpPurchaseFlowDO erpPurchaseFlowDO, SaveOrUpdateFlowPurchaseRequest updateRequest) {
        String source = FlowPurchaseOrderSourceEnum.OTHERS.getCode();
        Map<String, Integer> purchaseFlowOrderSourceMap = erpFlowOrderSourceConfig.getPurchaseFlowOrderSourceMap();
        Integer sourceType = purchaseFlowOrderSourceMap.get(OpenStringUtils.clearAllSpace(erpPurchaseFlowDO.getPoSource()));
        if (ObjectUtil.isNotNull(sourceType)) {
            source = sourceType.toString();
        }
        updateRequest.setPoSource(source);
    }

    private Long mathSupplierByName(String enterpriseName, CrmEnterpriseDTO crmEnterpriseDTO) {
        //判断是否存在，不存在就新增信息
        if (StrUtil.isEmpty(enterpriseName)) {
            return 0L;
        }
        enterpriseName = enterpriseName.trim();
        Long supplierId=0L;
        FlowEnterpriseSupplierMappingDTO flowEnterpriseSupplierMappingDTO=flowEnterpriseSupplierMappingApi.findBySupplierNameAndCrmEnterpriseId(enterpriseName,crmEnterpriseDTO.getId());
        if(flowEnterpriseSupplierMappingDTO==null||flowEnterpriseSupplierMappingDTO.getCrmOrgId()==0){
            SaveFlowEnterpriseSupplierMappingRequest saveFlowEnterpriseSupplierMappingRequest=new SaveFlowEnterpriseSupplierMappingRequest();
            saveFlowEnterpriseSupplierMappingRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            saveFlowEnterpriseSupplierMappingRequest.setEnterpriseName(crmEnterpriseDTO.getName());
            saveFlowEnterpriseSupplierMappingRequest.setFlowSupplierName(enterpriseName);
            saveFlowEnterpriseSupplierMappingRequest.setProvince(crmEnterpriseDTO.getProvinceName());
            saveFlowEnterpriseSupplierMappingRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
            saveFlowEnterpriseSupplierMappingRequest.setLastUploadTime(new Date());
            saveFlowEnterpriseSupplierMappingRequest.setOpTime(new Date());
            CrmEnterpriseDTO crmEnterprise = crmEnterpriseApi.getCrmEnterpriseCodeByName(enterpriseName, false);
            if(crmEnterprise!=null){
                saveFlowEnterpriseSupplierMappingRequest.setCrmOrgId(crmEnterprise.getId());
                saveFlowEnterpriseSupplierMappingRequest.setOrgLicenseNumber(crmEnterprise.getLicenseNumber());
                saveFlowEnterpriseSupplierMappingRequest.setOrgName(crmEnterprise.getName());
                supplierId=crmEnterprise.getId();
            }
            flowEnterpriseSupplierMappingApi.save(saveFlowEnterpriseSupplierMappingRequest);
            return supplierId;
        }
        return flowEnterpriseSupplierMappingDTO.getCrmOrgId();
    }

    private Long mathGoodsSpec(String goodsName, String spec, String manufacturer) {
        if (StrUtil.isEmpty(goodsName.trim()) || StrUtil.isEmpty(spec.trim())) {
            return 0L;
        }
        goodsName = goodsName.trim();
        spec = spec.trim();
        manufacturer = manufacturer.trim();
        Long specificationId = 0L;
        String lockName = StrUtil.format("mathGoodsSpec_{}_{}_{}", goodsName, spec, manufacturer);
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
}
