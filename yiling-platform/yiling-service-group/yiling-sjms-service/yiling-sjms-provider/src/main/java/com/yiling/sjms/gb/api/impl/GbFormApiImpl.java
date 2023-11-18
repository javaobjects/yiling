package com.yiling.sjms.gb.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.dto.GbFlowCompanyRelationDTO;
import com.yiling.sjms.gb.dto.GbFormFlowDTO;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbFormDTO;
import com.yiling.sjms.gb.dto.GbFormExportListDTO;
import com.yiling.sjms.gb.dto.GbFormInfoDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.gb.entity.GbBaseInfoDO;
import com.yiling.sjms.gb.entity.GbCompanyRelationDO;
import com.yiling.sjms.gb.entity.GbFormDO;
import com.yiling.sjms.gb.entity.GbGoodsInfoDO;
import com.yiling.sjms.gb.entity.GbMainInfoDO;
import com.yiling.sjms.gb.service.GbBaseInfoService;
import com.yiling.sjms.gb.service.GbCompanyRelationService;
import com.yiling.sjms.gb.service.GbFormService;
import com.yiling.sjms.gb.service.GbGoodsInfoService;
import com.yiling.sjms.gb.service.GbMainInfoService;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 团购表单
 *
 * @author: wei.wang
 * @date: 2023/02/22
 */
@DubboService
public class GbFormApiImpl implements GbFormApi {
    @Autowired
    private GbFormService gbFormService;
    @Autowired
    private FormService formService;
    @Autowired
    private GbCompanyRelationService gbCompanyRelationService;
    @Autowired
    GbGoodsInfoService gbGoodsInfoService;
    @Autowired
    GbMainInfoService gbMainInfoService;
    @Autowired
    GbBaseInfoService gbBaseInfoService;

    @Override
    public GbFormInfoDTO getOneById(Long id) {
        FormDO formDO = formService.getById(id);
        GbFormInfoDTO result = PojoUtils.map(formDO, GbFormInfoDTO.class);
        if(result != null && FormTypeEnum.getByCode(result.getType()) == FormTypeEnum.GB_SUBMIT || FormTypeEnum.getByCode(result.getType()) == FormTypeEnum.GB_CANCEL || FormTypeEnum.getByCode(result.getType()) == FormTypeEnum.GROUP_BUY_COST){
            GbFormDO gbForm = gbFormService.getOneByFormId(formDO.getId());
            result.setBizType(gbForm.getBizType());
            result.setCancelFlag(gbForm.getCancelFlag());
            result.setGbId(gbForm.getId());
            result.setGbNo(gbForm.getGbNo());
            result.setSrcGbNo(gbForm.getSrcGbNo());
            result.setReviewReply(gbForm.getReviewReply());
            result.setReviewStatus(gbForm.getReviewStatus());
            result.setReviewTime(gbForm.getReviewTime());
            result.setFeeApplicationReply(gbForm.getFeeApplicationReply());
        }
        return result;
    }

    @Override
    public GbFormDTO getOneByFormId(Long formId) {
        return PojoUtils.map(gbFormService.getOneByFormId(formId),GbFormDTO.class);
    }

    @Override
    public Long saveGbCancel(SaveGBCancelInfoRequest request){
        return gbFormService.saveGbCancel(request);
    }

    @Override
    public Long saveRejectGbCancel(SaveGBCancelInfoRequest request) {
        return  gbFormService.saveRejectGbCancel(request);
    }

    @Override
    public Long saveGBInfo(SaveGBInfoRequest request){

        return  gbFormService.saveGBInfo(request);
    }

    @Override
    public Page<GbFormInfoListDTO> getGBFormListPage(QueryGBFormListPageRequest request) {
        return gbFormService.getGBFormListPage(request);
    }



    @Override
    public Page<GbFormInfoListDTO> getGBFeeFormListPage(QueryGBFormListPageRequest request) {
        return gbFormService.getGBFeeFormListPage(request);
    }

    @Override
    public GbFormFlowDTO getGbFormFlowList(Long formId) {
        FormDO formDO = formService.getById(formId);
        GbFormFlowDTO result = PojoUtils.map(formDO, GbFormFlowDTO.class);

        GbFormDO gbForm = gbFormService.getOneByFormId(formId);
        GbMainInfoDO mainInfo = gbMainInfoService.getOneByGbId(formId);
        GbBaseInfoDO baseInfo = gbBaseInfoService.getOneByGbId(formId);
        result.setBizType(gbForm.getBizType());
        result.setCustomerId(mainInfo.getCustomerId());
        result.setCustomerName(mainInfo.getCustomerName());
        result.setMonth(mainInfo.getMonth());
        result.setRegionType(mainInfo.getRegionType());
        result.setGbType(mainInfo.getGbType());
        result.setSellerEmpId(baseInfo.getSellerEmpId());
        result.setSellerEmpName(baseInfo.getSellerEmpName());
        result.setSellerDeptId(baseInfo.getSellerDeptId());
        result.setCancelFlag(gbForm.getCancelFlag());
        result.setSellerDeptName(baseInfo.getSellerDeptName());
        result.setGbId(gbForm.getId());
        result.setGbNo(gbForm.getGbNo());
        result.setSrcGbNo(gbForm.getSrcGbNo());
        result.setReviewReply(gbForm.getReviewReply());
        result.setReviewStatus(gbForm.getReviewStatus());
        result.setReviewTime(gbForm.getReviewTime());

        List<GbGoodsInfoDO> goodsInfoDTOS = gbGoodsInfoService.listByGbId(formId);
        Map<Long, List<GbGoodsInfoDO>> goodsMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(goodsInfoDTOS)){
            goodsMap = goodsInfoDTOS.stream().collect(Collectors.groupingBy(GbGoodsInfoDO::getCompanyId));
        }

        List<GbCompanyRelationDO> gbCompanyRelationList = gbCompanyRelationService.listByFormId(formId);
        List<GbFlowCompanyRelationDTO> companyRelationList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(gbCompanyRelationList)){
            for(GbCompanyRelationDO companyRelation : gbCompanyRelationList){
                GbFlowCompanyRelationDTO companyOne = PojoUtils.map(companyRelation, GbFlowCompanyRelationDTO.class);
                List<GoodsInfoDTO> goodsInfoList = PojoUtils.map(goodsMap.get(companyRelation.getId()), GoodsInfoDTO.class);
                companyOne.setGoodsList(goodsInfoList);
                companyRelationList.add(companyOne);
            }
        }

        result.setCompanyRelationList(companyRelationList);
        return result;
    }


    @Override
    public Boolean updateStatusById(UpdateGBFormInfoRequest request) {
        return gbFormService.updateStatusById(request);
    }

    @Override
    public Boolean updateByFormId(GbFormDTO gbFormDTO) {
        QueryWrapper<GbFormDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbFormDO :: getFormId,gbFormDTO.getFormId());

        GbFormDO gbFormDO = new GbFormDO();
        gbFormDO.setReviewStatus(gbFormDTO.getReviewStatus());
        gbFormDO.setReviewReply(gbFormDTO.getReviewReply());
        gbFormDO.setReviewTime(new Date());
        return gbFormService.update(gbFormDO, wrapper);
    }

    @Override
    public Page<GbFormExportListDTO> getGBFormExportListPage(QueryGBFormListPageRequest request) {
        return gbFormService.getGBFormExportListPage(request);
    }

    @Override
    public void delete(Long formId, Long userId) {
        gbFormService.delete(formId,userId);
    }

    @Override
    public Long saveGbFeeApplication(SaveGBCancelInfoRequest request) {
        return gbFormService.saveGbFeeApplication(request);
    }

    @Override
    public Long saveRejectGbFeeApplication(SaveGBCancelInfoRequest request) {
        return gbFormService.saveRejectGbFeeApplication(request);
    }

    @Override
    public Page<GbFormInfoListDTO> getGBFeeApplicationFormListPage(QueryGBFormListPageRequest request) {
        return gbFormService.getGBFeeApplicationFormListPage(request);
    }

    @Override
    public GbFormDTO getByPramyKey(Long id) {
        return PojoUtils.map(gbFormService.getById(id),GbFormDTO.class);
    }
}
