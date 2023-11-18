package com.yiling.dataflow.flow.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dao.FlowMonthBiTaskMapper;
import com.yiling.dataflow.flow.dto.FlowCrmEnterpriseDTO;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.flow.entity.FlowMonthBiTaskDO;
import com.yiling.dataflow.flow.enums.ErpTopicName;
import com.yiling.dataflow.flow.enums.SyncStatus;
import com.yiling.dataflow.flow.excel.GoodsBatchFlowExcel;
import com.yiling.dataflow.flow.excel.PurchaseFlowExcel;
import com.yiling.dataflow.flow.excel.SaleFlowExcel;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.order.util.FlowCommonUtil;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.slf4j.Slf4j;

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
public class FlowMonthBiTaskServiceImpl extends BaseServiceImpl<FlowMonthBiTaskMapper, FlowMonthBiTaskDO> implements FlowMonthBiTaskService {

    private final static Integer day = -44;

    @Value("${flow.month.ip}")
    private String ftpIp;
    @Value("${flow.month.port}")
    private int ftpPort;
    @Value("${flow.month.userName}")
    private String userName;
    @Value("${flow.month.passWord}")
    private String passWord;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Autowired
    private FlowSaleService flowSaleService;

    @Autowired
    private FlowGoodsBatchService flowGoodsBatchService;

    @Override
    public void excelMonthFlowBiTask() {
        QueryWrapper<FlowMonthBiTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowMonthBiTaskDO::getSyncStatus, SyncStatus.UNSYNC.getCode()).last("limit 1000");
        List<FlowMonthBiTaskDO> list = this.list(queryWrapper);

        if (CollUtil.isNotEmpty(list)) {
            for (FlowMonthBiTaskDO flowMonthBiTaskDO : list) {
                try {
                    //先查找客户信息
                    if (flowMonthBiTaskDO.getCrmEnterpriseId() == 0) {
                        flowMonthBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                        flowMonthBiTaskDO.setSyncTime(new Date());
                        flowMonthBiTaskDO.setSyncMsg("CrmEnterpriseId数据为0");
                        flowMonthBiTaskDO.setOpTime(new Date());
                        flowMonthBiTaskDO.setOpUserId(0L);
                        this.updateById(flowMonthBiTaskDO);
                        continue;
                    }

                    CrmEnterpriseDO crmEnterpriseDO= crmEnterpriseService.getById(flowMonthBiTaskDO.getCrmEnterpriseId());
                    if (crmEnterpriseDO==null) {
                        flowMonthBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                        flowMonthBiTaskDO.setSyncTime(new Date());
                        flowMonthBiTaskDO.setSyncMsg("没有找到行业数据");
                        flowMonthBiTaskDO.setOpTime(new Date());
                        flowMonthBiTaskDO.setOpUserId(0L);
                        this.updateById(flowMonthBiTaskDO);
                        continue;
                    }

                    if (StrUtil.isEmpty(crmEnterpriseDO.getCrmNumber())) {
                        flowMonthBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                        flowMonthBiTaskDO.setSyncTime(new Date());
                        flowMonthBiTaskDO.setSyncMsg("dems编码为空");
                        flowMonthBiTaskDO.setOpTime(new Date());
                        flowMonthBiTaskDO.setOpUserId(0L);
                        this.updateById(flowMonthBiTaskDO);
                        continue;
                    }

                    boolean bool = false;
                    String remark = "";
                    //查询数据
                    DateTime lastMonthEndTime = DateUtil.beginOfMonth(flowMonthBiTaskDO.getTaskTime());
                    if (flowMonthBiTaskDO.getTaskCode().equals(ErpTopicName.ErpPurchaseFlow.getMethod())) {
                        String purchaseFlowExcel = "PD_" + crmEnterpriseDO.getCrmNumber() + "_" + crmEnterpriseDO.getName() + "_" + DateUtil.format(lastMonthEndTime, "yyyyMMdd") + "_YL.csv";
                        List<PurchaseFlowExcel> purchaseFlowExcelList = getPurchaseFlowExcelList(flowMonthBiTaskDO.getEid(),lastMonthEndTime);
                        if (CollUtil.isNotEmpty(purchaseFlowExcelList)) {
                            CsvExportParams exportParams = new CsvExportParams();
                            exportParams.setCreateHeadRows(true);
                            exportParams.setEncoding("GBK");
                            bool = exportExcelAndUpload(flowMonthBiTaskDO, purchaseFlowExcel, PurchaseFlowExcel.class, purchaseFlowExcelList, exportParams);
                            remark = purchaseFlowExcel;
                        }
                    }

                    if (flowMonthBiTaskDO.getTaskCode().equals(ErpTopicName.ErpSaleFlow.getMethod())) {
                        String saleFlowExcel = "SD_" + crmEnterpriseDO.getCrmNumber() + "_" + crmEnterpriseDO.getName() + "_" + DateUtil.format(lastMonthEndTime, "yyyyMMdd") + "_YL.csv";
                        List<SaleFlowExcel> saleFlowExcelList = getSaleFlowExcelList(flowMonthBiTaskDO.getEid(),lastMonthEndTime);
                        if (CollUtil.isNotEmpty(saleFlowExcelList)) {
                            CsvExportParams exportParams = new CsvExportParams();
                            exportParams.setCreateHeadRows(true);
                            exportParams.setEncoding("GBK");
                            bool = exportExcelAndUpload(flowMonthBiTaskDO, saleFlowExcel, SaleFlowExcel.class, saleFlowExcelList, exportParams);
                            remark = saleFlowExcel;
                        }
                    }

                    if (flowMonthBiTaskDO.getTaskCode().equals(ErpTopicName.ErpGoodsBatchFlow.getMethod())) {
                        String goodsBatchFlowExcel = "ID_" + crmEnterpriseDO.getCrmNumber() + "_" + crmEnterpriseDO.getName() + "_" + DateUtil.format(lastMonthEndTime, "yyyyMMdd") + "_YL.csv";
                        List<GoodsBatchFlowExcel> goodsBatchFlowExcelList = getGoodsBatchFlowExcelList(flowMonthBiTaskDO.getEid(),flowMonthBiTaskDO.getTaskTime());
                        if (CollUtil.isNotEmpty(goodsBatchFlowExcelList)) {
                            CsvExportParams exportParams = new CsvExportParams();
                            exportParams.setCreateHeadRows(true);
                            exportParams.setEncoding("GBK");
                            bool = exportExcelAndUpload(flowMonthBiTaskDO, goodsBatchFlowExcel, GoodsBatchFlowExcel.class, goodsBatchFlowExcelList, exportParams);
                            remark = goodsBatchFlowExcel;
                        }
                    }

                    if (bool) {
                        flowMonthBiTaskDO.setSyncStatus(SyncStatus.SUCCESS.getCode());
                        flowMonthBiTaskDO.setSyncTime(new Date());
                        flowMonthBiTaskDO.setSyncMsg("同步成功");
                        flowMonthBiTaskDO.setOpTime(new Date());
                        flowMonthBiTaskDO.setOpUserId(0L);
                        flowMonthBiTaskDO.setRemark(remark);
                        this.updateById(flowMonthBiTaskDO);
                    } else {
                        flowMonthBiTaskDO.setSyncStatus(SyncStatus.SUCCESS.getCode());
                        flowMonthBiTaskDO.setSyncTime(new Date());
                        flowMonthBiTaskDO.setSyncMsg("没有数据生成");
                        flowMonthBiTaskDO.setOpTime(new Date());
                        flowMonthBiTaskDO.setOpUserId(0L);
                        this.updateById(flowMonthBiTaskDO);
                    }
                } catch (Exception e) {
                    flowMonthBiTaskDO.setSyncStatus(SyncStatus.FAIL.getCode());
                    flowMonthBiTaskDO.setSyncTime(new Date());
                    flowMonthBiTaskDO.setSyncMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
                    flowMonthBiTaskDO.setOpTime(new Date());
                    flowMonthBiTaskDO.setOpUserId(0L);
                    this.updateById(flowMonthBiTaskDO);
                    log.error("生成BI数据异常", e);
                }
            }
        }
    }

    public boolean exportExcelAndUpload(FlowMonthBiTaskDO flowMonthBiTaskDO, String name, Class<?> pojoClass, Collection<?> dataSet, CsvExportParams exportParams) {
        Ftp ftp = null;
        File dir = null;
        FileOutputStream fileOutputStream = null;
        try {
            ftp = new Ftp(ftpIp, ftpPort, userName, passWord, Charset.forName("GBK"), FtpMode.Active);
            String ftpPath=DateUtil.format(flowMonthBiTaskDO.getTaskTime(), "yyyyMMdd");
            if (!ftp.exist(ftpPath)) {
                ftp.mkdir(ftpPath);
            }
            File tmpDir = FileUtil.getTmpDir();
            dir = FileUtil.newFile(tmpDir.getPath() + File.separator + flowMonthBiTaskDO.getEid());
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
            ftp.upload("/"+ftpPath+"/", excelDir);
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

    @Override
    public HashMap<String, List> getAllFlowMonthBiData(FlowMonthBiTaskRequest request){
        HashMap<String,List> map = new HashMap<>();
        // PD数据
        List<PurchaseFlowExcel> purchaseFlowExcelList = getPurchaseFlowExcelList(request.getEid(), request.getQueryDate());
        if(CollectionUtils.isNotEmpty(purchaseFlowExcelList)){
            map.put("PD",purchaseFlowExcelList);
        }
        // SD数据
        List<SaleFlowExcel> saleFlowExcelList = getSaleFlowExcelList(request.getEid(), request.getQueryDate());
        if(CollectionUtils.isNotEmpty(saleFlowExcelList)){
            map.put("SD",saleFlowExcelList);
        }
        // ID数据
        List<GoodsBatchFlowExcel> goodsBatchFlowExcelList = getGoodsBatchFlowExcelList(request.getEid(), new Date());
        if(CollectionUtils.isNotEmpty(goodsBatchFlowExcelList)){
            map.put("ID",goodsBatchFlowExcelList);
        }
        return map;
    }

    @Override
    public List<FlowMonthBiTaskDO> getAllFlowEidList(FlowMonthBiTaskRequest request) {
        QueryWrapper<FlowMonthBiTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowMonthBiTaskDO::getTaskTime,request.getQueryDate());
        queryWrapper.select("DISTINCT eid,ename");
        return this.list(queryWrapper);
    }

    @Override
    public FlowCrmEnterpriseDTO getCrmEnterpriseInfo(FlowMonthBiTaskRequest request) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
        if (enterpriseDTO == null) {
            throw new BusinessException(ResultCode.FAILED,"客户数据不存在");
        }
        if (StrUtil.isEmpty(enterpriseDTO.getLicenseNumber())){
            throw new BusinessException(ResultCode.FAILED,"客户数据没有信用代码");
        }

        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(request.getCrmEnterpriseId());
//        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getCrmEnterpriseByLicenseNumber(enterpriseDTO.getLicenseNumber());
        if (crmEnterpriseDO == null){
            throw new BusinessException(ResultCode.FAILED,"客户数据没有匹配到CRM");
        }
//        CrmEnterpriseCodeDO crmEnterpriseCodeDO = crmEnterpriseCodeService.getCrmEnterpriseCodeByName(crmEnterpriseDO.getCrmName());
        if (StringUtils.isEmpty(crmEnterpriseDO.getName())){
            throw new BusinessException(ResultCode.FAILED,"客户数据没有匹配到CRM的编码");
        }
        FlowCrmEnterpriseDTO dto = new FlowCrmEnterpriseDTO();
        dto.setCrmName(crmEnterpriseDO.getName());
        dto.setCrmCode(crmEnterpriseDO.getCode());
        dto.setCrmNumber(crmEnterpriseDO.getCrmNumber());
        return dto;
    }

    /**
     * 获取采购流向excel数据
     * @param eid
     * @param time
     * @return
     */
    private List<PurchaseFlowExcel> getPurchaseFlowExcelList(Long eid, Date time){
        List<PurchaseFlowExcel> purchaseFlowExcelList = Lists.newArrayList();
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setEidList(Arrays.asList(eid));

        // 起始时间为所选月份的起止时间
        request.setStartTime(DateUtil.beginOfMonth(time));
        request.setEndTime(DateUtil.endOfMonth(time));
        Page<FlowPurchaseDO> pageQurchase = null;
        int currentPurchase = 1;
        do {
            request.setCurrent(currentPurchase);
            request.setSize(500);
            pageQurchase = flowPurchaseService.page(request);

            if (pageQurchase == null || CollUtil.isEmpty(pageQurchase.getRecords())) {
                break;
            }
            for (FlowPurchaseDO e : pageQurchase.getRecords()) {
                PurchaseFlowExcel purchaseFlow = PojoUtils.map(e, PurchaseFlowExcel.class);
                purchaseFlow.setPoTime(DateUtil.formatDateTime(FlowCommonUtil.parseFlowDefaultTime(e.getPoTime())));
                purchaseFlowExcelList.add(purchaseFlow);
            }
            currentPurchase = currentPurchase + 1;
        } while (pageQurchase != null && CollUtil.isNotEmpty(pageQurchase.getRecords()));
        return purchaseFlowExcelList;
    }

    /**
     * 获取销售流向excel数据
     * @param eid
     * @param time
     * @return
     */
    private List<SaleFlowExcel> getSaleFlowExcelList(Long eid, Date time){
        List<SaleFlowExcel> saleFlowExcelList = Lists.newArrayList();
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setEidList(Arrays.asList(eid));

        // 起始时间为所选月份的起止时间
        request.setStartTime(DateUtil.beginOfMonth(time));
        request.setEndTime(DateUtil.endOfMonth(time));
        Page<FlowSaleDO> pageSale = null;
        int currentSale = 1;
        do {
            request.setCurrent(currentSale);
            request.setSize(500);
            pageSale = flowSaleService.page(request);

            if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                break;
            }

            for (FlowSaleDO e : pageSale.getRecords()) {
                SaleFlowExcel saleFlow = PojoUtils.map(e, SaleFlowExcel.class);
                saleFlow.setSoTime(DateUtil.formatDateTime(FlowCommonUtil.parseFlowDefaultTime(e.getSoTime())));
                saleFlowExcelList.add(saleFlow);
            }
            currentSale = currentSale + 1;
        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));
        return saleFlowExcelList;
    }

    /**
     * 获取商品库存流向excel数据
     * @param eid
     * @param time
     * @return
     */
    @Override
    public List<GoodsBatchFlowExcel> getGoodsBatchFlowExcelList(Long eid, Date time){
        List<GoodsBatchFlowExcel> goodsBatchFlowExcelList =  Lists.newArrayList();
        QueryFlowGoodsBatchListPageRequest requestGoodsBatch = new QueryFlowGoodsBatchListPageRequest();
        requestGoodsBatch.setEidList(Arrays.asList(eid));
        Page<FlowGoodsBatchDO> pageGoodsBatch = null;
        int currentGoodsBatch = 1;
        do {
            requestGoodsBatch.setCurrent(currentGoodsBatch);
            requestGoodsBatch.setSize(500);
            pageGoodsBatch = flowGoodsBatchService.page(requestGoodsBatch);

            if (pageGoodsBatch == null || CollUtil.isEmpty(pageGoodsBatch.getRecords())) {
                break;
            }

            for (FlowGoodsBatchDO e : pageGoodsBatch.getRecords()) {
                GoodsBatchFlowExcel goodsBatchFlow = new GoodsBatchFlowExcel();
                goodsBatchFlow.setGbName(e.getGbName());
                goodsBatchFlow.setGbManufacturer(e.getGbManufacturer());
                goodsBatchFlow.setGbBatchNo(e.getGbBatchNo());
                goodsBatchFlow.setGbNumber(e.getGbNumber().toPlainString());
                goodsBatchFlow.setGbSpecifications(e.getGbSpecifications());
                goodsBatchFlow.setGbUnit(e.getGbUnit());
                goodsBatchFlow.setGbTime(e.getGbTime());
                goodsBatchFlow.setDateTime(time);
                // 生产日期、有效期
                String gbProduceTime = e.getGbProduceTime();
                String gbEndTime = e.getGbEndTime();
                Date gbProduceTimeDate = StrUtil.isBlank(gbProduceTime) ? null : DateUtil.parse(gbProduceTime.trim());
                Date gbEndTimeDate = StrUtil.isBlank(gbEndTime) ? null : DateUtil.parse(gbEndTime.trim());
                goodsBatchFlow.setGbProduceTime(gbProduceTimeDate);
                goodsBatchFlow.setGbEndTime(gbEndTimeDate);

                goodsBatchFlowExcelList.add(goodsBatchFlow);
            }
            currentGoodsBatch = currentGoodsBatch + 1;
        } while (pageGoodsBatch != null && CollUtil.isNotEmpty(pageGoodsBatch.getRecords()));
        return goodsBatchFlowExcelList;
    }
}
