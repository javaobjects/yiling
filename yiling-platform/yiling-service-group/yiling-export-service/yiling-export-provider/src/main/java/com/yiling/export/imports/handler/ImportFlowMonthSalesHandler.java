package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.yiling.dataflow.flowcollect.api.FlowMonthSalesApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthSalesRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.enums.ExcelErrorCodeEnum;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.model.ImportFlowMonthSalesModel;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入月流向销售数据验证处理器
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Slf4j
@Data
public class ImportFlowMonthSalesHandler extends AbstractImportReaderListener<ImportFlowMonthSalesModel> {

    private FlowMonthSalesApi flowMonthSalesApi;

    private FlowMonthUploadRecordApi flowMonthUploadRecordApi;

    private ExcelTaskRecordService excelTaskRecordService;

    @Override
    public EasyExcelVerifyHandlerResult verify(ImportFlowMonthSalesModel model) {
        {
            // 销售日期格式校验
            String soTimeStr = model.getSoTimeStr();
            if (StrUtil.isNotEmpty(soTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(soTimeStr, "yyyy-MM-dd",
                            "yyyyMMdd","yyyy/MM/dd");
                } catch (Exception e) {
                    return this.error("销售日期格式不正确", "soTimeStr", soTimeStr);
                }

            }
            // 数量
            String soQuantityStr = model.getSoQuantityStr();
            if (StrUtil.isNotEmpty(soQuantityStr)) {
                try {
                    BigDecimal soQuantity = new BigDecimal(soQuantityStr);
                } catch (Exception e) {
                    return this.error("数量（盒）格式不正确", "soQuantityStr", soQuantityStr);
                }
            }
            // 单价
            String soPrice = model.getSoPrice();
            if (StrUtil.isNotEmpty(soPrice)) {
                try {
                    BigDecimal price = new BigDecimal(soPrice);
                } catch (Exception e) {
                    return this.error("单价格式不正确", "soPrice", soPrice);
                }
            }
            // 金额
            String soTotalAmount = model.getSoTotalAmount();
            if (StrUtil.isNotEmpty(soTotalAmount)) {
                try {
                    BigDecimal totalAmount = new BigDecimal(soTotalAmount);
                } catch (Exception e) {
                    return this.error("金额格式不正确", "soTotalAmount", soTotalAmount);
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
        UpdateFlowMonthUploadRecordRequest request = new UpdateFlowMonthUploadRecordRequest();
        request.setId(recordId);
        request.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
        request.setImportStatus(FlowMonthUploadImportStatusEnum.SUCCESS.getCode());
        request.setOpUserId(opUserId);
         flowMonthUploadRecordApi.updateFlowMonthRecord(request);
    }

    /**
     * 更新失败状态和原因
     *
     * @param uploadRecordId
     * @param opUserId
     */
    public void updateFailStatus(Long uploadRecordId, Long opUserId) {
        UpdateFlowMonthUploadRecordRequest request = new UpdateFlowMonthUploadRecordRequest();
        request.setId(uploadRecordId);
        request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
        request.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
        request.setFailReason("系统异常数据写入失败");
        request.setOpUserId(opUserId);
        flowMonthUploadRecordApi.updateFlowMonthRecord(request);
    }

    @Override
    public void saveData(Map<String, Object> paramMap) {
        List<SaveFlowMonthSalesRequest> requests = ListUtil.toList();
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        Long recordId = (Long) paramMap.get(ImportConstants.TASK_RECORD_ID);
        boolean present = cachedDataList.stream().filter(c -> c.getStatus() == 1).findAny().isPresent();
        if(present){
            log.info("含有校验不通过数据，不保存数据库");
            return;
        }
        Long uploadRecordId = 0L;
        try {
            for (ImportFlowMonthSalesModel form : cachedDataList) {
                // 保存上传数据记录
                SaveFlowMonthSalesRequest request = PojoUtils.map(form, SaveFlowMonthSalesRequest.class);
                request.setSoTime(DateUtil.parse(form.getSoTimeStr(), "yyyy-MM-dd",
                        "yyyyMMdd","yyyy/MM/dd"));
                request.setSoQuantity(new BigDecimal(form.getSoQuantityStr()));
                request.setOpUserId(opUserId);
                requests.add(request);
            }

            // 插入月流向上传记录
            FlowMonthUploadRecordDTO uploadRecordDTO = Optional.ofNullable(flowMonthUploadRecordApi.getByRecordId(recordId)).orElse(new FlowMonthUploadRecordDTO());
            uploadRecordId = uploadRecordDTO.getId();
            // 数据入库
            Long finalUploadRecordId = uploadRecordId;
            requests.forEach(saveFlowMonthSalesRequest -> saveFlowMonthSalesRequest.setRecordId(finalUploadRecordId));

            List<List<SaveFlowMonthSalesRequest>> saveFlowMonthSalesRequestList = Lists.partition(requests, 2000);
            for (List<SaveFlowMonthSalesRequest> list : saveFlowMonthSalesRequestList) {
                flowMonthSalesApi.saveBatch(list);
            }
            flowMonthSalesApi.updateFlowMonthSalesAndTask(0L, uploadRecordId);
        } catch (Exception e) {
            log.error("数据保存出错={}", e.getMessage(), e);
            cachedDataList.forEach(g -> {
                g.setStatus(1);
                g.setErrorMsg(e.getMessage());
            });
            throw new BusinessException(ExcelErrorCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public boolean isAfterSave() {
        return true;
    }
}
