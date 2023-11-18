package com.yiling.dataflow.statistics.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.statistics.bo.EnterpriseMonthQuantityBO;
import com.yiling.dataflow.statistics.bo.FlowDailyStatisticsBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecInfoBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecNoMatchedBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecStatisticsBO;
import com.yiling.dataflow.statistics.dao.FlowBalanceStatisticsMapper;
import com.yiling.dataflow.statistics.dto.DeteleFlowBalanceStatisticeRequest;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsPageRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.entity.FlowBalanceStatisticsDO;
import com.yiling.dataflow.statistics.service.FlowBalanceStatisticsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商业公司每天平衡表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-11
 */
@Service
@Slf4j
public class FlowBalanceStatisticsServiceImpl extends BaseServiceImpl<FlowBalanceStatisticsMapper, FlowBalanceStatisticsDO> implements FlowBalanceStatisticsService {

    private static DateTime defaultTime = DateUtil.parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:ss:mm");

    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @Autowired
    private FlowGoodsBatchService flowGoodsBatchService;

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Autowired
    private FlowSaleService flowSaleService;

    @Autowired
    private FlowGoodsSpecMappingService flowGoodsSpecMappingService;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    @Override
    public Integer deleteFlowBalanceStatistics(DeteleFlowBalanceStatisticeRequest request) {
        return this.baseMapper.deleteFlowBalanceStatistics(request.getStartTime(), request.getEndTime(), request.getEidList());
    }

    @Override
    public Integer saveFlowBalanceStatisticsList(List<FlowBalanceStatisticsDO> request) {
        List<List<FlowBalanceStatisticsDO>> lists = Lists.partition(request, 10000);
        for (List<FlowBalanceStatisticsDO> newList : lists) {
            newList.forEach(e -> {
                e.setRemark("");
                e.setDelFlag(0);
            });
            baseMapper.insertBatchSomeColumn(newList);
        }
        return 1;
    }

    @Override
    public List<FlowBalanceStatisticsDO> getFlowBalanceStatisticsEnterpriseList() {
        return baseMapper.getEnterpriseList();
    }

    @Override
    public Page<FlowBalanceStatisticsDO> getMonthPageList(QueryBalanceStatisticsMonthRequest request) {
        Page<FlowBalanceStatisticsDO> page = new Page<>(request.getCurrent(), request.getSize());
        return baseMapper.getMonthPageList(page, request);
    }

    @Override
    public List<EnterpriseMonthQuantityBO> getEnterpriseBeginMonthList(String beginMonth, List<Long> eidList) {
        return baseMapper.getEnterpriseBeginMonthList(beginMonth, eidList);
    }

    @Override
    public List<FlowBalanceStatisticsDO> getFlowBalanceStatisticsList(QueryBalanceStatisticsRequest request) {
        return baseMapper.getFlowBalanceStatisticsList(request);
    }

    @Override
    public Page<GoodsSpecStatisticsBO> getGoodsSpecStatisticsListPage(GoodsSpecStatisticsRequest request) {
        Page<GoodsSpecStatisticsBO> page = new Page<>(request.getCurrent(), request.getSize());
        return baseMapper.getGoodsSpecStatisticsListPage(page, request);
    }

    @Override
    public List<GoodsSpecInfoBO> getGoodsSpecInfoList(Long eid, String monthTime) {
        return baseMapper.getGoodsSpecInfoList(eid, monthTime);
    }

    @Override
    public List<EnterpriseMonthQuantityBO> getEnterpriseCurrentMonthList(String time, List<Long> eidList) {
        return baseMapper.getEnterpriseCurrentMonthList(time, eidList);
    }

    @Override
    public List<FlowBalanceStatisticsDO> getEnterpriseMonthFlowList(String monthTime, List<Long> eidList) {
        return baseMapper.getEnterpriseMonthFlowList(monthTime, eidList);
    }

    @Override
    public List<GoodsSpecNoMatchedBO> goodsSpecNoMatchedList(GoodsSpecStatisticsRequest request) {
        return baseMapper.goodsSpecNoMatchedList(request);
    }


    @Override
    @Async("asyncExecutor")
    public void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request) {
        if (CollUtil.isEmpty(request.getFlushDataList())) {
            return;
        }
        try {
            //  修改采购流向匹配数据
            flowPurchaseService.flushGoodsSpecificationId(request);

            //  修改销售流向匹配数据
            flowSaleService.flushGoodsSpecificationId(request);

            //  修改库存匹配数据
            flowGoodsBatchService.flushGoodsSpecificationId(request);
            flowGoodsBatchDetailService.flushGoodsSpecificationId(request);
        } catch (Exception e) {
            log.error("[月流向统计]更新流向数据--商品规格Id失败，请求参数：" + JSONUtil.toJsonStr(request));
            e.printStackTrace();
            throw new ServiceException("更新流向数据--商品规格Id失败");
        }

        try {
            //  修改映射表匹配数据
            flowGoodsSpecMappingService.flushGoodsSpecificationId(request.getFlushDataList());
        } catch (Exception e) {
            log.error("[月流向统计]更新商品规格映射表--商品规格Id失败，请求参数：" + JSONUtil.toJsonStr(request));
            e.printStackTrace();
            throw new ServiceException("更新商品规格映射表--商品规格Id失败");
        }
    }

    @Override
    public Page<FlowDailyStatisticsBO> page(QueryBalanceStatisticsPageRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public List<FlowBalanceStatisticsDO> getGbQuantityByEidAndDateTime(Long eid, Date startDateTime, Date endDateTime) {
        LambdaQueryWrapper<FlowBalanceStatisticsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowBalanceStatisticsDO::getEid, eid);
        wrapper.ge(FlowBalanceStatisticsDO::getDateTime, startDateTime);
        wrapper.le(FlowBalanceStatisticsDO::getDateTime, endDateTime);
        return this.list(wrapper);
    }

    @Override
    public void statisticsFlowBalance(Date startTime, Date endTime, ErpClientDataDTO erpClientDO) {
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(erpClientDO.getCrmEnterpriseId());
        List<FlowBalanceStatisticsDO> saveFlowBalanceStatisticsRequestList = execute(erpClientDO.getRkSuId(), startTime, endTime, defaultTime);
        for (FlowBalanceStatisticsDO saveFlowBalanceStatisticsRequest : saveFlowBalanceStatisticsRequestList) {
            saveFlowBalanceStatisticsRequest.setCrmEnterpriseId(erpClientDO.getCrmEnterpriseId());
            if (crmEnterpriseDO != null) {
                saveFlowBalanceStatisticsRequest.setProvinceCode(crmEnterpriseDO.getProvinceCode());
            } else {
                saveFlowBalanceStatisticsRequest.setProvinceCode("");
            }
            saveFlowBalanceStatisticsRequest.setEid(erpClientDO.getRkSuId());
            saveFlowBalanceStatisticsRequest.setEname(erpClientDO.getClientName());
            saveFlowBalanceStatisticsRequest.setInstallEmployee(erpClientDO.getInstallEmployee());
            saveFlowBalanceStatisticsRequest.setFlowMode(erpClientDO.getFlowMode());
            saveFlowBalanceStatisticsRequest.setOpTime(new Date());
            saveFlowBalanceStatisticsRequest.setOpUserId(0L);
        }
        //清除数据
        DeteleFlowBalanceStatisticeRequest deteleFlowBalanceStatisticeRequest = new DeteleFlowBalanceStatisticeRequest();
        deteleFlowBalanceStatisticeRequest.setStartTime(startTime);
        deteleFlowBalanceStatisticeRequest.setEndTime(endTime);
        deteleFlowBalanceStatisticeRequest.setEidList(Arrays.asList(erpClientDO.getRkSuId()));

        String lockName = StrUtil.format("statisticsFlowBalance_{}", erpClientDO.getRkSuId());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 3, 3, TimeUnit.SECONDS);
            this.deleteFlowBalanceStatistics(deteleFlowBalanceStatisticeRequest);
            this.saveFlowBalanceStatisticsList(saveFlowBalanceStatisticsRequestList);
        } catch (Exception e) {
            log.warn("获取锁超时", e);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    public List<FlowBalanceStatisticsDO> execute(Long eid, Date startDateTime, Date endDateTime, DateTime defaultTime) {
        //初始化时间
        List<FlowBalanceStatisticsDO> flowBalanceStatisticsDOList = this.getGbQuantityByEidAndDateTime(eid, startDateTime, endDateTime);
        Map<Date, FlowBalanceStatisticsDO> flowBalanceStatisticsDOMap = flowBalanceStatisticsDOList.stream().collect(Collectors.toMap(FlowBalanceStatisticsDO::getDateTime, Function.identity()));

        Map<String, FlowBalanceStatisticsDO> saveFlowBalanceStatisticsRequestMap = new HashMap<>();
        Date start = startDateTime;
        while (start.getTime() < endDateTime.getTime()) {
            FlowBalanceStatisticsDO saveFlowBalanceStatisticsRequest = flowBalanceStatisticsDOMap.get(DateUtil.parseDate(DateUtil.format(start, "yyyy-MM-dd")));
            if (saveFlowBalanceStatisticsRequest == null) {
                saveFlowBalanceStatisticsRequest = new FlowBalanceStatisticsDO();
                saveFlowBalanceStatisticsRequest.setDateTime(DateUtil.parseDate(DateUtil.format(start, "yyyy-MM-dd")));
                saveFlowBalanceStatisticsRequest.setGbQuantity(0L);
                saveFlowBalanceStatisticsRequest.setLastGbQuantity(0L);
                saveFlowBalanceStatisticsRequest.setGbRowNumber(0L);
            }
            if (DateUtil.format(start, "yyyy-MM-dd").equals(DateUtil.format(endDateTime, "yyyy-MM-dd"))) {
                saveFlowBalanceStatisticsRequest.setGbQuantity(0L);
                saveFlowBalanceStatisticsRequest.setGbRowNumber(0L);
            }
            saveFlowBalanceStatisticsRequest.setSoRowNumber(0L);
            saveFlowBalanceStatisticsRequest.setSoQuantity(0L);
            saveFlowBalanceStatisticsRequest.setPoRowNumber(0L);
            saveFlowBalanceStatisticsRequest.setPoQuantity(0L);
            saveFlowBalanceStatisticsRequest.setDifferQuantity(0L);
            saveFlowBalanceStatisticsRequest.setCollectTime(defaultTime);
            saveFlowBalanceStatisticsRequestMap.put(DateUtil.format(start, "yyyy-MM-dd"), saveFlowBalanceStatisticsRequest);

            start = DateUtil.offsetDay(start, 1);
        }
        //销售流向查询
        List<FlowSaleDO> flowSaleDOList = new ArrayList<>();
        {
            LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowSaleDO::getEid, eid);
            wrapper.ge(FlowSaleDO::getSoTime, startDateTime);
            wrapper.le(FlowSaleDO::getSoTime, endDateTime);
            Integer count = flowSaleService.count(wrapper);
            if (count != 0) {
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowSaleDO> page = new Page<>(current, size, count, false);
                    Page<FlowSaleDO> flowSaleDOPage = flowSaleService.page(page, wrapper);
                    List<FlowSaleDO> flowSaleList = flowSaleDOPage.getRecords();
                    if (CollUtil.isEmpty(flowSaleList)) {
                        break;
                    }
                    flowSaleDOList.addAll(flowSaleList);
                    current++;
                }
            }
        }

        //采购流向查询
        List<FlowPurchaseDO> flowPurchaseDOList = new ArrayList<>();
        {
            LambdaQueryWrapper<FlowPurchaseDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowPurchaseDO::getEid, eid);
            wrapper.ge(FlowPurchaseDO::getPoTime, startDateTime);
            wrapper.le(FlowPurchaseDO::getPoTime, endDateTime);
            Integer count = flowPurchaseService.count(wrapper);
            if (count != 0) {
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowPurchaseDO> page = new Page<>(current, size, count, false);
                    Page<FlowPurchaseDO> flowPurchaseDOPage = flowPurchaseService.page(page, wrapper);
                    List<FlowPurchaseDO> flowPurchaseList = flowPurchaseDOPage.getRecords();
                    if (CollUtil.isEmpty(flowPurchaseList)) {
                        break;
                    }
                    flowPurchaseDOList.addAll(flowPurchaseList);
                    current++;
                }
            }
        }
        //库存流向
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = new ArrayList<>();
        {
            LambdaQueryWrapper<FlowGoodsBatchDetailDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowGoodsBatchDetailDO::getEid, eid);
            wrapper.ge(FlowGoodsBatchDetailDO::getGbDetailTime, DateUtil.beginOfDay(endDateTime));
            wrapper.le(FlowGoodsBatchDetailDO::getGbDetailTime, endDateTime);
            Integer count = flowGoodsBatchDetailService.count(wrapper);
            if (count != 0) {
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowGoodsBatchDetailDO> page = new Page<>(current, size, count, false);
                    Page<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOPage = flowGoodsBatchDetailService.page(page, wrapper);
                    List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailList = flowGoodsBatchDetailDOPage.getRecords();
                    if (CollUtil.isEmpty(flowGoodsBatchDetailList)) {
                        break;
                    }
                    flowGoodsBatchDetailDOList.addAll(flowGoodsBatchDetailList);
                    current++;
                }
            }
        }

        for (FlowSaleDO flowSaleDO : flowSaleDOList) {
            String date = DateUtil.format(flowSaleDO.getSoTime(), "yyyy-MM-dd");
            FlowBalanceStatisticsDO saveFlowBalanceStatisticsRequest = saveFlowBalanceStatisticsRequestMap.get(date);
            saveFlowBalanceStatisticsRequest.setSoQuantity(saveFlowBalanceStatisticsRequest.getSoQuantity() + flowSaleDO.getSoQuantity().longValue());
            saveFlowBalanceStatisticsRequest.setSoRowNumber(saveFlowBalanceStatisticsRequest.getSoRowNumber() + 1);
            if (saveFlowBalanceStatisticsRequest.getCollectTime().getTime() < flowSaleDO.getCreateTime().getTime()) {
                saveFlowBalanceStatisticsRequest.setCollectTime(flowSaleDO.getCreateTime());
            }
        }

        for (FlowPurchaseDO flowPurchaseDO : flowPurchaseDOList) {
            String date = DateUtil.format(flowPurchaseDO.getPoTime(), "yyyy-MM-dd");
            FlowBalanceStatisticsDO saveFlowBalanceStatisticsRequest = saveFlowBalanceStatisticsRequestMap.get(date);
            saveFlowBalanceStatisticsRequest.setPoQuantity(saveFlowBalanceStatisticsRequest.getPoQuantity() + flowPurchaseDO.getPoQuantity().longValue());
            saveFlowBalanceStatisticsRequest.setPoRowNumber(saveFlowBalanceStatisticsRequest.getPoRowNumber() + 1);
            if (saveFlowBalanceStatisticsRequest.getCollectTime().getTime() < flowPurchaseDO.getCreateTime().getTime()) {
                saveFlowBalanceStatisticsRequest.setCollectTime(flowPurchaseDO.getCreateTime());
            }
        }

        for (FlowGoodsBatchDetailDO flowGoodsBatchDetailDO : flowGoodsBatchDetailDOList) {
            String date = DateUtil.format(flowGoodsBatchDetailDO.getGbDetailTime(), "yyyy-MM-dd");
            FlowBalanceStatisticsDO saveFlowBalanceStatisticsRequest = saveFlowBalanceStatisticsRequestMap.get(date);
            saveFlowBalanceStatisticsRequest.setGbQuantity(saveFlowBalanceStatisticsRequest.getGbQuantity() + flowGoodsBatchDetailDO.getGbNumber().longValue());
            saveFlowBalanceStatisticsRequest.setGbRowNumber(saveFlowBalanceStatisticsRequest.getGbRowNumber() + 1);
            if (saveFlowBalanceStatisticsRequest.getCollectTime().getTime() < flowGoodsBatchDetailDO.getCollectTime().getTime()) {
                saveFlowBalanceStatisticsRequest.setCollectTime(flowGoodsBatchDetailDO.getCollectTime());
            }

        }

        for (Map.Entry<String, FlowBalanceStatisticsDO> entry : saveFlowBalanceStatisticsRequestMap.entrySet()) {
            FlowBalanceStatisticsDO saveFlowBalanceStatisticsRequest = entry.getValue();
            if (entry.getKey().equals(DateUtil.format(endDateTime, "yyyy-MM-dd"))) {
                FlowBalanceStatisticsDO lastSaveFlowBalanceStatisticsRequest = saveFlowBalanceStatisticsRequestMap.get(DateUtil.format(DateUtil.offsetDay(saveFlowBalanceStatisticsRequest.getDateTime(), -1), "yyyy-MM-dd"));
                if (lastSaveFlowBalanceStatisticsRequest == null) {
                    saveFlowBalanceStatisticsRequest.setLastGbQuantity(0L);
                } else {
                    saveFlowBalanceStatisticsRequest.setLastGbQuantity(lastSaveFlowBalanceStatisticsRequest.getGbQuantity());
                }
            }
            //相差数量=（上一天库存数量+采购数量-当天库存数量-销售数量
            Long differ = saveFlowBalanceStatisticsRequest.getLastGbQuantity() + saveFlowBalanceStatisticsRequest.getPoQuantity() - saveFlowBalanceStatisticsRequest.getGbQuantity() - saveFlowBalanceStatisticsRequest.getSoQuantity();
            saveFlowBalanceStatisticsRequest.setDifferQuantity(differ);
        }
        return new ArrayList<>(saveFlowBalanceStatisticsRequestMap.values());
    }

}
