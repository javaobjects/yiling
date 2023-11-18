package com.yiling.sjms.gb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbAttachmentApi;
import com.yiling.sjms.gb.api.GbBaseInfoApi;
import com.yiling.sjms.gb.api.GbCompanyRelationApi;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.api.GbFormSubmitProcessApi;
import com.yiling.sjms.gb.api.GbGoodsInfoApi;
import com.yiling.sjms.gb.api.GbMainInfoApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.BaseInfoDTO;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;
import com.yiling.sjms.gb.dto.GbFormDTO;
import com.yiling.sjms.gb.dto.GbFormInfoDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.dto.MainInfoDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;
import com.yiling.sjms.gb.form.QueryGBFeeApplicationFormListPageForm;
import com.yiling.sjms.gb.form.QueryGBFormListPageForm;
import com.yiling.sjms.gb.form.SaveFeeApplicationInfoForm;
import com.yiling.sjms.gb.vo.FileNameInfoVO;
import com.yiling.sjms.gb.vo.GbBaseInfoVO;
import com.yiling.sjms.gb.vo.GbCancelFormInfoListVO;
import com.yiling.sjms.gb.vo.GbCompanyInfoVO;
import com.yiling.sjms.gb.vo.GbFormCancelListVO;
import com.yiling.sjms.gb.vo.GbFormDetailVO;
import com.yiling.sjms.gb.vo.GbGoodsInfoVO;
import com.yiling.sjms.gb.vo.GbMainInfoVO;
import com.yiling.sjms.gb.vo.GbProcessDetailVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购提报列表
 *
 * @author: shixing.sun
 * @date: 2023/04/03
 */

@Slf4j
@RestController
@RequestMapping("/gb/fee/application")
@Api(tags = "团购费用申请")
public class GbFeeApplicationController extends BaseController {


    @DubboReference
    GbFormApi gbFormApi;
    @DubboReference
    GbFormSubmitProcessApi gbFormSubmitProcessApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    GbBaseInfoApi gbBaseInfoApi;
    @DubboReference
    GbMainInfoApi gbMainInfoApi;
    @DubboReference
    GbGoodsInfoApi gbGoodsInfoApi;
    @DubboReference
    GbAttachmentApi gbAttachmentApi;
    @DubboReference
    ProcessApi processApi;
    @DubboReference
    GbCompanyRelationApi gbCompanyRelationApi;

    @Autowired
    FileService fileService;

    @DubboReference
    FormApi formApi;

    @ApiOperation(value = "团购费用申请选择列表")
    @PostMapping("/choose/list")
    public Result<Page<GbCancelFormInfoListVO>> getGBCancelFormChooseListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBFormListPageForm form) {

        QueryGBFormListPageRequest request = PojoUtils.map(form, QueryGBFormListPageRequest.class);
        if (StringUtils.isNotBlank(form.getStartMonthTime())) {
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse(form.getStartMonthTime(), "yyyy-MM")));
        }

        if (StringUtils.isNotBlank(form.getEndMonthTime())) {
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse(form.getEndMonthTime(), "yyyy-MM")));
        }

        // 审批通过，才能够
        request.setBizType(GbFormBizTypeEnum.SUBMIT.getCode());
        request.setCreateUser(userInfo.getCurrentUserId());
        request.setReviewStatus(2);
        Page<GbFormInfoListDTO> gbFormListPage = gbFormApi.getGBFeeApplicationFormListPage(request);

        return Result.success(PojoUtils.map(gbFormListPage, GbCancelFormInfoListVO.class));
    }

    @ApiOperation(value = "团购费用申请列表")
    @PostMapping("/list")
    public Result<Page<GbFormCancelListVO>> getGbSubmitFormListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBFeeApplicationFormListPageForm form) {
        QueryGBFormListPageRequest request = PojoUtils.map(form, QueryGBFormListPageRequest.class);
        request.setBizType(FormTypeEnum.GROUP_BUY_COST.getCode());

        if (StringUtils.isNotBlank(form.getStartMonthTime())) {
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse(form.getStartMonthTime(), "yyyy-MM")));
        }

        if (StringUtils.isNotBlank(form.getEndMonthTime())) {
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse(form.getEndMonthTime(), "yyyy-MM")));
        }
        if (request.getStartApproveTime() != null) {
            request.setStartApproveTime(DateUtil.beginOfDay(request.getStartApproveTime()));
        }
        if (request.getEndApproveTime() != null) {
            request.setEndApproveTime(DateUtil.endOfDay(request.getEndApproveTime()));
        }

        if (request.getStartSubmitTime() != null) {
            request.setStartSubmitTime(DateUtil.beginOfDay(request.getStartSubmitTime()));
        }
        if (request.getEndSubmitTime() != null) {
            request.setEndSubmitTime(DateUtil.endOfDay(request.getEndSubmitTime()));
        }
        if (form.getAllData().equals(2)) {
            request.setCreateUser(userInfo.getCurrentUserId());
        }
        request.setType(11);
        Page<GbFormInfoListDTO> gbFormListPage = gbFormApi.getGBFeeFormListPage(request);
        return Result.success(PojoUtils.map(gbFormListPage, GbFormCancelListVO.class));
    }

    @ApiOperation(value = "费用申请保存提交")
    @PostMapping("/save")
    public Result saveCancelForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFeeApplicationInfoForm form) {

        SaveGBCancelInfoRequest request = PojoUtils.map(form, SaveGBCancelInfoRequest.class);

        EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setBizType(FormTypeEnum.GROUP_BUY_COST.getCode());
        request.setEmpId(employee.getEmpId());
        request.setEmpName(employee.getEmpName());
        request.setDeptId(employee.getDeptId());
        request.setDeptName(employee.getYxDept());
        request.setBizArea(employee.getYxArea());
        request.setBizProvince(employee.getYxProvince());
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setFormType(FormTypeEnum.GROUP_BUY_COST.getCode());
        gbFormSubmitProcessApi.feeApplicationFormProcess(request);
        return Result.success();
    }

    @ApiOperation(value = "团购费用申请明细")
    @GetMapping("/detail")
    public Result<GbFormDetailVO> getGBSubmitFormDetailPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id",required = true)Long id){
        GbFormDTO gbFormInfoDTO = gbFormApi.getByPramyKey(id);
        GbFormInfoDTO formDTO = gbFormApi.getOneById(gbFormInfoDTO.getFormId());
        GbFormDetailVO result = PojoUtils.map(formDTO, GbFormDetailVO.class);
        // 11表示电子流业务类型费用申请，3表似乎gb——form里面只有1提报 2取消 3费用申请
        result.setBizType(11);
        if(result != null){
            BaseInfoDTO baseInfoOne = gbBaseInfoApi.getOneByGbId(formDTO.getId());
            GbBaseInfoVO baseInfo = PojoUtils.map(baseInfoOne, GbBaseInfoVO.class);
            result.setBaseInfo(baseInfo);

            MainInfoDTO mainInfoOne = gbMainInfoApi.getOneByGbId(formDTO.getId());
            GbMainInfoVO mainInfo = PojoUtils.map(mainInfoOne, GbMainInfoVO.class);
            result.setMainInfo(mainInfo);

            List<GbCompanyInfoVO> companyInfoList = new ArrayList<>();

            List<GoodsInfoDTO> goodsInfoDTOS = gbGoodsInfoApi.listByGbId(formDTO.getId());
            Map<Long, List<GoodsInfoDTO>> goodsMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(goodsInfoDTOS)){
                goodsMap = goodsInfoDTOS.stream().collect(Collectors.groupingBy(GoodsInfoDTO::getCompanyId));
            }
            List<GbCompanyRelationDTO> gbCompanyRelationList = gbCompanyRelationApi.listByFormId(formDTO.getId());
            if(CollectionUtil.isNotEmpty(gbCompanyRelationList)){
                for(GbCompanyRelationDTO companyRelation : gbCompanyRelationList){
                    GbCompanyInfoVO companyOne = PojoUtils.map(companyRelation, GbCompanyInfoVO.class);
                    List<GbGoodsInfoVO> goodsInfoList = PojoUtils.map(goodsMap.get(companyRelation.getId()), GbGoodsInfoVO.class);
                    companyOne.setGbGoodsInfoList(goodsInfoList);
                    companyInfoList.add(companyOne);
                }
            }

            result.setCompanyInfoList(companyInfoList);
            List<String> codes = new ArrayList<>();
            codes.add(formDTO.getSrcGbNo());
            List<FormDTO> formDTOS = formApi.listByCodes(codes);
            if(CollectionUtil.isNotEmpty(formDTOS)){
                FormDTO orangeForm = formDTOS.get(0);
                List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(orangeForm.getId());

                if(CollectionUtil.isNotEmpty(attachmentList)){
                    List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                    for(AttachmentDTO one : attachmentList){
                        FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                        fileInfoVO.setFileKey(one.getFileKey());
                        fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_PROCESS_SUBMIT_SUPPORT_PICTURE));
                        fileInfoVO.setFileMd5(one.getFileMd5());
                        fileInfoVO.setFileName(one.getFileName());
                        fileInfoList.add(fileInfoVO);
                    }
                    result.setFileInfoList(fileInfoList);
                }
            }
        }
        if(StringUtils.isNotBlank(formDTO.getGbNo()) && FormStatusEnum.getByCode(formDTO.getStatus()) != FormStatusEnum.UNSUBMIT){
            GetProcessDetailRequest request = new GetProcessDetailRequest();
            request.setBusinessKey(formDTO.getGbNo());
            List<ProcessDetailDTO> processDetail = processApi.getProcessDetail(request);
            List<GbProcessDetailVO> gbProcessDetailList = PojoUtils.map(processDetail, GbProcessDetailVO.class);
            result.setProcessDetailList(gbProcessDetailList);
        }
        // 团购费用取消，需要查询团否费用文件和申请理由
        if(formDTO.getType() == 11){
            result.setFeeApplicationReply(gbFormInfoDTO.getFeeApplicationReply());
            List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(gbFormInfoDTO.getFormId());
            if(CollectionUtil.isNotEmpty(attachmentList)){
                List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                for(AttachmentDTO one : attachmentList){
                    FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                    fileInfoVO.setFileKey(one.getFileKey());
                    if(formDTO.getType().equals(FormTypeEnum.GROUP_BUY_COST.getCode())){
                        fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_FEE_APPLICATION_UPLOAD_FILE));
                    }
                    fileInfoVO.setFileName(one.getFileName());
                    fileInfoList.add(fileInfoVO);
                }
                result.setFeeApplicationFileInfoList(fileInfoList);
            }
        }
        return Result.success(result);
    }

}
