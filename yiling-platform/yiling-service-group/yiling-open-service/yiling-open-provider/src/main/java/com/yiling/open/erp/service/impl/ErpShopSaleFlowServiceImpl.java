package com.yiling.open.erp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseGoodsMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.flow.enums.SyncStatus;
import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.api.FlowShopSaleApi;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import com.yiling.dataflow.order.dto.FlowShopSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowShopSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowShopSaleRequest;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.backup.util.BackupUtil;
import com.yiling.open.erp.api.ErpShopMappingApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpShopSaleFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.dto.request.QuerySaleFlowConditionRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;
import com.yiling.open.erp.entity.ErpFlowSealedDO;
import com.yiling.open.erp.entity.ErpShopSaleFlowDO;
import com.yiling.open.erp.enums.DataTagEnum;
import com.yiling.open.erp.enums.ErpFlowSealedStatusEnum;
import com.yiling.open.erp.enums.ErpFlowSealedTypeEnum;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;
import com.yiling.open.erp.service.ErpFlowSealedService;
import com.yiling.open.erp.service.ErpShopMappingService;
import com.yiling.open.erp.service.ErpShopSaleFlowService;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 连锁门店销售明细信息表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-03-20
 */
@Slf4j
@Service(value = "erpShopSaleFlowService")
public class ErpShopSaleFlowServiceImpl extends ErpEntityServiceImpl implements ErpShopSaleFlowService {

    @Autowired
    private ErpClientService          erpClientService;
    @Autowired
    private ErpShopSaleFlowMapper     erpShopSaleFlowMapper;
    @Autowired
    private ErpFlowSealedService      erpFlowSealedService;
    @Autowired
    private ErpFlowGoodsConfigService erpFlowGoodsConfigService;
    @Autowired
    private ErpShopMappingService erpShopMappingService;
    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    @DubboReference
    ErpShopMappingApi erpShopMappingApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    FlowShopSaleApi flowShopSaleApi;
    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private FlowGoodsSpecMappingApi flowGoodsSpecMappingApi;
    @DubboReference
    private FlowEnterpriseGoodsMappingApi flowEnterpriseGoodsMappingApi;
    @DubboReference
    private CrmGoodsApi crmGoodsApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpShopSaleFlowMapper;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpShopSaleFlowDO erpShopSaleFlowDO = (ErpShopSaleFlowDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpShopSaleFlowDO.getSuId(), erpShopSaleFlowDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpShopSaleFlowMapper.updateSyncStatusAndMsg(erpShopSaleFlowDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        // 总店、门店关系中同步状态校验，根据 总店的eid+门店编码 查询关系
        // 校验连锁总店对应关系、并设置门店信息
        FlowShopSaleDTO flowShopSaleInfo = new FlowShopSaleDTO();
        String erpShopMappingError = checkErpShopMapping(erpClient.getRkSuId(), erpShopSaleFlowDO, flowShopSaleInfo);
        if (StrUtil.isNotBlank(erpShopMappingError)) {
            erpShopSaleFlowMapper.updateSyncStatusAndMsg(erpShopSaleFlowDO.getId(), SyncStatus.FAIL.getCode(), erpShopMappingError);
            return false;
        }
        // 销售时间校验，销售时间不能超过6个月
        if (!checkSoTime(erpShopSaleFlowDO, erpClient)) {
            erpShopSaleFlowMapper.updateSyncStatusAndMsg(erpShopSaleFlowDO.getId(), SyncStatus.FAIL.getCode(), "销售时间已超过6个月，不能同步");
            return false;
        }

        if (erpShopSaleFlowDO.getOperType() == 3) {
            Long id = erpShopSaleFlowDO.getId();
            erpShopSaleFlowDO = erpShopSaleFlowMapper.selectById(id);
            if (erpShopSaleFlowDO == null) {
                // 如果是删除商品库存信息，但是查询op库数据为空
                erpShopSaleFlowMapper.updateSyncStatusAndMsg(id, 3, "查询连锁纯销流向信息为空");
                return false;
            }
        }
        return synErpSaleFlow(erpShopSaleFlowDO, erpClient, flowShopSaleInfo);
    }

    private String checkErpShopMapping(Long rkSuId, ErpShopSaleFlowDO erpShopSaleFlowDO, FlowShopSaleDTO flowShopSaleInfo) {
        String shopNo = erpShopSaleFlowDO.getShopNo();
        // 根据总店eid、门店编码，查询连锁总店对应关系
        ErpShopMappingDTO erpShopMappingDTO = erpShopMappingApi.findByMainShopAndShopCode(rkSuId, erpShopSaleFlowDO.getShopNo());
        if (ObjectUtil.isNull(erpShopMappingDTO)){
            return "连锁总店对应关系为空";
        }
        Integer syncStatus = erpShopMappingDTO.getSyncStatus();
        if (ObjectUtil.isNull(syncStatus) || 1 != syncStatus.intValue()) {
            return "连锁总店对应关系的同步状态未开启";
        }
        // 查询门店的企业信息
        Long shopEid = erpShopMappingDTO.getShopEid();
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(shopEid);
        if (ObjectUtil.isNull(enterpriseDTO)) {
            return "门店对应的企业信息为空";
        }
        // 设置门店信息
        flowShopSaleInfo.setShopEid(shopEid);
        flowShopSaleInfo.setShopEname(enterpriseDTO.getName());
        flowShopSaleInfo.setProvinceName(enterpriseDTO.getProvinceName());
        flowShopSaleInfo.setProvinceCode(enterpriseDTO.getProvinceCode());
        flowShopSaleInfo.setCityCode(enterpriseDTO.getCityCode());
        flowShopSaleInfo.setCityName(enterpriseDTO.getCityName());
        flowShopSaleInfo.setRegionCode(enterpriseDTO.getRegionCode());
        flowShopSaleInfo.setRegionName(enterpriseDTO.getRegionName());
        return null;
    }

    private boolean checkSoTime(ErpShopSaleFlowDO erpShopSaleFlowDO, ErpClientDTO erpClient) {
        if (ObjectUtil.isNotNull(erpClient.getDataInitStatus()) && ObjectUtil.equal(1, erpClient.getDataInitStatus())) {
            // 初始化状态是已完成，则限制同步的销售时间为6个月以内
            // 销售时间
            Date soTime = erpShopSaleFlowDO.getSoTime();
            // 包含当月向前推6个月之前的月份，推前的第7个月及之前的数据为限制同步的
            String monthBackup6 = BackupUtil.monthBackup(6);
            if (DateUtil.beginOfMonth(soTime).getTime() <= DateUtil.beginOfMonth(DateUtil.parse(monthBackup6, "yyyy-MM")).getTime()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void syncShopSaleFlow() {
        List<ErpShopSaleFlowDO> erpSaleFlowList = synSaleFlowPage();
        syncSaleFlow(erpSaleFlowList);
    }

    public void syncSaleFlow(List<ErpShopSaleFlowDO> erpSaleFlowList) {
        for (ErpShopSaleFlowDO erpSaleFlowDO : erpSaleFlowList) {
            int i = erpShopSaleFlowMapper.updateSyncStatusByStatusAndId(erpSaleFlowDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpSaleFlowDO);
            }
        }
    }

    @Override
    public List<ErpShopSaleFlowDO> synSaleFlowPage() {
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setClientStatus(1);
        request.setSyncStatus(1);

        List<ErpShopSaleFlowDO> erpSaleFlowList = new ArrayList<>();

        Page<ErpClientDO> pageSale = null;
        int currentSale = 1;
        do {
            request.setCurrent(currentSale);
            request.setSize(20);
            pageSale = erpClientService.page(request);

            if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                break;
            }

            List<QuerySaleFlowConditionRequest> erpClientDOList = new ArrayList<>();
            for (ErpClientDO e : pageSale.getRecords()) {
                QuerySaleFlowConditionRequest querySaleFlowConditionRequest = new QuerySaleFlowConditionRequest();
                querySaleFlowConditionRequest.setSuId(e.getSuId());
                querySaleFlowConditionRequest.setSuDeptNo(e.getSuDeptNo());
                erpClientDOList.add(querySaleFlowConditionRequest);
            }
            List<ErpShopSaleFlowDO> erpSaleFlowDOList = erpShopSaleFlowMapper.syncSaleFlowPage(erpClientDOList);
            erpSaleFlowList.addAll(erpSaleFlowDOList);
            currentSale = currentSale + 1;
        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));
        return erpSaleFlowList;
    }

    @Override
    public void unLockSynShopSaleFlow(ErpFlowSealedUnLockRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getRkSuId()) || StrUtil.isBlank(request.getStartMonth()) || StrUtil.isBlank(request.getEndMonth())) {
            return;
        }
        // 删除线上数据
        DeleteFlowShopSaleByUnlockRequest deleteFlowShopSaleRequest = new DeleteFlowShopSaleByUnlockRequest();
        deleteFlowShopSaleRequest.setEid(request.getRkSuId());
        deleteFlowShopSaleRequest.setStartMonth(request.getStartMonth());
        deleteFlowShopSaleRequest.setEndMonth(request.getEndMonth());
        flowShopSaleApi.deleteFlowShopSaleBydEidAndSoTime(deleteFlowShopSaleRequest);
        // 同步OP库数据
        List<ErpShopSaleFlowDO> erpShopSaleFlowList = erpShopSaleFlowMapper.unLockSynShopSaleFlow(request);
        if (CollUtil.isEmpty(erpShopSaleFlowList)) {
            return;
        }
        for (ErpShopSaleFlowDO erpShopSaleFlowDO : erpShopSaleFlowList) {
            int i = erpShopSaleFlowMapper.updateSyncStatusByStatusAndId(erpShopSaleFlowDO.getId(), com.yiling.open.erp.enums.SyncStatus.SYNCING.getCode(), com.yiling.open.erp.enums.SyncStatus.SUCCESS.getCode(), "连锁门店连锁纯销流向解封处理");
            if (i > 0) {
                onlineData(erpShopSaleFlowDO);
            }
        }
    }

    public boolean synErpSaleFlow(ErpShopSaleFlowDO erpSaleFlowDO, ErpClientDTO erpClient, FlowShopSaleDTO flowShopSaleInfo) {
        Long id = erpSaleFlowDO.getId();
        try {
            // 已封存连锁纯销流向
            if (checkErpFlowSealedLock(erpSaleFlowDO, erpClient)) {
                erpShopSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功：此月份线上连锁纯销流向已封存");
                return true;
            }

            // 流向非以岭商品配置
            ErpFlowGoodsConfigDO erpFlowGoodsConfigDO = erpFlowGoodsConfigService.getByEidAndGoodsInSn(erpClient.getRkSuId(), erpSaleFlowDO.getGoodsInSn());

            if (erpSaleFlowDO.getSoManufacturer().contains(ErpConstants.YI_LING) || ObjectUtil.isNotNull(erpFlowGoodsConfigDO)) {
                // 只同步生产厂家包含以岭、或有配置流向非以岭商品的
                syncYilingSaleFlow(erpSaleFlowDO, erpClient, flowShopSaleInfo);
                erpShopSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功");
            } else {
                // 未配置非以岭商品
                erpShopSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "生产厂家不包含以岭、且未配置此流向非以岭商品");
            }
            return true;
        } catch (Exception e) {
            log.error("[连锁纯销流向]同步出现错误", e);
            erpShopSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
        }
        return false;
    }


    private void syncYilingSaleFlow(ErpShopSaleFlowDO erpShopSaleFlowDO, ErpClientDTO erpClient, FlowShopSaleDTO flowShopSaleInfo) {
        long eid = erpClient.getRkSuId().longValue();
        // 线上变更前数据
        QueryFlowShopSaleRequest flowSaleRequest = new QueryFlowShopSaleRequest();
        flowSaleRequest.setEid(eid);
        flowSaleRequest.setSoId(erpShopSaleFlowDO.getSoId());
        List<FlowShopSaleDTO> oldFlowShopSaleList = flowShopSaleApi.getFlowSaleDTOBySoIdAndEid(flowSaleRequest);
        // 线上变更后数据
        List<FlowShopSaleDTO> newFlowShopSaleList = new ArrayList<>();

        if (erpShopSaleFlowDO.getOperType() == 3) {
            //删除逻辑
            if (CollUtil.isNotEmpty(oldFlowShopSaleList)) {
                List<Long> ids = oldFlowShopSaleList.stream().map(FlowShopSaleDTO::getId).distinct().collect(Collectors.toList());
                // 洞察系统日流向查询中，也会查软删除的数据，这里先修改数据标签，再软删除
                flowShopSaleApi.updateDataTagByIdList(ids, DataTagEnum.DELETE.getCode());
                flowShopSaleApi.deleteByIdList(ids);
            }
        } else {
            Boolean isNotSale = stringRedisTemplate.hasKey(ErpConstants.erp_flow_sale_Statistics);
            if (isNotSale) {
                stringRedisTemplate.opsForSet().remove(ErpConstants.erp_flow_sale_Statistics, String.valueOf(erpClient.getRkSuId()));
            }
            //修改逻辑
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
            //匹配商业公司crm编码
            CrmEnterpriseDTO crmEnterpriseDTO = null;
            if (erpClient.getCrmEnterpriseId() != null && erpClient.getCrmEnterpriseId() != 0) {
                crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(erpClient.getCrmEnterpriseId());
            }

            SaveOrUpdateFlowShopSaleRequest updateRequest = PojoUtils.map(erpShopSaleFlowDO, SaveOrUpdateFlowShopSaleRequest.class);
            updateRequest.setEid(enterpriseDTO.getId());
            updateRequest.setEname(enterpriseDTO.getName());
            updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
            updateRequest.setShopEid(flowShopSaleInfo.getShopEid());
            updateRequest.setShopEname(flowShopSaleInfo.getShopEname());
            // 门店省市区
            updateRequest.setProvinceName(flowShopSaleInfo.getProvinceName());
            updateRequest.setProvinceCode(flowShopSaleInfo.getProvinceCode());
            updateRequest.setCityCode(flowShopSaleInfo.getCityCode());
            updateRequest.setCityName(flowShopSaleInfo.getCityName());
            updateRequest.setRegionCode(flowShopSaleInfo.getRegionCode());
            updateRequest.setRegionName(flowShopSaleInfo.getRegionName());
            updateRequest.setOpUserId(0L);
            //  设置销售月份
            Date soTime = updateRequest.getSoTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            updateRequest.setSoMonth(sdf.format(soTime));

            // 匹配商品+规格id
            updateRequest.setSpecificationId(this.mathGoodsSpec(updateRequest.getGoodsName(), updateRequest.getSoSpecifications(), updateRequest.getSoManufacturer()));
            if (crmEnterpriseDTO != null) {
                updateRequest.setCrmGoodsCode(this.mathGoodsCrmCode(updateRequest.getGoodsName(), updateRequest.getSoSpecifications(), updateRequest.getSoManufacturer(), updateRequest.getSoUnit(), updateRequest.getGoodsInSn(), crmEnterpriseDTO));
                if (updateRequest.getCrmGoodsCode() == 0L && updateRequest.getSpecificationId() != 0L) {
                    List<CrmGoodsDTO> crmGoodsDTOList = crmGoodsApi.getCrmGoodsBySpecificationId(updateRequest.getSpecificationId());
                    if (CollUtil.isNotEmpty(crmGoodsDTOList)) {
                        if (crmGoodsDTOList.size() == 1) {
                            updateRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                        } else {
                            updateRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                            updateRequest.setGoodsPossibleError(1);
                        }
                    }
                }
            }
            Integer cnt = erpShopSaleFlowDO.getCnt();
            if (CollUtil.isNotEmpty(oldFlowShopSaleList)) {
                if (cnt != oldFlowShopSaleList.size()) {
                    List<Long> ids = oldFlowShopSaleList.stream().map(FlowShopSaleDTO::getId).distinct().collect(Collectors.toList());
                    flowShopSaleApi.updateDataTagByIdList(ids, DataTagEnum.DELETE.getCode());
                    flowShopSaleApi.deleteByIdList(ids);

                    for (int i = 0; i < cnt; i++) {
                        if (crmEnterpriseDTO != null) {
                            updateRequest.setCrmCode(crmEnterpriseDTO.getCode());
                        }
                        FlowShopSaleDTO newFlowShopSale = flowShopSaleApi.insertFlowSale(updateRequest);
                        newFlowShopSaleList.add(newFlowShopSale);
                    }
                } else {
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setCrmCode(crmEnterpriseDTO.getCode());
                    }
                    updateRequest.setOpUserId(0L);
                    updateRequest.setOpTime(new Date());
                    for (FlowShopSaleDTO flowShopSale : oldFlowShopSaleList) {
                        updateRequest.setId(flowShopSale.getId());
                        flowShopSaleApi.updateFlowSaleById(updateRequest);
                    }

                    oldFlowShopSaleList.forEach(o -> {
                        FlowShopSaleDTO newFlowShopSale = PojoUtils.map(updateRequest, FlowShopSaleDTO.class);
                        newFlowShopSale.setId(o.getId());
                        newFlowShopSaleList.add(newFlowShopSale);
                    });
                }
            } else {
                //新增
                for (int i = 0; i < cnt; i++) {
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setCrmCode(crmEnterpriseDTO.getCode());
                    }
                    FlowShopSaleDTO newFlowShopSale = flowShopSaleApi.insertFlowSale(updateRequest);
                    newFlowShopSaleList.add(newFlowShopSale);
                }
            }
        }
    }

    private boolean checkErpFlowSealedLock(ErpShopSaleFlowDO erpSaleFlowDO, ErpClientDTO erpClient) {
        QueryErpFlowSealedRequest requerst = new QueryErpFlowSealedRequest();
        requerst.setEid(erpClient.getRkSuId());
        requerst.setType(ErpFlowSealedTypeEnum.SHOP_SALE.getCode());
        requerst.setMonth(DateUtil.format(erpSaleFlowDO.getSoTime(), "yyyy-MM"));
        ErpFlowSealedDO erpFlowSealed = erpFlowSealedService.getErpFlowSealedByEidAndTypeAndMonth(requerst);
        if (ObjectUtil.isNotNull(erpFlowSealed) && ObjectUtil.equal(ErpFlowSealedStatusEnum.LOCK.getCode(), erpFlowSealed.getStatus())) {
            return true;
        }
        return false;
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

    private Long mathGoodsCrmCode(String goodsName, String spec, String manufacturer, String unit, String goodsInnerCode, CrmEnterpriseDTO crmEnterpriseDTO) {
        if (StrUtil.isEmpty(goodsName) || StrUtil.isEmpty(spec)) {
            return 0L;
        }
        FlowEnterpriseGoodsMappingDTO flowGoodsPriceRelationDTO = flowEnterpriseGoodsMappingApi.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(goodsName, spec, crmEnterpriseDTO.getId());
        if (flowGoodsPriceRelationDTO != null) {
            return flowGoodsPriceRelationDTO.getCrmGoodsCode();
        } else {
            SaveFlowEnterpriseGoodsMappingRequest saveFlowEnterpriseGoodsMappingRequest = new SaveFlowEnterpriseGoodsMappingRequest();
            saveFlowEnterpriseGoodsMappingRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            saveFlowEnterpriseGoodsMappingRequest.setFlowGoodsName(goodsName);
            saveFlowEnterpriseGoodsMappingRequest.setFlowSpecification(spec);
            saveFlowEnterpriseGoodsMappingRequest.setFlowManufacturer(manufacturer);
            saveFlowEnterpriseGoodsMappingRequest.setFlowUnit(unit);
            saveFlowEnterpriseGoodsMappingRequest.setFlowGoodsInSn(goodsInnerCode);
            saveFlowEnterpriseGoodsMappingRequest.setLastUploadTime(new Date());
            saveFlowEnterpriseGoodsMappingRequest.setOpTime(new Date());
            flowEnterpriseGoodsMappingApi.save(saveFlowEnterpriseGoodsMappingRequest);
            return 0L;
        }
    }
}
