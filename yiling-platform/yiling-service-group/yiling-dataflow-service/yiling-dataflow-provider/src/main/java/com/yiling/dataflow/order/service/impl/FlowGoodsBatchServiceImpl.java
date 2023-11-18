package com.yiling.dataflow.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import com.yiling.dataflow.order.dao.FlowGoodsBatchMapper;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchPageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDO;
import com.yiling.dataflow.order.entity.FlowGoodsSpecMappingDO;
import com.yiling.dataflow.order.enums.FlowGoodsBatchStatisticsStatusEnum;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.service.FlowGoodsPriceRelationService;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * ERP库存汇总同步数据 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Slf4j
@Service
public class FlowGoodsBatchServiceImpl extends BaseServiceImpl<FlowGoodsBatchMapper, FlowGoodsBatchDO> implements FlowGoodsBatchService {

    @Autowired
    private RedisDistributedLock          redisDistributedLock;
    @Autowired
    private FlowGoodsSpecMappingService   flowGoodsSpecMappingService;
    @Autowired
    private CrmEnterpriseService          crmEnterpriseService;
    @Autowired
    private FlowGoodsPriceRelationService flowGoodsPriceRelationService;
    @Autowired
    private CrmGoodsService               crmGoodsService;

    public static final int SUB_SIZE = 500;

    @Override
    public Integer deleteFlowGoodsBatchByGbIdNoAndEid(DeleteFlowGoodsBatchRequest request) {
        QueryWrapper<FlowGoodsBatchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowGoodsBatchDO::getGbIdNo, request.getGbIdNo()).eq(FlowGoodsBatchDO::getEid, request.getEid());

        FlowGoodsBatchDO flowGoodsBatchDO = new FlowGoodsBatchDO();
        flowGoodsBatchDO.setOpUserId(request.getOpUserId());
        return this.batchDeleteWithFill(flowGoodsBatchDO, queryWrapper);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        LambdaQueryWrapper<FlowGoodsBatchDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowGoodsBatchDO::getId, idList);

        FlowGoodsBatchDO flowGoodsBatchDO = new FlowGoodsBatchDO();
        flowGoodsBatchDO.setUpdateUser(0L);
        flowGoodsBatchDO.setUpdateTime(new Date());

        return this.batchDeleteWithFill(flowGoodsBatchDO, queryWrapper);
    }

    @Override
    public List<FlowGoodsBatchDO> getFlowGoodsBatchDTOByGbIdNoAndEid(QueryFlowGoodsBatchRequest request) {
        QueryWrapper<FlowGoodsBatchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowGoodsBatchDO::getGbIdNo, request.getGbIdNo()).eq(FlowGoodsBatchDO::getEid, request.getEid());

        return this.list(queryWrapper);
    }

    @Override
    public List<FlowGoodsBatchDO> getFlowGoodsBatchDTOByInSnAndEid(QueryFlowGoodsBatchRequest request) {
        QueryWrapper<FlowGoodsBatchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowGoodsBatchDO::getInSn, request.getInSn()).eq(FlowGoodsBatchDO::getEid, request.getEid());

        return this.list(queryWrapper);
    }

    @Override
    public Integer updateFlowGoodsBatchByGbIdNoAndEid(SaveOrUpdateFlowGoodsBatchRequest request) {
        QueryWrapper<FlowGoodsBatchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowGoodsBatchDO::getGbIdNo, request.getGbIdNo()).eq(FlowGoodsBatchDO::getEid, request.getEid());

        FlowGoodsBatchDO flowGoodsBatchDO = PojoUtils.map(request, FlowGoodsBatchDO.class);
        return this.update(flowGoodsBatchDO, queryWrapper) ? 1 : 0;
    }

    @Override
    public Integer updateFlowGoodsBatchByInSnAndEid(SaveOrUpdateFlowGoodsBatchRequest request) {
        QueryWrapper<FlowGoodsBatchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowGoodsBatchDO::getInSn, request.getInSn()).eq(FlowGoodsBatchDO::getEid, request.getEid());

        FlowGoodsBatchDO flowGoodsBatchDO = new FlowGoodsBatchDO();
        flowGoodsBatchDO.setTotalNumber(request.getTotalNumber());
        flowGoodsBatchDO.setOpUserId(request.getOpUserId());
        return this.update(flowGoodsBatchDO, queryWrapper) ? 1 : 0;
    }

    @Override
    public Integer insertFlowGoodsBatch(SaveOrUpdateFlowGoodsBatchRequest request) {
        FlowGoodsBatchDO flowGoodsBatchDO = PojoUtils.map(request, FlowGoodsBatchDO.class);
        return this.save(flowGoodsBatchDO) ? 1 : 0;
    }

    @Override
    public Page<FlowGoodsBatchDO> page(QueryFlowGoodsBatchListPageRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public Integer deleteFlowGoodsBatchByEids(List<Long> eids, Date createTime) {
        return this.baseMapper.deleteFlowGoodsBatchByEids(eids, createTime);
    }

    @Override
    public Boolean updateFlowGoodsBatchTotalNumberByIdList(List<Long> idList, BigDecimal totalNumber) {
        List<FlowGoodsBatchDO> list = new ArrayList<>();
        for (Long id : idList) {
            FlowGoodsBatchDO flowGoodsBatchDO = new FlowGoodsBatchDO();
            flowGoodsBatchDO.setId(id);
            flowGoodsBatchDO.setTotalNumber(totalNumber);
            list.add(flowGoodsBatchDO);
        }
        return this.updateBatchById(list, 1000);
    }

    @Override
    public Boolean updateFlowGoodsBatchById(SaveOrUpdateFlowGoodsBatchRequest request) {
        FlowGoodsBatchDO entity = PojoUtils.map(request, FlowGoodsBatchDO.class);
        return this.updateById(entity);
    }

    @Override
    public Boolean updateFlowGoodsBatchByIds(List<SaveOrUpdateFlowGoodsBatchRequest> request) {
        List<FlowGoodsBatchDO> entity = PojoUtils.map(request, FlowGoodsBatchDO.class);
        return this.updateBatchById(entity);
    }

    @Override
    public Page<FlowGoodsBatchDO> pageByEidAndGoodsInSn(QueryFlowGoodsBatchPageByEidAndGoodsInSnRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid()) || StrUtil.isBlank(request.getGoodsInSn())) {
            return request.getPage();
        }
        LambdaQueryWrapper<FlowGoodsBatchDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(FlowGoodsBatchDO::getEid, request.getEid());
        lambdaQueryWrapper.eq(FlowGoodsBatchDO::getInSn, request.getGoodsInSn());
        return this.page(new Page<>(request.getCurrent(), request.getSize()), lambdaQueryWrapper);
    }

    @Override
    public void statisticsFlowGoodsBatchTotalNumber() {
        long start = System.currentTimeMillis();
        log.info("[库存流向商品库存数量统计]任务执行开始...");
        QueryFlowGoodsBatchListPageRequest request = new QueryFlowGoodsBatchListPageRequest();
        request.setStatisticsStatus(FlowGoodsBatchStatisticsStatusEnum.TODO.getCode());
        Page<FlowGoodsBatchDO> page;
        int current = 1;
        int size = SUB_SIZE;
        do {
            request.setSize(size);
            request.setCurrent(current);
            page = this.baseMapper.page(request.getPage(), request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<FlowGoodsBatchDO> records = page.getRecords();

            // 按照 eid、inSn、gb_specifications 分组统计
            Map<String, FlowGoodsBatchDO> map = new HashMap<>();
            records.forEach(o -> {
                String key = statisticsTotalNumberKey(o.getEid(), o.getInSn(), o.getGbSpecifications());
                if (!map.containsKey(key)) {
                    map.put(key, o);
                }
            });
            List<FlowGoodsBatchDO> eidAndInSnAndGbSpecificationsGroups = new ArrayList<>(map.values());
            for (FlowGoodsBatchDO flowGoodsBatchDO : eidAndInSnAndGbSpecificationsGroups) {
                LambdaQueryWrapper<FlowGoodsBatchDO> lambdaQueryWrapper = new LambdaQueryWrapper();
                lambdaQueryWrapper.eq(FlowGoodsBatchDO::getEid, flowGoodsBatchDO.getEid());
                lambdaQueryWrapper.eq(FlowGoodsBatchDO::getInSn, flowGoodsBatchDO.getInSn());
                lambdaQueryWrapper.eq(FlowGoodsBatchDO::getGbSpecifications, flowGoodsBatchDO.getGbSpecifications());
                List<FlowGoodsBatchDO> list = this.list(lambdaQueryWrapper);
                if (CollUtil.isEmpty(list)) {
                    continue;
                }
                // 加锁
                Map<String, String> lockMap = new HashMap<>();
                try {
                    boolean lockFlag = true;
                    for (FlowGoodsBatchDO goodsBatch : list) {
                        String lockName = RedisKey.generate("mph-erp-online-lock-flow-goods-batch", "statisticsStatusById", goodsBatch.getId().toString());
                        String lockId = "";
                        try {
                            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            lockFlag = false;
                            log.warn("[库存流向商品库存数量统计], id:{}未获取到锁, 不统计库存总数的 eid + inSn + gbSpecifications[{}], exception:{}", goodsBatch.getId(), statisticsTotalNumberKey(goodsBatch.getEid(), goodsBatch.getInSn(), goodsBatch.getGbSpecifications()), e.getMessage());
                        }
                        if (StrUtil.isNotBlank(lockId)) {
                            lockMap.put(lockName, lockId);
                        }
                    }
                    if (!lockFlag) {
                        continue;
                    }
                    // 防止数据在加锁前被修改，根据id再查一次
                    List<Long> idList = list.stream().map(FlowGoodsBatchDO::getId).distinct().collect(Collectors.toList());
                    LambdaQueryWrapper<FlowGoodsBatchDO> lambdaQueryWrapper2 = new LambdaQueryWrapper();
                    lambdaQueryWrapper2.in(FlowGoodsBatchDO::getId, idList);
                    List<FlowGoodsBatchDO> flowGoodsBatchList = this.list(lambdaQueryWrapper2);
                    // 统计库存总数
                    List<FlowGoodsBatchDO> updateList = new ArrayList<>();
                    BigDecimal totalNumber = flowGoodsBatchList.stream().filter(o -> ObjectUtil.isNotNull(o.getGbNumber())).map(FlowGoodsBatchDO::getGbNumber).reduce(BigDecimal.ZERO, BigDecimal::add);
                    flowGoodsBatchList.forEach(o -> {
                        FlowGoodsBatchDO entity = new FlowGoodsBatchDO();
                        entity.setId(o.getId());
                        entity.setTotalNumber(totalNumber);
                        entity.setStatisticsStatus(FlowGoodsBatchStatisticsStatusEnum.DONE.getCode());
                        updateList.add(entity);
                    });
                    // 当前企业批量根据id更新库存总数量
                    this.updateBatchById(updateList, SUB_SIZE);
                } finally {
                    if (MapUtil.isNotEmpty(map)) {
                        for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                            redisDistributedLock.releaseLock(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            if (records.size() < size) {
                break;
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        log.info("[库存流向商品库存数量统计]任务执行结束, 耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    @Async("asyncExecutor")
    public void syncFlowGoodsBatchSpec() {
        log.info("每天同步库存流向 商品规格id任务开始执行...");
        //  若存在specificationId为-1的情况，将-1改为0
        updateSpecificationId();

        LambdaQueryWrapper<FlowGoodsBatchDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDO::getSpecificationId, 0);

        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }
        long current = 1;
        long size = 1000;
        while ((current - 1) * size < count) {
            Page<FlowGoodsBatchDO> page = new Page<>(current, size);
            Page<FlowGoodsBatchDO> flowGoodsBatchDOPage = baseMapper.selectPage(page, wrapper);
            List<FlowGoodsBatchDO> flowGoodsBatchDOList = flowGoodsBatchDOPage.getRecords();
            for (FlowGoodsBatchDO flowGoodsBatchDO : flowGoodsBatchDOList) {
                // 修改specificationId
                String goodsName = flowGoodsBatchDO.getGbName();
                String spec = flowGoodsBatchDO.getGbSpecifications();
                String manufacturer = flowGoodsBatchDO.getGbManufacturer();
                if (StringUtils.isEmpty(goodsName) || StringUtils.isEmpty(spec) || StringUtils.isEmpty(manufacturer)) {
                    flowGoodsBatchDO.setSpecificationId(-1L);
                } else {
                    FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = flowGoodsSpecMappingService.findByGoodsNameAndSpec(goodsName, spec, manufacturer);
                    if (flowGoodsSpecMappingDO == null) {
                        flowGoodsBatchDO.setSpecificationId(-1L);
                    } else {
                        flowGoodsBatchDO.setSpecificationId(flowGoodsSpecMappingDO.getSpecificationId());
                    }
                }

                baseMapper.updateById(flowGoodsBatchDO);
            }
            current++;
        }

        //  调度完成将-1改为0
        updateSpecificationId();
        log.info("每天同步库存流向 商品规格id任务执行完毕！");
    }

    @Override
    public void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request) {

        for (FlushGoodsSpecIdRequest.FlushDataRequest flushData : request.getFlushDataList()) {
            if (flushData.getRecommendSpecificationId() == null || flushData.getRecommendSpecificationId() == 0) {
                continue;
            }
            LambdaUpdateWrapper<FlowGoodsBatchDO> wrapper = new LambdaUpdateWrapper<>();
//            wrapper.eq(FlowGoodsBatchDO::getEid, request.getEid());
            wrapper.eq(FlowGoodsBatchDO::getGbName, flushData.getGoodsName());
            wrapper.eq(FlowGoodsBatchDO::getGbSpecifications, flushData.getSpec());
            wrapper.eq(FlowGoodsBatchDO::getSpecificationId, 0);

            FlowGoodsBatchDO flowGoodsBatchDO = new FlowGoodsBatchDO();
            flowGoodsBatchDO.setSpecificationId(flushData.getRecommendSpecificationId());

            baseMapper.update(flowGoodsBatchDO, wrapper);
        }
    }

    @Override
    public void updateFlowGoodsBatchCrmGoodsSign(List<Long> crmIds) {
        //1、先查询没有匹配的流向数据
        if (CollUtil.isNotEmpty(crmIds)) {
            for (Long crmId : crmIds) {
                LambdaQueryWrapper<FlowGoodsBatchDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FlowGoodsBatchDO::getCrmGoodsCode, 0);
                wrapper.eq(FlowGoodsBatchDO::getCrmEnterpriseId, crmId);
                Integer count = baseMapper.selectCount(wrapper);
                log.info("crm库存商品编码标记:eid{},count{}", crmId,count);
                if (count == 0) {
                    continue;
                }
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowGoodsBatchDO> page = new Page<>(1, size, count, false);
                    Page<FlowGoodsBatchDO> flowGoodsBatchDOPage = baseMapper.selectPage(page, wrapper);
                    List<FlowGoodsBatchDO> flowGoodsBatchDOList = flowGoodsBatchDOPage.getRecords();
                    if (CollUtil.isEmpty(flowGoodsBatchDOList)) {
                        break;
                    }
                    executeFlowGoodsBatchCrmGoodsCode(flowGoodsBatchDOList);
                    current++;
                }
                //todo  调度完成将-1改为0
                updateCrmGoodsCode(crmId);
            }
        }
    }

    @Override
    public Date getMaxGbTimeByEid(Long eid) {
        return this.baseMapper.getMaxGbTimeByEid(eid);
    }

    public void executeFlowGoodsBatchCrmGoodsCode(List<FlowGoodsBatchDO> list) {
        for (FlowGoodsBatchDO flowGoodsBatchDO : list) {
            if (StrUtil.isEmpty(flowGoodsBatchDO.getCrmCode())) {
                CrmEnterpriseDTO crmEnterpriseCodeDO = crmEnterpriseService.getCrmEnterpriseCodeByName(flowGoodsBatchDO.getEname(),true);
                if (crmEnterpriseCodeDO != null) {
                    flowGoodsBatchDO.setCrmCode(crmEnterpriseCodeDO.getCode());
                }
            }
        }

        for (FlowGoodsBatchDO flowGoodsBatchDO : list) {
            SaveOrUpdateFlowGoodsBatchRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowGoodsBatchRequest();
            saveOrUpdateFlowSaleRequest.setId(flowGoodsBatchDO.getId());
            if (StrUtil.isNotEmpty(flowGoodsBatchDO.getCrmCode())) {
                saveOrUpdateFlowSaleRequest.setCrmCode(flowGoodsBatchDO.getCrmCode());
                FlowGoodsPriceRelationDTO flowGoodsPriceRelationDO = flowGoodsPriceRelationService.getByGoodsNameAndSpecAndEnterpriseCode(flowGoodsBatchDO.getGbName(), flowGoodsBatchDO.getGbSpecifications(), flowGoodsBatchDO.getCrmCode());
                if (flowGoodsPriceRelationDO != null) {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(flowGoodsPriceRelationDO.getGoodsCode());
                } else {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                }
            } else {
                saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
            }

            if (saveOrUpdateFlowSaleRequest.getCrmGoodsCode() == -1) {
                //获取crm_goods
                if (flowGoodsBatchDO.getSpecificationId() != 0) {
                    List<CrmGoodsDTO> crmGoodsDTOList = crmGoodsService.getCrmGoodsBySpecificationId(flowGoodsBatchDO.getSpecificationId());
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
            this.updateFlowGoodsBatchById(saveOrUpdateFlowSaleRequest);
        }
    }

    private void updateCrmGoodsCode(Long crmId) {
        LambdaQueryWrapper<FlowGoodsBatchDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDO::getCrmGoodsCode, -1);
        wrapper.eq(FlowGoodsBatchDO::getCrmEnterpriseId,crmId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        FlowGoodsBatchDO flowSaleDO = new FlowGoodsBatchDO();
        flowSaleDO.setCrmGoodsCode(0L);
        baseMapper.update(flowSaleDO, wrapper);
    }

    @Override
    public boolean isUsedSpecificationId(Long specificationId) {
        Integer count = baseMapper.getSpecificationIdCount(specificationId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    private void updateSpecificationId() {
        LambdaQueryWrapper<FlowGoodsBatchDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDO::getSpecificationId, -1);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        LambdaUpdateWrapper<FlowGoodsBatchDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FlowGoodsBatchDO::getSpecificationId, -1);

        FlowGoodsBatchDO flowGoodsBatchDO = new FlowGoodsBatchDO();
        flowGoodsBatchDO.setSpecificationId(0L);
        baseMapper.update(flowGoodsBatchDO, updateWrapper);
    }

    public String statisticsTotalNumberKey(Long eid, String inSn, String specifications) {
        if (ObjectUtil.isNull(eid)) {
            eid = 0L;
        }
        return eid.toString().concat(":").concat(inSn).concat(":").concat(specifications);
    }

    private LambdaQueryWrapper<FlowGoodsBatchDO> getGoodsBatchPageQueryWrapper(QueryFlowGoodsBatchListPageRequest request) {
        LambdaQueryWrapper<FlowGoodsBatchDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            lambdaQueryWrapper.like(FlowGoodsBatchDO::getEname, ename);
        }

        String goodsName = request.getGoodsName();
        if (StrUtil.isNotBlank(goodsName)) {
            lambdaQueryWrapper.like(FlowGoodsBatchDO::getGbName, goodsName);
        }
        String gbLicense = request.getLicense();
        if (StrUtil.isNotBlank(gbLicense)) {
            lambdaQueryWrapper.like(FlowGoodsBatchDO::getGbLicense, gbLicense);
        }
        List<String> sourceList = request.getSourceList();
        if (CollUtil.isNotEmpty(sourceList)) {
            lambdaQueryWrapper.in(FlowGoodsBatchDO::getGbSource, sourceList);
        }
        String manufacturer = request.getManufacturer();
        if (StrUtil.isNotBlank(manufacturer)) {
            lambdaQueryWrapper.like(FlowGoodsBatchDO::getGbManufacturer, manufacturer);
        }
        //String provinceCode = request.getProvinceCode();
        //if (StrUtil.isNotBlank(provinceCode)) {
        //    lambdaQueryWrapper.like(FlowGoodsBatchDO::getProvinceCode, provinceCode);
        //}
        //String cityCode = request.getCityCode();
        //if (StrUtil.isNotBlank(cityCode)) {
        //    lambdaQueryWrapper.like(FlowGoodsBatchDO::getCityCode, cityCode);
        //}
        //String regionCode = request.getRegionCode();
        //if (StrUtil.isNotBlank(regionCode)) {
        //    lambdaQueryWrapper.like(FlowGoodsBatchDO::getRegionCode, regionCode);
        //}
        List<Long> eidList = request.getEidList();
        if (CollUtil.isNotEmpty(eidList) && eidList.get(0) != -99L) {
            lambdaQueryWrapper.in(FlowGoodsBatchDO::getEid, eidList);
        }
        return lambdaQueryWrapper;
    }

}
