package com.yiling.export.export.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.flow.api.FlowMonthBiTaskApi;
import com.yiling.dataflow.flow.dto.FlowCrmEnterpriseDTO;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.flow.excel.GoodsBatchFlowExcel;
import com.yiling.dataflow.flow.excel.PurchaseFlowExcel;
import com.yiling.dataflow.flow.excel.SaleFlowExcel;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowMonthBiTaskExportServiceImpl
 * @描述
 * @创建时间 2022/7/11
 * @修改人 shichen
 * @修改时间 2022/7/11
 **/
@Service("flowMonthBiTaskExportService")
@Slf4j
public class FlowMonthBiTaskExportServiceImpl implements BaseExportQueryDataService<FlowMonthBiTaskRequest> {

    @DubboReference(timeout = 5 * 60 * 1000)
    private FlowMonthBiTaskApi flowMonthBiTaskApi;
    @DubboReference(timeout = 10 * 1000)
    private ErpClientApi erpClientApi;

    private static final LinkedHashMap<String, Class<?>> EXCEL_CLASS = new LinkedHashMap<>();

    static {
        EXCEL_CLASS.put("PD", PurchaseFlowExcel.class);
        EXCEL_CLASS.put("SD", SaleFlowExcel.class);
        EXCEL_CLASS.put("ID", GoodsBatchFlowExcel.class);
    }

    @Override
    public QueryExportDataDTO queryData(FlowMonthBiTaskRequest flowMonthBiTaskRequest) {
        return null;
    }

    @Override
    public FlowMonthBiTaskRequest getParam(Map<String, Object> map) {
        if (ObjectUtil.isNull(map.get("eid")) || ObjectUtil.isNull(map.get("parentFlag")) || ObjectUtil.isNull(map.get("queryDate"))) {
            throw new BusinessException(ResultCode.PARAM_MISS, "企业id、导出公司、导出月份都不能为空");
        }

        String eid = map.get("eid").toString();
        String parentFlag = map.get("parentFlag").toString();
        String queryDate = map.get("queryDate").toString();
        FlowMonthBiTaskRequest request = new FlowMonthBiTaskRequest();
        request.setEid(Long.parseLong(eid));
        request.setParentFlag(Integer.parseInt(parentFlag));
        request.setQueryDate(DateUtil.parse(queryDate, "yyyy-MM"));
        return request;
    }

    @Override
    public String getFileSuffix() {
        return "zip";
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(FlowMonthBiTaskRequest flowMonthBiTaskRequest, String fileName) {

        Long eidRequest = flowMonthBiTaskRequest.getEid();
        ErpClientDTO erpClientDTO = getErpClientList(ListUtil.toList(eidRequest)).get(0);
        // 当前公司，获取企业crm信息
        flowMonthBiTaskRequest.setCrmEnterpriseId(erpClientDTO.getCrmEnterpriseId());
        FlowCrmEnterpriseDTO crmInfo = flowMonthBiTaskApi.getCrmEnterpriseInfo(flowMonthBiTaskRequest);
        // 当前公司，导出月份是否有心跳
        checkErpClientHeartTime(erpClientDTO, flowMonthBiTaskRequest.getQueryDate());
        // 导出月份，最小为前推6个月
        Date queryDate = flowMonthBiTaskRequest.getQueryDate();
        Date now = new Date();
        DateTime startDateTime = DateUtil.beginOfMonth(DateUtil.offsetMonth(now, -6));
        if (queryDate.getTime() < startDateTime.getTime()) {
            String month = DateUtil.format(startDateTime, "yyyy-MM");
            throw new BusinessException(ResultCode.FAILED, "最小月份不能小于" + month);
        }
        // 待导出的公司id，当前公司、或总公司及所有部门公司
        List<Long> eidList = buildEidList(flowMonthBiTaskRequest, erpClientDTO);
        if (CollUtil.isEmpty(eidList)) {
            throw new BusinessException(ResultCode.FAILED, "该企业未对接，或已关闭对接");
        }
        // 导出临时目录
        Long exportEid = eidRequest;
        if (eidList.size() > 1) {
            exportEid = erpClientDTO.getSuId();
        }
        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "FlowMonthBiTaskExport" + File.separator + exportEid;
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "excel");
        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }

        boolean exportFlag = doExport(flowMonthBiTaskRequest, eidRequest, crmInfo, eidList, queryDate, tmpExcelDir);
        if (!exportFlag) {
            return null;
        }

        if(FileUtil.isDirEmpty(tmpExcelDir)){
            throw new BusinessException(ResultCode.FAILED, "导出数据流为空");
        }

        try {
            File zipFile = ZipUtil.zip(tmpExcelDir);
            if (null != zipFile) {
                return FileUtil.readBytes(zipFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(tmpDirPath);
        }
        return null;
    }

    private boolean doExport(FlowMonthBiTaskRequest flowMonthBiTaskRequest, Long eidRequest, FlowCrmEnterpriseDTO crmInfo, List<Long> eidList, Date queryDate, File tmpExcelDir) {
        if (CollUtil.isEmpty(eidList)) {
            return false;
        }

        Map<Long, ErpClientDTO> erpClientMap = new HashMap<>();
                List<ErpClientDTO> erpClientList = getErpClientList(eidList);
        if (CollUtil.isNotEmpty(erpClientList)) {
            erpClientMap = erpClientList.stream().collect(Collectors.toMap(o -> o.getRkSuId(), o -> o, (k1, k2) -> k1));
        }

        for (Long eid : eidList) {
            flowMonthBiTaskRequest.setEid(eid);

            FlowCrmEnterpriseDTO crmInfoCurrent = null;
            if (ObjectUtil.equal(eid, eidRequest)) {
                crmInfoCurrent = crmInfo;
            } else {
                try {
                    ErpClientDTO erpClientDTO = erpClientMap.get(eid);
                    if (ObjectUtil.isEmpty(erpClientDTO)) {
                        throw new BusinessException(ResultCode.FAILED, "该企业未对接");
                    }
                    // 导出月份是否有心跳
                    checkErpClientHeartTime(erpClientDTO, flowMonthBiTaskRequest.getQueryDate());
                    // 当前公司，获取企业crm信息
                    flowMonthBiTaskRequest.setCrmEnterpriseId(erpClientDTO.getCrmEnterpriseId());
                    crmInfoCurrent = flowMonthBiTaskApi.getCrmEnterpriseInfo(flowMonthBiTaskRequest);
                }catch (BusinessException e){
                    log.info("总公司导出分公司流向分公司数据异常");
                }
            }

            if(crmInfoCurrent==null){
                continue;
            }
            HashMap<String, List> map = flowMonthBiTaskApi.getAllFlowMonthBiData(flowMonthBiTaskRequest);
            if (map.isEmpty()) {
                continue;
            }

            CsvExportParams exportParams = new CsvExportParams();
            exportParams.setCreateHeadRows(true);
            exportParams.setEncoding("GBK");

            // Excel文件名称格式：SD/ID/PD_dems编码_商业名称_20230401_YL
            for (Map.Entry<String, List> dataEntry : map.entrySet()) {
//                String fileDay = DateUtil.format(DateUtil.endOfMonth(queryDate), "yyyyMMdd");
                DateTime nextMonth = DateUtil.offsetMonth(queryDate, 1);
                String fileDay = DateUtil.format(DateUtil.beginOfMonth(nextMonth), "yyyyMMdd");
                String fileName = dataEntry.getKey() + "_" + crmInfoCurrent.getCrmNumber() + "_" + crmInfoCurrent.getCrmName() + "_" + fileDay + "_YL" + ".csv";
                exportCsvExcel(exportParams, tmpExcelDir.getPath(), fileName, dataEntry.getValue(), EXCEL_CLASS.get(dataEntry.getKey()));
            }
        }
        return true;
    }

    private List<Long> buildEidList(FlowMonthBiTaskRequest flowMonthBiTaskRequest, ErpClientDTO erpClientDTO) {
        List<Long> eidList = new ArrayList<>();
        if (ObjectUtil.equal(flowMonthBiTaskRequest.getParentFlag(), 1)) {
            if(ObjectUtil.equal(erpClientDTO.getClientStatus(), 1) && ObjectUtil.equal(erpClientDTO.getSyncStatus(), 1)){
                eidList.add(flowMonthBiTaskRequest.getEid());
            }
        } else {
            Long suId = erpClientDTO.getSuId();
            List<ErpClientDTO> erpClientList = erpClientApi.selectBySuId(suId);
            if (CollUtil.isEmpty(erpClientList)) {
                throw new BusinessException(ResultCode.FAILED, "该企业未对接");
            }
            List<Long> rksuIdList = erpClientList.stream().filter(o -> ObjectUtil.equal(o.getClientStatus(), 1) && ObjectUtil.equal(o.getSyncStatus(), 1))
                    .map(ErpClientDTO::getRkSuId).distinct().collect(Collectors.toList());
            eidList.addAll(rksuIdList);
        }
        return eidList;
    }

    private void exportCsvExcel(CsvExportParams exportParams, String tmpDirPath, String fileName, List dataList, Class<?> pojoClass) {
        FileOutputStream fileOutputStream = null;
        try {
            File excelDir = FileUtil.newFile(tmpDirPath + File.separator + fileName);
            if (excelDir.exists()) {
                FileUtil.del(excelDir);
            }
            excelDir.createNewFile();
            fileOutputStream = new FileOutputStream(excelDir);
            CsvExportUtil.exportCsv(exportParams, pojoClass, dataList, fileOutputStream);
        } catch (Exception e) {
            log.error("生成csvExcel文件失败", e);
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(fileOutputStream);
        }

    }

    private void checkErpClientHeartTime(ErpClientDTO erpClientDTO, Date queryDate) {
        DateTime queryTime = DateUtil.beginOfMonth(queryDate);
        String month = DateUtil.format(queryDate, "yyyy-MM");
        Date heartBeatTime = erpClientDTO.getHeartBeatTime();
        if(ObjectUtil.isNull(heartBeatTime)){
            throw new BusinessException(ResultCode.FAILED, "该企业的心跳时间为空");
        }
        if (heartBeatTime.getTime() < queryTime.getTime()) {
            throw new BusinessException(ResultCode.FAILED, "该企业的心跳时间小于导出月份"+ month);
        }
    }

    private List<ErpClientDTO> getErpClientList(List<Long> eidList){
        List<ErpClientDTO> erpClientDTOList = erpClientApi.selectByRkSuIdList(eidList);
        if (CollUtil.isEmpty(erpClientDTOList)) {
            throw new BusinessException(ResultCode.FAILED, "该企业未对接");
        }
        return erpClientDTOList;
    }
}
