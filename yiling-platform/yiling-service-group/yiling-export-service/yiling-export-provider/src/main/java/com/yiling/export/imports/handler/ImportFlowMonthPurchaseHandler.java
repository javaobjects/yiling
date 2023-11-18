package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.yiling.dataflow.flowcollect.api.FlowMonthPurchaseApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthPurchaseRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.enums.ExcelErrorCodeEnum;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.model.ImportFlowMonthPurchaseModel;
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
 * 导入月流向采购数据验证处理器
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
@Slf4j
public class ImportFlowMonthPurchaseHandler extends AbstractImportReaderListener<ImportFlowMonthPurchaseModel> {

    private FlowMonthPurchaseApi flowMonthPurchaseApi;

    private FlowMonthUploadRecordApi flowMonthUploadRecordApi;

    private ExcelTaskRecordService excelTaskRecordService;

    @Override
    public void saveData(Map<String, Object> paramMap) {
        List<SaveFlowMonthPurchaseRequest> requests = ListUtil.toList();
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        Long recordId = (Long) paramMap.get(ImportConstants.TASK_RECORD_ID);
        boolean present = cachedDataList.stream().filter(c -> c.getStatus() == 1).findAny().isPresent();
        if(present){
            log.info("含有校验不通过数据，不保存数据库");
            return;
        }
        Long uploadRecordId = 0L;
        try {
            for (ImportFlowMonthPurchaseModel form : cachedDataList) {
                SaveFlowMonthPurchaseRequest request = PojoUtils.map(form, SaveFlowMonthPurchaseRequest.class);
                request.setPoTime(DateUtil.parse(form.getPoTimeStr(), "yyyy-MM-dd",
                        "yyyyMMdd","yyyy/MM/dd"));
                request.setPoQuantity(new BigDecimal(form.getPoQuantityStr()));
                request.setOpUserId(opUserId);
                requests.add(request);
            }

            // 插入月流向上传记录
            FlowMonthUploadRecordDTO uploadRecordDTO = Optional.ofNullable(flowMonthUploadRecordApi.getByRecordId(recordId)).orElse(new FlowMonthUploadRecordDTO());
            uploadRecordId=uploadRecordDTO.getId();

            // 数据入库
            Long finalUploadRecordId = uploadRecordId;
            requests.forEach(saveFlowMonthPurchaseRequest -> saveFlowMonthPurchaseRequest.setRecordId(finalUploadRecordId));

            List<List<SaveFlowMonthPurchaseRequest>> saveFlowMonthSalesRequestList = Lists.partition(requests, 2000);
            for(List<SaveFlowMonthPurchaseRequest> list:saveFlowMonthSalesRequestList){
                flowMonthPurchaseApi.saveBatch(list);
            }

            flowMonthPurchaseApi.updateFlowMonthPurchaseAndTask(0L,uploadRecordId);
        } catch (Exception e) {
            log.error("数据保存出错={}", e.getMessage(), e);
            // 如果保存数据出错了，那么更新为失败
            cachedDataList.forEach(g-> {
                g.setStatus(1);
                g.setErrorMsg(e.getMessage());
            });
            throw new BusinessException(ExcelErrorCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public EasyExcelVerifyHandlerResult verify(ImportFlowMonthPurchaseModel model) {
        {
            // 购进日期格式校验
            String poTimeStr = model.getPoTimeStr();
            if (StrUtil.isNotEmpty(poTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(poTimeStr, "yyyy-MM-dd",
                            "yyyyMMdd","yyyy/MM/dd");
                } catch (Exception e) {
                    return this.error("购进日期格式不正确","PoTimeStr",poTimeStr);
                }

            }
            // 数量
            String poQuantityStr = model.getPoQuantityStr();
            if (StrUtil.isNotEmpty(poQuantityStr)) {
                try {
                    BigDecimal poQuantity = new BigDecimal(poQuantityStr);
                } catch (Exception e) {
                    return this.error("数量（盒）格式不正确","PoQuantityStr",poQuantityStr);
                }
            }
            // 单价
            String poPrice = model.getPoPrice();
            if (StrUtil.isNotEmpty(poPrice)) {
                try {
                    BigDecimal price = new BigDecimal(poPrice);
                } catch (Exception e) {
                    return this.error("单价格式不正确","PoPrice",poPrice);
                }
            }
            // 金额
            String poTotalAmount = model.getPoTotalAmount();
            if (StrUtil.isNotEmpty(poTotalAmount)) {
                try {
                    BigDecimal totalAmount = new BigDecimal(poTotalAmount);
                } catch (Exception e) {
                    return this.error("金额格式不正确","PoTotalAmount",poTotalAmount);
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

    public FlowMonthPurchaseApi getFlowMonthPurchaseApi() {
        return flowMonthPurchaseApi;
    }

    public void setFlowMonthPurchaseApi(FlowMonthPurchaseApi flowMonthPurchaseApi) {
        this.flowMonthPurchaseApi = flowMonthPurchaseApi;
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
