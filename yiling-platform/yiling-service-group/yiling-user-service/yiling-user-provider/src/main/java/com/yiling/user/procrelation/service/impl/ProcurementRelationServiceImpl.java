package com.yiling.user.procrelation.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.entity.AgreementConditionDO;
import com.yiling.user.agreement.entity.AgreementDO;
import com.yiling.user.agreement.service.AgreementConditionService;
import com.yiling.user.agreement.service.AgreementGoodsService;
import com.yiling.user.agreement.service.AgreementService;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.SaveOrDeleteSupplierRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterprisePurchaseRelationService;
import com.yiling.user.enterprise.service.EnterpriseSupplierService;
import com.yiling.user.procrelation.dao.ProcurementRelationMapper;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.QueryProcRelationByTimePageRequest;
import com.yiling.user.procrelation.dto.request.QueryProcRelationPageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateRelationStatusRequest;
import com.yiling.user.procrelation.entity.ProcRelationGoodsSnapshotDO;
import com.yiling.user.procrelation.entity.ProcRelationSnapshotDO;
import com.yiling.user.procrelation.entity.ProcurementRelationDO;
import com.yiling.user.procrelation.entity.ProcurementRelationGoodsDO;
import com.yiling.user.procrelation.enums.PorcRelationDeliveryTypeEnum;
import com.yiling.user.procrelation.enums.ProcRelationErrorCode;
import com.yiling.user.procrelation.enums.PorcRelationSnapshotTypeEnum;
import com.yiling.user.procrelation.enums.ProcurementRelationStatusEnum;
import com.yiling.user.procrelation.service.ProcRelationGoodsSnapshotService;
import com.yiling.user.procrelation.service.ProcRelationSnapshotService;
import com.yiling.user.procrelation.service.ProcurementRelationGoodsService;
import com.yiling.user.procrelation.service.ProcurementRelationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * pop采购关系表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Slf4j
@Service
public class ProcurementRelationServiceImpl extends BaseServiceImpl<ProcurementRelationMapper, ProcurementRelationDO> implements ProcurementRelationService {

    @Autowired
    ProcRelationSnapshotService relationSnapshotService;
    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;
    @Autowired
    @Lazy
    ProcurementRelationGoodsService relationGoodsService;
    @Autowired
    ProcRelationGoodsSnapshotService goodsSnapshotService;
    @Autowired
    EnterprisePurchaseRelationService enterprisePurchaseRelationService;
    @Autowired
    AgreementService agreementService;
    @Autowired
    AgreementConditionService agreementConditionService;
    @Autowired
    AgreementGoodsService agreementGoodsService;
    @Autowired
    DataSource dataSource;
    @Autowired
    EnterpriseSupplierService enterpriseSupplierService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveProcurementRelation(SaveProcRelationRequest request) {
        //结束日期不能小于当前日期
        if (DateUtil.compare(request.getEndTime(), new Date()) <= 0) {
            log.warn("结束日期不能小于当前日期，参数={}", JSON.toJSONString(request));
            throw new BusinessException(ProcRelationErrorCode.END_TIME_INVALID);
        }
        Boolean hasSameRelation = hasRelationAtSameTime(request);
        if (hasSameRelation) {
            log.warn("相同的工业主体、配送商、渠道商、在同一时间点不能存在多个，参数={}", JSON.toJSONString(request));
            throw new BusinessException(ProcRelationErrorCode.RELATION_ALREADY);
        }
        ProcurementRelationDO relationDO = PojoUtils.map(request, ProcurementRelationDO.class);

        //版本id
        String vid = UUID.randomUUID().toString().replace("-", "");
        ProcRelationSnapshotDO snapshot = relationSnapshotService.querySnapshotByVersionId(vid);
        if (ObjectUtil.isNotNull(snapshot)) {
            throw new BusinessException(ProcRelationErrorCode.VID_CONFLICT);
        }
        relationDO.setVersionId(vid);
        relationDO.setProcRelationStatus(ProcurementRelationStatusEnum.NOT_STARTED.getCode());
        //如果是工业配送，配送商置为工业
        if (ObjectUtil.equal(request.getDeliveryType(), PorcRelationDeliveryTypeEnum.FACTORY.getCode())) {
            relationDO.setDeliveryEid(request.getFactoryEid());
            relationDO.setDeliveryName(request.getFactoryName());
        }
        //判断已开始则新增客户关系
        if (DateUtil.compare(request.getStartTime(), DateUtil.beginOfDay(new Date())) <= 0) {
            relationDO.setProcRelationStatus(ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
            //添加采购关系
//            AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
//            addCustomerRequest.setCustomerEid(request.getChannelPartnerEid());
//            addCustomerRequest.setEid(relationDO.getDeliveryEid());
//            addCustomerRequest.setSource(4);
//            addCustomerRequest.setAddPurchaseRelationFlag(true);
//            Long var = enterpriseCustomerService.addCustomer(addCustomerRequest);
            Long var = addCustomerRelation(relationDO.getDeliveryEid(),request.getChannelPartnerEid());
            //关联客户关系id
            relationDO.setEnterpriseCustomerId(var);
        }
        //同步新增供应商erp信息维护
        SaveOrDeleteSupplierRequest saveOrDeleteSupplierRequest=new SaveOrDeleteSupplierRequest();
        saveOrDeleteSupplierRequest.setCustomerEid(request.getChannelPartnerEid());
        saveOrDeleteSupplierRequest.setEid(relationDO.getDeliveryEid());
        boolean isInsert = enterpriseSupplierService.insertSupplier(saveOrDeleteSupplierRequest);
        if (!isInsert){
            log.error("同步新增供应商erp信息维护失败，参数={}",saveOrDeleteSupplierRequest);
            throw new ServiceException(ResultCode.FAILED);
        }

//        relationDO.setProcRelationNumber(generateRelationNum());
        //存入pop采购关系
        boolean isSuccess = save(relationDO);
        if (!isSuccess) {
            log.error("新增pop采购关系失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        //生成采购编号
        relationDO.setProcRelationNumber("PR" + String.format("%08d", relationDO.getId()));
        isSuccess = updateById(relationDO);
        if (!isSuccess) {
            log.error("更新pop采购关系编号失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        //存入快照
        ProcRelationSnapshotDO snapshotDO = PojoUtils.map(relationDO, ProcRelationSnapshotDO.class);
        snapshotDO.setRelationId(relationDO.getId());
        isSuccess = relationSnapshotService.save(snapshotDO);
        if (!isSuccess) {
            log.error("新增pop采购关系时存入快照表失败，参数={}", snapshotDO);
            throw new ServiceException(ResultCode.FAILED);
        }

        return relationDO.getId();
    }

    @Override
    public Long updateProcurementRelation(UpdateProcRelationRequest request) {
        ProcurementRelationDO oldRelationDO = getById(request.getId());
        if (oldRelationDO == null) {
            log.warn("更新采购关系时未查到采购关系，id={}", request.getId());
            throw new BusinessException(ProcRelationErrorCode.RELATION_NOT_FIND);
        }
        //结束日期不能小于当前日期
        if (DateUtil.compare(request.getEndTime(), new Date()) <= 0) {
            log.warn("结束日期不能小于当前日期，参数={}", JSON.toJSONString(request));
            throw new BusinessException(ProcRelationErrorCode.END_TIME_INVALID);
        }
        Boolean hasSameRelation = hasRelationAtSameTime(PojoUtils.map(request, SaveProcRelationRequest.class));
        if (hasSameRelation) {
            log.warn("相同的工业主体、配送商、渠道商、在同一时间点不能存在多个，参数={}", JSON.toJSONString(request));
            throw new BusinessException(ProcRelationErrorCode.RELATION_ALREADY);
        }
        //如果老的采购关系已经生效则删除老的客户关系
        if (ObjectUtil.equal(oldRelationDO.getProcRelationStatus(), ProcurementRelationStatusEnum.IN_PROGRESS.getCode())) {
            //删除采购关系
            deleteCustomerRelation(oldRelationDO, request.getOpUserId());
        }

        //如果工业变更，则清空当前商品
        if (ObjectUtil.notEqual(request.getFactoryEid(), oldRelationDO.getFactoryEid())) {
            Boolean isSuccess = relationGoodsService.emptyGoodsByRelationId(oldRelationDO.getId(), request.getOpUserId());
            if (!isSuccess) {
                log.error("更新采购关系时，清空采购关系商品失败，采购关系id={}", oldRelationDO.getId());
                throw new ServiceException(ResultCode.FAILED);
            }
        }

        ProcurementRelationDO relationDO = PojoUtils.map(request, ProcurementRelationDO.class);
        relationDO.setId(request.getId());
        relationDO.setVersion(oldRelationDO.getVersion());
        relationDO.setOriginalVersion(oldRelationDO.getVersion());

        //版本id
        String vid = UUID.randomUUID().toString().replace("-", "");
        ProcRelationSnapshotDO snapshot = relationSnapshotService.querySnapshotByVersionId(vid);
        if (ObjectUtil.isNotNull(snapshot)) {
            throw new BusinessException(ProcRelationErrorCode.VID_CONFLICT);
        }
        relationDO.setVersionId(vid);
        relationDO.setProcRelationStatus(ProcurementRelationStatusEnum.NOT_STARTED.getCode());
        //如果是工业配送，配送商置为工业
        if (ObjectUtil.equal(request.getDeliveryType(), PorcRelationDeliveryTypeEnum.FACTORY.getCode())) {
            relationDO.setDeliveryEid(request.getFactoryEid());
            relationDO.setDeliveryName(request.getFactoryName());
        }
        //判断已开始则新增客户关系
        if (DateUtil.compare(request.getStartTime(), DateUtil.beginOfDay(new Date())) <= 0) {
            relationDO.setProcRelationStatus(ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
            //添加采购关系
//            AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
//            addCustomerRequest.setCustomerEid(request.getChannelPartnerEid());
//            addCustomerRequest.setEid(relationDO.getDeliveryEid());
//            addCustomerRequest.setSource(4);
//            addCustomerRequest.setAddPurchaseRelationFlag(true);
//            Long var = enterpriseCustomerService.addCustomer(addCustomerRequest);
            Long var = addCustomerRelation(relationDO.getDeliveryEid(),request.getChannelPartnerEid());
            //关联客户关系id
            relationDO.setEnterpriseCustomerId(var);
        }
        //更新pop采购关系
        int row = baseMapper.updateInfoByVersion(relationDO);
        if (row == 0) {
            log.error("新增pop采购关系失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        //存入类型为正在使用的快照
        //存入快照
        createSnapshot(relationDO);

        return relationDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void increaseVersion(Long procRelationId, Long opUser) {
        ProcurementRelationDO relationDO = getById(procRelationId);
        if (ObjectUtil.isNull(relationDO)) {
            log.error("版本号递增时，采购关系不存在，id={}", procRelationId);
            throw new BusinessException(ProcRelationErrorCode.RELATION_NOT_FIND);
        }
        //修改快照类型为历史版本
        ProcRelationSnapshotDO historySnapshot = relationSnapshotService.querySnapshotByVersionId(relationDO.getVersionId());
        if (ObjectUtil.isNull(historySnapshot)) {
            log.error("版本号递增时，快照不存在，versionId={}", relationDO.getVersionId());
            throw new BusinessException(ProcRelationErrorCode.RELATION_SNAPSHOT_FIND);
        }
        historySnapshot.setSnapshotType(PorcRelationSnapshotTypeEnum.HISTORY.getCode());
        historySnapshot.setOpTime(new Date());
        boolean isSuccess = relationSnapshotService.updateById(historySnapshot);
        if (!isSuccess) {
            log.error("版本号递增时，更新快照失败，参数={}", historySnapshot);
            throw new ServiceException(ResultCode.FAILED);
        }
        //采购关系表版本递增
        //版本id
        String vid = UUID.randomUUID().toString().replace("-", "");
        ProcRelationSnapshotDO snapshot = relationSnapshotService.querySnapshotByVersionId(vid);
        if (ObjectUtil.isNotNull(snapshot)) {
            throw new BusinessException(ProcRelationErrorCode.VID_CONFLICT);
        }
        relationDO.setOriginalVersion(relationDO.getVersion());
        relationDO.setVersionId(vid);
        relationDO.setOpTime(new Date());
        relationDO.setOpUserId(opUser);
        int row = baseMapper.updateVersion(relationDO);
        if (row == 0) {
            log.error("版本号递增时，更新采购关系失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        //存入类型为正在使用的快照
        //存入快照
        createSnapshot(relationDO);
    }

    @Override
    public List<ProcurementRelationDO> queryInProRelationListByChannelEid(Long channelPartnerEid) {
        if (ObjectUtil.isNull(channelPartnerEid) || ObjectUtil.equal(0L, channelPartnerEid)) {
            return ListUtil.toList();
        }
        Date currentDate = new Date();
        LambdaQueryWrapper<ProcurementRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcurementRelationDO::getChannelPartnerEid, channelPartnerEid);
        wrapper.eq(ProcurementRelationDO::getProcRelationStatus, ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
        wrapper.le(ProcurementRelationDO::getStartTime, currentDate);
        wrapper.ge(ProcurementRelationDO::getEndTime, currentDate);

        return list(wrapper);
    }

    @Override
    public List<ProcurementRelationDO> queryInProRelationListByEachOther(Long deliveryEid, Long channelPartnerEid) {
        if (ObjectUtil.isNull(deliveryEid) || ObjectUtil.equal(0L, deliveryEid)) {
            return null;
        }
        if (ObjectUtil.isNull(channelPartnerEid) || ObjectUtil.equal(0L, channelPartnerEid)) {
            return null;
        }
        Date currentDate = new Date();
        LambdaQueryWrapper<ProcurementRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcurementRelationDO::getDeliveryEid, deliveryEid);
        wrapper.eq(ProcurementRelationDO::getChannelPartnerEid, channelPartnerEid);
        wrapper.eq(ProcurementRelationDO::getProcRelationStatus, ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
        wrapper.le(ProcurementRelationDO::getStartTime, currentDate);
        wrapper.ge(ProcurementRelationDO::getEndTime, currentDate);
        return list(wrapper);
    }

    @Override
    public Page<ProcurementRelationDO> queryProcRelationPage(QueryProcRelationPageRequest request) {
        LambdaQueryWrapper<ProcurementRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(request.getProcRelationNumber()), ProcurementRelationDO::getProcRelationNumber, request.getProcRelationNumber());
        wrapper.eq(ObjectUtil.isNotNull(request.getFactoryEid()) && ObjectUtil.notEqual(0L, request.getFactoryEid()), ProcurementRelationDO::getFactoryEid, request.getFactoryEid());
        wrapper.eq(ObjectUtil.isNotNull(request.getDeliveryEid()) && ObjectUtil.notEqual(0L, request.getDeliveryEid()), ProcurementRelationDO::getDeliveryEid, request.getDeliveryEid());
        wrapper.in(CollUtil.isNotEmpty(request.getChannelPartnerEid()), ProcurementRelationDO::getChannelPartnerEid, request.getChannelPartnerEid());
        wrapper.eq(ObjectUtil.isNotNull(request.getDeliveryType()) && ObjectUtil.notEqual(0, request.getDeliveryType()), ProcurementRelationDO::getDeliveryType, request.getDeliveryType());
        wrapper.eq(ObjectUtil.isNotNull(request.getProcRelationStatus()) && ObjectUtil.notEqual(0, request.getProcRelationStatus()), ProcurementRelationDO::getProcRelationStatus, request.getProcRelationStatus());

        return page(request.getPage(), wrapper);
    }

    @Override
    public Boolean closeProcRelationById(Long relationId, Long opUser) {
        if (ObjectUtil.isNull(relationId) || ObjectUtil.equal(0L, relationId)) {
            log.warn("停用pop采购关系时，采购关系id不能为空");
            return Boolean.FALSE;
        }
        if (ObjectUtil.isNull(opUser) || ObjectUtil.equal(0L, opUser)) {
            log.warn("停用pop采购关系时，操作人不能为空");
            return Boolean.FALSE;
        }
        ProcurementRelationDO relationDO = getById(relationId);
        if (ObjectUtil.isNull(relationDO)) {
            log.warn("停用pop采购关系时，采购关系不存在，relationId={}", relationId);
            return Boolean.FALSE;
        }
        if (ObjectUtil.notEqual(relationDO.getProcRelationStatus(), ProcurementRelationStatusEnum.IN_PROGRESS.getCode())) {
            log.warn("停用pop采购关系时，只有进行中的可以停用，当前状态为={}", relationDO.getProcRelationStatus());
            return Boolean.FALSE;
        }
        //删除客户关系
        deleteCustomerRelation(relationDO, opUser);
        //更新pop采购关系表
        relationDO.setProcRelationStatus(ProcurementRelationStatusEnum.DEACTIVATED.getCode());
        relationDO.setStopTime(new Date());
        relationDO.setStopUser(opUser);
        relationDO.setOpUserId(opUser);
        relationDO.setOpTime(new Date());
        Boolean isSuccess = updateById(relationDO);
        if (!isSuccess) {
            log.error("采购关系停用时更新采购关系失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        return isSuccess;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initData() {

        List<Long> thirdEidList = agreementService.queryEidByMigrate();

        //分页查询协议
        List<Long> tempThirdEidList;
        int queryOrderCurrent = 0;
        do {
            tempThirdEidList = CollUtil.page(queryOrderCurrent, 10, thirdEidList);
            if (CollUtil.isEmpty(tempThirdEidList)) {
                break;
            }
            tempThirdEidList.forEach(thirdEid -> {
                //查询该企业下的补充协议
                List<AgreementDTO> agreementDTOS = agreementService.queryAgreementListByThirdEid(thirdEid);
                Map<Long, List<AgreementDTO>> eidMap = agreementDTOS.stream().collect(Collectors.groupingBy(AgreementDTO::getEid));
                List<AgreementDO> agreementUpdateList = ListUtil.toList();
                eidMap.forEach((factoryId, agreements) -> {

                    //可能涉及多家配送商
                    Map<Long, List<AgreementDTO>> secondMap = agreements.stream().collect(Collectors.groupingBy(AgreementDTO::getSecondEid));
                    secondMap.forEach((secondEid, aList) -> {
                        SaveProcRelationRequest var = new SaveProcRelationRequest();
                        List<AgreementDO> agreeVarList = CollUtil.toList();
                        aList.forEach(e -> {
                            AgreementDO aVar = new AgreementDO();
                            aVar.setId(e.getId());
                            agreeVarList.add(aVar);
                        });
                        AgreementDTO agreementDTO = aList.stream().findAny().get();
                        var.setFactoryEid(agreementDTO.getEid());
                        var.setFactoryName(agreementDTO.getEname());
                        var.setDeliveryEid(agreementDTO.getSecondEid());
                        var.setDeliveryName(agreementDTO.getSecondName());
                        var.setChannelPartnerEid(agreementDTO.getThirdEid());
                        var.setChannelPartnerName(agreementDTO.getThirdName());
                        var.setStartTime(agreementDTO.getStartTime());
                        var.setEndTime(DateUtil.offsetMillisecond(agreementDTO.getEndTime(), -999));
                        var.setDeliveryType(agreementDTO.getMode());
                        var.setOpUserId(1L);

                        Long relationId = saveByAgreement(var);
                        if (relationId == null || ObjectUtil.equal(relationId, 0L)) {
                            agreeVarList.forEach(e -> {
                                e.setMigrateStatus(1);
                            });
                            agreementUpdateList.addAll(agreeVarList);
                            return;
                        }
                        //查询协议条件
                        List<Long> aIdList = aList.stream().map(AgreementDTO::getId).distinct().collect(Collectors.toList());
                        List<AgreementConditionDO> conditionDOList = agreementConditionService.queryBuyConditionByAgreementId(aIdList);
                        Map<Long, AgreementConditionDO> conditionDOMap = conditionDOList.stream().collect(Collectors.toMap(AgreementConditionDO::getAgreementId, e -> e));
                        Map<Long, AddGoodsForProcRelationRequest> goodsMap = MapUtil.newHashMap();

                        //查询协议商品
                        Map<Long, List<AgreementGoodsDTO>> aGoodsMap = agreementGoodsService.getAgreementGoodsListByAgreementIds(aIdList);

                        aGoodsMap.forEach((aId, goodsList) -> {
                            goodsList.forEach(g -> {
                                AgreementConditionDO conditionDO = conditionDOMap.get(aId);
                                if (goodsMap.containsKey(g.getGoodsId())) {
                                    AddGoodsForProcRelationRequest gRequest = goodsMap.get(g.getGoodsId());
                                    //产品要求如果商品在多个补充协议中取有返利的政策
                                    if (gRequest.getRebate().compareTo(conditionDO.getPolicyValue()) < 0) {
                                        gRequest.setRebate(conditionDO.getPolicyValue());
                                        gRequest.setRequirements(conditionDO.getTotalAmount());
                                    }
                                } else {
                                    AddGoodsForProcRelationRequest gVar = PojoUtils.map(g, AddGoodsForProcRelationRequest.class);
                                    gVar.setId(null);
                                    gVar.setRelationId(relationId);
                                    gVar.setRebate(conditionDO.getPolicyValue());
                                    gVar.setRequirements(conditionDO.getTotalAmount());
                                    goodsMap.put(g.getGoodsId(), gVar);
                                }
                            });
                        });
                        //保存采购关系的商品
                        relationGoodsService.addGoodsForProcRelation(new ArrayList<>(goodsMap.values()));
                        agreeVarList.forEach(e -> {
                            e.setMigrateStatus(2);
                        });
                        agreementUpdateList.addAll(agreeVarList);

                    });
                });
                //回写协议迁移状态
                if (CollUtil.isNotEmpty(agreementUpdateList)) {
                    Boolean isSuccess = agreementService.updateMigrateStatus(agreementUpdateList);
                    if (!isSuccess) {
                        log.error("更新协议迁移状态失败，参数={}", JSON.toJSONString(agreementUpdateList));
                        throw new ServiceException(ResultCode.FAILED);
                    }
                }
            });

            queryOrderCurrent = queryOrderCurrent + 1;
        } while (CollUtil.isNotEmpty(tempThirdEidList));


    }

    @Override
    public void initEnterpriseSupplier() {
        Page<ProcurementRelationDO> page;
        QueryProcRelationPageRequest queryRequest = new QueryProcRelationPageRequest();
        ArrayList<ProcurementRelationDO> addList = ListUtil.toList();
        int current = 1;
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(100);
            //分页查询
            page = queryProcRelationPage(queryRequest);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<ProcurementRelationDO> records = page.getRecords();
            addList.addAll(records);
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        if (CollUtil.isEmpty(addList)){
            return;
        }
        addList.forEach(e->{
            SaveOrDeleteSupplierRequest saveOrDeleteSupplierRequest=new SaveOrDeleteSupplierRequest();
            saveOrDeleteSupplierRequest.setCustomerEid(e.getChannelPartnerEid());
            saveOrDeleteSupplierRequest.setEid(e.getDeliveryEid());
            boolean isInsert = enterpriseSupplierService.insertSupplier(saveOrDeleteSupplierRequest);
            if (!isInsert){
                log.error("同步新增供应商erp信息维护失败，参数={}",saveOrDeleteSupplierRequest);
            }
        });
    }

    @Override
    public Boolean deleteProcRelationById(Long relationId, Long opUser) {
        if (ObjectUtil.isNull(relationId) || ObjectUtil.equal(0L, relationId)) {
            log.warn("删除pop采购关系时，采购关系id不能为空");
            return Boolean.FALSE;
        }
        if (ObjectUtil.isNull(opUser) || ObjectUtil.equal(0L, opUser)) {
            log.warn("删除pop采购关系时，操作人不能为空");
            return Boolean.FALSE;
        }
        ProcurementRelationDO relationDO = getById(relationId);
        if (ObjectUtil.isNull(relationDO)) {
            log.warn("删除pop采购关系时，采购关系不存在，relationId={}", relationId);
            return Boolean.FALSE;
        }
        if (ObjectUtil.notEqual(relationDO.getProcRelationStatus(), ProcurementRelationStatusEnum.NOT_STARTED.getCode())) {
            log.warn("删除pop采购关系时，只有未开始的可以删除，当前状态为={}", relationDO.getProcRelationStatus());
            return Boolean.FALSE;
        }
        //同步删除供应商erp信息维护
        //查询当前是否还存在采购关系
        LambdaQueryWrapper<ProcurementRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcurementRelationDO::getDeliveryEid,relationDO.getDeliveryEid());
        wrapper.eq(ProcurementRelationDO::getChannelPartnerEid,relationDO.getChannelPartnerEid());
        wrapper.ne(ProcurementRelationDO::getId,relationDO.getId());
        List<ProcurementRelationDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)){
            SaveOrDeleteSupplierRequest saveOrDeleteSupplierRequest=new SaveOrDeleteSupplierRequest();
            saveOrDeleteSupplierRequest.setCustomerEid(relationDO.getChannelPartnerEid());
            saveOrDeleteSupplierRequest.setEid(relationDO.getDeliveryEid());
            boolean isDelete = enterpriseSupplierService.deleteSupplier(saveOrDeleteSupplierRequest);
            if (!isDelete){
                log.error("同步删除供应商erp信息维护失败，参数={}",saveOrDeleteSupplierRequest);
                throw new ServiceException(ResultCode.FAILED);
            }
        }

        relationDO.setOpUserId(opUser);
        relationDO.setOpTime(new Date());
        int rows = deleteByIdWithFill(relationDO);
        if (rows == 0) {
            log.error("采购关系删除时删除采购关系失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInProgress() {
        Page<ProcurementRelationDO> page;
        QueryProcRelationByTimePageRequest queryRequest = new QueryProcRelationByTimePageRequest();
        queryRequest.setProcRelationStatus(ProcurementRelationStatusEnum.NOT_STARTED.getCode());
        queryRequest.setStartTime(DateUtil.beginOfDay(new Date()));
        List<UpdateRelationStatusRequest> requestList =ListUtil.toList();

        int current = 1;
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(100);
            //分页查询符合结算条件的订单
            page = queryProcRelationPageForUpdateStatus(queryRequest);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<ProcurementRelationDO> records = page.getRecords();
            List<UpdateRelationStatusRequest> var = PojoUtils.map(records, UpdateRelationStatusRequest.class);
            var.forEach(e -> {
                e.setProcRelationStatus(ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
            });
            requestList.addAll(var);

            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        //更新状态
        updateRelationStatus(requestList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateExpired() {
        Page<ProcurementRelationDO> page;
        QueryProcRelationByTimePageRequest queryRequest = new QueryProcRelationByTimePageRequest();
        queryRequest.setProcRelationStatus(ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
        queryRequest.setEndTime(DateUtil.endOfDay(DateUtil.yesterday()));
        List<UpdateRelationStatusRequest> requestList=ListUtil.toList();

        int current = 1;
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(100);
            //分页查询符合结算条件的订单
            page = queryProcRelationPageForUpdateStatus(queryRequest);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<ProcurementRelationDO> records = page.getRecords();
            List<UpdateRelationStatusRequest> var = PojoUtils.map(records, UpdateRelationStatusRequest.class);
            var.forEach(e -> {
                e.setProcRelationStatus(ProcurementRelationStatusEnum.EXPIRED.getCode());
            });
            requestList.addAll(var);
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        //更新状态
        updateRelationStatus(requestList);
    }

    public Page<ProcurementRelationDO> queryProcRelationPageForUpdateStatus(QueryProcRelationByTimePageRequest request) {
        LambdaQueryWrapper<ProcurementRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getProcRelationStatus()) && ObjectUtil.notEqual(0, request.getProcRelationStatus()), ProcurementRelationDO::getProcRelationStatus, request.getProcRelationStatus());
        if (ObjectUtil.isNotNull(request.getStartTime())) {
            wrapper.le(ProcurementRelationDO::getStartTime, request.getStartTime());
        }
        if (ObjectUtil.isNotNull(request.getEndTime())) {
            wrapper.le(ProcurementRelationDO::getEndTime, request.getEndTime());
        }
        return page(request.getPage(), wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRelationStatus(List<UpdateRelationStatusRequest> requestList) {
        if (CollUtil.isEmpty(requestList)) {
            return Boolean.FALSE;
        }
        Map<Integer, List<UpdateRelationStatusRequest>> statusMap = requestList.stream().collect(Collectors.groupingBy(UpdateRelationStatusRequest::getProcRelationStatus));
        //更新为进行中的采购关系
        List<UpdateRelationStatusRequest> inProgressList = statusMap.get(ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
        List<ProcurementRelationDO> updateList = ListUtil.toList();
        if (CollUtil.isNotEmpty(inProgressList)) {
            inProgressList.forEach(e -> {
                ProcurementRelationDO var = new ProcurementRelationDO();
                var.setId(e.getId());
                var.setProcRelationStatus(ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
                //添加采购关系
//                AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
//                addCustomerRequest.setCustomerEid(e.getChannelPartnerEid());
//                addCustomerRequest.setEid(e.getDeliveryEid());
//                addCustomerRequest.setSource(4);
//                addCustomerRequest.setAddPurchaseRelationFlag(true);
//                Long id = enterpriseCustomerService.addCustomer(addCustomerRequest);
                Long id = addCustomerRelation(e.getDeliveryEid(),e.getChannelPartnerEid());
                //关联客户关系id
                var.setEnterpriseCustomerId(id);
                var.setOpUserId(e.getUpdateUser());
                var.setOpTime(e.getUpdateTime());
                updateList.add(var);
            });
        }
        //更新为已过期的采购关系
        List<UpdateRelationStatusRequest> expiredList = statusMap.get(ProcurementRelationStatusEnum.EXPIRED.getCode());
        if (CollUtil.isNotEmpty(expiredList)) {
            expiredList.forEach(e -> {
                ProcurementRelationDO relationDO = new ProcurementRelationDO();
                relationDO.setId(e.getId());
                relationDO.setEnterpriseCustomerId(e.getEnterpriseCustomerId());
                relationDO.setDeliveryEid(e.getDeliveryEid());
                relationDO.setChannelPartnerEid(e.getChannelPartnerEid());
                //删除客户关系
                try {
                    deleteCustomerRelation(relationDO, 1L);
                } catch (Exception ex) {
                   log.error("定时任务过期pop采购关系失败，原因={}",ex.getLocalizedMessage());
                }

                ProcurementRelationDO var = new ProcurementRelationDO();
                var.setId(e.getId());
                var.setExpireTime(new Date());
                var.setOpUserId(e.getUpdateUser());
                var.setOpTime(e.getUpdateTime());
                var.setProcRelationStatus(ProcurementRelationStatusEnum.EXPIRED.getCode());
                updateList.add(var);
            });
        }
        //更新采购关系表
        if (CollUtil.isNotEmpty(updateList)) {
            boolean isSuccess = updateBatchById(updateList);
            if (!isSuccess) {
                log.error("批量生效或过期POP采购关系时更新失败，参数={}", updateList);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 判断相同的工业主体、配送商、渠道商、在同一时间点是否存在多个
     *
     * @param request
     * @return
     */
    private Boolean hasRelationAtSameTime(SaveProcRelationRequest request) {
        //检查在同一时间段内相同的工业、配送商、渠道商是否以存在采购关系
        LambdaQueryWrapper<ProcurementRelationDO> queryWrappers = Wrappers.lambdaQuery();
        queryWrappers.eq(ProcurementRelationDO::getFactoryEid, request.getFactoryEid()).eq(ObjectUtil.equal(PorcRelationDeliveryTypeEnum.FACTORY.getCode(), request.getDeliveryType()), ProcurementRelationDO::getDeliveryEid, request.getFactoryEid()).eq(ObjectUtil.equal(PorcRelationDeliveryTypeEnum.THIRD_ENTERPRISE.getCode(), request.getDeliveryType()), ProcurementRelationDO::getDeliveryEid, request.getDeliveryEid()).eq(ProcurementRelationDO::getChannelPartnerEid, request.getChannelPartnerEid())
                //未开始和进行中的采购关系
                .in(ProcurementRelationDO::getProcRelationStatus, ListUtil.toList(ProcurementRelationStatusEnum.NOT_STARTED.getCode(), ProcurementRelationStatusEnum.IN_PROGRESS.getCode()))
                //时间范围
                .le(ProcurementRelationDO::getStartTime, request.getEndTime()).ge(ProcurementRelationDO::getEndTime, request.getStartTime());
        int count = count(queryWrappers);
        return count > 0;
    }

    private String generateRelationNum() {
        //        LambdaQueryWrapper<ProcurementRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        //        queryWrapper.select(ProcurementRelationDO::getId)
        //                .orderByDesc(ProcurementRelationDO::getId).last("LIMIT 1");
        //        List<Object> result = baseMapper.selectObjs(queryWrapper);
        //        if (result != null && !result.isEmpty()) {
        //            return "PR" + String.format("%08d", Long.parseLong(result.get(0).toString()) + 1L);
        //        }
        //        return "PR00000001";
        String databaseName = getDatabaseName();
        Long autoIncrement = baseMapper.queryAutoIncrement(databaseName);
        if (autoIncrement != null && autoIncrement != 0) {
            return "PR" + String.format("%08d", autoIncrement);
        } else {
            log.error("采购关系编号生成异常，autoIncrement={}", autoIncrement);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    private Long saveByAgreement(SaveProcRelationRequest request) {
        Boolean hasSameRelation = hasRelationAtSameTime(request);
        if (hasSameRelation) {
            log.warn("相同的工业主体、配送商、渠道商、在同一时间点不能存在多个，参数={}", JSON.toJSONString(request));
            return null;
        }
        ProcurementRelationDO relationDO = PojoUtils.map(request, ProcurementRelationDO.class);

        //版本id
        String vid = UUID.randomUUID().toString().replace("-", "");
        ProcRelationSnapshotDO snapshot = relationSnapshotService.querySnapshotByVersionId(vid);
        if (ObjectUtil.isNotNull(snapshot)) {
            throw new BusinessException(ProcRelationErrorCode.VID_CONFLICT);
        }
        relationDO.setVersionId(vid);
        relationDO.setProcRelationStatus(ProcurementRelationStatusEnum.NOT_STARTED.getCode());
        relationDO.setProcRelationNumber(generateRelationNum());
        //查询客户关系id
        EnterpriseCustomerDO customerDO = enterpriseCustomerService.get(request.getDeliveryEid(), request.getChannelPartnerEid());
        if (customerDO != null) {
            relationDO.setEnterpriseCustomerId(customerDO.getId());
        }
        relationDO.setRemark("协议导入");
        //存入pop采购关系
        boolean isSuccess = save(relationDO);
        if (!isSuccess) {
            log.error("新增pop采购关系失败，参数={}", relationDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        //存入快照
        ProcRelationSnapshotDO snapshotDO = PojoUtils.map(relationDO, ProcRelationSnapshotDO.class);
        snapshotDO.setRelationId(relationDO.getId());
        isSuccess = relationSnapshotService.save(snapshotDO);
        if (!isSuccess) {
            log.error("新增pop采购关系时存入快照表失败，参数={}", snapshotDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        return relationDO.getId();
    }

    /**
     * 创建快照
     *
     * @param relationDO
     */
    private void createSnapshot(ProcurementRelationDO relationDO) {
        //存入快照
        ProcRelationSnapshotDO newSnapshot = PojoUtils.map(relationDO, ProcRelationSnapshotDO.class);
        newSnapshot.setVersion(relationDO.getVersion() + 1);
        newSnapshot.setRelationId(relationDO.getId());
        Boolean isSuccess = relationSnapshotService.save(newSnapshot);
        if (!isSuccess) {
            log.error("版本号递增时,存入快照表失败，参数={}", newSnapshot);
            throw new ServiceException(ResultCode.FAILED);
        }
        List<ProcurementRelationGoodsDO> relationGoodsList = relationGoodsService.queryGoodsByRelationId(relationDO.getId());
        if (CollUtil.isEmpty(relationGoodsList)) {
            return;
        }
        List<ProcRelationGoodsSnapshotDO> goodsSnapshotDOS = PojoUtils.map(relationGoodsList, ProcRelationGoodsSnapshotDO.class);
        goodsSnapshotDOS.forEach(e -> {
            e.setRelationSnapshotId(newSnapshot.getId());
        });
        isSuccess = goodsSnapshotService.saveBatch(goodsSnapshotDOS);
        if (!isSuccess) {
            log.error("版本号递增时,存入商品快照表失败，参数={}", goodsSnapshotDOS);
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    /**
     * 新增客户关系
     *
     * @param deliveryEid
     * @param channelPartnerEid
     * @return
     */
    private Long addCustomerRelation(Long deliveryEid ,Long channelPartnerEid){
        //添加采购关系
        AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
        addCustomerRequest.setCustomerEid(channelPartnerEid);
        addCustomerRequest.setEid(deliveryEid);
        addCustomerRequest.setSource(4);
        addCustomerRequest.setAddPurchaseRelationFlag(true);
        Long id = enterpriseCustomerService.addCustomer(addCustomerRequest);
        return id;
    }
    /**
     * 根据采购关系清空客户关系
     *
     * @param relationDO
     */
    private void deleteCustomerRelation(ProcurementRelationDO relationDO, Long opUser) {
        Long customerId = relationDO.getEnterpriseCustomerId();
        //校验当前是否有生效的采购关系
        LambdaQueryWrapper<ProcurementRelationDO> checkWrapper = Wrappers.lambdaQuery();
        checkWrapper.eq(ProcurementRelationDO::getProcRelationStatus, ProcurementRelationStatusEnum.IN_PROGRESS.getCode());
        checkWrapper.eq(ProcurementRelationDO::getEnterpriseCustomerId, customerId);
        checkWrapper.ne(ProcurementRelationDO::getId, relationDO.getId());
        List<ProcurementRelationDO> relations = list(checkWrapper);
        //如果当前还有生效中的采购关系则返回
        if (CollUtil.isNotEmpty(relations)) {
            return;
        }

        //判断采购关系是否存在
        if (customerId == null || ObjectUtil.equal(0L, customerId)) {
            log.error("采购关系停用时根据买卖双方eid未查询到采购关系，eid={}，customerEid={}", relationDO.getDeliveryEid(), relationDO.getEnterpriseCustomerId());
            throw new BusinessException(ProcRelationErrorCode.RELATION_CUSTOMER_RELATION_NOT_FOUND);
        }
        //删除采购关系
        boolean isSuccess = enterpriseCustomerService.deleteEnterpriseCustomer(customerId, opUser);
        if (!isSuccess) {
            log.error("采购关系停用时删除客户关系失败，enterpriseCustomerId={}", relationDO.getEnterpriseCustomerId());
            throw new ServiceException(ResultCode.FAILED);
        }
        RemovePurchaseRelationFormRequest removePurchaseRelationFormRequest = new RemovePurchaseRelationFormRequest();
        removePurchaseRelationFormRequest.setBuyerId(relationDO.getChannelPartnerEid());
        removePurchaseRelationFormRequest.setSellerIds(Arrays.asList(relationDO.getDeliveryEid()));
        isSuccess = enterprisePurchaseRelationService.removePurchaseRelation(removePurchaseRelationFormRequest);
        if (!isSuccess) {
            log.error("采购关系停用时删除采购关系失败，channelPartnerEid={},deliveryEid={}", relationDO.getChannelPartnerEid(), relationDO.getDeliveryEid());
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    private String getDatabaseName() {
        String dbName = null;
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String url = metaData.getURL();
            dbName = extractDatabaseNameFromUrl(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbName;
    }

    private String extractDatabaseNameFromUrl(String url) {
        String[] parts = url.split("\\?");
        String[] pathParts = parts[0].split("/");
        return pathParts[pathParts.length - 1];
    }
}
