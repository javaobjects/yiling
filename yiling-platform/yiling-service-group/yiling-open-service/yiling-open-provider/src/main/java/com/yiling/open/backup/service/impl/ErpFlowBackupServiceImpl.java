package com.yiling.open.backup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.api.FlowBiTaskApi;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.open.backup.service.ErpFlowBackupService;
import com.yiling.open.backup.util.BackupUtil;
import com.yiling.open.backup.util.DataBaseConnection;
import com.yiling.open.config.DatasourceConfig;
import com.yiling.open.erp.bo.ErpPurchaseFlowMonthBO;
import com.yiling.open.erp.bo.ErpSaleFlowMonthBO;
import com.yiling.open.erp.dao.ErpFlowControlMapper;
import com.yiling.open.erp.dao.ErpGoodsBatchFlowMapper;
import com.yiling.open.erp.dao.ErpPurchaseFlowMapper;
import com.yiling.open.erp.dao.ErpSaleFlowMapper;
import com.yiling.open.erp.dao.SysHeartBeatMapper;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpPurchaseFlowService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * op库ERP流向数据备份清理服务类
 *
 * @author: houjie.sun
 * @date: 2022/7/12
 */
@Slf4j
@Service
public class ErpFlowBackupServiceImpl implements ErpFlowBackupService {

    @DubboReference
    private FlowBiTaskApi flowBiTaskApi;

    @Autowired
    private DatasourceConfig datasourceConfig;
    @Autowired
    private ErpPurchaseFlowService erpPurchaseFlowService;
    @Autowired
    private ErpClientService erpClientService;
    @Autowired
    private ErpPurchaseFlowMapper erpPurchaseFlowMapper;
    @Autowired
    private ErpSaleFlowMapper erpSaleFlowMapper;
    @Autowired
    private ErpGoodsBatchFlowMapper erpGoodsBatchFlowMapper;
    @Autowired
    private SysHeartBeatMapper sysHeartBeatMapper;
    @Autowired
    private ErpFlowControlMapper erpFlowControlMapper;

    @Value("${env.name}")
    private String envName;
    @Value("${aliyun.oss.endpoint:xxxxx}")
    private String endpoint;
    @Value("${aliyun.oss.access-key:xxxxx}")
    private String accessKey;
    @Value("${aliyun.oss.accessKeySecret:xxxxx}")
    private String accessKeySecret;

    public static final String ERP_PURCHASE_FLOW = "erp_purchase_flow";
    public static final String ERP_SALE_FLOW = "erp_sale_flow";
    public static final String ERP_GOODS_BATCH_FLOW = "erp_goods_batch_flow";
    public static final String SYS_HEART_BEAT = "sys_heart_beat";
    public static final String SU_DEPT_NO_NULL = "suDeptNo_null";

    @Override
    public void cleanErpFlowOssFile() {
        /* 清理时间 */
        //   清理第5个自然月的数据，做物理删除
        //   （不包括当前月份，向前推3个自然月，即是4个自然月之前的第5个月）
        // 保留文件的年份、月份列表
        Integer count = 3;
        List<String> yearList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        getRetaimYearAndMonthList(count, yearList, monthList);

        // 采购目录清理
        String purchaseDirectoryPrefix = envName.concat("/").concat(FileTypeEnum.ERP_PURCHASE_FLOW.getType()).concat("/");
        log.info("清理ERP流向OSS文件, 开始清理采购目录, purchaseDirectoryPrefix:{}", purchaseDirectoryPrefix);
        cleanRkSuIdDirectoryAndFile(purchaseDirectoryPrefix, yearList, monthList);
        // 销售目录清理
        String saleDirectoryPrefix = envName.concat("/").concat(FileTypeEnum.ERP_SALE_FLOW.getType()).concat("/");
        log.info("清理ERP流向OSS文件, 开始清理销售目录, saleDirectoryPrefix:{}", saleDirectoryPrefix);
        cleanRkSuIdDirectoryAndFile(saleDirectoryPrefix, yearList, monthList);

        log.info("ERP流向OSS文件清理任务执行完成");
    }


    private List<ErpClientDO> getErpClientList() {
        List<ErpClientDO> erpClientList = new ArrayList<>();
        int size = 1000;
        int current = 1;
        Page<ErpClientDO> erpClientPage;
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setSize(size);
        do {
            request.setCurrent(current);
            erpClientPage = erpClientService.page(request);
            if (ObjectUtil.isNull(erpClientPage) || CollUtil.isEmpty(erpClientPage.getRecords())) {
                break;
            }
            erpClientList.addAll(erpClientPage.getRecords());
            if (erpClientPage.getRecords().size() < size) {
                break;
            }
            current++;
        } while (erpClientPage != null && CollUtil.isNotEmpty(erpClientPage.getRecords()));
        return erpClientList;
    }

    private void getRetaimYearAndMonthList(Integer count, List<String> yearList, List<String> monthList) {
        Date endDate = new Date();
        Date startDate = DateUtil.offset(endDate, DateField.MONTH, count * -1);
        List<DateTime> dateTimeList = DateUtil.rangeToList(startDate, endDate, DateField.MONTH);
        for (DateTime dateTime : dateTimeList) {
            String dateStr = DateUtil.format(dateTime.toJdkDate(), "yyyy-MM");
            String[] split = dateStr.split("-");
            String year = split[0];
            String month = split[1];
            if (!yearList.contains(year)) {
                yearList.add(year);
            }
            if (!monthList.contains(month)) {
                monthList.add(month);
            }
        }
    }

    private void cleanRkSuIdDirectoryAndFile(String parentPrefix, List<String> yearList, List<String> monthList) {
        // 列举当前目录前缀的所有子目录、子文件（仅子一级）  dev/erpPurchaseFlow/、dev/erpSaleFlow/
        List<String> purchaseParentDirectoryList = BackupUtil.getOssOneLevelDirectoryAndFileByKey(endpoint, accessKey, accessKeySecret, parentPrefix);
        if (CollUtil.isEmpty(purchaseParentDirectoryList)) {
            return;
        }
        // 循环子一级列表，即是全部商业id目录
        for (String directoryAndFileRkSuId : purchaseParentDirectoryList) {
            // 子目录，不是目录的删除（文件）
            if (!directoryAndFileRkSuId.endsWith("/")) {
                List<String> fileKeyList = new ArrayList<>();
                fileKeyList.add(directoryAndFileRkSuId);
                BackupUtil.deleteOssFileByFileKeyList(endpoint, accessKey, accessKeySecret, fileKeyList);
                continue;
            }
            // 子目录应该是企业id，不是数字的需要删除
            String rkSuIdDirectoryName = BackupUtil.getOssDirectoryName(directoryAndFileRkSuId);
            if (!BackupUtil.isNumber(rkSuIdDirectoryName)) {
                // 删除此企业id目录
                deleteDirectoryByPrefixs(directoryAndFileRkSuId);
                continue;
            }
            // 空目录需要删除，列举一个子文件，没有子文件则为空目录
            Boolean oneFileFlag = BackupUtil.existOneFileByParentDirectory(endpoint, accessKey, accessKeySecret, directoryAndFileRkSuId);
            if (!oneFileFlag) {
                List<String> fileKeyList = new ArrayList<>();
                fileKeyList.add(directoryAndFileRkSuId);
                BackupUtil.deleteOssFileByFileKeyList(endpoint, accessKey, accessKeySecret, fileKeyList);
                continue;
            }

            // 列举此企业id对应的日期目录下的所有子文件
            // dev/erpPurchaseFlow/1227/ZDA/2022/06/01/4522677fa56648e29b5a55183ed86b95.zip
            List<String> allFileList = BackupUtil.getAllFileByParentDirectory(endpoint, accessKey, accessKeySecret, directoryAndFileRkSuId);
            if (CollUtil.isEmpty(allFileList)) {
                deleteDirectoryByPrefixs(directoryAndFileRkSuId);
            }

            /* 从后向前切割目录，取年月日位置的目录拼接成 年-月-日 时间格式进行校验，不符合的进行删除文件 */
            // 待删除文件
            Set<String> deleteFileList = new HashSet<>();
            // 已清理的年份
            Set<String> yearHasCleanSet = new HashSet<>();
            // 已清理的月份
            Set<String> monthHasCleanSet = new HashSet<>();

            for (String fileKey : allFileList) {
                // 2022/06/01/4522677fa56648e29b5a55183ed86b95.zip
                String[] directoryArray = fileKey.split("/");
                if (directoryArray.length < 4) {
                    deleteFileList.add(fileKey);
                    continue;
                }
                int length = directoryArray.length;
                String fileDate = directoryArray[length - 4] + "-" + directoryArray[length - 3] + "-" + directoryArray[length - 2];
                // 时间格式校验
                if (!BackupUtil.checkDate(fileDate)) {
                    deleteFileList.add(fileKey);
                    continue;
                }

                // 月份
                String monthDirectory = BackupUtil.getPrefixBySymbol(fileKey, "/");
                String monthOss = directoryArray[length - 3];
                // 年份
                String yearDirectory = BackupUtil.getPrefixBySymbol2(monthDirectory, "/");
                String yearOss = directoryArray[length - 4];

                // 年份，需要清理：不在需保留年份列表中、且yearHasCleanSet中不存在。
                // 删除此年份下的所有子文件、子目录、并删除此年份目录，记录已清理的年份yearHasCleanSet
                if (!yearList.contains(yearOss) && !yearHasCleanSet.contains(yearOss)) {
                    // 删除此年份目录
                    deleteDirectoryByPrefixs(yearDirectory.concat("/"));
                    yearHasCleanSet.add(yearDirectory);
                    continue;
                }

                // 未清理年份，月份，需要清理：不在需保留月份列表中、且monthHasCleanSet中不存在。
                // 删除此月份下的所有子文件、子目录、并删除此月份目录，记录已清理的月份monthHasCleanSet
                if (!monthList.contains(monthOss) && !monthHasCleanSet.contains(monthOss)) {
                    // 删除此月份目录
                    deleteDirectoryByPrefixs(monthDirectory.concat("/"));
                    monthHasCleanSet.add(monthDirectory);
                    continue;
                }

                /*
                // 清理其他目录，除了日期目录，其他目录中的文件都是多余需删除，dev/erpPurchaseFlow/1227/ZDA/2022/06
                // 删除月份目录多余的子文件
                deleteFileByPrefixs(monthDirectory.concat("/"));
                // 删除年份目录多余的子文件
                deleteFileByPrefixs(yearDirectory.concat("/"));
                // 部门多余的子文件(没有部门的这里取到的是企业id目录地址)
                boolean deptNoFileFlag = false;
                String deptNoDirectory = yearDirectory.substring(0, yearDirectory.lastIndexOf("/")).concat("/");
                if (!ObjectUtil.equal(directoryAndFileRkSuId, deptNoDirectory)) {
                    deleteFileByPrefixs(deptNoDirectory);
                    deptNoFileFlag = true;
                }
                // 企业id目录多余的子文件
                if (deptNoFileFlag) {
                    deleteFileByPrefixs(directoryAndFileRkSuId);
                }
                */
            }

            if (CollUtil.isNotEmpty(deleteFileList)) {
                BackupUtil.deleteOssFileByFileKeyList(endpoint, accessKey, accessKeySecret, new ArrayList<>(deleteFileList));

            }
        }
    }


    private void deleteFileByPrefixs(String prefixs) {
        List<String> fileList = BackupUtil.getOssOneLevelFileByKey(endpoint, accessKey, accessKeySecret, prefixs);
        if (CollUtil.isNotEmpty(fileList)) {
            BackupUtil.deleteOssFileByFileKeyList(endpoint, accessKey, accessKeySecret, fileList);
        }
    }

    private void deleteDirectoryByPrefixs(String prefixs) {
        List<String> filePrefixList = new ArrayList<>();
        filePrefixList.add(prefixs);
        BackupUtil.deleteOssFileByDirectoryPrefixs(endpoint, accessKey, accessKeySecret, filePrefixList);
    }


    @Override
    public void erpFlowBackup() {

        // 采购、销售，每年备份一个表，6个月之前的数据放进备份表，当前业务表保留当前6个月的数据

        // yiling_erp.erp_sale_flow  备份、清理
        // yiling_erp.erp_purchase_flow  备份、清理
        // yiling_erp.erp_goods_batch_flow  备份、清理
        log.info("OP库流向备份清理任务执行开始, erpFlowBackupJobHandler, erpFlowBackup");
        long start = System.currentTimeMillis();
        try {
            /* 获取当前年份值 */
//            int thisYear = DateUtil.thisYear();
            /* 第8个自然月数据备份 */
            // 备份时间
            //   备份第8个自然月的数据，复制到备份表、当前表做物理删除
            //   （包括当前月份，向前推6个自然月，即是7个自然月之前）
            String monthBackup = BackupUtil.monthBackup(6);
            DateTime monthBackupDate = DateUtil.parse(monthBackup, BackupUtil.FORMATE_MONTH);
            String yearBackup = DateUtil.format(monthBackupDate, BackupUtil.FORMATE_YEAR_ONLY);
            String startTimeBackup = DateUtil.format(DateUtil.beginOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);
            String endTimeBackup = DateUtil.format(DateUtil.endOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);
            // 上个月
            DateTime lastMonthTime = DateUtil.lastMonth();
            String lastMonth = DateUtil.format(lastMonthTime, BackupUtil.FORMATE_MONTH_1);
            DateTime lastSecondMonthTime = DateUtil.offsetMonth(lastMonthTime, -1);
            String lastSecondMonth = DateUtil.format(lastSecondMonthTime, BackupUtil.FORMATE_MONTH_1);
            // 备份表名称
            String erpPurchaseFlowNewTableName = ERP_PURCHASE_FLOW.concat("_").concat(yearBackup);
            String erpSaleFlowNewTableName = ERP_SALE_FLOW.concat("_").concat(yearBackup);
            String sysHeartBeatNewTableName = SYS_HEART_BEAT.concat("_").concat(lastMonth + "");
            String sysHeartBeatSecondNewTableName = SYS_HEART_BEAT.concat("_").concat(lastSecondMonth + "");

            // 查询是否有当前前年份的备份表，不存在则创建备份表：
            List<String> backupTableNames = DataBaseConnection.getInstance().getTableNames(datasourceConfig);
            // 备份表名不存在，创建备份表
            boolean erpPurchaseFlowTableFlag = true;
            boolean erpSaleFlowTableFlag = true;
            boolean sysHeartBeatTableFlag = true;
            boolean sysHeartBeatTableSecondFlag = true;
            if (CollUtil.isEmpty(backupTableNames)) {
                erpPurchaseFlowTableFlag = false;
                erpSaleFlowTableFlag = false;
                sysHeartBeatTableFlag = false;
                sysHeartBeatTableSecondFlag = false;
            } else {
                if (!backupTableNames.contains(erpPurchaseFlowNewTableName)) {
                    erpPurchaseFlowTableFlag = false;
                }
                if (!backupTableNames.contains(erpSaleFlowNewTableName)) {
                    erpSaleFlowTableFlag = false;
                }
                if (!backupTableNames.contains(sysHeartBeatNewTableName)) {
                    sysHeartBeatTableFlag = false;
                }
                if (!backupTableNames.contains(sysHeartBeatSecondNewTableName)) {
                    sysHeartBeatTableSecondFlag = false;
                }
            }

            // 采购、销售，物理删除第9个月及之前的所有数据
            String monthDelete = BackupUtil.monthBackup(8);
            DateTime monthDeleteDate = DateUtil.parse(monthDelete, BackupUtil.FORMATE_MONTH);
            String endTimeDelete = DateUtil.format(DateUtil.endOfMonth(monthDeleteDate), BackupUtil.FORMATE_SECOND);

            // erp对接企业
            List<Long> suIdList = new ArrayList<>();
            List<Long> rkSuIdList = new ArrayList<>();
            List<ErpClientDO> erpClientList = getErpClientList();
            if (CollUtil.isNotEmpty(erpClientList)) {
                suIdList = erpClientList.stream().map(ErpClientDO::getSuId).distinct().collect(Collectors.toList());
                rkSuIdList = erpClientList.stream().map(ErpClientDO::getRkSuId).distinct().collect(Collectors.toList());
            }

            if (CollUtil.isNotEmpty(suIdList)) {
                for (Long suId : suIdList) {
                    // 采购流向备份
                    backupErpPurchaseFlow(erpPurchaseFlowTableFlag, erpPurchaseFlowNewTableName, startTimeBackup, endTimeBackup, endTimeDelete, suId);

                    // 销售流向备份
                    backupErpSaleFlow(erpSaleFlowTableFlag, erpSaleFlowNewTableName, startTimeBackup, endTimeBackup, endTimeDelete, suId);

                    // 流向数据包解析记录数据清理，erp_flow_control 物理删除第4个月及之前的所有数据
                    String flowControlMonthBackup = BackupUtil.monthBackup(4);
                    DateTime flowControlMonthBackupDate = DateUtil.parse(flowControlMonthBackup, BackupUtil.FORMATE_MONTH);
                    String flowControlEndTimeBackup = DateUtil.format(DateUtil.endOfMonth(flowControlMonthBackupDate), BackupUtil.FORMATE_SECOND);
                    erpFlowControlMapper.deleteByfileTime(flowControlEndTimeBackup, suId);

                    /* op库三个流向，物理删除oper_type = 3、sync_status = 2 的 */
                    // 采购物理删除oper_type = 3、sync_status = 2 的
//                    erpPurchaseFlowMapper.deleteByOperTypeAndSyncStatus(suId);
                    // 销售物理删除oper_type = 3、sync_status = 2 的
//                    erpSaleFlowMapper.deleteByOperTypeAndSyncStatus(suId);
                    // 库存物理删除oper_type = 3、sync_status = 2 或 库存数量为0的
//                    erpGoodsBatchFlowMapper.deleteByOperTypeAndSyncStatusOrGbNumber(suId);
                    // 库存流向，物理删除oper_type = 3、sync_status = 3、六个月之前的
//                    erpGoodsBatchFlowMapper.deleteByGbTimeEndAndOperTypeAndSyncStatus(endTimeBackup, suId);
                }
            }

            // 心跳每个月一个备份表，备份上个月的，之前的月备份表删掉，物理删除当前表的、仅保留一条最新的心跳
            String lastMonthStart = DateUtil.format(DateUtil.beginOfMonth(lastMonthTime), BackupUtil.FORMATE_SECOND);
            String lastMonthEnd = DateUtil.format(DateUtil.endOfMonth(lastMonthTime), BackupUtil.FORMATE_SECOND);
            backupSysHeartBeat(sysHeartBeatTableSecondFlag, sysHeartBeatTableFlag, sysHeartBeatSecondNewTableName, sysHeartBeatNewTableName, lastMonthStart, lastMonthEnd);

            // flow_bi_task
            if (CollUtil.isNotEmpty(rkSuIdList)) {
                for (Long eid : rkSuIdList) {
                    /* flow_bi_task 物理删除第8个月及之前的所有数据 */
                    flowBiTaskApi.deleteByTaskTime(endTimeBackup, eid);
                }
            }

        } catch (Exception e) {
            log.error("[ERP流向数据备份], 创建采购流向备份表异常, exception:{}", e.getMessage());
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("OP库ERP流向备份清理任务执行完成, 耗时: {}", end - start);
    }

    @Override
    public void erpFlowBackupNew() {

        log.info("OP库流向备份清理任务执行开始, erpFlowBackupJobHandler, erpFlowBackupNew");
        long start = System.currentTimeMillis();
        // 采购、销售、库存，每年备份一个表，6个月之前的数据放进备份表，当前业务表保留当前6个月的数据
        // yiling_erp.erp_sale_flow  备份、清理
        // yiling_erp.erp_purchase_flow  备份、清理
        // yiling_erp.erp_goods_batch_flow  备份、清理

        // 查询流向已存在的表
        List<String> flowTableNames = DataBaseConnection.getInstance().getTableNames(datasourceConfig);
        // 已存在的备份表
        List<String> backupTableNames = new ArrayList<>();
        Date taskTime = new Date();

        // 第7个月及以前的备份，包括当前月份向前推6个自然月,取第7个自然月，前推第7个月线上的数据插入到备份表中，按照eid+年月 全量批量保存到备份表，已备份这个月的线上数据进行物理删除。
        String monthBackup7 = BackupUtil.monthBackup(6);
        DateTime monthBackupDate = DateUtil.parse(monthBackup7, BackupUtil.FORMATE_MONTH);

        // erp对接企业
        List<Long> suIdList = new ArrayList<>();
        List<Long> rkSuIdList = new ArrayList<>();
        List<ErpClientDO> erpClientList = getErpClientList();
        if (CollUtil.isNotEmpty(erpClientList)) {
            suIdList = erpClientList.stream().map(ErpClientDO::getSuId).distinct().collect(Collectors.toList());
            rkSuIdList = erpClientList.stream().map(ErpClientDO::getRkSuId).distinct().collect(Collectors.toList());
        }

        // 待备份表名称前缀
        String erpPurchaseBackupNamePrefix7 = ERP_PURCHASE_FLOW.concat("_");
        String erpSaleNewBackupNamePrefix7 = ERP_SALE_FLOW.concat("_");

        // 企业列表
        for (Long suId : suIdList) {
            try {
                // 第7个月及以前的备份，包括当前月份向前推6个自然月,取第7个自然月，前推第7个月线上的数据插入到备份表中，按照eid+年月 全量批量保存到备份表，已备份这个月的线上数据进行物理删除。
                // 采购
                backupErpPurchaseFlow7New(suId, flowTableNames, backupTableNames, erpPurchaseBackupNamePrefix7, monthBackup7);
                // 销售
                backupErpSale7New(suId, flowTableNames, backupTableNames, erpSaleNewBackupNamePrefix7, monthBackup7);

                // 流向数据包解析记录数据清理，erp_flow_control 物理删除第4个月及之前的所有数据
                String flowControlMonthBackup = BackupUtil.monthBackup(4);
                DateTime flowControlMonthBackupDate = DateUtil.parse(flowControlMonthBackup, BackupUtil.FORMATE_MONTH);
                String flowControlEndTimeBackup = DateUtil.format(DateUtil.endOfMonth(flowControlMonthBackupDate), BackupUtil.FORMATE_SECOND);
                erpFlowControlMapper.deleteByfileTime(flowControlEndTimeBackup, suId);

            } catch (Exception e) {
                log.error("[OP库ERP流向数据备份] 异常, exception:{}", e.getMessage());
                e.printStackTrace();
            }
        }

        // 心跳每个月一个备份表，备份上个月的，之前的月备份表删掉，物理删除当前表的、仅保留一条最新的心跳
        handlerSysHeartBeat(backupTableNames);

        long end = System.currentTimeMillis();
        log.info("OP库流向备份清理任务执行开始, erpFlowBackupJobHandler, erpFlowBackupNew, 耗时: {}", end - start);

    }

    private void handlerSysHeartBeat(List<String> backupTableNames) {
        // 上个月
        DateTime lastMonthTime = DateUtil.lastMonth();
        String lastMonth = DateUtil.format(lastMonthTime, BackupUtil.FORMATE_MONTH_1);
        DateTime lastSecondMonthTime = DateUtil.offsetMonth(lastMonthTime, -1);
        String lastSecondMonth = DateUtil.format(lastSecondMonthTime, BackupUtil.FORMATE_MONTH_1);
        // 备份表名称
        String sysHeartBeatNewTableName = SYS_HEART_BEAT.concat("_").concat(lastMonth + "");
        String sysHeartBeatSecondNewTableName = SYS_HEART_BEAT.concat("_").concat(lastSecondMonth + "");

        boolean sysHeartBeatTableFlag = true;
        boolean sysHeartBeatTableSecondFlag = true;
        if (CollUtil.isEmpty(backupTableNames)) {
            sysHeartBeatTableFlag = false;
            sysHeartBeatTableSecondFlag = false;
        } else {
            if (!backupTableNames.contains(sysHeartBeatNewTableName)) {
                sysHeartBeatTableFlag = false;
            }
            if (!backupTableNames.contains(sysHeartBeatSecondNewTableName)) {
                sysHeartBeatTableSecondFlag = false;
            }
        }

        String lastMonthStart = DateUtil.format(DateUtil.beginOfMonth(lastMonthTime), BackupUtil.FORMATE_SECOND);
        String lastMonthEnd = DateUtil.format(DateUtil.endOfMonth(lastMonthTime), BackupUtil.FORMATE_SECOND);
        backupSysHeartBeat(sysHeartBeatTableSecondFlag, sysHeartBeatTableFlag, sysHeartBeatSecondNewTableName, sysHeartBeatNewTableName, lastMonthStart, lastMonthEnd);
    }


    /**
     * 第7个月的采购备份
     * 处理未删除、已删除的所有
     *
     * @param suId 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupErpPurchaseFlow7New(Long suId, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        DateTime monthBackupDate = DateUtil.parse(monthBackup, BackupUtil.FORMATE_MONTH);
        String poTimeEnd = DateUtil.format(DateUtil.endOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);
        List<ErpPurchaseFlowMonthBO> poMonthList = erpPurchaseFlowMapper.getMonthListBySuidAndPoTime(suId, poTimeEnd);
        if (CollUtil.isEmpty(poMonthList)) {
            return;
        }
        for (ErpPurchaseFlowMonthBO erpPurchaseFlowMonthBO : poMonthList) {
            handlerErpPurchaseFlow7New(suId, flowTableNames, backupTableNames, tempTableNamePrefix, erpPurchaseFlowMonthBO, true);
        }
    }

    private void handlerErpPurchaseFlow7New(Long suId, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, ErpPurchaseFlowMonthBO erpPurchaseFlowMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String poMonth = erpPurchaseFlowMonthBO.getPoMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(poMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        // 创建备份表
        if (!backupTableNames.contains(tableName)) {
            erpPurchaseFlowMapper.createBackupTable(ERP_PURCHASE_FLOW, tableName);
            backupTableNames.add(tableName);
        }
        // 不管是否存在备份，直接保存到备份
        String poTimeStart = DateUtil.format(DateUtil.beginOfMonth(monthBackupDate7), BackupUtil.FORMATE_SECOND);
        String poTimeEnd = DateUtil.format(DateUtil.endOfMonth(monthBackupDate7), BackupUtil.FORMATE_SECOND);
        Integer insertCount = erpPurchaseFlowMapper.insertBackupDataByPoTimeStartEnd(ERP_PURCHASE_FLOW, tableName, poTimeStart, poTimeEnd, suId);
        if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
            throw new ServiceException("[ERP流向采购备份op库], 处理第7个月及之前的数据, 保存备份数据失败, suId:" + suId + ", 月份: " + poMonth + ", 表名: " + tableName);
        }
        // 物理删除此月份的op库数据
        Integer deleteBackCount = erpPurchaseFlowMapper.deleteByPoTimeStartEnd(poTimeStart, poTimeEnd, suId);
        if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
            throw new ServiceException("[ERP流向采购备份op库], 删除当前月份的线上数据失败, 企业id:" + suId + ", 月份: " + poMonth + ", 表名: " + ERP_PURCHASE_FLOW);
        }
    }

    /**
     * 第7个月的销售备份
     * 处理未删除、已删除的所有
     *
     * @param suId 企业信息
     * @param flowTableNames 已存在备份表名称列表
     * @param backupTableNames 待备份的表名称列表
     * @param tempTableNamePrefix 待备份的表名称前缀
     * @param monthBackup 备份月份
     */

    private void backupErpSale7New(Long suId, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, String monthBackup) {
        // 查询当前企业第7个月及之前的月份列表，按月份倒序
        DateTime monthBackupDate = DateUtil.parse(monthBackup, BackupUtil.FORMATE_MONTH);
        String soTimeEnd = DateUtil.format(DateUtil.endOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);
        List<ErpSaleFlowMonthBO> gbErpSaleMonthList = erpSaleFlowMapper.getMonthListBySuidAndSoTime(suId, soTimeEnd);
        if (CollUtil.isEmpty(gbErpSaleMonthList)) {
            return;
        }
        for (ErpSaleFlowMonthBO erpSaleMonthBO : gbErpSaleMonthList) {
            handlerErpSale7New(suId, flowTableNames, backupTableNames, tempTableNamePrefix, erpSaleMonthBO, true);
        }
    }

    private void handlerErpSale7New(Long suId, List<String> flowTableNames, List<String> backupTableNames, String tempTableNamePrefix, ErpSaleFlowMonthBO erpSaleMonthBO, boolean deleteFlag) {
        // 待备份的月份
        String soMonth = erpSaleMonthBO.getSoMonth();
        // 年份
        DateTime monthBackupDate7 = DateUtil.parse(soMonth, BackupUtil.FORMATE_MONTH);
        String yearBackup = DateUtil.format(monthBackupDate7, BackupUtil.FORMATE_YEAR_ONLY);
        String tableName = tempTableNamePrefix.concat(yearBackup);
        // 已存在的备份表
        getBackupTableNames(flowTableNames, backupTableNames, tableName);

        // 创建备份表
        if (!backupTableNames.contains(tableName)) {
            erpSaleFlowMapper.createBackupTable(ERP_SALE_FLOW, tableName);
            backupTableNames.add(tableName);
        }
        // 不管是否存在备份，直接保存到备份
        String soTimeStart = DateUtil.format(DateUtil.beginOfMonth(monthBackupDate7), BackupUtil.FORMATE_SECOND);
        String soTimeEnd = DateUtil.format(DateUtil.endOfMonth(monthBackupDate7), BackupUtil.FORMATE_SECOND);
        Integer insertCount = erpSaleFlowMapper.insertBackupDataBySuidAndSoTime(ERP_SALE_FLOW, tableName, soTimeStart, soTimeEnd, suId);
        if (ObjectUtil.isNull(insertCount) || insertCount.intValue() == 0) {
            throw new ServiceException("[ERP流向销售备份op库], 处理第7个月及之前的数据, 保存备份数据失败, suId:" + suId + ", 月份: " + soMonth + ", 表名: " + tableName);
        }
        // 物理删除此月份的op库数据
        Integer deleteBackCount = erpSaleFlowMapper.deleteBySuIdAndSoTime(soTimeStart, soTimeEnd, suId);
        if (ObjectUtil.isNull(deleteBackCount) || deleteBackCount == 0) {
            throw new ServiceException("[ERP流向销售备份op库], 删除当前月份的线上数据失败, suId:" + suId + ", 月份: " + soMonth + ", 表名: " + ERP_SALE_FLOW);
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

    private void deleteSysHeartBeatByCreateTime(String sysHeartBeatNewTableName, String startTimeBackup, String endTimeBackup) {
        // 查询备份表的id，在当前表中做物理删除
        Long count = sysHeartBeatMapper.getBackupTableCountByCreateTime(sysHeartBeatNewTableName, startTimeBackup, endTimeBackup);
        if (ObjectUtil.isNull(count) || count.intValue() < 1) {
            return;
        }
        // 每个公司上个月最新一条心跳
        List<Map<String, Long>> lastMonthMaxIdMapList = sysHeartBeatMapper.getIdByMaxCreateTime(startTimeBackup, endTimeBackup);
        if (CollUtil.isEmpty(lastMonthMaxIdMapList)) {
            return;
        }
        Map<Long, Long> lastMonthMaxIdMap = new HashMap<>();
        for (Map<String, Long> map : lastMonthMaxIdMapList) {
            lastMonthMaxIdMap.put(map.get("suId").longValue(), map.get("id"));
        }
        // 删除心跳上个月及之前所有数据，仅保留上个月最新一条
        List<Long> idList = new ArrayList<>(lastMonthMaxIdMap.values());
        sysHeartBeatMapper.deleteByIdAndCreateTime(idList, endTimeBackup);
    }

    private void backupErpPurchaseFlow(boolean erpPurchaseFlowTableFlag, String erpPurchaseFlowNewTableName, String startTimeBackup, String endTimeBackup, String endTimeDelete, Long suId) {
        if (!erpPurchaseFlowTableFlag) {
            // 创建采购流向备份表
            erpPurchaseFlowMapper.createBackupTable(ERP_PURCHASE_FLOW, erpPurchaseFlowNewTableName);
//            insertCount = erpPurchaseFlowMapper.initBackupData(ERP_PURCHASE_FLOW, erpPurchaseFlowNewTableName, endTimeBackup);
        }
        Integer insertCount = erpPurchaseFlowMapper.insertBackupDataByPoTime(ERP_PURCHASE_FLOW, erpPurchaseFlowNewTableName, endTimeBackup, suId);

        if (ObjectUtil.isNotNull(insertCount) && insertCount.intValue() != 0) {
            // 物理删除第6个月及之前的所有数据
            erpPurchaseFlowMapper.deleteByPoTimeEnd(endTimeBackup, suId);
            // 采购流向物理删除当前表中已备份的数据
//            erpPurchaseFlowMapper.deleteByPoTime(startTimeBackup, endTimeBackup);
            // 物理删除第9个月及之前的所有数据
//            erpPurchaseFlowMapper.deleteByPoTimeEnd(endTimeDelete);
        }
    }

    private void backupErpSaleFlow(boolean erpSaleFlowTableFlag, String erpSaleFlowNewTableName, String startTimeBackup, String endTimeBackup, String endTimeDelete, Long suId) {
        if (!erpSaleFlowTableFlag) {
            // 创建销售流向备份表
            erpSaleFlowMapper.createBackupTable(ERP_SALE_FLOW, erpSaleFlowNewTableName);
//            insertCount = erpSaleFlowMapper.initBackupData(ERP_SALE_FLOW, erpSaleFlowNewTableName, endTimeBackup);
        }
        Integer insertCount = erpSaleFlowMapper.insertBackupDataBySoTime(ERP_SALE_FLOW, erpSaleFlowNewTableName, endTimeBackup, suId);

        if (ObjectUtil.isNotNull(insertCount) && insertCount.intValue() != 0) {
            // 物理删除第6个月及之前的所有数据
            erpSaleFlowMapper.deleteBySoTimeEnd(endTimeBackup, suId);
            // 销售流向物理删除当前表中已备份的数据
//            erpSaleFlowMapper.deleteBySoTime(startTimeBackup, endTimeBackup);
            // 物理删除第9个月及之前的所有数据
//            erpSaleFlowMapper.deleteBySoTimeEnd(endTimeDelete);
        }
    }

    private void backupSysHeartBeat(boolean sysHeartBeatTableSecondFlag, boolean sysHeartBeatTableFlag, String sysHeartBeatSecondNewTableName, String sysHeartBeatNewTableName, String startTimeBackup, String endTimeBackup) {

        if (sysHeartBeatTableSecondFlag) {
            // 删除前面第二个月的备份表
            sysHeartBeatMapper.dropLastSecondMonthBackupTable(sysHeartBeatSecondNewTableName);
        }

        if (!sysHeartBeatTableFlag) {
            // 创建上个月的心跳备份表
            sysHeartBeatMapper.createBackupTable(SYS_HEART_BEAT, sysHeartBeatNewTableName);
        }

        Integer insertCount = sysHeartBeatMapper.insertBackupDataByCreateTime(SYS_HEART_BEAT, sysHeartBeatNewTableName, startTimeBackup, endTimeBackup);
        if (ObjectUtil.isNotNull(insertCount) && insertCount.intValue() != 0) {
            // 心跳表清理
            deleteSysHeartBeatByCreateTime(sysHeartBeatNewTableName, startTimeBackup, endTimeBackup);
        }
    }

    //    public static void main(String[] args) {

    //        int thisYear = DateUtil.thisYear();
    //        String monthBackup = BackupUtil.monthBackup(7);
    //        DateTime monthBackupDate = DateUtil.parse(monthBackup, BackupUtil.FORMATE_MONTH);
    //        String startTimeBackup = DateUtil.format(DateUtil.beginOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);
    //        String endTimeBackup = DateUtil.format(DateUtil.endOfMonth(monthBackupDate), BackupUtil.FORMATE_SECOND);
    //        System.out.println(">>>>>> thisYear:" + thisYear);
    //        System.out.println(">>>>>> startTimeBackup:" + startTimeBackup);
    //        System.out.println(">>>>>> endTimeBackup:" + endTimeBackup);
    //
    //        String monthDelete = BackupUtil.monthBackup(8);
    //        DateTime monthDeleteDate = DateUtil.parse(monthDelete, BackupUtil.FORMATE_MONTH);
    //        String endTimeDelete = DateUtil.format(DateUtil.endOfMonth(monthDeleteDate), BackupUtil.FORMATE_SECOND);
    //        System.out.println(">>>>>> endTimeDelete:" + endTimeDelete);

    //        String year = "2022";
    //        boolean isYear = GenericValidator.isDate(year, BackupUtil.FORMATE_YEAR_ONLY, true);
    //        System.out.println(">>>>> isYear:" + isYear);
    //        String month = "07";
    //        boolean isMonth = GenericValidator.isDate(month, BackupUtil.FORMATE_MONTH_ONLY, true);
    //        System.out.println(">>>>> isMonth:" + isMonth);

    //        String directoryUrl = "control/fun/movie/2022/";
    //        System.out.println(">>>>> directoryUrl:" + directoryUrl);
    //        String directoryUrl2 = directoryUrl.substring(0, directoryUrl.lastIndexOf("/"));
    //        System.out.println(">>>>> directoryUrl2:" + directoryUrl2);
    //        String directory = directoryUrl2.substring(directoryUrl2.lastIndexOf("/") + 1, directoryUrl2.length());
    //        System.out.println(">>>>> directory:" + directory);

    //        String month = "07";
    //        int monthInt = Integer.parseInt(month);
    //        System.out.println(">>>>> monthInt:" + monthInt);

    //                String str = "dev/asdfa/12/";
    //                String str1 = "dev/asdfa/12.txt";
    //                if(!str.endsWith("/")){
    //                    System.out.println(">>>>> str 是文件");
    //                } else {
    //                    System.out.println(">>>>> str 是目录");
    //                }
    //                if(!str1.endsWith("/")){
    //                    System.out.println(">>>>> str1 是文件");
    //                } else {
    //                    System.out.println(">>>>> str1 是目录");
    //                }

    //                String eidStr = "1";
    //                List<Long> eidList = new ArrayList<>();
    //                eidList.add(1L);
    //                if(eidList.contains(Integer.parseInt(eidStr))){
    //                    System.out.println(">>>>> contains Integer.parseInt(eidStr)");
    //                }
    //                if(eidList.contains(Long.parseLong(eidStr))){
    //                    System.out.println(">>>>> contains Long.parseLong(eidStr)");
    //                }
    //    }

}
