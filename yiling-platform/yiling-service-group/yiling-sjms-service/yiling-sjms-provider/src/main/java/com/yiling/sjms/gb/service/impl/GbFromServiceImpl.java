package com.yiling.sjms.gb.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.dao.GbFormMapper;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbFormExportListDTO;
import com.yiling.sjms.gb.dto.request.FileInfoRequest;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.SaveFeeApplicationInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCompanyRelationRequest;
import com.yiling.sjms.gb.dto.request.SaveGBGoodsInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.gb.entity.GbAttachmentDO;
import com.yiling.sjms.gb.entity.GbBaseInfoDO;
import com.yiling.sjms.gb.entity.GbCompanyRelationDO;
import com.yiling.sjms.gb.entity.GbFormDO;
import com.yiling.sjms.gb.entity.GbGoodsInfoDO;
import com.yiling.sjms.gb.entity.GbMainInfoDO;
import com.yiling.sjms.gb.entity.GbStatisticDO;
import com.yiling.sjms.gb.enums.GBErrorCode;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;
import com.yiling.sjms.gb.service.GbAttachmentService;
import com.yiling.sjms.gb.service.GbBaseInfoService;
import com.yiling.sjms.gb.service.GbCompanyRelationService;
import com.yiling.sjms.gb.service.GbFormService;
import com.yiling.sjms.gb.service.GbGoodsInfoService;
import com.yiling.sjms.gb.service.GbMainInfoService;
import com.yiling.sjms.gb.service.GbStatisticService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.user.common.enums.NoEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 团购表单 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-02-22
 */

@Slf4j
@Service
public class GbFromServiceImpl extends BaseServiceImpl<GbFormMapper, GbFormDO> implements GbFormService {

    @Autowired
    private NoService noService;
    @Autowired
    private GbBaseInfoService gbBaseInfoService;
    @Autowired
    private GbMainInfoService gbMainInfoService;
    @Autowired
    private GbAttachmentService gbAttachmentService;
    @Autowired
    private GbGoodsInfoService gbGoodsInfoService;
    @Autowired
    private FormService formService;
    @Autowired
    private GbStatisticService gbStatisticService;
    @Autowired
    private GbCompanyRelationService gbCompanyRelationService;

    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @Override
    public GbFormDO getOneByFormId(Long formId) {
        QueryWrapper<GbFormDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbFormDO::getFormId, formId).last(" limit 1 ");
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveGBInfo(SaveGBInfoRequest request) {
        FormDO formOne = new FormDO();
        GbBaseInfoDO gbBaseInfoDO = PojoUtils.map(request, GbBaseInfoDO.class);
        GbMainInfoDO gbMainInfoDO = PojoUtils.map(request, GbMainInfoDO.class);
        GbFormDO gbFormDO = new GbFormDO();
        if (StringUtils.isNotBlank(request.getMonthTime())) {
            gbMainInfoDO.setMonth(DateUtil.parse(request.getMonthTime(), "yyyy-MM"));
        }

        if (request.getGbId() == null || request.getGbId() == 0) {

            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setCode(noService.gen(NoEnum.GB_NO));
            createFormRequest.setName(OaTodoUtils.genGbTitle(GbFormBizTypeEnum.getByCode(1), request.getEmpName(), createFormRequest.getCode(), new Date()));
            createFormRequest.setType(FormTypeEnum.GB_SUBMIT.getCode());
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            Long formId = formService.create(createFormRequest);
            formOne.setId(formId);

            gbFormDO.setBizType(GbFormBizTypeEnum.SUBMIT.getCode());
            gbFormDO.setGbNo(createFormRequest.getCode());
        } else {
            formOne = formService.getById(request.getGbId());

            if (formOne != null && !(FormStatusEnum.getByCode(formOne.getStatus()) == FormStatusEnum.UNSUBMIT || FormStatusEnum.REJECT == FormStatusEnum.getByCode(formOne.getStatus()))) {
                throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
            }

            GbFormDO oneByFormId = this.getOneByFormId(request.getGbId());
            gbFormDO.setId(oneByFormId.getId());

            GbBaseInfoDO baseInfoOne = gbBaseInfoService.getOneByGbId(formOne.getId());
            if (baseInfoOne != null) {
                gbBaseInfoDO.setId(baseInfoOne.getId());
                if (StringUtils.isEmpty(gbBaseInfoDO.getProvinceName())) {
                    gbBaseInfoDO.setProvinceName("");
                }
                if (gbBaseInfoDO.getOrgId() == null || gbBaseInfoDO.getOrgId() == 0) {
                    gbBaseInfoDO.setOrgId(0L);
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getOrgName())) {
                    gbBaseInfoDO.setOrgName("");
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getSellerEmpId())) {
                    gbBaseInfoDO.setSellerEmpId("");
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getSellerEmpName())) {
                    gbBaseInfoDO.setSellerEmpName("");
                }
                if (gbBaseInfoDO.getSellerDeptId() == null || gbBaseInfoDO.getSellerDeptId() == 0) {
                    gbBaseInfoDO.setSellerDeptId(0L);
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getSellerDeptName())) {
                    gbBaseInfoDO.setSellerDeptName("");
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getSellerYxDeptName())) {
                    gbBaseInfoDO.setSellerYxDeptName("");
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getManagerEmpId())) {
                    gbBaseInfoDO.setManagerEmpId("");
                }

                if (StringUtils.isEmpty(gbBaseInfoDO.getManagerEmpName())) {
                    gbBaseInfoDO.setManagerEmpName("");
                }
                if (StringUtils.isEmpty(gbBaseInfoDO.getManagerYxDeptName())) {
                    gbBaseInfoDO.setManagerYxDeptName("");
                }
            }

            GbMainInfoDO mainInfoOne = gbMainInfoService.getOneByGbId(formOne.getId());

            if (mainInfoOne != null) {
                gbMainInfoDO.setId(mainInfoOne.getId());
                if (gbMainInfoDO.getCustomerId() == null || gbMainInfoDO.getCustomerId() == 0) {
                    gbMainInfoDO.setCustomerId(0L);
                }
                if (StringUtils.isEmpty(gbMainInfoDO.getCustomerName())) {
                    gbMainInfoDO.setCustomerName("");
                }

                if (gbMainInfoDO.getMonth() == null) {
                    gbMainInfoDO.setMonth(Convert.toDate("1970-01-01"));
                }

                if (gbMainInfoDO.getRebateAmount() == null  ) {
                    gbMainInfoDO.setRebateAmount(new BigDecimal("0"));
                }
                if (gbMainInfoDO.getRegionType() == null) {
                    gbMainInfoDO.setRegionType(0);
                }

                if (gbMainInfoDO.getGbType() == null || gbMainInfoDO.getGbType() == 0) {
                    gbMainInfoDO.setGbType(0);
                }
                if (StringUtils.isEmpty(gbMainInfoDO.getEvidences())) {
                    gbMainInfoDO.setEvidences("");
                }
                if (StringUtils.isEmpty(gbMainInfoDO.getOtherEvidence())) {
                    gbMainInfoDO.setOtherEvidence("");
                }
                if (StringUtils.isEmpty(gbMainInfoDO.getRemark())) {
                    gbMainInfoDO.setRemark("");
                }

            }

        }

        gbFormDO.setFormId(formOne.getId());
        this.saveOrUpdate(gbFormDO);

        GbAttachmentDO gbAttachmentDO = new GbAttachmentDO();
        gbAttachmentDO.setOpTime(request.getOpTime());
        gbAttachmentDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<GbAttachmentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GbAttachmentDO::getGbId, formOne.getId());
        gbAttachmentService.batchDeleteWithFill(gbAttachmentDO, queryWrapper);


        if (CollectionUtil.isNotEmpty(request.getFileKeyList())) {
            for(FileInfoRequest one : request.getFileKeyList()){
                if(StringUtils.isNotBlank(one.getFileMd5())){
                    GbAttachmentDO attachment = gbAttachmentService.getAttachmentByMD5(one.getFileMd5(),1);
                    if(attachment != null){
                        throw new BusinessException(GBErrorCode.GB_FORM_FILE_INFO_NOT_REPEAT);
                    }
                }
            }

            List<GbAttachmentDO> list = new ArrayList<>();
            for (FileInfoRequest fileKey : request.getFileKeyList()) {
                GbAttachmentDO one = new GbAttachmentDO();
                one.setGbId(formOne.getId());
                one.setFileType(1);
                one.setFileMd5(fileKey.getFileMd5());
                one.setFileKey(fileKey.getFileUrl());
                one.setFileName(fileKey.getFileName());
                one.setOpTime(request.getOpTime());
                one.setOpUserId(request.getOpUserId());
                list.add(one);
            }
            gbAttachmentService.saveBatch(list);
        }
        gbCompanyRelationService.deleteByFormId(formOne.getId());
        gbGoodsInfoService.deleteGoodsByGbId(formOne.getId());
        List<GbGoodsInfoDO> gbGoodsInfoDOList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(request.getCompanyList())){
            for(SaveGBCompanyRelationRequest company : request.getCompanyList() ){
                GbCompanyRelationDO companyRelationDO = PojoUtils.map(company, GbCompanyRelationDO.class);
                companyRelationDO.setFormId(formOne.getId());
                companyRelationDO.setOpUserId(request.getOpUserId());
                gbCompanyRelationService.save(companyRelationDO);
                if(CollectionUtil.isNotEmpty(company.getGbGoodsInfoList())){
                    for (SaveGBGoodsInfoRequest one : company.getGbGoodsInfoList()) {
                        GbGoodsInfoDO goodsOne = PojoUtils.map(one, GbGoodsInfoDO.class);
                        if (StringUtils.isNotBlank(one.getFlowMonthDay())) {
                            goodsOne.setFlowMonth(DateUtil.parse(one.getFlowMonthDay(), "yyyy-MM"));
                        }
                        goodsOne.setGbId(formOne.getId());
                        goodsOne.setCompanyId(companyRelationDO.getId());
                        gbGoodsInfoDOList.add(goodsOne);
                    }
                }
            }

        }else{
            GbCompanyRelationDO companyRelationDO = new GbCompanyRelationDO();
            companyRelationDO.setFormId(formOne.getId());
            companyRelationDO.setOpUserId(request.getOpUserId());
            gbCompanyRelationService.save(new GbCompanyRelationDO());
        }

        if(CollectionUtil.isNotEmpty(gbGoodsInfoDOList)){
            gbGoodsInfoService.saveBatch(gbGoodsInfoDOList);
        }

        gbBaseInfoDO.setGbId(formOne.getId());

        gbBaseInfoService.saveOrUpdate(gbBaseInfoDO);
        gbMainInfoDO.setGbId(formOne.getId());
        gbMainInfoService.saveOrUpdate(gbMainInfoDO);

        return formOne.getId();
    }

    @Override
    @Transactional
    public Long saveGbCancel(SaveGBCancelInfoRequest request) {
        FormDO formDO = formService.getById(request.getGbId());

        GbFormDO gbFormDO = this.getOneByFormId(request.getGbId());
        gbFormDO.setCancelFlag(2);
        this.updateById(gbFormDO);
        String code = formDO.getCode();

        CreateFormRequest createFormRequest = new CreateFormRequest();
        createFormRequest.setCode("X-" + code);
        createFormRequest.setName(OaTodoUtils.genGbTitle(GbFormBizTypeEnum.getByCode(1), request.getEmpName(), createFormRequest.getCode(), new Date()));
        createFormRequest.setType(FormTypeEnum.GB_CANCEL.getCode());
        createFormRequest.setOpUserId(request.getOpUserId());

        Long id = formService.create(createFormRequest);

        gbFormDO.setId(null);
        gbFormDO.setSrcGbNo(code);
        gbFormDO.setGbNo(createFormRequest.getCode());
        gbFormDO.setBizType(GbFormBizTypeEnum.CANCEL.getCode());
        gbFormDO.setFormId(id);
        gbFormDO.setCancelFlag(1);
        gbFormDO.setReviewReply("");
        gbFormDO.setReviewStatus(1);
        gbFormDO.setReviewTime(Convert.toDate("1970-01-01 00:00:00"));
        this.save(gbFormDO);

        GbMainInfoDO gbMainInfoDO = gbMainInfoService.getOneByGbId(formDO.getId());
        gbMainInfoDO.setId(null);
        gbMainInfoDO.setCancelReason(request.getCancelReason());
        gbMainInfoDO.setGbId(id);
        gbMainInfoService.save(gbMainInfoDO);

        GbBaseInfoDO gbBaseInfoDO = gbBaseInfoService.getOneByGbId(formDO.getId());
        gbBaseInfoDO.setId(null);
        gbBaseInfoDO.setGbId(id);
        gbBaseInfoService.save(gbBaseInfoDO);

        List<GbCompanyRelationDO> gbCompanyRelationList = gbCompanyRelationService.listByFormId(formDO.getId());
        for(GbCompanyRelationDO companyOne :  gbCompanyRelationList){
            List<GbGoodsInfoDO> gbGoodsInfoList = gbGoodsInfoService.listByCompanyId(companyOne.getId());
            companyOne.setId(null);
            companyOne.setFormId(id);
            gbCompanyRelationService.save(companyOne);

            for (GbGoodsInfoDO one : gbGoodsInfoList) {
                one.setId(null);
                one.setGbId(id);
                one.setCompanyId(companyOne.getId());
                one.setQuantityBox(0 - one.getQuantityBox());
                one.setQuantity(BigDecimal.ZERO.subtract(one.getQuantity()));
                one.setFinalAmount(BigDecimal.ZERO.subtract(one.getFinalAmount()));
                one.setAmount(BigDecimal.ZERO.subtract(one.getAmount()));
            }
            gbGoodsInfoService.saveBatch(gbGoodsInfoList);
        }


        List<GbAttachmentDO> attachmentList = gbAttachmentService.listByGbId(formDO.getId());
        for (GbAttachmentDO one : attachmentList) {
            one.setId(null);
            one.setGbId(id);
        }
        gbAttachmentService.saveBatch(attachmentList);

        return id;
    }

    @Override
    @Transactional
    public Long saveRejectGbCancel(SaveGBCancelInfoRequest request) {
        FormDO formDO = formService.getById(request.getGbId());

        GbMainInfoDO gbMainInfoDO = gbMainInfoService.getOneByGbId(formDO.getId());
        gbMainInfoDO.setCancelReason(request.getCancelReason());
        gbMainInfoService.updateById(gbMainInfoDO);
        return formDO.getId();
    }

    @Override
    public Page<GbFormInfoListDTO> getGBFormListPage(QueryGBFormListPageRequest request) {
        return this.baseMapper.getGBFormListPage(request.getPage(), request);
    }





    @Override
    public Boolean updateStatusById(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_NOT_EXISTS);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {

            throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
        }
        Boolean result = false;
        if (request.getOriginalStatus() == FormStatusEnum.UNSUBMIT && request.getNewStatus() == FormStatusEnum.AUDITING) {

            SubmitFormRequest submitFormRequest = PojoUtils.map(request, SubmitFormRequest.class);
            submitFormRequest.setOpTime(request.getSubmitTime());
            submitFormRequest.setFlowTplName(request.getFlowName());
            result = formService.submit(submitFormRequest);
            if (FormTypeEnum.getByCode(formDO.getType()) == FormTypeEnum.GB_SUBMIT || FormTypeEnum.getByCode(formDO.getType()) == FormTypeEnum.GB_CANCEL) {
                addStatisticFormQuantity(formDO.getId());
            }
        } else if (request.getOriginalStatus() == FormStatusEnum.AUDITING && request.getNewStatus() == FormStatusEnum.APPROVE) {
            ApproveFormRequest approveFormRequest = PojoUtils.map(request, ApproveFormRequest.class);
            result = formService.approve(approveFormRequest);
            log.info("团购审核发送团购数据id:{}",JSON.toJSONString(request.getId()));
            MqMessageBO mqMessage = new MqMessageBO(Constants.TOPIC_FLOW_SALE_GB_APPROVED_TASK, Constants.TAG_FLOW_SALE_GB_APPROVED_TASK, JSON.toJSONString(request.getId()));
            mqMessage = mqMessageSendApi.prepare(mqMessage);
            mqMessageSendApi.send(mqMessage);

        } else if (request.getOriginalStatus() == FormStatusEnum.AUDITING && request.getNewStatus() == FormStatusEnum.REJECT) {
            RejectFormRequest rejectFormRequest = PojoUtils.map(request, RejectFormRequest.class);
            result = formService.reject(rejectFormRequest);
            if (FormTypeEnum.getByCode(formDO.getType()) == FormTypeEnum.GB_SUBMIT || FormTypeEnum.getByCode(formDO.getType()) == FormTypeEnum.GB_CANCEL) {
                reduceStatisticFormQuantity(formDO.getId(),formDO.getSubmitTime());
            }

        } else if (request.getOriginalStatus() == FormStatusEnum.REJECT && request.getNewStatus() == FormStatusEnum.AUDITING) {
            SubmitFormRequest submitFormRequest = PojoUtils.map(request, SubmitFormRequest.class);
            submitFormRequest.setOpTime(request.getOpTime());
            submitFormRequest.setFlowTplName(request.getFlowName());
            result = formService.submit(submitFormRequest);
            if (FormTypeEnum.getByCode(formDO.getType()) == FormTypeEnum.GB_SUBMIT || FormTypeEnum.getByCode(formDO.getType()) == FormTypeEnum.GB_CANCEL) {
                addStatisticFormQuantity(formDO.getId());
            }

        }

        return result;
    }


    @Override
    public void addStatisticFormQuantity(Long id) {

        log.info("提交团购类型基础表单统计数据表单ID -> {}", id);
        FormDO one = formService.getById(id);
        List<GbStatisticDO> statisticList = new ArrayList<>();
        if (one != null) {
            GbFormDO gbFormDO = this.getOneByFormId(one.getId());
            Date dayTime = DateUtil.parse(DateUtil.format(one.getSubmitTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
            GbBaseInfoDO baseInfoOne = gbBaseInfoService.getOneByGbId(one.getId());
            GbMainInfoDO mainInfoOne = gbMainInfoService.getOneByGbId(one.getId());

            List<GbGoodsInfoDO> goodsInfoList = gbGoodsInfoService.listByGbId(one.getId());

            Map<Long, GbGoodsInfoDO> goodsMap = new HashMap<>();
            for (GbGoodsInfoDO good : goodsInfoList) {
                if (goodsMap.containsKey(good.getCode())) {
                    GbGoodsInfoDO goodsInfoDO = goodsMap.get(good.getCode());
                    goodsInfoDO.setQuantityBox(goodsInfoDO.getQuantityBox() + good.getQuantityBox());
                    goodsInfoDO.setFinalAmount(goodsInfoDO.getFinalAmount().add(good.getFinalAmount()));
                } else {
                    goodsMap.put(good.getCode(), good);
                }
            }

            for (GbGoodsInfoDO goodsInfoOne : goodsMap.values()) {
                GbStatisticDO statisticOne = gbStatisticService.getStatisticOne(baseInfoOne.getProvinceName(), goodsInfoOne.getCode(), dayTime,mainInfoOne.getMonth());
                if (statisticOne != null) {
                    if (GbFormBizTypeEnum.getByCode(gbFormDO.getBizType()) == GbFormBizTypeEnum.SUBMIT) {
                        statisticOne.setQuantityBox(statisticOne.getQuantityBox() + goodsInfoOne.getQuantityBox());
                        statisticOne.setFinalAmount(statisticOne.getFinalAmount().add(goodsInfoOne.getFinalAmount()));
                    } else if (GbFormBizTypeEnum.getByCode(gbFormDO.getBizType()) == GbFormBizTypeEnum.CANCEL) {
                        statisticOne.setCancelFinalAmount(statisticOne.getCancelFinalAmount().add(goodsInfoOne.getFinalAmount()));
                        statisticOne.setCancelQuantityBox(statisticOne.getCancelQuantityBox() + goodsInfoOne.getQuantityBox());
                    }
                    StringBuilder str = new StringBuilder();
                    statisticOne.setGbListId(str.append(statisticOne.getGbListId()).append(",").append(one.getId()).toString());
                } else {
                    statisticOne = new GbStatisticDO();
                    statisticOne.setProvinceName(baseInfoOne.getProvinceName());
                    statisticOne.setGoodsName(goodsInfoOne.getName());
                    statisticOne.setGoodsCode(goodsInfoOne.getCode());
                    statisticOne.setDayTime(dayTime);
                    statisticOne.setMonth(mainInfoOne.getMonth());
                    statisticOne.setOpUserId(one.getCreateUser());
                    statisticOne.setOpTime(new Date());
                    statisticOne.setGbListId(one.getId() + "");
                    if (GbFormBizTypeEnum.getByCode(gbFormDO.getBizType()) == GbFormBizTypeEnum.SUBMIT) {
                        statisticOne.setFinalAmount(goodsInfoOne.getFinalAmount());
                        statisticOne.setQuantityBox(goodsInfoOne.getQuantityBox());
                    } else if (GbFormBizTypeEnum.getByCode(gbFormDO.getBizType()) == GbFormBizTypeEnum.CANCEL) {
                        statisticOne.setCancelFinalAmount(goodsInfoOne.getFinalAmount());
                        statisticOne.setCancelQuantityBox(goodsInfoOne.getQuantityBox());
                    }
                }
                statisticList.add(statisticOne);
            }
        }

        if (CollectionUtil.isNotEmpty(statisticList)) {
            log.info("提交团购类型基础表单统计数据 -> {}", JSONUtil.toJsonStr(statisticList));
            gbStatisticService.saveOrUpdateBatch(statisticList, 1000);
        }
    }

    @Override
    public void reduceStatisticFormQuantity(Long id,Date submitTime) {
        log.info("驳回团购类型基础表单统计数据表单ID -> {}，时间->{}", id,DateUtil.format(submitTime, "yyyy-MM-dd"));
        FormDO one = formService.getById(id);
        List<GbStatisticDO> statisticList = new ArrayList<>();

        List<GbStatisticDO> deleteStatisticList = new ArrayList<>();
        if (one != null) {
            GbFormDO gbFormDO = this.getOneByFormId(one.getId());
            Date dayTime = DateUtil.parse(DateUtil.format(submitTime, "yyyy-MM-dd"), "yyyy-MM-dd");
            GbBaseInfoDO baseInfoOne = gbBaseInfoService.getOneByGbId(one.getId());
            GbMainInfoDO mainInfoOne = gbMainInfoService.getOneByGbId(one.getId());
            List<GbGoodsInfoDO> goodsInfoList = gbGoodsInfoService.listByGbId(one.getId());

            Map<Long, GbGoodsInfoDO> goodsMap = new HashMap<>();
            for (GbGoodsInfoDO good : goodsInfoList) {
                if (goodsMap.containsKey(good.getCode())) {
                    GbGoodsInfoDO goodsInfoDO = goodsMap.get(good.getCode());
                    goodsInfoDO.setQuantityBox(goodsInfoDO.getQuantityBox() + good.getQuantityBox());
                    goodsInfoDO.setFinalAmount(goodsInfoDO.getFinalAmount().add(good.getFinalAmount()));
                } else {
                    goodsMap.put(good.getCode(), good);
                }
            }

            for (GbGoodsInfoDO goodsInfoOne : goodsMap.values()) {
                GbStatisticDO statisticOne = gbStatisticService.getStatisticOne(baseInfoOne.getProvinceName(), goodsInfoOne.getCode(), dayTime,mainInfoOne.getMonth());
                if (statisticOne != null) {
                    if (GbFormBizTypeEnum.getByCode(gbFormDO.getBizType()) == GbFormBizTypeEnum.SUBMIT) {
                        statisticOne.setQuantityBox(statisticOne.getQuantityBox() - goodsInfoOne.getQuantityBox());
                        statisticOne.setFinalAmount(statisticOne.getFinalAmount().subtract(goodsInfoOne.getFinalAmount()));
                    } else if (GbFormBizTypeEnum.getByCode(gbFormDO.getBizType()) == GbFormBizTypeEnum.CANCEL) {
                        statisticOne.setCancelFinalAmount(statisticOne.getCancelFinalAmount().subtract(goodsInfoOne.getFinalAmount()));
                        statisticOne.setCancelQuantityBox(statisticOne.getCancelQuantityBox() - goodsInfoOne.getQuantityBox());
                    }
                    if(statisticOne.getQuantityBox() < 0){
                        log.error("团购统计数据有问题->{}", JSON.toJSONString(goodsInfoOne));
                    } else if (statisticOne.getQuantityBox() == 0) {
                        deleteStatisticList.add(statisticOne);
                    } else {

                        List<String> listIds = Arrays.asList(statisticOne.getGbListId().split(","));
                        listIds.removeIf(o -> String.valueOf(o).equals(id));
                        statisticOne.setGbListId(StringUtils.join(listIds, ","));
                        statisticList.add(statisticOne);
                    }

                }
            }

        }

        if (CollectionUtil.isNotEmpty(statisticList)) {
            log.info("驳回团购类型基础表单统计数据 -> {}", JSONUtil.toJsonStr(statisticList));
            gbStatisticService.saveOrUpdateBatch(statisticList, 1000);
        }

        if (CollectionUtil.isNotEmpty(deleteStatisticList)) {
            log.info("驳回团购类型基础表单删除数据 -> {}", JSONUtil.toJsonStr(deleteStatisticList));
            for(GbStatisticDO statisticDO : deleteStatisticList){
                gbStatisticService.deleteByIdWithFill(statisticDO);
            }
        }

    }

    @Override
    public Page<GbFormExportListDTO> getGBFormExportListPage(QueryGBFormListPageRequest request) {
        return this.baseMapper.getGBFormExportListPage(request.getPage(), request);
    }

    @Override
    @Transactional
    public void delete(Long formId, Long userId) {
        formService.delete(formId,userId);
        GbAttachmentDO gbAttachmentDO = new GbAttachmentDO();
        gbAttachmentDO.setOpTime(new Date());
        gbAttachmentDO.setOpUserId(userId);
        LambdaQueryWrapper<GbAttachmentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GbAttachmentDO::getGbId, formId);
        gbAttachmentService.batchDeleteWithFill(gbAttachmentDO, queryWrapper);
    }

    @Override
    public Long saveGbFeeApplication(SaveGBCancelInfoRequest request) {
        FormDO formDO = formService.getById(request.getGbId());

        GbFormDO gbFormDO = this.getOneByFormId(request.getGbId());
        /*gbFormDO.setCancelFlag(2);
        this.updateById(gbFormDO);*/
        String code = formDO.getCode();

        CreateFormRequest createFormRequest = new CreateFormRequest();
        createFormRequest.setCode("F-" + code);
        createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.GROUP_BUY_COST.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
        createFormRequest.setType(FormTypeEnum.GROUP_BUY_COST.getCode());
        createFormRequest.setOpUserId(request.getOpUserId());

        Long id = formService.create(createFormRequest);

        gbFormDO.setId(null);
        gbFormDO.setFeeApplicationReply(request.getFeeApplicationReply());
        gbFormDO.setSrcGbNo(code);
        gbFormDO.setGbNo(createFormRequest.getCode());
        gbFormDO.setBizType(FormTypeEnum.GROUP_BUY_COST.getCode());
        gbFormDO.setFormId(id);
        //gbFormDO.setCancelFlag(1);
        this.save(gbFormDO);

        GbMainInfoDO gbMainInfoDO = gbMainInfoService.getOneByGbId(formDO.getId());
        gbMainInfoDO.setId(null);
        gbMainInfoDO.setGbId(id);
        gbMainInfoService.save(gbMainInfoDO);

        GbBaseInfoDO gbBaseInfoDO = gbBaseInfoService.getOneByGbId(formDO.getId());
        gbBaseInfoDO.setId(null);
        gbBaseInfoDO.setGbId(id);
        gbBaseInfoService.save(gbBaseInfoDO);

        List<GbCompanyRelationDO> gbCompanyRelationList = gbCompanyRelationService.listByFormId(formDO.getId());
        for(GbCompanyRelationDO companyOne :  gbCompanyRelationList){
            List<GbGoodsInfoDO> gbGoodsInfoList = gbGoodsInfoService.listByCompanyId(companyOne.getId());
            companyOne.setId(null);
            companyOne.setFormId(id);
            gbCompanyRelationService.save(companyOne);

            for (GbGoodsInfoDO one : gbGoodsInfoList) {
                one.setId(null);
                one.setGbId(id);
                one.setCompanyId(companyOne.getId());
                /*one.setQuantityBox(0 - one.getQuantityBox());
                one.setQuantity(BigDecimal.ZERO.subtract(one.getQuantity()));
                one.setFinalAmount(BigDecimal.ZERO.subtract(one.getFinalAmount()));
                one.setAmount(BigDecimal.ZERO.subtract(one.getAmount()));*/
            }
            gbGoodsInfoService.saveBatch(gbGoodsInfoList);
        }

        List<SaveFeeApplicationInfoRequest> attachInfoForms = request.getAttachInfoForms();
        List<GbAttachmentDO> attachmentList = PojoUtils.map(attachInfoForms, GbAttachmentDO.class);
        //List<GbAttachmentDO> attachmentList = gbAttachmentService.listByGbId(formDO.getId());
        for (GbAttachmentDO one : attachmentList) {
            one.setGbId(id);
        }
        gbAttachmentService.saveBatch(attachmentList);
        return id;
    }

    @Override
    public Long saveRejectGbFeeApplication(SaveGBCancelInfoRequest request) {
        GbFormDO gbFormDO = this.getById(request.getGbId());
        gbFormDO.setFeeApplicationReply(request.getFeeApplicationReply());
        this.updateById(gbFormDO);
        // 删除之前的文件
        List<GbAttachmentDO> attachmentListExit = gbAttachmentService.listByGbId(gbFormDO.getFormId());
        if (CollectionUtil.isNotEmpty(attachmentListExit)) {
            List<Long> ids = attachmentListExit.stream().map(GbAttachmentDO::getId).collect(Collectors.toList());
            GbAttachmentDO deleteDO = new GbAttachmentDO();
            QueryWrapper<GbAttachmentDO> deleteWrapper = new QueryWrapper();
            deleteWrapper.lambda().in(GbAttachmentDO::getId, ids);
            gbAttachmentService.batchDeleteWithFill(deleteDO, deleteWrapper);
        }
        // 添加新文件
        List<SaveFeeApplicationInfoRequest> attachInfoForms = request.getAttachInfoForms();
        List<GbAttachmentDO> attachmentList = PojoUtils.map(attachInfoForms, GbAttachmentDO.class);
        if (CollectionUtil.isNotEmpty(attachmentList)) {
            for (GbAttachmentDO one : attachmentList) {
                one.setGbId(gbFormDO.getFormId());
            }
            gbAttachmentService.saveBatch(attachmentList);
        }
        return gbFormDO.getFormId();
    }

    @Override
    public Page<GbFormInfoListDTO> getGBFeeApplicationFormListPage(QueryGBFormListPageRequest request) {
        return this.baseMapper.getGBFeeApplicationFormListPage(request.getPage(), request);
    }

    @Override
    public Page<GbFormInfoListDTO> getGBFeeFormListPage(QueryGBFormListPageRequest request) {
        return this.baseMapper.getGBFeeFormListPage(request.getPage(), request);
    }

    @Override
    public Boolean updateFeeApplicationInfo(Long formId) {
        UpdateWrapper<GbFormDO> gbFormDOUpdateWrapper = new UpdateWrapper<>();
        gbFormDOUpdateWrapper.lambda().eq(GbFormDO::getFormId,formId);
        gbFormDOUpdateWrapper.lambda().eq(GbFormDO::getBizType,FormTypeEnum.GROUP_BUY_COST.getCode());
        GbFormDO gbFormDO = new GbFormDO();
        gbFormDO.setCancelFlag(2);
        return this.update(gbFormDO, gbFormDOUpdateWrapper);
    }

}
