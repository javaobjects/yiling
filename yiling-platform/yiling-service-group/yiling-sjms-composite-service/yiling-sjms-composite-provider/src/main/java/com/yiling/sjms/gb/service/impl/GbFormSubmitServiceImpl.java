package com.yiling.sjms.gb.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbAttachmentApi;
import com.yiling.sjms.gb.api.GbBaseInfoApi;
import com.yiling.sjms.gb.api.GbCompanyRelationApi;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.api.GbGoodsInfoApi;
import com.yiling.sjms.gb.api.GbMainInfoApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.BaseInfoDTO;
import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;
import com.yiling.sjms.gb.dto.GbFormInfoDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.dto.MainInfoDTO;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.enums.GBErrorCode;
import com.yiling.sjms.gb.service.GbFormSubmitService;
import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.workflow.workflow.dto.request.ResubmitGroupBuyRequest;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyCancleRequest;
import com.yiling.workflow.workflow.dto.request.StartGroupBuyRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购表单提交按钮
 */
@Service
@Slf4j
@RefreshScope
public class GbFormSubmitServiceImpl implements GbFormSubmitService {

    @DubboReference
    GbAttachmentApi gbAttachmentApi;
    @DubboReference
    GbFormApi gbFormApi;
    @DubboReference
    FormApi formApi;
    @DubboReference
    GbGoodsInfoApi gbGoodsInfoApi;
    @DubboReference
    GbMainInfoApi gbMainInfoApi;
    @DubboReference
    GbBaseInfoApi gbBaseInfoApi;
    @DubboReference
    GbCompanyRelationApi gbCompanyRelationApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @DubboReference
    CrmGoodsInfoApi crmGoodsInfoApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> gbProcess ;

    @Override
    public Boolean submitFormProcess(SaveGBInfoRequest request) {

        GbFormInfoDTO formDTO = judeFormSubmit(request);
        BaseInfoDTO baseInfoDTO = gbBaseInfoApi.getOneByGbId(formDTO.getId());
        MainInfoDTO mainInfoDTO = gbMainInfoApi.getOneByGbId(formDTO.getId());
        List<GbCompanyRelationDTO> gbCompanyRelationList = gbCompanyRelationApi.listByFormId(formDTO.getId());
        List<String> codeList = gbCompanyRelationList.stream().map(o -> o.getTermainalCompanyCode()).collect(Collectors.toList());

        if(FormStatusEnum.getByCode(formDTO.getStatus()) == FormStatusEnum.UNSUBMIT){

            StartGroupBuyRequest startGroupBuyRequest = new StartGroupBuyRequest();
            startGroupBuyRequest.setProcDefId(gbProcess.get("gb_submit"));
            startGroupBuyRequest.setBusinessKey(formDTO.getGbNo());
            startGroupBuyRequest.setProvinceName(baseInfoDTO.getProvinceName());
            startGroupBuyRequest.setStartUserId(formDTO.getEmpId());
            startGroupBuyRequest.setOrgId(baseInfoDTO.getOrgId());
            startGroupBuyRequest.setTerminalCompanyCode(codeList);
            startGroupBuyRequest.setGovernmentBuy(mainInfoDTO.getGbType() == 1 ? false:true );
            startGroupBuyRequest.setGbId(formDTO.getId());
            startGroupBuyRequest.setOpUserId(request.getOpUserId());
            startGroupBuyRequest.setOpTime(request.getOpTime());
            log.info("团购提报发布流程参数:{}", JSON.toJSONString(startGroupBuyRequest));
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_GB_SUBMIT_SEND_WORKFLOW, Constants.TAG_GB_SUBMIT_SEND_WORKFLOW,JSON.toJSONString(startGroupBuyRequest) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);

        }else if (FormStatusEnum.getByCode(formDTO.getStatus()) == FormStatusEnum.REJECT){
            ResubmitGroupBuyRequest resubmitGroupBuyRequest = new ResubmitGroupBuyRequest();
            resubmitGroupBuyRequest.setProvinceName(baseInfoDTO.getProvinceName());
            resubmitGroupBuyRequest.setOrgId(baseInfoDTO.getOrgId());
            resubmitGroupBuyRequest.setTerminalCompanyCode(codeList);
            resubmitGroupBuyRequest.setGbNo(formDTO.getGbNo());

            log.info("团购提报驳回重新发布流程参数:{}", JSON.toJSONString(resubmitGroupBuyRequest));
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_GB_RESUBMIT_SEND_WORKFLOW, Constants.TAG_GB_RESUBMIT_SEND_WORKFLOW,JSON.toJSONString(resubmitGroupBuyRequest) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);

        }

        return true;
    }
    @GlobalTransactional
    private GbFormInfoDTO judeFormSubmit(SaveGBInfoRequest request){
        Long id = gbFormApi.saveGBInfo(request);
        GbFormInfoDTO formDTO = gbFormApi.getOneById(id);

        //判断是否能发起流程，不能直接报错
        List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(formDTO.getId());
        if(CollectionUtil.isEmpty(attachmentList)){
            throw new BusinessException(GBErrorCode.GB_FORM_FILE_NOT_EMPTY);
        }
        List<GoodsInfoDTO> goodsInfoList = gbGoodsInfoApi.listByGbId(formDTO.getId());

        if(CollectionUtil.isEmpty(goodsInfoList)){
            throw new BusinessException(GBErrorCode.GB_FORM_GOODS_INFO_NOT_EMPTY);
        }

        List<Long> codeList = goodsInfoList.stream().map(o -> o.getCode()).collect(Collectors.toList());
        List<CrmGoodsInfoDTO> crmGoodsList = crmGoodsInfoApi.findByCodeList(codeList);
        Map<Long,CrmGoodsInfoDTO> crmGoodsMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(crmGoodsList)){
            crmGoodsMap = crmGoodsList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO :: getGoodsCode,o->o,(k1,k2)->k1));
        }

        for(GoodsInfoDTO one : goodsInfoList ){
            if(one.getCode() == null ||
                    one.getCode() <=0 ||
                    StringUtils.isBlank(one.getName()) ||
                    StringUtils.isBlank(one.getSpecification()) ||
                    one.getSmallPackage() <=0 ||
                    one.getQuantityBox() <= 0 ||
                    one.getFinalPrice().compareTo(BigDecimal.ZERO) <=0 ||
                    DateUtil.compare(one.getPaymentTime(), DateUtil.parseDate("1970-01-01 00:00:00")) == 0 ||
                    DateUtil.compare(one.getFlowMonth(), DateUtil.parseDate("1970-01-01 00:00:00")) == 0 ){
                throw new BusinessException(GBErrorCode.GB_FORM_GOODS_INFO_NOT_COMPLETE);
            }
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsMap.get(one.getCode());
            if(crmGoodsInfoDTO == null){
                throw new BusinessException(GBErrorCode.GB_FORM_GOODS_INFO_LAPSE_ERROR);
            }
            if(crmGoodsInfoDTO.getStatus() == 1 ||
                    crmGoodsInfoDTO.getIsGroupPurchase() == 0 ||
                    !one.getName().equals(crmGoodsInfoDTO.getGoodsName()) ||
                    one.getPrice().compareTo(crmGoodsInfoDTO.getSupplyPrice()) !=0 ||
                    one.getSmallPackage().compareTo(Integer.valueOf(crmGoodsInfoDTO.getPacking())) !=0  ){
                throw new BusinessException(GBErrorCode.GB_FORM_GOODS_INFO_LAPSE_ERROR);
            }



            if(one.getQuantityBox() <= 0){
                throw new BusinessException(GBErrorCode.GB_FORM_GOODS_QUANTITY_NOT_TIGHT);

            }
        }

        List<GbCompanyRelationDTO> gbCompanyRelationList = gbCompanyRelationApi.listByFormId(id);
        if(CollectionUtil.isEmpty(gbCompanyRelationList)){
            throw new BusinessException(GBErrorCode.GB_FORM_COMPANY_INFO_NOT_EMPTY);
        }
        List<Long> companyIds = gbCompanyRelationList.stream().map(o -> o.getId()).collect(Collectors.toList());
        List<GoodsInfoDTO> goodsList = gbGoodsInfoApi.listByCompanyIds(companyIds);
        Map<Long, List<GoodsInfoDTO>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(GoodsInfoDTO::getCompanyId));

        for(GbCompanyRelationDTO one : gbCompanyRelationList ){

            if(CollectionUtil.isEmpty(goodsMap.get(one.getId()))){
                throw new BusinessException(GBErrorCode.GB_FORM_GOODS_INFO_NOT_COMPLETE);
            }

            if(StringUtils.isBlank(one.getBusinessCompanyName()) ||
                    StringUtils.isBlank(one.getTermainalCompanyName())){
                throw new BusinessException(GBErrorCode.GB_FORM_GOODS_INFO_NOT_COMPLETE);
            }
        }

        Boolean mainFlag = false;
        MainInfoDTO mainInfoDTO = gbMainInfoApi.getOneByGbId(formDTO.getId());
        if(mainInfoDTO != null && mainInfoDTO.getCustomerId() != null && mainInfoDTO.getCustomerId() !=0
                && StringUtils.isNotBlank(mainInfoDTO.getCustomerName())
                && mainInfoDTO.getMonth() != null && DateUtil.compare(mainInfoDTO.getMonth(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0
                && mainInfoDTO.getRegionType() != null && mainInfoDTO.getRegionType() >0
                && mainInfoDTO.getGbType() != null && mainInfoDTO.getGbType() > 0 ){
            mainFlag = true;
        }
        if(!mainFlag){
            throw new BusinessException(GBErrorCode.GB_FORM_MAIN_INFO_NOT_EMPTY);
        }
        mainFlag = false;
        if(mainInfoDTO !=null &&
                (StringUtils.isNotBlank(mainInfoDTO.getEvidences())|| StringUtils.isNotBlank(mainInfoDTO.getOtherEvidence()) )){
            mainFlag = true;
        }
        if(!mainFlag){
            throw new BusinessException(GBErrorCode.GB_FORM_EVIDENCE_INFO_NOT_EMPTY);
        }

        mainFlag = false;
        BaseInfoDTO baseInfoDTO = gbBaseInfoApi.getOneByGbId(formDTO.getId());
        if(baseInfoDTO != null && StringUtils.isNotBlank(baseInfoDTO.getProvinceName())
                && baseInfoDTO.getOrgId() != null && baseInfoDTO.getOrgId() !=0
                && StringUtils.isNotBlank(baseInfoDTO.getSellerEmpId()) && StringUtils.isNotBlank(baseInfoDTO.getSellerEmpName())
                && StringUtils.isNotBlank(baseInfoDTO.getManagerEmpId()) && StringUtils.isNotBlank(baseInfoDTO.getManagerEmpName()) ){
            mainFlag = true;
        }
        if(!mainFlag){
            throw new BusinessException(GBErrorCode.GB_FORM_BASE_INFO_NOT_EMPTY);
        }

        return formDTO;
    }

    @Override
    public Boolean cancelFormProcess(SaveGBCancelInfoRequest request) {

        if(request.getType() == 2){
            Long id = gbFormApi.saveGbCancel(request);
            GbFormInfoDTO formDTO = gbFormApi.getOneById(id);
            StartGroupBuyCancleRequest startGroupBuyCancleRequest = new StartGroupBuyCancleRequest();
            startGroupBuyCancleRequest.setGbNo(formDTO.getGbNo());
            startGroupBuyCancleRequest.setSrcGbNo(formDTO.getSrcGbNo());
            startGroupBuyCancleRequest.setGbId(formDTO.getId());
            log.info("团购取消发布流程参数:{}", JSON.toJSONString(startGroupBuyCancleRequest));
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_GB_CANCEL_SEND_WORKFLOW, Constants.TAG_GB_CANCEL_SEND_WORKFLOW,JSON.toJSONString(startGroupBuyCancleRequest) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);

        }else if (request.getType() == 3){
            Long id = gbFormApi.saveRejectGbCancel(request);
            GbFormInfoDTO formDTO = gbFormApi.getOneById(id);
            log.info("团购取消驳回重新发布流程参数:{}",  formDTO.getGbNo());
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_GB_CANCEL_RESUBMIT_SEND_WORKFLOW, Constants.TAG_GB_CANCEL_RESUBMIT_SEND_WORKFLOW,formDTO.getGbNo() );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);
        }
        return true;
    }

    @Override
    public Boolean feeApplicationFormProcess(SaveGBCancelInfoRequest request) {
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        Map<String, Object> var = Maps.newHashMap();
        var.put(ApproveConstant.FINANCE,gbProcess.get(ApproveConstant.FINANCE));
        if(request.getType() == 2){
            Long id = gbFormApi.saveGbFeeApplication(request);
            FormDTO formDTO = formApi.getById(id);
            //工作流部分
            WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
            // 从配置文件获取ProcDefId
            workFlowBaseRequest.setBusinessKey(formDTO.getCode());
            workFlowBaseRequest.setEmpName(esbEmployeeDTO.getEmpName());
            workFlowBaseRequest.setStartUserId(request.getEmpId());

            workFlowBaseRequest.setFormType(FormTypeEnum.GROUP_BUY_COST.getCode());
            workFlowBaseRequest.setTitle(formDTO.getName());
            workFlowBaseRequest.setId(formDTO.getId());
            workFlowBaseRequest.setVariables(var);
            workFlowBaseRequest.setProcDefId(gbProcess.get("groupBuyCost"));
            //首次提交
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_SUBMIT_SEND_WORKFLOW,Constants.TAG_GB_FEE_APPLICATION_SEND_WORKFLOW,JSON.toJSONString(workFlowBaseRequest) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);

        }else if (request.getType() == 3){
            Long id = gbFormApi.saveRejectGbFeeApplication(request);
            GbFormInfoDTO formDTO = gbFormApi.getOneById(id);
            log.info("团购费用申请驳回重新发布流程参数:{}",  formDTO.getGbNo());
            //工作流部分
            WorkFlowBaseRequest updateRequest = new WorkFlowBaseRequest();
            updateRequest.setId(id);
            updateRequest.setFormType(FormTypeEnum.GROUP_BUY_COST.getCode());
            updateRequest.setVariables(var);
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_RESUBMIT_SEND_WORKFLOW, Constants.TAG_GB_FEE_APPLICATION_SEND_WORKFLOW,JSON.toJSONString(updateRequest));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);
        }
        return true;
    }
}
