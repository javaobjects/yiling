package com.yiling.dataflow.wash.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.dataflow.wash.dao.UnlockCustomerMatchingInfoMapper;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerMatchingInfoPageRequest;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.entity.UnlockCustomerMatchingInfoDO;
import com.yiling.dataflow.wash.entity.UnlockCustomerMatchingTaskDO;
import com.yiling.dataflow.wash.service.UnlockCustomerMatchingInfoService;
import com.yiling.dataflow.wash.service.UnlockCustomerMatchingTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.search.flow.api.EsFlowEnterpriseCustomerMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 非锁客户匹配度表 服务实现类
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
@Slf4j
@Service
public class UnlockCustomerMatchingInfoServiceImpl extends BaseServiceImpl<UnlockCustomerMatchingInfoMapper, UnlockCustomerMatchingInfoDO> implements UnlockCustomerMatchingInfoService {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Autowired
    private UnlockCustomerMatchingTaskService unlockCustomerMatchingTaskService;

    @Autowired
    private FlowEnterpriseCustomerMappingService flowEnterpriseCustomerMappingService;

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @DubboReference
    private EsFlowEnterpriseCustomerMappingApi esFlowEnterpriseCustomerMappingApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    // 消息队列
    @Override
    public void matchingRateExecute(String name) {
        if (StringUtils.isEmpty(name)) {
            return;
        }

        UnlockCustomerMatchingTaskDO unlockCustomerMatchingTaskDO = new UnlockCustomerMatchingTaskDO();

        // 加锁
        String lockKey = RedisKey.generate("customer-matching-rate-task-", name);
        String lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);
        try {
            //  匹配度任务防止重复跑
            UnlockCustomerMatchingTaskDO taskDO = unlockCustomerMatchingTaskService.getByCustomerName(name);
            if (taskDO != null) {
                //  该客户名称正在跑 或者 已跑过匹配度
                return;
            }

            unlockCustomerMatchingTaskDO.setCustomerName(name);
            unlockCustomerMatchingTaskDO.setStartTime(new Date());
            unlockCustomerMatchingTaskDO.setStatus(1);  // 进行中
            unlockCustomerMatchingTaskService.save(unlockCustomerMatchingTaskDO);
        } finally {
            redisDistributedLock.releaseLock(lockKey, lockId);
        }

        // 执行匹配度计算
        List<UnlockCustomerMatchingInfoDO> list = executeRate(name, unlockCustomerMatchingTaskDO.getId());

        if (list.size() > 0) {
            this.batchInsert(list);

            // 根据分数排序
            list.sort(Comparator.comparing(UnlockCustomerMatchingInfoDO::getRecommendScore).reversed());
            UnlockCustomerMatchingInfoDO matchingInfo = list.get(0);    // 匹配分数最高一条

            //  修改客户映射表
            updateMappingDTOListByName(name, matchingInfo.getRecommendCrmId(), matchingInfo.getRecommendName(), matchingInfo.getRecommendScore());
        }

        unlockCustomerMatchingTaskDO.setStatus(2);  // 已完成
        unlockCustomerMatchingTaskDO.setMatchCount(list.size());
        unlockCustomerMatchingTaskDO.setEndTime(new Date());
        unlockCustomerMatchingTaskDO.setUpdateTime(new Date());
        unlockCustomerMatchingTaskService.updateById(unlockCustomerMatchingTaskDO);
    }

    @Async("asyncExecutor")
    @Override
    public void matchingRateBatchExecute() {
        log.info("开始批量执行客户对照匹配度任务...");
        EsFlowEnterpriseCustomerMappingSearchRequest request = new EsFlowEnterpriseCustomerMappingSearchRequest();
        request.setCrmOrgId(0L);
        request.setRecommendOrgCrmId(0L);
        request.setOrgDatascope(3);

//        Map<String, List<CrmEnterpriseDTO>> crmEnterpriseCacheMap = new HashMap<>();
        int current = 1;
        int size = 2000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);
            EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> esAggregationDTO = esFlowEnterpriseCustomerMappingApi.searchFlowEnterpriseCustomerMapping(request);
            if (CollUtil.isEmpty(esAggregationDTO.getData())) {
                break;
            }

            List<String> nameList = esAggregationDTO.getData().stream().map(EsFlowEnterpriseCustomerMappingDTO::getFlowCustomerName).distinct().collect(Collectors.toList());

            //  批量执行匹配度任务
            this.matchingRateBatchExecuteByNameList(nameList);

            if (esAggregationDTO.getData().size() < size) {
                break;
            }
            current++;
        }
        log.info("开始批量执行客户对照匹配度任务...");
    }

    private void matchingRateBatchExecuteByNameList(List<String> nameList) {
        if (CollUtil.isEmpty(nameList)) {
            return;
        }
        // 获取已经执行过的名字列表
        List<UnlockCustomerMatchingTaskDO> unlockCustomerMatchingTaskDOList = unlockCustomerMatchingTaskService.getByCustomerNameList(nameList);
        Set<String> executedNameList = unlockCustomerMatchingTaskDOList.stream().map(UnlockCustomerMatchingTaskDO::getCustomerName).collect(Collectors.toSet());

        List<UnlockCustomerMatchingTaskDO> taskList = new ArrayList<>();
        for (String name : nameList) {
            if (executedNameList.contains(name)) {
                // 已经执行过的不再执行
                continue;
            }

            // 如果行业库存在该名字，则直接匹配
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseService.getCrmEnterpriseCodeByName(name, false);
            if (crmEnterpriseDTO != null) {
                UnlockCustomerMatchingTaskDO unlockCustomerMatchingTaskDO = new UnlockCustomerMatchingTaskDO();
                unlockCustomerMatchingTaskDO.setCustomerName(name);
                unlockCustomerMatchingTaskDO.setStartTime(new Date());
                unlockCustomerMatchingTaskDO.setEndTime(unlockCustomerMatchingTaskDO.getStartTime());
                unlockCustomerMatchingTaskDO.setStatus(2);  // 已完成
                unlockCustomerMatchingTaskDO.setMatchCount(1);
                unlockCustomerMatchingTaskService.save(unlockCustomerMatchingTaskDO);

                updateMappingDTOListByName(name, crmEnterpriseDTO.getId(), crmEnterpriseDTO.getName(), BigDecimal.valueOf(100));

                UnlockCustomerMatchingInfoDO unlockCustomerMatchingInfoDO = new UnlockCustomerMatchingInfoDO();
                unlockCustomerMatchingInfoDO.setTaskId(unlockCustomerMatchingTaskDO.getId());
                unlockCustomerMatchingInfoDO.setCustomerName(name);
                unlockCustomerMatchingInfoDO.setRecommendCrmId(crmEnterpriseDTO.getId());
                unlockCustomerMatchingInfoDO.setRecommendName(crmEnterpriseDTO.getName());
                unlockCustomerMatchingInfoDO.setRecommendScore(new BigDecimal(100));
                this.save(unlockCustomerMatchingInfoDO);
                continue;
            }

            UnlockCustomerMatchingTaskDO unlockCustomerMatchingTaskDO = new UnlockCustomerMatchingTaskDO();
            unlockCustomerMatchingTaskDO.setCustomerName(name);
            unlockCustomerMatchingTaskDO.setStartTime(new Date());
            unlockCustomerMatchingTaskDO.setStatus(1);  // 进行中
            unlockCustomerMatchingTaskService.save(unlockCustomerMatchingTaskDO);
            taskList.add(unlockCustomerMatchingTaskDO);
        }

        // 执行匹配度批量计算
        Map<String, List<UnlockCustomerMatchingInfoDO>> map = executeRateBatch(taskList);

        for (UnlockCustomerMatchingTaskDO task : taskList) {
            List<UnlockCustomerMatchingInfoDO> list = new ArrayList<>();
            if (map.containsKey(task.getCustomerName())) {
                list = map.get(task.getCustomerName());
            }
            if (CollUtil.isNotEmpty(list)) {
                // 根据分数排序
                list.sort(Comparator.comparing(UnlockCustomerMatchingInfoDO::getRecommendScore).reversed());
                UnlockCustomerMatchingInfoDO matchingInfo = list.get(0);    // 匹配分数最高一条

                //  修改客户映射表
                updateMappingDTOListByName(task.getCustomerName(), matchingInfo.getRecommendCrmId(), matchingInfo.getRecommendName(), matchingInfo.getRecommendScore());

                // 批量插入UnlockCustomerMatchingInfoDO
                this.batchInsert(list);
            }

            task.setStatus(2);  // 已完成
            task.setEndTime(new Date());
            task.setUpdateTime(new Date());
            int matchCount = 0;
            if (map.containsKey(task.getCustomerName())) {
                matchCount = map.get(task.getCustomerName()).size();
            }
            task.setMatchCount(matchCount);
            unlockCustomerMatchingTaskService.updateById(task);
        }

    }




    @Override
    public List<UnlockCustomerMatchingInfoDO> getListByCustomerName(String customerName) {
        LambdaQueryWrapper<UnlockCustomerMatchingInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCustomerMatchingInfoDO::getCustomerName, customerName);
        wrapper.orderByDesc(UnlockCustomerMatchingInfoDO::getRecommendScore);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<UnlockCustomerMatchingInfoDO> getPageList(QueryUnlockCustomerMatchingInfoPageRequest request) {
        if (StringUtils.isEmpty(request.getCustomerName())) {
            return new Page<>();
        }
        Page<UnlockCustomerMatchingInfoDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockCustomerMatchingInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCustomerMatchingInfoDO::getCustomerName, request.getCustomerName());
        wrapper.orderByDesc(UnlockCustomerMatchingInfoDO::getRecommendScore);
        return baseMapper.selectPage(page, wrapper);
    }


    private List<UnlockCustomerMatchingInfoDO> executeRate(String name, Long taskId) {
        // 匹配度大于90%的客户信息，分页执行
        List<UnlockCustomerMatchingInfoDO> list = new ArrayList<>();
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        int current = 1;
        int size = 2000;
        outer: while (true) {
            // 分页获取crmEnterprise列表
            List<CrmEnterpriseIdAndNameBO> crmEnterpriseList = getCrmEnterpriseDTOList(current, size);
            if (CollUtil.isEmpty(crmEnterpriseList)) {
                break;
            }

            try {
                for (CrmEnterpriseIdAndNameBO crmEnterprise : crmEnterpriseList) {
                    if (StringUtils.isEmpty(crmEnterprise.getName())) {
                        continue;
                    }
                    //  计算相似度
                    double b2 = 1 - jaroWinklerDistance.apply(name, crmEnterprise.getName());
                    if (b2 > 0.9) {
                        // 相似度>90% 插入至非锁客户匹配度表
                        UnlockCustomerMatchingInfoDO unlockCustomerMatchingInfoDO = new UnlockCustomerMatchingInfoDO();
                        unlockCustomerMatchingInfoDO.setTaskId(taskId);
                        unlockCustomerMatchingInfoDO.setCustomerName(name);
                        unlockCustomerMatchingInfoDO.setRecommendCrmId(crmEnterprise.getId());
                        unlockCustomerMatchingInfoDO.setRecommendName(crmEnterprise.getName());
                        unlockCustomerMatchingInfoDO.setRecommendScore(new BigDecimal(b2 * 100));
                        list.add(unlockCustomerMatchingInfoDO);
                        if (b2 == 1) {      // 如果存在完全匹配的，则直接跳出
                            break outer;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            current ++;
        }

        return list;
    }

    private List<CrmEnterpriseIdAndNameBO> getCrmEnterpriseDTOList(Integer current, Integer size) {
        Page<CrmEnterpriseIdAndNameBO> pageResult = crmEnterpriseService.getIdAndNameListPage(current, size);
        return pageResult.getRecords();
    }

    private Map<String, List<UnlockCustomerMatchingInfoDO>> executeRateBatch(List<UnlockCustomerMatchingTaskDO> taskList) {
        Map<String, List<UnlockCustomerMatchingInfoDO>> map = new HashMap<>();

        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        int current = 1;
        int size = 2000;
        while (true) {
            // 分页获取crmEnterprise列表
            List<CrmEnterpriseIdAndNameBO> crmEnterpriseList = getCrmEnterpriseDTOList(current, size);
            if (CollUtil.isEmpty(crmEnterpriseList)) {
                break;
            }

            for (UnlockCustomerMatchingTaskDO task : taskList) {
                try {
                    for (CrmEnterpriseIdAndNameBO crmEnterprise : crmEnterpriseList) {
                        if (StringUtils.isEmpty(crmEnterprise.getName())) {
                            continue;
                        }
                        //  计算相似度
                        double b2 = 1 - jaroWinklerDistance.apply(task.getCustomerName(), crmEnterprise.getName());
                        if (b2 > 0.9) {
                            // 相似度>90% 插入至非锁客户匹配度表
                            UnlockCustomerMatchingInfoDO unlockCustomerMatchingInfoDO = new UnlockCustomerMatchingInfoDO();
                            unlockCustomerMatchingInfoDO.setTaskId(task.getId());
                            unlockCustomerMatchingInfoDO.setCustomerName(task.getCustomerName());
                            unlockCustomerMatchingInfoDO.setRecommendCrmId(crmEnterprise.getId());
                            unlockCustomerMatchingInfoDO.setRecommendName(crmEnterprise.getName());
                            unlockCustomerMatchingInfoDO.setRecommendScore(new BigDecimal(b2 * 100));

                            if (!map.containsKey(task.getCustomerName())) {
                                map.put(task.getCustomerName(), new ArrayList<>());
                            }
                            map.get(task.getCustomerName()).add(unlockCustomerMatchingInfoDO);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            current ++;
        }

        return map;
    }

    public void batchInsert(List<UnlockCustomerMatchingInfoDO> list) {
        // 分页截取并插入
        if (CollUtil.isEmpty(list)) {
            return;
        }
        handleUnlockCustomerMatchingInfoList(list);

        int total = list.size();
        int pageCount;
        int size = 1000;

        if (total % size == 0) {
            pageCount = total / size;
        } else {
            pageCount = total / size + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引
        for (int i = 0; i < pageCount; i++) {
            fromIndex = i * size;
            toIndex = (i + 1) * size;
            if (toIndex > total) {
                toIndex = total;
            }
            baseMapper.insertBatchSomeColumn(list.subList(fromIndex, toIndex));
        }
    }

    private void handleUnlockCustomerMatchingInfoList(List<UnlockCustomerMatchingInfoDO> list) {
        for (UnlockCustomerMatchingInfoDO unlockCustomerMatchingInfoDO : list) {
            unlockCustomerMatchingInfoDO.setTaskId(Convert.toLong(unlockCustomerMatchingInfoDO.getTaskId(), 0L));
            unlockCustomerMatchingInfoDO.setCustomerName(Convert.toStr(unlockCustomerMatchingInfoDO.getCustomerName(), ""));
            unlockCustomerMatchingInfoDO.setRecommendCrmId(Convert.toLong(unlockCustomerMatchingInfoDO.getRecommendCrmId(), 0L));
            unlockCustomerMatchingInfoDO.setRecommendName(Convert.toStr(unlockCustomerMatchingInfoDO.getRecommendName(), ""));
            unlockCustomerMatchingInfoDO.setRecommendScore(Convert.toBigDecimal(unlockCustomerMatchingInfoDO.getRecommendScore(), BigDecimal.ZERO));
            unlockCustomerMatchingInfoDO.setDelFlag(Convert.toInt(unlockCustomerMatchingInfoDO.getDelFlag(), 0));
            unlockCustomerMatchingInfoDO.setCreateTime(Convert.toDate(unlockCustomerMatchingInfoDO.getCreateTime(), new Date()));
            unlockCustomerMatchingInfoDO.setUpdateTime(Convert.toDate(unlockCustomerMatchingInfoDO.getUpdateTime(), new Date()));
            unlockCustomerMatchingInfoDO.setCreateUser(Convert.toLong(unlockCustomerMatchingInfoDO.getCreateUser(), 0L));
            unlockCustomerMatchingInfoDO.setUpdateUser(Convert.toLong(unlockCustomerMatchingInfoDO.getUpdateUser(), 0L));
            unlockCustomerMatchingInfoDO.setRemark(Convert.toStr(unlockCustomerMatchingInfoDO.getRemark(), ""));
        }
    }

    private void updateMappingDTOListByName(String name, Long recommendCrmId, String recommendName, BigDecimal recommendScore) {
        //  修改客户映射表
        List<FlowEnterpriseCustomerMappingDTO> mappingDTOList = flowEnterpriseCustomerMappingService.findUnmappedByFlowCustomerName(name);
        if (CollUtil.isNotEmpty(mappingDTOList)) {
            for (FlowEnterpriseCustomerMappingDTO flowEnterpriseCustomerMappingDTO : mappingDTOList) {
                if (flowEnterpriseCustomerMappingDTO.getRecommendScore() == null || flowEnterpriseCustomerMappingDTO.getRecommendScore().compareTo(recommendScore) < 0) {
                    FlowEnterpriseCustomerMappingDO flowEnterpriseCustomerMappingDO = new FlowEnterpriseCustomerMappingDO();
                    flowEnterpriseCustomerMappingDO.setId(flowEnterpriseCustomerMappingDTO.getId());
                    flowEnterpriseCustomerMappingDO.setRecommendOrgCrmId(recommendCrmId);
                    flowEnterpriseCustomerMappingDO.setRecommendOrgName(recommendName);
                    flowEnterpriseCustomerMappingDO.setRecommendScore(recommendScore);
                    flowEnterpriseCustomerMappingService.updateById(flowEnterpriseCustomerMappingDO);
                }
            }
        }
    }

}
