package com.yiling.admin.data.center.report.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.report.form.ImportParLadderForm;
import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.request.SaveOrUpdateParamSubGoodsRequest;
import com.yiling.settlement.report.enums.ReportParamTypeEnum;
import com.yiling.settlement.report.enums.ReportRewardTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
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
public class ImportParLadderHandler implements IExcelVerifyHandler<ImportParLadderForm>, ImportDataHandler<ImportParLadderForm> {


    @DubboReference
    FlowGoodsRelationApi flowGoodsRelationApi;
    @DubboReference
    ReportParamApi reportParamApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private Long paramId;

    private Long paramSubId;

    private ReportParamTypeEnum paramTypeEnum;

    public void initPar(Long paramId, Long paramSubId, ReportParamTypeEnum paramTypeEnum) {
        this.paramId = paramId;
        this.paramSubId = paramSubId;
        this.paramTypeEnum = paramTypeEnum;
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportParLadderForm form) {
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
            //校验商品是否存在
            {
                if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.isNotNull(form.getYlGoodsId())) {
                    List<FlowGoodsRelationBO> goodsRelationList = flowGoodsRelationApi.getListByEidAndYlGoodsId(form.getEid(), form.getYlGoodsId());
                    if (CollUtil.isEmpty(goodsRelationList)) {
                        result.setSuccess(false);
                        result.setMsg("此以岭品id下没有查询到与以岭品对应关系");
                        return result;
                    } else {
                        form.setGoodsRelationList(goodsRelationList);
                    }
                }
            }
        } catch (BusinessException e) {
            form.setErrorMsg(e.getMessage());
            log.error("返利报表接替参数导入报错", e);
        } catch (Exception e) {
            form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
            log.error("数据保存出错：{}", e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<ImportParLadderForm> execute(List<ImportParLadderForm> object, Map<String, Object> paramMap) {
        for (ImportParLadderForm form : object) {
            List<FlowGoodsRelationBO> relationList = form.getGoodsRelationList();
            List<String> succeedList = ListUtil.toList();
            for (FlowGoodsRelationBO relationBO : relationList) {
                SaveOrUpdateParamSubGoodsRequest request = new SaveOrUpdateParamSubGoodsRequest();
                request.setParamId(paramId);
                request.setParamSubId(paramSubId);
                request.setParType(paramTypeEnum.getCode());
                request.setEid(form.getEid());
                request.setRewardType(form.getRewardType());
                request.setYlGoodsId(form.getYlGoodsId());
                request.setGoodsInSn(relationBO.getGoodsInSn());
                request.setGoodsName(relationBO.getGoodsName());
                request.setGoodsSpecification(relationBO.getGoodsSpecifications());
                request.setThresholdCount(form.getThresholdCount());
                request.setOrderSource(form.getOrderSource());
                request.setStartTime(DateUtil.parseDate(form.getStartTime()));
                request.setEndTime(DateUtil.parseDate(form.getEndTime()));
                request.setOpUserId((Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID));
                request.setOpTime(new Date());

                if (ObjectUtil.equal(form.getRewardType().toString(), ReportRewardTypeEnum.CASH.getCode().toString())) {
                    request.setRewardAmount(form.getRewardAmount());
                } else if (ObjectUtil.equal(form.getRewardType().toString(), ReportRewardTypeEnum.PERCENTAGE.getCode().toString())) {
                    request.setRewardPercentage(form.getRewardAmount());
                } else {
                    form.setErrorMsg("金额类型不正确");
                    break;
                }
                try {
                    Boolean isSucceed = reportParamApi.saveOrUpdateParamSubLadderGoods(request);
                    if (isSucceed) {
                        succeedList.add(relationBO.getGoodsInSn());
                    }
                } catch (BusinessException be) {
                    String errMsg = buildErrMsg(form.getErrorMsg(), "此以岭id关联的商品内码为{" + relationBO.getGoodsInSn() + "}的失败原因为" + be.getMessage());
                    form.setErrorMsg(errMsg);
                } catch (Exception e) {
                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                    log.error("数据保存出错：{}", e.getMessage(), e);
                }
                if (CollUtil.isNotEmpty(succeedList) && StrUtil.isNotBlank(form.getErrorMsg()) && ObjectUtil.notEqual(form.getErrorMsg(), ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage())) {
                    String errMsg = buildErrMsg(form.getErrorMsg(), "内码为[" + String.join(",", succeedList) + "]关联的商品内码已保存成功");
                    form.setErrorMsg(errMsg);
                }
            }
        }
        return object;
    }

    private String buildErrMsg(String oldErrMsg, String errMsg) {
        if (StrUtil.isBlank(oldErrMsg)) {
            return errMsg;
        }
        return oldErrMsg + "\n" + errMsg;
    }

}
