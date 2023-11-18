package com.yiling.sjms.workflow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.yiling.sjms.gb.api.GbGoodsInfoApi;
import com.yiling.sjms.gb.api.GbMainInfoApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.BaseInfoDTO;
import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;
import com.yiling.sjms.gb.dto.GbFormInfoDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.dto.MainInfoDTO;
import com.yiling.sjms.gb.vo.FileNameInfoVO;
import com.yiling.sjms.gb.vo.GbBaseInfoVO;
import com.yiling.sjms.gb.vo.GbCompanyInfoVO;
import com.yiling.sjms.gb.vo.GbGoodsInfoVO;
import com.yiling.sjms.gb.vo.GbMainInfoVO;
import com.yiling.sjms.gb.vo.GbProcessDetailVO;
import com.yiling.sjms.workflow.form.QueryFinishedProcessPageForm;
import com.yiling.sjms.workflow.form.QueryTodoProcessPageForm;
import com.yiling.sjms.workflow.vo.FlowGbFormDetailVO;
import com.yiling.sjms.workflow.vo.WfTaskVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.api.TaskApi;
import com.yiling.workflow.workflow.api.WfActHistoryApi;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.WfTaskDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;
import com.yiling.workflow.workflow.dto.request.GetTaskDetailRequest;
import com.yiling.workflow.workflow.dto.request.QueryFinishedProcessPageRequest;
import com.yiling.workflow.workflow.dto.request.QueryTodoProcessPageRequest;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/11/28
 */
@Slf4j
@RestController
@RequestMapping("/process")
@Api(tags = "流程中心")
public class ProcessController extends BaseController {

    @DubboReference
    private ProcessApi processApi;
    @DubboReference
    private GbFormApi gbFormApi;
    @DubboReference
    private GbBaseInfoApi gbBaseInfoApi;
    @DubboReference
    private GbMainInfoApi gbMainInfoApi;
    @DubboReference
    private GbGoodsInfoApi gbGoodsInfoApi;
    @DubboReference
    private  GbAttachmentApi gbAttachmentApi;
    @DubboReference
    GbCompanyRelationApi gbCompanyRelationApi;
    @DubboReference
    WfActHistoryApi wfActHistoryApi;

    @Autowired
    private FileService fileService;

    @DubboReference
    private TaskApi taskApi;

    @DubboReference
    FormApi formApi;

    @ApiOperation(value = "待办列表")
    @GetMapping("/queryTodoProcessPage")
    public Result<Page<WfTaskVO>> queryTodoProcessPage(@CurrentUser CurrentSjmsUserInfo userInfo,@Valid QueryTodoProcessPageForm form){
        QueryTodoProcessPageRequest request = new QueryTodoProcessPageRequest();
        PojoUtils.map(form,request);
        request.setBusinessKey(form.getGbNo());
        request.setCurrentUserCode(userInfo.getCurrentUserCode());
        Page<WfTaskDTO> wfTaskDTOPage = processApi.queryTodoProcessList(request);
        Page<WfTaskVO> taskVOPage = PojoUtils.map(wfTaskDTOPage,WfTaskVO.class);
        return Result.success(taskVOPage);
    }

    @ApiOperation(value = "已办列表")
    @GetMapping("/queryFinishedProcessPage")
    public Result<Page<WfTaskVO>> queryFinishedProcessPage(@CurrentUser CurrentSjmsUserInfo userInfo,@Valid QueryFinishedProcessPageForm form){
        QueryFinishedProcessPageRequest request = new QueryFinishedProcessPageRequest();
        PojoUtils.map(form,request);
        request.setBusinessKey(form.getGbNo());
        request.setCurrentUserCode(userInfo.getCurrentUserCode());
        Page<WfTaskDTO> wfTaskDTOPage = processApi.queryFinishedProcessList(request);
        Page<WfTaskVO> taskVOPage = PojoUtils.map(wfTaskDTOPage,WfTaskVO.class);
        return Result.success(taskVOPage);
    }

    @ApiOperation(value = "团购明细-旧接口只为兼容老数据保留")
    @GetMapping("/detail")
    public Result<FlowGbFormDetailVO> getGBSubmitFormDetailPage(@CurrentUser CurrentSjmsUserInfo userInfo,
                                                                @ApiParam(value = "任务ID", required = true) @RequestParam String taskId,
                                                                @ApiParam(value = "转发历史记录ID", required = false) @RequestParam(required = false) Long forwardHistoryId){
        GetTaskDetailRequest getTaskDetailRequest = new GetTaskDetailRequest();
        getTaskDetailRequest.setTaskId(taskId).setCurrentUserCode(userInfo.getCurrentUserCode());
        WfTaskDTO taskDTO = taskApi.getById(getTaskDetailRequest);
        Long id = taskDTO.getGbId();
        GbFormInfoDTO formDTO = gbFormApi.getOneById(id);
        FlowGbFormDetailVO result = PojoUtils.map(formDTO, FlowGbFormDetailVO.class);
        if(result != null){
            result.setCategory(taskDTO.getCategory());
            result.setNeedCheck(taskDTO.getNeedCheck());
            result.setIsFinished(taskDTO.getIsFinished());
            BaseInfoDTO baseInfoOne = gbBaseInfoApi.getOneByGbId(id);
            GbBaseInfoVO baseInfo = PojoUtils.map(baseInfoOne, GbBaseInfoVO.class);
            result.setBaseInfo(baseInfo);

            MainInfoDTO mainInfoOne = gbMainInfoApi.getOneByGbId(id);
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


            List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(id);

            if(CollectionUtil.isNotEmpty(attachmentList)){
                List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                for(AttachmentDTO one : attachmentList){
                    FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                    fileInfoVO.setFileKey(one.getFileKey());
                    fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_PROCESS_SUBMIT_SUPPORT_PICTURE));
                    fileInfoVO.setFileName(one.getFileName());
                    fileInfoList.add(fileInfoVO);
                }
                result.setFileInfoList(fileInfoList);
            }

            if (result.getIsFinished()) {
                if (forwardHistoryId != null && forwardHistoryId != 0L) {
                    boolean hasComment = wfActHistoryApi.hasComment(forwardHistoryId, userInfo.getCurrentUserId());
                    result.setShowCommentButtonFlag(!hasComment);
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
        return Result.success(result);
    }

    @ApiOperation(value = "团购明细-新接口")
    @GetMapping("groupBuy/detail")
    public Result<FlowGbFormDetailVO> getGroupBuyDetail(@CurrentUser CurrentSjmsUserInfo userInfo,
                                                                @ApiParam(value = "表单id", required = true) @RequestParam Long formId,
                                                                @ApiParam(value = "转发历史记录ID", required = false) @RequestParam(required = false) Long forwardHistoryId){
        WfTaskDTO todoTask = taskApi.getByFormId(formId, userInfo.getCurrentUserCode(),forwardHistoryId);
        WfTaskDTO taskDTO =null;
        if(Objects.nonNull(todoTask)){
            GetTaskDetailRequest getTaskDetailRequest = new GetTaskDetailRequest();
            getTaskDetailRequest.setTaskId(todoTask.getTaskId()).setCurrentUserCode(userInfo.getCurrentUserCode());
            taskDTO = taskApi.getById(getTaskDetailRequest);
        }

        Long id = formId;
        GbFormInfoDTO formDTO = gbFormApi.getOneById(formId);
        FlowGbFormDetailVO result = PojoUtils.map(formDTO, FlowGbFormDetailVO.class);
        if(result != null){
            result.setCategory(formDTO.getType().toString());
            if(Objects.nonNull(taskDTO) && !taskDTO.getTaskName().equals(FlowConstant.FORWARD_NODE)){
                result.setNeedCheck(taskDTO.getNeedCheck());
                result.setIsFinished(taskDTO.getIsFinished());
            }else{
                result.setNeedCheck(false);
                result.setIsFinished(true);
            }

            BaseInfoDTO baseInfoOne = gbBaseInfoApi.getOneByGbId(id);
            GbBaseInfoVO baseInfo = PojoUtils.map(baseInfoOne, GbBaseInfoVO.class);
            result.setBaseInfo(baseInfo);

            MainInfoDTO mainInfoOne = gbMainInfoApi.getOneByGbId(id);
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
            if (FormTypeEnum.GROUP_BUY_COST.getCode().equals(formDTO.getBizType())) {
                List<String> codes = new ArrayList<>();
                codes.add(formDTO.getSrcGbNo());
                List<FormDTO> formDTOS = formApi.listByCodes(codes);
                if (CollectionUtil.isNotEmpty(formDTOS)) {
                    FormDTO orangeForm = formDTOS.get(0);
                    List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(orangeForm.getId());
                    if (CollectionUtil.isNotEmpty(attachmentList)) {
                        List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                        for (AttachmentDTO one : attachmentList) {
                            FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                            fileInfoVO.setFileKey(one.getFileKey());
                            fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_PROCESS_SUBMIT_SUPPORT_PICTURE));
                            fileInfoVO.setFileName(one.getFileName());
                            fileInfoList.add(fileInfoVO);
                        }
                        result.setFileInfoList(fileInfoList);
                    }
                }
            } else {
                List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(id);

                if (CollectionUtil.isNotEmpty(attachmentList)) {
                    List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                    for (AttachmentDTO one : attachmentList) {
                        FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                        fileInfoVO.setFileKey(one.getFileKey());
                        fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_PROCESS_SUBMIT_SUPPORT_PICTURE));
                        fileInfoVO.setFileName(one.getFileName());
                        fileInfoList.add(fileInfoVO);
                    }
                    result.setFileInfoList(fileInfoList);
                }
            }

            if (result.getIsFinished()) {
                if (forwardHistoryId != null && forwardHistoryId != 0L) {
                    boolean hasComment = wfActHistoryApi.hasComment(forwardHistoryId, userInfo.getCurrentUserId());
                    result.setShowCommentButtonFlag(!hasComment);
                }else if(Objects.nonNull(taskDTO) && taskDTO.getTaskName().equals("转发")){
                    boolean hasComment = wfActHistoryApi.hasComment(Long.parseLong(todoTask.getExecutionId()), userInfo.getCurrentUserId());
                    result.setShowCommentButtonFlag(!hasComment);
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
        if(formDTO.getType().equals(FormTypeEnum.GROUP_BUY_COST.getCode())){
            result.setFeeApplicationReply(formDTO.getFeeApplicationReply());
            List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(id);
            if(CollectionUtil.isNotEmpty(attachmentList)){
                List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                for(AttachmentDTO one : attachmentList){
                    FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                    fileInfoVO.setFileKey(one.getFileKey());
                    fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_FEE_APPLICATION_UPLOAD_FILE));
                    fileInfoVO.setFileName(one.getFileName());
                    fileInfoList.add(fileInfoVO);
                }
                result.setFeeApplicationFileInfoList(fileInfoList);
            }
        }
        return Result.success(result);
    }
}