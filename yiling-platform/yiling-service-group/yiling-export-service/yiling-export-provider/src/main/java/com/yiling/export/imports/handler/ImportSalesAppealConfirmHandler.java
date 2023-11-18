package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.yiling.dataflow.flowcollect.api.SalesAppealConfirmApi;
import com.yiling.dataflow.flowcollect.dto.request.SaveSalesAppealDateRequest;
import com.yiling.export.excel.enums.ExcelErrorCodeEnum;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.model.ImportSalesAppealConfirmModel;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入销量申诉的入库处理器
 *
 * @author: shixing.sun
 * @date: 2023-03-15
 */
@Slf4j
@Data
public class ImportSalesAppealConfirmHandler extends AbstractImportReaderListener<ImportSalesAppealConfirmModel> {
    SalesAppealConfirmApi  salesAppealConfirmApi;
    SaleaAppealFormApi     saleaAppealFormApi;
    ExcelTaskRecordService excelTaskRecordService;

    @Override
    protected EasyExcelVerifyHandlerResult verify(ImportSalesAppealConfirmModel model) {

        {
            // 库存提取日期格式校验
            String soTimeStr = model.getSoTime();
            if (StrUtil.isNotEmpty(soTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(soTimeStr, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("库存提取日期格式不正确", "soTimeStr", soTimeStr);
                }
            }
        }
        return null;
    }


    /**
     * 校验成功才会入库
     *
     * @param paramMap
     * @return
     */
    @Override
    public void saveData(Map<String, Object> paramMap) {
        List<SaveSalesAppealDateRequest> requests = ListUtil.toList();
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        Long recordId = (Long) paramMap.get(ImportConstants.TASK_RECORD_ID);

        for (ImportSalesAppealConfirmModel form : cachedDataList) {
            SaveSalesAppealDateRequest request = PojoUtils.map(form, SaveSalesAppealDateRequest.class);
            request.setOpUserId(opUserId);
            requests.add(request);
        }

        try {
            // 插入月流向上传记录
            SalesAppealFormDTO salesAppealFormDTO = saleaAppealFormApi.getByTaskId(recordId);
            // 数据入库
            requests.forEach(item -> item.setRecordId(salesAppealFormDTO.getId()));
            List<List<SaveSalesAppealDateRequest>> saveList = Lists.partition(requests, 1000);
            for (List<SaveSalesAppealDateRequest> goodsRequestList : saveList) {
                salesAppealConfirmApi.saveSalesAppealConfirmDate(goodsRequestList);
            }
        } catch (Exception e) {
            log.error("数据保存出错：{}", e.getMessage(), e);
            cachedDataList.forEach(g -> {
                g.setStatus(1);
                g.setErrorMsg(e.getMessage());
            });
            throw new BusinessException(ExcelErrorCodeEnum.DATA_ERROR);
        }
        log.info("ImportSalesAppealConfirmHandler execute end");
    }
}
