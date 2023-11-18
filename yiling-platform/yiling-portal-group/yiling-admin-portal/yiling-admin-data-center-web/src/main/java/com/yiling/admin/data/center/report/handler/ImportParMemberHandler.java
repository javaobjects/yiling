package com.yiling.admin.data.center.report.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.report.form.ImportParMemberForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.request.AddReportSubParamRequest;
import com.yiling.settlement.report.enums.ReportParamTypeEnum;
import com.yiling.settlement.report.enums.ReportRewardTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.date.DateUtil;
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
public class ImportParMemberHandler implements IExcelVerifyHandler<ImportParMemberForm>, ImportDataHandler<ImportParMemberForm> {


    @DubboReference
    ReportParamApi reportParamApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private Long paramId;
    private ReportParamTypeEnum paramTypeEnum;

    public void initPar(Long paramId, ReportParamTypeEnum paramTypeEnum) {
        this.paramId = paramId;
        this.paramTypeEnum = paramTypeEnum;
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportParMemberForm form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        try {
            //校验企业是否存在
            {
                if (ObjectUtil.isNotNull(form.getEid())) {
                    EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
                    if (ObjectUtil.isNull(enterpriseDTO)) {
                        result.setSuccess(false);
                        result.setMsg("企业不存在");
                        return result;
                    }
                }
            }
            //校验起止时间
            {
                if (StrUtil.isNotBlank(form.getStartTime()) && StrUtil.isNotBlank(form.getEndTime())) {
                    if (DateUtil.compare(DateUtil.parseDate(form.getStartTime()), DateUtil.parseDate(form.getEndTime())) >= 0) {
                        result.setSuccess(false);
                        result.setMsg("开始时间不能小于结束时间");
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
    public List<ImportParMemberForm> execute(List<ImportParMemberForm> object, Map<String, Object> paramMap) {
        for (ImportParMemberForm form : object) {
            AddReportSubParamRequest request = new AddReportSubParamRequest();
            //如果是奖励百分比类型rewardAmount=会员售价*百分比
            if (ObjectUtil.equal(ReportRewardTypeEnum.PERCENTAGE.getCode(), form.getRewardType())) {
                request.setRewardPercentage(form.getRewardValue());
            } else if (ObjectUtil.equal(ReportRewardTypeEnum.CASH.getCode(), form.getRewardType())) {
                request.setRewardAmount(form.getRewardValue());
            } else {
                form.setErrorMsg("金额类型不正确");
                continue;
            }
            request.setParamId(paramId);
            request.setParType(paramTypeEnum.getCode());
            request.setEid(form.getEid());
            request.setRewardType(form.getRewardType());
            request.setThresholdAmount(form.getThresholdAmount());
            request.setMemberSource(form.getMemberSource());
            request.setStartTime(DateUtil.parseDate(form.getStartTime()));
            request.setEndTime(DateUtil.parseDate(form.getEndTime()));
            request.setMemberSource(form.getMemberSource());
            request.setMemberId(form.getMemberId());
            request.setOpUserId((Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID));
            request.setOpTime(new Date());

            try {
                Boolean isSucceed = reportParamApi.saveOrUpdateReportSubMemberParam(request);
                if (!isSucceed) {
                    form.setErrorMsg("会员参数保存失败，请重试");
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
