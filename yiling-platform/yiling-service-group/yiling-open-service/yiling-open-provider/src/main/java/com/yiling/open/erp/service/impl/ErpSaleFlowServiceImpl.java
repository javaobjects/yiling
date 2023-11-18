package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.enums.CrmGoodsStatusEnum;
import com.yiling.dataflow.flow.api.FlowEnterpriseCustomerMappingApi;
import com.yiling.dataflow.flow.api.FlowEnterpriseGoodsMappingApi;
import com.yiling.dataflow.flow.api.SyncFlowSaleApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.api.FlowSettlementEnterpriseTagApi;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowSaleRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.open.backup.util.BackupUtil;
import com.yiling.open.config.ErpFlowOrderSourceConfig;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpSaleFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.dto.request.QuerySaleFlowConditionRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;
import com.yiling.open.erp.entity.ErpFlowSealedDO;
import com.yiling.open.erp.entity.ErpSaleFlowDO;
import com.yiling.open.erp.enums.DataTagEnum;
import com.yiling.open.erp.enums.ErpFlowSealedStatusEnum;
import com.yiling.open.erp.enums.ErpFlowSealedTypeEnum;
import com.yiling.open.erp.enums.FlowsaleOrderSourceEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;
import com.yiling.open.erp.service.ErpFlowSealedService;
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
 * <p>
 * 流向销售明细信息表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@Slf4j
@Service(value = "erpSaleFlowService")
public class ErpSaleFlowServiceImpl extends ErpEntityServiceImpl implements ErpSaleFlowService {

    @DubboReference
    private   EnterpriseApi                    enterpriseApi;
    @DubboReference(timeout = 10 * 1000)
    private   FlowSaleApi                      flowSaleApi;
    @DubboReference
    private   GoodsApi                         goodsApi;
    @DubboReference
    private   PopGoodsApi                      popGoodsApi;
    @DubboReference(timeout = 10 * 1000)
    private   FlowGoodsRelationApi             flowGoodsRelationApi;
    @DubboReference
    private   FlowEnterpriseGoodsMappingApi    flowEnterpriseGoodsMappingApi;
    @DubboReference
    private   FlowGoodsSpecMappingApi          flowGoodsSpecMappingApi;
    @DubboReference
    private   FlowSettlementEnterpriseTagApi   flowSettlementEnterpriseTagTagApi;
    @DubboReference
    private   CrmEnterpriseApi                 crmEnterpriseApi;
    @DubboReference
    private   CrmSupplierApi                   crmSupplierApi;
    @DubboReference
    private   FlowGoodsRelationEditTaskApi     flowGoodsRelationEditTaskApi;
    @DubboReference
    private   FlowEnterpriseCustomerMappingApi flowEnterpriseCustomerMappingApi;
    @DubboReference
    private   CrmGoodsApi                      crmGoodsApi;
    @DubboReference
    private   SyncFlowSaleApi                  syncFlowSaleApi;
    @DubboReference
    private   CrmGoodsInfoApi                  crmGoodsInfoApi;
    @Autowired
    protected RedisDistributedLock             redisDistributedLock;
    @Autowired
    private   ErpSaleFlowMapper                erpSaleFlowMapper;
    @Autowired
    private   ErpClientService                 erpClientService;
    @Autowired
    private   ErpFlowOrderSourceConfig         erpFlowOrderSourceConfig;
    @Autowired
    private   ErpFlowSealedService             erpFlowSealedService;
    @Autowired
    private   ErpFlowGoodsConfigService        erpFlowGoodsConfigService;
    @Resource
    private   StringRedisTemplate              stringRedisTemplate;

    @Autowired
    @Lazy
    ErpSaleFlowServiceImpl _this;
    @DubboReference
    MqMessageSendApi       mqMessageSendApi;

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpSaleFlowMapper;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpSaleFlowDO erpSaleFlowDO = (ErpSaleFlowDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpSaleFlowDO.getSuId(), erpSaleFlowDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpSaleFlowMapper.updateSyncStatusAndMsg(erpSaleFlowDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        // 销售时间校验，销售时间不能超过6个月
        if (ObjectUtil.isNotNull(erpClient.getDataInitStatus()) && ObjectUtil.equal(1, erpClient.getDataInitStatus())) {
            // 初始化状态是已完成，则限制同步的销售时间为6个月以内
            // 销售时间
            Date soTime = erpSaleFlowDO.getSoTime();
            // 包含当月向前推6个月之前的月份，推前的第7个月及之前的数据为限制同步的
            String monthBackup6 = BackupUtil.monthBackup(6);
            if (DateUtil.beginOfMonth(soTime).getTime() <= DateUtil.beginOfMonth(DateUtil.parse(monthBackup6, "yyyy-MM")).getTime()) {
                erpSaleFlowMapper.updateSyncStatusAndMsg(erpSaleFlowDO.getId(), SyncStatus.FAIL.getCode(), "销售时间已超过6个月，不能同步");
                return false;
            }
        }
        if (erpSaleFlowDO.getOperType() == 3) {
            Long id = erpSaleFlowDO.getId();
            erpSaleFlowDO = erpSaleFlowMapper.selectById(id);
            if (erpSaleFlowDO == null) {
                // 如果是删除商品库存信息，但是查询op库数据为空
                erpSaleFlowMapper.updateSyncStatusAndMsg(id, 3, "查询销售流向信息为空");
                return false;
            }
        }
        return synErpSaleFlow(erpSaleFlowDO, erpClient);
    }

    @Override
    public void synSaleFlow() {
        List<ErpSaleFlowDO> erpSaleFlowList = synSaleFlowPage();
        syncSaleFlow(erpSaleFlowList);
    }

    public void syncSaleFlow(List<ErpSaleFlowDO> erpSaleFlowList) {
        for (ErpSaleFlowDO erpSaleFlowDO : erpSaleFlowList) {
            int i = erpSaleFlowMapper.updateSyncStatusByStatusAndId(erpSaleFlowDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpSaleFlowDO);
            }
        }
    }

    @Override
    public List<ErpSaleFlowDO> synSaleFlowPage() {
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setClientStatus(1);
        request.setSyncStatus(1);

        List<ErpSaleFlowDO> erpSaleFlowList = new ArrayList<>();

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
            List<ErpSaleFlowDO> erpSaleFlowDOList = erpSaleFlowMapper.syncSaleFlowPage(erpClientDOList);
            erpSaleFlowList.addAll(erpSaleFlowDOList);
            currentSale = currentSale + 1;
        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));
        return erpSaleFlowList;
    }

    @Override
    public void unLockSynSaleFlow(ErpFlowSealedUnLockRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getRkSuId()) || StrUtil.isBlank(request.getStartMonth()) || StrUtil.isBlank(request.getEndMonth())) {
            return;
        }
        // 删除线上数据
        DeleteFlowSaleByUnlockRequest deleteFlowSaleRequest = new DeleteFlowSaleByUnlockRequest();
        deleteFlowSaleRequest.setEid(request.getRkSuId());
        deleteFlowSaleRequest.setStartMonth(request.getStartMonth());
        deleteFlowSaleRequest.setEndMonth(request.getEndMonth());
        flowSaleApi.deleteFlowSaleBydEidAndSoTime(deleteFlowSaleRequest);
        // 同步OP库数据
        List<ErpSaleFlowDO> erpSaleFlowList = erpSaleFlowMapper.unLockSynSaleFlow(request);
        if (CollUtil.isEmpty(erpSaleFlowList)) {
            return;
        }
        for (ErpSaleFlowDO erpSaleFlowDO : erpSaleFlowList) {
            int i = erpSaleFlowMapper.updateSyncStatusByStatusAndId(erpSaleFlowDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.SUCCESS.getCode(), "销售流向解封处理");
            if (i > 0) {
                onlineData(erpSaleFlowDO);
            }
        }
    }

    public boolean synErpSaleFlow(ErpSaleFlowDO erpSaleFlowDO, ErpClientDTO erpClient) {
        Long id = erpSaleFlowDO.getId();
        try {
            // 已封存销售流向
            if (checkErpFlowSealedLock(erpSaleFlowDO, erpClient)) {
                erpSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功：此月份线上销售流向已封存");
                return true;
            }

            // 流向非以岭商品配置
            ErpFlowGoodsConfigDO erpFlowGoodsConfigDO = erpFlowGoodsConfigService.getByEidAndGoodsInSn(erpClient.getRkSuId(), erpSaleFlowDO.getGoodsInSn());

            if (erpSaleFlowDO.getSoManufacturer().contains(ErpConstants.YI_LING) || ObjectUtil.isNotNull(erpFlowGoodsConfigDO)) {
                // 只同步生产厂家包含以岭、或有配置流向非以岭商品的
                syncYilingSaleFlow(erpSaleFlowDO, erpClient);
                // 封存流向
                erpSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功");
            } else {
                // 封存流向
                erpSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "生产厂家不包含以岭、且未配置此流向非以岭商品");
            }
            return true;
        } catch (Exception e) {
            log.error("[销售流向]同步出现错误", e);
            erpSaleFlowMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
        }
        return false;
    }

    private boolean checkErpFlowSealedLock(ErpSaleFlowDO erpSaleFlowDO, ErpClientDTO erpClient) {
        QueryErpFlowSealedRequest requerst = new QueryErpFlowSealedRequest();
        requerst.setEid(erpClient.getRkSuId());
        requerst.setType(ErpFlowSealedTypeEnum.SALE.getCode());
        requerst.setMonth(DateUtil.format(erpSaleFlowDO.getSoTime(), "yyyy-MM"));
        ErpFlowSealedDO erpFlowSealed = erpFlowSealedService.getErpFlowSealedByEidAndTypeAndMonth(requerst);
        if (ObjectUtil.isNotNull(erpFlowSealed) && ObjectUtil.equal(ErpFlowSealedStatusEnum.LOCK.getCode(), erpFlowSealed.getStatus())) {
            return true;
        }
        return false;
    }

    private void syncYilingSaleFlow(ErpSaleFlowDO erpSaleFlowDO, ErpClientDTO erpClient) {
        long eid = erpClient.getRkSuId().longValue();
        // 线上变更前数据
        QueryFlowSaleRequest flowSaleRequest = new QueryFlowSaleRequest();
        flowSaleRequest.setEid(eid);
        flowSaleRequest.setSoId(erpSaleFlowDO.getSoId());
        List<FlowSaleDTO> oldFlowSaleList = flowSaleApi.getFlowSaleDTOBySoIdAndEid(flowSaleRequest);
        // 线上变更后数据
        List<FlowSaleDTO> newFlowSaleList = new ArrayList<>();

        if (erpSaleFlowDO.getOperType() == 3) {
            //删除逻辑
            if (CollUtil.isNotEmpty(oldFlowSaleList)) {
                List<Long> ids = oldFlowSaleList.stream().map(FlowSaleDTO::getId).distinct().collect(Collectors.toList());
                // 洞察系统日流向查询中，也会查软删除的数据，这里先修改数据标签，再软删除
                flowSaleApi.updateDataTagByIdList(ids, DataTagEnum.DELETE.getCode());
                flowSaleApi.deleteByIdList(ids);
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
            CrmSupplierDTO crmSupplierDTO = null;
            if (erpClient.getCrmEnterpriseId() != null && erpClient.getCrmEnterpriseId() != 0) {
                crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(erpClient.getCrmEnterpriseId());
                //  匹配经销商级别
                crmSupplierDTO = crmSupplierApi.getCrmSupplierByCrmEnterId(crmEnterpriseDTO.getId());
            }

            SaveOrUpdateFlowSaleRequest updateRequest = PojoUtils.map(erpSaleFlowDO, SaveOrUpdateFlowSaleRequest.class);
            updateRequest.setOpUserId(0L);
            // 订单来源
            buildOrderSource(erpSaleFlowDO, updateRequest);
            //  设置销售月份
            Date soTime = updateRequest.getSoTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            updateRequest.setSoMonth(sdf.format(soTime));

            // 匹配以岭品关系 //todo 需要改成批量
            getYlGoodsId(erpClient, erpSaleFlowDO);

            // 匹配商品+规格id
            updateRequest.setSpecificationId(this.mathGoodsSpec(updateRequest.getGoodsName(), updateRequest.getSoSpecifications(), updateRequest.getSoManufacturer()));
            if (crmEnterpriseDTO != null) {
                updateRequest.setEnterpriseCrmCode(this.mathEnterpriseCrmCode(updateRequest.getEnterpriseName(), crmEnterpriseDTO));
                updateRequest.setCrmGoodsCode(this.mathGoodsCrmCode(updateRequest.getGoodsName(), updateRequest.getSoSpecifications(), updateRequest.getSoManufacturer(), updateRequest.getSoUnit(), updateRequest.getGoodsInSn(), crmEnterpriseDTO));
//                if (updateRequest.getCrmGoodsCode() == 0L && updateRequest.getSpecificationId() != 0L) {
//                    List<CrmGoodsDTO> crmGoodsDTOList = crmGoodsApi.getCrmGoodsBySpecificationId(updateRequest.getSpecificationId());
//                    if (CollUtil.isNotEmpty(crmGoodsDTOList)) {
//                        if (crmGoodsDTOList.size() == 1) {
//                            updateRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
//                        } else {
//                            updateRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
//                            updateRequest.setGoodsPossibleError(1);
//                        }
//                    }
//                }
            }
            Integer cnt = erpSaleFlowDO.getCnt();
            if (CollUtil.isNotEmpty(oldFlowSaleList)) {
                if (cnt != oldFlowSaleList.size()) {
                    //  判断数量，新增差值的数据
                    if (cnt > oldFlowSaleList.size()) {
                        int diff = cnt - oldFlowSaleList.size();
                        // 新增
                        for (int i = 0; i < diff; i++) {
                            updateRequest.setEid(enterpriseDTO.getId());
                            updateRequest.setEname(enterpriseDTO.getName());
                            updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                            if (crmEnterpriseDTO != null) {
                                updateRequest.setCrmCode(crmEnterpriseDTO.getCode());
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
                            updateRequest.setDataTag(DataTagEnum.EXC_ADD.getCode());
                            FlowSaleDTO newFlowSaleDTO = flowSaleApi.insertFlowSale(updateRequest);
                            newFlowSaleList.add(newFlowSaleDTO);
                        }
                    } else {
                        // 删除多余的数据
                        int diff = oldFlowSaleList.size() - cnt;
                        oldFlowSaleList.sort(Comparator.comparing(FlowSaleDTO::getId).reversed());  // id倒序
                        List<Long> deleteIdList = new ArrayList<>();

                        for (FlowSaleDTO flowSaleDTO : oldFlowSaleList) {
                            if (diff == 0) {
                                break;
                            }
                            deleteIdList.add(flowSaleDTO.getId());
                            diff--;
                        }
                        flowSaleApi.updateDataTagByIdList(deleteIdList, DataTagEnum.DELETE.getCode());
                        flowSaleApi.deleteByIdList(deleteIdList);
                    }
                } else {
                    // 业务不会执行该方法块代码
                    updateRequest.setEid(enterpriseDTO.getId());
                    updateRequest.setEname(enterpriseDTO.getName());
                    updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                        updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                        updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                        updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                        updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                        updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                        updateRequest.setCrmCode(crmEnterpriseDTO.getCode());
                    }
                    if (crmSupplierDTO != null) {
                        updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                    }
                    updateRequest.setOpUserId(0L);
                    updateRequest.setOpTime(new Date());
                    for (FlowSaleDTO flowSale : oldFlowSaleList) {
                        updateRequest.setId(flowSale.getId());
                        flowSaleApi.updateFlowSaleById(updateRequest);
                    }

                    oldFlowSaleList.forEach(o -> {
                        FlowSaleDTO newFlowSale = PojoUtils.map(updateRequest, FlowSaleDTO.class);
                        newFlowSale.setId(o.getId());
                        newFlowSaleList.add(newFlowSale);
                    });
                }
            } else {
                //新增
                for (int i = 0; i < cnt; i++) {
                    updateRequest.setEid(enterpriseDTO.getId());
                    updateRequest.setEname(enterpriseDTO.getName());
                    updateRequest.setCrmEnterpriseId(erpClient.getCrmEnterpriseId());
                    if (crmSupplierDTO != null) {
                        updateRequest.setSupplierLevel(crmSupplierDTO.getSupplierLevel());
                    }
                    if (crmEnterpriseDTO != null) {
                        updateRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
                        updateRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
                        updateRequest.setCityCode(crmEnterpriseDTO.getCityCode());
                        updateRequest.setCityName(crmEnterpriseDTO.getCityName());
                        updateRequest.setRegionCode(crmEnterpriseDTO.getRegionCode());
                        updateRequest.setRegionName(crmEnterpriseDTO.getRegionName());
                        updateRequest.setCrmCode(crmEnterpriseDTO.getCode());
                    }
                    FlowSaleDTO newFlowSale = flowSaleApi.insertFlowSale(updateRequest);
                    newFlowSaleList.add(newFlowSale);
                }
            }
        }

        List<Long> ids = new ArrayList<>();
        if (CollUtil.isNotEmpty(oldFlowSaleList)) {
            ids.addAll(oldFlowSaleList.stream().map(e -> e.getId()).collect(Collectors.toList()));
        }
        if (CollUtil.isNotEmpty(newFlowSaleList)) {
            ids.addAll(newFlowSaleList.stream().map(e -> e.getId()).collect(Collectors.toList()));
        }
        syncFlowSaleApi.insertList(ids.stream().distinct().collect(Collectors.toList()));
    }

    private void getYlGoodsId(ErpClientDTO erpClient, ErpSaleFlowDO erpSaleFlowDO) {
        if (ObjectUtil.isNotNull(erpClient) && ObjectUtil.isNotNull(erpSaleFlowDO) &&
                ObjectUtil.isNotNull(erpClient.getRkSuId()) && StrUtil.isNotBlank(erpClient.getClientName()) && StrUtil.isNotBlank(erpSaleFlowDO.getGoodsInSn())
                && StrUtil.isNotBlank(erpSaleFlowDO.getGoodsName()) && StrUtil.isNotBlank(erpSaleFlowDO.getSoSpecifications())) {
            QueryFlowGoodsRelationYlGoodsIdRequest ylGoodsIdRequest = new QueryFlowGoodsRelationYlGoodsIdRequest();
            ylGoodsIdRequest.setEid(erpClient.getRkSuId());
            ylGoodsIdRequest.setEname(erpClient.getClientName());
            ylGoodsIdRequest.setGoodsInSn(erpSaleFlowDO.getGoodsInSn());
            ylGoodsIdRequest.setGoodsName(erpSaleFlowDO.getGoodsName());
            ylGoodsIdRequest.setGoodsSpecifications(erpSaleFlowDO.getSoSpecifications());
            MqMessageBO mqMessageBO = _this.sendPrepare(Constants.TOPIC_ERP_FLOW_GOODS_RELATION_SAVEORUPDATE, Constants.TAG_ERP_FLOW_GOODS_RELATION_SAVEORUPDATE, JSON.toJSONString(ylGoodsIdRequest));
            mqMessageSendApi.send(mqMessageBO);
        }
    }


    private void buildOrderSource(ErpSaleFlowDO erpSaleFlowDO, SaveOrUpdateFlowSaleRequest updateRequest) {
        String source = FlowsaleOrderSourceEnum.OTHERS.getCode();
        Map<String, Integer> saleFlowOrderSourceMap = erpFlowOrderSourceConfig.getSaleFlowOrderSourceMap();
        Integer sourceType = saleFlowOrderSourceMap.get(OpenStringUtils.clearAllSpace(erpSaleFlowDO.getSoSource()));
        if (ObjectUtil.isNotNull(sourceType)) {
            source = sourceType.toString();
        }
        updateRequest.setSoSource(source);
    }

    private String mathEnterpriseCrmCode(String customerName, CrmEnterpriseDTO crmEnterpriseDTO) {
        if (StrUtil.isEmpty(customerName)) {
            return "";
        }
        customerName=customerName.trim();
        FlowEnterpriseCustomerMappingDTO flowCustomerMappingDTO = flowEnterpriseCustomerMappingApi.findByCustomerNameAndCrmEnterpriseId(customerName, crmEnterpriseDTO.getId());
        if (flowCustomerMappingDTO == null || flowCustomerMappingDTO.getCrmOrgId() == 0) {
            SaveFlowEnterpriseCustomerMappingRequest saveFlowEnterpriseCustomerMappingRequest = new SaveFlowEnterpriseCustomerMappingRequest();
            saveFlowEnterpriseCustomerMappingRequest.setFlowCustomerName(customerName);
            saveFlowEnterpriseCustomerMappingRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            saveFlowEnterpriseCustomerMappingRequest.setEnterpriseName(crmEnterpriseDTO.getName());
            saveFlowEnterpriseCustomerMappingRequest.setProvince(crmEnterpriseDTO.getProvinceName());
            saveFlowEnterpriseCustomerMappingRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
            saveFlowEnterpriseCustomerMappingRequest.setLastUploadTime(new Date());
            saveFlowEnterpriseCustomerMappingRequest.setOpTime(new Date());
            CrmEnterpriseDTO crmEnterprise = crmEnterpriseApi.getCrmEnterpriseCodeByName(customerName, false);
            String orgId = "";
            if (crmEnterprise != null) {
                saveFlowEnterpriseCustomerMappingRequest.setCrmOrgId(crmEnterprise.getId());
                saveFlowEnterpriseCustomerMappingRequest.setOrgName(crmEnterprise.getName());
                saveFlowEnterpriseCustomerMappingRequest.setOrgLicenseNumber(crmEnterprise.getLicenseNumber());
                orgId = String.valueOf(crmEnterprise.getId());
            }
            flowEnterpriseCustomerMappingApi.save(saveFlowEnterpriseCustomerMappingRequest);
            return orgId;
        }
        return String.valueOf(flowCustomerMappingDTO.getCrmOrgId());

    }

    @Override
    public Long mathGoodsCrmCode(String goodsName, String spec, String manufacturer, String unit, String goodsInnerCode, CrmEnterpriseDTO crmEnterpriseDTO) {
        if (StrUtil.isEmpty(goodsName) || StrUtil.isEmpty(spec)) {
            return 0L;
        }
        goodsName = goodsName.trim();
        spec = spec.trim();
        FlowEnterpriseGoodsMappingDTO flowGoodsPriceRelationDTO = flowEnterpriseGoodsMappingApi.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(goodsName, spec, crmEnterpriseDTO.getId());
        if (flowGoodsPriceRelationDTO == null || flowGoodsPriceRelationDTO.getCrmGoodsCode() == 0) {
            Long goodsCode = 0L;
            SaveFlowEnterpriseGoodsMappingRequest saveFlowEnterpriseGoodsMappingRequest = new SaveFlowEnterpriseGoodsMappingRequest();
            saveFlowEnterpriseGoodsMappingRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            saveFlowEnterpriseGoodsMappingRequest.setEnterpriseName(crmEnterpriseDTO.getName());
            saveFlowEnterpriseGoodsMappingRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
            saveFlowEnterpriseGoodsMappingRequest.setProvince(crmEnterpriseDTO.getProvinceName());
            saveFlowEnterpriseGoodsMappingRequest.setFlowGoodsName(goodsName);
            saveFlowEnterpriseGoodsMappingRequest.setFlowSpecification(spec);
            saveFlowEnterpriseGoodsMappingRequest.setFlowManufacturer(manufacturer);
            saveFlowEnterpriseGoodsMappingRequest.setFlowUnit(unit);
            saveFlowEnterpriseGoodsMappingRequest.setFlowGoodsInSn(goodsInnerCode);
            saveFlowEnterpriseGoodsMappingRequest.setLastUploadTime(new Date());
            saveFlowEnterpriseGoodsMappingRequest.setOpTime(new Date());
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoApi.getByNameAndSpec(goodsName, spec, CrmGoodsStatusEnum.NORMAL.getCode());
            if (crmGoodsInfoDTO!=null) {
                saveFlowEnterpriseGoodsMappingRequest.setConvertUnit(1);
                saveFlowEnterpriseGoodsMappingRequest.setConvertNumber(BigDecimal.ONE);
                saveFlowEnterpriseGoodsMappingRequest.setCrmGoodsCode(crmGoodsInfoDTO.getGoodsCode());
                saveFlowEnterpriseGoodsMappingRequest.setGoodsName(crmGoodsInfoDTO.getGoodsName());
                saveFlowEnterpriseGoodsMappingRequest.setGoodsSpecification(crmGoodsInfoDTO.getGoodsSpec());
                goodsCode = crmGoodsInfoDTO.getGoodsCode();
            }
            flowEnterpriseGoodsMappingApi.save(saveFlowEnterpriseGoodsMappingRequest);
            return goodsCode;
        }
        return flowGoodsPriceRelationDTO.getCrmGoodsCode();
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

}
