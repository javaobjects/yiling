package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.yiling.dataflow.flowcollect.api.FlowMonthInventoryApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthInventoryRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.enums.ExcelErrorCodeEnum;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.model.ImportFlowMonthInventoryModel;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入月流向库存数据验证处理器
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
@Slf4j
public class ImportFlowMonthInventoryHandler extends AbstractImportReaderListener<ImportFlowMonthInventoryModel> {

    private FlowMonthInventoryApi flowMonthInventoryApi;

    private FlowMonthUploadRecordApi flowMonthUploadRecordApi;

    private ExcelTaskRecordService excelTaskRecordService;

    @Override
    public void saveData(Map<String, Object> paramMap) {
        List<SaveFlowMonthInventoryRequest> requests = ListUtil.toList();
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        Long recordId = (Long) paramMap.get(ImportConstants.TASK_RECORD_ID);
        boolean present = cachedDataList.stream().filter(c -> c.getStatus() == 1).findAny().isPresent();
        if(present){
            log.info("含有校验不通过数据，不保存数据库");
            return;
        }
        Long uploadRecordId = 0L;
        try {
            for (ImportFlowMonthInventoryModel form : cachedDataList) {
                SaveFlowMonthInventoryRequest request = PojoUtils.map(form, SaveFlowMonthInventoryRequest.class);
                if (StrUtil.isNotEmpty(form.getGbTimeStr())) {
                    request.setGbTime(DateUtil.parse(form.getGbTimeStr(), "yyyy-MM-dd",
                            "yyyyMMdd","yyyy/MM/dd"));
                }
                if (StrUtil.isNotEmpty(form.getGbExtractTimeStr())) {
                    request.setGbExtractTime(DateUtil.parseDate(form.getGbExtractTimeStr()));
                }
                request.setGbNumber(new BigDecimal(form.getGbNumberStr()));
                request.setOpUserId(opUserId);
                requests.add(request);
            }

            // 插入月流向上传记录
            FlowMonthUploadRecordDTO uploadRecordDTO = Optional.ofNullable(flowMonthUploadRecordApi.getByRecordId(recordId)).orElse(new FlowMonthUploadRecordDTO());
            uploadRecordId=uploadRecordDTO.getId();
            // 数据入库
            Long finalUploadRecordId = uploadRecordId;
            requests.forEach(saveFlowMonthSalesRequest -> saveFlowMonthSalesRequest.setRecordId(finalUploadRecordId));


            List<List<SaveFlowMonthInventoryRequest>> saveFlowMonthSalesRequestList = Lists.partition(requests, 2000);
            for(List<SaveFlowMonthInventoryRequest> list:saveFlowMonthSalesRequestList){
                flowMonthInventoryApi.saveBatch(list);
            }

            flowMonthInventoryApi.updateFlowMonthInventoryAndTask(0L,uploadRecordId);
        } catch (Exception e) {
            log.error("数据保存出错={}", e.getMessage(), e);
            // 如果保存数据出错了，那么更新为失败
            // 如果保存数据出错了，那么更新为失败
            cachedDataList.forEach(g-> {
                g.setStatus(1);
                g.setErrorMsg(e.getMessage());
            });
            throw new BusinessException(ExcelErrorCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public EasyExcelVerifyHandlerResult verify(ImportFlowMonthInventoryModel model) {
        {
            // 库存提取日期格式校验
            String soTimeStr = model.getGbExtractTimeStr();
            if (StrUtil.isNotEmpty(soTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(soTimeStr, "yyyy-MM-dd",
                            "yyyyMMdd","yyyy/MM/dd");
                } catch (Exception e) {
                    return this.error("库存提取日期格式不正确","GbExtractTimeStr",soTimeStr);
                }

            }

            // 数量
            String gbNumberStr = model.getGbNumberStr();
            if (StrUtil.isNotEmpty(gbNumberStr)) {
                try {
                    BigDecimal gbNumber = new BigDecimal(gbNumberStr);
                } catch (Exception e) {
                    return this.error("数量（盒）格式不正确","GbNumberStr",gbNumberStr);
                }
            }

            // 入库日期格式校验
            String gbTimeStr = model.getGbTimeStr();
            if (StrUtil.isNotEmpty(gbTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(gbTimeStr, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("入库日期格式不正确","GbTimeStr",gbTimeStr);
                }

            }

            // 生产日期格式校验
            String gbProduceTime = model.getGbProduceTime();
            if (StrUtil.isNotEmpty(gbProduceTime)) {
                try {
                    DateTime dateTime = DateUtil.parse(gbProduceTime, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("生产日期格式不正确","GbProduceTime",gbProduceTime);
                }

            }

            // 有效期格式校验
            String gbEndTime = model.getGbEndTime();
            if (StrUtil.isNotEmpty(gbEndTime)) {
                try {
                    DateTime dateTime = DateUtil.parse(gbEndTime, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("有效期格式不正确","GbEndTime",gbEndTime);
                }

            }

            // 单价
            String gbPrice = model.getGbPrice();
            if (StrUtil.isNotEmpty(gbPrice)) {
                try {
                    BigDecimal price = new BigDecimal(gbPrice);
                } catch (Exception e) {
                    return this.error("单价格式不正确","GbPrice",gbPrice);
                }
            }
            // 金额
            String gbTotalAmount = model.getGbTotalAmount();
            if (StrUtil.isNotEmpty(gbTotalAmount)) {
                try {
                    BigDecimal totalAmount = new BigDecimal(gbTotalAmount);
                } catch (Exception e) {
                    return this.error("金额格式不正确","GbTotalAmount",gbTotalAmount);
                }
            }
        }
        return null;
    }

    /**
     * 插入月流向上传记录
     *
     * @param opUserId
     * @param recordId
     * @return
     */
    public void updateSuccess(Long opUserId, Long recordId) {
        UpdateFlowMonthUploadRecordRequest recordRequest = new UpdateFlowMonthUploadRecordRequest();
        recordRequest.setId(recordId);
        recordRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
        recordRequest.setImportStatus(FlowMonthUploadImportStatusEnum.SUCCESS.getCode());
        recordRequest.setOpUserId(opUserId);
         flowMonthUploadRecordApi.updateFlowMonthRecord(recordRequest);
    }

    /**
     * 更新失败状态和原因
     *
     * @param uploadRecordId
     * @param opUserId
     */
    public void updateFailStatus(Long uploadRecordId, Long opUserId) {
        // 如果保存数据出错了，那么更新为失败
        UpdateFlowMonthUploadRecordRequest request = new UpdateFlowMonthUploadRecordRequest();
        request.setId(uploadRecordId);
        request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
        request.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
        request.setFailReason("系统异常数据写入失败");
        request.setOpUserId(opUserId);
        flowMonthUploadRecordApi.updateFlowMonthRecord(request);
    }

    public FlowMonthInventoryApi getFlowMonthInventoryApi() {
        return flowMonthInventoryApi;
    }

    public void setFlowMonthInventoryApi(FlowMonthInventoryApi flowMonthInventoryApi) {
        this.flowMonthInventoryApi = flowMonthInventoryApi;
    }

    public FlowMonthUploadRecordApi getFlowMonthUploadRecordApi() {
        return flowMonthUploadRecordApi;
    }

    public void setFlowMonthUploadRecordApi(FlowMonthUploadRecordApi flowMonthUploadRecordApi) {
        this.flowMonthUploadRecordApi = flowMonthUploadRecordApi;
    }

    public ExcelTaskRecordService getExcelTaskRecordService() {
        return excelTaskRecordService;
    }

    public void setExcelTaskRecordService(ExcelTaskRecordService excelTaskRecordService) {
        this.excelTaskRecordService = excelTaskRecordService;
    }
}
