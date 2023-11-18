package com.yiling.sjms.form.controller;

import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.crm.api.HospitalDrugstoreRelationFormApi;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.vo.AppendixDetailVO;
import com.yiling.sjms.flow.api.FixMonthFlowApi;
import com.yiling.sjms.flow.dto.MonthFlowExtFormDTO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.vo.ApproveVO;
import com.yiling.sjms.form.vo.BizFormDetailVO;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gxl
 */
@Slf4j
@RestController
@RequestMapping("/form/common")
@Api(tags = "表单详情公共部分")
public class CommonController extends AbstractBizFormController {

    @DubboReference
    SaleaAppealFormApi saleaAppealFormApi;
    @DubboReference
    FleeingGoodsFormApi fleeingGoodsFormApi;

    @DubboReference
    HospitalDrugstoreRelationFormApi hospitalDrugstoreRelationFormApi;

    @DubboReference
    FixMonthFlowApi fixMonthFlowApi;

    @Autowired
    FileService fileService;

    @DubboReference
    FormApi formApi;



    @Override
    void setBizInfo(BizFormDetailVO vo){
        ApproveVO approveVO=new ApproveVO();
        setApproveInfo(vo.getBasicInfo().getType(),vo.getBasicInfo().getId(),approveVO);
        FormDTO formDTO = formApi.getById(vo.getBasicInfo().getId());
        if (Objects.nonNull(formDTO)) {
            approveVO.setTransferType(formDTO.getTransferType());
        }
        vo.setBizInfo(approveVO);
    }

    private void setApproveInfo(Integer formType, Long formId, ApproveVO formVO){
        if (ObjectUtil.equal(formType, FormTypeEnum.GOODS_FLEEING.getCode())){
            FleeingGoodsFormExtDTO fleeingGoods = fleeingGoodsFormApi.queryExtByFormId(formId);
            if (ObjectUtil.isNotNull(fleeingGoods)){
                formVO.setAppealType(fleeingGoods.getReportType());
                formVO.setAppendix(fleeingGoods.getAppendix());
                formVO.setDescribe(fleeingGoods.getFleeingDescribe());
                formVO.setConfirmDescribe(fleeingGoods.getConfirmDescribe());
            }
        }
        if (ObjectUtil.equal(formType,FormTypeEnum.SALES_APPEAL.getCode())){
            SalesAppealExtFormDTO salesAppealExt = saleaAppealFormApi.queryAppendix(formId);
            if (ObjectUtil.isNotNull(salesAppealExt)){
                formVO.setAppealType(salesAppealExt.getAppealType());
                formVO.setAppealAmount(salesAppealExt.getAppealAmount());
                formVO.setAppendix(salesAppealExt.getAppendix());
                formVO.setDescribe(salesAppealExt.getAppealDescribe());
                formVO.setConfirmDescribe(salesAppealExt.getConfirmRemark());
                formVO.setMonthAppealType(salesAppealExt.getMonthAppealType());
            }
        }
        if (ObjectUtil.equal(formType,FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode())){
            HospitalDrugstoreRelationExtFormDTO hospitalDrugstoreRelationExtFormDTO = hospitalDrugstoreRelationFormApi.queryAppendix(formId);
            if (ObjectUtil.isNotNull(hospitalDrugstoreRelationExtFormDTO)){
                formVO.setAppendix(hospitalDrugstoreRelationExtFormDTO.getAppendix());
            }
        }
        if (ObjectUtil.equal(formType,FormTypeEnum.FIX_MONTH_FLOW.getCode())){
            MonthFlowExtFormDTO monthFlowExtFormDTO = fixMonthFlowApi.queryAppendix(formId);
            if (ObjectUtil.isNotNull(monthFlowExtFormDTO)){
                formVO.setAppealType(monthFlowExtFormDTO.getAppealType());
                formVO.setAppealAmount(monthFlowExtFormDTO.getAppealAmount());
                formVO.setAppendix(monthFlowExtFormDTO.getAppendix());
                formVO.setDescribe(monthFlowExtFormDTO.getAppealDescribe());
            }
        }
        if (StrUtil.isNotBlank(formVO.getAppendix())){
            List<AppendixDetailVO> appendixList = JSON.parseArray(formVO.getAppendix(), AppendixDetailVO.class);

            appendixList.forEach(e->{

                FileTypeEnum fileTypeEnum=null;
                if (ObjectUtil.equal(FormTypeEnum.GOODS_FLEEING.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.FLEEING_GOODS_APPENDIX_UPLOAD_FILE;
                }
                if (ObjectUtil.equal(FormTypeEnum.SALES_APPEAL.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.SALES_APPEAL_UPLOAD_FILE;
                }
                if (ObjectUtil.equal(FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.HOSPITAL_DRUGSTORE_REL_APPENDIX_UPLOAD_FILE;
                }
                if(ObjectUtil.equal(FormTypeEnum.FIX_MONTH_FLOW.getCode(),formType)){
                    fileTypeEnum = FileTypeEnum.FIX_FLOW_MONTH_UPLOAD_FILE;
                }
                String url = fileService.getUrl(e.getKey(), fileTypeEnum);
                e.setUrl(url);
            });
            formVO.setAppendixList(appendixList);
        }
    }

}