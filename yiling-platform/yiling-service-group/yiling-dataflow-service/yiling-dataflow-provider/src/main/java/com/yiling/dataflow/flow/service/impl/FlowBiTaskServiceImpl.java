package com.yiling.dataflow.flow.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dao.FlowBiTaskMapper;
import com.yiling.dataflow.flow.entity.FlowBiTaskDO;
import com.yiling.dataflow.flow.enums.ErpTopicName;
import com.yiling.dataflow.flow.enums.SyncStatus;
import com.yiling.dataflow.flow.excel.GoodsBatchFlowExcel;
import com.yiling.dataflow.flow.excel.PurchaseFlowExcel;
import com.yiling.dataflow.flow.excel.SaleFlowExcel;
import com.yiling.dataflow.flow.service.FlowBiTaskService;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDO;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.utils.DateTransUtil;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-04-06
 */
@Slf4j
@RefreshScope
@Service
public class FlowBiTaskServiceImpl extends BaseServiceImpl<FlowBiTaskMapper, FlowBiTaskDO> implements FlowBiTaskService {

    private final static Integer day = -44;

    @Value("${flow.bi.ip}")
    private String ftpIp;
    @Value("${flow.bi.port}")
    private int    ftpPort;
    @Value("${flow.bi.userName}")
    private String userName;
    @Value("${flow.bi.passWord}")
    private String passWord;

    @Autowired
    private CrmEnterpriseService        crmEnterpriseService;
    @Autowired
    private FlowPurchaseService         flowPurchaseService;
    @Autowired
    private FlowSaleService             flowSaleService;
    @Autowired
    private FlowGoodsBatchService       flowGoodsBatchService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;
    @Autowired
    private FlowMonthBiTaskService      flowMonthBiTaskService;
    @DubboReference
    private EnterpriseApi               enterpriseApi;


    @Override
    public void excelFlowBiTask() {
        //每一个月底不执行上传日流向
        int dayOfMonth = DateUtil.dayOfMonth(new Date());
        boolean hasMonth = false;
        if (dayOfMonth == 1) {
            hasMonth = true;
        }

        QueryWrapper<FlowBiTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowBiTaskDO::getSyncStatus, SyncStatus.UNSYNC.getCode()).last("limit 1000");
        List<FlowBiTaskDO> list = this.list(queryWrapper);

        if (CollUtil.isNotEmpty(list)) {
            for (FlowBiTaskDO flowBiTaskDO : list) {
                try {
                    //先查找客户信息
                    if (flowBiTaskDO.getCrmEnterpriseId() == 0) {
                        flowBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                        flowBiTaskDO.setSyncTime(new Date());
                        flowBiTaskDO.setSyncMsg("CrmEnterpriseId数据为0");
                        flowBiTaskDO.setOpTime(new Date());
                        flowBiTaskDO.setOpUserId(0L);
                        this.updateById(flowBiTaskDO);
                        continue;
                    }

                    CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(flowBiTaskDO.getCrmEnterpriseId());
                    if (crmEnterpriseDO == null) {
                        flowBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                        flowBiTaskDO.setSyncTime(new Date());
                        flowBiTaskDO.setSyncMsg("没有找到行业数据");
                        flowBiTaskDO.setOpTime(new Date());
                        flowBiTaskDO.setOpUserId(0L);
                        this.updateById(flowBiTaskDO);
                        continue;
                    }

                    if (StrUtil.isEmpty(crmEnterpriseDO.getCrmNumber())) {
                        flowBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                        flowBiTaskDO.setSyncTime(new Date());
                        flowBiTaskDO.setSyncMsg("dems编码为空");
                        flowBiTaskDO.setOpTime(new Date());
                        flowBiTaskDO.setOpUserId(0L);
                        this.updateById(flowBiTaskDO);
                        continue;
                    }
                    boolean bool = false;
                    String remark = "";
                    //查询数据
                    if (flowBiTaskDO.getTaskCode().equals(ErpTopicName.ErpPurchaseFlow.getMethod())) {
                        String purchaseFlowExcel = "PD_" + crmEnterpriseDO.getCrmNumber() + "_" + crmEnterpriseDO.getName() + "_" + DateUtil.format(flowBiTaskDO.getTaskTime(), "yyyyMMdd") + "_YL.csv";
                        List<PurchaseFlowExcel> purchaseFlowExcelList = new ArrayList<>();
                        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
                        request.setEidList(Arrays.asList(flowBiTaskDO.getEid()));
                        request.setStartTime(DateUtil.offsetDay(DateUtil.beginOfDay(flowBiTaskDO.getTaskTime()), day));
                        request.setEndTime(DateUtil.endOfDay(flowBiTaskDO.getTaskTime()));
                        Page<FlowPurchaseDTO> pageQurchase = null;
                        int currentPurchase = 1;
                        do {
                            request.setCurrent(currentPurchase);
                            request.setSize(500);
                            pageQurchase = PojoUtils.map(flowPurchaseService.page(request), FlowPurchaseDTO.class);

                            if (pageQurchase == null || CollUtil.isEmpty(pageQurchase.getRecords())) {
                                break;
                            }

                            for (FlowPurchaseDTO e : pageQurchase.getRecords()) {
                                PurchaseFlowExcel purchaseFlow = PojoUtils.map(e, PurchaseFlowExcel.class);
                                purchaseFlow.setPoTime(DateUtil.formatDateTime(e.getPoTime()));
                                purchaseFlowExcelList.add(purchaseFlow);
                            }
                            currentPurchase = currentPurchase + 1;
                        } while (pageQurchase != null && CollUtil.isNotEmpty(pageQurchase.getRecords()));

                        if (CollUtil.isNotEmpty(purchaseFlowExcelList)) {
                            CsvExportParams exportParams = new CsvExportParams();
                            exportParams.setCreateHeadRows(true);
                            exportParams.setEncoding("GBK");
                            bool = exportExcelAndUpload(flowBiTaskDO, purchaseFlowExcel, PurchaseFlowExcel.class, purchaseFlowExcelList, exportParams);
                            remark = purchaseFlowExcel;
                        }
                    }

                    if (flowBiTaskDO.getTaskCode().equals(ErpTopicName.ErpSaleFlow.getMethod())) {
                        String saleFlowExcel = "SD_" + crmEnterpriseDO.getCrmNumber() + "_" + crmEnterpriseDO.getName() + "_" + DateUtil.format(flowBiTaskDO.getTaskTime(), "yyyyMMdd") + "_YL.csv";
                        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
                        request.setEidList(Arrays.asList(flowBiTaskDO.getEid()));
                        request.setStartTime(DateUtil.offsetDay(DateUtil.beginOfDay(flowBiTaskDO.getTaskTime()), day));
                        request.setEndTime(DateUtil.endOfDay(flowBiTaskDO.getTaskTime()));
                        List<SaleFlowExcel> saleFlowExcelList = new ArrayList<>();
                        Page<FlowSaleDTO> pageSale = null;
                        int currentSale = 1;
                        do {
                            request.setCurrent(currentSale);
                            request.setSize(500);
                            pageSale = PojoUtils.map(flowSaleService.page(request), FlowSaleDTO.class);

                            if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                                break;
                            }

                            for (FlowSaleDTO e : pageSale.getRecords()) {
                                SaleFlowExcel saleFlow = PojoUtils.map(e, SaleFlowExcel.class);
                                saleFlow.setSoTime(DateUtil.formatDateTime(e.getSoTime()));
                                saleFlowExcelList.add(saleFlow);
                            }
                            currentSale = currentSale + 1;
                        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));

                        if (CollUtil.isNotEmpty(saleFlowExcelList)) {
                            CsvExportParams exportParams = new CsvExportParams();
                            exportParams.setCreateHeadRows(true);
                            exportParams.setEncoding("GBK");
                            bool = exportExcelAndUpload(flowBiTaskDO, saleFlowExcel, SaleFlowExcel.class, saleFlowExcelList, exportParams);
                            remark = saleFlowExcel;
                        }
                    }


                    if (flowBiTaskDO.getTaskCode().equals(ErpTopicName.ErpGoodsBatchFlow.getMethod())) {
                        List<GoodsBatchFlowExcel> goodsBatchFlowExcelList = new ArrayList<>();
                        //月流向查询备份库存
                        if (hasMonth) {
                            goodsBatchFlowExcelList = flowMonthBiTaskService.getGoodsBatchFlowExcelList(flowBiTaskDO.getEid(), flowBiTaskDO.getTaskTime());
                        } else {
                            //日流向查询备份库存
                            QueryFlowGoodsBatchDetailListPageRequest goodsBatchDetailRequest = new QueryFlowGoodsBatchDetailListPageRequest();
                            goodsBatchDetailRequest.setEidList(Arrays.asList(flowBiTaskDO.getEid()));
                            goodsBatchDetailRequest.setStartTime(DateUtil.beginOfDay(flowBiTaskDO.getTaskTime()));
                            goodsBatchDetailRequest.setEndTime(DateUtil.endOfDay(flowBiTaskDO.getTaskTime()));
                            Page<FlowGoodsBatchDetailDO> pageGoodsBatch = null;
                            int currentGoodsBatchDetail = 1;
                            do {
                                goodsBatchDetailRequest.setCurrent(currentGoodsBatchDetail);
                                goodsBatchDetailRequest.setSize(2000);
                                pageGoodsBatch = flowGoodsBatchDetailService.page(goodsBatchDetailRequest);
                                if (pageGoodsBatch == null || CollUtil.isEmpty(pageGoodsBatch.getRecords())) {
                                    break;
                                }
                                for (FlowGoodsBatchDetailDO e : pageGoodsBatch.getRecords()) {
                                    GoodsBatchFlowExcel goodsBatchFlow = new GoodsBatchFlowExcel();
                                    goodsBatchFlow.setGbName(e.getGbName());
                                    goodsBatchFlow.setGbManufacturer(e.getGbManufacturer());
                                    goodsBatchFlow.setGbBatchNo(e.getGbBatchNo());
                                    goodsBatchFlow.setGbNumber(e.getGbNumber().toPlainString());
                                    goodsBatchFlow.setGbSpecifications(e.getGbSpecifications());
                                    goodsBatchFlow.setGbUnit(e.getGbUnit());
                                    goodsBatchFlow.setGbTime(e.getGbTime());
                                    goodsBatchFlow.setDateTime(flowBiTaskDO.getTaskTime());
                                    goodsBatchFlow.setGbEndTime(DateTransUtil.parseDate(e.getGbEndTime()));
                                    goodsBatchFlow.setGbProduceTime(DateTransUtil.parseDate(e.getGbProduceTime()));
                                    goodsBatchFlowExcelList.add(goodsBatchFlow);
                                }
                                currentGoodsBatchDetail = currentGoodsBatchDetail + 1;
                            } while (pageGoodsBatch != null && CollUtil.isNotEmpty(pageGoodsBatch.getRecords()));
                        }

                        if (CollUtil.isNotEmpty(goodsBatchFlowExcelList)) {
                            String goodsBatchFlowExcel = "ID_" + crmEnterpriseDO.getCrmNumber() + "_" + crmEnterpriseDO.getName() + "_" + DateUtil.format(flowBiTaskDO.getTaskTime(), "yyyyMMdd") + "_YL.csv";
                            CsvExportParams exportParams = new CsvExportParams();
                            exportParams.setCreateHeadRows(true);
                            exportParams.setEncoding("GBK");
                            bool = exportExcelAndUpload(flowBiTaskDO, goodsBatchFlowExcel, GoodsBatchFlowExcel.class, goodsBatchFlowExcelList, exportParams);
                            remark = goodsBatchFlowExcel;
                        }
                    }

                    if (bool) {
                        flowBiTaskDO.setSyncStatus(SyncStatus.SUCCESS.getCode());
                        flowBiTaskDO.setSyncTime(new Date());
                        flowBiTaskDO.setSyncMsg("同步成功");
                        flowBiTaskDO.setOpTime(new Date());
                        flowBiTaskDO.setOpUserId(0L);
                        flowBiTaskDO.setRemark(remark);
                        this.updateById(flowBiTaskDO);
                    } else {
                        flowBiTaskDO.setSyncStatus(SyncStatus.SUCCESS.getCode());
                        flowBiTaskDO.setSyncTime(new Date());
                        flowBiTaskDO.setSyncMsg("没有数据生成");
                        flowBiTaskDO.setOpTime(new Date());
                        flowBiTaskDO.setOpUserId(0L);
                        this.updateById(flowBiTaskDO);
                    }
                } catch (Exception e) {
                    flowBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                    flowBiTaskDO.setSyncTime(new Date());
                    flowBiTaskDO.setSyncMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
                    flowBiTaskDO.setOpTime(new Date());
                    flowBiTaskDO.setOpUserId(0L);
                    this.updateById(flowBiTaskDO);
                    log.info("生成BI数据异常id={}", flowBiTaskDO.getId(), e);
                }
            }
        }
    }

    @Override
    public Integer deleteByTaskTime(String taskTimeEnd, Long eid) {
        return this.baseMapper.deleteByTaskTime(taskTimeEnd, eid);
    }

//    public boolean exportExcelAndUpload(FlowBiTaskDO flowBiTaskDO, String name, Class<?>
//            pojoClass, Collection<?> dataSet, CsvExportParams exportParams) {
//        Ftp ftp = null;
//        File dir = null;
//        FileOutputStream fileOutputStream = null;
//        try {
//            ftp = new Ftp(ftpIp, ftpPort, userName, passWord, Charset.forName("GBK"), FtpMode.Active);
//            String ftpPath=DateUtil.format(flowBiTaskDO.getTaskTime(), "yyyyMMdd");
//            if (!ftp.exist(ftpPath)) {
//                ftp.mkdir(ftpPath);
//            }
//            File tmpDir = FileUtil.getTmpDir();
//            dir = FileUtil.newFile(tmpDir.getPath() + File.separator + flowBiTaskDO.getEid());
//            if (!dir.isDirectory()) {
//                dir.mkdirs();
//            }
//            File excelDir = FileUtil.newFile(dir.getPath() + File.separator + name);
//            if (excelDir.exists()) {
//                FileUtil.del(excelDir);
//            } else {
//                excelDir.createNewFile();
//            }
//
//            fileOutputStream = new FileOutputStream(excelDir);
//            CsvExportUtil.exportCsv(exportParams, pojoClass, dataSet, fileOutputStream);
//            ftp.upload("/"+ftpPath+"/", excelDir);
//            return true;
//        } catch (IOException e) {
//            log.error("excel生成BI文件报错name={}", name, e);
//            throw new RuntimeException(e);
//        } finally {
//            // 清空临时文件
//            FileUtil.del(dir);
//            try {
//                if (ftp != null) {
//                    ftp.close();
//                }
//                if (fileOutputStream != null) {
//                    fileOutputStream.close();
//                }
//            } catch (IOException e) {
//                log.error("数据流关闭异常", e);
//            }
//        }
//    }

    public boolean exportExcelAndUpload(FlowBiTaskDO flowBiTaskDO, String name, Class<?>
            pojoClass, Collection<?> dataSet, CsvExportParams exportParams) {
        Ftp ftp = null;
        File dir = null;
        FileOutputStream fileOutputStream = null;
        try {
            FtpConfig ftpConfig = new FtpConfig();
            ftpConfig.setUser(userName);
            ftpConfig.setHost(ftpIp);
            ftpConfig.setPort(ftpPort);
            ftpConfig.setPassword(passWord);
            ftpConfig.setSoTimeout(1000 * 60 * 60);
            ftpConfig.setConnectionTimeout(1000 * 60 * 60);
            ftpConfig.setCharset(Charset.forName("GBK"));
            ftp = new Ftp(ftpConfig, FtpMode.Passive);
            File tmpDir = FileUtil.getTmpDir();
            dir = FileUtil.newFile(tmpDir.getPath() + File.separator + flowBiTaskDO.getEid());
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            File excelDir = FileUtil.newFile(dir.getPath() + File.separator + name);
            if (excelDir.exists()) {
                FileUtil.del(excelDir);
            } else {
                excelDir.createNewFile();
            }

            fileOutputStream = new FileOutputStream(excelDir);
            CsvExportUtil.exportCsv(exportParams, pojoClass, dataSet, fileOutputStream);
            ftp.upload("/", excelDir);
            return true;
        } catch (IOException e) {
            log.error("excel生成BI文件报错name={}", name, e);
            throw new RuntimeException(e);
        } finally {
            // 清空临时文件
            FileUtil.del(dir);
            try {
                if (ftp != null) {
                    ftp.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error("数据流关闭异常", e);
            }
        }
    }

    private String getKey(FlowGoodsBatchDO flowGoodsBatchDO) {
        return flowGoodsBatchDO.getSpecificationId() + "-" + flowGoodsBatchDO.getInSn() + "-" + flowGoodsBatchDO.getGbBatchNo();
    }
}
