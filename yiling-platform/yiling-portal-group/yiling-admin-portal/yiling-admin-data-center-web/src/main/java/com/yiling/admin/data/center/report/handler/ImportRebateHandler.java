package com.yiling.admin.data.center.report.handler;

import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.report.form.ImportRebateForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.request.RebateByReportRequest;
import com.yiling.settlement.report.enums.ReportRebateStatusEnum;
import com.yiling.settlement.report.enums.ReportTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入参数处理器
 *
 * @author: dexi.yao
 * @date: 2022/07/14
 */
@Slf4j
@Component
public class ImportRebateHandler implements IExcelVerifyHandler<ImportRebateForm>, ImportDataHandler<ImportRebateForm> {


    @DubboReference
    ReportApi reportApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportRebateForm form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        try {
            ReportDTO reportDTO;
            //校验报表是否存在
            {
                if (ObjectUtil.isNotNull(form.getReportId())) {
                    reportDTO = reportApi.queryReportById(form.getReportId());
                    if (ObjectUtil.isNull(reportDTO)) {
                        result.setSuccess(false);
                        result.setMsg("报表不存在");
                        return result;
                    }
                    if (ObjectUtil.equal(reportDTO.getRebateStatus(), ReportRebateStatusEnum.REBATED.getCode())) {
                        result.setSuccess(false);
                        result.setMsg("报表状态为已返利，不能再标识返利");
                        return result;
                    }
                } else {
                    result.setSuccess(false);
                    result.setMsg("报表id为空");
                    return result;
                }
            }
            //校验起止时间
            {
                if (ObjectUtil.equal(reportDTO.getType(), ReportTypeEnum.B2B.getCode())) {
                    ReportDetailB2bDTO detailB2bDTO = reportApi.queryB2bReportDetailById(form.getReportDetailId());
                    if (ObjectUtil.isNull(detailB2bDTO)) {
                        result.setSuccess(false);
                        result.setMsg("报表明细不存在");
                        return result;
                    }
                    if (ObjectUtil.equal(detailB2bDTO.getRebateStatus(), ReportRebateStatusEnum.REBATED.getCode())) {
                        result.setSuccess(false);
                        result.setMsg("该条记录状态为已返利");
                        return result;
                    }
                }
                if (ObjectUtil.equal(reportDTO.getType(), ReportTypeEnum.FLOW.getCode())) {
                    ReportDetailFlowDTO detailFlowDTO = reportApi.queryFlowReportDetailById(form.getReportDetailId());
                    if (ObjectUtil.isNull(detailFlowDTO)) {
                        result.setSuccess(false);
                        result.setMsg("报表明细不存在");
                        return result;
                    }
                    if (ObjectUtil.equal(detailFlowDTO.getRebateStatus(), ReportRebateStatusEnum.REBATED.getCode())) {
                        result.setSuccess(false);
                        result.setMsg("该条记录状态为已返利");
                        return result;
                    }
                }
            }
        } catch (BusinessException e) {
            form.setErrorMsg(e.getMessage());
            log.error("供应商商品导入数据库报错", e);
        } catch (Exception e) {
            form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
            log.error("数据保存出错：{}", e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<ImportRebateForm> execute(List<ImportRebateForm> object, Map<String, Object> paramMap) {
        for (ImportRebateForm form : object) {
            RebateByReportRequest request = new RebateByReportRequest();
            request.setReportId(form.getReportId());
            request.setDetailIdList(ListUtil.toList(form.getReportDetailId()));
            request.setOpUserId((Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID));

            try {
                Boolean isSucceed = reportApi.rebateByReport(request);
                if (!isSucceed) {
                    form.setErrorMsg("标识返利失败，请重试");
                    continue;
                }
            } catch (BusinessException be) {
                form.setErrorMsg(be.getMessage());
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }

}
