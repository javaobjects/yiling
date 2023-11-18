package com.yiling.dataflow.order.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.dto.request.UpdateSupplierCrmCodeRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsService;
import com.yiling.dataflow.order.dao.FlowPurchaseMapper;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByPoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchasePageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowGoodsSpecMappingDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDetailDO;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.service.FlowGoodsPriceRelationService;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.search.flow.api.EsFlowPurchaseApi;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 采购流向表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Service
@Slf4j
public class FlowPurchaseServiceImpl extends BaseServiceImpl<FlowPurchaseMapper, FlowPurchaseDO> implements FlowPurchaseService {

    @Autowired
    protected RedisDistributedLock        redisDistributedLock;
    @Autowired
    private   FlowGoodsSpecMappingService flowGoodsSpecMappingService;
    @Autowired
    FlowPurchaseInventoryService  flowPurchaseInventoryService;
    @Autowired
    FlowGoodsRelationService      flowGoodsRelationService;
    @Autowired
    CrmGoodsService               crmGoodsService;
    @Autowired
    CrmEnterpriseService          crmEnterpriseService;
    @Autowired
    FlowGoodsPriceRelationService flowGoodsPriceRelationService;

    @DubboReference(timeout = 10 * 1000)
    private GoodsApi goodsApi;
    @DubboReference(timeout = 1000*60*60)
    private EsFlowPurchaseApi esFlowPurchaseApi;

    public static final int SUB_SIZE = 500;

    @Override
    public Integer deleteFlowPurchaseByPoIdAndEid(DeleteFlowPurchaseRequest request) {
        QueryWrapper<FlowPurchaseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowPurchaseDO::getPoId, request.getPoId()).eq(FlowPurchaseDO::getEid, request.getEid());

        FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
        flowPurchaseDO.setOpUserId(request.getOpUserId());
        return this.batchDeleteWithFill(flowPurchaseDO, queryWrapper);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        LambdaQueryWrapper<FlowPurchaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowPurchaseDO::getId, idList);

        FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
        flowPurchaseDO.setUpdateUser(0L);
        flowPurchaseDO.setUpdateTime(new Date());

        return this.batchDeleteWithFill(flowPurchaseDO, queryWrapper);
    }

    @Override
    public Integer updateDataTagByIdList(List<Long> idList, Integer dataTag) {
        LambdaQueryWrapper<FlowPurchaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowPurchaseDO::getId, idList);

        FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
        flowPurchaseDO.setDataTag(dataTag);
        flowPurchaseDO.setUpdateUser(0L);
        flowPurchaseDO.setUpdateTime(new Date());
        return baseMapper.update(flowPurchaseDO, queryWrapper);
    }

    @Override
    public List<FlowPurchaseDO> getFlowPurchaseDTOByPoIdAndEid(QueryFlowPurchaseRequest request) {
        QueryWrapper<FlowPurchaseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowPurchaseDO::getPoId, request.getPoId()).eq(FlowPurchaseDO::getEid, request.getEid());

        return this.list(queryWrapper);
    }

    @Override
    public Integer updateFlowPurchaseByPoIdAndEid(SaveOrUpdateFlowPurchaseRequest request) {
        QueryWrapper<FlowPurchaseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowPurchaseDO::getPoId, request.getPoId()).eq(FlowPurchaseDO::getEid, request.getEid());

        FlowPurchaseDO flowPurchaseDO = PojoUtils.map(request, FlowPurchaseDO.class);
        return this.update(flowPurchaseDO, queryWrapper) ? 1 : 0;
    }

    @Override
    public FlowPurchaseDO insertFlowPurchase(SaveOrUpdateFlowPurchaseRequest request) {
        FlowPurchaseDO flowPurchaseDO = PojoUtils.map(request, FlowPurchaseDO.class);
        this.save(flowPurchaseDO);
        return flowPurchaseDO;
    }

    @Override
    public Page<FlowPurchaseDO> page(QueryFlowPurchaseListPageRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public List<String> getByEidAndGoodsInSn(QueryFlowPurchaseByGoodsInSnRequest request) {
        return this.baseMapper.getByEidAndGoodsInSn(request);
    }

    @Override
    public Integer deleteFlowPurchaseBydEidAndPoTime(DeleteFlowPurchaseByUnlockRequest request) {
        return this.baseMapper.deleteFlowPurchaseBydEidAndPoTime(request);
    }

    @Override
    public Boolean updateFlowPurchaseById(SaveOrUpdateFlowPurchaseRequest request) {
        FlowPurchaseDO entity = PojoUtils.map(request, FlowPurchaseDO.class);
        return this.updateById(entity);
    }

    @Override
    public Boolean updateFlowPurchaseByIds(List<SaveOrUpdateFlowPurchaseRequest> requestList) {
        List<FlowPurchaseDO> entityList = PojoUtils.map(requestList, FlowPurchaseDO.class);
        return this.updateBatchById(entityList);
    }

    @Override
    public Page<FlowPurchaseDO> pageByEidAndGoodsInSn(QueryFlowPurchasePageByEidAndGoodsInSnRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid()) || StrUtil.isBlank(request.getGoodsInSn())) {
            return request.getPage();
        }
        LambdaQueryWrapper<FlowPurchaseDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(FlowPurchaseDO::getEid, request.getEid());
        lambdaQueryWrapper.eq(FlowPurchaseDO::getGoodsInSn, request.getGoodsInSn());
        return this.page(new Page<>(request.getCurrent(), request.getSize()), lambdaQueryWrapper);
    }

    @Override
    public List<FlowPurchaseDO> getFlowPurchaseEnterpriseList(Integer channelId) {
        return baseMapper.getFlowPurchaseEnterpriseList(channelId);
    }

    @Override
    public List<FlowPurchaseDO> getFlowPurchaseSupplierList(Integer channelId) {
        return baseMapper.getFlowPurchaseSupplierList(channelId);
    }

    @Override
    public List<Map<String, Object>> getFlowPurchaseMonthList(QueryFlowPurchaseListRequest request) {
        return baseMapper.getFlowPurchaseMonthList(request);
    }

    @Override
    public List<Map<String, Object>> getFlowPurchaseAllMonthList(QueryFlowPurchaseListRequest request) {
        return baseMapper.getFlowPurchaseAllMonthList(request);
    }

    @Override
    public List<FlowPurchaseDetailDO> getFlowPurchaseDetail(Long eid, Long supplierId, String time) {
        return baseMapper.getFlowPurchaseDetail(eid, supplierId, time);
    }

    @Override
    public List<Map<String, Object>> getFlowPurchaseGoodsMonthList(QueryPurchaseGoodsListRequest request) {
        return baseMapper.getFlowPurchaseGoodsMonthList(request);
    }

    @Override
    public List<String> getPurchaseGoodsNameList() {
        List<String> goodsNameList = baseMapper.getPurchaseGoodsNameList();
        return goodsNameList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    @Async("asyncExecutor")
    public void syncFlowPurchaseSpec() {
        log.info("每天同步采购流向 商品规格id任务开始执行...");
        //  若存在specificationId为-1的情况，将-1改为0
        updateSpecificationId();

        LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseDO::getSpecificationId, 0);

        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }
        long current = 1;
        long size = 1000;
        while ((current - 1) * size < count) {
            Page<FlowPurchaseDO> page = new Page<>(current, size);
            Page<FlowPurchaseDO> flowPurchaseDOPage = baseMapper.selectPage(page, wrapper);
            List<FlowPurchaseDO> flowPurchaseDOList = flowPurchaseDOPage.getRecords();
            for (FlowPurchaseDO flowPurchaseDO : flowPurchaseDOList) {
                // 修改specificationId
                String goodsName = flowPurchaseDO.getGoodsName();
                String spec = flowPurchaseDO.getPoSpecifications();
                String manufacturer = flowPurchaseDO.getPoManufacturer();
                if (StringUtils.isEmpty(goodsName) || StringUtils.isEmpty(spec) || StringUtils.isEmpty(manufacturer)) {
                    flowPurchaseDO.setSpecificationId(-1L);
                } else {
                    FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = flowGoodsSpecMappingService.findByGoodsNameAndSpec(goodsName, spec, manufacturer);
                    if (flowGoodsSpecMappingDO == null) {
                        flowPurchaseDO.setSpecificationId(-1L);
                    } else {
                        flowPurchaseDO.setSpecificationId(flowGoodsSpecMappingDO.getSpecificationId());
                    }
                }

                baseMapper.updateById(flowPurchaseDO);
            }
            current++;
        }

        //  调度完成将-1改为0
        updateSpecificationId();
        log.info("每天同步采购流向 商品规格id任务执行完毕！");
    }

    @Override
    public void updateFlowPurchaseCrmGoodsSign(List<Long> crmIds) {
        if (CollUtil.isNotEmpty(crmIds)) {
            for (Long crmId : crmIds) {
                LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FlowPurchaseDO::getCrmGoodsCode, 0);
                wrapper.eq(FlowPurchaseDO::getCrmEnterpriseId, crmId);
                Integer count = baseMapper.selectCount(wrapper);
                log.info("crm采购单商品编码标记:eid{},count{}", crmId,count);
                if (count == 0) {
                    continue;
                }
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowPurchaseDO> page = new Page<>(1, size, count, false);
                    Page<FlowPurchaseDO> flowPurchaseDOPage = baseMapper.selectPage(page, wrapper);
                    List<FlowPurchaseDO> flowPurchaseDOList = flowPurchaseDOPage.getRecords();
                    if (CollUtil.isEmpty(flowPurchaseDOList)) {
                        break;
                    }
                    executeFlowPurchaseCrmGoodsCode(flowPurchaseDOList);
                    current++;
                }
                //todo  调度完成将-1改为0
                updateCrmGoodsCode(crmId);
            }
        }
    }

    private void updateCrmGoodsCode(Long crmId) {
        LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseDO::getCrmGoodsCode, -1);
        wrapper.eq(FlowPurchaseDO::getCrmEnterpriseId,crmId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        FlowPurchaseDO flowSaleDO = new FlowPurchaseDO();
        flowSaleDO.setCrmGoodsCode(0L);
        baseMapper.update(flowSaleDO, wrapper);
    }

    public void executeFlowPurchaseCrmGoodsCode(List<FlowPurchaseDO> list) {
        for (FlowPurchaseDO flowPurchaseDO : list) {
            if (StrUtil.isEmpty(flowPurchaseDO.getCrmCode())) {
                CrmEnterpriseDTO crmEnterpriseCodeDO = crmEnterpriseService.getCrmEnterpriseCodeByName(flowPurchaseDO.getEname(),true);
                if (crmEnterpriseCodeDO != null) {
                    flowPurchaseDO.setCrmCode(crmEnterpriseCodeDO.getCode());
                }
            }
        }

        for (FlowPurchaseDO flowPurchaseDO : list) {
            SaveOrUpdateFlowPurchaseRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowPurchaseRequest();
            saveOrUpdateFlowSaleRequest.setId(flowPurchaseDO.getId());
            saveOrUpdateFlowSaleRequest.setCrmCode(flowPurchaseDO.getCrmCode());
            if (StrUtil.isNotEmpty(flowPurchaseDO.getCrmCode())) {
                FlowGoodsPriceRelationDTO flowGoodsPriceRelationDO = flowGoodsPriceRelationService.getByGoodsNameAndSpecAndEnterpriseCode(flowPurchaseDO.getGoodsName(), flowPurchaseDO.getPoSpecifications(), flowPurchaseDO.getCrmCode());
                if (flowGoodsPriceRelationDO != null) {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(flowGoodsPriceRelationDO.getGoodsCode());
                } else {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                }
            }else {
                saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
            }

            if (saveOrUpdateFlowSaleRequest.getCrmGoodsCode() == -1) {
                //获取crm_goods
                if (flowPurchaseDO.getSpecificationId() != 0) {
                    List<CrmGoodsDTO> crmGoodsDTOList = crmGoodsService.getCrmGoodsBySpecificationId(flowPurchaseDO.getSpecificationId());
                    if (CollUtil.isNotEmpty(crmGoodsDTOList)) {
                        if (crmGoodsDTOList.size() == 1) {
                            saveOrUpdateFlowSaleRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                        } else {
                            saveOrUpdateFlowSaleRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                            saveOrUpdateFlowSaleRequest.setGoodsPossibleError(1);
                        }
                    } else {
                        saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                    }
                } else {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                }
            }
            this.updateFlowPurchaseById(saveOrUpdateFlowSaleRequest);
        }
    }

    @Override
    public List<FlowPurchaseDO> getCantMatchGoodsQuantityList(List<Long> eidList, String monthTime) {
        return baseMapper.getCantMatchGoodsQuantityList(eidList, monthTime);
    }

    @Override
    public void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request) {
        for (FlushGoodsSpecIdRequest.FlushDataRequest flushData : request.getFlushDataList()) {
            if (flushData.getRecommendSpecificationId() == null || flushData.getRecommendSpecificationId() == 0) {
                continue;
            }
            LambdaUpdateWrapper<FlowPurchaseDO> wrapper = new LambdaUpdateWrapper<>();
//            wrapper.eq(FlowPurchaseDO::getEid, request.getEid());
            wrapper.eq(FlowPurchaseDO::getGoodsName, flushData.getGoodsName());
            wrapper.eq(FlowPurchaseDO::getPoSpecifications, flushData.getSpec());
            wrapper.eq(FlowPurchaseDO::getSpecificationId, 0);

            FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
            flowPurchaseDO.setSpecificationId(flushData.getRecommendSpecificationId());

            baseMapper.update(flowPurchaseDO, wrapper);
        }
    }

    @Override
    public boolean isUsedSpecificationId(Long specificationId) {
        Integer count = baseMapper.getSpecificationIdCount(specificationId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Page<FlowPurchaseDO> pageBackup(QueryFlowPurchaseBackupListPageRequest request) {
        return baseMapper.pageBackup(request.getPage(), request);
    }

    @Override
    public int refreshFlowPurchase(UpdateFlowIndexRequest request) {
        Integer count = baseMapper.selectFlowPurchaseCount(request);
        log.info("销售流向数据刷新索引:eid{},count{}", request.getEid(), count);
        if (count == 0) {
            return 0;
        }
        long current = 1;
        long size = 2000;
        while ((current - 1) * size < count) {
            Page<FlowPurchaseDO> page = new Page<>(current, size, count, false);
            Page<FlowPurchaseDO> flowSaleDOPage = baseMapper.selectFlowPurchasePage(page, request);
            List<FlowPurchaseDO> flowSaleDOList = flowSaleDOPage.getRecords();
            if (CollUtil.isEmpty(flowSaleDOList)) {
                break;
            }
            current++;
            Long start=System.currentTimeMillis();
            esFlowPurchaseApi.updateFlowPurchase(PojoUtils.map(flowSaleDOList, EsFlowPurchaseDTO.class));
            log.info("执行时间：{}",System.currentTimeMillis()-start);
        }
        return count;
    }

    @Override
    public int refreshFlowPurchaseBackup(UpdateFlowIndexRequest request) {
        log.info("采购流向数据刷新索引:eid{}", request.getEid());
        QueryFlowPurchaseBackupListPageRequest queryFlowPurchaseBackupListPageRequest=new QueryFlowPurchaseBackupListPageRequest();
        queryFlowPurchaseBackupListPageRequest.setEidList(Arrays.asList(request.getEid()));
        queryFlowPurchaseBackupListPageRequest.setTableName("flow_purchase_"+request.getYear());
        Page<FlowPurchaseDO> page;
        int current = 1;
        int size = 2000;
        do {
            queryFlowPurchaseBackupListPageRequest.setSize(size);
            queryFlowPurchaseBackupListPageRequest.setCurrent(current);
            page = this.baseMapper.pageBackup(queryFlowPurchaseBackupListPageRequest.getPage(), queryFlowPurchaseBackupListPageRequest);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<FlowPurchaseDO> records = page.getRecords();
            esFlowPurchaseApi.updateFlowPurchase(PojoUtils.map(records, EsFlowPurchaseDTO.class));
            if (records.size() < size) {
                break;
            }
            current++;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return 0;
    }

    @Override
    public Integer getCountByEidAndSoTime(Long eid, Date startTime, Date endTime) {
        LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseDO::getEid, eid);
        wrapper.ge(FlowPurchaseDO::getPoTime, startTime);
        wrapper.le(FlowPurchaseDO::getPoTime, endTime);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public boolean isHaveDataByEidAndPoTime(QueryFlowPurchaseExistRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getEid(), "参数 eid 不能为空");
        Assert.notNull(request.getPoTimeStart(), "参数 poTimeStart 不能为空");
        Assert.notNull(request.getPoTimeEnd(), "参数 poTimeEnd 不能为空");
        LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseDO::getEid, request.getEid());
        wrapper.ge(FlowPurchaseDO::getPoTime, request.getPoTimeStart());
        wrapper.le(FlowPurchaseDO::getPoTime, request.getPoTimeEnd());
        wrapper.last("limit 1");
        FlowPurchaseDO one = this.getOne(wrapper);
        if (ObjectUtil.isNotNull(one)) {
            return true;
        }
        return false;
    }

    @Override
    public Page<FlowPurchaseDTO> getPageByPoMonth(QueryFlowPurchaseByPoMonthPageRequest request) {
        Page<FlowPurchaseDTO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(baseMapper.getPageByPoMonth(page, request), FlowPurchaseDTO.class);
    }

    @Override
    public void updateCrmGoodsCode(UpdateCrmGoodsCodeRequest request) {
        FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
        flowPurchaseDO.setId(request.getId());
        flowPurchaseDO.setCrmGoodsCode(request.getCrmGoodsCode());
        flowPurchaseDO.setUpdateTime(new Date());
        baseMapper.updateById(flowPurchaseDO);
    }

    @Override
    public void updateSupplierCrmCodeCode(UpdateSupplierCrmCodeRequest request) {
        FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
        flowPurchaseDO.setId(request.getId());
        flowPurchaseDO.setSupplierId(request.getSupplierId());
        flowPurchaseDO.setUpdateTime(new Date());
        baseMapper.updateById(flowPurchaseDO);
    }

    @Override
    public List<FlowStatisticsBO> getFlowPurchaseStatistics(QueryFlowStatisticesRequest request) {
        return baseMapper.getFlowPurchaseStatistics(request);
    }

    @Override
    public Date getMaxPoTimeByEid(Long eid) {
        return baseMapper.getMaxPoTimeByEid(eid);
    }

    private void updateSpecificationId() {
        LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseDO::getSpecificationId, -1);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        LambdaUpdateWrapper<FlowPurchaseDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FlowPurchaseDO::getSpecificationId, -1);

        FlowPurchaseDO flowPurchaseDO = new FlowPurchaseDO();
        flowPurchaseDO.setSpecificationId(0L);
        baseMapper.update(flowPurchaseDO, updateWrapper);
    }

    private LambdaQueryWrapper<FlowPurchaseDO> getPurchasePageQueryWrapper(QueryFlowPurchaseListPageRequest request) {
        LambdaQueryWrapper<FlowPurchaseDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        String enterpriseInnerCode = request.getEnterpriseInnerCode();
        if (StrUtil.isNotBlank(enterpriseInnerCode)) {
            lambdaQueryWrapper.like(FlowPurchaseDO::getEnterpriseInnerCode, enterpriseInnerCode);
        }
        String enterpriseName = request.getEnterpriseName();
        if (StrUtil.isNotBlank(enterpriseName)) {
            lambdaQueryWrapper.like(FlowPurchaseDO::getEnterpriseName, enterpriseName);
        }
        String goodsName = request.getGoodsName();
        if (StrUtil.isNotBlank(goodsName)) {
            lambdaQueryWrapper.like(FlowPurchaseDO::getGoodsName, goodsName);
        }
        String poLicense = request.getLicense();
        if (StrUtil.isNotBlank(poLicense)) {
            lambdaQueryWrapper.like(FlowPurchaseDO::getPoLicense, poLicense);
        }
        Date poTimeStart = request.getStartTime();
        if (ObjectUtil.isNotNull(poTimeStart)) {
            lambdaQueryWrapper.ge(FlowPurchaseDO::getPoTime, DateUtil.beginOfDay(poTimeStart));
        }
        Date poTimeEnd = request.getEndTime();
        if (ObjectUtil.isNotNull(poTimeEnd)) {
            lambdaQueryWrapper.le(FlowPurchaseDO::getPoTime, DateUtil.endOfDay(poTimeEnd));
        }
        List<String> sourceList = request.getSourceList();
        if (CollUtil.isNotEmpty(sourceList)) {
            lambdaQueryWrapper.in(FlowPurchaseDO::getPoSource, sourceList);
        }
        String manufacturer = request.getManufacturer();
        if (StrUtil.isNotBlank(manufacturer)) {
            lambdaQueryWrapper.like(FlowPurchaseDO::getPoManufacturer, manufacturer);
        }

        //String provinceCode = request.getProvinceCode();
        //if (StrUtil.isNotBlank(provinceCode)) {
        //    lambdaQueryWrapper.like(FlowPurchaseDO::getProvinceCode, provinceCode);
        //}
        //String cityCode = request.getCityCode();
        //if (StrUtil.isNotBlank(cityCode)) {
        //    lambdaQueryWrapper.like(FlowPurchaseDO::getCityCode, cityCode);
        //}
        //String regionCode = request.getRegionCode();
        //if (StrUtil.isNotBlank(regionCode)) {
        //    lambdaQueryWrapper.like(FlowPurchaseDO::getRegionCode, regionCode);
        //}
        List<Long> eidList = request.getEidList();
        if (CollUtil.isNotEmpty(eidList) && eidList.get(0) != -99L) {
            lambdaQueryWrapper.in(FlowPurchaseDO::getEid, eidList);
        }
        return lambdaQueryWrapper;
    }
}
