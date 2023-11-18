package com.yiling.dataflow.backup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;
import com.yiling.dataflow.backup.service.DataFlowBackupService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.backup.util.DataBaseConnection;
import com.yiling.dataflow.config.DatasourceConfig;
import com.yiling.dataflow.flow.api.FlowBiTaskApi;
import com.yiling.dataflow.order.bo.FlowGoodsBatchDetailMonthBO;
import com.yiling.dataflow.order.bo.FlowPurchaseMonthBO;
import com.yiling.dataflow.order.bo.FlowSaleMonthBO;
import com.yiling.dataflow.order.dao.FlowGoodsBatchDetailMapper;
import com.yiling.dataflow.order.dao.FlowGoodsBatchMapper;
import com.yiling.dataflow.order.dao.FlowPurchaseMapper;
import com.yiling.dataflow.order.dao.FlowSaleMapper;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 线上ERP流向数据备份清理服务类
 *
 * @author: houjie.sun
 * @date: 2022/7/15
 */
@Slf4j
@Service
public class DataFlowBackupServiceImpl implements DataFlowBackupService {

    @Autowired
    private DatasourceConfig datasourceConfig;
    @Autowired
    private FlowPurchaseMapper flowPurchaseMapper;
    @Autowired
    private FlowSaleMapper flowSaleMapper;
    @Autowired
    private FlowGoodsBatchMapper flowGoodsBatchMapper;
    @Autowired
    private FlowGoodsBatchDetailMapper flowGoodsBatchDetailMapper;

    @Autowired
    private FlowSaleService flowSaleService;
    @Autowired
    private FlowPurchaseService flowPurchaseService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @DubboReference
    private FlowBiTaskApi flowBiTaskApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String FLOW_PURCHASE = "flow_purchase";
    public static final String FLOW_SALE = "flow_sale";
    public static final String FLOW_GOODS_BATCH = "flow_goods_batch";
    public static final String FLOW_GOODS_BATCH_DETAIL = "flow_goods_batch_detail";
    public static final String FLOW_PURCHASE_TABLE_NAME = "flow_purchase_table_name";

    public static void main(String[] args) {
        String monthBackup7 = BackupUtil.monthBackup(6);

    }

    @Override
    public void dataFlowBackup(DataFlowBackupRequest request) {
        log.info("线上流向备份清理任务执行开始, dataFlowBackupJobHandler, dataFlowBackup");
        Assert.notNull(request, "参数 eidList 不能为空");
        Assert.notNull(request.getEidList(), "参数 eidList 不能为空");
        long start = System.currentTimeMillis();
        // 采购、销售，每年备份一个表，6个月之前的数据放进备份表，当前业务表保留当前6个月的数据
        // yiling.flow_purchase  备份、清理
        // yiling.flow_sale  备份、清理
        // yiling.flow_goods_batch_detail  备份、清理
        // yiling.flow_goods_batch 清理

        // 查询流向已存在的表
        List<String> flowTableNames = DataBaseConnection.getInstance().getTableNames(datasourceConfig, "flow");
        // 已存在的备份表
        List<String> backupTableNames = new ArrayList<>();
        Date taskTime = new Date();
        // 企业列表
        List<Long> eidList = request.getEidList();
        for (Long eid : eidList) {
            try {
                /* 备份的月份，两种处理逻辑：*/
                /*     1.第6个月的备份，不包括当前月份，向前推6个自然月（即是第7个自然月），前推第6个月线上的数据插入到备份表中。1.1：每月1号全量备份；1.2：其他时间明细比较备份，备份完成后则线上数据进行物理删除。*/
                /*     2.第7个月及以前的备份，不包括当前月份，向前推7个自然月（即是第8个自然月），前推第7个月线上的数据插入到备份表中，全量或明细比较，已备份这个月的线上数据进行物理删除。*/

                // 一、排除当前月份，向前推第6个月的数据备份，备份表有第6个月的则删除，然后把线上的批量插入备份表，线上这个月的不动
                // 第6个月的 年份、月份

                /*{
                    // 备份表年份
                    String monthBackup6 = BackupUtil.monthBackup(6);
                    DateTime monthBackupDate6 = DateUtil.parse(monthBackup6, BackupUtil.FORMATE_MONTH);
                    String yearBackup6 = DateUtil.format(monthBackupDate6, BackupUtil.FORMATE_YEAR_ONLY);
                    // 待备份表名称
                    String flowPurchaseBackupName6 = FLOW_PURCHASE.concat("_").concat(yearBackup6);
                    String flowSaleNewBackupName6 = FLOW_SALE.concat("_").concat(yearBackup6);
                    String flowGoodsBatchDetailBackupName6 = FLOW_GOODS_BATCH_DETAIL.concat("_").concat(yearBackup6);
                    // 已存在的备份表
                    getBackupTableNames(flowTableNames, backupTableNames, flowPurchaseBackupName6, flowSaleNewBackupName6, flowGoodsBatchDetailBackupName6);

                    // 每月1号全量备份，线上数据不动；其他时间明细比较备份，备份完成后则线上数据进行物理删除
                    String taskDay = DateUtil.format(taskTime, "yyyy-MM-dd");
                    String beginOfMonth = DateUtil.format(DateUtil.beginOfMonth(taskTime), "yyyy-MM-dd");
                    if (ObjectUtil.equal(taskDay, beginOfMonth)){
                        // 库存
                        backupFlowGoodsBatchDetail6(backupTableNames, eid, flowGoodsBatchDetailBackupName6, monthBackup6, taskTime);
                        // 采购
                        backupFlowPurchase6(backupTableNames, eid, flowPurchaseBackupName6, monthBackup6);
                        // 销售
                        backupFlowSale6(backupTableNames, eid, flowSaleNewBackupName6, monthBackup6);
                    } else {
                        // 待备份表名称
                        String flowPurchaseBackupNamePrefix7 = FLOW_PURCHASE.concat("_");
                        String flowSaleNewBackupNamePrefix7 = FLOW_SALE.concat("_");
                        String flowGoodsBatchDetailBackupNamePrefix7 = FLOW_GOODS_BATCH_DETAIL.concat("_");
                        // 库存
                        handlerFlowGoodsBatchDetail6(eid, flowTableNames, backupTableNames, flowGoodsBatchDetailBackupNamePrefix7, monthBackup6);
                        // 采购
                        handlerFlowPurchase6(eid, flowTableNames, backupTableNames, flowPurchaseBackupNamePrefix7, monthBackup6);
                        // 销售
                        handlerFlowSale6(eid, flowTableNames, backupTableNames, flowSaleNewBackupNamePrefix7, monthBackup6);
                    }
                }*/

                // 二、排除当前月份，向前推第7个月及之前的数据备份，备份表有这个备份月份的则删除，然后把线上的批量插入备份表，最后删除线上的所备份月份数据
                String monthBackup7 = BackupUtil.monthBackup(6);
                {
                    // 第7个月的 年份、月份

                    // 备份表年份
                    DateTime monthBackupDate7 = DateUtil.parse(monthBackup7, BackupUtil.FORMATE_MONTH);
                    String yearBackup7 = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
                    // 待备份表名称
                    String flowPurchaseBackupNamePrefix7 = FLOW_PURCHASE.concat("_");
                    String flowSaleNewBackupNamePrefix7 = FLOW_SALE.concat("_");
                    String flowGoodsBatchDetailBackupNamePrefix7 = FLOW_GOODS_BATCH_DETAIL.concat("_");

                    // 库存
                    backupFlowGoodsBatchDetail7(eid, flowTableNames, backupTableNames, flowGoodsBatchDetailBackupNamePrefix7, monthBackup7);
                    // 采购
                    backupFlowPurchase7(eid, flowTableNames, backupTableNames, flowPurchaseBackupNamePrefix7, monthBackup7);
                    // 销售
                    backupFlowSale7(eid, flowTableNames, backupTableNames, flowSaleNewBackupNamePrefix7, monthBackup7);
                }

                /* 采购、销售、库存、库存汇总，物理删除 del_flag=1 的 */
                // 采购，del_flag=1 删除6个月之内的
                //                flowPurchaseMapper.deleteByDelFlagAndEid(eid, monthBackup7);
                // 销售，del_flag=1 删除6个月之内的
                //                flowSaleMapper.deleteByDelFlagAndEid(eid, monthBackup7);
                flowGoodsBatchMapper.deleteByDelFlagAndEid(eid);
                flowGoodsBatchDetailMapper.deleteByDelFlagAndEid(eid);
            } catch (Exception e) {
                log.error("[线上ERP流向数据备份] 异常, exception:{}", e.getMessage());
                e.printStackTrace();
            }

        }
        long end = System.currentTimeMillis();
        log.info("线上库ERP流向备份清理任务执行完成, dataFlowBackupJobHandler, dataFlowBackup, 耗时: {}", end - start);
    }

    @Override
    public List<String> getTableNamesByPrefix(String tableNamePrefix) {
        // 查询流向已存在的表
        List<String> flowTableNames = null;
        String key = FLOW_PURCHASE_TABLE_NAME;
        String tableNameson = (String) stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(tableNameson)) {
            flowTableNames = DataBaseConnection.getInstance().getTableNames(datasourceConfig, tableNamePrefix);
            stringRedisTemplate.opsForValue().set(key, JSONArray.toJSONString(flowTableNames), 5, TimeUnit.MINUTES);
        } else {
            flowTableNames = JSONArray.parseArray(tableNameson, String.class);
        }
        return flowTableNames;
    }

    @Override
    public void dataFlowBackupNew(DataFlowBackupRequest request) {
        log.info("线上流向备份清理任务执行开始, dataFlowBackupJobHandler, dataFlowBackup");
        Assert.notNull(request, "参数 eidList 不能为空");
        Assert.notNull(request.getEidList(), "参数 eidList 不能为空");
        long start = System.currentTimeMillis();
        // 采购、销售，每年备份一个表，6个月之前的数据放进备份表，当前业务表保留当前6个月的数据
        // yiling.flow_purchase  备份、清理
        // yiling.flow_sale  备份、清理
        // yiling.flow_goods_batch_detail  备份、清理
        // yiling.flow_goods_batch 清理

        // 查询流向已存在的表
        List<String> flowTableNames = DataBaseConnection.getInstance().getTableNames(datasourceConfig, "flow");
        // 已存在的备份表
        List<String> backupTableNames = new ArrayList<>();
        Date taskTime = new Date();
        // 第7个月及以前的备份，包括当前月份向前推6个自然月,取第7个自然月，前推第7个月线上的数据插入到备份表中，按照eid+年月 全量批量保存到备份表，已备份这个月的线上数据进行物理删除。
        String monthBackup7 = BackupUtil.monthBackup(6);
        DateTime monthBackupDate = DateUtil.parse(monthBackup7, BackupUtil.FORMATE_MONTH);
        String endTimeBackup = DateUtil.format(DateUtil.endOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);

        // 企业列表
        List<Long> eidList = request.getEidList();
        for (Long eid : eidList) {
            try {
                // 待备份表名称前缀
                String flowPurchaseBackupNamePrefix7 = FLOW_PURCHASE.concat("_");
                String flowSaleNewBackupNamePrefix7 = FLOW_SALE.concat("_");
                String flowGoodsBatchDetailBackupNamePrefix7 = FLOW_GOODS_BATCH_DETAIL.concat("_");

                // 库存
                backupFlowGoodsBatchDetail7New(eid, flowTableNames, backupTableNames, flowGoodsBatchDetailBackupNamePrefix7, monthBackup7);
                // 采购
                backupFlowPurchase7New(eid, flowTableNames, backupTableNames, flowPurchaseBackupNamePrefix7, monthBackup7);
                // 销售
                backupFlowSale7New(eid, flowTableNames, backupTableNames, flowSaleNewBackupNamePrefix7, monthBackup7);

                flowGoodsBatchMapper.deleteByDelFlagAndEid(eid);
                flowGoodsBatchDetailMapper.deleteByDelFlagAndEid(eid);

                // flow_bi_task 物理删除第8个月及之前的所有数据
                flowBiTaskApi.deleteByTaskTime(endTimeBackup, eid);
            } catch (Exception e) {
                log.error("[线上ERP流向数据备份] 异常, exception:{}", e.getMessage());
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        log.info("线上库ERP流向备份清理任务执行完成, dataFlowBackupJobHandler, dataFlowBackup, 耗时: {}", end - start);
    }

    /**
     * 第7个月的库存备份
     *
     * @param eid 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupFlowGoodsBatchDetail7New(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        List<FlowGoodsBatchDetailMonthBO> gbDetailMonthList = flowGoodsBatchDetailMapper.getMonthListByEidAndGbDetailMonth(eid, monthBackup);
        if (CollUtil.isEmpty(gbDetailMonthList)) {
            return;
        }
        for (FlowGoodsBatchDetailMonthBO flowGoodsBatchDetailMonthBO : gbDetailMonthList) {
            handlerFlowGoodsBatchDetail7New(eid, flowTableNames, backupTableNames, tempTableNamePrefix, flowGoodsBatchDetailMonthBO, true);
        }
    }

    @GlobalTransactional
    private void handlerFlowGoodsBatchDetail7New(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, FlowGoodsBatchDetailMonthBO flowGoodsBatchDetailMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String gbDetailMonth = flowGoodsBatchDetailMonthBO.getGbDetailMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(gbDetailMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        // 创建备份表
        if (!backupTableNames.contains(tableName)) {
            flowGoodsBatchDetailMapper.createBackupTable(FLOW_GOODS_BATCH_DETAIL, tableName);
            flowGoodsBatchDetailMapper.addColumn(tableName);
            backupTableNames.add(tableName);
        }
        // 不管是否存在备份，直接保存到备份
        Integer insertCount = flowGoodsBatchDetailMapper.insertBackupDataByEidAndGbDetailMonth(eid, tableName, FLOW_GOODS_BATCH_DETAIL, gbDetailMonth);
        if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
            throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据, 保存备份数据失败, 企业id:" + eid + ", 月份: " + gbDetailMonth + ", 表名: " + tableName);
        }
        // 物理删除此月份的线上数据
        Integer deleteBackCount = flowGoodsBatchDetailMapper.deleteByEidAndGbDetailMonth(eid, FLOW_GOODS_BATCH_DETAIL, gbDetailMonth);
        if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
            throw new ServiceException("[ERP流向库存汇总明细备份], 删除当前月份的线上数据失败, 企业id:" + eid + ", 月份: " + gbDetailMonth + ", 表名: " + FLOW_GOODS_BATCH_DETAIL);
        }
    }

    /**
     * 第7个月的采购备份
     * 处理未删除、已删除的所有
     *
     * @param eid 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupFlowPurchase7New(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        List<FlowPurchaseMonthBO> gbDetailMonthList = flowPurchaseMapper.getMonthListByEidAndPoMonth(eid, monthBackup);
        if (CollUtil.isEmpty(gbDetailMonthList)) {
            return;
        }
        for (FlowPurchaseMonthBO flowPurchaseMonthBO : gbDetailMonthList) {
            handlerFlowPurchase7New(eid, flowTableNames, backupTableNames, tempTableNamePrefix, flowPurchaseMonthBO, true);
        }
    }

    @GlobalTransactional
    private void handlerFlowPurchase7New(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, FlowPurchaseMonthBO flowPurchaseMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String poMonth = flowPurchaseMonthBO.getPoMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(poMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        // 创建备份表
        if (!backupTableNames.contains(tableName)) {
            flowPurchaseMapper.createBackupTable(FLOW_PURCHASE, tableName);
            flowPurchaseMapper.addColumn(tableName);
            backupTableNames.add(tableName);
        }
        // 不管是否存在备份，直接保存到备份
        Integer insertCount = flowPurchaseMapper.insertBackupDataByEidAndPoMonth(eid, tableName, FLOW_PURCHASE, poMonth);
        if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
            throw new ServiceException("[ERP流向采购备份], 处理第7个月及之前的数据, 保存备份数据失败, 企业id:" + eid + ", 月份: " + poMonth + ", 表名: " + tableName);
        }
        // 物理删除此月份的线上数据
        Integer deleteBackCount = flowPurchaseMapper.deleteByEidAndPoMonth(eid, FLOW_PURCHASE, poMonth);
        if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
            throw new ServiceException("[ERP流向采购备份], 删除当前月份的线上数据失败, 企业id:" + eid + ", 月份: " + poMonth + ", 表名: " + FLOW_PURCHASE);
        }
    }

    /**
     * 第7个月的销售备份
     * 处理未删除、已删除的所有
     *
     * @param eid 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupFlowSale7New(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        List<FlowSaleMonthBO> gbDetailMonthList = flowSaleMapper.getMonthListByEidAndSoMonth(eid, monthBackup);
        if (CollUtil.isEmpty(gbDetailMonthList)) {
            return;
        }
        for (FlowSaleMonthBO flowSaleMonthBO : gbDetailMonthList) {
            handlerFlowSale7New(eid, flowTableNames, backupTableNames, tempTableNamePrefix, flowSaleMonthBO, true);
        }
    }

    @GlobalTransactional
    private void handlerFlowSale7New(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, FlowSaleMonthBO flowSaleMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String soMonth = flowSaleMonthBO.getSoMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(soMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        // 创建备份表
        if (!backupTableNames.contains(tableName)) {
            flowSaleMapper.createBackupTable(FLOW_SALE, tableName);
            flowSaleMapper.addColumn(tableName);
            backupTableNames.add(tableName);
        }
        // 不管是否存在备份，直接保存到备份
        Integer insertCount = flowSaleMapper.insertBackupDataByEidAndSoMonth(eid, tableName, FLOW_SALE, soMonth);
        if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
            throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 保存备份数据失败, 企业id:" + eid + ", 月份: " + soMonth + ", 表名: " + tableName);
        }
        // 物理删除此月份的线上数据
        Integer deleteBackCount = flowSaleMapper.deleteByEidAndSoMonth(eid, FLOW_SALE, soMonth);
        if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
            throw new ServiceException("[ERP流向销售备份], 删除当前月份的线上数据失败, 企业id:" + eid + ", 月份: " + soMonth + ", 表名: " + FLOW_SALE);
        }
    }


    private void handlerFlowSale6(Long eid, List<String> flowTableNames, List<String> backupTableNames, String backupNamePrefix7, String monthBackup6) {
        Long count = flowSaleMapper.getOneByEidAndSoMonth(eid, FLOW_SALE, monthBackup6);
        if (ObjectUtil.isNotNull(count) && count.intValue() > 0) {
            FlowSaleMonthBO flowSaleMonthBO = new FlowSaleMonthBO();
            flowSaleMonthBO.setEid(eid);
            flowSaleMonthBO.setSoMonth(monthBackup6);
            handlerFlowSale7(eid, flowTableNames, backupTableNames, backupNamePrefix7, flowSaleMonthBO, true);
        }
    }

    private void handlerFlowPurchase6(Long eid, List<String> flowTableNames, List<String> backupTableNames, String backupNamePrefix7, String monthBackup6) {
        Long count = flowPurchaseMapper.getOneByEidAndPoMonth(eid, FLOW_PURCHASE, monthBackup6);
        if (ObjectUtil.isNotNull(count) && count.intValue() > 0) {
            FlowPurchaseMonthBO flowPurchaseMonthBO = new FlowPurchaseMonthBO();
            flowPurchaseMonthBO.setEid(eid);
            flowPurchaseMonthBO.setPoMonth(monthBackup6);
            handlerFlowPurchase7(eid, flowTableNames, backupTableNames, backupNamePrefix7, flowPurchaseMonthBO, true);
        }
    }

    private void handlerFlowGoodsBatchDetail6(Long eid, List<String> flowTableNames, List<String> backupTableNames, String backupNamePrefix7, String monthBackup6) {
        // 第6个月是否存在
        Long count = flowGoodsBatchDetailMapper.getOneByEidAndGbDetailMonth(eid, FLOW_GOODS_BATCH_DETAIL, monthBackup6);
        if (ObjectUtil.isNotNull(count) && count.intValue() > 0) {
            FlowGoodsBatchDetailMonthBO flowGoodsBatchDetailMonthBO = new FlowGoodsBatchDetailMonthBO();
            flowGoodsBatchDetailMonthBO.setEid(eid);
            flowGoodsBatchDetailMonthBO.setGbDetailMonth(monthBackup6);
            handlerFlowGoodsBatchDetail7(eid, flowTableNames, backupTableNames, backupNamePrefix7, flowGoodsBatchDetailMonthBO, true);
        }
    }

    /**
     * 第7个月的销售备份
     * 处理未删除、已删除的所有
     *
     * @param eid 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupFlowSale7(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        List<FlowSaleMonthBO> gbDetailMonthList = flowSaleMapper.getMonthListByEidAndSoMonth(eid, monthBackup);
        if (CollUtil.isEmpty(gbDetailMonthList)) {
            return;
        }
        for (FlowSaleMonthBO flowSaleMonthBO : gbDetailMonthList) {
            handlerFlowSale7(eid, flowTableNames, backupTableNames, tempTableNamePrefix, flowSaleMonthBO, true);
        }
    }

    @GlobalTransactional
    private void handlerFlowSale7(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, FlowSaleMonthBO flowSaleMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String soMonth = flowSaleMonthBO.getSoMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(soMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        if (!backupTableNames.contains(tableName)) {
            // 创建备份表
            flowSaleMapper.createBackupTable(FLOW_SALE, tableName);
            flowSaleMapper.addColumn(tableName);
            backupTableNames.add(tableName);
        }

        /*if(!backupTableNames.contains(tableName)){
            // 创建备份表
            flowSaleMapper.createBackupTable(FLOW_SALE, tableName);
            flowSaleMapper.addColumn(tableName);
            backupTableNames.add(tableName);
            // 直接保存备份
            Integer insertCount = flowSaleMapper.insertBackupDataByEidAndSoMonth(eid, tableName, FLOW_SALE, soMonth);
            if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 创建备份表、插入数据失败, 企业id:" + eid +", 月份: "+ soMonth + ", 创建备份表名: " + tableName);
            }
        } else*/
        //            {
        // 此年份的备份表已存在
        // 该月份是否存在备份数据，包含已删除、未删除的所有
            /*Long backupCount = flowSaleMapper.getOneByEidAndSoMonth(eid, tableName, soMonth);
            if(ObjectUtil.isNull(backupCount) || backupCount.intValue() == 0){
                // 该月份备份不存在，直接保存备份
                Integer insertCount = flowSaleMapper.insertBackupDataByEidAndSoMonth(eid, tableName, FLOW_SALE, soMonth);
                if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                    throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 直接插入备份数据失败, 企业id:" + eid +", 月份: "+ soMonth + ", 已存在备份表名: " + tableName);
                }
            } else*/
        //                {
        // 该月份备份存在，则查询该月份的数据、备份数据
        //   查询此月份的线上明细数据，分页。逻辑删除的也查出来，已删除的id对备份中存在的进行删除
        List<FlowSaleDO> flowSaleList = new ArrayList<>();
        QueryFlowPurchaseListPageRequest saleRequest = new QueryFlowPurchaseListPageRequest();
        saleRequest.setEidList(ListUtil.toList(eid));
        saleRequest.setMonth(soMonth);
        saleRequest.setDeleteFlag(2);
        Page<FlowSaleDO> pageSale;
        int currentFlowSale = 1;
        do {
            saleRequest.setCurrent(currentFlowSale);
            saleRequest.setSize(2000);
            pageSale = flowSaleService.page(saleRequest);
            if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                break;
            }
            flowSaleList.addAll(pageSale.getRecords());

            if (pageSale.getRecords().size() < 2000) {
                break;
            }
            currentFlowSale++;
        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));

        if (CollUtil.isEmpty(flowSaleList)) {
            log.warn("[ERP流向销售备份], 处理第7个月及之前的数据, 此月份数据为空, 企业id:{}, 月份:{}", eid, soMonth);
            return;
        }

        // 查询此月份的备份明细数据,分页
        List<FlowSaleDO> flowSaleBackupList = new ArrayList<>();
        QueryFlowPurchaseBackupListPageRequest flowSaleBackupRequest = new QueryFlowPurchaseBackupListPageRequest();
        flowSaleBackupRequest.setEidList(ListUtil.toList(eid));
        flowSaleBackupRequest.setMonth(soMonth);
        flowSaleBackupRequest.setTableName(tableName);
        Page<FlowSaleDO> pageFlowSaleBackup;
        int currentFlowSaleBackup = 1;
        do {
            flowSaleBackupRequest.setCurrent(currentFlowSaleBackup);
            flowSaleBackupRequest.setSize(2000);
            pageFlowSaleBackup = flowSaleService.pageBackup(flowSaleBackupRequest);
            if (pageFlowSaleBackup == null || CollUtil.isEmpty(pageFlowSaleBackup.getRecords())) {
                break;
            }
            flowSaleBackupList.addAll(pageFlowSaleBackup.getRecords());

            if (pageFlowSaleBackup.getRecords().size() < 2000) {
                break;
            }
            currentFlowSaleBackup++;
        } while (pageFlowSaleBackup != null && CollUtil.isNotEmpty(pageFlowSaleBackup.getRecords()));

        // 线上、备份数据明细进行比较，匹配维度 采购主键id
        List<Long> deleteBackList = new ArrayList<>();
        List<Long> deleteOnLineFlagList = new ArrayList<>();
        List<Long> deleteOnLineBackedList = new ArrayList<>();
        List<FlowSaleDO> insertBackList = new ArrayList<>();
        // 已存在备份map, 分别对比主键id、数据主键soId，id或soId一样的即为相同数据
        Map<Long, FlowSaleDO> backupIdMap = new HashMap<>();
        Map<String, List<FlowSaleDO>> backupSoIdMap = new HashMap<>();
        Set<String> backupSoIdSet = new HashSet<>();
        if (CollUtil.isNotEmpty(flowSaleBackupList)) {
            backupIdMap = flowSaleBackupList.stream().collect(Collectors.toMap(FlowSaleDO::getId, Function.identity()));
            backupSoIdMap = flowSaleBackupList.stream().collect(Collectors.groupingBy(FlowSaleDO::getSoId));
        }

        Date backupTime = new Date();
        for (FlowSaleDO flowSaleDO : flowSaleList) {
            Long id = flowSaleDO.getId();
            String soId = flowSaleDO.getSoId();
            FlowSaleDO flowSaleBackupDO = backupIdMap.get(id);
            // 待删除备份
            if (ObjectUtil.isNotNull(flowSaleBackupDO)) {
                // 明细的主键id存在的，删除、重新插入备份
                deleteBackList.add(id);
            } else if (!backupSoIdSet.contains(soId)) {
                // soId存在的，删除、重新插入备份
                List<FlowSaleDO> flowSaleBackupDOS = backupSoIdMap.get(soId);
                if (CollUtil.isNotEmpty(flowSaleBackupDOS)) {
                    List<Long> backupIdList = flowSaleBackupDOS.stream().map(FlowSaleDO::getId).distinct().collect(Collectors.toList());
                    deleteBackList.addAll(backupIdList);
                    backupSoIdSet.add(soId);
                }
            }

            // 线上已逻辑删除，不需备份
            if (flowSaleDO.getDelFlag() == 1) {
                // 逻辑删除的，已经跟备份对比过，线上的可以进行删除
                deleteOnLineFlagList.add(id);
                continue;
            }
            // 待保存备份
            FlowSaleDO insertBackupDO = PojoUtils.map(flowSaleDO, FlowSaleDO.class);
            insertBackList.add(insertBackupDO);
            deleteOnLineBackedList.add(id);
        }

        // 删除已存在备份
        if (CollUtil.isNotEmpty(deleteBackList)) {
            List<List<Long>> deleteSubList = partitionList(deleteBackList.stream().distinct().collect(Collectors.toList()));
            for (List<Long> ids : deleteSubList) {
                int deleteCount = flowSaleMapper.deleteById(tableName, ids);
                if (deleteCount < ids.size()) {
                    throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 删除已存在备份失败，企业id: " + eid + ", 月份: " + soMonth + ", 备份表名: " + tableName);
                }
            }
        }

        // 保存备份
        if (CollUtil.isNotEmpty(insertBackList)) {
            // 进行批量保存
            List<List<FlowSaleDO>> insertSubList = partitionList(insertBackList);
            for (List<FlowSaleDO> backupDOs : insertSubList) {
                int count = flowSaleMapper.saveBatchBackup(tableName, backupDOs);
                if (count < backupDOs.size()) {
                    throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 删除后再保存到备份失败，企业id: " + eid + ", 月份: " + soMonth + ", 备份表名: " + tableName);
                }
            }
        }

        // 已对比过逻辑删除的，进行物理删除
        if (CollUtil.isNotEmpty(deleteOnLineFlagList)) {
            List<List<Long>> delFlagSubList = partitionList(deleteOnLineFlagList);
            for (List<Long> ids : delFlagSubList) {
                int deleteCount = flowSaleMapper.deleteById(FLOW_SALE, ids);
                if (deleteCount < ids.size()) {
                    throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 删除已逻辑删除的失败，企业id: " + eid + ", 月份: " + soMonth + ", 表名: " + FLOW_SALE);
                }
            }
        }
        //            }
        //        }
        // 删除已备份的线上数据
        if (deleteFlag && CollUtil.isNotEmpty(deleteOnLineBackedList)) {
            //            flowSaleMapper.deleteByEidAndSoMonth(eid, FlowBackupTableNamesEnum.FLOW_SALE.getCode(), soMonth);
            List<List<Long>> deleteOnLineSubList = partitionList(deleteOnLineBackedList);
            for (List<Long> ids : deleteOnLineSubList) {
                int deleteCount = flowSaleMapper.deleteById(FLOW_SALE, ids);
                if (deleteCount < ids.size()) {
                    throw new ServiceException("[ERP流向销售备份], 处理第7个月及之前的数据, 删除线上已备份的失败，企业id: " + eid + ", 月份: " + soMonth + ", 表名: " + FLOW_SALE);
                }
            }
        }
    }

    /**
     * 第7个月的采购备份
     * 处理未删除、已删除的所有
     *
     * @param eid 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupFlowPurchase7(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        List<FlowPurchaseMonthBO> gbDetailMonthList = flowPurchaseMapper.getMonthListByEidAndPoMonth(eid, monthBackup);
        if (CollUtil.isEmpty(gbDetailMonthList)) {
            return;
        }
        for (FlowPurchaseMonthBO flowPurchaseMonthBO : gbDetailMonthList) {
            handlerFlowPurchase7(eid, flowTableNames, backupTableNames, tempTableNamePrefix, flowPurchaseMonthBO, true);
        }
    }

    @GlobalTransactional
    private void handlerFlowPurchase7(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, FlowPurchaseMonthBO flowPurchaseMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String poMonth = flowPurchaseMonthBO.getPoMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(poMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        if (!backupTableNames.contains(tableName)) {
            // 创建备份表
            flowPurchaseMapper.createBackupTable(FLOW_PURCHASE, tableName);
            flowPurchaseMapper.addColumn(tableName);
            backupTableNames.add(tableName);
        }

        /*if(!backupTableNames.contains(tableName)){
            // 创建备份表
            flowPurchaseMapper.createBackupTable(FLOW_PURCHASE, tableName);
            flowPurchaseMapper.addColumn(tableName);
            backupTableNames.add(tableName);
            // 直接保存备份
            Integer insertCount = flowPurchaseMapper.insertBackupDataByEidAndPoMonth(eid, tableName, FLOW_PURCHASE, poMonth);
            if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                throw new ServiceException("[ERP流向采购备份], 处理第7个月及之前的数据, 创建备份表、插入数据失败, 企业id:" + eid +", 月份: "+ poMonth + ", 创建备份表名: " + tableName);
            }
        } else */
        //            {
        // 此年份的备份表已存在
        // 该月份是否存在备份数据
            /*Long backupCount = flowPurchaseMapper.getOneByEidAndPoMonth(eid, tableName, poMonth);
            if(ObjectUtil.isNull(backupCount) || backupCount.intValue() == 0){
                // 该月份备份不存在，直接保存备份
                Integer insertCount = flowPurchaseMapper.insertBackupDataByEidAndPoMonth(eid, tableName, FLOW_PURCHASE, poMonth);
                if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                    throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据, 直接插入备份数据失败, 企业id:" + eid +", 月份: "+ poMonth + ", 已存在备份表名: " + tableName);
                }
            } else */
        //                {
        // 该月份备份存在，则查询该月份的数据、备份数据
        //   查询此月份的线上明细数据，分页。逻辑删除的也查出来，已删除的id对备份中存在的进行删除
        List<FlowPurchaseDO> flowPurchaseList = new ArrayList<>();
        QueryFlowPurchaseListPageRequest purchaseRequest = new QueryFlowPurchaseListPageRequest();
        purchaseRequest.setEidList(ListUtil.toList(eid));
        purchaseRequest.setMonth(poMonth);
        purchaseRequest.setDeleteFlag(2);
        Page<FlowPurchaseDO> pagePurchase;
        int currentFlowPurchase = 1;
        do {
            purchaseRequest.setCurrent(currentFlowPurchase);
            purchaseRequest.setSize(2000);
            pagePurchase = flowPurchaseService.page(purchaseRequest);
            if (pagePurchase == null || CollUtil.isEmpty(pagePurchase.getRecords())) {
                break;
            }
            flowPurchaseList.addAll(pagePurchase.getRecords());

            if (pagePurchase.getRecords().size() < 2000) {
                break;
            }
            currentFlowPurchase++;
        } while (pagePurchase != null && CollUtil.isNotEmpty(pagePurchase.getRecords()));

        if (CollUtil.isEmpty(flowPurchaseList)) {
            log.warn("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据, 此月份数据为空, 企业id:{}, 月份:{}", eid, poMonth);
            return;
        }

        // 查询此月份的备份明细数据,分页
        List<FlowPurchaseDO> flowPurchaseBackupList = new ArrayList<>();
        QueryFlowPurchaseBackupListPageRequest flowPurchaseBackupRequest = new QueryFlowPurchaseBackupListPageRequest();
        flowPurchaseBackupRequest.setEidList(ListUtil.toList(eid));
        flowPurchaseBackupRequest.setMonth(poMonth);
        flowPurchaseBackupRequest.setTableName(tableName);
        Page<FlowPurchaseDO> pageFlowPurchaseBackup;
        int currentFlowPurchaseBackup = 1;
        do {
            flowPurchaseBackupRequest.setCurrent(currentFlowPurchaseBackup);
            flowPurchaseBackupRequest.setSize(2000);
            pageFlowPurchaseBackup = flowPurchaseService.pageBackup(flowPurchaseBackupRequest);
            if (pageFlowPurchaseBackup == null || CollUtil.isEmpty(pageFlowPurchaseBackup.getRecords())) {
                break;
            }
            flowPurchaseBackupList.addAll(pageFlowPurchaseBackup.getRecords());

            if (pageFlowPurchaseBackup.getRecords().size() < 2000) {
                break;
            }
            currentFlowPurchaseBackup++;
        } while (pageFlowPurchaseBackup != null && CollUtil.isNotEmpty(pageFlowPurchaseBackup.getRecords()));

        // 线上、备份数据明细进行比较，匹配维度 采购主键id
        List<Long> deleteBackList = new ArrayList<>();
        List<Long> deleteOnLineFlagList = new ArrayList<>();
        List<Long> deleteOnLineBackedList = new ArrayList<>();
        List<FlowPurchaseDO> insertBackList = new ArrayList<>();
        // 已存在备份map, 分别对比主键id、数据主键poId，id或poId一样的即为相同数据
        Map<Long, FlowPurchaseDO> backupIdMap = new HashMap<>();
        Map<String, List<FlowPurchaseDO>> backupPoIdMap = new HashMap<>();
        Set<String> backupPoIdSet = new HashSet<>();
        if (CollUtil.isNotEmpty(flowPurchaseBackupList)) {
            backupIdMap = flowPurchaseBackupList.stream().collect(Collectors.toMap(FlowPurchaseDO::getId, Function.identity()));
            backupPoIdMap = flowPurchaseBackupList.stream().collect(Collectors.groupingBy(FlowPurchaseDO::getPoId));
        }

        Date backupTime = new Date();
        for (FlowPurchaseDO flowPurchaseDO : flowPurchaseList) {
            Long id = flowPurchaseDO.getId();
            String poId = flowPurchaseDO.getPoId();
            FlowPurchaseDO flowPurchaseBackupDO = backupIdMap.get(id);
            // 待删除备份
            if (ObjectUtil.isNotNull(flowPurchaseBackupDO)) {
                // 明细的主键id存在的，删除、重新插入备份
                deleteBackList.add(id);
            } else if (!backupPoIdSet.contains(poId)) {
                // poId存在的，删除、重新插入备份
                List<FlowPurchaseDO> flowPurchaseBackupDOS = backupPoIdMap.get(poId);
                if (CollUtil.isNotEmpty(flowPurchaseBackupDOS)) {
                    List<Long> backupIdList = flowPurchaseBackupDOS.stream().map(FlowPurchaseDO::getId).distinct().collect(Collectors.toList());
                    deleteBackList.addAll(backupIdList);
                    backupPoIdSet.add(poId);
                }
            }

            // 线上已逻辑删除，不需备份
            if (flowPurchaseDO.getDelFlag() == 1) {
                // 逻辑删除的，已经跟备份对比过，线上的可以进行删除
                deleteOnLineFlagList.add(id);
                continue;
            }
            // 待保存备份
            FlowPurchaseDO insertBackupDO = PojoUtils.map(flowPurchaseDO, FlowPurchaseDO.class);
            insertBackList.add(insertBackupDO);
            deleteOnLineBackedList.add(id);
        }

        // 删除已存在备份
        if (CollUtil.isNotEmpty(deleteBackList)) {
            List<List<Long>> deleteSubList = partitionList(deleteBackList.stream().distinct().collect(Collectors.toList()));
            for (List<Long> ids : deleteSubList) {
                int deleteCount = flowPurchaseMapper.deleteById(tableName, ids);
                if (deleteCount < ids.size()) {
                    throw new ServiceException("[ERP流向采购备份], 处理第7个月及之前的数据, 删除已存在备份失败，企业id: " + eid + ", 月份: " + poMonth + ", 备份表名: " + tableName);
                }
            }
        }

        // 保存备份
        if (CollUtil.isNotEmpty(insertBackList)) {
            // 进行批量保存
            List<List<FlowPurchaseDO>> insertSubList = partitionList(insertBackList);
            for (List<FlowPurchaseDO> backupDOs : insertSubList) {
                int count = flowPurchaseMapper.saveBatchBackup(tableName, backupDOs);
                if (count < backupDOs.size()) {
                    throw new ServiceException("[ERP流向采购备份], 处理第7个月及之前的数据, 删除后再保存到备份失败，企业id: " + eid + ", 月份: " + poMonth + ", 备份表名: " + tableName);
                }
            }
        }

        // 已对比过逻辑删除的，进行物理删除
        if (CollUtil.isNotEmpty(deleteOnLineFlagList)) {
            List<List<Long>> delFlagSubList = partitionList(deleteOnLineFlagList);
            for (List<Long> ids : delFlagSubList) {
                int deleteCount = flowPurchaseMapper.deleteById(FLOW_PURCHASE, ids);
                if (deleteCount < ids.size()) {
                    throw new ServiceException("[ERP流向采购备份], 处理第7个月及之前的数据, 删除已逻辑删除的失败，企业id: " + eid + ", 月份: " + poMonth + ", 表名: " + FLOW_PURCHASE);
                }
            }
        }
        //            }
        //        }
        // 删除此月份的线上数据
        if (deleteFlag && CollUtil.isNotEmpty(deleteOnLineBackedList)) {
            //            flowPurchaseMapper.deleteByEidAndPoMonth(eid, FlowBackupTableNamesEnum.FLOW_PURCHASE.getCode(), poMonth);
            List<List<Long>> deleteOnLineSubList = partitionList(deleteOnLineBackedList);
            for (List<Long> ids : deleteOnLineSubList) {
                int deleteCount = flowPurchaseMapper.deleteById(FLOW_PURCHASE, ids);
                if (deleteCount < ids.size()) {
                    throw new ServiceException("[ERP流向采购备份], 处理第7个月及之前的数据, 删除线上已备份的失败，企业id: " + eid + ", 月份: " + poMonth + ", 表名: " + FLOW_PURCHASE);
                }
            }
        }
    }

    /**
     * 第7个月的库存备份
     *
     * @param eid 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupFlowGoodsBatchDetail7(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        List<FlowGoodsBatchDetailMonthBO> gbDetailMonthList = flowGoodsBatchDetailMapper.getMonthListByEidAndGbDetailMonth(eid, monthBackup);
        if (CollUtil.isEmpty(gbDetailMonthList)) {
            return;
        }
        for (FlowGoodsBatchDetailMonthBO flowGoodsBatchDetailMonthBO : gbDetailMonthList) {
            handlerFlowGoodsBatchDetail7(eid, flowTableNames, backupTableNames, tempTableNamePrefix, flowGoodsBatchDetailMonthBO, true);
        }
    }

    @GlobalTransactional
    private void handlerFlowGoodsBatchDetail7(Long eid, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, FlowGoodsBatchDetailMonthBO flowGoodsBatchDetailMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String gbDetailMonth = flowGoodsBatchDetailMonthBO.getGbDetailMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(gbDetailMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        if (!backupTableNames.contains(tableName)) {
            // 创建备份表
            flowGoodsBatchDetailMapper.createBackupTable(FLOW_GOODS_BATCH_DETAIL, tableName);
            flowGoodsBatchDetailMapper.addColumn(tableName);
            backupTableNames.add(tableName);
        }

        /*if(!backupTableNames.contains(tableName)){
            // 创建备份表
            flowGoodsBatchDetailMapper.createBackupTable(FLOW_GOODS_BATCH_DETAIL, tableName);
            flowGoodsBatchDetailMapper.addColumn(tableName);
            backupTableNames.add(tableName);
            // 直接保存备份
            Integer insertCount = flowGoodsBatchDetailMapper.insertBackupDataByEidAndGbDetailMonth(eid, tableName, FLOW_GOODS_BATCH_DETAIL, gbDetailMonth);
            if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据, 创建备份表、插入数据失败, 企业id:" + eid +"月份: "+ gbDetailMonth + ", 创建备份表名: " + tableName);
            }
        } else */
        //            {
        // 此年份的备份表已存在
        // 该月份是否存在备份数据
            /*Long backupCount = flowGoodsBatchDetailMapper.getOneByEidAndGbDetailMonth(eid, tableName, gbDetailMonth);
            if(ObjectUtil.isNull(backupCount) || backupCount.intValue() == 0){
                // 该月份备份不存在，直接保存备份
                Integer insertCount = flowGoodsBatchDetailMapper.insertBackupDataByEidAndGbDetailMonth(eid, tableName, FLOW_GOODS_BATCH_DETAIL, gbDetailMonth);
                if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                    throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据, 直接插入备份数据失败, 企业id:" + eid +", 月份: "+ gbDetailMonth + ", 已存在备份表名: " + tableName);
                }
            } else */
        //                {
        // 该月份备份存在，则查询该月份的数据、备份数据
        // 查询此月份的线上明细数据,分页
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailList = new ArrayList<>();
        QueryFlowGoodsBatchDetailListPageRequest goodsBatchDetailRequest = new QueryFlowGoodsBatchDetailListPageRequest();
        goodsBatchDetailRequest.setEidList(ListUtil.toList(eid));
        goodsBatchDetailRequest.setGbDetailMonth(gbDetailMonth);
        Page<FlowGoodsBatchDetailDO> pageGoodsBatch;
        int currentGoodsBatchDetail = 1;
        do {
            goodsBatchDetailRequest.setCurrent(currentGoodsBatchDetail);
            goodsBatchDetailRequest.setSize(2000);
            pageGoodsBatch = flowGoodsBatchDetailService.page(goodsBatchDetailRequest);
            if (pageGoodsBatch == null || CollUtil.isEmpty(pageGoodsBatch.getRecords())) {
                break;
            }
            flowGoodsBatchDetailList.addAll(pageGoodsBatch.getRecords());

            if (pageGoodsBatch.getRecords().size() < 2000) {
                break;
            }
            currentGoodsBatchDetail++;
        } while (pageGoodsBatch != null && CollUtil.isNotEmpty(pageGoodsBatch.getRecords()));

        // 查询此月份的备份明细数据,分页
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailBackupList = new ArrayList<>();
        QueryFlowGoodsBatchDetailBackupListPageRequest goodsBatchDetailBackupRequest = new QueryFlowGoodsBatchDetailBackupListPageRequest();
        goodsBatchDetailBackupRequest.setEidList(ListUtil.toList(eid));
        goodsBatchDetailBackupRequest.setGbDetailMonth(gbDetailMonth);
        goodsBatchDetailBackupRequest.setTableName(tableName);
        Page<FlowGoodsBatchDetailDO> pageGoodsBatchBackup;
        int currentGoodsBatchDetailBackup = 1;
        do {
            goodsBatchDetailBackupRequest.setCurrent(currentGoodsBatchDetailBackup);
            goodsBatchDetailBackupRequest.setSize(2000);
            pageGoodsBatchBackup = flowGoodsBatchDetailService.pageBackup(goodsBatchDetailBackupRequest);
            if (pageGoodsBatchBackup == null || CollUtil.isEmpty(pageGoodsBatchBackup.getRecords())) {
                break;
            }
            flowGoodsBatchDetailBackupList.addAll(pageGoodsBatchBackup.getRecords());

            if (pageGoodsBatchBackup.getRecords().size() < 2000) {
                break;
            }
            currentGoodsBatchDetailBackup++;
        } while (pageGoodsBatchBackup != null && CollUtil.isNotEmpty(pageGoodsBatchBackup.getRecords()));

        // 线上、备份数据明细进行比较，匹配维度 flow_goods_batch_detail 明细的主键id
        List<Long> deleteList = new ArrayList<>();
        List<FlowGoodsBatchDetailDO> insertList = new ArrayList<>();
        // 已存在备份map
        Map<Long, FlowGoodsBatchDetailDO> backupMap = flowGoodsBatchDetailBackupList.stream().collect(Collectors.toMap(FlowGoodsBatchDetailDO::getId, Function.identity()));
        Date backupTime = new Date();
        for (FlowGoodsBatchDetailDO flowGoodsBatchDetailDO : flowGoodsBatchDetailList) {
            Long id = flowGoodsBatchDetailDO.getId();
            FlowGoodsBatchDetailDO FlowGoodsBatchDetailDO = backupMap.get(id);
            if (ObjectUtil.isNotNull(FlowGoodsBatchDetailDO)) {
                // 明细的主键id存在的，删除、重新插入备份
                deleteList.add(id);
            }
            // 插入备份
            FlowGoodsBatchDetailDO insertBackupDO = PojoUtils.map(flowGoodsBatchDetailDO, FlowGoodsBatchDetailDO.class);
            insertList.add(insertBackupDO);
        }

        // 删除已存在备份
        if (CollUtil.isNotEmpty(deleteList)) {
            List<List<Long>> deleteSubList = partitionList(deleteList);
            for (List<Long> ids : deleteSubList) {
                int count = flowGoodsBatchDetailMapper.deleteById(tableName, ids);
                if (count < ids.size()) {
                    throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据失败, 删除已存在备份失败，企业id: " + eid + ", 月份: " + gbDetailMonth + ", 备份表名: " + tableName);
                }
            }
        }

        // 保存备份
        if (CollUtil.isNotEmpty(insertList)) {
            List<List<FlowGoodsBatchDetailDO>> insertSubList = partitionList(insertList);
            for (List<FlowGoodsBatchDetailDO> backupDOs : insertSubList) {
                int count = flowGoodsBatchDetailMapper.saveBatchBackup(tableName, backupDOs);
                if (count < backupDOs.size()) {
                    throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据失败, 保存到备份失败，企业id: " + eid + ", 月份: " + gbDetailMonth + ", 备份表名: " + tableName);
                }
            }
        }
        //            }
        //        }
        // 删除已备份的线上数据
        if (deleteFlag && CollUtil.isNotEmpty(insertList)) {
            //            flowGoodsBatchDetailMapper.deleteByEidAndGbDetailMonth(eid, FlowBackupTableNamesEnum.FLOW_GOODS_BATCH_DETAIL.getCode(), gbDetailMonth);
            List<Long> deleteIdOnLineList = insertList.stream().map(FlowGoodsBatchDetailDO::getId).distinct().collect(Collectors.toList());
            List<List<Long>> deleteSubOnLineList = partitionList(deleteIdOnLineList);
            for (List<Long> ids : deleteSubOnLineList) {
                int count = flowGoodsBatchDetailMapper.deleteById(FLOW_GOODS_BATCH_DETAIL, ids);
                if (count < ids.size()) {
                    throw new ServiceException("[ERP流向库存汇总明细备份], 处理第7个月及之前的数据失败, 删除线上已备份的失败，企业id: " + eid + ", 月份: " + gbDetailMonth + ", 备份表名: " + FLOW_GOODS_BATCH_DETAIL);
                }
            }
        }
    }

    private <T> List<List<T>> partitionList(List<T> list) {
        List<List<T>> subList = new ArrayList<>();
        if (list.size() > 200) {
            subList = Lists.partition(list, 200);
        } else {
            subList.add(list);
        }
        return subList;
    }


    /**
     * 第6个月的销售备份
     *
     * @param backupTableNames 已存在备份表名称
     * @param eid 企业id
     * @param tempTableName 待备份的表名称
     * @param monthBackup 备份月份
     */
    private void backupFlowSale6(List<String> backupTableNames, Long eid, String tempTableName, String monthBackup) {
        if (!backupTableNames.contains(tempTableName)) {
            // 创建备份表
            flowSaleMapper.createBackupTable(FLOW_SALE, tempTableName);
            flowSaleMapper.addColumn(tempTableName);
            backupTableNames.add(tempTableName);
        }
        // 备份第6个月的销售
        insertFlowSaleBackup6(eid, tempTableName, monthBackup);
    }

    @GlobalTransactional
    private void insertFlowSaleBackup6(Long eid, String tempTableName, String monthBackup) {
        // 第6个月的销售备份，存在则进行删除
        Long backupCount = flowSaleMapper.getOneByEidAndSoMonth(eid, tempTableName, monthBackup);
        if (ObjectUtil.isNotNull(backupCount) && backupCount.intValue() > 0) {
            // 删除这个月的备份
            Integer deleteBackCount = flowSaleMapper.deleteByEidAndSoMonth(eid, tempTableName, monthBackup);
            if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
                throw new ServiceException("[ERP流向销售备份], 删除当前月份的备份失败, 月份: " + monthBackup + ", 备份表名: " + tempTableName);
            }
        }
        // 第6个月的保存到备份表
        Long count = flowSaleMapper.getOneByEidAndSoMonth(eid, FLOW_SALE, monthBackup);
        if (ObjectUtil.isNotNull(count) && count.intValue() != 0) {
            Integer insertCount = flowSaleMapper.insertBackupDataByEidAndSoMonth(eid, tempTableName, FLOW_SALE, monthBackup);
            if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                throw new ServiceException("[ERP流向销售备份], 保存当前月份的数据到备份表失败, 月份: " + monthBackup + ", 备份表名: " + tempTableName);
            }
        }
    }

    /**
     * 第6个月的采购备份
     *
     * @param backupTableNames 已存在备份表名称
     * @param eid 企业id
     * @param tempTableName 待备份的表名称
     * @param monthBackup 备份月份
     */
    private void backupFlowPurchase6(List<String> backupTableNames, Long eid, String tempTableName, String monthBackup) {
        if (!backupTableNames.contains(tempTableName)) {
            // 创建备份表
            flowPurchaseMapper.createBackupTable(FLOW_PURCHASE, tempTableName);
            flowPurchaseMapper.addColumn(tempTableName);
            backupTableNames.add(tempTableName);
        }
        // 备份第6个月的采购
        insertFlowPurchaseBackup6(eid, tempTableName, monthBackup);
    }

    @GlobalTransactional
    private void insertFlowPurchaseBackup6(Long eid, String tempTableName, String monthBackup) {
        // 第6个月的采购备份，存在则进行删除
        Long backupCount = flowPurchaseMapper.getOneByEidAndPoMonth(eid, tempTableName, monthBackup);
        if (ObjectUtil.isNotNull(backupCount) && backupCount.intValue() > 0) {
            // 删除这个月的备份
            Integer deleteBackCount = flowPurchaseMapper.deleteByEidAndPoMonth(eid, tempTableName, monthBackup);
            if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
                throw new ServiceException("[ERP流向采购备份], 删除当前月份的备份失败, 月份: " + monthBackup + ", 备份表名: " + tempTableName);
            }
        }
        // 第6个月的保存到备份表
        Long count = flowPurchaseMapper.getOneByEidAndPoMonth(eid, FLOW_PURCHASE, monthBackup);
        if (ObjectUtil.isNotNull(count) && count.intValue() != 0) {
            Integer insertCount = flowPurchaseMapper.insertBackupDataByEidAndPoMonth(eid, tempTableName, FLOW_PURCHASE, monthBackup);
            if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                throw new ServiceException("[ERP流向采购备份], 保存当前月份的数据到备份表失败, 月份: " + monthBackup + ", 备份表名: " + tempTableName);
            }
        }
    }

    /**
     * 第6个月的库存备份
     *
     * @param backupTableNames 已存在备份表名称
     * @param eid 企业id
     * @param tempTableName 待备份的表名称
     * @param monthBackup 备份月份
     */

    private void backupFlowGoodsBatchDetail6(List<String> backupTableNames, Long eid, String tempTableName, String monthBackup, Date taskTime) {
        if (!backupTableNames.contains(tempTableName)) {
            // 创建备份表
            flowGoodsBatchDetailMapper.createBackupTable(FLOW_GOODS_BATCH_DETAIL, tempTableName);
            flowGoodsBatchDetailMapper.addColumn(tempTableName);
            backupTableNames.add(tempTableName);
        }
        // 备份第6个月的库存汇总明细
        insertFlowGoodsBatchDetailBackup6(eid, tempTableName, monthBackup, taskTime);
    }

    @GlobalTransactional
    private void insertFlowGoodsBatchDetailBackup6(Long eid, String tempTableName, String monthBackup, Date taskTime) {
        // 1号进行全量备份，其他时间进行明细比较
        String taskDay = DateUtil.format(taskTime, "yyyy-MM-dd");
        String beginOfMonth = DateUtil.format(DateUtil.beginOfMonth(taskTime), "yyyy-MM-dd");
        if (ObjectUtil.equal(taskDay, beginOfMonth)) {

        }
        // 第6个月的库存备份，存在则进行删除
        Long backupCount = flowGoodsBatchDetailMapper.getOneByEidAndGbDetailMonth(eid, tempTableName, monthBackup);
        if (ObjectUtil.isNotNull(backupCount) && backupCount.intValue() > 0) {
            // 删除这个月的备份
            Integer deleteBackCount = flowGoodsBatchDetailMapper.deleteByEidAndGbDetailMonth(eid, tempTableName, monthBackup);
            if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
                throw new ServiceException("[ERP流向库存汇总明细备份], 删除当前库存月份的备份失败, 月份: " + monthBackup + ", 备份表名: " + tempTableName);
            }
        }
        // 第6个月的保存到备份表
        Long count = flowGoodsBatchDetailMapper.getOneByEidAndGbDetailMonth(eid, FLOW_GOODS_BATCH_DETAIL, monthBackup);
        if (ObjectUtil.isNotNull(count) && count.intValue() != 0) {
            Integer insertCount = flowGoodsBatchDetailMapper.insertBackupDataByEidAndGbDetailMonth(eid, tempTableName, FLOW_GOODS_BATCH_DETAIL, monthBackup);
            if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
                throw new ServiceException("[ERP流向库存汇总明细备份], 保存当前库存月份的数据到备份表失败, 月份: " + monthBackup + ", 备份表名: " + tempTableName);
            }
        }
    }

    /**
     * 获取已存在备份表的名称列表
     *
     * @param flowTableNames 流向表名称列表
     * @param backupTableNames 备份表列表
     * @param names 待备份表名称
     */
    private void getBackupTableNames(List<String> flowTableNames, List<String> backupTableNames, String... names) {
        if (ArrayUtil.isEmpty(names)) {
            return;
        }
        for (String name : names) {
            if (flowTableNames.contains(name) && !backupTableNames.contains(name)) {
                backupTableNames.add(name);
            }
        }
    }


    private void backupFlowPurchase(boolean flowPurchaseTableFlag, String flowPurchaseNewTableName, String startTimeBackup, String endTimeBackup, String endTimeDelete) {
        Integer insertCount = 0;
        if (!flowPurchaseTableFlag) {
            // 创建采购流向备份表
            flowPurchaseMapper.createBackupTable(FLOW_PURCHASE, flowPurchaseNewTableName);
            insertCount = flowPurchaseMapper.initBackupData(FLOW_PURCHASE, flowPurchaseNewTableName, endTimeBackup);
        } else {
            insertCount = flowPurchaseMapper.insertBackupDataByPoTime(FLOW_PURCHASE, flowPurchaseNewTableName, startTimeBackup, endTimeBackup);
        }
        if (ObjectUtil.isNotNull(insertCount) && insertCount.intValue() != 0) {
            // 采购流向物理删除当前表中已备份的数据
            flowPurchaseMapper.deleteByPoTime(startTimeBackup, endTimeBackup);
            // 物理删除第9个月及之前的所有数据
            flowPurchaseMapper.deleteByPoTimeEnd(endTimeDelete);
        }
    }

    private void backupErpSaleFlow(boolean flowSaleTableFlag, String flowSaleNewTableName, String startTimeBackup, String endTimeBackup, String endTimeDelete) {
        Integer insertCount = 0;
        if (!flowSaleTableFlag) {
            // 创建销售流向备份表
            flowSaleMapper.createBackupTable(FLOW_SALE, flowSaleNewTableName);
            insertCount = flowSaleMapper.initBackupData(FLOW_SALE, flowSaleNewTableName, endTimeBackup);
        } else {
            insertCount = flowSaleMapper.insertBackupDataBySoTime(FLOW_SALE, flowSaleNewTableName, startTimeBackup, endTimeBackup);
        }
        if (ObjectUtil.isNotNull(insertCount) && insertCount.intValue() != 0) {
            // 销售流向物理删除当前表中已备份的数据
            flowSaleMapper.deleteBySoTime(startTimeBackup, endTimeBackup);
            // 物理删除第9个月及之前的所有数据
            flowSaleMapper.deleteBySoTimeEnd(endTimeDelete);
        }
    }

    private void backupFlowGoodsBatchDetail(boolean flowGoodsBatchDetailTableFlag, String flowGoodsBatchDetailNewTableName, String startTimeBackup, String endTimeBackup, String endTimeDelete) {
        Integer insertCount = 0;
        if (!flowGoodsBatchDetailTableFlag) {
            // 创建销售流向备份表
            flowGoodsBatchDetailMapper.createBackupTable(FLOW_GOODS_BATCH_DETAIL, flowGoodsBatchDetailNewTableName);
            insertCount = flowGoodsBatchDetailMapper.initBackupData(FLOW_GOODS_BATCH_DETAIL, flowGoodsBatchDetailNewTableName, endTimeBackup);
        } else {
            insertCount = flowGoodsBatchDetailMapper.insertBackupDataBySoTime(FLOW_GOODS_BATCH_DETAIL, flowGoodsBatchDetailNewTableName, startTimeBackup, endTimeBackup);
        }
        if (ObjectUtil.isNotNull(insertCount) && insertCount.intValue() != 0) {
            // ERP库存汇总物理删除当前表中已备份的数据
            flowGoodsBatchDetailMapper.deleteByGbDetailTime(startTimeBackup, endTimeBackup);
            // 物理删除第9个月及之前的所有数据
            flowGoodsBatchDetailMapper.deleteByGbDetailTimeEnd(endTimeDelete);
        }
    }

    //    public static void main(String[] args) {
    //                int thisYear = DateUtil.thisYear();
    //                System.out.println(">>>>>> thisYear:" + thisYear);
    //
    //                String monthBackup = BackupUtil.monthBackup(7);
    //                System.out.println(">>>>>> monthBackup:" + monthBackup);
    //
    //                DateTime monthBackupDate = DateUtil.parse(monthBackup, BackupUtil.FORMATE_MONTH);
    //                DateTime startTime = DateUtil.beginOfMonth(monthBackupDate);
    //                DateTime endTime = DateUtil.endOfMonth(monthBackupDate);
    //                System.out.println(">>>>>> startTime:" + startTime);
    //                System.out.println(">>>>>> endTime:" + endTime);
    //    }

}
