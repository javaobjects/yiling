package com.yiling.admin.data.center.report.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.report.form.ImportOrderIdentForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.settlement.report.api.ReportOrderSyncApi;
import com.yiling.settlement.report.dto.ReportB2bOrderSyncDTO;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.settlement.report.enums.ReportOrderIdenEnum;
import com.yiling.settlement.report.enums.ReportOrderaAnormalReasonEnum;
import com.yiling.settlement.report.enums.ReportTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入参数处理器
 *
 * @author: dexi.yao
 * @date: 2022/07/14
 */
@Slf4j
@Component
public class ImportOrderIdentHandler implements IExcelVerifyHandler<ImportOrderIdentForm>, ImportDataHandler<ImportOrderIdentForm> {

    @DubboReference
    ReportOrderSyncApi reportOrderSyncApi;


    private ReportTypeEnum reportTypeEnum;

    public void initPar(ReportTypeEnum reportTypeEnum) {
        this.reportTypeEnum = reportTypeEnum;
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportOrderIdentForm form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        try {
            //校验参数必填
            if (ObjectUtil.equal(ReportOrderIdenEnum.ABNORMAL.getCode(), form.getIdentificationStatus())) {
                if (ObjectUtil.isNull(form.getAbnormalReason()) || ObjectUtil.equal(form.getAbnormalReason(), 0)) {
                    result.setSuccess(false);
                    result.setMsg("请手动填写异常情况");
                    return result;
                }
                if (ObjectUtil.equal(ReportOrderaAnormalReasonEnum.OTHER.getCode(), form.getAbnormalReason()) && StrUtil.isBlank(form.getAbnormalDescribed())) {
                    result.setSuccess(false);
                    result.setMsg("请手动填写异常原因");
                    return result;
                }
            }
            //校验记录否存在
            {
                if (ObjectUtil.equal(reportTypeEnum.getCode(), ReportTypeEnum.B2B.getCode())) {
                    ReportB2bOrderSyncDTO syncDTO = reportOrderSyncApi.queryB2bOrderSyncById(form.getId());
                    if (ObjectUtil.isNull(syncDTO)) {
                        result.setSuccess(false);
                        result.setMsg("订单不存在");
                        return result;
                    }
                }
                if (ObjectUtil.equal(reportTypeEnum.getCode(), ReportTypeEnum.FLOW.getCode())) {
                    ReportFlowSaleOrderSyncDTO syncDTO = reportOrderSyncApi.queryFlowOrderSyncById(form.getId());
                    if (ObjectUtil.isNull(syncDTO)) {
                        result.setSuccess(false);
                        result.setMsg("订单不存在");
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
    public List<ImportOrderIdentForm> execute(List<ImportOrderIdentForm> object, Map<String, Object> paramMap) {
        for (ImportOrderIdentForm form : object) {
            Boolean isSucceed = Boolean.FALSE;
            try {
                if (ObjectUtil.equal(reportTypeEnum.getCode(), ReportTypeEnum.B2B.getCode())) {
                    UpdateB2bOrderIdenRequest request = new UpdateB2bOrderIdenRequest();
                    request.setUpdateIdenStatus(form.getIdentificationStatus());
                    request.setAbnormalReason(form.getAbnormalReason());
                    request.setAbnormalDescribed(form.getAbnormalDescribed());
                    request.setOpUserId((Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID));
                    request.setOpTime(new Date());
                    request.setIdList(ListUtil.toList(form.getId()));
                    isSucceed = reportOrderSyncApi.updateB2bOrderIdentification(request);
                }
                if (ObjectUtil.equal(reportTypeEnum.getCode(), ReportTypeEnum.FLOW.getCode())) {
                    UpdateFlowOrderIdenRequest request = new UpdateFlowOrderIdenRequest();
                    request.setUpdateIdenStatus(form.getIdentificationStatus());
                    request.setAbnormalReason(form.getAbnormalReason());
                    request.setAbnormalDescribed(form.getAbnormalDescribed());
                    request.setOpUserId((Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID));
                    request.setOpTime(new Date());
                    request.setIdList(ListUtil.toList(form.getId()));
                    isSucceed = reportOrderSyncApi.updateFlowOrderIdentification(request);
                }
                if (!isSucceed) {
                    form.setErrorMsg("更新标识失败，请重试");
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
