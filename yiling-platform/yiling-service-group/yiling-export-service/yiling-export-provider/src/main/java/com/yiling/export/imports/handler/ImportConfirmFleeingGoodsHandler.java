package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.yiling.dataflow.flowcollect.api.FlowFleeingGoodsApi;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowFleeingGoodsRequest;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.export.excel.enums.ExcelErrorCodeEnum;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.model.ImportConfirmFleeingGoodsModel;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.request.UpdateFleeingGoodsFormRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 窜货申报确认上传
 *
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Slf4j
@Data
public class ImportConfirmFleeingGoodsHandler extends AbstractImportReaderListener<ImportConfirmFleeingGoodsModel> {

    private FleeingGoodsFormApi fleeingGoodsFormApi;

    private FlowFleeingGoodsApi flowFleeingGoodsApi;

    private ExcelTaskRecordService excelTaskRecordService;

    @Override
    protected EasyExcelVerifyHandlerResult verify(ImportConfirmFleeingGoodsModel model) {
        {
            // 销售时间格式校验
            String soTimeStr = model.getSoTimeStr();
            if (StrUtil.isNotEmpty(soTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(soTimeStr, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("销售时间格式不正确", "soTimeStr", soTimeStr);
                }

            }
            // 数量
            String soQuantityStr = model.getSoQuantity();
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

    @Override
    public void saveData(Map<String, Object> paramMap) {
        List<SaveFlowFleeingGoodsRequest> requestList = ListUtil.toList();
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        Long recordId = (Long) paramMap.get(ImportConstants.TASK_RECORD_ID);
        log.info("ImportConfirmFleeingGoodsHandler execute start recordId:[{}]", recordId);

        for (ImportConfirmFleeingGoodsModel form : cachedDataList) {
            SaveFlowFleeingGoodsRequest request = PojoUtils.map(form, SaveFlowFleeingGoodsRequest.class);
            if (StrUtil.isNotEmpty(form.getSoTimeStr())) {
                request.setSoTime(DateUtil.parseDate(form.getSoTimeStr()));
            }
            request.setOpUserId(opUserId);
            requestList.add(request);
        }

        try {
            // 插入月流向上传记录
            FleeingGoodsFormDTO fleeingGoodsFormDTO = fleeingGoodsFormApi.getByTaskId(recordId);
            requestList.forEach(e -> e.setRecordId(fleeingGoodsFormDTO.getId()));
            List<List<SaveFlowFleeingGoodsRequest>> saveList = Lists.partition(requestList, 1000);
            for (List<SaveFlowFleeingGoodsRequest> goodsRequestList : saveList) {
                flowFleeingGoodsApi.saveFlowFleeingGoodsAndTask(goodsRequestList);
            }
        } catch (Exception e) {
            log.error("数据保存出错：", e);
            cachedDataList.forEach(g -> {
                g.setStatus(1);
                g.setErrorMsg(e.getMessage());
            });
            throw new BusinessException(ExcelErrorCodeEnum.DATA_ERROR);
        }
        log.info("ImportConfirmFleeingGoodsHandler execute end");
    }

}
